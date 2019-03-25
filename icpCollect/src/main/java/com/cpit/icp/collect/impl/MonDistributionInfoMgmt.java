package com.cpit.icp.collect.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonDistributionInfoDao;
import com.cpit.icp.dto.collect.MonDistributionInfoDto;

@Service
public class MonDistributionInfoMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonDistributionInfoMgmt.class);
@Autowired MonDistributionInfoDao monDistributionInfoDao;
public boolean addDto(MonDistributionInfoDto dto){
	monDistributionInfoDao.addDto(dto);
	return true;
}

public List<MonDistributionInfoDto> listByDate(String deviceNo,int modePos,Date startD, Date endD){
	return monDistributionInfoDao.listByDate(deviceNo, modePos, startD, endD);
}

}
