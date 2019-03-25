package com.cpit.icp.collect.dao;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;

import java.util.List;

import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface MonRechargeRecordDao {
    public void addDto(MonRechargeRecordDto dto);

    public void updateDto(MonRechargeRecordDto dto);

    public MonRechargeRecordDto getByDeviceNo(@Param(value = "deviceNo")String deviceNo, @Param(value = "cardId")String cardId, @Param(value = "startTime")String startTime, @Param(value = "platTransFlowNum")String platTransFlowNum);
    public List<MonRechargeRecordDto> queryRecent5Record(@Param(value = "cardId")String cardId);
    public List<MonRechargeRecordDto> getSumKwhByDeviceno(@Param(value = "list")List<String> list, @Param(value = "startTime")String startTime, @Param(value = "endTime")String endTime);
    
}
