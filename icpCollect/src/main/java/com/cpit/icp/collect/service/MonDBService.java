package com.cpit.icp.collect.service;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpit.common.SequenceId;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.dao.MonRechargeRecordDao;
import com.cpit.icp.collect.dao.OriginalDataStoreDao;
import com.cpit.icp.collect.impl.MonFaultRecordMgmt;
import com.cpit.icp.collect.impl.MonOverallStateMgmt;
import com.cpit.icp.collect.impl.MonRechargeRecordMgmt;
import com.cpit.icp.collect.impl.MonRechargingInfoMgmt;
import com.cpit.icp.collect.impl.MonRechargingStatusInfoMgmt;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import  com.cpit.icp.dto.collect.OriginalDataStoreDto;

import com.cpit.icp.dto.collect.MonFaultRecordDto;
import com.cpit.icp.dto.collect.MonOverallStateDto;
import  com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.MonRechargingInfoParseDto;
import com.cpit.icp.dto.collect.MonRechargingStatusInfoDto;
import com.cpit.icp.dto.collect.MonRechargingStatusInfoParseDto;
import com.cpit.icp.dto.collect.MonRechargingInfoDto;;

@Service
/**
 * 统一接口，所有与数据库相关的操作
 * @author zhangqianqian
 *
 */
public class MonDBService {
	private final static Logger logger = LoggerFactory.getLogger(MonDBService.class);
	@Autowired OriginalDataStoreDao originalDataStoreDao;
	//@Autowired MonRechargeRecordDao monRechargeRecordDao;
	@Autowired MonRechargeRecordMgmt  monRechargeRecordMgmt;
	@Autowired MonRechargingStatusInfoMgmt monRechargingStatusInfoMgmt;
	@Autowired MonRechargingInfoMgmt monRechargingInfoMgmt;
	@Autowired MonOverallStateMgmt  monOverallStateMgmt;
	@Autowired MonFaultRecordMgmt monFaultRecordMgmt;
	
	private static String originalId ="collectOriginalId";
	public boolean storeDB(){
		return true;
	}
	
	public boolean storeOriginalData(MsgOfPregateDto dto,int messType,int testResult,String testResultContent,String receivedTime,String parsedData) {
		
	//	int id = SequenceId.getInstance().getId(originalId);
		
		
		OriginalDataStoreDto originalDataDto = new OriginalDataStoreDto();
		
		originalDataDto.setDeviceNo(dto.getDeviceNo());
		originalDataDto.setMessCode(dto.getMsgCode());
		originalDataDto.setMessType(messType);
		originalDataDto.setMessData(dto.getMsgData());
		originalDataDto.setMsgFormatTR(testResult);
		originalDataDto.setMsgFormatTC(testResultContent);
		originalDataDto.setInTime(receivedTime);
		int day = getDayByIntime(receivedTime);
		originalDataDto.setInDay(day);
		originalDataDto.setParsedData(parsedData);
		originalDataStoreDao.addDto(originalDataDto);
		logger.info("storeOriginalData success.");
		return true;
		
		
	}
	private int getMonthByIntime(String intime) {
		Calendar cal=Calendar.getInstance();
		Date d = TimeConvertor.stringTime2Date(intime, TimeConvertor.FORMAT_MINUS_24HOUR);
		cal.setTime(d);
		return cal.get(Calendar.MONTH)+1;
	}
	private int getDayByIntime(String intime) {
		Calendar cal=Calendar.getInstance();
		Date d = TimeConvertor.stringTime2Date(intime, TimeConvertor.FORMAT_MINUS_24HOUR);
		cal.setTime(d);
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * 获取模块数量
	 * @param deviceNo
	 * @return
	 */
	public int getDeviceModuleNums(String deviceNo) {
		MonOverallStateDto dto=	monOverallStateMgmt.getByDeviceNo(deviceNo);
		if(dto==null) {
			return 1;
		}
		String moduleNums = dto.getChargemodNum();
		if(moduleNums!=null) {
			return Integer.parseInt(moduleNums);
		}
		else {
			return 1;
		}
		//return 1;
	}
	/**
	 * mesCode 包含0x
	 * @param obj
	 * @param mesCode
	 * @return
	 */
	public boolean storeObjData(Object obj,String mesCode,String deviceNo) {
		logger.info("storeObjData begins");
		String newMsgCode = null;
		if(mesCode.contains("0x")) {
			newMsgCode=	mesCode.substring(mesCode.indexOf("0x")+2, mesCode.length());
			logger.debug("get rid of pre 0x");
		}
		Byte code = CommFunction.hexStringToByte(newMsgCode);
		logger.debug(" "+code);
		String inDate = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		switch(code) {
		case 0x79:
			MonRechargeRecordDto d = (MonRechargeRecordDto)obj;
			d.setDeviceNo(deviceNo);
			
			d.setInDate(inDate);
			monRechargeRecordMgmt.addDto(d);
			logger.info("79 stored.");
			break;
		case 0x7D:
			MonRechargeRecordDto d7d = (MonRechargeRecordDto)obj;
			d7d.setDeviceNo(deviceNo);
			
			d7d.setInDate(inDate);
			monRechargeRecordMgmt.addDto(d7d);
			logger.info("7D stored.");
			break;	
			
		case 0x78:
			MonRechargeRecordDto d78 = (MonRechargeRecordDto)obj;
			d78.setDeviceNo(deviceNo);
			
			d78.setInDate(inDate);
			d78.setPlatTransFlowNum("3178");
			monRechargeRecordMgmt.addDto(d78);
			logger.info("78 31 stored.");
			break;
			
		case 34:
			store34((MonRechargingStatusInfoParseDto)obj,deviceNo);
			logger.debug("34 stored.");
			break;
		case 39:
			store39((MonRechargingInfoParseDto)obj,deviceNo);
			logger.debug("39 stored.");
			break;
		}
		return true;
	}
	private void store39(MonRechargingInfoParseDto parseDto,String deviceNo) {
		if(parseDto ==null) {
			logger.error("store39 error,obj is null");
			return;
		}
		if(parseDto.getPosition().isEmpty() || parseDto.getPosition().size() ==0) {
			logger.error("store39 obj error,size is 0");
			return;
		}
		int size = parseDto.getPosition().size();
		for(int i=0;i<size;i++ ) {
			MonRechargingInfoDto dto = new MonRechargingInfoDto();
			dto.setDeviceNo(deviceNo);
			dto.setOperatorId(parseDto.getOperatorId());
			dto.setSerialNo(parseDto.getSerialNo());
			dto.setGunAmount(parseDto.getGunAmount());
			
			dto.setPosition(parseDto.getPosition().get(i));
			dto.setRechargeType(parseDto.getRechargeType().get(i));
			dto.setKwh(parseDto.getKwh().get(i));
			dto.setAh(parseDto.getAh().get(i));
			dto.setRechargeTime(parseDto.getRechargeTime().get(i));
			dto.setStartSoc(parseDto.getStartSoc().get(i));
			dto.setCurrentSoc(parseDto.getCurrentSoc().get(i));
			dto.setCurrentKwh(parseDto.getCurrentKwh().get(i));
			dto.setInDate(TimeConvertor.getDate(TimeConvertor.FORMAT_SLASH_24HOUR));
			
			monRechargingInfoMgmt.addDto(dto);
		}
	}
	private void store34(MonRechargingStatusInfoParseDto parseDto,String deviceNo){
		//List<MonRechargingStatusInfoDto> list = new ArrayList<MonRechargingStatusInfoDto>();
		if(parseDto ==null) {
			logger.error("store34 error,obj is null");
			return;
		}
		if(parseDto.getPosition().isEmpty() || parseDto.getPosition().size() ==0) {
			logger.error("store34 obj error,size is 0");
			return;
		}
		int size = parseDto.getPosition().size();
		for(int i=0;i<size;i++) {
			MonRechargingStatusInfoDto dto = new MonRechargingStatusInfoDto();
			dto.setDeviceNo(deviceNo);
			dto.setOperatorId(parseDto.getOperatorId());
			dto.setSerialNo(parseDto.getSerialNo());
			dto.setGunAmount(parseDto.getGunAmount());
			
			dto.setPosition(parseDto.getPosition().get(i));
			dto.setRechargeType(parseDto.getRechargeType().get(i));
			dto.setModuleId(parseDto.getModuleId().get(i));
			dto.setBmsCode(parseDto.getBmsCode().get(i));
			dto.setVol(parseDto.getVol().get(i));
			dto.setTem(parseDto.getTem().get(i));
			dto.setOutputPower(parseDto.getOutputPower().get(i));
			dto.setErrorCode(parseDto.getErrorCode().get(i));
			dto.setChargerStatus(parseDto.getChargerStatus().get(i));
			dto.setInDate(TimeConvertor.getDate(TimeConvertor.FORMAT_SLASH_24HOUR));
			dto.setExeOrganStatus(parseDto.getExeOrganStatus().get(0));
			monRechargingStatusInfoMgmt.addDto(dto);
			//list.add(dto);
			
		}
		
	
		
	}
	
	/**
	 * 
	 * @param deviceNo
	 * @return
	 */
	public MonRechargingInfoDto queryChargingStatus(String deviceNo,String postion) {
		return monRechargingInfoMgmt.getCurrentDto(deviceNo, postion);
	}
	
	/**
	 * 写入故障信息
	 * @param deviceNo 设备编码
	 * @param faultMsgCode 故障来源报文code  eg:0x11/0x31/0x35/0x79/0x34
	 * @param faultInfo 故障内容 十六进制 00001
	 * @param faultLevel 故障等级  1/2/3
	 */
	public void setDeviceFaultInfo(MonFaultRecordDto faultDto) {
		logger.info("setDeviceFaultInfo");
		monFaultRecordMgmt.addDto(faultDto);
	}
	
	public void setMonOverallState(MonOverallStateDto dto) {
		logger.info("setMonOverallState");
		monOverallStateMgmt.addDto(dto);
	}
	/**
	 * 查询充电计费信息
	 * @param deviceNo
	 * @param cardId
	 * @param startTime
	 * @param platTransFlowNum
	 * @return
	 */
	public MonRechargeRecordDto queryRechargeRecord(String deviceNo,String cardId,String startTime,String platTransFlowNum) {
		MonRechargeRecordDto dto= monRechargeRecordMgmt.getByDeviceNo(deviceNo, cardId, startTime, platTransFlowNum);
	return dto;
	}
	/**
	 * 返回五条充电记录，for0x64
	 * @param cardId
	 * @return
	 */
	public List<MonRechargeRecordDto> queryRecent5Record(String cardId){
		return monRechargeRecordMgmt.queryRecent5Record(cardId);
	}
	
	/**
	 * 数据迁移,把这个时间之前的数据迁移到新库
	 * @param time yyyy-mm-dd HH:mm:ss
	 * @return
	 */
	public boolean originalDataTransfer(String time) {
	
		List<Date> dataList = getDayDate(time);
		for(Date nowDate:dataList) {
			int devideGrade =0;
			Date pre1Hour = this.getPrevious1Hour(nowDate);
		process(pre1Hour,nowDate,devideGrade,pre1Hour);
			
		}
		return true;
	}
	
	public void process(Date preDate,Date nowDate,int devideGrade,Date endDate) {
		Date preD;
	
			if(canbeProcess(preDate,nowDate,devideGrade)) {
			nowDate=preDate;
			 preD =getPreD(devideGrade,nowDate);
			
		}else {
			devideGrade++;
			 preD =getPreD(devideGrade,nowDate);
			
		}
		if(preD.compareTo(endDate) <0) {
			String preStr = TimeConvertor.date2String(preD, TimeConvertor.FORMAT_MINUS_24HOUR);
			String endDateStr = TimeConvertor.date2String(endDate, TimeConvertor.FORMAT_MINUS_24HOUR);
				logger.debug("exit: " + preStr +" "+ endDateStr);
				return;
			}	
		process(preD,nowDate,devideGrade ,endDate);
		
	}
	private boolean canbeProcess(Date srcDate,Date endDate,int devideGrade) {
		
		String startTime =  TimeConvertor.date2String(srcDate, TimeConvertor.FORMAT_MINUS_24HOUR);
		String endTime =  TimeConvertor.date2String(endDate, TimeConvertor.FORMAT_MINUS_24HOUR);
		int count = this.getOriginalDataNums(startTime, endTime);
		logger.debug(" "+startTime+" "+endTime +"   "+count+"   "+devideGrade);
		if(count>2 && devideGrade <4) {
			return false;
		}else {
			return true;
		}
		
	}
	private Date getPreD(int devideGrade,Date nowDate) {
		if(devideGrade>=4) {
			return this.getPrevious1Min(nowDate);
		}else {
			switch(devideGrade) {
			case 0: return this.getPrevious1Hour(nowDate);
			case 1: return this.getPreviousHalfHour(nowDate);
			case 2: return this.getPreviousQuarter(nowDate);
			case 3: return this.getPrevious5Min(nowDate);
			default: return null;
			}
			
		}
	
	}


	/**
	 * 
	 * @param startTime
	 * @param endTime
	 * @return 0 ,no data
	 */
	public int getOriginalDataNums(String startTime,String endTime) {
		
		int count =originalDataStoreDao.countDto(startTime, endTime);
		return count;
		
	}
	@Transactional
	public List<OriginalDataStoreDto> getAndInsert(String startTime,String endTime){
	
		List<OriginalDataStoreDto> count =originalDataStoreDao.getBetweenTime(startTime, endTime);
	
		return count;
	}
	
	private Date getPrevious1Hour(Date srcdate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(srcdate);
		
		int hour =cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH,day);
		cal.set(Calendar.HOUR_OF_DAY, hour-1);
	
		Date newD = cal.getTime();
		return newD;
	}
	
	private Date getPreviousHalfHour(Date srcdate) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(srcdate);
		int min =cal.get(Calendar.MINUTE);
		//int hour =cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH,day);
	//	cal.set(Calendar.HOUR_OF_DAY, hour);
	cal.set(Calendar.MINUTE, min-30);
		Date newD = cal.getTime();
		return newD;
	}
	private Date getPreviousQuarter(Date srcdate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(srcdate);
		int min = cal.get(Calendar.MINUTE);
		//int hour =cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH,day);
		cal.set(Calendar.MINUTE, min-15);
	
		Date newD = cal.getTime();
		return newD;
	}
	private Date getPrevious5Min(Date srcdate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(srcdate);
		int min = cal.get(Calendar.MINUTE);
		//int hour =cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH,day);
		cal.set(Calendar.MINUTE, min-5);
	
		Date newD = cal.getTime();
		return newD;
	}
	private Date getPrevious1Min(Date srcdate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(srcdate);
		int min = cal.get(Calendar.MINUTE);
		//int hour =cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH,day);
		cal.set(Calendar.MINUTE, min-1);
	
		Date newD = cal.getTime();
		return newD;
	}
	public List<Date> getDayDate(String time){
		List<Date> list = new ArrayList<Date>();
		
		Date d = TimeConvertor.stringTime2Date(time, TimeConvertor.FORMAT_MINUS_24HOUR);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		
		int hour =cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		for(int i=0;i<24;i++) {
			cal.set(Calendar.DAY_OF_MONTH,day);
			cal.set(Calendar.HOUR_OF_DAY, hour-i);
			
			Date newD = cal.getTime();
			list.add(newD);
			
		
		}
		
		
		return list;
	}
	
	public List<MonRechargeRecordDto> getSumKwhByDeviceNo(List<String> deviceNoList,String startTime,String endTime){
		return monRechargeRecordMgmt.getSumkwh(deviceNoList,startTime,endTime);
	}
}
