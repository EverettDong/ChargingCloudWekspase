package com.cpit.icp.collect.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.OriginalDataStoreDto;
@MyBatisDao
public interface OriginalDataStoreDao {
public int addDto(OriginalDataStoreDto dto);
public int countDto(@Param("startTime") String startTime,@Param("endTime") String endTime);
public List<OriginalDataStoreDto> getBetweenTime(@Param("startTime") String startTime,@Param("endTime") String endTime);
}
