package com.cpit.icp.collect.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonRechargingSourceDao;
import com.cpit.icp.dto.collect.MonRechargingSourceDto;

@Service
public class MonRechargingSourceMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonRechargingSourceMgmt.class);
	
	@Autowired MonRechargingSourceDao dao;
	
	public MonRechargingSourceDto getByDeviceno(String deviceNo){
		return dao.getByDeviceno(deviceNo);
	}
	
	public boolean addDto(MonRechargingSourceDto dto){
		dao.addDto(dto);;
		return true;
	}
	//充电结束或者充电中途有异常的情况下，必须调用改 接口
	public boolean updateDto(MonRechargingSourceDto dto){
		dao.updateDto(dto);
		return true;
	}
}
