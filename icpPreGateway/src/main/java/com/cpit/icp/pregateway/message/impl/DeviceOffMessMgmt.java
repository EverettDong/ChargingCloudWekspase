/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.pregateway.message.impl
 * 文件名：DeviceOffMessMgmt.java
 * 版本信息：1.0.0
 * 日期：2018年8月2日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.icp.pregateway.message.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.pregateway.message.dao.MessageDao;
import com.cpit.icp.pregateway.message.dto.DeviceOfflineMessage;

/**
 * 类名称：DeviceOffMessMgmt 类描述： 创建人：jinzhiwei 创建时间：2018年8月2日 下午2:51:16 修改人： 修改时间：
 * 修改备注：
 * 
 * @version 1.0.0
 */
@Service
public class DeviceOffMessMgmt {

	@Autowired
	private MessageDao messageDao;

	public List<DeviceOfflineMessage> selectByCon(String pregateIP) {
		List<DeviceOfflineMessage> offlineMsg =new ArrayList<DeviceOfflineMessage>();
		offlineMsg =messageDao.selectByCon(pregateIP);
		return offlineMsg;
	}

	public void updateDeviceOfflineMessageById(int id) {
		 messageDao.updateDeviceOfflineMessageById(id);
	}

	public void insertDeviceOfflineMessage(DeviceOfflineMessage record) {
		messageDao.insertDeviceOfflineMessage(record);
	}

}
