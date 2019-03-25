package com.cpit.icp.collect.dao;

import org.apache.ibatis.annotations.Param;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.dto.collect.MonRechargingStatusInfoDto;

@MyBatisDao
public interface MonRechargingStatusInfoDao {
	public void addDto(MonRechargingStatusInfoDto dto);

	public void updateDto(MonRechargingStatusInfoDto dto);

	public String getBmsCodeByDeviceNoAP(@Param(value = "deviceNo") String deviceNo, @Param(value = "position") String position);
	/**
	 * 按照deviceno和position查询出的数据用indate来order。取出等于当前时间或者距离当前时间最近的一条
	 * @param deviceNo
	 * @param position
	 * @return
	 */
	public MonRechargingStatusInfoDto getOrderByInDate(@Param(value = "deviceNo") String deviceNo, @Param(value = "position") String position);
	public MonRechargingStatusInfoDto getByDeviceNoAP(@Param(value = "deviceNo") String deviceNo, @Param(value = "position") String position,@Param(value = "inData") String inData);
}
