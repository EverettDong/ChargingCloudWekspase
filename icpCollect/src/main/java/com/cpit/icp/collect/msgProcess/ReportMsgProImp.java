package com.cpit.icp.collect.msgProcess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.cpit.common.Encodes;
import com.cpit.common.SpringContextHolder;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.common.configurable.type;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureDecode;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
import com.cpit.icp.dto.collect.MonChargingProcessDataDto;
import com.cpit.icp.dto.collect.MonChargingStatus4App;
import com.cpit.icp.dto.collect.MonFaultRecordDto;
import com.cpit.icp.dto.collect.MonOverallStateDto;
import com.cpit.icp.dto.collect.MonRechargingStatusInfoParseDto;
import com.cpit.icp.dto.collect.coderDecoder.DecodedMsgDto;
import com.cpit.icp.dto.collect.coderDecoder.FormatTestResultDto;
import com.cpit.icp.dto.collect.coderDecoder.PackageDto;
import com.cpit.icp.dto.collect.coderDecoder.UIDataContentDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.collect.uiSend.UISendMsgResDto;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.exception.ReceiveDataException;
import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.service.MsgSend;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.MsgCodeUtil;
import com.cpit.icp.collect.utils.ParseUtil;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.collect.utils.cache.DeviceCacheUtil;
import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import  com.cpit.icp.collect.impl.OriginalDataStoreMgmt;
import com.cpit.icp.collect.rabbitConfig.RabbitMsgSender;

import static com.cpit.icp.collect.utils.Consts.CODE_MODE;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_2;
import static com.cpit.icp.collect.utils.Consts.TERM_DEL_FLAG;

/**
 * 
 *
 * 
 * @author zhangqianqian
 *
 */
public class ReportMsgProImp implements MsgProcessInterface {
	private final static Logger logger = LoggerFactory.getLogger(ReportMsgProImp.class);
//obj--begin
	private ConfigureDecode configureDecode;

	private MonDBService monDBService;
	private MsgCodeUtil msgCodeUtil;
	
	private CacheUtil cacheUtil;
	private DeviceCacheUtil deviceCacheUtil;
	private ConstructMsgUtil constructMsgUtil;
	
	private MsgSend msgSend;
	private ParseUtil parseUtil;
	
	  private RabbitMsgSender rabbitMsgSender;
	
	
	//obj--end
	
	// variable--begin
	private String FAULT_LEVEL="故障等级:";
	private String FAULT_DEF="故障定义:";
	private String END_SEMICOLON=";";
	private String SPLIT_COLON=":";
	private String receivedTime;
	
	private String cp_currentElec;//a相电流 0x34
	
	private String cp_voltage;//A相电压 0x34
	private String cp_soc;//电池剩余电量
	private String cp_startTime;//开始充电时间
	private String cp_receivedTime;//本次采样时间
	private String cp_totalPower;//累计充电量
	private String cp_currentSoc;//当前soc
	private String cp_gunConnStatus;//充电枪推入是否到位 0，不到位：1，到位
	// variable--end
	

	{
		configureDecode = SpringContextHolder.getBean(ConfigureDecode.class);
	
		monDBService = SpringContextHolder.getBean(MonDBService.class);
	//	msgDataConvertor = SpringContextHolder.getBean(MsgDataConvertor.class);
		msgCodeUtil = SpringContextHolder.getBean(MsgCodeUtil.class);
		cacheUtil = SpringContextHolder.getBean(CacheUtil.class);
		constructMsgUtil = SpringContextHolder.getBean(ConstructMsgUtil.class);
	//	gateRouteInfoMgmt = SpringContextHolder.getBean(GateRouteInfoMgmt.class);
		msgSend =  SpringContextHolder.getBean(MsgSend.class);
		cacheUtil =  SpringContextHolder.getBean(CacheUtil.class);
		parseUtil =  SpringContextHolder.getBean(ParseUtil.class);
		rabbitMsgSender = SpringContextHolder.getBean(RabbitMsgSender.class);
		deviceCacheUtil =  SpringContextHolder.getBean(DeviceCacheUtil.class);
	}

	private void doMsgProcessN(MsgOfPregateDto dto) {
		logger.info("reportData doMsgProcess : ");
		logger.debug("msgData: "+dto.toString());
		String codeMode= null;
		if(dto.getMsgVersion().equalsIgnoreCase(VERSION_3_5)) {
			codeMode = deviceCacheUtil.getDeviceCodeMode(dto.getDeviceNo());
			if(codeMode==null) {
				logger.error("codeMode is null.");
				codeMode=CODE_MODE;
			}
		}else {
			codeMode=CODE_MODE;
		}
		logger.info("deviceNo: "+dto.getDeviceNo()+" "+dto.getMsgVersion()+ "codemode : "+codeMode);
		String[] decodedData= null;
		 receivedTime=TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		try {
			DecodedMsgDto decodedMsgDto =new DecodedMsgDto();
			decodedMsgDto = configureDecode.DecodeReportPackageContent(Encodes.decodeHex(dto.getMsgData()), null, 0, 3, codeMode,dto.getDeviceNo(),dto.getMsgVersion());
		decodedData = decodedMsgDto.getDecodedMsg();
		
		} catch (ReceiveDataException e) {
			// TODO Auto-generated catch block
			logger.error("decodeReportPackage wrong ",e);
			return ;
		}
		if(decodedData==null) {
			logger.error("configureDecode is wrong.");
			return;
		}
		
		storeOriginalData(dto, decodedData);
		if(!decodedData[1].equals(null) ||!decodedData[1].equals("")) {
			
			parseToSpecialObj(dto,decodedData[1]);
			getProcessData(receivedTime,dto,decodedData[1]);
			updateCPStatus(dto,decodedData[1]);
			checkFault(dto,receivedTime,decodedData[1]);
			checkCache(dto,decodedData[1]);
		
			
		}else{
			
			logger.info("decode data is null, return.");
			return;
		}
	
		
		//	
		
			
			
			
		}
	
	
	/**
	 * 从报文中解析出过程数据，设置到缓存中。
	 * 
	 * @param dto
	 * @param parsedMsgData
	 */
	private void getProcessData(String receivedTime,MsgOfPregateDto dto,String parsedMsgData) {
		logger.info("getProcessData begins.");
		String msgCode = dto.getMsgCode();
		if(msgCode.equalsIgnoreCase("0x34")&&dto.getMsgVersion().equalsIgnoreCase(VERSION_3_4)) {
			//this.cp_currentA;			this.cp_voltageA
			logger.info("0x34/3.4v");
			String nameV = "充电电压:";
			String nameElec = "充电电流:";
			String nameStatus="执行机构状态:";
			String statusStr= parseUtil.getTeamStr(nameStatus, parsedMsgData);
			
			String statusHexStr= parseUtil.getMiddleStr(statusStr, SPLIT_COLON, SPLIT_COLON);
			byte[] b=CommFunction.hexStringToBytes(statusHexStr);
			String str=type._bit(b, "");
			char[] chars = str.toCharArray();
			String status="0";
			if(chars[6]=='1'){
				status="1";
			}
			this.cp_gunConnStatus = status;
			String vTeamStr = parseUtil.getTeamStr(nameV, parsedMsgData);
			String ElecTeamStr = parseUtil.getTeamStr(nameElec, parsedMsgData);
			this.cp_currentElec = parseUtil.getMeaningStr(nameElec, ElecTeamStr);
			this.cp_voltage = parseUtil.getMeaningStr(nameV, vTeamStr);
			this.cp_receivedTime = this.receivedTime;
			upCPProcessData(dto.getDeviceNo());
			logger.info("0x34/3.4v ends.");
		}
		if(msgCode.equalsIgnoreCase("0x39")&&dto.getMsgVersion().equalsIgnoreCase(VERSION_3_4)) {
			logger.info("0x39/3.4v");
			String nameKwh="本次累计充电Kwh:";
			String nameTime="本次累计充电时间:";
			String kwhStr = parseUtil.getTeamStr(nameKwh, parsedMsgData);
			String timeStr = parseUtil.getTeamStr(nameTime, parsedMsgData);
			
			String chargingTime = parseUtil.getMeaningStr(nameTime, timeStr);
			Long startTime=TimeConvertor.getCurrentTime();
			if(!chargingTime.equalsIgnoreCase("")) {
				 startTime=TimeConvertor.getCurrentTime()-Long.parseLong(chargingTime)*1000;	
			}
			
			this.cp_startTime = TimeConvertor.longtime2String(startTime, TimeConvertor.FORMAT_MINUS_24HOUR);
			this.cp_totalPower = parseUtil.getMeaningStr(nameKwh, kwhStr);
			this.cp_receivedTime = this.receivedTime;
			upCPProcessData(dto.getDeviceNo());
			logger.info("0x39/3.4v ends");
		}
		if(msgCode.equalsIgnoreCase("0x3A")&&dto.getMsgVersion().equalsIgnoreCase(VERSION_3_4)) {
			logger.info("0x3A/3.4v");
			String soc1="SOC1:";
			String socStr = parseUtil.getTeamStr(soc1, parsedMsgData);
			this.cp_soc = parseUtil.getMeaningStr(soc1,socStr);
			this.cp_currentSoc = cp_soc;
			this.cp_receivedTime = this.receivedTime;
			upCPProcessData(dto.getDeviceNo());
			logger.info("0x3A/3.4v ends.");
		}
		if(msgCode.equalsIgnoreCase("0x3C")&&dto.getMsgVersion().equalsIgnoreCase(VERSION_3_5)) {
			//this.cp_currentA;			this.cp_voltageA
			logger.info("0x3c/3.5v");
			String gunPos="充电枪位置";
			String gunAmount="充电枪数量:";
			String gunTeamStr = parseUtil.getTeamStr(gunAmount, parsedMsgData);
			String gunAmountS = parseUtil.getMeaningStr(gunAmount, gunTeamStr);
			int gunAmt=0;
			if(!gunAmountS.equalsIgnoreCase("")) {
				 gunAmt = Integer.parseInt(gunAmountS);
			}
			for(int i=1;i<gunAmt+1;i++) {
				String nameV = gunPos+i+"充/放电电压:";
				String nameElec = gunPos+i+ "充/放电电流:";
				String vTeamStr = parseUtil.getTeamStr(nameV, parsedMsgData);
				String ElecTeamStr = parseUtil.getTeamStr(nameElec, parsedMsgData);
				this.cp_currentElec = parseUtil.getMeaningStr(nameElec, ElecTeamStr);
				this.cp_voltage = parseUtil.getMeaningStr(nameV, vTeamStr);
				this.cp_receivedTime = this.receivedTime;
				String key=dto.getDeviceNo()+"_"+i;
				logger.debug(key);
				upCPProcessData(key);
			}
		logger.info("0x3c/3.5 ends");
		}
		if(msgCode.equalsIgnoreCase("0x39")&&dto.getMsgVersion().equalsIgnoreCase(VERSION_3_5)) {
			logger.info("0x39/3.5v begins.");
			String gunPos="充电枪位置";
			String gunAmount="充电枪数量:";
			String gunTeamStr = parseUtil.getTeamStr(gunAmount, parsedMsgData);
			String gunAmountS = parseUtil.getMeaningStr(gunAmount, gunTeamStr);
			int gunAmt=0;
			if(!gunAmountS.equalsIgnoreCase("")) {
				 gunAmt = Integer.parseInt(gunAmountS);
			}
			
			for(int i=1;i<gunAmt+1;i++) {
				logger.debug("for "+i);
			String nameKwh=gunPos+i+"本次累计充/放电Kwh:";
			String nameTime=gunPos+i+"本次累计充/放电时间:";
			String kwhStr = parseUtil.getTeamStr(nameKwh, parsedMsgData);
			String timeStr = parseUtil.getTeamStr(nameTime, parsedMsgData);
			
			String chargingTime = parseUtil.getMeaningStr(nameTime, timeStr);
			Long startTime=TimeConvertor.getCurrentTime();
			if(!chargingTime.equalsIgnoreCase("")&&!chargingTime.equalsIgnoreCase(null)) {
				 startTime=TimeConvertor.getCurrentTime()-Long.parseLong(chargingTime)*1000;	
			}
			
			this.cp_startTime = TimeConvertor.longtime2String(startTime, TimeConvertor.FORMAT_MINUS_24HOUR);
			this.cp_totalPower = parseUtil.getMeaningStr(nameKwh, kwhStr);
			this.cp_receivedTime = this.receivedTime;
			logger.debug("begin upProcessData.");
			upCPProcessData(dto.getDeviceNo()+"_"+i);
			}
		logger.info("0x39/3.5v ends.");
		}
		if(msgCode.equalsIgnoreCase("0x3A")&&dto.getMsgVersion().equalsIgnoreCase(VERSION_3_5)) {
			logger.info("0x3A/3.5v");
			String gunPos="充电枪位置";
			String gunAmount="充电枪数量:";
			String gunTeamStr = parseUtil.getTeamStr(gunAmount, parsedMsgData);
			String gunAmountS = parseUtil.getMeaningStr(gunAmount, gunTeamStr);
			int gunAmt=0;
			if(!gunAmountS.equalsIgnoreCase("")) {
				 gunAmt = Integer.parseInt(gunAmountS);
			}
			for(int i=1;i<gunAmt+1;i++) {
				String soc1="SOC"+i;
				String socStr = parseUtil.getTeamStr(soc1, parsedMsgData);
				this.cp_soc = parseUtil.getTeamStr(soc1, socStr);
				this.cp_receivedTime = this.receivedTime;
				upCPProcessData(dto.getDeviceNo()+"_"+i);
			}
			
			logger.info("0x3A/3.5v ends.");
		}
		
	}


	
		/**
		 * 更新缓存中的状态
		 * @param dto
		 * @param parsedMsgData
		 */
		private void updateCPStatus(MsgOfPregateDto dto,String parsedMsgData) {
			logger.info("checkStatus begins:");
			String msgCode = dto.getMsgCode();
			String deviceNo = dto.getDeviceNo();
			String statusStr=null;//statusStr / name:hexValue:meaning
			
			if(msgCode.equalsIgnoreCase("0x31")&& dto.getMsgVersion().equalsIgnoreCase(VERSION_3_4)) {
				logger.info("get 3.4-0x31 data.");
				String beginStr ="充电系统总体状态:";
				String endStr = "充电系统故障状态:";
				//statusStr =this.getSubStr(beginStr, endStr, parsedMsgData);
				statusStr = parseUtil.getTeamStr(beginStr, parsedMsgData);
				logger.debug("3.4 statusStr: "+statusStr);
				
			}
			if(msgCode.equalsIgnoreCase("0x31")&& dto.getMsgVersion().equalsIgnoreCase(VERSION_3_5)) {
				logger.info("get 3.5-0x31 data.");
				String beginStr ="工作状态:";
				String endStr = "可用充电机模块数量:";
				statusStr = parseUtil.getTeamStr(beginStr, parsedMsgData);
				//statusStr =this.getSubStr(beginStr, endStr, parsedMsgData);
				logger.debug("3.5 statusStr: "+statusStr);
				
			}
			if(msgCode.equalsIgnoreCase("0x11")&& dto.getMsgVersion().equalsIgnoreCase(VERSION_3_4)) {
				String workStatus =parseUtil.getTeamStr("工作状态:", parsedMsgData);
				String workStHexData = parseUtil.getMiddleStr(workStatus, SPLIT_COLON, SPLIT_COLON);
				if(workStHexData.equalsIgnoreCase("01")) {
					logger.info("01 management status");
					this.upCPStatusMng(deviceNo);
				}
				if(workStHexData.equalsIgnoreCase("02")) {
					logger.info("02 service status");
					this.upCPStatusSer(deviceNo);
				}
				if(workStHexData.equalsIgnoreCase("03")) {
					logger.info("03 fault status");
					this.upCPStatusError(deviceNo);
				}
			}
		
			if(statusStr!=null && !statusStr.equalsIgnoreCase("")) {
				logger.info("get 31 status");
				String statusHexValue =parseUtil.getMiddleStr(statusStr, SPLIT_COLON, SPLIT_COLON);
				
				Byte statusByte = CommFunction.hexStringToByte(statusHexValue);
				 byte[] array = new byte[8];  
			        for (int i = 7; i >= 0; i--) {  
			            array[i] = (byte)(statusByte & 1);  
			            statusByte = (byte) (statusByte >> 1);  
			        } 
			        logger.debug("byte 2 array success.");
			      if(array[7] ==0 && array[5]==0)
			      {
			    	  logger.debug("free");
			    	  upCPStatusSer(deviceNo);
			      }
			      if(array[7] ==0 && array[5]==1) {
			    	  logger.debug("charging");
			    	  upCPStatusCharging(deviceNo);
			      }
			}
			
			

			logger.info("checkStatus ends.");
		}
	/**
	 * 0x11 服务状态
	 * @param deviceNo
	 * @return
	 */
		private void upCPStatusSer(String deviceNo) {
			logger.debug("upCPStatusSer");
			MonChargingStatus4App statusData = cacheUtil.getStatusData(deviceNo);
			statusData.setIsTakeUp("no");
			statusData.setIsOnline("yes");
			statusData.setEnabled("yes");
			statusData.setIsError("no");
			cacheUtil.setStatusData(statusData);
			
		}
		
	/**
	 * 管理状态。
	 * @param deviceNo
	 */
		private void upCPStatusMng(String deviceNo) {
			logger.debug("upCPStatusMng");
			MonChargingStatus4App statusData = cacheUtil.getStatusData(deviceNo);
			statusData.setIsTakeUp("no");
			statusData.setIsOnline("no");
			statusData.setEnabled("no");
			cacheUtil.setStatusData(statusData);
			//return CP_STATUS_OFF;
			
		}
		
		private void upCPStatusCharging(String deviceNo) {
			logger.debug("upCPStatusCharging");
			MonChargingStatus4App statusData = cacheUtil.getStatusData(deviceNo);
			
			statusData.setIsTakeUp("yes");
			cacheUtil.setStatusData(statusData);
		}
	
		private void upCPStatusError(String deviceNo) {
			logger.debug("upCPStatusError");
			MonChargingStatus4App statusData = cacheUtil.getStatusData(deviceNo);
			
			statusData.setIsError("yes");
			cacheUtil.setStatusData(statusData);
		}
	
	/**
	 * 3.5 多了一个0x3d
	 * 故障报文0x11/0x31/0x34/0x35，check fault，if fault level =1，send05
	 * @param dto
	 */
	private void checkFault(MsgOfPregateDto dto,String inDate,String parsedMsgData) {
		logger.info("begin to checkFault.");
		
		boolean stopCharging = needStopCharging(parsedMsgData,inDate,dto);
		if(stopCharging) {
			logger.info("need stop charging");
			MsgOfPregateDto stopDto = constructMsgUtil.stopCharging(dto);
			if(stopDto==null) {
				logger.error("construct stop dto failed");
				return;
			}else {
				msgSend.msgSendToPreG(stopDto, false);
			}
			
		}
		
	}
//	.need version.指令序号从缓存中去，存到原始记录表中
	
	/**
	 * 解析故障报文，故障级别。
	 * @param msgCode 0x11/0x31/0x34/0x35/0x34
	 * @param obj 报文解析后的对象 0x11/0x31/0x34/0x35
	 * @return
	 */
	private boolean needStopCharging(String parsedMsgData,String inDate,MsgOfPregateDto dto) {
		logger.info("begin needStopCharging  "+dto.getMsgCode());
		String msgCode = dto.getMsgCode();
		switch(msgCode) {
		case "0x34": return check34(dto.getMsgCode(),parsedMsgData,dto.getMsgVersion(),dto.getDeviceNo(),inDate);
		
		case "0x31":return  check31(dto.getMsgCode(),parsedMsgData,dto.getMsgVersion(),dto.getDeviceNo(),inDate);
		case "0x11":return check11(dto.getMsgCode(),parsedMsgData,dto.getMsgVersion(),dto.getDeviceNo(),inDate);
		case "0x35":return check35(dto.getMsgCode(),parsedMsgData,dto.getMsgVersion(),dto.getDeviceNo(),inDate);
		//case "0x3D":return check3D(dto.getMsgCode(),parsedMsgData,dto.getMsgVersion(),dto.getDeviceNo(),inDate);
		case "0x3C":return check34(dto.getMsgCode(),parsedMsgData,dto.getMsgVersion(),dto.getDeviceNo(),inDate);
		
	}
		return false;
	}
	
	private boolean check34(String msgCode,String parsedMsgData,String deviceVersion,String deviceNo,String inDate) {
		logger.info("check 34.");
		boolean needStop = false;
		String start="充电模块故障代码";
		int startIdx = parsedMsgData.indexOf(start);
		String end = "充电机状态";
		int endIdx = parsedMsgData.indexOf(end);
		
		//faultStr / name:hexValue:meaning
		String faultstr = parsedMsgData.substring(startIdx,endIdx);
		String faultHexData = parseUtil.getMiddleStr(faultstr, SPLIT_COLON, SPLIT_COLON);
		int firstSplit = faultstr.indexOf(SPLIT_COLON);
		int secondSplit = faultstr.indexOf(SPLIT_COLON, firstSplit+1);
		
		String meaningStr = faultstr.substring(secondSplit+1,faultstr.length());
		logger.info("meaningStr");
		logger.debug(meaningStr);
		String[] newStrs = meaningStr.split("#");
		for(String newstr:newStrs) {
		logger.info("newstrs");
			
			String faultLevel="";
			String faultDef="";
			
		if(newstr.contains(FAULT_LEVEL)) {
			logger.info("faultlevel.");
			 faultLevel = parseUtil.getMiddleStr(newstr, FAULT_LEVEL, END_SEMICOLON);
			
		}
		if(newstr.contains(FAULT_DEF)) {
			logger.info("fault_def");
			faultDef = parseUtil.getMiddleStr(newstr, FAULT_DEF, END_SEMICOLON);
			

		}
		if(faultLevel.equalsIgnoreCase("")&&faultDef.equalsIgnoreCase("")) {
			logger.info("no fault");
			
		}else {
			upCPStatusError(deviceNo);
			//construct faultRecordDto and store
			MonFaultRecordDto  faultDto= new MonFaultRecordDto();
			faultDto.setDeviceNo(deviceNo);
			faultDto.setFaultSrcMsgCode(msgCode);
			faultDto.setDeviceVersion(deviceVersion);
			faultDto.setParsedFaultData(newstr);
			faultDto.setFaultLevel(faultLevel);
			faultDto.setInDate(inDate);
			faultDto.setFaultData(faultHexData);
			faultDto.setFaultDef(faultDef);
			logger.info("store faultDto begins");
			monDBService.setDeviceFaultInfo(faultDto);
			logger.info("store faultDto success");
			
		if( checkfaultLevel(faultLevel) ) {
			logger.info("needStop");
			needStop=true;
		}
		}
	
		}
		return needStop;
	}
	private boolean check31(String msgCode,String parsedMsgData,String deviceVersion,String deviceNo,String inDate) {
		logger.info("check 31.");
		boolean needStop = false;
		String start="充电系统故障状态";
		int startIdx = parsedMsgData.indexOf(start);
		String end = "AC/DC控制模块故障状态";
		int endIdx = parsedMsgData.indexOf(end);
		
		//faultStr / name:hexValue:meaning
		String faultstr = parsedMsgData.substring(startIdx,endIdx);
		String faultHexData = parseUtil.getMiddleStr(faultstr, SPLIT_COLON, SPLIT_COLON);
		
		int firstSplit = faultstr.indexOf(SPLIT_COLON);
		int secondSplit = faultstr.indexOf(SPLIT_COLON, firstSplit+1);
		
		String meaningStr = faultstr.substring(secondSplit+1,faultstr.length());
		logger.info("meaningStr");
		logger.debug(meaningStr);
		String[] newStrs = meaningStr.split("#");
		for(String newstr:newStrs) {
		logger.info("newstrs");
			
			String faultLevel="";
			String faultDef="";
			
		if(newstr.contains(FAULT_LEVEL)) {
			logger.info("faultlevel.");
			 faultLevel =parseUtil.getMiddleStr(newstr, FAULT_LEVEL, END_SEMICOLON);
			
		}
		if(newstr.contains(FAULT_DEF)) {
			logger.info("fault_def");
			faultDef =parseUtil.getMiddleStr(newstr, FAULT_DEF, END_SEMICOLON);
			

		}
		if(faultLevel.equalsIgnoreCase("")&&faultDef.equalsIgnoreCase("")) {
			logger.info("no fault");
			
		}else {
			logger.info("get0x31,upCPStatus");
			this.upCPStatusError(deviceNo);
			//construct faultRecordDto and store
			MonFaultRecordDto  faultDto= new MonFaultRecordDto();
			faultDto.setDeviceNo(deviceNo);
			faultDto.setFaultSrcMsgCode(msgCode);
			faultDto.setDeviceVersion(deviceVersion);
			faultDto.setParsedFaultData(newstr);
			faultDto.setFaultLevel(faultLevel);
			faultDto.setInDate(inDate);
			faultDto.setFaultData(faultHexData);
			faultDto.setFaultDef(faultDef);
			logger.info("store faultDto begins");
			monDBService.setDeviceFaultInfo(faultDto);
			logger.info("store faultDto success");
			
		if( checkfaultLevel(faultLevel) ) {
			logger.info("needStop");
			needStop=true;
		}
		}
	
		}
		return needStop;
		
	

	}
	
	private boolean check11(String msgCode,String parsedMsgData,String deviceVersion,String deviceNo,String inDate) {
		logger.info("check11");
		boolean needStop = false;
		
		String start="系统故障状态";
		int startIdx = parsedMsgData.indexOf(start);
		String end = "下游设备1";
		int endIdx = parsedMsgData.indexOf(end);
		
		//faultStr / name:hexValue:meaning
		String faultstr = parsedMsgData.substring(startIdx,endIdx);
		String faultHexData = parseUtil.getMiddleStr(faultstr, SPLIT_COLON, SPLIT_COLON);
		
		int firstSplit = faultstr.indexOf(SPLIT_COLON);
		int secondSplit = faultstr.indexOf(SPLIT_COLON, firstSplit+1);
		
		String meaningStr = faultstr.substring(secondSplit+1,faultstr.length());
		logger.info("meaningStr");
		logger.debug(meaningStr);
		String[] newStrs = meaningStr.split("#");
		for(String newstr:newStrs) {
		logger.info("newstrs");
			
			String faultLevel="";
			String faultDef="";
			
		if(newstr.contains(FAULT_LEVEL)) {
			logger.info("faultlevel.");
			 faultLevel =parseUtil.getMiddleStr(newstr, FAULT_LEVEL, END_SEMICOLON);
			
		}
		if(newstr.contains(FAULT_DEF)) {
			logger.info("fault_def");
			faultDef =parseUtil.getMiddleStr(newstr, FAULT_DEF, END_SEMICOLON);
			

		}
		if(faultLevel.equalsIgnoreCase("")&&faultDef.equalsIgnoreCase("")) {
			logger.info("no fault");
			
		}else {
			logger.info("get 0x11,upCPStatusError");
			this.upCPStatusError(deviceNo);
			//construct faultRecordDto and store
			MonFaultRecordDto  faultDto= new MonFaultRecordDto();
			faultDto.setDeviceNo(deviceNo);
			faultDto.setFaultSrcMsgCode(msgCode);
			faultDto.setDeviceVersion(deviceVersion);
			faultDto.setParsedFaultData(newstr);
			faultDto.setFaultLevel(faultLevel);
			faultDto.setInDate(inDate);
			faultDto.setFaultData(faultHexData);
			faultDto.setFaultDef(faultDef);
			logger.info("store faultDto begins");
			monDBService.setDeviceFaultInfo(faultDto);
			logger.info("store faultDto success");
			
		if( checkfaultLevel(faultLevel) ) {
			logger.info("needStop");
			needStop=true;
		}
		}
	
		}
		return needStop;
		
	
	}
	private boolean check35(String msgCode,String parsedMsgData,String deviceVersion,String deviceNo,String inDate) {
		logger.info("check35");
		boolean b = false;
		List<MonFaultRecordDto> list = cacheUtil.getFaultData(deviceNo);
		if(list.size()==0 || list.isEmpty()) {
			logger.info("no faultRecord in redis");
			
		}else {
			logger.info("get faultRecord from redis"+list.size());
			for(MonFaultRecordDto dto:list) {
				
				monDBService.setDeviceFaultInfo(dto);
			}
			b=true;
		}
		logger.info("del faultRecord in redis");
		cacheUtil.rmFaultData(deviceNo);
		return b;
	}
	
	private boolean checkfaultLevel(String faultLevel) {
		logger.debug("faultLevel "+ faultLevel);
		if(faultLevel.contains("1"))
		{
			logger.info("faultLevel is 1");
			return true;
		}
		return false;
	}
	/**
	 * 截取字符串中两个字符之间的数据：
	 * @param beginStr
	 * @param endStr
	 * @return
	 */
	private String getSubStr(String beginStr,String endStr,String srcStr) {
		int beginPos = srcStr.indexOf(beginStr);
		int endPos = srcStr.indexOf(endStr);
		return srcStr.substring(beginPos, endPos);
	}
	
	private void checkCache(MsgOfPregateDto dto,String parsedMsgData) {
		logger.debug("begin checkCache");
		String mesCode = dto.getMsgCode();
		
		 Map<String,String> codePairTs = cacheUtil.getCodePairTS(dto.getMsgVersion());
			if(codePairTs.isEmpty()) {
				logger.info("codePairTs is empty."+dto.getMsgVersion());
			return;
			}
			
		String codePair = codePairTs.get(mesCode);//get 05 by 15
		if(codePair==null||codePair.equalsIgnoreCase("")) {
			logger.info("codePair is null."+dto.getMsgVersion()+" "+mesCode);
			return;
		}
		String userId="用户ID:";
		String serialNo="指令序号:";
		String userIdTeamStr=parseUtil.getTeamStr(userId, parsedMsgData);
		String userIdHex = parseUtil.getMiddleStr( userIdTeamStr,SPLIT_COLON,SPLIT_COLON);
		String serialTeamSr=parseUtil.getTeamStr(serialNo, parsedMsgData);;
		String serialHex=parseUtil.getMiddleStr(serialTeamSr,SPLIT_COLON,SPLIT_COLON);
		StringBuilder cacheKey = new StringBuilder();
		cacheKey.append(dto.getDeviceNo());
		cacheKey.append("_");
		cacheKey.append(codePair);
		cacheKey.append("_");
		cacheKey.append(userIdHex);
		cacheKey.append(serialHex);
		
		String cacheKeyStr= cacheKey.toString();
		logger.info("cacheKeyStr = "+cacheKeyStr);
		
		String sendTime =cacheUtil.getMatchCode(cacheKeyStr);
		if(sendTime==null) {
			logger.info(cacheKeyStr +"does not exist in cache");
			return;
		}
		long inTime=TimeConvertor.stringTime2long(sendTime,TimeConvertor.FORMAT_MINUS_24HOUR);
		int interval = (int) (System.currentTimeMillis() - inTime);
		boolean b = false;
		String sucess="0";
		if(interval>10000) {
		logger.info(cacheKeyStr +"exist more than 10s.");
		cacheUtil.delMatchCode(cacheKeyStr);
		}else {
		cacheUtil.delMatchCode(cacheKeyStr);
		sucess="1";
		
		
	}
		UISendMsgResDto sendMsgres= new UISendMsgResDto();
		
		sendMsgres.setDeviceNo(dto.getDeviceNo());
		sendMsgres.setMsgCodeSt(dto.getDeviceNo());
		sendMsgres.setMsgCodeTs(mesCode);
		sendMsgres.setResult(sucess);
		sendMsgres.setUiSendTime(sendTime);
		
		rabbitMsgSender.sendUIMsgRes(sendMsgres);
		logger.info("senUiMsgRes success.");
	}
	
	private void parseToSpecialObj(MsgOfPregateDto dto,String parsedMsgData) {
		logger.info("parseToSpecialObj");
		if(dto.getMsgCode().equalsIgnoreCase("0x31")&&dto.getMsgVersion().equalsIgnoreCase(VERSION_3_4)) {
			parse31(dto,parsedMsgData);
		}
		
		if(dto.getMsgCode().equalsIgnoreCase("0x31")&&dto.getMsgVersion().equalsIgnoreCase(VERSION_3_2)) {
			parse31(dto,parsedMsgData);
		}
		
	}
	//get moduleAmount and gunAmount
	private void parse31(MsgOfPregateDto dto,String parsedMsgData) {
		logger.info("parse31");
		String receivedTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		String moduleAmount;
		String gunAmount;
	
	String subStr = parseUtil.getTeamStr("可充电机模块数量:", parsedMsgData);
	moduleAmount =parseUtil.getMiddleStr(subStr, SPLIT_COLON, SPLIT_COLON);
	byte moduleAmt = CommFunction.hexStringToByte(moduleAmount);
	String subStrGun = parseUtil.getTeamStr("充电枪数量:", parsedMsgData);
	gunAmount = parseUtil.getMiddleStr(subStrGun, SPLIT_COLON, SPLIT_COLON);
	byte gunAmt = CommFunction.hexStringToByte(gunAmount);

	logger.debug("getOverallData: "+moduleAmt+" "+gunAmt);
	MonOverallStateDto stateDto = new MonOverallStateDto();
	stateDto.setDeviceNo(dto.getDeviceNo());
	stateDto.setGunAmount(String.valueOf(gunAmt));
	stateDto.setChargemodNum(String.valueOf(moduleAmt));
	stateDto.setInDate(receivedTime);
	logger.info("set overAllState begins.");
	monDBService.setMonOverallState(stateDto);
	logger.info("set overAllState success.");
	}
	
	
	

	
/**
 * 
 * compareSockData(这里用一句话描述这个方法的作�?
 * (这里描述这个方法适用条件 �?可�?
 * @param tsDto from terminal to server
 * @param stDto from server to terminal
 * @return 
 *boolean
 * @exception 
 * @since  1.0.0
 */
	private boolean compareSockData(MsgOfPregateDto  tsDto, MsgOfPregateDto stDto) {
		logger.debug("compareSockData ts: "+tsDto.getMsgData());
		logger.debug("compareSockData st: "+stDto.getMsgData());
		byte[] tsDtoData = Encodes.decodeHex(tsDto.getMsgData());
		byte[] stDtoData = Encodes.decodeHex(stDto.getMsgData());
		if((tsDtoData.length<=2)||(stDtoData.length<=2)) {
			logger.error("dataformat is wrong.");
			return false;
		}
		if((tsDtoData[0]==stDtoData[0])&&
				(tsDtoData[1]==stDtoData[1])){
			logger.debug("matching.");
			return true;
		}else{
			logger.debug("not matching.");
			return false;
		}
	

	}



	@Override
	public void msgProcess(Object obj) {
		// TODO Auto-generated method stub
		doMsgProcessN((MsgOfPregateDto)obj);
	}
	
	
	private boolean storeOriginalData(MsgOfPregateDto dto,String[] decodedData) {
		logger.info("storeOriginalData begins:");
		if(decodedData.length!=2) {
			logger.info("decoded data is wrong.");
			return false;
		}
		int tr;
		String trc;
		
		
		if(decodedData[0].equals(null) ||decodedData[0].equals("")) {
			tr =0;//ok
			trc = null;
		}else{
			
			tr =1;//fail
			trc=decodedData[0];
		}
		int messType = msgCodeUtil.getMessTypeByCode(dto.getMsgCode(),dto.getMsgVersion());
		if(messType ==0) {
			logger.info("messType is wrong");
			return false;
		}
		monDBService.storeOriginalData(dto, messType, tr, trc, receivedTime, decodedData[1]);
		logger.info("storeOriginalData ends.");
			return true;
	}
	
	
	private void upCPProcessData(String deviceNo) {
		MonChargingProcessDataDto procData =cacheUtil.getCPProcessData(deviceNo);
		
		procData.setCurrentA(this.cp_currentElec);
		procData.setCurrentSoc(cp_currentSoc);
		procData.setEndTime(cp_receivedTime);
		procData.setSoc(cp_soc);
		procData.setTotalPower(cp_totalPower);
		procData.setVoltageA(cp_voltage);
		procData.setStartTime(cp_startTime);
		procData.setChargeStatus("0");//收到状态数据时，状态是充电中。
		procData.setGunConnStatus(cp_gunConnStatus);
		cacheUtil.upCPProcessData(procData);
	}
}
