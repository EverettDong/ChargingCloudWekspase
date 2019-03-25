package com.cpit.icp.collect.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonRechargeRecordDao;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;

@Service
public class MonRechargeRecordMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonRechargeRecordMgmt.class);
@Autowired MonRechargeRecordDao monRechargeRecordDao;


public boolean addDto(MonRechargeRecordDto dto){
	MonRechargeRecordDto d = this.getByDeviceNo(dto.getDeviceNo(),dto.getCardId(),dto.getStartTime(),dto.getPlatTransFlowNum());
	if(d == null){
		monRechargeRecordDao.addDto(dto);
		logger.info("dto does not exist,add.");
		return true;
	}else{
		monRechargeRecordDao.updateDto(dto);
		logger.info("dto exists,update");
		
		return true;
	}
	
}
public MonRechargeRecordDto getByDeviceNo(String deviceNo,String cardId,String startTime,String platTransFlowNum){
	if(deviceNo ==null || cardId == null || startTime == null || platTransFlowNum == null){
		return null;
	}else{
		return monRechargeRecordDao.getByDeviceNo(deviceNo,cardId,startTime,platTransFlowNum);
	}
}

public List<MonRechargeRecordDto> queryRecent5Record(String cardId){
	return monRechargeRecordDao.queryRecent5Record(cardId);
}

public List<MonRechargeRecordDto> getSumkwh(List<String> deviceNoList,String startTime,String endTime){
	return monRechargeRecordDao.getSumKwhByDeviceno(deviceNoList,startTime,endTime);
}
}
