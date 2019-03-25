package com.cpit.icp.collect.dao;

import java.util.Date;
import java.util.List;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonDistributionInfoDto;
@MyBatisDao
public interface MonDistributionInfoDao {
	public void addDto(MonDistributionInfoDto dto);

	public List<MonDistributionInfoDto> listByDate(String deviceNo,int modePos,Date startD, Date endD);

}
