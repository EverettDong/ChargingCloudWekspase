package com.cpit.icp.collect.impl;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.GateRouteInfoDao;
import com.cpit.icp.collect.utils.cache.DeviceCacheUtil;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
@Service
public class GateRouteInfoMgmt {
	private final static Logger logger = LoggerFactory.getLogger(GateRouteInfoMgmt.class);
	@Autowired GateRouteInfoDao gateRouteInfoDao;
	@Autowired DeviceCacheUtil deviceCacheUtil;

public boolean delGateRouteInfo(GateRouteInfoDto dto){
	
	GateRouteInfoDto oldDto = gateRouteInfoDao.getByDeviceNo(dto.getDeviceNo());
	if(oldDto !=null){
		gateRouteInfoDao.delDto(dto);
		logger.debug("del");
		return true;
	}else{
		
		logger.info("does not exist");
		return true;
	}
}

public boolean updateGateRouteInfo(GateRouteInfoDto dto){
	GateRouteInfoDto oldDto = gateRouteInfoDao.getByDeviceNo(dto.getDeviceNo());
	if(oldDto !=null){
		gateRouteInfoDao.updateDto(dto);
		logger.debug("update");
		return true;
	}else{
		gateRouteInfoDao.addDto(dto);
		logger.info("does not exist,add");
		return true;
	}
	
}
public boolean addGateRouteInfo(GateRouteInfoDto dto){
	gateRouteInfoDao.addDto(dto);
	return true;
}
public GateRouteInfoDto getByDeviceNo(String deviceNo){
	GateRouteInfoDto dto = deviceCacheUtil.getGateRouteInfo(deviceNo);
	if(dto !=null){
		return dto;
	}else{
		logger.info("no deviceNo gateWayinfo in redis.");
		return null;
	}
}

public GateRouteInfoDto getByDeviceNoSQL(String deviceNo){
	GateRouteInfoDto dto = gateRouteInfoDao.getByDeviceNo(deviceNo);
	if(dto !=null){
		return dto;
	}else{
		logger.info("getByDeviceNo get null");
		return null;
	}
}
/**
 * 
 * @param deviceNo
 * @return
 */
public boolean deviceDisConn(String deviceNo) {
	logger.debug("begin to disConn device");
	GateRouteInfoDto dto = this.getByDeviceNo(deviceNo);
	if(dto!=null) {
		logger.debug("get GateRouteInfoDto");
		//dto.setPreGateIp("0.0.0.0");
		//dto.setPreGatePort("0");
		//this.updateGateRouteInfo(dto);
		gateRouteInfoDao.delDto(dto);
	}else
	{
		
	}
	return true;
}
}
