package com.cpit.icp.collect.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonOverallStateDao;
import com.cpit.icp.dto.collect.MonOverallStateDto;
@Service
public class MonOverallStateMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonOverallStateMgmt.class);
	
	@Autowired MonOverallStateDao  monOverallStateDao;
	
	public boolean addDto(MonOverallStateDto dto){
		MonOverallStateDto old = getByDeviceNo(dto.getDeviceNo());
		if(old!=null) {
			monOverallStateDao.upDto(dto);
		}else {
			monOverallStateDao.addDto(dto);
		}
		
		
		return true;
	}
	public MonOverallStateDto getByDeviceNo(String deviceNo){
		return monOverallStateDao.getByDeviceNo(deviceNo);
	}
	
}
