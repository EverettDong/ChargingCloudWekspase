package com.cpit.icp.collect.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonDeviceOnlineStatusDao;
import com.cpit.icp.dto.collect.MonDeviceOnlineStatusDto;

@Service
public class MonDeviceOnlineStatusMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonDeviceOnlineStatusMgmt.class);
	private String work_state_fault ="2";
@Autowired MonDeviceOnlineStatusDao dao;
public boolean addDto(MonDeviceOnlineStatusDto dto) {
	dao.addDto(dto);
	return true;
}

public MonDeviceOnlineStatusDto getByDeviceNo(String deviceNo) {
	logger.debug("deviceNo: "+deviceNo);
	return dao.getByDeviceNo(deviceNo);
}

/**
 * 
 * @param deviceNo
 * @param onlineState 0 offline,1 online
 * @param updateTime
 * @return
 */
public boolean updateOnlineState(String deviceNo,String onlineState,String updateTime) {
	logger.debug("params: "+ deviceNo +" "+ onlineState);
	if(!onlineState.equalsIgnoreCase("0") || !onlineState.equalsIgnoreCase("1")) {
		logger.debug("params value is wrong.");
		return false;
	}
	
	return true;
}
/**
 * 
 * @param deviceNo
 * @param faultMsgCode 0x31/0x35/0x79
 * @param updateTime
 * @return
 */
public boolean updateFaultState(String deviceNo,String faultMsgCode,String updateTime) {
	MonDeviceOnlineStatusDto dto = getByDeviceNo(deviceNo);
	if(dto == null) {
		logger.debug("obj doesnot exist");
		return false;
	}else {
		dto.setWorkState(work_state_fault);
		dto.setUpdateTime(updateTime);
		dto.setFaultSourceMsg(faultMsgCode);
		dao.updateWorkStatus(dto);
		return true;
	}
}
/**
 * 
 * @param deviceNo
 * @param workState 工作状态 0空闲 1充电 2 故障
 * @param updateTime
 * @return
 */
public boolean updateWorkState(String deviceNo,String workState,String updateTime ) {
	MonDeviceOnlineStatusDto dto = getByDeviceNo(deviceNo);
	if(dto == null) {
		logger.debug("obj doesnot exist");
		return false;
	}else {
		String preWorkState = dto.getWorkState();
		if(preWorkState.equalsIgnoreCase("2")) {
			dto.setFaultSourceMsg("");
			
		}
		dto.setWorkState(workState);
		dto.setUpdateTime(updateTime);
		//dto.setFaultSourceMsg(faultMsgCode);
		dao.updateWorkStatus(dto);
		return true;
	}
}
}
