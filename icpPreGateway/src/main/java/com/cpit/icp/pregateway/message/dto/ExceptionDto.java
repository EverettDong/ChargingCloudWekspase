package com.cpit.icp.pregateway.message.dto;

import java.io.Serializable;
import java.util.Date;

import com.cpit.icp.pregateway.util.DateUtil;

public class ExceptionDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int month;
	private String device_ip;
	private int device_port;
	private String type;// 1:上行；2:下行；
	private String message;
	private String info;
	private Date insertdate;

	public ExceptionDto(String device_ip, int device_port, String type, String message, String info) {
		super();
		this.month = DateUtil.getMonth();
		this.device_ip = device_ip;
		this.device_port = device_port;
		this.type = type;
		this.message = message;
		this.info = info;

	}

	/**
	 * month
	 *
	 * @return the month
	 * @since 1.0.0
	 */

	public int getMonth() {
		return month;
	}

	/**
	 * @param month
	 *            the month to set
	 */
	public void setMonth(int month) {
		this.month = month;
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

	public int getDevice_port() {
		return device_port;
	}

	/**
	 * @param device_port
	 *            the device_port to set
	 */
	public void setDevice_port(int device_port) {
		this.device_port = device_port;
	}

	/**
	 * type
	 *
	 * @return the type
	 * @since 1.0.0
	 */

	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
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

	/**
	 * info
	 *
	 * @return the info
	 * @since 1.0.0
	 */

	public String getInfo() {
		return info;
	}

	/**
	 * @param info
	 *            the info to set
	 */
	public void setInfo(String info) {
		this.info = info;
	}

	/**
	 * insertdate
	 *
	 * @return the insertdate
	 * @since 1.0.0
	 */

	public Date getInsertdate() {
		return insertdate;
	}

	/**
	 * @param insertdate
	 *            the insertdate to set
	 */
	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ExceptionDto [month=" + month + ", device_ip=" + device_ip + ", device_port=" + device_port + ", type="
				+ type + ", message=" + message + ", info=" + info + ", insertdate=" + insertdate + "]";
	}

}
