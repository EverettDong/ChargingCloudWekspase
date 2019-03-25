package com.cpit.icp.collect.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonRechargingStatusInfoDao;
import com.cpit.icp.dto.collect.MonRechargingStatusInfoDto;

@Service
public class MonRechargingStatusInfoMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonRechargingStatusInfoMgmt.class);
	@Autowired MonRechargingStatusInfoDao monRechargingStatusInfoDao;

	public boolean addDto(MonRechargingStatusInfoDto dto){
		MonRechargingStatusInfoDto d =getByDeviceNoAP(dto.getDeviceNo(), dto.getPosition(),dto.getInDate());
		if(d!=null){
			monRechargingStatusInfoDao.updateDto(dto);
			logger.info("dto exist,update");
			return true;
		}else{
			monRechargingStatusInfoDao.addDto(dto);
			logger.info("dto does not exist,add");
			return true;
		}
	}



	public String getBmsCodeByDeviceNoAP(String deviceNo, String position){
		if(deviceNo == null || position.equals("-1")){
			return null;
		}else{
			String bmsCode  =monRechargingStatusInfoDao.getBmsCodeByDeviceNoAP(deviceNo, position);
			return bmsCode;
		}
	}

	public MonRechargingStatusInfoDto getByDeviceNoAP(String deviceNo, String position,String inDate){
		if(deviceNo == null || position.equals("-1")){
			return null;
		}else{
			MonRechargingStatusInfoDto dto =monRechargingStatusInfoDao.getByDeviceNoAP(deviceNo, position,inDate);
			return dto;
		}
	}
	
	public List<MonRechargingStatusInfoDto> list(String deviceNo,String position){
		return null;
		
	}
}
