package com.cpit.icp.pregateway.message.dto;

import java.io.Serializable;

public class ErrorMsg implements Serializable{
	private static final long serialVersionUID = 1L;
	
	
	public static final int ERR_MISS_PARAM = 1;//参数缺失
	public static final int ERR_WRONG_PARAM = 2;//参数错误
	public static final int ERR_SYSTEM_ERROR = 3;//系统异常
	
	
	private int id;
	
	private String msg;
	
	public ErrorMsg(){
		
	}
	
	public ErrorMsg(int id, String msg){
		this.id = id;
		this.msg = msg;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((msg == null) ? 0 : msg.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorMsg other = (ErrorMsg) obj;
		if (id != other.id)
			return false;
		if (msg == null) {
			if (other.msg != null)
				return false;
		} else if (!msg.equals(other.msg))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ErrorMsg [id=" + id + ", msg=" + msg + "]";
	}


	

}

