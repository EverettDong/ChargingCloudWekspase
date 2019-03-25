package com.cpit.icp.collect.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cpit.icp.dto.collect.MonRehargeModuleParamDto;
import com.cpit.icp.collect.dao.MonRehargeModuleParamDao;

@Service
public class MonRehargeModuleParamMgmt {
	
	private final static Logger logger = LoggerFactory.getLogger(MonRehargeModuleParamMgmt.class);
@Autowired MonRehargeModuleParamDao monRehargeModuleParamDao;

public boolean addDto(MonRehargeModuleParamDto dto){
	if(dto.getDeviceNo()!=null && dto.getPosition()!= -1)
	{
		MonRehargeModuleParamDto d = monRehargeModuleParamDao.getByDeviceAndPos(dto.getDeviceNo(), String.valueOf(dto.getPosition()));
		if(d!=null){
			monRehargeModuleParamDao.updateDto(dto);
		}else{
			monRehargeModuleParamDao.addDto(dto);
		}
		return true;
	}
	else{
		return false;
	}
	
}

public MonRehargeModuleParamDto getByDevicenoAndPos(String deviceNo,int position){
	if(deviceNo!=null && position !=-1){
		MonRehargeModuleParamDto dto = monRehargeModuleParamDao.getByDeviceAndPos(deviceNo, String.valueOf(position));
	return dto;
	}else{
		return null;
	}
}

}
