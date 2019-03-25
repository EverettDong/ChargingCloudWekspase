package com.cpit.icp.collect.dao;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonBatteryModuleParamDto;

@MyBatisDao
public interface MonBatteryModuleParamDao {
public void addDto(MonBatteryModuleParamDto dto);
public MonBatteryModuleParamDto getByDevicenoAndPos(String deviceNo,int position );
public void updateDto(MonBatteryModuleParamDto dto);
}
