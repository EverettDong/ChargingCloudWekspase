package com.cpit.icp.collect.dao;

import org.apache.ibatis.annotations.Param;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonRechargingSourceDto;

@MyBatisDao
public interface MonRechargingSourceDao {
public void addDto(MonRechargingSourceDto dto);
public void updateDto(MonRechargingSourceDto dto);
public MonRechargingSourceDto getByDeviceno(@Param("deviceNo") String deviceNo);

}
