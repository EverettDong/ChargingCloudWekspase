package com.cpit.icp.collect.msgProcess;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cpit.common.SequenceId;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureEncode;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import com.cpit.icp.collect.service.UnionChargeProcessImp;
import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.collect.utils.cache.DeviceCacheUtil;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
import com.cpit.icp.dto.collect.MonRechargeRecordReplyDto;
import com.cpit.icp.dto.collect.MonSequenceDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;

import static com.cpit.icp.collect.utils.Consts.CODE_MODE;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_1;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_2;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.List;
import java.util.Map;

@Service
public class ConstructMsgUtil {
	private final static Logger logger = LoggerFactory.getLogger(ConstructMsgUtil.class);
	private static int msgNum=1;//报文编号
	private static int msgSequenceNum =1;//指令序号
@Autowired ConfigureEncode configureEncode;
@Autowired GateRouteInfoMgmt  gateRouteInfoMgmt;
@Autowired
MonDBService monDBService;
@Autowired CacheUtil cacheUtil;
@Autowired DeviceCacheUtil deviceCacheUtil;
/**
 * 生成用戶id和指令序号
 * 数组0存放的是高字节， 数组3存放的是低字节
 * @return

 */
public List<byte[]> genUserIdSerialNo(int sequenceId) {
	
	byte[] bs = new byte[2];
	byte[] bs1 = new byte[2];
	for (int i = 0; i < 2; i++) {
		bs[i] = (byte) (sequenceId >>> ((3 - i) * 8) & 0xFF);
		
		
		bs1[i] = (byte) (sequenceId >>> ((1 - i) * 8) & 0xFF);
	}
	List<byte[]> res = new ArrayList<byte[]>();
	res.add(bs);
	res.add(bs1);
	return res;
}
public List<String> genUserIdSerialNoString(int sequenceId){
	byte[] bs = new byte[2];
	byte[] bs1 = new byte[2];
	for (int i = 0; i < 2; i++) {
		bs[i] = (byte) (sequenceId >>> ((3 - i) * 8) & 0xFF);
		
		
		bs1[i] = (byte) (sequenceId >>> ((1 - i) * 8) & 0xFF);
	}
	List<String> res = new ArrayList<String>();
	
	res.add(String.valueOf(CommFunction.byteArrayToInt(bs)));
	res.add(String.valueOf(CommFunction.byteArrayToInt(bs1)));
	return res;
}

public byte[] constructData(byte msgCode,String deviceNo) {
	switch(msgCode) {
	case 0x05:
		return construct05Stop(deviceNo);
	}
	return null;
}

private byte[] construct05Stop(String devideNo) {
	String modeNums = "1";
	String sequenceName ="MonUserIdSerical";
	int sequenceId = SequenceId.getInstance().getId(sequenceName);
	
	
	return null;
}
/**
 * 3.4----0x80
 * 3.5----0x35
 * @param ver
 * @return
 * 版本号转换至前置网关格式
 */
public String versionConvertToP(String ver) {
	if(ver.equalsIgnoreCase(VERSION_3_4)) {
		return "0x80";
	}else if(ver.equalsIgnoreCase(VERSION_3_5)) {
		return "0x35";
	}else {
		return null;
	}
	
}
public  String versionConvertToS(String str) {
	if(str.equalsIgnoreCase("0x80")) {
		return VERSION_3_4;
	}else if(str.equalsIgnoreCase("0x35")) {
		return VERSION_3_5;
	}else {
		return null;
	}
		
		
		
}
/**
 * 构造下发的dto
 * @param data
 * @return
 */
public MsgOfPregateDto constructDto(byte[] data,String msgCode,String deviceNo,GateRouteInfoDto preGateInfo) {
	logger.info("MsgOfPregateDto construct"+CodeTransfer.byteArrayToHexStr(data));
	
	
	MsgOfPregateDto dto = new MsgOfPregateDto();
	dto.setPreGateIp(preGateInfo.getPreGateIp());
	dto.setPreGatePort(preGateInfo.getPreGatePort());
	dto.setDeviceNo(deviceNo);
	dto.setMsgVersion(versionConvertToP(preGateInfo.getDeviceVersion()));
	dto.setMsgData(CodeTransfer.byteArrayToHexStr(data));
	dto.setMsgCode(msgCode);
	logger.debug("MsgOfPregateDto construct success");
	return dto;
}


/**
 * 生成05报文，
 * @param deviceNo 设备编码.
 * @return
 */
public byte[] stopCharing(String deviceNo,String version) {
	byte[] code = new byte[0];
	int moduleNum = monDBService.getDeviceModuleNums(deviceNo);
	logger.info("get moduleNum success.");
	List<String> dataContent = new ArrayList<String>();
	
	List<String> msgNum = this.genMsgNum(deviceNo);
	if(msgNum.isEmpty()||msgNum.size()==0) {
		logger.debug("genMsgNum failed.");
		dataContent.add("0");
		dataContent.add("0");	
	}else
	{
		dataContent.add(msgNum.get(0));
		dataContent.add(msgNum.get(1));
	}

	dataContent.add("2");
	dataContent.add(String.valueOf(moduleNum));
	for(int i=0;i<moduleNum;i++) {
		dataContent.add(String.valueOf(i+1));//从1开始
		dataContent.add("2");
	}
	
	try {
		
		code = configureEncode.ParseData(dataContent, "0x05",version, deviceNo, CODE_MODE);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		logger.error("enocode 05 error "+e.getMessage());
	}
	return code;
}
/**
 * send 05
 * @param dto
 */
public MsgOfPregateDto stopCharging(MsgOfPregateDto dto) {
	logger.info("stop charing,send 0x05");
	byte[] code= stopCharing(dto.getDeviceNo(),dto.getMsgVersion());
	 
	 if(code.length ==0) {
			logger.info("encode 05 wrong and return");
			return null;
		}
		GateRouteInfoDto preGateInfo = gateRouteInfoMgmt.getByDeviceNo(dto.getDeviceNo());
		if (preGateInfo == null) {
			logger.info("preGateInfo  is null");
			return null;
		}
		String msgCode05="0x05";
		MsgOfPregateDto sendDto = constructDto(code, msgCode05, dto.getDeviceNo(), preGateInfo);
		logger.debug("constructDto success  ");
		
		String currentTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		monDBService.storeOriginalData(sendDto, 1, 0, "",currentTime,"");
		logger.debug("st originalData store into db success");
		//msgSend.msgSendToPreG(sendDto, false);
		return sendDto;
}
/**
 * 生成报文编号 0001,返回两位的16进制数，最大的是65535
 */
private String genMsgNum(int msgNum ) {
	logger.debug("genMsgNum"+msgNum);
	
	
	StringBuilder sb = new StringBuilder();
	String hexNum = Integer.toHexString(msgNum);
	
	String hexStr = CommFunction.padLeft(hexNum, 4, "0");
	
	return hexStr;
}
/**
 * 生成指令序号，返回两位的16进制，
 * @return
 */
private String genMsgSequenceNum(int msgSequenceNum ) {
logger.debug("genMsgSequenceNum"+msgNum);
	
	
	StringBuilder sb = new StringBuilder();
	String hexNum = Integer.toHexString(msgSequenceNum);
	
	String hexStr = CommFunction.padLeft(hexNum, 4, "0");

	return hexStr;
}

public List<String> genMsgNum(String deviceNo) {
	
	MonSequenceDto seqDto = cacheUtil.getSequence(deviceNo);
	logger.info("sequenceData: "+seqDto.toString());
	int msgNum = seqDto.getFlowNum();
	int sequenceNum = seqDto.getSequenceNum();
	
	List<String> res = new ArrayList<String>();
	res.add(String.valueOf(msgNum));
	res.add(String.valueOf(sequenceNum));
	
	return res;
}


public byte[] gen63Codes(MonRechargeRecordReplyDto dto,String version,String msgCode) {
	logger.info("gen33codes param: "+dto.toString()+" version: "+version);
	List<String> dataContent = new ArrayList<String>();
	String defaultBalance="0";
	byte[] code= new byte[0];
	String serialNo = dto.getSerialNo();
	dataContent.add(dto.getUserId());
	dataContent.add(serialNo);
	
	dataContent.add(dto.getChargeType());
	dataContent.add(dto.getCardId());
	dataContent.add(dto.getCustomerId());
	//dataContent.add(dto.getChargeTimeAndElec());
	dataContent.add(defaultBalance);
	dataContent.add(defaultBalance);
	dataContent.add(dto.getServiceFee());
	dataContent.add(defaultBalance);
	dataContent.add(dto.getPlatTransFlowNum());
	
	dataContent.add(dto.getBossFlowNum());
	dataContent.add(dto.getTransTime());
	if(version.equalsIgnoreCase(VERSION_3_1)) {
		
		try {
			code = configureEncode.ParseData(dataContent, msgCode,VERSION_3_1, dto.getDeviceNo(), CODE_MODE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("enocode 68 error "+e.getMessage());
		}
	}

	
	
	return code;
}

public byte[] gen68codes(MonRechargeRecordReplyDto dto,String version,String msgCode) {
	logger.info("gen68codes param: "+dto.toString()+" version: "+version);
	List<String> dataContent = new ArrayList<String>();
	byte[] code= new byte[0];
	String serialNo = dto.getSerialNo();
	if(version.equalsIgnoreCase(VERSION_3_5)) {

		 serialNo = this.processSerialNo(dto.getSerialNo());
	}
	
	logger.info("gen68codes serialNo: "+serialNo);
	dataContent.add(dto.getUserId());
	dataContent.add(serialNo);
	dataContent.add(dto.getChargeType());
	dataContent.add(dto.getCardId());
	dataContent.add(dto.getCustomerId());
	dataContent.add(dto.getChargeTimeAndElec());
	dataContent.add(dto.getElecFee());
	dataContent.add(dto.getServiceFee());
	dataContent.add(dto.getTotalAmount());
	dataContent.add(dto.getPlatTransFlowNum());
	dataContent.add(dto.getBossFlowNum());
	dataContent.add(dto.getTransTime());
	if(version.equalsIgnoreCase(VERSION_3_4)||version.equalsIgnoreCase(VERSION_3_2)) {
		
		try {
			code = configureEncode.ParseData(dataContent, msgCode,VERSION_3_4, dto.getDeviceNo(), CODE_MODE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("enocode 68 error "+e.getMessage());
		}
	}
	if(version.equalsIgnoreCase(VERSION_3_5)) {
		
		try {
			String deviceNo = dto.getDeviceNo();
			String codeMode = deviceCacheUtil.getDeviceCodeMode(deviceNo);
			if(codeMode==null) {
				logger.error("codeMode is null.");
				codeMode=CODE_MODE;
			}
			logger.info("encode 68,deviceNo is: "+deviceNo+" codeMode is: "+codeMode);
			code = configureEncode.ParseData(dataContent, msgCode,VERSION_3_5, dto.getDeviceNo(), codeMode);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("enocode 68 error "+e.getMessage());
		}
	}
	
	
	
	return code;
}

public String processSerialNo(String serialNo) {
	
	logger.info("processSerialNo: "+serialNo);
	int i= Integer.parseInt(serialNo);
	if(i<32768) {
		logger.error("serialNo < 32768");
		i=32768;
	}
	i=i-32768;
	
	return String.valueOf(i);
}
public byte[] genUISendMsgCodes(String deviceNo,String version,String msgCode,List<String> values) {
	logger.info("genUISendMsgCodes");
	logger.info("deviceNo: "+deviceNo+" version: "+version+" msgCode: "+msgCode+" valueSize: "+values.size());
	
	byte[] codes= new byte[0];

		try {
			codes = configureEncode.ParseData(values, msgCode,version, deviceNo, CODE_MODE);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("encode error,msgCode is "+msgCode,e);
		} 
	

	return codes;
 
}


}
