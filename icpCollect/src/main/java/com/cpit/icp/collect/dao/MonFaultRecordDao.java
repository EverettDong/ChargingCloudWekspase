package com.cpit.icp.collect.dao;

import java.util.List;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonFaultRecordDto;

@MyBatisDao
public interface MonFaultRecordDao {
	public void addDto(MonFaultRecordDto dto);
	/**
	 * 
	 * @param dto deviceNo/faultDef/faultLevel
	 * @return
	 */
	public List<MonFaultRecordDto> list(MonFaultRecordDto dto);

}
