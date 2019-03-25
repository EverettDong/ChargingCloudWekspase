/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.pregateway.message.impl
 * 文件名：ExceptionMgmt.java
 * 版本信息：1.0.0
 * 日期：2018年8月2日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.icp.pregateway.message.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.pregateway.message.dao.MessageDao;
import com.cpit.icp.pregateway.message.dto.ExceptionDto;

/**
 * 类名称：ExceptionMgmt 类描述： 创建人：jinzhiwei 创建时间：2018年8月2日 下午2:40:50 修改人： 修改时间：
 * 修改备注：
 * 
 * @version 1.0.0
 */
@Service
public class ExceptionMgmt {

	@Autowired
	private MessageDao messageDao;

	public void addException(ExceptionDto dto) {
		 messageDao.addException(dto);
	}
}
