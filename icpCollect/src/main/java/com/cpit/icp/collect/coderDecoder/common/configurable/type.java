package com.cpit.icp.collect.coderDecoder.common.configurable;

import java.text.SimpleDateFormat;
import java.util.List;

import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;

/**
 * use for decode
 * 数据类型处理
 * @author maming
 * 避免与基本类型冲突，所有类型加上前缀_
 */
public class type {

	/**
	 * 16进制字符集
	 */
	private static char[] cHEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 字节整型转成字符串
	 * @param bsrc
	 * @param str
	 * @return
	 */
	//充电类型
	public static String _chargeType(byte[] bsrc,String chineseEncoding) {
		switch (bsrc[0]) {
			case 1:
				return "直流";
			case 2:
				return "交流";		
			default:
				return "状态的值不在范围内";
		}
	}
	//故障等级
	public static String _faultRank(byte[] bsrc,String chineseEncoding) {
		int rank;
		if(bsrc.length==2){
			rank=CommFunction.byteArrayToUnsignInt(bsrc);
		}else{
			rank=bsrc[0];
		}
		switch (rank) {
			case 0:
				return "无告警";
			default:
				return CommFunction.bytesToHexString(bsrc);
		}
	}
	//分组标识
	public static String _groupID(byte[] bsrc,String chineseEncoding) {
		switch (bsrc[0]) {
			case -1:
				return "结束标识：0xff";
			case 85:
				return "电池故障代码标识：0x55";
			case 102:
				return "电池系统故障代码标识：0x66";
			case 119:
				return "BMS故障代码标识：0x77";
			default:
				return "分组标识";
		}
	}

	public static String _authPro(byte[] bsrc,String chineseEncoding) {
		StringBuffer sb = new StringBuffer();
		String str =_bit(bsrc,chineseEncoding);
		//if("1".equals(str.substring(0, 1)))sb.append("预留;");
		//if("1".equals(str.substring(1, 2)))sb.append("预留;");
		if("1".equals(str.substring(2, 3)))sb.append("二维码信息认证,");
		if("1".equals(str.substring(3, 4)))sb.append("服务类型认证,");
		if("1".equals(str.substring(4, 5)))sb.append("电池认证,");
		if("1".equals(str.substring(5, 6)))sb.append("车VIN认证,");
		if("1".equals(str.substring(6, 7)))sb.append("车牌认证,");
		if("1".equals(str.substring(7, 8)))sb.append("第三方平台用户认证,");
		if(sb.length() ==0){
			return "" ;
		}else{
		return sb.toString().substring(0,sb.length()-1);
		}
	}
/*
 * 2019-02-19 add, according to protocal(v3.4a)
 * */

	public static String _cdqtype34(byte[] bsrc,String chineseEncoding) {
		StringBuffer sb = new StringBuffer();
		String str =_bit(bsrc,chineseEncoding);
		char[] chars = str.toCharArray();
		sb.append("充电枪枪号-"+Integer.parseInt(str.substring(2, 6), 2)+"_");
		if (chars[6]== '1' &&chars[7] == '1') {
			sb.append("充/放电类型-放电_");
		}else if (chars[6]== '1' &&chars[7] == '0') {
			sb.append("充/放电类型-交流充电_");
		}else if (chars[6]== '0' &&chars[7] == '1') {
			sb.append("充/放电类型-直流充电_");
		}
		if (chars[0]== '0' ) {
			sb.append("充电设备工作模式-单枪工作");
		}else if (chars[0]== '1' ) {
			sb.append("充电设备工作模式-并枪工作");
		}
		
		return sb.toString();
	}
/**
 * user for 3.5
 * @param bsrc
 * @param chineseEncoding
 * @return
 */
	public static String _cdqtype(byte[] bsrc,String chineseEncoding) {
		StringBuffer sb = new StringBuffer();
		String str =_bit(bsrc,chineseEncoding);
		char[] chars = str.toCharArray();
		sb.append("充电枪枪号-"+Integer.parseInt(str.substring(2, 6), 2)+"_");
		if (chars[6]== '1' &&chars[7] == '1') {
			sb.append("充/放电类型-放电");
		}else if (chars[6]== '1' &&chars[7] == '0') {
			sb.append("充/放电类型-交流充电");
		}else if (chars[6]== '0' &&chars[7] == '1') {
			sb.append("充/放电类型-直流充电");
		}
		else if (chars[6]== '0' &&chars[7] == '0') {
			sb.append("充/放电类型-放电结束");
		}
		return sb.toString();
	}
	
	/**
	 * user for 3.5
	 * @param 侧充电枪状态 0x3A 
	 * Bit0 定义:电子锁锁止反馈（充电枪与车辆侧）1=枪锁止，即枪在；0=枪未锁止
	 * @param chineseEncoding
	 * @return
	 */
		public static String _cecdqtype(byte[] bsrc,String chineseEncoding) {
			StringBuffer sb = new StringBuffer();
			String str =_bit(bsrc,chineseEncoding);
			char[] chars = str.toCharArray();
		
			if (chars[7] == '1') {
				sb.append("枪锁止，即枪在");
			}else if(chars[7] == '0') {
				sb.append("枪未锁止");
			}
			
			return sb.toString();
		}
	public static String _cdstatetype(byte[] bsrc,String chineseEncoding) {
		StringBuffer sb = new StringBuffer();
		String str =_bit(bsrc,chineseEncoding);
		char[] chars = str.toCharArray();
		if (chars[3]== '0' &&chars[2] == '0') {
			sb.append("编码_充电连接器温度正常");
		}else if (chars[3]== '0' &&chars[2] == '1') {
			sb.append("编码_充电连接器过温");
		}else if (chars[3]== '1' &&chars[2] == '0') {
			sb.append("编码_不可信");
		}
		if(chars[6]=='1'){
			sb.append(",充电枪推入到位");
		}
		if(chars[7]=='1'){
			sb.append(",充电枪推入不到位");
		}
		return sb.toString();
	}
	

	/**
	 * 0x31充电系统总体状态用
	 * @param bsrc
	 * @param chineseEncoding
	 * @return
	 */
	public static String _cdxtzt(byte[] bsrc,String chineseEncoding) {
		StringBuffer sb = new StringBuffer();
		String str =_bit(bsrc,chineseEncoding);
		char[] chars = str.toCharArray();

		if (chars[0] == '0'&&chars[1] == '0'&&chars[2] == '0'&&chars[3] == '0') {
			sb.append("独立工作,");
		};
		if (chars[0] == '0'&&chars[1] == '0'&&chars[2] == '0'&&chars[3] == '1') {
			sb.append("开机工作,");
		};
		if (chars[4] == '0') {
			sb.append("充电枪归位,");
		};
		if (chars[4] == '1') {
			sb.append("充电枪未归位,");
		};
		if (chars[5] == '0') {
			sb.append("桩位空闲,");
		};
		if (chars[5] == '1') {
			sb.append("桩位占用,");
		};
		if (chars[6] == '0') {
			sb.append("门关闭,");
		};
		if (chars[6] == '1') {
			sb.append("门开,");
		};	
		if (chars[7] == '0') {
			sb.append("正常");
		};
		if (chars[7] == '1') {
			sb.append("故障");
		};		
		return sb.toString();
	}

	/**
	 * 0x31充电系统总体状态用 for 3.4
	 * @param bsrc
	 * @param chineseEncoding
	 * @return
	 */
	public static String _cdxtzt34(byte[] bsrc,String chineseEncoding) {
		StringBuffer sb = new StringBuffer();
		String str =_bit(bsrc,chineseEncoding);
		char[] chars = str.toCharArray();

		if (chars[0] == '0'&&chars[1] == '0'&&chars[2] == '0'&&chars[3] == '0') {
			sb.append("单枪工作,");
		};
		if (chars[0] == '0'&&chars[1] == '0'&&chars[2] == '0'&&chars[3] == '1') {
			sb.append("单机并枪工作,");
		};
		if (chars[0] == '0'&&chars[1] == '0'&&chars[2] == '1'&&chars[3] == '1') {
			sb.append("多机并枪工作,");
		};
		if (chars[5] == '0') {
			sb.append("桩位空闲,");
		};
		if (chars[5] == '1') {
			sb.append("桩位占用,");
		};
		if (chars[6] == '0') {
			sb.append("门关闭,");
		};
		if (chars[6] == '1') {
			sb.append("门开,");
		};	
		if (chars[7] == '0') {
			sb.append("正常");
		};
		if (chars[7] == '1') {
			sb.append("故障");
		};		
		return sb.toString();
	}


	public static String _process(byte[] bsrc,String chineseEncoding) {
		StringBuffer sb = new StringBuffer();
		String str =_bit(bsrc,chineseEncoding);
		if("1".equals(str.substring(2, 3)))sb.append("打印凭条;");
		if("1".equals(str.substring(3, 4)))sb.append("结算;");
		if("1".equals(str.substring(4, 5)))sb.append("充电枪/充电舱门锁紧;");
		if("1".equals(str.substring(5, 6)))sb.append("断开充电线缆;");
		if("1".equals(str.substring(6, 7)))sb.append("充电结束刷卡密码;");
		if("1".equals(str.substring(7, 8)))sb.append("充电结束刷卡;");
		if("1".equals(str.substring(8, 9)))sb.append("充电;");
		if("1".equals(str.substring(9, 10)))sb.append("充电模式选择;");
		if("1".equals(str.substring(10, 11)))sb.append("连接充电线缆;");
		if("1".equals(str.substring(11, 12)))sb.append("充电枪/充电舱门解锁;");
		if("1".equals(str.substring(12, 13)))sb.append("输入车牌;");
		if("1".equals(str.substring(13, 14)))sb.append("充电/查询服务选择;");
		if("1".equals(str.substring(14, 15)))sb.append("输入密码;");
		if("1".equals(str.substring(15, 16)))sb.append("刷卡/插卡;");
		return sb.toString();
	}




	public static String _bit(byte[] bsrc,String chineseEncoding) {
		StringBuffer sb = new StringBuffer();
		bsrc = CommFunction.reserveByteArray(bsrc);
		for (int i = 0; i < bsrc.length; i++) {
			sb.append(CommFunction.byteToBit(bsrc[i]));
		}
		return sb.toString();
	}
	/**
	 * 年 转化
	 * @param bsrc
	 * @param chineseEncoding
	 * @return
	 */
	public static String _ybyte(byte[] bsrc,String chineseEncoding) {
		byte[] byear = CommFunction.reserveByteArray(bsrc);
		int iyear = CommFunction.byteArrayToUnsignInt(byear);
		return String.valueOf(iyear);
	}
	/**
	 * month day hour min sec 
	 * @param bsrc
	 * @param chineseEncoding
	 * @return
	 */
	public static String _mbyte(byte[] bsrc,String chineseEncoding) {
		return String.valueOf(CommFunction.unsignByteToInt(bsrc[0]));
	}

	public static String _byte(byte[] bsrc,String chineseEncoding) {
		return CommFunction.byteArrayToHexStr(bsrc);
	}

	public static String _int(byte[] bsrc,String chineseEncoding) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		Integer result = CommFunction.byteArrayToInt(bsrc);
		return String.valueOf(result);
	}

	public static String _hex(byte[] bsrc,String chineseEncoding) {
		//byte[] bfault = CommFunction.reserveByteArray(bsrc);
		return CommFunction.bytesToHexString(bsrc);
	}

	public static String _AHint(byte[] bsrc,String chineseEncoding) {

		return MessgeTransfer.bytesTostr(bsrc) + "AH";
	}

	public static String _Vint(byte[] bsrc,String chineseEncoding) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = CommFunction.byteArrayToUnsignInt(bsrc);
		StringBuilder sbuilder = new StringBuilder();
		if (isr == 65535) {
			sbuilder.append("缺省值FF");
		} else {
			sbuilder.append(((float) isr) / 10f);
		}
		sbuilder.append("V");
		return sbuilder.toString();
	}

	public static String _Aint(byte[] bsrc,String chineseEncoding) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = CommFunction.byteArrayToUnsignInt(bsrc);
		isr = isr - 3200;
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(((float) isr) / 10f);
		sbuilder.append("A");
		return sbuilder.toString();
	}

	// 千瓦时的字节转成字符串：即：kwh
	public static String _kwhint(byte[] bsrc,String chineseEncoding) {
		StringBuilder sbuilder = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		float result = 0;
		if (bsrc.length == 2) {
			int iresult = CommFunction.byteArrayToUnsignInt(bsrc);
			result = (float) iresult;
		} else if (bsrc.length == 4) {
			long lresult = bytes2long(bsrc);
			result = (float) lresult;
		}
		sbuilder.append(result / 100f);
		//sbuilder.append("kwh");
		return sbuilder.toString();
	}

	public static String _socint(byte[] bsrc,String chineseEncoding) {
		StringBuilder sbuilder = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		float result = 0;
		if (bsrc.length == 2) {
			int iresult = CommFunction.byteArrayToUnsignInt(bsrc);
			result = (float) iresult;
		} else if (bsrc.length == 4) {
			long lresult = bytes2long(bsrc);
			result = (float) lresult;
		}
		sbuilder.append(result / 100f);
		return sbuilder.toString();
	}

	// 版本类字节转成字符串：int 表示占几个字节的版本
	public static String _version(byte[] bsrc,String chineseEncoding) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append("V");
		Integer[] isoftversion = new Integer[4];
		for (int i = 0; i < bsrc.length; i++) {
			isoftversion[i] = CommFunction.unsignByteToInt(bsrc[i]);
			sBuilder.append(isoftversion[i].toString());
			if (i < bsrc.length - 1) {
				sBuilder.append(".");
			}
		}
		return sBuilder.toString();
	}

	// 模块故障
	public static String _ModuleFault(byte[] bsrc,String chineseEncoding){
		List<String> sFault = MessgeTransfer.moduleFaultAnalyze(bsrc, 1);
		StringBuilder sBuilder = new StringBuilder();
		for(String ss:sFault){
			
		sBuilder.append(ss).append("#");
			
		};
	
		return sBuilder.toString();
	}

	//系统故障
	public static String _SystemFault(byte[] bsrc,String chineseEncoding){
		String[] sb=MessgeTransfer.SystemFaultbyteToHexStr(bsrc,1);
		
		
		List<String> sFault = MessgeTransfer.systemFaultToStr(bsrc,1);
		StringBuilder sBuilder = new StringBuilder();
		for(String ss:sFault){
			
		sBuilder.append(ss).append("#");
			
		};
	
		return sBuilder.toString();
	//	return sb[0].toString()+"#"+sb[1].toString();
	}

	//电池系统
	public static String _BatterySystemFault(byte[] bsrc,String chineseEncoding){
		//return MessgeTransfer.BatterySystemFaultbyteToHexStr(bsrc,1)[1].toString();
	
		List<String> sFault = MessgeTransfer.batterySystemFaultbyteToHexStr(bsrc,1);
		StringBuilder sBuilder = new StringBuilder();
		for(String ss:sFault){
			
		sBuilder.append(ss).append("#");
			
		};
	
		return sBuilder.toString();
	}

	//充电系统故障
	public static String _ChargeSystemFault(byte[] bsrc,String chineseEncoding){
	//	return MessgeTransfer.ChargeSystemFaultbyteToHexStr(bsrc,1)[1].toString();
		List<String> sFault = MessgeTransfer.chargeFaultToStr(bsrc,1);
		StringBuilder sBuilder = new StringBuilder();
		for(String ss:sFault){
			
		sBuilder.append(ss).append("#");
			
		};
	
		return sBuilder.toString();
	}

	//车况报警状态
	public static String _StateFault(byte[] bsrc,String chineseEncoding){
		return MessgeTransfer.StateFaultbyteTointStr(bsrc)[1].toString();
	}

	//电池系统故障
	public static String _BatterySystemFaultToBitStr(byte[] bsrc,String chineseEncoding){
		return MessgeTransfer.BatterySystemFaultToBitStr(bsrc)[1].toString();
	}
	
		
	//电池故障
	public static String _BatteryFault(byte[] bsrc,String chineseEncoding){
		return MessgeTransfer.BatteryFaultToBitStr(bsrc)[1].toString();
	}

	//ＢＭＳ故障代码
	public static String _BMSFault(byte[] bsrc,String chineseEncoding){
		return MessgeTransfer.BMSFaultToBitStr(bsrc)[1].toString();
	}

	//整车故障
	public static String _WholeCarFault(byte[] bsrc,String chineseEncoding){
		return MessgeTransfer.WholeCarFaultToBitStr(bsrc)[1].toString();
	}


	// 温度类型的字节转成字符串：
	public static String _Tint(byte[] bsrc,String chineseEncoding) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = 0;

		if (bsrc.length == 1) {
			Byte by = bsrc[0];
			if (by.equals((byte) 0xFF)) {
				return "无温度传感器";
			}
			isr = CommFunction.unsignByteToInt(by);
		} else if (bsrc.length == 2) {
			Byte by = bsrc[0];
			Byte by1 = bsrc[1];
			if (by.equals((byte) 0xFF) && by1.equals((byte) 0xFF)) {
				return "FFFF";
			}
			isr = CommFunction.byteArrayToUnsignInt(bsrc);
		}
		isr = isr - 50;
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(isr);
		sbuilder.append("℃");
		return sbuilder.toString();
	}

	/**
	 * 字节数组转成Hex字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String _hexprocess(byte[] data,String chineseEncoding) {
		StringBuilder result = new StringBuilder();
		result.setLength(0);
		for (int i = 0; i < data.length; i++) {
			byte tmp = (byte) (data[i] & 0xFF);
			char hi = cHEX[((tmp & 0xF0) >>> 4)];// 高4位
			char low = cHEX[(tmp & 0x0F)];// 低4位

			result.append(hi);
			result.append(low);
		}
		return result.toString();
	}

	// 单休电压类型的字节转成字符串
	public static String _sVint(byte[] bsrc,String chineseEncoding) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = CommFunction.byteArrayToUnsignInt(bsrc);
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(((float) isr) / 100f);
		sbuilder.append("V");
		return sbuilder.toString();
	}
	
	public static String _sVint2(byte[] bsrc,String chineseEncoding) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = CommFunction.byteArrayToUnsignInt(bsrc);
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(((float) isr) / 1000f);
		sbuilder.append("V");
		return sbuilder.toString();
	}



	/**
	 * 字节转化成Ascii字符
	 * 
	 * @param b
	 * @return char
	 */
	public static char byteToAsciiChar(byte b) {
		return (char) (unsignByteToInt(b));
	}

	/**
	 * 无符号byte转成int
	 * 
	 * @param data
	 * @return int
	 */
	public static int unsignByteToInt(byte data) {
		int result = 0;

		int hightestBit = ((data >>> 7) & 0x01);
		result = (data & 0x7F);
		if (1 == hightestBit) {
			result = result | 0x80;
		}
		return result;

	}

	public static long bytes2long(byte[] b) {
		long temp = 0;
		long res = 0;
		for (int i = 0; i < 4; i++) {
			res <<= 8;
			temp = b[i] & 0xff;
			res |= temp;
		}
		return res;
	}

	public static String _carstr(byte[] bsrc,String chineseEncoding) {
		return MessgeTransfer.carCodebyteT0str(bsrc,chineseEncoding);
	}	

	public static String _ip(byte[] bsrc,String chineseEncoding) {
		return MessgeTransfer.IpbyteToIntstr(bsrc);
	}
	public static String _GB(byte[] bsrc,String chineseEncoding) throws Exception {
		return MessgeTransfer.byteToGB(bsrc,chineseEncoding);
	}	
	public static String _ascII(byte[] src,String chineseEncoding){
		char[] cs = CommFunction.bytesToAscii(src);
		return new String(cs);
	}
	
	public static String _ascIIcodeMode(byte[] src,String chineseEncoding)throws Exception{
		return MessgeTransfer.byteToGB(src,chineseEncoding);
	}
	public static String _date(byte[] bsrc,String chineseEncoding) throws Exception {
		return MessgeTransfer.DatebyteTostr(bsrc);
	}	

	public static String _time(byte[] bsrc,String chineseEncoding) throws Exception {
		return MessgeTransfer.TimebyteTostr(bsrc);
	}	
	public static String _timeN(byte[] bsrc,String chineseEncoding) throws Exception {
		String old= MessgeTransfer.TimebyteTostr(bsrc);
		String inputPattern = "yyyy-MM-dd HH-mm-ss";
		String outPattern = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat formatIn = new SimpleDateFormat(inputPattern);
		SimpleDateFormat formatOut = new SimpleDateFormat(outPattern);

		return formatOut.format(formatIn.parse(old));
	}	
	public static String _timeStamp(byte[] bsrc,String chineseEncoding) throws Exception {
		return MessgeTransfer.TimeStampbyteTostr(bsrc);
	}
	public static String _ascIITime(byte[] src,String chineseEncoding){
		char[] cs = CommFunction.bytesToAscii(src);
		return MessgeTransfer.asciiTimeStampbyteTostr(cs);
	}

	// 电压/功率类型的字节转成字符串：这里的电压是普通电压：
	public static String _Wint(byte[] bsrc,String chineseEncoding) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = CommFunction.byteArrayToUnsignInt(bsrc);
		StringBuilder sbuilder = new StringBuilder();
		if (isr == 65535) {
			sbuilder.append("缺省值FF");
		} else {
			sbuilder.append(((float) isr) / 10f);
			sbuilder.append("kw");
		}
		return sbuilder.toString();
	}
	public static String _parameterType31(byte[] bsrc,String chineseEncoding) {
		switch (bsrc[0]) {
		case 2:
			return "设置充电机启停";
		case 3:
			return "设置输出功率";
		case 4:
			return "设置单体最高允许电压";
		case 5:
			return "设置单体最高允许温度";
		
		default:
			return "参数类型错误";
		}
	}
	public static String _parameterType(byte[] bsrc,String chineseEncoding) {
		switch (bsrc[0]) {
		case 2:
			return "设置充电机启停";
		case 3:
			return "设置输出功率";
		case 4:
			return "设置单体最高允许电压";
		case 5:
			return "设置单体最高允许温度";
		case 6:
			return "设置语音提示功能启停";
		case 7:
			return "设置充电设备服务启停";
		case 8:
			return "设置充电接口温度监控停机保护值";
		case 9:
			return "：设置充电设备电价执行方式";
		case 10:
			return "设置结算界面是否显示结算电费";
		case 11:
			return "设置智能终端主动与中心平台断开连接，并按照设置的恢复连接时间重新连接中心平台";
		default:
			return "参数类型";
		}
	}
	public static String _controlType(byte[] bsrc,String chineseEncoding, int parameteType) {
		int value = Integer.parseInt(type._int(bsrc,chineseEncoding));
		if(value==0){
			return "设置失败";
		}
		if(parameteType==2){
			if(value==1) return "充电启动";
			else if(value==2)  return "充电停止";
			else if(value==3) return "紧急停止";
			else if(value==4)  return "放电启动";
			else if(value==5)  return "放电停止";
		}
		else if(parameteType==3){
			return _Wint(bsrc,chineseEncoding);
		}
		else if(parameteType==4){
			return _sVint2(bsrc,chineseEncoding);
		}
		else if(parameteType==5){
			return _Tint(bsrc,chineseEncoding);
		}
		else if(parameteType==6){
			if(value==1) return "开启语音提示功能";
			else if(value==2)  return "关闭语音提示功能";
		}
		else if(parameteType==7){
			if(value==1) return "开启充电服务功能";
			else if(value==2)  return "关闭充电服务功能";
		}
		else if(parameteType==8){
			return _Tint(bsrc,chineseEncoding);
		}
		else if(parameteType==9){
			if(value==1) return "单一电价";
			else if(value==2)  return "尖峰平谷定价";
			else if(value==3) return "峰平谷定价";
		}
		else if(parameteType==10){
			if(value==1) return "显示结算信息";
			else if(value==2)  return "不显示结算信息";
		}
		else if(parameteType==11){
			return _int(bsrc,chineseEncoding);
		}
		return "参数类型";
	}
	public static String _controlType34(byte[] bsrc,String chineseEncoding, int parameteType,int num) {
		int value = Integer.parseInt(type._int(bsrc,chineseEncoding));
		if(num==0){
			if(value==2)  return "紧急停止";
		}
		else{
			if(parameteType==2){
				if(value==1) return "启动";
				else if(value==2)  return "停止";
			}
		
			else if(parameteType==3){
				return _Wint(bsrc,chineseEncoding);
			}
			else if(parameteType==4){
				return _sVint2(bsrc,chineseEncoding);
			}
			else if(parameteType==5){
				return _Tint(bsrc,chineseEncoding);
			}
			else if(parameteType==6){
				if(value==1) return "开启语音提示功能";
				else if(value==2)  return "关闭语音提示功能";
			}
			else if(parameteType==7){
				if(value==1) return "开启充电服务功能";
				else if(value==2)  return "关闭充电服务功能";
			}
		}
		return "参数类型";
	}
	
}

