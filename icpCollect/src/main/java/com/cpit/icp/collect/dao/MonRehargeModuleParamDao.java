package com.cpit.icp.collect.dao;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonRehargeModuleParamDto;

@MyBatisDao
public interface MonRehargeModuleParamDao {
public void addDto(MonRehargeModuleParamDto dto);
public void updateDto(MonRehargeModuleParamDto dto);
public MonRehargeModuleParamDto getByDeviceAndPos(String deviceNo,String position);
}
