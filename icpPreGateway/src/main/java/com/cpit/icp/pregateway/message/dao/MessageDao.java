package com.cpit.icp.pregateway.message.dao;

import java.util.List;

import com.cpit.common.MyBatisDao;
import com.cpit.icp.pregateway.message.dto.DeviceOfflineMessage;
import com.cpit.icp.pregateway.message.dto.ExceptionDto;
import com.cpit.icp.pregateway.message.dto.MessageDto;

@MyBatisDao
public interface MessageDao {

	public List<MessageDto> getAllmessage();

	public void addMessage(MessageDto dto);

	public void addException(ExceptionDto dto);

	void insertDeviceOfflineMessage(DeviceOfflineMessage record);

	List<DeviceOfflineMessage> selectByCon(String pregate_ip);

	void updateDeviceOfflineMessageById(int id);

}
