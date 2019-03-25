package com.cpit.icp.collect.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonBatteryModuleParamDao;
import com.cpit.icp.dto.collect.MonBatteryModuleParamDto;
@Service
public class MonBatteryModuleParamMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonBatteryModuleParamMgmt.class);
@Autowired MonBatteryModuleParamDao monBatteryModuleParamDao;

public boolean addDto(MonBatteryModuleParamDto dto){
	MonBatteryModuleParamDto d = getByDevicenoAndPos(dto.getDeviceNo(),dto.getPosition());
	if(d == null){
		logger.info("dto exist,update");
		monBatteryModuleParamDao.updateDto(dto);
		return true;
	}else{
		logger.info("dto does not exist,add");
		monBatteryModuleParamDao.addDto(dto);
		return true;
	}
	
}

public MonBatteryModuleParamDto getByDevicenoAndPos(String deviceNo,int position){
	if(deviceNo == null || position ==-1){
		logger.error("getByDevicenoAndPos para wrong");
		return null;
	}else{
		MonBatteryModuleParamDto dto = monBatteryModuleParamDao.getByDevicenoAndPos(deviceNo, position);
		return dto;
	}
}
}
