package com.cpit.icp.collect.coderDecoder.common.configurable;

public class deviceInfo {

	private String version;//版本
	
	private String deviceCode;//充电桩编码
	
	private String ChineseEncoding;//智能终端中文编码方式
	
	private String errorMsg;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public String getChineseEncoding() {
		return ChineseEncoding;
	}

	public void setChineseEncoding(String chineseEncoding) {
		ChineseEncoding = chineseEncoding;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Override
	public String toString() {
		return "deviceInfo [version=" + version + ", deviceCode=" + deviceCode + ", ChineseEncoding=" + ChineseEncoding
				+ ", errorMsg=" + errorMsg + "]";
	}

}
