package com.cpit.icp.collect.dao;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonRechargingBatteryInfoDto;


@MyBatisDao
public interface MonRechargingBatteryInfoDao {
public void addDto(MonRechargingBatteryInfoDto dto);
public void updateDto(MonRechargingBatteryInfoDto dto);
public MonRechargingBatteryInfoDto getByDeviceNoAP(String deviceNo,String position);
}
