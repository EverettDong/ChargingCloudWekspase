package com.cpit.icp.collect.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.common.configurable._type;
import com.cpit.icp.collect.coderDecoder.common.configurable.type;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.controller.MsgReceived;
import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import com.cpit.icp.collect.msgProcess.ConstructMsgUtil;
import com.cpit.icp.collect.msgProcess.MsgSendThread;
import com.cpit.icp.collect.msgProcess.MsgThreadPool;
import com.cpit.icp.collect.msgProcess.UIMsgResMonitorThread;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.billing.recharge.RechargeBeginResponse;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
import com.cpit.icp.dto.collect.MonRechargeRecordReplyDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;

import static com.cpit.icp.collect.utils.Consts.CODE_MODE;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_1;
/**
 * 具体的处理流程，尽量复用
 * 
 * @author zhangqianqian
 *
 */
@Service
public class UnionChargeProcessImp {
	@Autowired
	GateRouteInfoMgmt gateRouteInfoMgmt;
	@Autowired
	ConstructMsgUtil constructMsgUtil;
	@Autowired
	MonDBService monDBService;
	@Autowired
	MsgSend msgSend;
	@Autowired CacheUtil cacheUtil;

	private final static Logger logger = LoggerFactory.getLogger(UnionChargeProcessImp.class);

	/**
	 * 下发具体的计费数据，构造报文，下发至前置网关。
	 * 
	 * @param dto
	 * @return
	 */
	public boolean sendCharingMsg(MonRechargeRecordReplyDto dto) {
		logger.info("sendCharingMsg begins. ");
		logger.info(dto.toString());

		if (dto.getDeviceNo() == null || dto.getDeviceNo().equals("")) {
			logger.error("deviceNo is null");
			return false;
		}
		String deviceNo = dto.getDeviceNo();
		GateRouteInfoDto preGateInfo = gateRouteInfoMgmt.getByDeviceNo(deviceNo);
		if (preGateInfo == null) {
			logger.error("preGateInfo  is null");
			return false;
		}
		logger.debug("get code");
		byte[] code=new byte[0];
		String version = preGateInfo.getDeviceVersion();
		String mesCode=null;
		if(version.equalsIgnoreCase(VERSION_3_1)) {
			mesCode="0x63";
			code = constructMsgUtil.gen63Codes(dto, preGateInfo.getDeviceVersion(), mesCode);
		}else {
			mesCode="0x68";
			 code = constructMsgUtil.gen68codes(dto, preGateInfo.getDeviceVersion(), mesCode);
		}
		
		if (code.length == 0) {
			logger.error("encode68/63  wrong and return");
			return false;
		}
		
		
		MsgOfPregateDto pregateDto = constructMsgUtil.constructDto(code, mesCode, deviceNo, preGateInfo);
		logger.info("constructDto success  ");

		String currentTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		monDBService.storeOriginalData(pregateDto, 1, 0, null, currentTime, null);
		logger.debug("st originalData store into db success");
		msgSend.msgSendToPreG(pregateDto, false);
		return true;

	}

	public boolean UIMsgProcess(List<String> deviceNoList, String msgCode, List<String> values,String sendTime) {
		logger.info("deviceNoList size is: " + deviceNoList.size());
		logger.info("UIMsgProcess param:"+msgCode+" "+sendTime+" "+values.toString());
		for (String deviceNo : deviceNoList) {
			if (!deviceNo.equalsIgnoreCase("")) {
				UIMsgSendToOneCP(deviceNo, msgCode, values,sendTime);
			} else {
				logger.info("deviceNo " + deviceNo + " is null.");
				continue;
			}

		}
		return true;
	}

	private void UIMsgSendToOneCP(String deviceNo, String msgCode, List<String> values,String sendTime) {
		try {
			GateRouteInfoDto pregateRouteInfo = gateRouteInfoMgmt.getByDeviceNo(deviceNo);
			if (pregateRouteInfo == null) {
				logger.info("getRouteInfo is null,deviceNo is " + deviceNo);
				return ;
			}
			String version = pregateRouteInfo.getDeviceVersion();
			String msgFlag= msgCode;
			if(version.equalsIgnoreCase(VERSION_3_5)&&msgCode.equalsIgnoreCase("0x46")) {
				msgFlag = "0x4A";
			}
			List<String> msgValues = new ArrayList<String>();
			
			if(msgCode.equalsIgnoreCase("0x46")) {
				
				msgValues.add(deviceNo);
				msgValues.addAll(values);
				
				
				logger.info("UImsg46 process values ok");
			}else {
				msgValues.addAll(values);
			}
			
			List<String> dataContent= new ArrayList<String>();
			List<String> msgNum = constructMsgUtil.genMsgNum(deviceNo);
			if(msgNum.isEmpty()||msgNum.size()==0) {
				logger.debug("genMsgNum failed.");
				dataContent.add("0");
				dataContent.add("0");	
			}else
			{
				dataContent.add(msgNum.get(0));
				dataContent.add(msgNum.get(1));
			}
			
			
			for(String s:msgValues) {
				dataContent.add(s);
			}
			logger.info("UISend msgCode "+msgCode +" values size: "+dataContent.size());
			byte[] codes = constructMsgUtil.genUISendMsgCodes(deviceNo, version, msgFlag, dataContent);
			MsgOfPregateDto pregateDto = constructMsgUtil.constructDto(codes, msgFlag, deviceNo, pregateRouteInfo);
			logger.info("constructDto success.");
			String currentTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
			monDBService.storeOriginalData(pregateDto, 1, 0, null, currentTime, null);
			
	//---set matchcode cache
			
			StringBuilder cacheKey= new StringBuilder();
			cacheKey.append(deviceNo);
			cacheKey.append("_");
			cacheKey.append(msgCode);
			cacheKey.append("_");
			cacheKey.append(CommFunction.byteArrayToHexStr(_type._int(dataContent.get(0), 2, "")));
			cacheKey.append(CommFunction.byteArrayToHexStr(_type._int(dataContent.get(1), 2, "")));
			String cachekeyStr= cacheKey.toString();
			String currenT = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
			logger.info("setMacheCode: "+cachekeyStr);
			cacheUtil.setMatchCode(cachekeyStr, sendTime);
			msgSend.msgSendToPreG(pregateDto, false);
			MsgThreadPool.getThreadPool().execute(new UIMsgResMonitorThread( cachekeyStr));
			
			
			
			
		} catch (Exception e) {
			logger.error("UIMsgSendToOneCP process error", e);
			
		}
	}

	
	

		
	
	
}
