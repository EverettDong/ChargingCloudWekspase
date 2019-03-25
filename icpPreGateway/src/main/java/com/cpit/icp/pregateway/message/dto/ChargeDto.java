package com.cpit.icp.pregateway.message.dto;

import java.io.Serializable;
import java.util.Date;

public class ChargeDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private String command;
	private String type;
	private String message;

	public ChargeDto(String code, String type, String message) {
		super();
		this.command = code;
		this.type = type;
		this.message = message;
	}

	public ChargeDto(String code, String type, String message, Date insertdate, String ip) {
		super();
		this.command = code;
		this.type = type;
		this.message = message;

	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageDto [command=" + command + ", type=" + type + ", message=" + message + "]";
	}

}
