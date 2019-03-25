package com.cpit.icp.collect.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonChargingHistoryDao;
import com.cpit.icp.dto.collect.MonChargingHistoryDto;

@Service
public class MonChargingHistoryMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonChargingHistoryMgmt.class);
@Autowired MonChargingHistoryDao monChargingHistoryDao;
public boolean addDto(MonChargingHistoryDto dto){
	monChargingHistoryDao.addDto(dto);
	return true;
	
}

public List<MonChargingHistoryDto> list(MonChargingHistoryDto dto){
	List<MonChargingHistoryDto> dtoList = monChargingHistoryDao.list(dto);
	return dtoList;
}
}
