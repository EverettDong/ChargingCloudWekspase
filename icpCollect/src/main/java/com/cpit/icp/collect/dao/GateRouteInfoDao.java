package com.cpit.icp.collect.dao;

import org.apache.ibatis.annotations.Param;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
@MyBatisDao
public interface GateRouteInfoDao {
	public void addDto(GateRouteInfoDto dto);

	public void updateDto(GateRouteInfoDto dto);

	public GateRouteInfoDto getByDeviceNo(@Param("deviceNo") String deviceNo);

	public void delDto(GateRouteInfoDto dto);
}
