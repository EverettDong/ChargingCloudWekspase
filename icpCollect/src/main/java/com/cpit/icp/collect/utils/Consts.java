package com.cpit.icp.collect.utils;

public class Consts {
	public static int REGISTER_CODE = 1;
	public static int HEART_CODE = 2;
	public static int REQUEST_CODE = 3;//设备请求报文，需要回复
	public static int REPORT_CODE=4;//设备主动上报报文，不需要回复
	
	
	public static char TERMINAL_FLAG = '0';
	public static long WAITTHREHOLD = 3*60*10000;
	public static byte WRONG_FLAG = (byte) 0xFF;
	public static char TERM_DEL_FLAG = '0';//0 not del;1 deleted
	
	
	public static final String FILENAME_V_34="34";
	public static final String FILENAME_V_35="35";
	public static final String VERSION_3_1 ="3.1";
	public static final String VERSION_3_2 ="3.2";
	public static final String VERSION_3_4 ="3.4";
	public static final String VERSION_3_5 ="3.5";
	
	public static final String TERMINAL_DISCONNECT ="0";
	
	public static final int TEST_PHRASE_1 =1;
	public static final int TEST_PHRASE_2 =3;
	public static final int TEST_PHRASE_3 =3;
	
	public static final String CODE_MODE ="GB2312";
	public static final int PHRASE =3;
	public static final String ST_NEEDREPLY34 = "ST_NR34";
	
//	public static final String CP_STATUS_OFF ="0";
//	public static final String CP_STATUS_FREE ="1";
//	public static final String CP_STATUS_CHARGING ="3";
//	public static final String CP_STATUS_ORDER ="4";
//	public static final String CP_STATUS_ERROR ="255";
	

	public static final String CHARGE_SRC_CARD="1";
	public static final String CHARGE_SRC_APP="2";
	public static final String CHARGE_SRC_HLHT="3";
	
	public static final long TIME_GAP=120000;//ms
	
	
	public static final String hlht_charge_status_1 ="1";//启动中
	public static final String hlht_charge_status_2 ="2";//充电中
	public static final String hlht_charge_status_3 ="3";//停止中 
	
	public static final String hlht_methodName_auth="auth";
	public static final String hlht_methodName_qu_buss="quBuss";
	public static final String hlht_methodName_qu_start="quStart";
	public static final String hlht_methodName_qu_status="quStatus";
	public static final String hlht_methodName_qu_stop="quStop";
	
	
	
	
	
}
