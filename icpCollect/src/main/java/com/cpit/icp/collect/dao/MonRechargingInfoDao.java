package com.cpit.icp.collect.dao;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonRechargingInfoDto;
import org.apache.ibatis.annotations.Param;

@MyBatisDao
public interface MonRechargingInfoDao {
    public void addDto(MonRechargingInfoDto dto);

    public void updateDto(MonRechargingInfoDto dto);
/**
 * 按照deviceno和position查询出的数据用indate来order。取出等于当前时间或者距离当前时间最近的一条
 * @param deviceNo
 * @param position
 * @return
 */
    public MonRechargingInfoDto getOrderByInDate(@Param(value = "deviceNo")String deviceNo, @Param(value = "position")String position);
    public MonRechargingInfoDto getByDeviceNoAP(@Param(value = "deviceNo")String deviceNo, @Param(value = "position")String position, @Param(value = "inDate")String inDate);
}
