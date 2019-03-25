package com.cpit.icp.pregateway.message.dto;

import java.io.Serializable;
import java.util.Date;

import com.cpit.icp.pregateway.util.DateUtil;
import com.cpit.icp.pregateway.util.FtpUtil;

public class DeviceOfflineMessage implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;

	private String device_no;

	private String type;
	
	private String pregate_ip;

	private String device_ip;

	private String device_port;

	private int month;

	private Date p_time;

	private String message;

	private int state;

	private String version;

	public DeviceOfflineMessage(String version, String type, String device_no, String device_ip, String device_port,
			String message, int state, Date p_time) {
		this.month = DateUtil.getMonth();
		this.version = version;
		this.type = type;
		this.device_no = device_no;
		this.device_ip = device_ip;
		this.device_port = device_port;
		this.state = state;
		this.message = message;
		this.p_time = p_time;
		this.pregate_ip=FtpUtil.pregateIp;

	}
	
	public DeviceOfflineMessage() {
	}
	
		
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPregate_ip() {
		return pregate_ip;
	}



	public void setPregate_ip(String pregate_ip) {
		this.pregate_ip = pregate_ip;
	}



	/**
	 * version
	 *
	 * @return the version
	 * @since 1.0.0
	 */

	public String getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}



	/**
	 * device_no
	 *
	 * @return the device_no
	 * @since 1.0.0
	 */

	public String getDevice_no() {
		return device_no;
	}

	/**
	 * @param device_no
	 *            the device_no to set
	 */
	public void setDevice_no(String device_no) {
		this.device_no = device_no;
	}

	/**
	 * device_ip
	 *
	 * @return the device_ip
	 * @since 1.0.0
	 */

	public String getDevice_ip() {
		return device_ip;
	}

	/**
	 * @param device_ip
	 *            the device_ip to set
	 */
	public void setDevice_ip(String device_ip) {
		this.device_ip = device_ip;
	}

	/**
	 * device_port
	 *
	 * @return the device_port
	 * @since 1.0.0
	 */

	public String getDevice_port() {
		return device_port;
	}

	/**
	 * @param device_port
	 *            the device_port to set
	 */
	public void setDevice_port(String device_port) {
		this.device_port = device_port;
	}





	/**
	 * p_time
	 *
	 * @return the p_time
	 * @since 1.0.0
	 */

	public Date getP_time() {
		return p_time;
	}

	/**
	 * @param p_time
	 *            the p_time to set
	 */
	public void setP_time(Date p_time) {
		this.p_time = p_time;
	}

	/**
	 * message
	 *
	 * @return the message
	 * @since 1.0.0
	 */

	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public int getMonth() {
		return month;
	}


	public void setMonth(int month) {
		this.month = month;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DeviceOfflineMessage [id=" + id + ", device_no=" + device_no + ", device_ip=" + device_ip
				+ ", device_port=" + device_port + ", month=" + month + ", p_time=" + p_time + ", message=" + message
				+ ", state=" + state + "]";
	}

}