package com.cpit.icp.collect.dao;

import org.apache.ibatis.annotations.Param;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonDeviceOnlineStatusDto;
@MyBatisDao
public interface MonDeviceOnlineStatusDao {
	public void addDto(MonDeviceOnlineStatusDto dto);

	public MonDeviceOnlineStatusDto getByDeviceNo(@Param(value = "deviceNo")String deviceNo);



	public void updateWorkStatus(MonDeviceOnlineStatusDto dto);
}
