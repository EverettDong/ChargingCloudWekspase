package com.cpit.icp.collect.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cpit.common.Encodes;
import com.cpit.common.SequenceId;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureEncode;
import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import com.cpit.icp.collect.msgProcess.CallHlhtInf;
import com.cpit.icp.collect.msgProcess.CallOuterInf;
import com.cpit.icp.collect.msgProcess.ConstructMsgUtil;

import com.cpit.icp.collect.msgProcess.MsgMap;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.billing.recharge.RechargeBeginResponse;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
import com.cpit.icp.dto.collect.MonChargingProcessDataDto;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.MonRechargingInfoDto;
import com.cpit.icp.dto.collect.coderDecoder.UIDataContentDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.common.ResultInfo;
import com.cpit.icp.dto.resource.BrBatterycharge;

import static com.cpit.icp.collect.utils.Consts.CODE_MODE;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_APP;
import static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_HLHT;
import static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_CARD;



@Service
public class AppChargingProcessImp {
	private final static Logger logger = LoggerFactory.getLogger(AppChargingProcessImp.class);
	@Autowired
	UnionChargeProcessImp chargeProcessImp;

	@Autowired
	ConfigureEncode configureEncode;
	@Autowired
	MsgSend msgSend;
	@Autowired
	ConstructMsgUtil constructMsgUtil;
	@Autowired
	GateRouteInfoMgmt gateRouteInfoMgmt;
	@Autowired
	MonDBService monDBService;
	@Autowired CallOuterInf callOuterInf;
	@Autowired CacheUtil cacheUtil;
	
/**
 * 
 * @param deviceNo
 * @param cardUserId 卡号
 * @param chargeMode 0x20/0x30/0x40
 * @param planChargeTime
 * @param planChargePower
 * @param password
 * @param src
 * return chargeOrderNo
 */
	public String beginToCharge(String deviceNo, String cardUserId, String chargeMode, String planChargeTime,
			String planChargePower, String password,String src) {
		logger.info("param: "+password+" "+chargeMode +" "+ planChargeTime +" "+ planChargePower+" "+src);
		logger.info("param: deviceNo "+deviceNo+" "+cardUserId);
		password = password.trim();
		String encodePassword =Encodes.encodeMD5(password);
		logger.info("encodedPass: "+encodePassword);
		boolean accountAuth = callOuterInf.accountAuth(cardUserId, encodePassword);
		if(!accountAuth) {
			logger.info("accountAuth failed and return");
			return  null;
		}
		logger.info("accountAuth success");
		String msgCode6c="0x6C";
		GateRouteInfoDto preGateInfo = gateRouteInfoMgmt.getByDeviceNo(deviceNo);
		if (preGateInfo == null) {
			logger.info("preGateInfo  is null");
			return null;
		}

		RechargeBeginResponse accountInfo = callOuterInf.getAccountInfo(cardUserId);
		if(accountInfo==null) {
			logger.info("get AccountInfo is null and return");
			return null;
		}
	logger.info("accountInfo: "+accountInfo.toString());
		if(src.equalsIgnoreCase(CHARGE_SRC_APP))
		{
			//Map<String, String> chargePoleInUse = MsgMap.getInstance().getChargePoleInAppUse();
			cacheUtil.setAppCardDeviceNo(deviceNo, cardUserId);
			cacheUtil.setAppRelatedCard(deviceNo, cardUserId);
			logger.info("cardUserid,deviceNo "+ cardUserId+" "+deviceNo);
		}else if(src.equalsIgnoreCase(CHARGE_SRC_HLHT)) {
			Map<String, String> chargePoleInUse = MsgMap.getInstance().getChargePoleInHLHTUse();
			chargePoleInUse.put(accountInfo.getCenterTransactionSerialNumber(), deviceNo);
		}else {
			logger.info("src is wrong.");
		}
		
		logger.info("set chargeSrc to redis");
		setChargeSrc(deviceNo,src);
		logger.info("getUserAccoutInfo success");
		byte[] code =getEncodeData6C(accountInfo,msgCode6c,preGateInfo.getDeviceVersion(),deviceNo,CODE_MODE,chargeMode,planChargeTime,planChargePower);
		logger.info("encodePackageContent success");
	if(code.length ==0) {
		logger.info("encode6C wrong and return");
		return null;
	}
		MsgOfPregateDto dto = constructMsgUtil.constructDto(code, msgCode6c, deviceNo, preGateInfo);
		logger.info("constructDto success  ");
		
		String currentTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		monDBService.storeOriginalData(dto, 1, 0, null,currentTime,null);
		logger.info("st originalData store into db success");
		logger.info("up redis .");
		cacheUtil.upCPStartChargeTime(deviceNo);
		
		msgSend.msgSendToPreG(dto, false);
	String chargeOrderNo=	accountInfo.getCenterTransactionSerialNumber();
	logger.info("chargeOrderNo" + chargeOrderNo);
	
		return chargeOrderNo;

	}
	/**
	 * 
	 * @param accountInfo 计费返回的数据
	 * @param msgCode 报文编码 0x6c
	 * @param version 设备版本 3.4
	 * @param deviceNo 设备编码
	 * @param codeMode 编码模式 GB2312
	 * @param chargeMode 充电模式 十六进制的0x20/0x30/0x40
	 * @param planChargeTime 计划充电时间
	 * @param planChargePower 计划充电度数
	 * @return
	 */
	public byte[] getEncodeData6C(RechargeBeginResponse accountInfo,String msgCode,String version,String deviceNo,String codeMode,String chargeMode,String planChargeTime,
			String planChargePower) {
    logger.info("begin to encode6C");
		List<String> dataContent = new ArrayList<String>();
		byte[] code= new byte[0];
		if(version.equalsIgnoreCase(VERSION_3_4)) {
			int flowCode = SequenceId.getInstance().getId("collectMessFlowCode");
			//string 字符串的顺序按照3.4规范协议
			//List<String> userId = constructMsgUtil.genUserIdSerialNoString(flowCode);
			List<String> msgNum = constructMsgUtil.genMsgNum(deviceNo);
			if(msgNum.isEmpty()||msgNum.size()==0) {
				logger.info("genMsgNum failed.");
				dataContent.add("0");
				dataContent.add("0");	
			}else
			{
				dataContent.add(msgNum.get(0));
				dataContent.add(msgNum.get(1));
			}
			
			dataContent.add(accountInfo.getCardNumber());
			dataContent.add(accountInfo.getProcessSerialNumber());
			dataContent.add(accountInfo.getCustomerNumber());//车牌号
			dataContent.add(accountInfo.getElectricityMainAccountBalance());
			dataContent.add(accountInfo.getElectricityMainAccountAvailableBalance());
			dataContent.add(accountInfo.getServiceChargeAccountBalance());
			dataContent.add(accountInfo.getServerAccountAvailableBalance());
			dataContent.add(accountInfo.getCenterTransactionSerialNumber());
			dataContent.add(accountInfo.getBossDocumentsSerialNumber());
			String currentT = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
			dataContent.add(currentT);
			dataContent.add("01");//充电端口 3.4
			dataContent.add(chargeMode);//chargeMode，0x20,0x30,0x40
			dataContent.add(planChargeTime);
			dataContent.add(planChargePower);
			dataContent.add("00000");
			
			try {
				code = configureEncode.ParseData(dataContent, msgCode,VERSION_3_4, deviceNo, CODE_MODE);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				logger.error("enocode 6c error "+e.getMessage());
			}
		}else {
			//3.5 充电端口需要从接口获取，暂不支持。
			logger.info("3.5 not availiable now.");
			
		}
		
		
		
		
		return code;
	}
	/**
	 * userd for app&hlht
	 * @param srcType CHARGINT_SRC_APP/CHARGINT_SRC_HLHT
	 * @param relatedNo if(CHARGINT_SRC_APP) cardUserId,if(CHARGINT_SRC_HLHT) chargeOrderNo
	
	 */
	public void endCharge(String srcType,String relatedNo) {
		
	logger.info("endCharge begins."+srcType+" "+relatedNo);
	String deviceNo = null;
	Map<String, String> chargePoleInUse = null;
		if(srcType.equalsIgnoreCase(CHARGE_SRC_APP)) {
			
			 deviceNo = cacheUtil.getAppCardDeviceNo(relatedNo);
		}
		if(srcType.equalsIgnoreCase(CHARGE_SRC_HLHT)) {
			 chargePoleInUse = MsgMap.getInstance().getChargePoleInHLHTUse();
			 deviceNo = chargePoleInUse.get(relatedNo);
		}
		if(deviceNo==null||deviceNo.equalsIgnoreCase("")) {
			logger.info("device is not is charing..");
			return;
		}
		logger.info("deviceNo is  "+deviceNo);
		// contruct 05
	
		GateRouteInfoDto preGateInfo = gateRouteInfoMgmt.getByDeviceNo(deviceNo);
		if (preGateInfo == null) {
			logger.info("preGateInfo  is null");
			return;
		}
		byte[] code = constructMsgUtil.stopCharing(deviceNo,preGateInfo.getDeviceVersion());
		if(code.length ==0) {
			logger.info("encode 05 wrong and return");
			return;
		}
		String msgCode05="0x05";
		MsgOfPregateDto dto = constructMsgUtil.constructDto(code, msgCode05, deviceNo, preGateInfo);
		logger.info("constructDto success  ");
		
		//chargePoleInUse.remove(relatedNo);
		String currentTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		monDBService.storeOriginalData(dto, 1, 0, null,currentTime,null);
		logger.info("st originalData store into db success");
		msgSend.msgSendToPreG(dto, false);
		cacheUtil.delCPStartChargeTime(deviceNo);
		
	}
	
	
	public String unlockChargePole(String chargeOrderNo) {
		String s=null;
		int len = chargeOrderNo.length();
		if(len<6) {
			s = chargeOrderNo;
		}else {
			int pos = len-6;
			s = chargeOrderNo.substring(pos, len);
		}
		return s;
	}
	
	public MonChargingProcessDataDto queryChargingProcData(String deviceNo) {
		logger.info("queryChargingProcData"+deviceNo);
	
	
		
		MonChargingProcessDataDto dto =cacheUtil.getCPProcessData(deviceNo);
	
		return dto;
	}
	

	
	
	


	
	
private void setChargeSrc(String deviceNo,String src) {
	if(src.equalsIgnoreCase(CHARGE_SRC_APP)) {
		cacheUtil.setChargeSrc(deviceNo,CHARGE_SRC_APP);
	}else if(src.equalsIgnoreCase(CHARGE_SRC_HLHT)) {
		cacheUtil.setChargeSrc(deviceNo,CHARGE_SRC_HLHT);
	}else {
		cacheUtil.setChargeSrc(deviceNo,CHARGE_SRC_CARD);
	}
}

public List<MonRechargeRecordDto> queryChargePoleKwh(List<String> deviceNoList,String startTime,String endTime){
	logger.info("queryChargePoleKwh");
	List<MonRechargeRecordDto> list =monDBService.getSumKwhByDeviceNo(deviceNoList, startTime, endTime);
	
	return list;
}



}
