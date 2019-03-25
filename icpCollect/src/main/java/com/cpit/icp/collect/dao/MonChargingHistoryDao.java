package com.cpit.icp.collect.dao;

import java.util.List;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonChargingHistoryDto;
@MyBatisDao
public interface MonChargingHistoryDao {
	
	public void addDto(MonChargingHistoryDto dto);
	public List<MonChargingHistoryDto> list(MonChargingHistoryDto dto);
}
