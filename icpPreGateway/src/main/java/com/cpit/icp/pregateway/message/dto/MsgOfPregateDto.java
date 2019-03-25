/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.pregateway.message.dto
 * 文件名：MsgOfPregateDto.java
 * 版本信息：1.0.0
 * 日期：2018年8月10日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.icp.pregateway.message.dto;

import java.io.Serializable;

/**
 * 类名称：MsgOfPregateDto 类描述： 创建人：jinzhiwei 创建时间：2018年8月10日 下午3:34:36 修改人： 修改时间：
 * 修改备注：
 * 
 * @version 1.0.0
 */
public class MsgOfPregateDto implements Serializable {
	private static final long serialVersionUID = 1L;
	// 设备号
	private String deviceNo;
	// 命令代码
	private String msgCode;
	private String msgVersion;
	private String preGateIp;
	private String preGatePort;
	private String msgData;

	/**
	 * 创建一个新的实例 MsgOfPregateDto.
	 *
	 * @param msgCode
	 * @param msgVersion
	 * @param preGateIp
	 * @param preGatePort
	 * @param msgData
	 */
	public MsgOfPregateDto(String deviceNo, String msgCode, String msgVersion, String preGateIp, String preGatePort,
			String msgData) {
		this.deviceNo = deviceNo;
		this.msgCode = msgCode;
		this.msgVersion = msgVersion;
		this.preGateIp = preGateIp;
		this.preGatePort = preGatePort;
		this.msgData = msgData;
	}
	
	public MsgOfPregateDto() {

	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	/**
	 * msgCode
	 *
	 * @return the msgCode
	 * @since 1.0.0
	 */

	public String getMsgCode() {
		return msgCode;
	}

	/**
	 * @param msgCode
	 *            the msgCode to set
	 */
	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

	/**
	 * msgVersion
	 *
	 * @return the msgVersion
	 * @since 1.0.0
	 */

	public String getMsgVersion() {
		return msgVersion;
	}

	/**
	 * @param msgVersion
	 *            the msgVersion to set
	 */
	public void setMsgVersion(String msgVersion) {
		this.msgVersion = msgVersion;
	}

	/**
	 * preGateIp
	 *
	 * @return the preGateIp
	 * @since 1.0.0
	 */

	public String getPreGateIp() {
		return preGateIp;
	}

	/**
	 * @param preGateIp
	 *            the preGateIp to set
	 */
	public void setPreGateIp(String preGateIp) {
		this.preGateIp = preGateIp;
	}

	/**
	 * preGatePort
	 *
	 * @return the preGatePort
	 * @since 1.0.0
	 */

	public String getPreGatePort() {
		return preGatePort;
	}

	/**
	 * @param preGatePort
	 *            the preGatePort to set
	 */
	public void setPreGatePort(String preGatePort) {
		this.preGatePort = preGatePort;
	}

	/**
	 * msgData
	 *
	 * @return the msgData
	 * @since 1.0.0
	 */

	public String getMsgData() {
		return msgData;
	}

	/**
	 * @param msgData
	 *            the msgData to set
	 */
	public void setMsgData(String msgData) {
		this.msgData = msgData;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MsgOfPregateDto [msgCode=" + msgCode + ", msgVersion=" + msgVersion + ", preGateIp=" + preGateIp
				+ ", preGatePort=" + preGatePort + ", msgData=" + msgData + "]";
	}

}
