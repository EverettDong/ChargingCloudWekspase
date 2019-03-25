package com.cpit.icp.pregateway.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cpit.icp.pregateway.message.dto.ChargeDto;

public class MessageInfo {
	// 上行报文
	public static String type1 = "1";
	// 下行报文
	public static String type2 = "2";
	
	// 0x05,0x6c下行报文重发次数 最大10次
	public static int reSendNum = 5;
	// 0x05,0x6c下行报文重发缓存时间 最大20秒
	public static int reSendRedis = 20;

	// 桩默认最大数据流量 默认500M
	public static int deviceMsgThreshold = 600;

	public static String errorMsg = "报文格式不正确";
	public static String successMsg = "报文格式正确";
	public static String errorInfo = "没有错误";

	public static String validate_head = "报文协议起始域不正确";
	public static String validate_version = "报文版本域不正确";
	public static String validate_id = "报文协议不正确";
	public static String validate_lenght = "报文长度不正确";
	public static String validate_lenghtDomain = "报文长度域不正确";
	public static String validate_checkSum = "检验和不正确";

	public static boolean heat = false;

	public static boolean heat2 = false;
    //充电报文
	public static String msg_0x73 = "0x73";
	public static String msg_0x78 = "0x78";
	public static String msg_0x79 = "0x79";
	public static String msg_0x7D = "0x7D";
	//重发报文对应的回复报文
	public static String msg_0x15 = "0x15";
	public static String msg_0x7A = "0x7A";
	
	public static byte [] msg_0x48 = {0x48};
	
	public static String underline = "_";
	
	public static String msg_version34 = "0x80";
    //key code;value ip_port
	public static Map<String, String> iptab = new HashMap<String, String>();
	//key ip_port;value code
	public static Map<String, String> ipcode = new HashMap<String, String>();
	//0x05重发计数器 key code_command_idandSN
	public static Map<String, Integer> msgReSendMap = new HashMap<String, Integer>();
	//0x6C重发计数器 key code_idandSN
	public static Map<String, Integer> msgMap6C = new HashMap<String, Integer>();
   

	public static Map<String, ChargeDto> charge = new HashMap<String, ChargeDto>();

	public static List<String> Storage = new ArrayList<String>();

	// 充电状态报文 流量超界也要上报
	public static List<String> StatesMsg = new ArrayList<String>();

	public static List<String> protocol = new ArrayList<String>();
	//非法设备编码
	public static List<String> errorCodeList=new ArrayList<String>();
	
	

	public static boolean isNull(String str) {
		if (str != null && str.length() != 0) {
			return false;
		}
		return true;
	}

}