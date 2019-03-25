package com.cpit.icp.pregateway.message.dto;

import java.io.Serializable;
import java.util.Date;

public class MessageDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String command;
	private String version;
	private String type;// 1:上行；2:下行；
	private String message;
	private String device_no;
	private String device_ip;
	private int device_port;

	private Date insertdate;
	private Date begindate;
	private Date enddate;
	private int month;
	
	private byte[] data;

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

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

	/**
	 * command
	 *
	 * @return the command
	 * @since 1.0.0
	 */

	public String getCommand() {
		return command;
	}

	/**
	 * @param command
	 *            the command to set
	 */
	public void setCommand(String command) {
		this.command = command;
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
	 * begindate
	 *
	 * @return the begindate
	 * @since 1.0.0
	 */

	public Date getBegindate() {
		return begindate;
	}

	/**
	 * @param begindate
	 *            the begindate to set
	 */
	public void setBegindate(Date begindate) {
		this.begindate = begindate;
	}

	/**
	 * enddate
	 *
	 * @return the enddate
	 * @since 1.0.0
	 */

	public Date getEnddate() {
		return enddate;
	}

	/**
	 * @param enddate
	 *            the enddate to set
	 */
	public void setEnddate(Date enddate) {
		this.enddate = enddate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "MessageDto [command=" + command + ", type=" + type + ", message=" + message + ", device_no=" + device_no
				+ ", device_ip=" + device_ip + ", device_port=" + device_port + ", insertdate=" + insertdate
				+ ", begindate=" + begindate + ", enddate=" + enddate + "]";
	}

}
