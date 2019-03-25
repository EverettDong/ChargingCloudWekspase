package com.cpit.icp.collect.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonOverallStateDto;

@MyBatisDao
public interface MonOverallStateDao {
	public void addDto(MonOverallStateDto dto);
	public void upDto(MonOverallStateDto dto);
	public MonOverallStateDto getByDeviceNo(@Param("deviceNo")String deviceNo);
}
