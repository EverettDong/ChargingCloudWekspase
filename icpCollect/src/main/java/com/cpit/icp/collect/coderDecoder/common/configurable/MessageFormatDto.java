package com.cpit.icp.collect.coderDecoder.common.configurable;



import java.io.Serializable;

/**
 * @author maming
 * 报文头DTO
 */
@SuppressWarnings("serial")
public class MessageFormatDto implements Serializable {
	//元素长度属性
	int length;
	//元素名称属性
	String name;
	//该元素在整个报文中位置，解析时计算填入
	int pos;
	//该元素值，十六进制字符串
	String value;
	public int getLength() {
		return length;
	}
	public String getName() {
		return name;
	}
	public int getPos() {
		return pos;
	}
	public String getValue() {
		return value;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setPos(int pos) {
		this.pos = pos;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
