package com.cpit.icp.collect.coderDecoder.util;





import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.collect.service.MonDBService;


//import com.cpit.icp.collect.coderDecoder.util.configurable.ConfigTool;

/*=======================================================================================
 CommFunction.bytesToAscii(char[] chs);
 CommFunction.hexStringToBytes(String hexstr);
 CommFunction.byteArrayToHexStr(byte[] b);   //充电桩编码，充电机编码；
 CommFunction.unsignByteToInt(byte b)  //一个字节转成整数
 CommFunction.byteToHexStr(byte b)　 //一个字节转换成16进制的数。
 * ======================================================================================
 * 设备发的报文有:0x10,0x11,0x15,0x17,0x18,0x31,0x34,0x35,0x36,0x39,0x3A,0x3B,0x50,0x51,0x52,0x53,0x56,0x58,0x59,0x70,0x71,0x72,0x74,0x75,
 *  0x78,0x92,0x96,0x97,0x98,0xD1,0xD2,0xF0,0xF1,0xF2
 *  中心发的报文有"01 05  07  08  09 21 22  23 24   25  26  40 41 42 43 46 48 49 61 62   64 65 68 69  80 86 87 88 C1 C2 E0 E1 E2 ;
 *                4  771 300 300 12 4  300 300 300 300 301 18 24 12  5 21 6  6  64 2309 68 38 142 283 7 23 23 23 88 4  6  6  7
 * ======================================================================================
 */
public class MessgeTransfer {
	private final static Logger logger = LoggerFactory.getLogger(MessgeTransfer.class);
	// 编码函数:
	// Int型字符串转成byte数组:即占两个字节的10进制字符串
	public static byte[] IntstrTobytes(String str) {
		Integer i = Integer.parseInt(str);
		String hexStr = i.toHexString(i);
		hexStr = CommFunction.padLeft(hexStr, 4, "0");
		byte[] b = CommFunction.hexStringToBytes(hexStr);
		return CommFunction.reserveByteArray(b);
	}

	// 电流类型字符串转成byte数组:即占两个字节的10进制字符串
	public static byte[] kwhIntstrTobytes(String str) {
		if (str.contains(".")) {
			str = str.replace(".", "");
		}
		Long i = Long.parseLong(str);
		// i=i*100;
		String hexStr = i.toHexString(i);
		hexStr = CommFunction.padLeft(hexStr, 4, "0");
		byte[] b = CommFunction.hexStringToBytes(hexStr);
		return CommFunction.reserveByteArray(b);
	}

	// 占一个字节的10进制字符串转成byte:
	public static byte strTobytes(String str) {
		Integer i = Integer.parseInt(str);
		String hexStr = i.toHexString(i);
		return CommFunction.hexStringToByte(hexStr);
	}

	// 占8个字节的16进制编码字符串转成byte数组:充电机编码
	public static byte[] eightstrTobytes(String hexstr) {
		return CommFunction.hexStringToBytes(hexstr);
	}

	// 占n个字节的16进制编码字符串转成byte数组:充电机编码
	public static byte[] mulitystrTohexbytes(String hexstr, int size) {
		return CommFunction.hexStringToBytes(hexstr, size);
	}

	// 占４个字节的10进制编字符串转成byte数组；
	public static byte[] longstrTobytes(String str) {
		Long l = Long.parseLong(str);
		String hexStr = l.toHexString(l);
		hexStr = CommFunction.padLeft(hexStr, 4, "0");
		byte[] b = CommFunction.hexStringToBytes(hexStr, 4);
		return CommFunction.reserveByteArray(b);
	}

	public static byte[] longstr2Tobytes(String str, int size) {
		Long l = Long.parseLong(str);
		String hexStr = l.toHexString(l);
		hexStr = CommFunction.padLeft(hexStr, size, "0");
		byte[] b = CommFunction.hexStringToBytes(hexStr, size);
		return CommFunction.reserveByteArray(b);
	}

	// 占４个字节的10进制编字符串转成byte数组；
	public static byte[] kwhlongstrTobytes(String str) {
		Float f = Float.parseFloat(str);
		f = f * 100;
		Long l = f.longValue();
		String hexStr = l.toHexString(l);
		hexStr = CommFunction.padLeft(hexStr, 4, "0");
		byte[] b = CommFunction.hexStringToBytes(hexStr, 4);
		return CommFunction.reserveByteArray(b);
	}

	// 占多个字节的金额类字符串转成byte数组；
	public static byte[] AmountstrTobytes(String amountStr, int num) {
		if (num <= 0 || amountStr == null || amountStr.length() <= 0) {
			return null;
		}
		/*
		 * String tmp = amountStr; if (tmp.length() > num) { tmp =
		 * tmp.substring(0, num); } else if (tmp.length() < num) { int s = num -
		 * tmp.length(); String sub = ""; for (int i = 0; i < s; i++) { sub +=
		 * " "; } tmp = asciiStr+sub; }
		 */
		int length = amountStr.length();
		byte[] result = new byte[num];
		for (int i = 0; i < length; i++) {
			result[i] = CommFunction.asciiCharToByte(amountStr.charAt(i));
		}
		return result;
	}

	// 时间类型的字符串转换成byte数组；
	/*
	 * public static byte[] TimeTobytes(String str) { byte[] btime = new
	 * byte[8]; if(str==null||str.equalsIgnoreCase("0")){ byte[] by=new byte[8];
	 * return by; } if(str.contains("-")){ String[] bstr=StringUtils.split(
	 * str,"-"); byte[] by = new byte[2]; String syear = bstr[0]; by =
	 * longstrTobytes(syear); ArraysN.copy(btime, 0, by, 0, 2); byte[] bdate =
	 * new byte[5]; for (int i = 1; i < 6; i++) { bdate[i-1]
	 * =strTobytes(bstr[i]); } ArraysN.copy(btime, 2, bdate, 0, 5); btime[7] =
	 * (byte) 0xFF; } else if(str.contains(":")){ String[]
	 * bstr=StringUtils.split( str,":"); byte[] by = new byte[2]; String syear =
	 * bstr[0]; by = longstrTobytes(syear); ArraysN.copy(btime, 0, by, 0, 2);
	 * byte[] bdate = new byte[5]; for (int i = 1; i < 6; i++) { bdate[i-1]
	 * =strTobytes(bstr[i]); } ArraysN.copy(btime, 2, bdate, 0, 5); btime[7] =
	 * (byte) 0xFF; } else{ byte[] by = new byte[2]; String syear =
	 * str.substring(0, 4); by = longstrTobytes(syear); ArraysN.copy(btime, 0,
	 * by, 0, 2); String sdate = str.substring(4, 14); byte[] bdate = new
	 * byte[5]; String[] strs = new String[5]; for (int i = 0; i < 5; i++) {
	 * strs[i] = sdate.substring(i, i + 2); } if (strs != null) { for (int j =
	 * 0; j < 5; j++) { bdate[j] = strTobytes(strs[j]); } } ArraysN.copy(btime,
	 * 2, bdate, 0, 5); btime[7] = (byte) 0xFF; } return btime; }
	 */
	// 时间戳类型的字符串转换成byte数组(9字节)；
	public static byte[] Time2StampTobytes() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
		String formatStr = formatter.format(new Date());
		return TimeStampTobytes(formatStr);
	}

	public static byte[] DateTobytes(String str) {
		if (str == null || str.equalsIgnoreCase("0")) {
			byte[] by = new byte[4];
			return by;
		}
		str = str.replaceAll(" ", "-");
		str = str.replaceAll(":", "-");
		String[] ids = StringUtils.split(str, "-");
		byte[] by = new byte[2];
		String syear = ids[0];
		by = IntstrTobytes(syear);
		byte[] btime = new byte[4];
		ArraysN.copy(btime, 0, by, 0, 2);
		btime[2] = strTobytes(ids[1]);
		btime[3] = strTobytes(ids[2]);
		return btime;
	}

	public static byte[] TimeStampTobytes(String str) {
		if (str == null || str.equalsIgnoreCase("0")) {
			byte[] by = new byte[9];
			return by;
		}
		str = str.replaceAll(" ", "-");
		str = str.replaceAll(":", "-");
		String[] ids = StringUtils.split(str, "-");
		byte[] by = new byte[2];
		String syear = ids[0];
		by = IntstrTobytes(syear);
		byte[] btime = new byte[9];
		ArraysN.copy(btime, 0, by, 0, 2);
		byte[] bdate = new byte[5];
		for (int i = 1; i < 6; i++) {
			if (ids[i].length() == 1) {
				ids[i] = "0" + ids[i];
			}
		}
		if (ids != null) {
			for (int j = 0; j < 5; j++) {
				bdate[j] = strTobytes(ids[j + 1]);
			}
		}
		ArraysN.copy(btime, 2, bdate, 0, 5);
		byte[] bhm = IntstrTobytes(ids[6]);// 毫秒
		ArraysN.copy(btime, 7, bhm, 0, 2);
		return btime;
	}

	// 时间类型的字符串转换成byte数组(8字节)；
	public static byte[] TimeTobytes(String str) {

		if (str == null || str.equalsIgnoreCase("0") ||str.equalsIgnoreCase("")) {
			byte[] by = new byte[8];
			return by;
		}
		// str=str.replaceAll("-", "");
		str = str.replaceAll(" ", "-");
		str = str.replaceAll(":", "-");
		String[] ids = StringUtils.split(str, "-");
		byte[] by = new byte[2];
		String syear = ids[0];
		by = IntstrTobytes(syear);
		byte[] btime = new byte[8];
		ArraysN.copy(btime, 0, by, 0, 2);
		// String sdate = str.substring(4, 14);
		byte[] bdate = new byte[5];
		// String[] strs = new String[5];
		for (int i = 1; i < 6; i++) {
			// strs[i] = sdate.substring(k, k + 2);
			if (ids[i].length() == 1) {
				ids[i] = "0" + ids[i];
			}
			// k=k+2;
		}
		if (ids != null) {
			for (int j = 0; j < 5; j++) {
				bdate[j] = strTobytes(ids[j + 1]);
			}
		}
		ArraysN.copy(btime, 2, bdate, 0, 5);
		btime[7] = (byte) 0xFF;

		return btime;
	}

	// IP类型的字符串转成byte数组:1020
	/*
	 * public static byte[] IpstrTobytes(String str){ byte[] bip= new byte[4];
	 * String[] strs=new String[4]; for(int i=0;i<4;i++){
	 * strs[i]=str.substring(i,i+2); } if (strs != null) { for(int j=0;j<4;j++){
	 * bip[j]=strTobytes(strs[j]); } } return bip; }
	 */
	// IP类型的字符串转成byte数组:如:16.80.10.20
	public static byte[] IpstrTobytes(String str) {
		byte[] bIp = new byte[4];
		if (str.equalsIgnoreCase("0")) {
			str = "0.0.0.0";
		}
		String[] sIp = new String[4];
		while (str.contains(".")) {
			str = str.replace(".", ",");
		}
		sIp = str.split(",");
		if (sIp != null) {
			for (int j = 0; j < 4; j++) {
				bIp[j] = strTobytes(sIp[j]);
			}
		}
		return bIp;
	}

	public static byte[] StrToTimeStampbyte(String timeStamp) {
		byte[] TimeStamp = new byte[3];
		String[] TimeStamps = timeStamp.split("-");
		for (int i = 0; i < TimeStamps.length; i++) {
			TimeStamp[i] = strTobytes(TimeStamps[i]);
		}
		return TimeStamp;
	}

	public static byte[] secIpstrTobytes(String str) {
		byte[] bIp = new byte[4];
		if (str.equalsIgnoreCase("0")) {
			str = "0.0.0.0";
		}
		String[] sIp = new String[4];
		while (str.contains(".")) {
			str = str.replace(".", ",");
		}
		sIp = str.split(",");
		if (sIp != null) {
			for (int j = 0; j < 4; j++) {
				bIp[j] = strTobytes(sIp[j]);
			}
		}
		byte[] newIp = new byte[20];
		ArraysN.copy(newIp, 0, bIp, 0, 4);
		return newIp;
	}

	// GB2312 中汉字编码:
	public static byte[] isCS(String str) {
		byte[] bytes = new byte[2];
		if (str.length() == 0) {
			return bytes;
		}
		try {
			bytes = str.getBytes("GB2312");
		} catch (Exception e) {
		}
		return bytes;
	}

	public static byte[] isCS(String str, String ChineseEncoding) {
		if(ChineseEncoding.equalsIgnoreCase("UTF-8")) {
			return isCS_utf8(str);
		}else {
			byte[] bytes = new byte[2];
			if (str.length() == 0) {
				return bytes;
			}
			try {
				bytes = str.getBytes(ChineseEncoding);
			} catch (Exception e) {
			}
			return bytes;
		}
		
	}
	private static byte[] isCS_utf8(String str) {
		byte[] bytes = new byte[3];
		if (str.length() == 0) {
			return bytes;
		}
		try {
			bytes = str.getBytes("utf-8");
		} catch (Exception e) {
		}
		return bytes;
	}

	// 多字节的Assic字符串转成字节数组用:CommFunction.asciiStringToByteArray(String asciiStr,
	// int num);
	// 比如卡号，车辆ＶＩＮ
	// 车辆车牌编码转换成byte数组:
	public static byte[] CarCodeTobytes(String str,String codeMode) {
		byte[] bCarcode = new byte[8];
		if (str.length() == 0) {
			return bCarcode;
		}
		boolean bl = checkuserid(str);
		if (bl) {
			if(codeMode.equalsIgnoreCase("utf-8")) {
				byte[] bdistrictCode = new byte[3];
				String temstr = str.substring(0, 1);
				/*
				 * if(temstr.length()==1){ temstr=temstr+temstr; }
				 */
				bdistrictCode = CommFunction.reserveByteArray(isCS(temstr,codeMode));
				ArraysN.copy(bCarcode, 0, bdistrictCode, 0, bdistrictCode.length);
				byte[] bCodeContent = new byte[5];
				// str = str.substring(0,temstr.length());
				str = str.substring(1);
				int n = str.length();
				if (str.length() < 5) {
					for (int i = 0; i < (5 - n); i++) {
						str = str + " ";
					}
				}
				bCodeContent = CommFunction.asciiStringToByteArray(str, 5);
				ArraysN.copy(bCarcode, 3, bCodeContent, 0, 5);
			}else {
				byte[] bdistrictCode = new byte[2];
				String temstr = str.substring(0, 1);
				/*
				 * if(temstr.length()==1){ temstr=temstr+temstr; }
				 */
				bdistrictCode = CommFunction.reserveByteArray(isCS(temstr,codeMode));
				ArraysN.copy(bCarcode, 0, bdistrictCode, 0, bdistrictCode.length);
				byte[] bCodeContent = new byte[6];
				// str = str.substring(0,temstr.length());
				str = str.substring(1);
				int n = str.length();
				if (str.length() < 6) {
					for (int i = 0; i < (6 - n); i++) {
						str = str + " ";
					}
				}
				bCodeContent = CommFunction.asciiStringToByteArray(str, 6);
				ArraysN.copy(bCarcode, 2, bCodeContent, 0, 6);
			}
			
		} else {
			// byte[] bdistrictCode = new byte[2];
			// String temstr = str.substring(0, 2);
			// bdistrictCode = CommFunction.reserveByteArray(isCS(temstr));
			// ArraysN.copy(bCarcode, 0, bdistrictCode, 0, temstr.length());
			int m = str.length();
			if (str.length() < 8) {
				for (int j = 0; j < (8 - m); j++) {
					str = str + " ";
				}
			}
			byte[] bCodeContent = new byte[8];
			// str = str.substring(1);
			bCodeContent = CommFunction.asciiStringToByteArray(str, 8);
			ArraysN.copy(bCarcode, 0, bCodeContent, 0, 8);
		}
		return bCarcode;

	}

	/*
	 * =========================================================================
	 * = ============
	 * ==============================================================
	 * =======================
	 * ==================================================
	 * ===================================
	 */
	// 解码函数:
	// 版本类字节转成字符串:int 表示占几个字节的版本
	public static String VersionbyteTostr(byte[] bsrc) {
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

	public static String IpbyteToIntstr(byte[] bsrc) {
		StringBuilder sBuilder = new StringBuilder();
		// sBuilder.append("V");
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

	// 16进制字节转成10进制字符串:int 表示占几个字节
	public static String byteToMulityIntstr(byte[] bsrc, int size) {
		StringBuilder sBuilder = new StringBuilder();
		Integer[] isrcbyte = new Integer[size];
		bsrc = CommFunction.reserveByteArray(bsrc);
		for (int i = 0; i < size; i++) {
			isrcbyte[i] = CommFunction.unsignByteToInt(bsrc[i]);
			if (isrcbyte[i] < 10) {
				sBuilder.append(0);
			}
			sBuilder.append(isrcbyte[i].toString());
		}
		if (size == 8 && sBuilder.toString().equals("255255255255255255255255")) {
			sBuilder.delete(0, sBuilder.length());
			sBuilder.append("缺省值FFFFFFFFFFFFFFFF");
		}
		return sBuilder.toString();
	}

	// 16进制字节转成10进制字符串:int 表示占几个字节,不反转
	public static String byteToMulityIntstNoReverse(byte[] bsrc, int size) {
		StringBuilder sBuilder = new StringBuilder();
		Integer[] isrcbyte = new Integer[size];
		for (int i = 0; i < size; i++) {
			isrcbyte[i] = CommFunction.unsignByteToInt(bsrc[i]);
			if (isrcbyte[i] < 10) {
				sBuilder.append(0);
			}
			sBuilder.append(isrcbyte[i].toString());
		}
		if (size == 8 && sBuilder.toString().equals("255255255255255255255255")) {
			sBuilder.delete(0, sBuilder.length());
			sBuilder.append("缺省值FFFFFFFFFFFFFFFF");
		}
		return sBuilder.toString();
	}

	// 4字节的整型转换成10进制的字符串；这个是低字节在前，高字节在后:比如:3D 00 00 00 转换成61
	public static String[] byteToIntstr(byte[] bsrc, String str) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		Long result = bytes2long(bsrc);
		// String s=l.toString();
		// 对该字节的值做判断:
		if (str.equalsIgnoreCase("设置结果")) {
			if (result == 0) {
				sT2Builder.append("成功");
			} else if (result != 0) {
				sT2Builder.append("失败");
			} else {
				sError.append("设置结果不在范围内");
			}
		} else {
			sT2Builder.append(result);
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
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

	// 四字节的byte数据转成整型字符串
	public static String bytesToIntstr(byte[] bsrc) {
		StringBuilder sT2Builder = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		Long result = bytes2long(bsrc);
		sT2Builder.append(result);
		return sT2Builder.toString();
	}

	// 时间类型的字节转换成字符串:
	public static String TimebyteTostr(byte[] bsrc) {
		StringBuilder sBuilder = new StringBuilder();
		byte[] byear = new byte[2];
		ArraysN.copy(byear, 0, bsrc, 0, 2);
		byear = CommFunction.reserveByteArray(byear);
		int iyear = CommFunction.byteArrayToUnsignInt(byear);
		sBuilder.append(iyear);
		sBuilder.append("-");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[2]));
		sBuilder.append("-");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[3]));
		sBuilder.append(" ");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[4]));
		sBuilder.append("-");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[5]));
		sBuilder.append("-");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[6]));
		/*
		 * if (bsrc.length == 7) { sBuilder.append("-");
		 * sBuilder.append(CommFunction.unsignByteToInt((byte) 0xFF)); } else if
		 * (bsrc.length == 8) { sBuilder.append("-");
		 * sBuilder.append(CommFunction.unsignByteToInt(bsrc[7])); }
		 */
		return sBuilder.toString();
		
		
	}

	// 日期类型的字节转换成字符串:
	public static String DatebyteTostr(byte[] bsrc) {
		StringBuilder sBuilder = new StringBuilder();
		byte[] byear = new byte[2];
		ArraysN.copy(byear, 0, bsrc, 0, 2);
		byear = CommFunction.reserveByteArray(byear);
		int iyear = CommFunction.byteArrayToUnsignInt(byear);
		sBuilder.append(iyear);
		sBuilder.append("年");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[2]));
		sBuilder.append("月");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[3]));
		sBuilder.append("日");
		return sBuilder.toString();
	}

	// 时间戳类型的字节转换成字符串:
	public static String TimeStampbyteTostr(byte[] bsrc) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[0]));
		sBuilder.append("时");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[1]));
		sBuilder.append("分");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[2]));
		sBuilder.append("秒");
		return sBuilder.toString();
	}
	public static String asciiTimeStampbyteTostr(char[] bsrc) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(bsrc[0]);
		sBuilder.append("时");
		sBuilder.append(bsrc[1]);
		sBuilder.append("分");
		sBuilder.append(bsrc[2]);
		sBuilder.append("秒");
		return sBuilder.toString();
	}

	public static String TimeStampbyteTostr2(byte[] bsrc) {
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[0]));
		sBuilder.append("-");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[1]));
		sBuilder.append("-");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[2]));
		// sBuilder.append("-");
		return sBuilder.toString();
	}

	// 时间类型的字节转换成字符串:
	public static String Time97byteTostr(byte[] bsrc) {
		StringBuilder sBuilder = new StringBuilder();
		byte[] byear = new byte[2];
		ArraysN.copy(byear, 0, bsrc, 0, 2);
		byear = CommFunction.reserveByteArray(byear);
		int iyear = CommFunction.byteArrayToUnsignInt(byear);
		sBuilder.append(iyear);
		sBuilder.append("-");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[2]));
		sBuilder.append("-");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[3]));
		sBuilder.append(" ");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[4]));
		sBuilder.append(":");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[5]));
		sBuilder.append(":");
		sBuilder.append(CommFunction.unsignByteToInt(bsrc[6]));
		if (bsrc.length == 7) {
			sBuilder.append(":");
			sBuilder.append(CommFunction.unsignByteToInt((byte) 0xFF));
		} else if (bsrc.length == 8) {
			sBuilder.append(":");
			sBuilder.append(CommFunction.unsignByteToInt(bsrc[7]));
		}
		return sBuilder.toString();
	}

	// 2字节整型转成字符串
	public static String bytesTostr(byte[] bsrc) {
		StringBuilder sT2Builder = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		sT2Builder.append(CommFunction.byteArrayToUnsignInt(bsrc));
		return sT2Builder.toString();
	}

	// 2字节整型转成字符串
	public static String[] ShortbyteTostr(byte[] bsrc, String str) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		Integer result = CommFunction.byteArrayToUnsignInt(bsrc);
		// 对该字节的值做判断:
		if (str.contains("充电模块控制参数")) {
			if (result == 1) {
				sT2Builder.append("启动");
			} else if (result == 2) {
				sT2Builder.append("停止");
			} else {
				sError.append("充电模块控制参数的值不在范围内;");
				sT2Builder.append("充电模块控制参数的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("设置结果")) {
			if (result == 0) {
				sT2Builder.append("成功");
			} else if (result == 1) {
				sT2Builder.append("失败");
			} else {
				sError.append("设置结果不在范围内");
				sT2Builder.append("设置结果不在范围内");
			}
		} /*
			 * else if (str.equalsIgnoreCase("充电满策略参数")) { if (result == 1) {
			 * sT2Builder.append("自定义时间"); } else if (result == 2) {
			 * sT2Builder.append("自定义电度数"); } else if (result == 3) {
			 * sT2Builder.append("自定义金额"); } else if (result == 0) {
			 * sT2Builder.append("自然充电"); } else {
			 * sError.append("充电满策略参数不在范围内"); sT2Builder.append("充电满策略参数不在范围内");
			 * } }
			 */ else if (str.contains("输入电源谐波含量")) {
			if (result == 65535) {
				sT2Builder.append("无");
			} else {
				sT2Builder.append(result);
			}
		} else {
			if (result == 65535) {
				sT2Builder.append("缺省值FFFF");
			} else {
				sT2Builder.append(result);
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	// 电压/功率类型的字节转成字符串:这里的电压是普通电压:
	public static String VbyteTostr(byte[] bsrc) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = CommFunction.byteArrayToUnsignInt(bsrc);
		StringBuilder sbuilder = new StringBuilder();
		if (isr == 65535) {
			sbuilder.append("缺省值FF");
		} else {
			sbuilder.append(((float) isr) / 10f);
		}
		return sbuilder.toString();
	}

	// 千瓦时的字节转成字符串:即:kwh
	public static String KwhbyteTostr(byte[] bsrc) {
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

	// soc
	public static String SocbyteTostr(byte[] bsrc) {
		int isr = CommFunction.unsignByteToInt(bsrc[0]);
		// int isr=CommFunction.byteArrayToUnsignInt(bsrc);
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(((float) isr) / 100f);
		return sbuilder.toString();
	}

	// 单休电压类型的字节转成字符串
	public static String SingleVbyteTostr(byte[] bsrc) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = CommFunction.byteArrayToUnsignInt(bsrc);
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(((float) isr) / 100f);
		return sbuilder.toString();
	}

	// 电流类型的字节转成字符串:这里的电流是普通电流:
	public static String AbyteTostr(byte[] bsrc) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		int isr = CommFunction.byteArrayToUnsignInt(bsrc);
		isr = isr - 3200;
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append(((float) isr) / 10f);
		return sbuilder.toString();
	}

	// 温度类型的字节转成字符串:
	public static String TbyteTostr(byte[] bsrc) {
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
		return sbuilder.toString();
	}

	// 汽车牌类字节转换成string
	public static String carCodebyteT0str(byte[] bsrc,String codeMode) {
		if(codeMode.equalsIgnoreCase("gb2312")) {
			return carCodebyteT0str_gb2312(bsrc);
		}else if(codeMode.equalsIgnoreCase("utf-8")) {
			return carCodebyteT0str_utf8(bsrc);
		}else {
			return null;
		}
	}
	private static String carCodebyteT0str_utf8(byte[] bsrc) {
		String desstr = new String();
		StringBuilder sbuilder = new StringBuilder();
		byte[] bf = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF };
		if (Arrays.equals(bsrc, bf)) {
			return "FFFFFFFFFFFFFFFF";
		}
		byte[] bprovite = new byte[3];
		ArraysN.copy(bprovite, 0, bsrc, 0, 3);
		bprovite = CommFunction.reserveByteArray(bprovite);
		try {
			String sdes = new String(bprovite, "Utf-8");
			sbuilder.append(sdes);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		logger.error(e.getMessage());
		}
		byte[] bnumpart = new byte[5];
		ArraysN.copy(bnumpart, 0, bsrc, 3, 5);
		sbuilder.append(CommFunction.bytesToAscii(bnumpart));
		desstr = sbuilder.toString();
		//////////////////////
		boolean bl = checkuserid(sbuilder.toString());
		if (bl) {

		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(CommFunction.bytesToAscii(bsrc));
			desstr = sb.toString();
		}
		return desstr;
	}
	private static String carCodebyteT0str_gb2312(byte[] bsrc) {
		String desstr = new String();
		StringBuilder sbuilder = new StringBuilder();
		byte[] bf = { (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
				(byte) 0xFF };
		if (Arrays.equals(bsrc, bf)) {
			return "FFFFFFFFFFFFFFFF";
		}
		byte[] bprovite = new byte[2];
		ArraysN.copy(bprovite, 0, bsrc, 0, 2);
		bprovite = CommFunction.reserveByteArray(bprovite);
		try {
			String sdes = new String(bprovite, "GB2312");
			sbuilder.append(sdes);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
		logger.error(e.getMessage());
		}
		byte[] bnumpart = new byte[6];
		ArraysN.copy(bnumpart, 0, bsrc, 2, 6);
		sbuilder.append(CommFunction.bytesToAscii(bnumpart));
		desstr = sbuilder.toString();
		//////////////////////
		boolean bl = checkuserid(sbuilder.toString());
		if (bl) {

		} else {
			StringBuilder sb = new StringBuilder();
			sb.append(CommFunction.bytesToAscii(bsrc));
			desstr = sb.toString();
		}
		return desstr;
	}
			
	public static String[] BytetoBitstr(byte bsrc, String str) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		String bitStr = CommFunction.byteConvertToStr(bsrc);
		String strbit0 = bitStr.substring(7, 8);
		String strbit1 = bitStr.substring(6, 7);
		String strbit2 = bitStr.substring(5, 6);
		String strbit3 = bitStr.substring(4, 5);
		String strbit4 = bitStr.substring(3, 4);
		String strbit5 = bitStr.substring(2, 3);
		String strbit6 = bitStr.substring(1, 2);
		String strbit7 = bitStr.substring(0, 1);
		String strbit4bit5 = bitStr.substring(2, 4);
		String strbit7tobit4 = bitStr.substring(0, 4);
		// String strbit4bit7=bitStr.substring(4,7);
		if (str.contains("充电枪位置执行机构状态")) {
			if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("充电枪推入不到位,");
			} else if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("1")) {
				sT2Builder.append("充电枪推入到位,");
			}
			if (strbit4bit5.equalsIgnoreCase("00")) {
				sT2Builder.append("充电连接器温度正常,");
			} else if (strbit4bit5.equalsIgnoreCase("01")) {
				sT2Builder.append("充电连接器过温,");
			} else if (strbit4bit5.equalsIgnoreCase("10")) {
				sT2Builder.append("不可信,");
			} else {
				sError.append("充电枪位置执行机构状态值不在范围内,");
				sT2Builder.append("充电枪位置执行机构状态值不在范围内,");
			}

		} else if (str.contains("电池绝缘状态")) {
			// if(bitStr.equals("11111111")){
			/// sT2Builder.append("缺省值FF");
			// }else{
			if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("电池绝缘状态正常,");
			} else if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("1")) {
				sT2Builder.append("电池绝缘状态不可信,");
			} else if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("电池绝缘状态不正常,");
			}
			if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("电池连接器连接状态正常,");
			} else if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("1")) {
				sT2Builder.append("电池连接器连接状态不可信,");
			} else if (strbit2.equalsIgnoreCase("1") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("电池连接器连接状态不正常,");
			}
			/*
			 * else{ sError.append("电池绝缘状态值不在范围内,");
			 * sT2Builder.append("电池绝缘状态值不在范围内,"); }
			 */
			// }
		} else if (str.contains("单体电压过高或过低")) {
			// if(bitStr.equals("11111111")){
			// sT2Builder.append("缺省值FF");
			// }else{
			if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("单体电压正常,");
			} else if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("1")) {
				sT2Builder.append("单体电压过低,");
			} else if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("单体电压过高,");
			}
			if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("SOC正常,");
			} else if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("1")) {
				sT2Builder.append("SOC过低,");
			} else if (strbit2.equalsIgnoreCase("1") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("SOC过高,");
			}
			if (strbit4.equalsIgnoreCase("0") && strbit5.equalsIgnoreCase("0")) {
				sT2Builder.append("充电过电流正常,");
			} else if (strbit4.equalsIgnoreCase("0") && strbit5.equalsIgnoreCase("1")) {
				sT2Builder.append("充电过电流不可信,");
			} else if (strbit4.equalsIgnoreCase("1") && strbit5.equalsIgnoreCase("0")) {
				sT2Builder.append("充电过电流过流,");
			}
			if (strbit6.equalsIgnoreCase("0") && strbit7.equalsIgnoreCase("0")) {
				sT2Builder.append("电池温度过正常,");
			} else if (strbit6.equalsIgnoreCase("0") && strbit7.equalsIgnoreCase("1")) {
				sT2Builder.append("电池温度过不可信,");
			} else if (strbit6.equalsIgnoreCase("1") && strbit7.equalsIgnoreCase("0")) {
				sT2Builder.append("电池温度过高,");
			}
			// }
		} else if (str.contains("BMS中止充电原因")) {
			if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("未达到所需SOC目标值,");
			} else if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("1")) {
				sT2Builder.append("达到所需SOC目标值不可信状态,");
			} else if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("达到所需SOC目标值,");
			}
			if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("达到总电压设定值:正常,");
			} else if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("1")) {
				sT2Builder.append("达到总电压设定值不可信状态,");
			} else if (strbit2.equalsIgnoreCase("1") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("达到总电压设定值,");
			}
			if (strbit4.equalsIgnoreCase("0") && strbit5.equalsIgnoreCase("0")) {
				sT2Builder.append("未达到单体电压设定值,");
			} else if (strbit4.equalsIgnoreCase("0") && strbit5.equalsIgnoreCase("1")) {
				sT2Builder.append("达到单体电压设定值不可信状态,");
			} else if (strbit4.equalsIgnoreCase("1") && strbit5.equalsIgnoreCase("0")) {
				sT2Builder.append("达到单体电压设定值,");
			}
			if (strbit6.equalsIgnoreCase("0") && strbit7.equalsIgnoreCase("0")) {
				sT2Builder.append("充电机主动中止:正常,");
			} else if (strbit6.equalsIgnoreCase("0") && strbit7.equalsIgnoreCase("1")) {
				sT2Builder.append("充电机中止不可信状态,");
			} else if (strbit6.equalsIgnoreCase("1") && strbit7.equalsIgnoreCase("0")) {
				sT2Builder.append("充电机中止(收到CST帧),");
			}
		} else if (str.contains("充电机中止充电原因")) {
			if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("达到充电机设定的条件中止:正常,");
			} else if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("1")) {
				sT2Builder.append("达到充电机设定的条件中止:不可信状态,");
			} else if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("达到充电机设定的条件中止,");
			}
			if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("人工中止:正常,");
			} else if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("1")) {
				sT2Builder.append("人工中止:不可信状态,");
			} else if (strbit2.equalsIgnoreCase("1") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("人工中止,");
			}
			if (strbit4.equalsIgnoreCase("0") && strbit5.equalsIgnoreCase("0")) {
				sT2Builder.append("故障中止:正常,");
			} else if (strbit4.equalsIgnoreCase("0") && strbit5.equalsIgnoreCase("1")) {
				sT2Builder.append("故障中止:不可信状态,");
			} else if (strbit4.equalsIgnoreCase("1") && strbit5.equalsIgnoreCase("0")) {
				sT2Builder.append("故障中止,");
			}
			if (strbit6.equalsIgnoreCase("0") && strbit7.equalsIgnoreCase("0")) {
				sT2Builder.append("BMS主动中止:正常,");
			} else if (strbit6.equalsIgnoreCase("0") && strbit7.equalsIgnoreCase("1")) {
				sT2Builder.append("BMS主动中止:不可信状态),");
			} else if (strbit6.equalsIgnoreCase("1") && strbit7.equalsIgnoreCase("0")) {
				sT2Builder.append("BMS主动中止,");
			}
		} else if (str.contains("BMS中止充电错误原因")) {
			if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("电流正常,");
			} else if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("1")) {
				sT2Builder.append("电流超过需求值:不可信状态,");
			} else if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("电流超过需求值,");
			}
			if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("电压正常,");
			} else if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("1")) {
				sT2Builder.append("电压:不可信状态,");
			} else if (strbit2.equalsIgnoreCase("1") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("电压异常,");
			}
		} else if (str.contains("充电机中止充电错误原因")) {
			if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("电流匹配,");
			} else if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("1")) {
				sT2Builder.append("电流:不可信状态,");
			} else if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("电流不匹配,");
			}
			if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("电压:正常,");
			} else if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("1")) {
				sT2Builder.append("电压:不可信状态,");
			} else if (strbit2.equalsIgnoreCase("1") && strbit3.equalsIgnoreCase("0")) {
				sT2Builder.append("电压异常,");
			}
		} else if (str.contains("转向灯工作状态")) {
			if (bitStr.equals("11111111")) {
				sT2Builder.append("缺省值FF");
			} else {
				if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("0")) {
					sT2Builder.append("右灯禁用,");
				} else if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("0")) {
					sT2Builder.append("右灯开启,");
				} else if (strbit0.equalsIgnoreCase("0") && strbit1.equalsIgnoreCase("1")) {
					sT2Builder.append("右灯检测到错误,");
				} else if (strbit0.equalsIgnoreCase("1") && strbit1.equalsIgnoreCase("1")) {
					sT2Builder.append("右灯未用,");
				}
				if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("0")) {
					sT2Builder.append("左灯禁用,");
				} else if (strbit2.equalsIgnoreCase("1") && strbit3.equalsIgnoreCase("0")) {
					sT2Builder.append("左灯开启,");
				} else if (strbit2.equalsIgnoreCase("0") && strbit3.equalsIgnoreCase("1")) {
					sT2Builder.append("左灯检测到错误,");
				} else if (strbit2.equalsIgnoreCase("1") && strbit3.equalsIgnoreCase("1")) {
					sT2Builder.append("左灯未用,");
				} else {
					sError.append("转向灯工作状态值不在范围内,");
					sT2Builder.append("转向灯工作状态值不在范围内,");
				}
			}
		} else if (str.contains("充电系统总体状态")) {
			if (strbit0.equalsIgnoreCase("0")) {
				sT2Builder.append("正常,");
			} else if (strbit0.equalsIgnoreCase("1")) {
				sT2Builder.append("故障,");
			}
			if (strbit1.equalsIgnoreCase("0")) {
				sT2Builder.append("门关闭,");
			} else if (strbit1.equalsIgnoreCase("1")) {
				sT2Builder.append("门开,");
			}
			if (strbit2.equalsIgnoreCase("0")) {
				sT2Builder.append("桩位空闲,");
			} else if (strbit2.equalsIgnoreCase("1")) {
				sT2Builder.append("桩位占用,");
			}
			if (strbit7tobit4.equalsIgnoreCase("0000")) {
				sT2Builder.append("独立工作");
			} else if (strbit7tobit4.equalsIgnoreCase("0001")) {
				sT2Builder.append("并机工作");
			}
		} else if (str.contains("车门状态")) {
			if (bitStr.equals("11111111")) {
				sT2Builder.append("缺省值FF");
			} else {
				if (strbit0.equalsIgnoreCase("0")) {
					sT2Builder.append("前门关,");
				} else if (strbit0.equalsIgnoreCase("1")) {
					sT2Builder.append("前门开,");
				}
				if (strbit1.equalsIgnoreCase("0")) {
					sT2Builder.append("后门关,");
				} else if (strbit1.equalsIgnoreCase("1")) {
					sT2Builder.append("后门开,");
				}
			}
		} else if (str.contains("充电枪状态")) {
			if (strbit0.equalsIgnoreCase("1")) {
				sT2Builder.append("在");
			} else if (strbit0.equalsIgnoreCase("0")) {
				sT2Builder.append("空");
			} else {
				sError.append("充电枪状态值不在范围内");
				sT2Builder.append("充电枪状态值不在范围内");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	public static String InttoBitstr(byte[] bsrc, String str) {
		bsrc = CommFunction.reserveByteArray(bsrc);
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 2; i++) {
			String srcFault = CommFunction.byteConvertToStr(bsrc[i]);
			sb.append(srcFault);
		}
		String bitStr = sb.toString();
		String strbit1 = bitStr.substring(15, 16);
		String strbit2 = bitStr.substring(14, 15);
		String strbit3 = bitStr.substring(13, 14);
		String strbit4 = bitStr.substring(12, 13);
		String strbit5 = bitStr.substring(11, 12);
		String strbit6 = bitStr.substring(10, 11);
		String strbit7 = bitStr.substring(9, 10);
		String strbit8 = bitStr.substring(8, 9);
		String strbit9 = bitStr.substring(7, 8);
		String strbit10 = bitStr.substring(6, 7);
		String strbit11 = bitStr.substring(5, 6);
		String strbit12 = bitStr.substring(4, 5);
		if (str.equalsIgnoreCase("BMS中止充电故障原因")) {
			if (strbit1.equalsIgnoreCase("0") && strbit2.equalsIgnoreCase("0")) {
				sT2Builder.append("绝缘故障:正常,");
			} else if (strbit1.equalsIgnoreCase("0") && strbit2.equalsIgnoreCase("1")) {
				sT2Builder.append("绝缘故障:不可信状态 ,");
			} else if (strbit1.equalsIgnoreCase("1") && strbit2.equalsIgnoreCase("0")) {
				sT2Builder.append("绝缘故障:故障,");
			}
			if (strbit3.equalsIgnoreCase("0") && strbit4.equalsIgnoreCase("0")) {
				sT2Builder.append("输出连接器过温故障:正常,");
			} else if (strbit3.equalsIgnoreCase("0") && strbit4.equalsIgnoreCase("1")) {
				sT2Builder.append("输出连接器过温故障:不可信状态,");
			} else if (strbit3.equalsIgnoreCase("1") && strbit4.equalsIgnoreCase("0")) {
				sT2Builder.append("输出连接器过温故障:故障 ,");
			}
			if (strbit5.equalsIgnoreCase("0") && strbit6.equalsIgnoreCase("0")) {
				sT2Builder.append("BMS元件、输出连接器过温:正常,");
			} else if (strbit5.equalsIgnoreCase("0") && strbit6.equalsIgnoreCase("1")) {
				sT2Builder.append("BMS元件、输出连接器过温:不可信状态,");
			} else if (strbit5.equalsIgnoreCase("1") && strbit6.equalsIgnoreCase("0")) {
				sT2Builder.append("BMS元件、输出连接器过温: 故障,");
			}
			if (strbit7.equalsIgnoreCase("0") && strbit8.equalsIgnoreCase("0")) {
				sT2Builder.append("充电连接器故障:正常,");
			} else if (strbit7.equalsIgnoreCase("0") && strbit8.equalsIgnoreCase("1")) {
				sT2Builder.append("充电连接器故障:不可信状态,");
			} else if (strbit7.equalsIgnoreCase("1") && strbit8.equalsIgnoreCase("0")) {
				sT2Builder.append("充电连接器故障:故障 ,");
			}
			if (strbit9.equalsIgnoreCase("0") && strbit10.equalsIgnoreCase("0")) {
				sT2Builder.append("电池组温度正常,");
			} else if (strbit9.equalsIgnoreCase("0") && strbit10.equalsIgnoreCase("1")) {
				sT2Builder.append("电池组温度不可信状态,");
			} else if (strbit9.equalsIgnoreCase("1") && strbit10.equalsIgnoreCase("0")) {
				sT2Builder.append("电池组温度过高 ,");
			}
			if (strbit11.equalsIgnoreCase("0") && strbit12.equalsIgnoreCase("0")) {
				sT2Builder.append("其他故障:正常,");
			} else if (strbit11.equalsIgnoreCase("0") && strbit12.equalsIgnoreCase("1")) {
				sT2Builder.append("其他故障:不可信状态,");
			} else if (strbit11.equalsIgnoreCase("1") && strbit12.equalsIgnoreCase("0")) {
				sT2Builder.append("其他故障:故障 ,");
			}
		} else if (str.equalsIgnoreCase("充电机中止充电故障原因")) {
			if (strbit1.equalsIgnoreCase("0") && strbit2.equalsIgnoreCase("0")) {
				sT2Builder.append("充电机温度正常,");
			} else if (strbit1.equalsIgnoreCase("0") && strbit2.equalsIgnoreCase("1")) {
				sT2Builder.append("充电机过温不可信状态,");
			} else if (strbit1.equalsIgnoreCase("1") && strbit2.equalsIgnoreCase("0")) {
				sT2Builder.append("充电机过温 ,");
			}
			if (strbit3.equalsIgnoreCase("0") && strbit4.equalsIgnoreCase("0")) {
				sT2Builder.append("充电连接器正常,");
			} else if (strbit3.equalsIgnoreCase("0") && strbit4.equalsIgnoreCase("1")) {
				sT2Builder.append("充电连接器不可信状态,");
			} else if (strbit3.equalsIgnoreCase("1") && strbit4.equalsIgnoreCase("0")) {
				sT2Builder.append("充电连接器:故障 ,");
			}
			if (strbit5.equalsIgnoreCase("0") && strbit6.equalsIgnoreCase("0")) {
				sT2Builder.append("充电机内部温度正常,");
			} else if (strbit5.equalsIgnoreCase("0") && strbit6.equalsIgnoreCase("1")) {
				sT2Builder.append("充电机内部不可信状态,");
			} else if (strbit5.equalsIgnoreCase("1") && strbit6.equalsIgnoreCase("0")) {
				sT2Builder.append("充电机内部温度:过温 ,");
			}
			if (strbit7.equalsIgnoreCase("0") && strbit8.equalsIgnoreCase("0")) {
				sT2Builder.append("电量传送正常,");
			} else if (strbit7.equalsIgnoreCase("0") && strbit8.equalsIgnoreCase("1")) {
				sT2Builder.append("电量不可信状态,");
			} else if (strbit7.equalsIgnoreCase("1") && strbit8.equalsIgnoreCase("0")) {
				sT2Builder.append("电量不能传送 ,");
			}
			if (strbit9.equalsIgnoreCase("0") && strbit10.equalsIgnoreCase("0")) {
				sT2Builder.append(" 充电机正常,");
			} else if (strbit9.equalsIgnoreCase("0") && strbit10.equalsIgnoreCase("1")) {
				sT2Builder.append("充电机急停不可信状态,");
			} else if (strbit9.equalsIgnoreCase("1") && strbit10.equalsIgnoreCase("0")) {
				sT2Builder.append("充电机急停 ,");
			}
			if (strbit11.equalsIgnoreCase("0") && strbit12.equalsIgnoreCase("0")) {
				sT2Builder.append("其他故障:正常,");
			} else if (strbit11.equalsIgnoreCase("0") && strbit12.equalsIgnoreCase("1")) {
				sT2Builder.append("其他故障:不可信状态,");
			} else if (strbit11.equalsIgnoreCase("1") && strbit12.equalsIgnoreCase("0")) {
				sT2Builder.append("其他故障: 故障,");
			}
		}
		return sT2Builder.toString();
	}

	// 事件类型1个字节转成16进制字符串:
	public static String[] EventTypebyteToHexStr(byte[] bsrc) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		String sEvent = CommFunction.bytesToHexString(bsrc);
		// 01
		if (sEvent.equalsIgnoreCase("01")) {
			sT2Builder.append("事件类别:开机上电,定义:外部220V 电源给控制板上电");
		}
		// 02
		else if (sEvent.equalsIgnoreCase("02")) {
			sT2Builder.append("事件类别:关机,定义:220V 外部电源断开");
		}
		// 03
		else if (sEvent.equalsIgnoreCase("03")) {
			sT2Builder.append("事件类别:关机,定义:220V 外部电源断开");
		}
		// 04
		else if (sEvent.equalsIgnoreCase("04")) {
			sT2Builder.append("事件类别:刷卡,定义:有刷卡");
		}
		// 05
		else if (sEvent.equalsIgnoreCase("05")) {
			sT2Builder.append("事件类别:充电开始,定义:充电机输出功率从无变为有");
		}
		// 06
		else if (sEvent.equalsIgnoreCase("06")) {
			sT2Builder.append("事件类别:充电结束,定义:充电机输出功率从有变为无");

		}
		// 07
		else if (sEvent.equalsIgnoreCase("07")) {
			sT2Builder.append("事件类别:检修,定义:充电机从正常（故障）工作态转为检修状态");
		}
		// 08
		else if (sEvent.equalsIgnoreCase("08")) {
			sT2Builder.append("事件类别:升级,定义:充电机完成升级操作");
		}
		// 09
		else if (sEvent.equalsIgnoreCase("09")) {
			sT2Builder.append("事件类别:参数配置,定义:充电机工作配置参数通过外部指令进行了调整");
		}
		// 0A
		else if (sEvent.equalsIgnoreCase("0A")) {
			sT2Builder.append("事件类别:数据补传,定义:充电机将存储在本地的未来得及上传给后台的数据启动传输给后台");
		}
		// 0B
		else if (sEvent.equalsIgnoreCase("0B")) {
			sT2Builder.append("事件类别:删除历史数据,定义:充电机将存储在本地的数据进行了删除操作（不含覆盖操作）");
		}
		// 0C
		else if (sEvent.equalsIgnoreCase("0C")) {
			sT2Builder.append("事件类别:历史数据导出,定义:充电机通过外部存储器将存储在本地的数据进行了导出");
		}
		// 20
		else if (sEvent.equalsIgnoreCase("20")) {
			sT2Builder.append("自然充");
		} else if (sEvent.equalsIgnoreCase("30")) {
			sT2Builder.append("按时间");
		} else if (sEvent.equalsIgnoreCase("40")) {
			sT2Builder.append("按电量");
		} else {

			sT2Builder.append(sEvent);
			// sError.append("事件类型的值不正确");
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	// 事件类型1个字节转成16进制字符串:
	public static String[] byteTo1HexStr(byte[] bsrc) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		String sEvent = CommFunction.bytesToHexString(bsrc);
		// 01
		if (sEvent.equalsIgnoreCase("01")) {
			sT2Builder.append("人工擦除");
		}
		// 02
		else if (sEvent.equalsIgnoreCase("02")) {
			sT2Builder.append("自动擦除");
		} else if (sEvent.equalsIgnoreCase("00")) {
			sT2Builder.append("0");
		} else {

			sT2Builder.append("数据管理模式的值不正确");
			sError.append("数据管理模式的值不正确");
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}
public static List<String> moduleFaultAnalyze(byte[] bsrc,int type){
	if(bsrc.length<2){
		return null;
	}
	List<byte[]> s = new ArrayList<byte[]>();
List<String> result = new ArrayList<String>();
	for (int i = 0; i < bsrc.length; i++) {
		if (bsrc[i] == 0)
			continue;
		if ((bsrc[i] & 0x80) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x80;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x40) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x40;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x20) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x20;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x10) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x10;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x08) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x08;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x04) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x04;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x02) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x02;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x01) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x01;
			s.add(bArray);
		}

	}
	if(s.size()==0){
		result.add("无告警");
	}else
	{
		for(byte[] b:s){
			result.add(ModuleFaultbyteToHexStr(b,type)[1]);
		}
	}
	return result;
	
}
	// 模块故障2字节转成16进制字符串:0x34
	public static String[] ModuleFaultbyteToHexStr(byte[] bsrc, int i) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		String sFault = CommFunction.bytesToHexString(bsrc);
		if (i == 1) {
			//
			if (sFault.equalsIgnoreCase("0000")) {
				sT2Builder.append("无告警");
			}
			// 30
			else if (sFault.equalsIgnoreCase("0001")) {
				sT2Builder.append("实体:一级-模块	二级-PFC模块,故障分类:PFC模块母线过压;故障等级:1级;故障定义:I05001;");
			}
			// 31
			else if (sFault.equalsIgnoreCase("0002")) {
				sT2Builder.append("实体:一级-模块	二级-PFC模块,故障分类,PFC模块母线欠压;故障等级:1级;故障定义:I05001;");
			}
			// 32
			else if (sFault.equalsIgnoreCase("0004")) {
				sT2Builder.append("实体:一级-模块	二级-PFC模块,故障分类:PFC模块控制电源异常;故障等级:1级;故障定义:I05001;");
			}
			// 33
			else if (sFault.equalsIgnoreCase("0008")) {
				sT2Builder.append("实体:一级-模块	二级-DC/DC模块,故障分类:DC/DC模块控制电源异常;故障等级:1级;故障定义:I05002;");
			}
			// 34
			else if (sFault.equalsIgnoreCase("0010")) {
				sT2Builder.append("实体:一级-模块	二级-IGBT模块,故障分类:IGBT驱动过流;故障等级:1级;故障定义:I05003;");

			}
			// 35
			else if (sFault.equalsIgnoreCase("0020")) {
				sT2Builder.append("实体:一级-模块	二级-IGBT模块,故障分类:IGBT驱动欠压;故障等级:1级;故障定义:I05003;");
			}
			// 36
			else if (sFault.equalsIgnoreCase("0040")) {
				sT2Builder.append("实体:一级-模块	二级-变压器模块,故障分类:变压器过热;故障等级:1级;故障定义:I05004;");
			}
			// 37
			else if (sFault.equalsIgnoreCase("0080")) {
				sT2Builder.append("实体:一级-模+34:49块	二级-PFC散热器,故障分类:散热器损坏，散热器与面板脱离;故障等级:3级;故障定义:I05005;");
			}
			// 38
			else if (sFault.equalsIgnoreCase("0100")) {
				sT2Builder.append("实体:一级-模块	二级-PFC散热器,故障分类:散热器损坏，散热器与面板脱离;故障等级:2级;故障定义:I05005;");
			}
			// 39
			else if (sFault.equalsIgnoreCase("0200")) {
				sT2Builder.append("实体:一级-模块	二级-PFC散热器,故障分类:外界温度过高，风扇出现异常;故障等级:1级;故障定义:I05005;");
			}
			// 40
			else if (sFault.equalsIgnoreCase("0400")) {
				sT2Builder.append("实体:一级-模块	二级-模块故障,故障分类:充电模块无输出（新增）;故障等级:1级;故障定义:I05009;");
			}
			// 41
			else if (sFault.equalsIgnoreCase("0800")) {
				sT2Builder.append("实体:一级-模块	二级-模块内部温度过高,故障分类:模块内部温度过高;故障等级:3级;故障定义:I05006;");
			}
			// 42
			else if (sFault.equalsIgnoreCase("1000")) {
				sT2Builder.append("实体:一级-模块	二级-模块内部温度过高,故障分类:出风口温度过高;故障等级:2级;故障定义:I05006;");
			}
			// 43
			else if (sFault.equalsIgnoreCase("2000")) {
				sT2Builder.append("实体:一级-模块	二级-模块内部温度过高,故障分类:进风口温度过高;故障等级:1级;故障定义:I05006;");
			}
			// 44
			else if (sFault.equalsIgnoreCase("4000")) {
				sT2Builder.append("实体:一级-模块	二级-直流输出继电器故障,故障分类:直流输出继电器的状态信息;故障等级:1级;故障定义:I05007;");
			}
			// 45
			else if (sFault.equalsIgnoreCase("8000")) {
				sT2Builder.append("实体:一级-模块	二级-直流输出继电器故障,故障分类:直流输出继电器无触发信号;故障等级:1级;故障定义:I05007;");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("模块故障状态不在允许范围内");
				sError.append("模块故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		} else if (i == 2) {
			if (sFault.equalsIgnoreCase("0000")) {
				sT2Builder.append("无告警");
			}
			// 30
			else if (sFault.equalsIgnoreCase("0001")) {
				sT2Builder.append(
						"实体:一级-模块	二级-PFC模块；故障分类:PFC模块母线过压；故障描述:	IGBT故障，PFC过压保护失效，母线电压过高，DC输入过压；属性:DC模块输入过压，无法正常工作；是否与运营服务相关:否；等级:1级；处理措施:模块停机，后台显示信息“PFC模块母线过压，设备无法正常工作”，由后台系统通知维护人员进行现场维护；设备告警显示要求:	本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:充电模块；");
			}
			// 31
			else if (sFault.equalsIgnoreCase("0002")) {
				sT2Builder.append(
						"实体:一级-模块	二级-PFC模块；故障分类:PFC模块母线欠压；故障描述:GBT故障，PFC输出电压降低，带载能力变差，DC输入欠压；属性:	DC模块输入欠压，无法正常工作；是否与运营服务相关:是；等级:1级；处理措施:	模块停机，后台显示信息:“PFC模块母线欠压，设备无法正常工作”，由后台系统通知维护人员进行现场维护；设备告警显示要求:	本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:充电模块；");
			}
			// 32
			else if (sFault.equalsIgnoreCase("0004")) {
				sT2Builder.append(
						"实体:一级-模块	二级-PFC模块；故障分类:PFC模块控制电源异常	；故障描述:电路板的控制芯片、电容、电阻等元器件虚焊、损坏，电源插座接触不良；属性:控制电源故障，PFC模块无法正常工作；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“PFC模块控制电源异常，设备无法正常工作”，由后台系统通知维护人员进行现场维护；设备告警显示要求:	本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:充电模块；");
			}
			// 33
			else if (sFault.equalsIgnoreCase("0008")) {
				sT2Builder.append(
						"实体:一级-模块	二级-DC/DC模块；故障分类:DC/DC模块控制电源异常；故障描述:电路板的控制芯片、电容、电阻等元器件虚焊、损滑，电源插座接触不良；属性:控制电源故障，DC/DC模块无法正常工作；是否与运营服务相关:是	；等级:1级；处理措施:模块停机，后台显示信息“DC/DC模块控制电源异常，设备无法正常工作”，由后台系统通知维护人员进行现场维护；设备告警显示要求:	本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:充电模块；");
			}
			// 34
			else if (sFault.equalsIgnoreCase("0010")) {
				sT2Builder.append(
						"实体:一级-模块	二级-IGBT模块；故障分类:IGBT驱动过流；故障描述:晶体管或二极管损坏，控制或驱动电路故障，或由于干扰引起的误动，输出线接错或绝缘击穿造成短路，输出端对地短路或绝缘损坏；属性:IGBT运行指标超过安全电流阈值，长时间过流运行造成很高功耗，损坏器件；是否与运营服务相关:是	；等级:1级；处理措施:模块停机，后台显示信息“驱动过流，设备无法正常工作”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:充电模块；");

			}
			// 35
			else if (sFault.equalsIgnoreCase("0020")) {
				sT2Builder.append(
						"实体:一级-模块	二级-IGBT模块；故障分类:IGBT驱动欠压；故障描述:驱动变压器放大能力不足，驱动电压不足；属性:驱动欠压，PFC模块不能正常工作；是否与运营服务相关:是；等级:1级；处理措施:	模块停机，后台显示信息“驱动欠压，设备无法正常工作”，由后台系统通知维护人员进行现场维护；设备告警显示要求:	本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:充电模块");
			}
			// 36
			else if (sFault.equalsIgnoreCase("0040")) {
				sT2Builder.append(
						"实体:一级-模块	二级-变压器模块；故障分类:变压器过热；故障描述:风扇故障或风道堵塞，散热故障，变压器温度过高；属性:变压器线圈发热，严重时烧毁；是否与运营服务相关:是；等级:1级；处理措施:	模块停机，后台显示信息:“驱动欠压，设备无法正常工作”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电模块");
			}
			// 37
			else if (sFault.equalsIgnoreCase("0080")) {
				sT2Builder.append(
						"实体:一级-模+34:49块	二级-PFC散热器；故障分类:散热器损坏，散热器与面板脱离；故障描述:PFC模块散热器温度过高；属性:PFC模块无法正常工作；是否与运营服务相关:是；等级:70＜温度＜75℃:3级；处理措施:3级告警，降功率80%运行，将“故障描述”信息传输到后台监控系统，后台显示信息:“充电设备故障”由后台系统通知维护人员进行现场维护	；设备告警显示要求:设备正常工作；系统管理分类:充电模块");
			}
			// 38
			else if (sFault.equalsIgnoreCase("0100")) {
				sT2Builder.append(
						"实体:一级-模块	二级-PFC散热器；故障分类:散热器损坏，散热器与面板脱离；故障描述:PFC模块散热器温度过高；属性:PFC模块无法正常工作；是否与运营服务相关:是；等级:75≤温度＜80℃:2级；处理措施:2级告警:降功率50%运行；将“故障描述”信息传输到后台监控系统，后台显示信息:“充电设备故障”由后台系统通知维护人员进行现场维护；设备告警显示要求:设备正常工作；系统管理分类:充电模块");
			}
			// 39
			else if (sFault.equalsIgnoreCase("0200")) {
				sT2Builder.append(
						"实体:一级-模块	二级-PFC散热器；故障分类:外界温度过高，风扇出现异常；故障描述:PFC模块散热器温度过高；属性:PFC模块无法正常工作；是否与运营服务相关:是；等级:80℃≤温度:1级；处理措施:1级故障:停止充电；将“故障描述”信息传输到后台监控系统，后台显示信息:“充电设备故障”由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电模块");
			}
			// 40
			else if (sFault.equalsIgnoreCase("0400")) {
				sT2Builder.append(
						"实体:一级-模块	二级-模块故障；故障分类:充电模块无输出（新增）；故障描述:充电模块无法正常工作；属性:　；是否与运营服务相关:　；等级:1级	；处理措施:　；系统管理分类:充电模块");
			}
			// 41
			else if (sFault.equalsIgnoreCase("0800")) {
				sT2Builder.append(
						"实体:一级-模块	二级-模块内部温度过高；故障分类:模块内部温度过高；故障描述:模块内部温度过高，无法正常工作；属性:充电设备内部温度过高；是否与运营服务相关:是；等级:70＜温度＜75℃:3级；处理措施:3级告警:降功率80%运行，将“故障描述”信息传输到后台监控系统，后台显示信息“充电机内部过温故障，请检查充电设备”由后台系统通知维护人员进行现场维护；设备告警显示要求:设备正常工作；系统管理分类:充电模块");
			}
			// 42
			else if (sFault.equalsIgnoreCase("1000")) {
				sT2Builder.append(
						"实体:一级-模块	二级-模块内部温度过高；故障分类:出风口温度过高；故障描述:出风口温度过高；属性:充电设备内部温度过高；是否与运营服务相关:是；等级:75≤温度＜80℃:2级；处理措施:2级告警:降功率50%运行，将“故障描述”信息传输到后台监控系统，后台显示信息“充电机内部过温故障，请检查充电设备”由后台系统通知维护人员进行现场维护；设备告警显示要求:设备正常工作；系统管理分类:充电模块");
			}
			// 43
			else if (sFault.equalsIgnoreCase("2000")) {
				sT2Builder.append(
						"实体:一级-模块	二级-模块内部温度过高；故障分类:进风口温度过高；故障描述:进风口温度过高；属性:充电设备内部温度过高；是否与运营服务相关:是；等级:80℃≤温度:1级；处理措施:1级故障，停止充电，将“故障描述”信息传输到后台监控系统，后台显示信息“充电机内部过温故障，请检查充电设备”由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电模块");
			}
			// 44
			else if (sFault.equalsIgnoreCase("4000")) {
				sT2Builder.append(
						"实体:一级-模块	二级-直流输出继电器故障；故障分类:直流输出继电器的状态信息；故障描述:无直流输出；属性:直流输出侧无输出电压；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“直流输出继电器故障，请检查直流输出继电器的触发信号，或更换继电器”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电模块");
			}
			// 45
			else if (sFault.equalsIgnoreCase("8000")) {
				sT2Builder.append(
						"实体:一级-模块	二级-直流输出继电器故障；故障分类:直流输出继电器无触发信号；故障描述:直流输出继电器无动作；属性:直流输出侧无输出电压；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“直流输出继电器故障，请检查直流输出继电器的触发信号，或更换继电器”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电模块");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("系统故障状态不在允许范围内");
				sError.append("模块故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		} else {
			if (sFault.equalsIgnoreCase("0000")) {
				sT2Builder.append("无告警");
			}
			// 30
			else if (sFault.equalsIgnoreCase("0001")) {
				sT2Builder.append(
						"一级-模块	二级-PFC模块；PFC模块母线过压；IGBT故障，PFC过压保护失效，母线电压过高，DC输入过压；DC模块输入过压，无法正常工作；否；1级；模块停机，后台显示信息“PFC模块母线过压，设备无法正常工作”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；充电模块");
			}
			// 31
			else if (sFault.equalsIgnoreCase("0002")) {
				sT2Builder.append(
						"一级-模块	二级-PFC模块；PFC模块母线欠压；GBT故障，PFC输出电压降低，带载能力变差，DC输入欠压；	DC模块输入欠压，无法正常工作；是；1级；模块停机，后台显示信息:“PFC模块母线欠压，设备无法正常工作”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；充电模块");
			}
			// 32
			else if (sFault.equalsIgnoreCase("0004")) {
				sT2Builder.append(
						"一级-模块	二级-PFC模块；PFC模块控制电源异常	；电路板的控制芯片、电容、电阻等元器件虚焊、损坏，电源插座接触不良；控制电源故障，PFC模块无法正常工作；是；1级；模块停机，后台显示信息“PFC模块控制电源异常，设备无法正常工作”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；充电模块");
			}
			// 33
			else if (sFault.equalsIgnoreCase("0008")) {
				sT2Builder.append(
						"一级-模块	二级-DC/DC模块；DC/DC模块控制电源异常；电路板的控制芯片、电容、电阻等元器件虚焊、损滑，电源插座接触不良；控制电源故障，DC/DC模块无法正常工作；是	；1级；模块停机，后台显示信息“DC/DC模块控制电源异常，设备无法正常工作”，由后台系统通知维护人员进行现场维护；	本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；充电模块");
			}
			// 34
			else if (sFault.equalsIgnoreCase("0010")) {
				sT2Builder.append(
						"一级-模块	二级-IGBT模块；IGBT驱动过流；晶体管或二极管损坏，控制或驱动电路故障，或由于干扰引起的误动，输出线接错或绝缘击穿造成短路，输出端对地短路或绝缘损坏；IGBT运行指标超过安全电流阈值，长时间过流运行造成很高功耗，损坏器件；是	；1级；模块停机，后台显示信息“驱动过流，设备无法正常工作”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；充电模块");

			}
			// 35
			else if (sFault.equalsIgnoreCase("0020")) {
				sT2Builder.append(
						"一级-模块	二级-IGBT模块；IGBT驱动欠压；驱动变压器放大能力不足，驱动电压不足；驱动欠压，PFC模块不能正常工作；是；1级；模块停机，后台显示信息“驱动欠压，设备无法正常工作”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；充电模块");
			}
			// 36
			else if (sFault.equalsIgnoreCase("0040")) {
				sT2Builder.append(
						"一级-模块	二级-变压器模块；变压器过热；风扇故障或风道堵塞，散热故障，变压器温度过高；变压器线圈发热，严重时烧毁；是；1级；	模块停机，后台显示信息:“驱动欠压，设备无法正常工作”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电模块");
			}
			// 37
			else if (sFault.equalsIgnoreCase("0080")) {
				sT2Builder.append(
						"一级-模+34:49块	二级-PFC散热器；散热器损坏，散热器与面板脱离；PFC模块散热器温度过高；PFC模块无法正常工作；是；70＜温度＜75℃:3级；3级告警，降功率80%运行，将“故障描述”信息传输到后台监控系统，后台显示信息:“充电设备故障”由后台系统通知维护人员进行现场维护	；设备正常工作；充电模块");
			}
			// 38
			else if (sFault.equalsIgnoreCase("0100")) {
				sT2Builder.append(
						"一级-模块	二级-PFC散热器；散热器损坏，散热器与面板脱离；PFC模块散热器温度过高；PFC模块无法正常工作；是；75≤温度＜80℃:2级；2级告警:降功率50%运行,将“故障描述”信息传输到后台监控系统，后台显示信息:“充电设备故障”由后台系统通知维护人员进行现场维护；设备正常工作；充电模块");
			}
			// 39
			else if (sFault.equalsIgnoreCase("0200")) {
				sT2Builder.append(
						"一级-模块	二级-PFC散热器；外界温度过高，风扇出现异常；PFC模块散热器温度过高；PFC模块无法正常工作；是；80℃≤温度:1级；1级故障:停止充电,将“故障描述”信息传输到后台监控系统，后台显示信息:“充电设备故障”由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电模块");
			}
			// 40
			else if (sFault.equalsIgnoreCase("0400")) {
				sT2Builder.append("一级-模块	二级-模块故障；充电模块无输出（新增）；充电模块无法正常工作；　；　；1级	；　；  　；充电模块");
			}
			// 41
			else if (sFault.equalsIgnoreCase("0800")) {
				sT2Builder.append(
						"一级-模块	二级-模块内部温度过高；模块内部温度过高；模块内部温度过高，无法正常工作；充电设备内部温度过高；是；70＜温度＜75℃:3级；3级告警:降功率80%运行，将“故障描述”信息传输到后台监控系统，后台显示信息“充电机内部过温故障，请检查充电设备”由后台系统通知维护人员进行现场维护；设备正常工作；充电模块");
			}
			// 42
			else if (sFault.equalsIgnoreCase("1000")) {
				sT2Builder.append(
						"一级-模块	二级-模块内部温度过高；出风口温度过高；出风口温度过高；充电设备内部温度过高；是；75≤温度＜80℃:2级；2级告警:降功率50%运行，将“故障描述”信息传输到后台监控系统，后台显示信息“充电机内部过温故障，请检查充电设备”由后台系统通知维护人员进行现场维护；设备正常工作；充电模块");
			}
			// 43
			else if (sFault.equalsIgnoreCase("2000")) {
				sT2Builder.append(
						"一级-模块	二级-模块内部温度过高；进风口温度过高；进风口温度过高；充电设备内部温度过高；是；80℃≤温度:1级；1级故障，停止充电，将“故障描述”信息传输到后台监控系统，后台显示信息“充电机内部过温故障，请检查充电设备”由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电模块");
			}
			// 44
			else if (sFault.equalsIgnoreCase("4000")) {
				sT2Builder.append(
						"一级-模块	二级-直流输出继电器故障；直流输出继电器的状态信息；无直流输出；直流输出侧无输出电压；是；1级；模块停机，后台显示信息“直流输出继电器故障，请检查直流输出继电器的触发信号，或更换继电器”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电模块");
			}
			// 45
			else if (sFault.equalsIgnoreCase("8000")) {
				sT2Builder.append(
						"一级-模块	二级-直流输出继电器故障；直流输出继电器无触发信号；直流输出继电器无动作；直流输出侧无输出电压；是；1级；模块停机，后台显示信息“直流输出继电器故障，请检查直流输出继电器的触发信号，或更换继电器”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电模块");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("　；　；　；　；　；　；　；　；充电模块");
				sError.append("模块故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	public static String[] BMSSystemFaultbyteToHexStr(byte[] bsrc) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		String sFault = CommFunction.bytesToHexString(bsrc);
		//
		if (sFault.equalsIgnoreCase("0000")) {
			sT2Builder.append("无告警");
		} else {
			// ResultDto.setCheckContent("无法解析!");
			sT2Builder.append("BMS系统故障不在允许范围内");
			sError.append("BMS系统故障值不正确");
			// ResultDto.setErrorInfo("系统故障状态值不正确");
			// ResultDto.setT2CheckResult("T2不正确");
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	private static List<byte[]> splitFaultCode(byte[] bsrc) {
		List<byte[]> s = new ArrayList<byte[]>();

		for (int i = 0; i < bsrc.length; i++) {
			if (bsrc[i] == 0)
				continue;
			if ((bsrc[i] & 0x80) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x80;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x40) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x40;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x20) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x20;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x10) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x10;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x08) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x08;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x04) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x04;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x02) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x02;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x01) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x01;
				s.add(bArray);
			}

		}
		return s;
	}

	public static List<String> systemFaultToStr(byte[] bsrc, int type) {
		if (bsrc.length < 4) {
			return null;
		}
		List<String> result = new ArrayList<String>();
		List<byte[]> s = splitFaultCode(bsrc);
		if (s.isEmpty()) {
			result.add("无告警");
		} else {
			for (byte[] bArray : s) {
				result.add(SystemFaultbyteToHexStr(bArray, type)[1]);
			}
		}

		return result;

	}

	public static List<String> chargeFaultToStr(byte[] bsrc, int type) {
		if (bsrc.length < 4) {
			return null;
		}
		
		List<String> result = new ArrayList<String>();
		List<byte[]> s = splitFaultCode(bsrc);

if(s.isEmpty()){
	result.add("无告警");
}else{
	for(byte[] bArray:s){
		result.add(ChargeSystemFaultbyteToHexStr(bArray,type)[1]);
	}
}

return result;
		

	}

	// 系统故障4字节转成16进制字符串:
	public static String[] SystemFaultbyteToHexStr(byte[] bsrc, int i) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		String sFault = CommFunction.bytesToHexString(bsrc);

		if (i == 1) {
			
			if (sFault.equalsIgnoreCase("00000001")) {
				sT2Builder.append("实体:一级-人机交互　二级-读卡器;故障分类:读卡器通信故障;故障等级:1级;故障定义:I01001;");
			}
			// 5
			else if (sFault.equalsIgnoreCase("01000000")) {
				sT2Builder.append("实体:一级-人机交互　二级-读卡器,故障分类:读卡器通信故障;故障等级:3级;故障定义:I01001;");
			}
			// 6
			else if (sFault.equalsIgnoreCase("00000002")) {
				sT2Builder.append("实体:一级-人机交互　二级-读卡器,故障分类:与主控板通信超时故障;故障等级:1级;故障定义:I01001;");
			}
			// 7
			else if (sFault.equalsIgnoreCase("02000000")) {
				sT2Builder.append("实体:一级-人机交互　二级-读卡器,故障分类:与主控板通信超时故障;故障等级:3级;故障定义:I01001;");
			}
			// 8
			else if (sFault.equalsIgnoreCase("00000004")) {
				sT2Builder.append("实体:一级-人机交互　二级-显示屏,故障分类:显示屏硬件损坏;故障等级:1级;故障定义:I01002;");

			}
			// 9
			else if (sFault.equalsIgnoreCase("00000008")) {
				sT2Builder.append("实体:一级-人机交互　二级-显示屏,故障分类:与主控板通信超时故障;故障等级:1级;故障定义:I01002;");
			}
			// 10
			else if (sFault.equalsIgnoreCase("00010000")) {
				sT2Builder.append("实体:一级-人机交互	二级-键盘,故障分类:键盘硬件损坏;故障等级:2级;故障定义:I01003;");
			}
			// 11
			else if (sFault.equalsIgnoreCase("00020000")) {
				sT2Builder.append("实体:一级-人机交互	二级-键盘,故障分类:与主控板通信故障;故障等级:2级;故障定义:I01003;");
			}
			// 12
			else if (sFault.equalsIgnoreCase("04000000")) {
				sT2Builder.append("实体:一级-人机交互	二级-打印机,故障分类:打印机硬件损坏;故障等级:3级;故障定义:I01004;");
			}
			// 13
			else if (sFault.equalsIgnoreCase("10000000")) {
				sT2Builder.append("实体:一级-人机交互	二级-打印机,故障分类:缺纸状态（新增）;故障等级:3级;故障定义:I01004;");
			}
			// 14
			else if (sFault.equalsIgnoreCase("08000000")) {
				sT2Builder.append("实体:一级-人机交互	二级-打印机,故障分类:与主控板通信故障;故障等级:3级;故障定义:I01004;");
			}
			// 15
			else if (sFault.equalsIgnoreCase("00040000")) {
				sT2Builder.append("实体:一级-外部设备	二级-电磁锁,故障分类:电磁锁故障；故障描述:无法驱动电磁锁;故障等级:2级;故障定义:I02001;");
			}
			// 16
			else if (sFault.equalsIgnoreCase("00080000")) {
				sT2Builder.append("实体:一级-外部设备	二级-电表,故障分类:电表硬件故障;故障等级:2级;故障定义:I02002;");
			}
			// 17
			else if (sFault.equalsIgnoreCase("00100000")) {
				sT2Builder.append("实体:一级-外部设备	二级-电表,故障分类:与主控板通信故障;故障等级:2级;故障定义:I02002;");
			}
			// 18
			else if (sFault.equalsIgnoreCase("00200000")) {
				sT2Builder.append("实体:一级-外部设备	二级-急停,故障分类:急停状态监测	按下后，主控板无法检测到按钮信息输入;故障等级:2级;故障定义:I02003;");
			}
			// 19
			else if (sFault.equalsIgnoreCase("00000010")) {
				sT2Builder.append("实体:一级-外部设备	二级-交流接触器,故障分类:交流接触器状态监测;故障等级:1级;故障定义:I02004;");
			}
			// 20
			else if (sFault.equalsIgnoreCase("00000020")) {
				sT2Builder.append("实体:一级-外部设备	二级-交流接触器,故障分类:交流接触器供电故障;故障等级:1级;故障定义:I02004;");
			}
			// 21
			else if (sFault.equalsIgnoreCase("00000040")) {
				sT2Builder.append("实体:一级-外部设备	二级-BMS供电电源,故障分类:低压电源异常;故障等级:1级;故障定义:I02005;");
			}
			// 22
			else if (sFault.equalsIgnoreCase("00000800")) {
				sT2Builder.append("实体:一级-网络通讯	二级-主控板与后台通讯,故障分类:数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）;故障等级:3级;故障定义:I03001;");
			}
			// 23
			else if (sFault.equalsIgnoreCase("00400000")) {
				sT2Builder.append("实体:一级-网络通讯	二级-主控板与后台通讯,故障分类:数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）;故障等级:2级;故障定义:I03001;");
			}
			// 24
			else if (sFault.equalsIgnoreCase("20000000")) {
				sT2Builder.append("实体:一级-网络通讯	二级-主控板与后台通讯,故障分类:数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）;故障等级:3级;故障定义:I03001;");
			}
			// 25
			else if (sFault.equalsIgnoreCase("00800000")) {
				sT2Builder.append("实体:一级-网络通讯	二级-主控板与后台通讯,故障分类:通讯故障;故障等级:2级;故障定义:I03001;");
			}
			// 26
			else if (sFault.equalsIgnoreCase("00000080")) {
				sT2Builder.append("实体:一级-网络通讯	二级-主控板,故障分类:主控板硬件故障;故障等级:1级;故障定义:I03002;");
			}
			// 27
			else if (sFault.equalsIgnoreCase("00000100")) {
				sT2Builder.append("实体:一级-网络通讯	二级-主控板,故障分类:主控板程序异常;故障等级:1级;故障定义:I03002;");
			}
			// 28
			else if (sFault.equalsIgnoreCase("00000200")) {
				sT2Builder.append("实体:一级-通讯	二级-模块与控制板通讯,故障分类:通讯不稳定;故障等级:1级;故障定义:I04001;");
			}
			// 29
			else if (sFault.equalsIgnoreCase("00000400")) {
				sT2Builder.append("实体:一级-通讯	二级-模块与控制板通讯,故障分类:模块通讯接收端故障;故障等级:1级;故障定义:I04001;");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("系统故障状态不在允许范围内");
				sError.append("系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		} else if (i == 2) {
			if (sFault.equalsIgnoreCase("00000000")) {
				sT2Builder.append("无告警");
			}
			// 4
			else if (sFault.equalsIgnoreCase("00000001")) {
				sT2Builder.append(
						"实体:一级-人机交互　二级-读卡器；故障分类:读卡器通信故障；故障描述:刷卡后，读卡器无响应；属性:无法身份识别，无法完成整体操作流程；是否与运营服务相关:是；等级:1级；处理措施:设备停止充电，后台显示信息“读卡器故障”，由后台系统通知维护人员检测读卡器与主控板通信线路，或更换读卡器设备”；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:系统故障状态");
			}
			// 5
			else if (sFault.equalsIgnoreCase("01000000")) {
				sT2Builder.append(
						"实体:一级-人机交互　二级-读卡器；故障分类:读卡器通信故障；故障描述:刷卡后，读卡器无响应；属性:无法身份识别，无法完成整体操作流程；是否与运营服务相关:否；等级:3级；处理措施:设备无动作，后台显示信息“读卡器故障”，由后台系统通知维护人员检测读卡器与主控板通信线路，或更换读卡器设备”；设备告警显示要求:充电设备正常工作；系统管理分类:系统故障状态");
			}
			// 6
			else if (sFault.equalsIgnoreCase("00000002")) {
				sT2Builder.append(
						"实体:一级-人机交互　二级-读卡器；故障分类:与主控板通信超时故障；故障描述:读卡器无反应或者响应超时，通讯判断时间为2s；属性:无法身份识别，无法完成整体操作流程；是否与运营服务相关:是；等级:	1级；处理措施:设备停止充电，后台显示信息“读卡器与主控板通信故障”，由后台系统通知维护人员检测读卡器与主控板通信线路，或更换读卡器设备”；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:系统故障状态");
			}
			// 7
			else if (sFault.equalsIgnoreCase("02000000")) {
				sT2Builder.append(
						"实体:一级-人机交互　二级-读卡器；故障分类:与主控板通信超时故障；故障描述:读卡器无反应或者响应超时，通讯判断时间为2s；属性:无法身份识别，无法完成整体操作流程	；是否与运营服务相关:否；等级:3级；处理措施:设备无动作，后台显示信息“读卡器与主控板通信故障”，由后台系统通知维护人员检测读卡器与主控板通信线路，或更换读卡器设备”；设备告警显示要求:充电设备正常工作；系统管理分类:系统故障状态");
			}
			// 8
			else if (sFault.equalsIgnoreCase("00000004")) {
				sT2Builder.append(
						"实体:一级-人机交互　二级-显示屏；故障分类:显示屏硬件损坏；故障描述:黑屏、偏色、缺色、画面闪烁、重影；属性:无法正常显示卡信息、工作状态信息，影响整体操作流程；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“显示屏故障”，由后台系统通知维护人员现场检测显示屏与主控板通信线路，或更换显示屏设备”；设备告警显示要求:本地告警指示灯显示“红色”，屏幕已坏，无法显示任何信息	；系统管理分类:系统故障状态");

			}
			// 9
			else if (sFault.equalsIgnoreCase("00000008")) {
				sT2Builder.append(
						"实体:一级-人机交互　二级-显示屏；故障分类:与主控板通信超时故障；故障描述:与主控板通讯线松脱、损坏，提示“通信错误”；属性:无法正常显示卡信息、工作状态信息，影响整体操作流程；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“显示屏故障”，由后台系统通知维护人员现场检测显示屏与主控板通信线路，或更换显示屏设备”；设备告警显示要求:本地告警指示灯显示“红色”，显示屏与主控板通信故障，无法显示故障信息；系统管理分类:系统故障状态");
			}
			// 10
			else if (sFault.equalsIgnoreCase("00010000")) {
				sT2Builder.append(
						"实体:一级-人机交互	二级-键盘；故障分类:键盘硬件损坏；故障描述:按键失效，卡键；属性:无法读取键盘数据，影响整体操作流程；是否与运营服务相关:是；等级:2级；处理措施:完成本次充电任务后设备停止使用，后台显示信息“键盘故障”，由后台系统通知维护人员现场检测键盘与主控板通信线路，或更换键盘设备；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:系统故障状态");
			}
			// 11
			else if (sFault.equalsIgnoreCase("00020000")) {
				sT2Builder.append(
						"实体:一级-人机交互	二级-键盘；故障分类:与主控板通信故障；故障描述:提示“通信错误”；属性:无法读取键盘数据，影响整体操作流程；是否与运营服务相关:是；等级:2级；处理措施:完成本次充电任务后设备停止使用，后台显示信息“键盘故障”，由后台系统通知维护人员现场检测键盘与主控板通信线路，或更换键盘设备；设备告警显示要求:	本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:系统故障状态");
			}
			// 12
			else if (sFault.equalsIgnoreCase("04000000")) {
				sT2Builder.append(
						"实体:一级-人机交互	二级-打印机；故障分类:打印机硬件损坏；故障描述:卡纸，与主控板连接线缆松脱、损坏，数据端口损坏；属性:无法打印结算账单，不影响正常充电；是否与运营服务相关:否；等级:3级；处理措施:设备正常工作，后台显示信息“打印机故障，请检测打印机与主控板通信线路，或更换打印机设备”，由后台系统通知维护人员进行现场维护；设备告警显示要求:不影响设备的正常运行，无任何显示；系统管理分类:系统故障状态");
			}
			// 13
			else if (sFault.equalsIgnoreCase("10000000")) {
				sT2Builder.append(
						"实体:一级-人机交互	二级-打印机；故障分类:缺纸状态（新增）；故障描述:打印机缺少打印纸；属性:　；是否与运营服务相关:　；等级:3级；；处理措施:　；设备告警显示要求:　；系统管理分类:系统故障状态");
			}
			// 14
			else if (sFault.equalsIgnoreCase("08000000")) {
				sT2Builder.append(
						"实体:一级-人机交互	二级-打印机；故障分类:与主控板通信故障；故障描述:检测打印机状态；属性:无法打印结算账单，不影响正常充电；是否与运营服务相关:否；等级:3级；处理措施:设备正常工作，后台显示信息“打印机故障，请检测打印机与主控板通信线路，或更换打印机设备”，由后台系统通知维护人员进行现场维护；设备告警显示要求:不影响设备的正常运行，无任何显示；系统管理分类:系统故障状态");
			}
			// 15
			else if (sFault.equalsIgnoreCase("00040000")) {
				sT2Builder.append(
						"实体:一级-外部设备	二级-电磁锁；故障分类:电磁锁故障；故障描述:无法驱动电磁锁；属性:无法正常进行开闭；是否与运营服务相关:否；等级:2级；处理措施:2级告警，设备不停止工作，后台显示信息“电磁锁故障，请立刻检测电磁锁与主控板通信线路，或更换电磁锁”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	是否停止服务待讨论；系统管理分类:系统故障状态");
			}
			// 16
			else if (sFault.equalsIgnoreCase("00080000")) {
				sT2Builder.append(
						"实体:一级-外部设备	二级-电表；故障分类:电表硬件故障；故障描述:死机、无脉冲输出；属性:电表无法工作；是否与运营服务相关:是；等级:2级；处理措施:2级告警，设备不停止工作，后台显示信息“电表故障，请检测电能表与主控板通信线路，或更换电能表”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	是否停止服务待讨论；系统管理分类:系统故障状态");
			}
			// 17
			else if (sFault.equalsIgnoreCase("00100000")) {
				sT2Builder.append(
						"实体:一级-外部设备	二级-电表；故障分类:与主控板通信故障；故障描述:无法向主控板传输电量信息；属性:电表通讯异常；是否与运营服务相关:是；等级:2级；处理措施:2级告警，设备不停止工作，后台显示信息“电表故障，请检测电能表与主控板通信线路，或更换电能表”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	是否停止服务待讨论；系统管理分类:系统故障状态");
			}
			// 18
			else if (sFault.equalsIgnoreCase("00200000")) {
				sT2Builder.append(
						"实体:一级-外部设备	二级-急停；故障分类:急停状态监测	按下后，主控板无法检测到按钮信息输入；故障描述:无法获得急停按钮状态；是否与运营服务相关:否；等级:2级；处理措施:2级告警，设备不停止工作，后台显示信息“急停按钮故障，请检测急停按钮与主控板通信线路，或更换急停按钮”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	是否停止服务待讨论；系统管理分类:系统故障状态");
			}
			// 19
			else if (sFault.equalsIgnoreCase("00000010")) {
				sT2Builder.append(
						"实体:一级-外部设备	二级-交流接触器；故障分类:交流接触器状态监测；故障描述:供电后，交流接触器无动作；属性:交流继电器无法正常动作；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“交流继电器故障，请检测交流继电器与主控板通信线路，或更换交流继电器”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:系统故障状态");
			}
			// 20
			else if (sFault.equalsIgnoreCase("00000020")) {
				sT2Builder.append(
						"实体:一级-外部设备	二级-交流接触器；故障分类:交流接触器供电故障；故障描述:供电后，交流接触器无法检测到电压；属性:未给交流继电器供电；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“交流继电器故障，请检测交流继电器与主控板通信线路，或更换交流继电器”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:系统故障状态");
			}
			// 21
			else if (sFault.equalsIgnoreCase("00000040")) {
				sT2Builder.append(
						"实体:一级-外部设备	二级-BMS供电电源；故障分类:低压电源异常；故障描述:电源输出不稳定，电压输出异常；属性:充电设备无法正常启动；是否与运营服务相关:是；等级:1级；处理措施:设备停止充电，后台显示信息“BMS供电电源故障”，数据补传后，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:系统故障状态");
			}
			// 22
			else if (sFault.equalsIgnoreCase("00000040")) {
				sT2Builder.append(
						"实体:一级-网络通讯	二级-主控板与后台通讯；故障分类:数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）；故障描述:数据传输出现数据丢失；属性:数据传输异常；是否与运营服务相关:是；等级:3级（ 15%＜丢包率，1级）；处理措施:3级告警，后台显示信息“数据异常，请检测充电机与后台的数据传输”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:系统故障状态");
			}
			// 23
			else if (sFault.equalsIgnoreCase("00400000")) {
				sT2Builder.append(
						"实体:一级-网络通讯	二级-主控板与后台通讯；故障分类:数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）；故障描述:数据传输出现数据丢失；属性:　；是否与运营服务相关:　；等级:1%＜丢包率≤15%，2级		；处理措施:　；设备告警显示要求:　；系统管理分类:系统故障状态");
			}
			// 24
			else if (sFault.equalsIgnoreCase("20000000")) {
				sT2Builder.append(
						"实体:一级-网络通讯	二级-主控板与后台通讯；故障分类:数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）；故障描述:数据传输出现数据丢失；属性:　；是否与运营服务相关:　；等级:丢包率≤1%，3级；处理措施:　；设备告警显示要求:　；系统管理分类:系统故障状态");
			}
			// 25
			else if (sFault.equalsIgnoreCase("00800000")) {
				sT2Builder.append(
						"实体:一级-网络通讯	二级-主控板与后台通讯；故障分类:通讯故障；故障描述:与后台通讯断开；属性:数据记录异常；是否与运营服务相关:是；等级:2级；处理措施:	3级告警，后台显示信息“数据异常，请检测充电机与后台的数据传输”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:系统故障状态");
			}
			// 26
			else if (sFault.equalsIgnoreCase("00000080")) {
				sT2Builder.append(
						"实体:一级-网络通讯	二级-主控板；故障分类:主控板硬件故障；故障描述:供电后，主控板无法正常工作；属性:主控板无法正常工作；是否与运营服务相关:是；等级:1级；处理措施:	后台显示信息“主控板故障，请检测主控板，或更换主控板”，数据补传后由后台系统通知维护人员进行现场维护；设备告警显示要求:无故障信息显示；系统管理分类:系统故障状态");
			}
			// 27
			else if (sFault.equalsIgnoreCase("00000100")) {
				sT2Builder.append(
						"实体:一级-网络通讯	二级-主控板；故障分类:主控板程序异常；故障描述:供电后，主控板程序无法正常运行；属性:主控板无法正常工作；是否与运营服务相关:是；等级:1级；处理措施:	后台显示信息“主控板故障，请检测主控板，或更换主控板”，数据补传后由后台系统通知维护人员进行现场维护；设备告警显示要求:无故障信息显示；系统管理分类:系统故障状态");
			}
			// 28
			else if (sFault.equalsIgnoreCase("00000200")) {
				sT2Builder.append(
						"实体:一级-通讯	二级-模块与控制板通讯；故障分类:通讯不稳定；故障描述:控制板与模块通讯不稳定；属性:模块与控制板通讯质量差；是否与运营服务相关:否；等级:1级；处理措施:	模块停机，后台显示信息“模块与控制板通讯异常，请检测模块与控制板通信线路”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:系统故障状态");
			}
			// 29
			else if (sFault.equalsIgnoreCase("00000400")) {
				sT2Builder.append(
						"实体:一级-通讯	二级-模块与控制板通讯；故障分类:模块通讯接收端故障；故障描述:控制板无法与模块进行通讯；属性:模块与控制板通讯不上；是否与运营服务相关:是；等级:1级；处理措施:	模块停机，后台显示信息“模块与控制板通讯断开，请检测模块与控制板通信线路”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:系统故障状态");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("系统故障状态不在允许范围内");
				sError.append("系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		} else {
			// 3
			if (sFault.equalsIgnoreCase("00000000")) {
				sT2Builder.append("无告警");
			}
			// 4
			else if (sFault.equalsIgnoreCase("00000001")) {
				sT2Builder.append(
						"一级-人机交互　二级-读卡器；读卡器通信故障；刷卡后，读卡器无响应；无法身份识别，无法完成整体操作流程；是；1级；设备停止充电，后台显示信息“读卡器故障”，由后台系统通知维护人员检测读卡器与主控板通信线路，或更换读卡器设备”；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统故障状态");
			}
			// 5
			else if (sFault.equalsIgnoreCase("01000000")) {
				sT2Builder.append(
						"一级-人机交互　二级-读卡器；读卡器通信故障；刷卡后，读卡器无响应；无法身份识别，无法完成整体操作流程；否；3级；设备无动作，后台显示信息“读卡器故障”，由后台系统通知维护人员检测读卡器与主控板通信线路，或更换读卡器设备”；充电设备正常工作；系统故障状态");
			}
			// 6
			else if (sFault.equalsIgnoreCase("00000002")) {
				sT2Builder.append(
						"一级-人机交互　二级-读卡器；与主控板通信超时故障；读卡器无反应或者响应超时，通讯判断时间为2s；无法身份识别，无法完成整体操作流程；是；1级；设备停止充电，后台显示信息“读卡器与主控板通信故障”，由后台系统通知维护人员检测读卡器与主控板通信线路，或更换读卡器设备”；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统故障状态");
			}
			// 7
			else if (sFault.equalsIgnoreCase("02000000")) {
				sT2Builder.append(
						"一级-人机交互　二级-读卡器；与主控板通信超时故障；读卡器无反应或者响应超时，通讯判断时间为2s；无法身份识别，无法完成整体操作流程	；否；3级；设备无动作，后台显示信息“读卡器与主控板通信故障”，由后台系统通知维护人员检测读卡器与主控板通信线路，或更换读卡器设备”；充电设备正常工作；系统故障状态");
			}
			// 8
			else if (sFault.equalsIgnoreCase("00000004")) {
				sT2Builder.append(
						"一级-人机交互　二级-显示屏；显示屏硬件损坏；黑屏、偏色、缺色、画面闪烁、重影；无法正常显示卡信息、工作状态信息，影响整体操作流程；是；1级；模块停机，后台显示信息“显示屏故障”，由后台系统通知维护人员现场检测显示屏与主控板通信线路，或更换显示屏设备”；本地告警指示灯显示“红色”，屏幕已坏，无法显示任何信息	；系统故障状态");

			}
			// 9
			else if (sFault.equalsIgnoreCase("00000008")) {
				sT2Builder.append(
						"一级-人机交互　二级-显示屏；与主控板通信超时故障；与主控板通讯线松脱、损坏，提示“通信错误”；无法正常显示卡信息、工作状态信息，影响整体操作流程；是；1级；模块停机，后台显示信息“显示屏故障”，由后台系统通知维护人员现场检测显示屏与主控板通信线路，或更换显示屏设备”；本地告警指示灯显示“红色”，显示屏与主控板通信故障，无法显示故障信息；系统故障状态");
			}
			// 10
			else if (sFault.equalsIgnoreCase("00010000")) {
				sT2Builder.append(
						"一级-人机交互	二级-键盘；键盘硬件损坏；按键失效，卡键；无法读取键盘数据，影响整体操作流程；是；2级；完成本次充电任务后设备停止使用，后台显示信息“键盘故障”，由后台系统通知维护人员现场检测键盘与主控板通信线路，或更换键盘设备；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统故障状态");
			}
			// 11
			else if (sFault.equalsIgnoreCase("00020000")) {
				sT2Builder.append(
						"一级-人机交互	二级-键盘；与主控板通信故障；提示“通信错误”；无法读取键盘数据，影响整体操作流程；是；2级；完成本次充电任务后设备停止使用，后台显示信息“键盘故障”，由后台系统通知维护人员现场检测键盘与主控板通信线路，或更换键盘设备；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统故障状态");
			}
			// 12
			else if (sFault.equalsIgnoreCase("04000000")) {
				sT2Builder.append(
						"一级-人机交互	二级-打印机；打印机硬件损坏；卡纸，与主控板连接线缆松脱、损坏，数据端口损坏；无法打印结算账单，不影响正常充电；否；3级；设备正常工作，后台显示信息“打印机故障，请检测打印机与主控板通信线路，或更换打印机设备”，由后台系统通知维护人员进行现场维护；不影响设备的正常运行，无任何显示；系统故障状态");
			}
			// 13
			else if (sFault.equalsIgnoreCase("10000000")) {
				sT2Builder.append("一级-人机交互	二级-打印机；缺纸状态（新增）；打印机缺少打印纸；　；　　；3级；；　　；　；系统故障状态");
			}
			// 14
			else if (sFault.equalsIgnoreCase("08000000")) {
				sT2Builder.append(
						"一级-人机交互	二级-打印机；与主控板通信故障；检测打印机状态；无法打印结算账单，不影响正常充电；否；3级；设备正常工作，后台显示信息“打印机故障，请检测打印机与主控板通信线路，或更换打印机设备”，由后台系统通知维护人员进行现场维护；不影响设备的正常运行，无任何显示；系统故障状态");
			}
			// 15
			else if (sFault.equalsIgnoreCase("00040000")) {
				sT2Builder.append(
						"一级-外部设备	二级-电磁锁；电磁锁故障；无法驱动电磁锁；无法正常进行开闭；否；2级；2级告警，设备不停止工作，后台显示信息“电磁锁故障，请立刻检测电磁锁与主控板通信线路，或更换电磁锁”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统故障状态");
			}
			// 16
			else if (sFault.equalsIgnoreCase("00080000")) {
				sT2Builder.append(
						"一级-外部设备	二级-电表；电表硬件故障；死机、无脉冲输出；电表无法工作；是；2级；2级告警，设备不停止工作，后台显示信息“电表故障，请检测电能表与主控板通信线路，或更换电能表”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统故障状态");
			}
			// 17
			else if (sFault.equalsIgnoreCase("00100000")) {
				sT2Builder.append(
						"一级-外部设备	二级-电表；与主控板通信故障；无法向主控板传输电量信息；电表通讯异常；是；2级；2级告警，设备不停止工作，后台显示信息“电表故障，请检测电能表与主控板通信线路，或更换电能表”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统故障状态");
			}
			// 18
			else if (sFault.equalsIgnoreCase("00200000")) {
				sT2Builder.append(
						"一级-外部设备	二级-急停；急停状态监测；按下后，主控板无法检测到按钮信息输入；无法获得急停按钮状态；否；2级；2级告警，设备不停止工作，后台显示信息“急停按钮故障，请检测急停按钮与主控板通信线路，或更换急停按钮”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统故障状态");
			}
			// 19
			else if (sFault.equalsIgnoreCase("00000010")) {
				sT2Builder.append(
						"一级-外部设备	二级-交流接触器；交流接触器状态监测；供电后，交流接触器无动作；交流继电器无法正常动作；是；1级；模块停机，后台显示信息“交流继电器故障，请检测交流继电器与主控板通信线路，或更换交流继电器”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统故障状态");
			}
			// 20
			else if (sFault.equalsIgnoreCase("00000020")) {
				sT2Builder.append(
						"一级-外部设备	二级-交流接触器；交流接触器供电故障；供电后，交流接触器无法检测到电压；未给交流继电器供电；是；1级；模块停机，后台显示信息“交流继电器故障，请检测交流继电器与主控板通信线路，或更换交流继电器”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统故障状态");
			}
			// 21
			else if (sFault.equalsIgnoreCase("00000040")) {
				sT2Builder.append(
						"一级-外部设备	二级-BMS供电电源；低压电源异常；电源输出不稳定，电压输出异常；充电设备无法正常启动；是；1级；设备停止充电，后台显示信息“BMS供电电源故障”，数据补传后，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统故障状态");
			}
			// 22
			else if (sFault.equalsIgnoreCase("00000800")) {
				sT2Builder.append(
						"一级-网络通讯	二级-主控板与后台通讯；数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）；数据传输出现数据丢失；数据传输异常；是；3级（ 15%＜丢包率，1级）；3级告警，后台显示信息“数据异常，请检测充电机与后台的数据传输”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统故障状态");
			}
			// 23
			else if (sFault.equalsIgnoreCase("00400000")) {
				sT2Builder.append(
						"一级-网络通讯	二级-主控板与后台通讯；数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）；数据传输出现数据丢失；　　；　　；1%＜丢包率≤15%，2级		；　　；　　；系统故障状态");
			}
			// 24
			else if (sFault.equalsIgnoreCase("20000000")) {
				sT2Builder.append(
						"一级-网络通讯	二级-主控板与后台通讯；数据丢包率10-3、10-6（数据传输错误、数据接收数据包差错，分三级）；数据传输出现数据丢失；　　；　　；丢包率≤1%，3级；　　；　；系统故障状态");
			}
			// 25
			else if (sFault.equalsIgnoreCase("00800000")) {
				sT2Builder.append(
						"一级-网络通讯	二级-主控板与后台通讯；通讯故障；与后台通讯断开；数据记录异常；是；2级；3级告警，后台显示信息“数据异常，请检测充电机与后台的数据传输”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统故障状态");
			}
			// 26
			else if (sFault.equalsIgnoreCase("00000080")) {
				sT2Builder.append(
						"一级-网络通讯	二级-主控板；主控板硬件故障；供电后，主控板无法正常工作；主控板无法正常工作；是；1级；后台显示信息“主控板故障，请检测主控板，或更换主控板”，数据补传后由后台系统通知维护人员进行现场维护；无故障信息显示；系统故障状态");
			}
			// 27
			else if (sFault.equalsIgnoreCase("00000100")) {
				sT2Builder.append(
						"一级-网络通讯	二级-主控板；主控板程序异常；供电后，主控板程序无法正常运行；主控板无法正常工作；是；1级；后台显示信息“主控板故障，请检测主控板，或更换主控板”，数据补传后由后台系统通知维护人员进行现场维护；无故障信息显示；系统故障状态");
			}
			// 28
			else if (sFault.equalsIgnoreCase("00000200")) {
				sT2Builder.append(
						"一级-通讯	二级-模块与控制板通讯；通讯不稳定；控制板与模块通讯不稳定；模块与控制板通讯质量差；否；1级；模块停机，后台显示信息“模块与控制板通讯异常，请检测模块与控制板通信线路”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统故障状态");
			}
			// 29
			else if (sFault.equalsIgnoreCase("00000400")) {
				sT2Builder.append(
						"一级-通讯	二级-模块与控制板通讯；模块通讯接收端故障；控制板无法与模块进行通讯；模块与控制板通讯不上；是；1级；模块停机，后台显示信息“模块与控制板通讯断开，请检测模块与控制板通信线路”，由后台系统通知维护人员进行现场维护；本地告警指示灯显示“红色”，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统故障状态");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("　；　；　；　；　；　；　；　；系统故障状态");
				sError.append("系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}
	public static List<String> four_batterySystemFaultbyteToHexStr(byte[] bsrc,int type){
		if (bsrc.length <2) {
			return null;
		}
		List<byte[]> s = new ArrayList<byte[]>();
		
		for (int i = 0; i < bsrc.length; i++) {
			if (bsrc[i] == 0)
				continue;
			if ((bsrc[i] & 0x80) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x80;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x40) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x40;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x20) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x20;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x10) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x10;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x08) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x08;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x04) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x04;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x02) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x02;
				s.add(bArray);
			}
			if ((bsrc[i] & 0x01) != 0) {
				byte[] bArray = new byte[4];
				bArray[i] = (byte) 0x01;
				s.add(bArray);
			}

		}
		
		
		List<String> result = new ArrayList<String>();
		

	if(s.isEmpty()){
	result.add("无告警");
	}else{
	for(byte[] bArray:s){
		result.add(BatterySystemFaultbyteToHexStr(bArray,type)[1]);
	}
	}

	return result;
		
	}
public static List<String> batterySystemFaultbyteToHexStr(byte[] bsrc,int type){
	if (bsrc.length <2) {
		return null;
	}
	List<byte[]> s = new ArrayList<byte[]>();
	
	for (int i = 0; i < bsrc.length; i++) {
		if (bsrc[i] == 0)
			continue;
		if ((bsrc[i] & 0x80) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x80;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x40) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x40;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x20) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x20;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x10) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x10;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x08) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x08;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x04) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x04;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x02) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x02;
			s.add(bArray);
		}
		if ((bsrc[i] & 0x01) != 0) {
			byte[] bArray = new byte[2];
			bArray[i] = (byte) 0x01;
			s.add(bArray);
		}

	}
	
	
	List<String> result = new ArrayList<String>();
	

if(s.isEmpty()){
result.add("无告警");
}else{
for(byte[] bArray:s){
	result.add(BatterySystemFaultbyteToHexStr(bArray,type)[1]);
}
}

return result;
	
}
	// 电池系统故障2字节转成16进制字符串:
	public static String[] BatterySystemFaultbyteToHexStr(byte[] bsrc, int i) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		String sFault = CommFunction.bytesToHexString(bsrc);
		if (i == 1) {
			//
		//	if (sFault.equalsIgnoreCase("0000")) {
		//		sT2Builder.append("无告警");
		//	}
			// 64
		//	else 
				if (sFault.equalsIgnoreCase("0004")) {
				sT2Builder.append(" 实体:一级-充电过程	二级-单体电池,故障分类:单体电池电压过高");
			}
			// 65
			else if (sFault.equalsIgnoreCase("0008")) {
				sT2Builder.append("实体:一级-充电过程	二级-单体电池,故障分类:单体电池温度过高");
			}
			// 66
			else if (sFault.equalsIgnoreCase("0010")) {
				sT2Builder.append("实体:一级-充电过程	二级-BMS,故障分类:BMS错误");
			}
			// 67
			else if (sFault.equalsIgnoreCase("0020")) {
				sT2Builder.append("实体:一级-充电过程	二级-BMS,故障分类:BMS通信超时");
			}
			// 68
			else if (sFault.equalsIgnoreCase("0040")) {
				sT2Builder.append("实体:一级-充电过程	二级-整体电池,故障分类:电池反接");

			}
			// 69
			else if (sFault.equalsIgnoreCase("0080")) {
				sT2Builder.append("实体:一级-充电过程	二级-整体电池,故障分类:电池未接");
			}
			// 70
			else if (sFault.equalsIgnoreCase("0100")) {
				sT2Builder.append("实体:一级-充电过程	二级-整体电池,故障分类:电池总电压过高");
			}
			// 71
			else if (sFault.equalsIgnoreCase("0200")) {
				sT2Builder.append("实体:一级-充电过程	二级-整体电池,故障分类:电池总电压过低");
			}
			// 72
			else if (sFault.equalsIgnoreCase("0400")) {
				sT2Builder.append("实体:一级-充电过程	二级-整体电池,故障分类:SOC过高");
			}
			// 73
			else if (sFault.equalsIgnoreCase("0800")) {
				sT2Builder.append("实体:一级-充电过程	二级-整体电池,故障分类:电池组绝缘故障");
			}
			// 74
			else if (sFault.equalsIgnoreCase("1000")) {
				sT2Builder.append("实体:一级-充电过程	二级-整体电池,故障分类:电池组输出连接器故障");
			}
			// 75
			else if (sFault.equalsIgnoreCase("2000")) {
				sT2Builder.append("实体:一级-充电过程	二级-充电线路连接,故障分类:CC1故障（连接确认故障）");
			} else if (sFault.equalsIgnoreCase("04000000")) {
				sT2Builder.append("实体:一级-充电过程	二级-充电线路连接,故障分类:CC1故障（连接确认故障）");
			} 
			 else if (sFault.equalsIgnoreCase("08000000")) {
					sT2Builder.append("实体:一级-充电过程	二级-充电线路连接,故障分类:CC1故障（连接确认故障）");
				} 
			else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("电池系统故障状态不在允许范围内");
				sError.append("电池系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		} else if (i == 2) {
			//if (sFault.equalsIgnoreCase("0000")) {
			//	sT2Builder.append("无告警");
			//}
			/// 64
			//else 
				if (sFault.equalsIgnoreCase("0004")) {
				sT2Builder.append(
						" 实体:一级-充电过程	二级-单体电池；故障分类:单体电池电压过高；故障描述:单体电池电压高于限定值；属性:电池故障，单体电池电压过高；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“单体电池电压过高，请检查电池设备”，由后台系统通知运维人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			}
			// 65
			else if (sFault.equalsIgnoreCase("0008")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-单体电池；故障分类:单体电池温度过高；故障描述:单体电池温度高于限定值；属性:电池故障，单体电池温度过高；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“单体电池温度过高，请检查电池设备”，由后台系统通知运维人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			}
			// 66
			else if (sFault.equalsIgnoreCase("0010")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-BMS；故障分类:BMS错误；故障描述:充电机无法与车辆通信；属性:BMS故障；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“BMS错误，请检查BMS系统”，由后台系统通知运维人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			}
			// 67
			else if (sFault.equalsIgnoreCase("0020")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-BMS；故障分类:BMS通信超时；故障描述:充电机无法与车辆通信；属性:BMS故障；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“BMS通信超时，请检查BMS系统”，由后台系统通知运维人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障 ");
			}
			// 68
			else if (sFault.equalsIgnoreCase("0040")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-整体电池；故障分类:电池反接；故障描述:电池正反级接反；属性:电池故障，外部连接故障；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“电池反接，请检查电池连接”，由后台系统通知运维人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");

			}
			// 69
			else if (sFault.equalsIgnoreCase("0080")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-整体电池；故障分类:电池未接；故障描述:电池未正常连接；属性:物理连接失败，外部连接故障；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“电池未接，请检查电池连接”，由后台系统通知运维人员，正确连接电池；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			}
			// 70
			else if (sFault.equalsIgnoreCase("0100")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-整体电池；故障分类:电池总电压过高；故障描述:电池电压高于限定值；属性:电池故障，电池电压过高；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“电池电压过高，请检查电池设备”，由后台系统通知运维人员现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			}
			// 71
			else if (sFault.equalsIgnoreCase("0200")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-整体电池；故障分类:电池总电压过低；故障描述:电池电压低于限定值；属性:电池故障，电池电压过低；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“电池电压过低，请检查电池设备”，由后台系统通知运维人员现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			}
			// 72
			else if (sFault.equalsIgnoreCase("0400")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-整体电池；故障分类:SOC过高；故障描述:SOC高于限定值；属性:BMS故障；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“SOC过高，请检查电池设备”，由后台系统通知运维人员现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			}
			// 73
			else if (sFault.equalsIgnoreCase("0800")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-整体电池；故障分类:电池组绝缘故障；故障描述:无法正常充电，绝缘故障报警；属性:电池故障；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“电池组绝缘故障，请检查电池设备”，由后台系统通知运维人员现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			}
			// 74
			else if (sFault.equalsIgnoreCase("1000")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-整体电池；故障分类:电池组输出连接器故障；故障描述:无法正常充电，连接器故障报警；属性:电池故障；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“电池组输出连接器故障，请检查电池设备”，由后台系统通知运维人员现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:电池系统故障");
			}
			// 75
			else if (sFault.equalsIgnoreCase("2000")) {
				sT2Builder.append(
						"实体:一级-充电过程	二级-充电线路连接；故障分类:CC1故障（连接确认故障）；故障描述:	BMS故障；属性:	BMS辨识超时	；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息:“连接确认故障，请检查充电线路”，由后台系统通知运维人员现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:电池系统故障");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("系统故障状态不在允许范围内");
				sError.append("电池系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		} else {
			//if (sFault.equalsIgnoreCase("0000")) {
			//	sT2Builder.append("无告警");
			//}
			// 64
			//else
			if (sFault.equalsIgnoreCase("0004")) {
				sT2Builder.append(
						" 一级-充电过程	二级-单体电池；单体电池电压过高；单体电池电压高于限定值；电池故障，单体电池电压过高；是；1级；模块停机，后台显示信息“单体电池电压过高，请检查电池设备”，由后台系统通知运维人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			}
			// 65
			else if (sFault.equalsIgnoreCase("0008")) {
				sT2Builder.append(
						"一级-充电过程	二级-单体电池；单体电池温度过高；单体电池温度高于限定值；电池故障，单体电池温度过高；是；1级；模块停机，后台显示信息“单体电池温度过高，请检查电池设备”，由后台系统通知运维人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			}
			// 66
			else if (sFault.equalsIgnoreCase("0010")) {
				sT2Builder.append(
						"一级-充电过程	二级-BMS；BMS错误；充电机无法与车辆通信；BMS故障；是；1级；模块停机，后台显示信息“BMS错误，请检查BMS系统”，由后台系统通知运维人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			}
			// 67
			else if (sFault.equalsIgnoreCase("0020")) {
				sT2Builder.append(
						"一级-充电过程	二级-BMS；BMS通信超时；充电机无法与车辆通信；BMS故障；是；1级；模块停机，后台显示信息“BMS通信超时，请检查BMS系统”，由后台系统通知运维人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障 ");
			}
			// 68
			else if (sFault.equalsIgnoreCase("0040")) {
				sT2Builder.append(
						"一级-充电过程	二级-整体电池；电池反接；电池正反级接反；电池故障，外部连接故障；是；1级；模块停机，后台显示信息“电池反接，请检查电池连接”，由后台系统通知运维人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");

			}
			// 69
			else if (sFault.equalsIgnoreCase("0080")) {
				sT2Builder.append(
						"一级-充电过程	二级-整体电池；电池未接；电池未正常连接；物理连接失败，外部连接故障；是；1级；模块停机，后台显示信息“电池未接，请检查电池连接”，由后台系统通知运维人员，正确连接电池；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			}
			// 70
			else if (sFault.equalsIgnoreCase("0100")) {
				sT2Builder.append(
						"一级-充电过程	二级-整体电池；电池总电压过高；电池电压高于限定值；电池故障，电池电压过高；是；1级；模块停机，后台显示信息“电池电压过高，请检查电池设备”，由后台系统通知运维人员现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			}
			// 71
			else if (sFault.equalsIgnoreCase("0200")) {
				sT2Builder.append(
						"一级-充电过程	二级-整体电池；电池总电压过低；电池电压低于限定值；电池故障，电池电压过低；是；1级；模块停机，后台显示信息“电池电压过低，请检查电池设备”，由后台系统通知运维人员现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			}
			// 72
			else if (sFault.equalsIgnoreCase("0400")) {
				sT2Builder.append(
						"实一级-充电过程	二级-整体电池；SOC过高；SOC高于限定值；BMS故障；是；1级；模块停机，后台显示信息“SOC过高，请检查电池设备”，由后台系统通知运维人员现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			}
			// 73
			else if (sFault.equalsIgnoreCase("0800")) {
				sT2Builder.append(
						"一级-充电过程	二级-整体电池；电池组绝缘故障；无法正常充电，绝缘故障报警；电池故障；是；1级；模块停机，后台显示信息“电池组绝缘故障，请检查电池设备”，由后台系统通知运维人员现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			}
			// 74
			else if (sFault.equalsIgnoreCase("1000")) {
				sT2Builder.append(
						"一级-充电过程	二级-整体电池；电池组输出连接器故障；无法正常充电，连接器故障报警；电池故障；是；1级；模块停机，后台显示信息“电池组输出连接器故障，请检查电池设备”，由后台系统通知运维人员现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”	；电池系统故障");
			}
			// 75
			else if (sFault.equalsIgnoreCase("2000")) {
				sT2Builder.append(
						"一级-充电过程	二级-充电线路连接；CC1故障（连接确认故障）；BMS故障；	BMS辨识超时	；是；1级；处模块停机，后台显示信息“连接确认故障，请检查充电线路”，由后台系统通知运维人员现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；电池系统故障");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("　；　；　；　；　；　；　；　；电池系统故障");
				sError.append("电池系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	// 充电系统故障4字节转成16进制字符串:符号是英文状态下的符号
	public static String[] ChargeSystemFaultbyteToHexStr(byte[] bsrc, int i) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		String sFault = CommFunction.bytesToHexString(bsrc);
		if (i == 1) {
			//if (sFault.equalsIgnoreCase("00000000")) {
			///	sT2Builder.append("无告警");
			//}
			// 47
			//else
				if (sFault.equalsIgnoreCase("00000001")) {
				sT2Builder.append("实体:一级-整机系统   二级-绝缘故障; 故障分类:绝缘电阻值小于阀值  ; 故障等级:1级  ;故障定义:I06001;");
			}
			// 48
			else if (sFault.equalsIgnoreCase("00000002")) {
				sT2Builder.append("实体:一级-整机系统	二级-绝缘故障；故障分类:绝缘模块故障（通信判断进一步确认）;故障等级:1级;故障定义:I06001;");
			}
			// 49
			else if (sFault.equalsIgnoreCase("01000000")) {
				sT2Builder.append("实体:一级-整机系统	二级-内部过温；故障分类:风扇故障;故障等级:3级;故障定义:I06002;");
			}
			// 50
			else if (sFault.equalsIgnoreCase("00010000")) {
				sT2Builder.append("实体:一级-整机系统	二级-内部过温；故障分类:出风口温度过高；故障等级:2级;故障定义:I06002;");
			}
			// 51
			else if (sFault.equalsIgnoreCase("00000004")) {
				sT2Builder.append("实体:一级-整机系统	二级-内部过温；故障分类:进风口温度过高;故障等级:1级 ;故障定义:I06002;");

			}
			// 52
			else if (sFault.equalsIgnoreCase("04000000")) {
				sT2Builder.append("实体:一级-整机系统	二级-充电机存储器；故障分类:读取数据失败; 故障等级:3级;故障定义:I06003;");
			}
			// 53
			else if (sFault.equalsIgnoreCase("08000000")) {
				sT2Builder.append("实体:一级-整机系统	二级-充电机存储器；故障分类:存储器已满; 故障等级:3级;故障定义:I06003;");
			}
			// 54
			else if (sFault.equalsIgnoreCase("00000008")) {
				sT2Builder.append("实体:一级-电网侧	二级-输入电压；故障分类:输入过压;故障等级:1级 ;故障定义:I07001;");
			}
			// 55
			else if (sFault.equalsIgnoreCase("00000010")) {
				sT2Builder.append("实体:一级-电网侧	二级-输入电压；故障分类:输入欠压;故障等级:1级;故障定义:I07001;");
			}
			// 56
			else if (sFault.equalsIgnoreCase("00000020")) {
				sT2Builder.append("实体:一级-电网侧	二级-输入缺相；故障分类:输入缺相;故障等级:1级;故障定义:I07002;");
			}
			// 57
			else if (sFault.equalsIgnoreCase("00000040")) {
				sT2Builder.append("实体:一级-电网侧	二级-输入电流；故障分类:输入过流;故障等级:1级;故障定义:I07003;");
			}
			// 58
			else if (sFault.equalsIgnoreCase("00000080")) {
				sT2Builder.append("实体:一级-电网侧	二级-谐波；故障分类:谐波超限（预留）按国标最低容限;故障等级:1级;故障定义:I07004;");
			}
			// 59
			else if (sFault.equalsIgnoreCase("00000100")) {
				sT2Builder.append("实体:一级-输出侧	二级-输出电压；故障分类:输出过压;故障等级:1级;故障定义:I08001;");
			}
			// 60
			else if (sFault.equalsIgnoreCase("00000200")) {
				sT2Builder.append("实体:一级-输出侧	二级-输出电压；故障分类:输出欠压;故障等级:1级;故障定义:I08001;");
			}
			// 61
			else if (sFault.equalsIgnoreCase("00000400")) {
				sT2Builder.append("实体:一级-输出侧	二级-输出电流；故障分类:输出过流;故障等级:1级;故障定义:I08002;");
			}
			// 62
			else if (sFault.equalsIgnoreCase("00000800")) {
				sT2Builder.append("实体:一级-输出侧	二级-输出线路；故障分类:输出短路;故障等级:1级;故障定义:I08003;");
			}
			// 63
			else if (sFault.equalsIgnoreCase("00001000")) {
				sT2Builder.append("实体:一级-输出侧	二级-输出线路；故障分类:充电模块无输出电压、电流;故障等级:1级;故障定义:I08003;");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("充电系统故障状态不在允许范围内");
				sError.append("充电系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		} else if (i == 2) {
			//if (sFault.equalsIgnoreCase("00000000")) {
			//	sT2Builder.append("无告警");
			//}
			// 47
			//else 
				if (sFault.equalsIgnoreCase("00000001")) {
				sT2Builder.append(
						"实体:一级-整机系统	二级-绝缘故障；故障分类:绝缘电阻值小于阀值；故障描述:绝缘检测模块检测输出正负极对地的绝缘电阻小于阀值；属性:绝缘检测故障，导致设备无法正常充电；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“充电设备绝缘故障，请检查绝缘模块，或者输出高压线路”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电系统故障状态");
			}
			// 48
			else if (sFault.equalsIgnoreCase("00000002")) {
				sT2Builder.append(
						"实体:一级-整机系统	二级-绝缘故障；故障分类:绝缘模块故障（通信判断进一步确认）；故障描述:绝缘模块检测绝缘电阻值不稳定，或无法检测；属性:绝缘检测故障，导致设备无法正常充电；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“充电设备绝缘故障，请检查绝缘模块，或者输出高压线路”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电系统故障状态");
			}
			// 49
			else if (sFault.equalsIgnoreCase("01000000")) {
				sT2Builder.append(
						"实体:一级-整机系统	二级-内部过温；故障分类:风扇故障；故障描述:风扇转速降低、停机；属性:充电机内部温度过高；是否与运营服务相关:是；等级:60≤温度＜65℃:3级；处理措施:3级告警，降功率80%运行后台显示信息“整机内部温度过高，请检查整机内部”，由后台系统通知维护人员进行现场维护；设备告警显示要求:设备正常显示，正常工作；系统管理分类:充电系统故障状态");
			}
			// 50
			else if (sFault.equalsIgnoreCase("00010000")) {
				sT2Builder.append(
						"实体:一级-整机系统	二级-内部过温；故障分类:出风口温度过高；故障描述:出风口温度过高；属性:充电机内部温度过高；是否与运营服务相关:是；等级:65≤温度＜70℃:2级；处理措施:2级告警，降功率50%运行，后台显示信息“整机内部温度过高，请检查整机内部”，由后台系统通知维护人员进行现场维护；设备告警显示要求:设备正常显示，正常工作；系统管理分类:充电系统故障状态");
			}
			// 51
			else if (sFault.equalsIgnoreCase("00000004")) {
				sT2Builder.append(
						"实体:一级-整机系统	二级-内部过温；故障分类:进风口温度过高；故障描述:进风口温度过高；属性:充电机内部温度过高；是否与运营服务相关:是；等级:70℃≤温度:1级；处理措施:模块停机，后台显示信息“整机进风口温度过高，请检查整机内部”，由后台系统通知维护人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电系统故障状态");

			}
			// 52
			else if (sFault.equalsIgnoreCase("04000000")) {
				sT2Builder.append(
						"实体:一级-整机系统	二级-充电机存储器；故障分类:读取数据失败；故障描述:无法读取数据；属性:读取或存储数据失败；是否与运营服务相关:是；等级:3级；处理措施:3级告警，后台显示信息“充电机存储器异常，请检查存储器模块”，由后台系统通知维护人员进行现场维护；设备告警显示要求:设备正常工作；系统管理分类:充电系统故障状态 ");
			}
			// 53
			else if (sFault.equalsIgnoreCase("08000000")) {
				sT2Builder.append(
						"实体:一级-整机系统	二级-充电机存储器；故障分类:存储器已满；故障描述:无法存储参数信息；属性:读取或存储数据失败；是否与运营服务相关:是；等级:3级；处理措施:3级告警，后台显示信息“充电机存储器异常，请检查存储器模块”，由后台系统通知维护人员进行现场维护；设备告警显示要求:设备正常工作；系统管理分类:充电系统故障状态 ");
			}
			// 54
			else if (sFault.equalsIgnoreCase("00000008")) {
				sT2Builder.append(
						"实体:一级-电网侧	二级-输入电压；故障分类:输入过压；故障描述:交流电压输入过压；属性:交流输入电压过高，电网电压异常波动，充电机停机保护；是否与运营服务相关:是；等级:380V+15%＜输入电压:1级；处理措施:充电机停机工作，后台显示信息“交流输入过压，请检查配电输入系统”，由后台系统通知运维人员进行现场维护；设备告警显示要求:充电设备已断电，无任何信息显示；系统管理分类:充电系统故障状态");
			}
			// 55
			else if (sFault.equalsIgnoreCase("00000010")) {
				sT2Builder.append(
						"实体:一级-电网侧	二级-输入电压；故障分类:输入欠压；故障描述:交流电压输入欠压；属性:交流输入电压过低，电网电压异常波动，充电机停机保护；是否与运营服务相关:是；等级:输入电压＜380V-15%:1级	；处理措施:充电机停机工作，后台显示信息“交流输入欠压，请检查配电输入系统”，由后台系统通知运维人员进行现场维护	；设备告警显示要求:充电设备已断电，无任何信息显示；系统管理分类:充电系统故障状态");
			}
			// 56
			else if (sFault.equalsIgnoreCase("00000020")) {
				sT2Builder.append(
						"实体:一级-电网侧	二级-输入缺相；故障分类:输入缺相；故障描述:电网A、B、C三相供电系统中，有一相由于故障断电，其他两相之间的线电压不变；属性:交流输入电压缺相，电网缺相故障，充电机停机保护；是否与运营服务相关:是；等级:1级；处理措施:充电机停机工作，后台显示信息“交流输入电压缺相，请检查配电输入系统”，由后台系统通知运维人员进行现场维护；设备告警显示要求:充电设备已断电，无任何信息显示；系统管理分类:充电系统故障状态 ");
			}
			// 57
			else if (sFault.equalsIgnoreCase("00000040")) {
				sT2Builder.append(
						"实体:一级-电网侧	二级-输入电流；故障分类:输入过流；故障描述:超出额定输入电流；属性:交流输入过流，变压器过负荷运行，导致充电机模块故障；是否与运营服务相关:是；等级:1级；处理措施:充电机停机工作，后台显示信息“交流输入过流，请检查配电输入系统”，由后台系统通知运维人员进行现场维护；设备告警显示要求:充电设备已断电，无任何信息显示；系统管理分类:充电系统故障状态");
			}
			// 58
			else if (sFault.equalsIgnoreCase("00000080")) {
				sT2Builder.append(
						"实体:一级-电网侧	二级-谐波；故障分类:谐波超限（预留）按国标最低容限；故障描述:交流输入频率超出50Hz±1Hz范围；属性:交流输入频率异常，电网异常波动，充电机停机保护；是否与运营服务相关:是；等级:1级；处理措施:充电机停机工作，后台显示信息“交流输入频率异常”，由后台系统通知运维人员进行现场维护；设备告警显示要求:充电设备已断电，无任何信息显示；系统管理分类:充电系统故障状态");
			}
			// 59
			else if (sFault.equalsIgnoreCase("00000100")) {
				sT2Builder.append(
						"实体:一级-输出侧	二级-输出电压；故障分类:输出过压；故障描述:　；属性:充电机模块故障，充电机无法工作充电；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“直流输出过压，充电机维修或者充电机更换”，由后台系统通知运维人员进行现场维护；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电系统故障状态");
			}
			// 60
			else if (sFault.equalsIgnoreCase("00000200")) {
				sT2Builder.append(
						"实体:一级-输出侧	二级-输出电压；故障分类:输出欠压；故障描述:　；属性:充电机模块故障，充电机无法工作充电；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“直流输出欠压，请检查充电设备”，由后台系统通知运维人员，充电机维修或者充电机更换；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电系统故障状态");
			}
			// 61
			else if (sFault.equalsIgnoreCase("00000400")) {
				sT2Builder.append(
						"实体:一级-输出侧	二级-输出电流；故障分类:输出过流；故障描述:超出额定输出电流+10%；属性:充电机模块故障，充电机无法工作充电；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“超出额定输出过流，请检查充电设备”，由后台系统通知运维人员，充电机维修或者充电机更换；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”	；系统管理分类:充电系统故障状态");
			}
			// 62
			else if (sFault.equalsIgnoreCase("00000800")) {
				sT2Builder.append(
						"实体:一级-输出侧	二级-输出线路；故障分类:输出短路；故障描述:充电机短路保护开关跳闸；属性:充电模块或系统故障，充电机无法工作充电；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“输出短路，请检查充电设备”，由后台系统通知运维人员，充电机维修或者充电机更换；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电系统故障状态");
			}
			// 63
			else if (sFault.equalsIgnoreCase("00001000")) {
				sT2Builder.append(
						"实体:一级-输出侧	二级-输出线路；故障分类:充电模块无输出电压、电流；故障描述:充电模块故障断开；属性:充电模块或系统故障，充电机无法工作充电；是否与运营服务相关:是；等级:1级；处理措施:模块停机，后台显示信息“输出断路，请检查充电设备”，由后台系统通知运维人员，充电机维修或者充电机更换；设备告警显示要求:本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；系统管理分类:充电系统故障状态");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("系统故障状态不在允许范围内");
				sError.append("充电系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		} else {
			//
			//if (sFault.equalsIgnoreCase("00000000")) {
			//	sT2Builder.append("无告警");
			//}
			// 47
			//else
				if (sFault.equalsIgnoreCase("00000001")) {
				sT2Builder.append(
						"一级-整机系统	二级-绝缘故障；绝缘电阻值小于阀值；绝缘检测模块检测输出正负极对地的绝缘电阻小于阀值；绝缘检测故障，导致设备无法正常充电；是；1级；模块停机，后台显示信息“充电设备绝缘故障，请检查绝缘模块，或者输出高压线路”，由后台系统通知维护人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电系统故障状态");
			}
			// 48
			else if (sFault.equalsIgnoreCase("00000002")) {
				sT2Builder.append(
						"一级-整机系统	二级-绝缘故障；绝缘模块故障（通信判断进一步确认）；绝缘模块检测绝缘电阻值不稳定，或无法检测；绝缘检测故障，导致设备无法正常充电；是；1级；模块停机，后台显示信息“充电设备绝缘故障，请检查绝缘模块，或者输出高压线路”，由后台系统通知维护人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电系统故障状态");
			}
			// 49
			else if (sFault.equalsIgnoreCase("01000000")) {
				sT2Builder.append(
						"一级-整机系统	二级-内部过温；风扇故障；风扇转速降低、停机；充电机内部温度过高；是；60≤温度＜65℃:3级；3级告警，降功率80%运行后台显示信息“整机内部温度过高，请检查整机内部”，由后台系统通知维护人员进行现场维护；设备正常显示，正常工作；充电系统故障状态");
			}
			// 50
			else if (sFault.equalsIgnoreCase("00010000")) {
				sT2Builder.append(
						"一级-整机系统	二级-内部过温；出风口温度过高；出风口温度过高；充电机内部温度过高；是；65≤温度＜70℃:2级；2级告警，降功率50%运行，后台显示信息“整机内部温度过高，请检查整机内部”，由后台系统通知维护人员进行现场维护；设备正常显示，正常工作；充电系统故障状态");
			}
			// 51
			else if (sFault.equalsIgnoreCase("00000004")) {
				sT2Builder.append(
						"一级-整机系统	二级-内部过温；进风口温度过高；进风口温度过高；充电机内部温度过高；是；70℃≤温度:1级；模块停机，后台显示信息“整机进风口温度过高，请检查整机内部”，由后台系统通知维护人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电系统故障状态");

			}
			// 52
			else if (sFault.equalsIgnoreCase("04000000")) {
				sT2Builder.append(
						"一级-整机系统	二级-充电机存储器；读取数据失败；无法读取数据；读取或存储数据失败；是；3级；3级告警，后台显示信息“充电机存储器异常，请检查存储器模块”，由后台系统通知维护人员进行现场维护；设备正常工作；充电系统故障状态");
			}
			// 53
			else if (sFault.equalsIgnoreCase("08000000")) {
				sT2Builder.append(
						"一级-整机系统	二级-充电机存储器；存储器已满；无法存储参数信息；读取或存储数据失败；是；3级；3级告警，后台显示信息“充电机存储器异常，请检查存储器模块”，由后台系统通知维护人员进行现场维护；设备正常工作；充电系统故障状态 ");
			}
			// 54
			else if (sFault.equalsIgnoreCase("00000008")) {
				sT2Builder.append(
						"一级-电网侧	二级-输入电压；输入过压；交流电压输入过压；交流输入电压过高，电网电压异常波动，充电机停机保护；是；380V+15%＜输入电压:1级；充电机停机工作，后台显示信息“交流输入过压，请检查配电输入系统”，由后台系统通知运维人员进行现场维护；充电设备已断电，无任何信息显示；充电系统故障状态");
			}
			// 55
			else if (sFault.equalsIgnoreCase("00000010")) {
				sT2Builder.append(
						"一级-电网侧	二级-输入电压；输入欠压；交流电压输入欠压；交流输入电压过低，电网电压异常波动，充电机停机保护；是；输入电压＜380V-15%:1级	；充电机停机工作，后台显示信息“交流输入欠压，请检查配电输入系统”，由后台系统通知运维人员进行现场维护	；充电设备已断电，无任何信息显示；充电系统故障状态");
			}
			// 56
			else if (sFault.equalsIgnoreCase("00000020")) {
				sT2Builder.append(
						"一级-电网侧	二级-输入缺相；输入缺相；电网A、B、C三相供电系统中，有一相由于故障断电，其他两相之间的线电压不变；交流输入电压缺相，电网缺相故障，充电机停机保护；是；1级；充电机停机工作，后台显示信息“交流输入电压缺相，请检查配电输入系统”，由后台系统通知运维人员进行现场维护；充电设备已断电，无任何信息显示；充电系统故障状态 ");
			}
			// 57
			else if (sFault.equalsIgnoreCase("00000040")) {
				sT2Builder.append(
						"一级-电网侧	二级-输入电流；输入过流；超出额定输入电流；交流输入过流，变压器过负荷运行，导致充电机模块故障；是；1级；充电机停机工作，后台显示信息“交流输入过流，请检查配电输入系统”，由后台系统通知运维人员进行现场维护；充电设备已断电，无任何信息显示；充电系统故障状态");
			}
			// 58
			else if (sFault.equalsIgnoreCase("00000080")) {
				sT2Builder.append(
						"一级-电网侧	二级-谐波；谐波超限（预留）按国标最低容限；交流输入频率超出50Hz±1Hz范围；交流输入频率异常，电网异常波动，充电机停机保护；是；1级；充电机停机工作，后台显示信息“交流输入频率异常”，由后台系统通知运维人员进行现场维护；充电设备已断电，无任何信息显示；充电系统故障状态");
			}
			// 59
			else if (sFault.equalsIgnoreCase("00000100")) {
				sT2Builder.append(
						"一级-输出侧	二级-输出电压；输出过压；　；充电机模块故障，充电机无法工作充电；是；1级；模块停机，后台显示信息“直流输出过压，充电机维修或者充电机更换”，由后台系统通知运维人员进行现场维护；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电系统故障状态");
			}
			// 60
			else if (sFault.equalsIgnoreCase("00000200")) {
				sT2Builder.append(
						"一级-输出侧	二级-输出电压；输出欠压；　；充电机模块故障，充电机无法工作充电；是；1级；模块停机，后台显示信息“直流输出欠压，请检查充电设备”，由后台系统通知运维人员，充电机维修或者充电机更换；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电系统故障状态");
			}
			// 61
			else if (sFault.equalsIgnoreCase("00000400")) {
				sT2Builder.append(
						"一级-输出侧	二级-输出电流；输出过流；超出额定输出电流+10%；充电机模块故障，充电机无法工作充电；是；1级；模块停机，后台显示信息“超出额定输出过流，请检查充电设备”，由后台系统通知运维人员，充电机维修或者充电机更换；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”	；充电系统故障状态");
			}
			// 62
			else if (sFault.equalsIgnoreCase("00000800")) {
				sT2Builder.append(
						"一级-输出侧	二级-输出线路；输出短路；充电机短路保护开关跳闸；充电模块或系统故障，充电机无法工作充电；是；1级；模块停机，后台显示信息“输出短路，请检查充电设备”，由后台系统通知运维人员，充电机维修或者充电机更换；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电系统故障状态");
			}
			// 63
			else if (sFault.equalsIgnoreCase("00001000")) {
				sT2Builder.append(
						"一级-输出侧	二级-输出线路；充电模块无输出电压、电流；充电模块故障断开；充电模块或系统故障，充电机无法工作充电；是；1级；模块停机，后台显示信息“输出断路，请检查充电设备”，由后台系统通知运维人员，充电机维修或者充电机更换；本地告警提示灯显示红色，显示屏显示“充电设备故障，暂时无法提供充电服务”；充电系统故障状态");
			} else {
				// ResultDto.setCheckContent("无法解析!");
				sT2Builder.append("　；　；　；　；　；　；　；　；充电系统故障状态");
				sError.append("充电系统故障状态值不正确");
				// ResultDto.setErrorInfo("系统故障状态值不正确");
				// ResultDto.setT2CheckResult("T2不正确");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	//////////////////////////////////////////////////
	// 车况报警状态
	public static String[] StateFaultbyteTointStr(byte[] bsrc) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		StringBuilder bitStr = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			bitStr.append(CommFunction.byteConvertToStr(bsrc[i]));
		}
		String sFault = bitStr.toString();
		if (sFault.equalsIgnoreCase("00000000000000000000000000000000")) {
			sT2Builder.append("无告警");
		} else {
			// 1
			if ((sFault.substring(31, 32)).equalsIgnoreCase("1")) {
				sT2Builder.append("<传感器0>ACC有效,");
			}
			// 2
			if ((sFault.substring(30, 31)).equalsIgnoreCase("1")) {
				sT2Builder.append("超速,");
			}
			// 3
			if ((sFault.substring(29, 30)).equalsIgnoreCase("1")) {
				sT2Builder.append("<传感器1>输入高电平,");
			}
			// 4
			if ((sFault.substring(28, 29)).equalsIgnoreCase("1")) {
				sT2Builder.append("任意点停留超时,");
			}
			// 5
			if ((sFault.substring(27, 28)).equalsIgnoreCase("1")) {
				sT2Builder.append("用户按键报警,");

			}
			// 6
			if ((sFault.substring(26, 27)).equalsIgnoreCase("1")) {
				sT2Builder.append("GPS天线短路,");
			}
			// 7
			if ((sFault.substring(25, 26)).equalsIgnoreCase("1")) {
				sT2Builder.append("GPS天线断路,");
			}
			// 8
			if ((sFault.substring(24, 25)).equalsIgnoreCase("1")) {
				sT2Builder.append("GPS信号报警,");
			}
			// 10
			if ((sFault.substring(22, 23)).equalsIgnoreCase("1")) {
				sT2Builder.append("GPS信息,");
			}
			// 12
			if ((sFault.substring(20, 21)).equalsIgnoreCase("1")) {
				sT2Builder.append("充电状态,");
			}
			// 18
			if ((sFault.substring(14, 15)).equalsIgnoreCase("1")) {
				sT2Builder.append("<传感器2>输入低电平,");
			}
			// 20
			if ((sFault.substring(12, 13)).equalsIgnoreCase("1")) {
				sT2Builder.append("满载、空驶状态,");
			}
			// 21
			if ((sFault.substring(11, 12)).equalsIgnoreCase("1")) {
				sT2Builder.append("越界报警,");
			}
			// 23
			if ((sFault.substring(9, 10)).equalsIgnoreCase("1")) {
				sT2Builder.append("<传感器3>输入低电平,");
			}
			// 24
			if ((sFault.substring(8, 9)).equalsIgnoreCase("1")) {
				sT2Builder.append("补传数据标志,");
			}
			// 25
			if ((sFault.substring(7, 8)).equalsIgnoreCase("1")) {
				sT2Builder.append("定位跟踪标志,");
			} // 26
			if ((sFault.substring(6, 7)).equalsIgnoreCase("1")) {
				sT2Builder.append("点名标志,");
			} // 27
			if ((sFault.substring(5, 6)).equalsIgnoreCase("1")) {
				sT2Builder.append("<传感器4>输入高电平,");
			} // 28
			if ((sFault.substring(4, 5)).equalsIgnoreCase("1")) {
				sT2Builder.append("<传感器5>输入高电平,");
			} // 29
			if ((sFault.substring(3, 4)).equalsIgnoreCase("1")) {
				sT2Builder.append("疲劳驾驶标志,");
			} // 30
			if ((sFault.substring(2, 3)).equalsIgnoreCase("1")) {
				sT2Builder.append("蓄电池报警,");
			} // 31
			if ((sFault.substring(1, 2)).equalsIgnoreCase("1")) {
				sT2Builder.append("车辆断电标志,");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}
	// 一个字节转成10进制整型的str，加个错误判断。

	// 电池系统故障代码
	public static String[] BatterySystemFaultToBitStr(byte[] bsrc) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		String srcFault = CommFunction.byteConvertToStr(bsrc[0]);
		if (srcFault.equalsIgnoreCase("00000000")) {
			sT2Builder.append("无告警");
		} else {
			String sFault = srcFault.substring(0, 6);
			// 1
			if (sFault.equalsIgnoreCase("000001")) {
				sT2Builder.append("正极继电器粘连");
			}
			// 2
			else if (sFault.equalsIgnoreCase("000010")) {
				sT2Builder.append("正极继电器不能吸合");
			}
			// 3
			else if (sFault.equalsIgnoreCase("000011")) {
				sT2Builder.append("负极继电器粘连");
			}
			// 4
			else if (sFault.equalsIgnoreCase("000100")) {
				sT2Builder.append("负极继电器不能吸合");
			}
			// 5
			else if (sFault.equalsIgnoreCase("000101")) {
				sT2Builder.append("充电继电器粘连");
			}
			// 6
			else if (sFault.equalsIgnoreCase("000110")) {
				sT2Builder.append("充电继电器不能吸合");
			}
			// 7
			else if (sFault.equalsIgnoreCase("000111")) {
				sT2Builder.append("预充电继电器粘连");
			}
			// 8
			else if (sFault.equalsIgnoreCase("001000")) {
				sT2Builder.append("预充电继电器不能吸合");
			}
			// 9
			else if (sFault.equalsIgnoreCase("001001")) {
				sT2Builder.append("风机堵转");
			}
			// 10
			else if (sFault.equalsIgnoreCase("001010")) {
				sT2Builder.append("风机开路");
			}
			// 11
			else if (sFault.equalsIgnoreCase("001011")) {
				sT2Builder.append("加热器不能开启");
			}
			// 12
			else if (sFault.equalsIgnoreCase("001100")) {
				sT2Builder.append("加热器不能停止");
			}
			// 13
			else if (sFault.equalsIgnoreCase("001101")) {
				sT2Builder.append("烟雾报警");
			}
			// 14
			else if (sFault.equalsIgnoreCase("001110")) {
				sT2Builder.append("下电异常");
			}
			// 15
			else if (sFault.equalsIgnoreCase("001111")) {
				sT2Builder.append("保险损坏");
			}
			// 16
			else if (sFault.equalsIgnoreCase("010000")) {
				sT2Builder.append("预充电超时");
			} // 17
			else if (sFault.equalsIgnoreCase("010001")) {
				sT2Builder.append("保险过温");
			} // 18
			else if (sFault.equalsIgnoreCase("010010")) {
				sT2Builder.append("极柱过温");
			} // 19
			else if (sFault.equalsIgnoreCase("010011")) {
				sT2Builder.append("加热器过温");
			} // 20
			else if (sFault.equalsIgnoreCase("010100")) {
				sT2Builder.append("电池组绝缘故障");
			} // 21
			else if (sFault.equalsIgnoreCase("010101")) {
				sT2Builder.append("充/放电连接器接触不良");
			} // 22
			else if (sFault.equalsIgnoreCase("010110")) {
				sT2Builder.append("充/放电连接器过温");
			} // 23
			else if (sFault.equalsIgnoreCase("010111")) {
				sT2Builder.append("12V/24V启动电源欠压故障");
			} // 27
			else {
				sT2Builder.append("电池系统故障状态值不正确");
				sError.append("电池系统故障状态值不正确");
			}
			String sFault2 = srcFault.substring(6, 8);
			if (sFault2.equalsIgnoreCase("01")) {
				sT2Builder.append("一级故障");
			} else if (sFault2.equalsIgnoreCase("10")) {
				sT2Builder.append("二级故障");
			} else if (sFault2.equalsIgnoreCase("11")) {
				sT2Builder.append("三级故障");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	// 电池故障代码
	public static String[] BatteryFaultToBitStr(byte[] bsrc) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		String srcFault = CommFunction.byteConvertToStr(bsrc[0]);
		if (srcFault.equalsIgnoreCase("00000000")) {
			sT2Builder.append("无告警");
		} else {
			String sFault = srcFault.substring(0, 6);
			// 1
			if (sFault.equalsIgnoreCase("000001")) {
				sT2Builder.append("电池温度过高");
			}
			// 2
			else if (sFault.equalsIgnoreCase("000010")) {
				sT2Builder.append("电池充电温度过低");
			}
			// 3
			else if (sFault.equalsIgnoreCase("000011")) {
				sT2Builder.append("电池放电温度过低");
			}
			// 4
			else if (sFault.equalsIgnoreCase("000100")) {
				sT2Builder.append("单体电压过高");
			}
			// 5
			else if (sFault.equalsIgnoreCase("000101")) {
				sT2Builder.append("单体电压过低");
			}
			// 6
			else if (sFault.equalsIgnoreCase("000110")) {
				sT2Builder.append("模块电压过高");
			}
			// 7
			else if (sFault.equalsIgnoreCase("000111")) {
				sT2Builder.append("模块电压过低");
			}
			// 8
			else if (sFault.equalsIgnoreCase("001000")) {
				sT2Builder.append("总电压过高");
			}
			// 9
			else if (sFault.equalsIgnoreCase("001001")) {
				sT2Builder.append("总电压过低");
			}
			// 10
			else if (sFault.equalsIgnoreCase("001010")) {
				sT2Builder.append("SOC过高");
			}
			// 11
			else if (sFault.equalsIgnoreCase("001011")) {
				sT2Builder.append("SOC过低1");
			}
			// 12
			else if (sFault.equalsIgnoreCase("001100")) {
				sT2Builder.append("单体电池压差过大");
			}
			// 13
			else if (sFault.equalsIgnoreCase("001101")) {
				sT2Builder.append("温度不一致");
			}
			// 14
			else if (sFault.equalsIgnoreCase("001110")) {
				sT2Builder.append("充电电流过大");
			}
			// 15
			else if (sFault.equalsIgnoreCase("001111")) {
				sT2Builder.append("放电电流过大");
			}
			// 16
			else if (sFault.equalsIgnoreCase("010000")) {
				sT2Builder.append("温度上升过快");
			} // 17
			else if (sFault.equalsIgnoreCase("010001")) {
				sT2Builder.append("短路保护");
			} // 27
			else {
				sT2Builder.append("电池故障状态值不正确");
				sError.append("电池故障状态值不正确");
			}
			String sFault2 = srcFault.substring(6, 8);
			if (sFault2.equalsIgnoreCase("01")) {
				sT2Builder.append("一级故障");
			} else if (sFault2.equalsIgnoreCase("10")) {
				sT2Builder.append("二级故障");
			} else if (sFault2.equalsIgnoreCase("11")) {
				sT2Builder.append("三级故障");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	// ＢＭＳ故障代码
	public static String[] BMSFaultToBitStr(byte[] bsrc) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		String srcFault = CommFunction.byteConvertToStr(bsrc[0]);
		if (srcFault.equalsIgnoreCase("00000000")) {
			sT2Builder.append("无告警");
		} else {
			String sFault = srcFault.substring(0, 6);
			// 1
			if (sFault.equalsIgnoreCase("000001")) {
				sT2Builder.append("电流检测模块故障");
			}
			// 2
			else if (sFault.equalsIgnoreCase("000010")) {
				sT2Builder.append("总电压检测模块故障");
			}
			// 3
			else if (sFault.equalsIgnoreCase("000011")) {
				sT2Builder.append("环境温度测量模块故障");
			}
			// 4
			else if (sFault.equalsIgnoreCase("000100")) {
				sT2Builder.append("烟雾传感器故障");
			}
			// 5
			else if (sFault.equalsIgnoreCase("000101")) {
				sT2Builder.append("碰撞传感器故障");
			}
			// 6
			else if (sFault.equalsIgnoreCase("000110")) {
				sT2Builder.append("绝缘电阻测量模块故障");
			}
			// 7
			else if (sFault.equalsIgnoreCase("000111")) {
				sT2Builder.append("单体电压模块测量故障");
			}
			// 8
			else if (sFault.equalsIgnoreCase("001000")) {
				sT2Builder.append("温度测量模块故障");
			}
			// 9
			else if (sFault.equalsIgnoreCase("001001")) {
				sT2Builder.append("BMS非易失存储器或EEPROM故障");
			}
			// 10
			else if (sFault.equalsIgnoreCase("001010")) {
				sT2Builder.append("BMS RAM失效");
			}
			// 11
			else if (sFault.equalsIgnoreCase("001011")) {
				sT2Builder.append("外部时钟失效");
			}
			// 12
			else if (sFault.equalsIgnoreCase("001100")) {
				sT2Builder.append("BMS故障指示灯控制电路故障");
			}
			// 13
			else if (sFault.equalsIgnoreCase("001101")) {
				sT2Builder.append("BMS数据支持系统故障");
			}
			// 14
			else if (sFault.equalsIgnoreCase("001110")) {
				sT2Builder.append("内部总线超时");
			}
			// 15
			else if (sFault.equalsIgnoreCase("001111")) {
				sT2Builder.append("充电总线超时");
			}
			// 16
			else if (sFault.equalsIgnoreCase("010000")) {
				sT2Builder.append("整车总线超时");
			} // 17
			else if (sFault.equalsIgnoreCase("010001")) {
				sT2Builder.append("BMS供电电源异常报警");
			} // 27
			else {
				sT2Builder.append("BMS故障状态值不正确");
				sError.append("BMS故障状态值不正确");
			}
			String sFault2 = srcFault.substring(6, 8);
			if (sFault2.equalsIgnoreCase("01")) {
				sT2Builder.append("一级故障");
			} else if (sFault2.equalsIgnoreCase("10")) {
				sT2Builder.append("二级故障");
			} else if (sFault2.equalsIgnoreCase("11")) {
				sT2Builder.append("三级故障");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	// 整车故障代码
	public static String[] WholeCarFaultToBitStr(byte[] bsrc) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		bsrc = CommFunction.reserveByteArray(bsrc);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			String srcFault = CommFunction.byteConvertToStr(bsrc[i]);
			sb.append(srcFault);
		}
		String sFault = sb.toString();
		if (sFault.equalsIgnoreCase("00000000000000000000000000000000")) {
			sT2Builder.append("无告警");
		} else {
			// 1
			if ((sFault.substring(31, 32)).equalsIgnoreCase("1")) {
				sT2Builder.append("母线电流过流,");
			}
			// 2
			if ((sFault.substring(30, 31)).equalsIgnoreCase("1")) {
				sT2Builder.append("模块故障,");
			}
			// 3
			if ((sFault.substring(29, 30)).equalsIgnoreCase("1")) {
				sT2Builder.append("母线电压过压,");
			}
			// 4
			if ((sFault.substring(28, 29)).equalsIgnoreCase("1")) {
				sT2Builder.append("电机超速,");
			}
			// 5
			if ((sFault.substring(27, 28)).equalsIgnoreCase("1")) {
				sT2Builder.append("电机温度过热,");

			}
			// 6
			if ((sFault.substring(26, 27)).equalsIgnoreCase("1")) {
				sT2Builder.append("电机控制器温度过热,");
			}
			// 7
			if ((sFault.substring(25, 26)).equalsIgnoreCase("1")) {
				sT2Builder.append("母线电压欠压,");
			}
			// 8
			if ((sFault.substring(24, 25)).equalsIgnoreCase("1")) {
				sT2Builder.append("相电流过载,");
			}
			// 9
			if ((sFault.substring(23, 24)).equalsIgnoreCase("1")) {
				sT2Builder.append("加速踏板故障,");
			}
			// 10
			if ((sFault.substring(22, 23)).equalsIgnoreCase("1")) {
				sT2Builder.append("档位故障,");
			}
			// 11
			if ((sFault.substring(21, 22)).equalsIgnoreCase("1")) {
				sT2Builder.append("电动转向故障,");
			}
			// 12
			if ((sFault.substring(20, 21)).equalsIgnoreCase("1")) {
				sT2Builder.append("12V欠压,");
			}
			// 13
			if ((sFault.substring(19, 20)).equalsIgnoreCase("1")) {
				sT2Builder.append("预充电失败,");
			}
			// 14
			if ((sFault.substring(18, 19)).equalsIgnoreCase("1")) {
				sT2Builder.append("电机IGBT故障,");
			}
			// 15
			if ((sFault.substring(17, 18)).equalsIgnoreCase("1")) {
				sT2Builder.append("压缩机内部电压异常故障,");
			}
			// 16
			if ((sFault.substring(16, 17)).equalsIgnoreCase("1")) {
				sT2Builder.append("降功率行驶故障,");
			}
			// 17
			if ((sFault.substring(15, 16)).equalsIgnoreCase("1")) {
				sT2Builder.append("压缩机输入电压过压故障,");
			}
			// 18
			if ((sFault.substring(14, 15)).equalsIgnoreCase("1")) {
				sT2Builder.append("压缩机输入电压欠压故障,");
			} // 19
			if ((sFault.substring(13, 14)).equalsIgnoreCase("1")) {
				sT2Builder.append("AC过流保护故障,");
			}
			// 20
			if ((sFault.substring(12, 13)).equalsIgnoreCase("1")) {
				sT2Builder.append("AC电机堵转故障,");
			}
			// 21
			if ((sFault.substring(11, 12)).equalsIgnoreCase("1")) {
				sT2Builder.append("压缩机过温故障,");
			}
			// 22
			if ((sFault.substring(10, 11)).equalsIgnoreCase("1")) {
				sT2Builder.append("跛行行驶故障,");
			}
			// 23
			if ((sFault.substring(9, 10)).equalsIgnoreCase("1")) {
				sT2Builder.append("充电机故障,");
			}
			// 24
			if ((sFault.substring(8, 9)).equalsIgnoreCase("1")) {
				sT2Builder.append("DC-DC故障,");
			}
			// 25
			if ((sFault.substring(7, 8)).equalsIgnoreCase("1")) {
				sT2Builder.append("电动空调故障,");
			} // 26
			if ((sFault.substring(6, 7)).equalsIgnoreCase("1")) {
				sT2Builder.append("真空泵故障,");
			} // 27
			if ((sFault.substring(5, 6)).equalsIgnoreCase("1")) {
				sT2Builder.append("电机冷却系统故障,");
			} // 28
			if ((sFault.substring(4, 5)).equalsIgnoreCase("1")) {
				sT2Builder.append("12V过压,");
			} // 29
			if ((sFault.substring(3, 4)).equalsIgnoreCase("1")) {
				sT2Builder.append("系统故障,");
			} // 30
			if ((sFault.substring(2, 3)).equalsIgnoreCase("1")) {
				sT2Builder.append("CAN总线故障,");
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	public static String[] unsignByteToIntEx(String command, byte data, String str) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		int result = 0;
		int hightestBit = ((data >>> 7) & 0x01);
		result = (data & 0x7F);
		if (1 == hightestBit) {
			result = result | 0x80;
		}
		// 对该字节的值做判断:
		if (str.equalsIgnoreCase("工作状态")) {
			if (result == 1) {
				sT2Builder.append("管理状态");
			} else if (result == 2) {
				sT2Builder.append("服务状态");
			} else if (result == 3) {
				sT2Builder.append("故障状态");
			} else if (result == 9) {
				sT2Builder.append("初始化状态");
			} else {
				sError.append("工作状态的值不在范围内");
				sT2Builder.append("工作状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("输入电源状态")) {
			if (result == 1) {
				sT2Builder.append("正常");
			} else if (result == 2) {
				sT2Builder.append("异常");
			} else {
				sError.append("输入电源状态值不在范围内");
				sT2Builder.append("输入电源状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("执行控制")) {
			if (result == 1) {
				sT2Builder.append("立即执行");
			} else if (result == 2) {
				sT2Builder.append("空闲执行");
			} else if (result == 3) {
				sT2Builder.append("加电执行");
			} else {
				sError.append("执行控制的值不在范围内");
				sT2Builder.append("执行控制的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("确认升级")) {
			if (result == 0) {
				sT2Builder.append("开始升级");
			} else if (result == 1) {
				sT2Builder.append("异常，不执行升级");
			} else {
				sError.append("确认升级的值不在范围内");
				sT2Builder.append("确认升级的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备1")) {
			if (result == 1) {
				sT2Builder.append("环境板未配置");
			} else if (result == 2) {
				sT2Builder.append("环境板未检测到");
			} else if (result == 3) {
				sT2Builder.append("环境板工作正常");
			} else if (result == 4) {
				sT2Builder.append("环境板正常待机");
			} else if (result == 5) {
				sT2Builder.append("环境板故障");
			} else {
				sError.append("环境板状态的值不在范围内");
				sT2Builder.append("环境板状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备2")) {
			if (result == 1) {
				sT2Builder.append("POS机未配置");
			} else if (result == 2) {
				sT2Builder.append("POS机未检测到");
			} else if (result == 3) {
				sT2Builder.append("POS机工作正常");
			} else if (result == 4) {
				sT2Builder.append("POS机正常待机");
			} else if (result == 5) {
				sT2Builder.append("POS机故障");
			} else {
				sError.append("POS机状态的值不在范围内");
				sT2Builder.append("POS机状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备3")) {
			if (result == 1) {
				sT2Builder.append("电表未配置");
			} else if (result == 2) {
				sT2Builder.append("电表未检测到");
			} else if (result == 3) {
				sT2Builder.append("电表工作正常");
			} else if (result == 4) {
				sT2Builder.append("电表正常待机");
			} else if (result == 5) {
				sT2Builder.append("电表故障");
			} else {
				sError.append("电表的值不在范围内");
				sT2Builder.append("电表的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备4")) {
			if (result == 1) {
				sT2Builder.append("UPS未配置");
			} else if (result == 2) {
				sT2Builder.append("UPS未检测到");
			} else if (result == 3) {
				sT2Builder.append("UPS工作正常");
			} else if (result == 4) {
				sT2Builder.append("UPS正常待机");
			} else if (result == 5) {
				sT2Builder.append("UPS故障");
			} else {
				sError.append("UPS状态的值不在范围内");
				sT2Builder.append("UPS状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备5")) {
			if (result == 1) {
				sT2Builder.append("充电系统未配置");
			} else if (result == 2) {
				sT2Builder.append("充电系统未检测到");
			} else if (result == 3) {
				sT2Builder.append("充电系统工作正常");
			} else if (result == 4) {
				sT2Builder.append("充电系统正常待机");
			} else if (result == 5) {
				sT2Builder.append("充电系统故障");
			} else {
				sError.append("充电系统状态的值不在范围内");
				sT2Builder.append("充电系统状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备6")) {
			if (result == 1) {
				sT2Builder.append("显示屏幕未配置");
			} else if (result == 2) {
				sT2Builder.append("显示屏幕未检测到");
			} else if (result == 3) {
				sT2Builder.append("显示屏幕工作正常");
			} else if (result == 4) {
				sT2Builder.append("显示屏幕正常待机");
			} else if (result == 5) {
				sT2Builder.append("显示屏幕故障");
			} else {
				sError.append("显示屏幕状态的值不在范围内");
				sT2Builder.append("显示屏幕状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备7") || str.equalsIgnoreCase("下游设备8")) {
			if (result == 1) {
				sT2Builder.append("未配置");
			} else if (result == 2) {
				sT2Builder.append("未检测到");
			} else if (result == 3) {
				sT2Builder.append("工作正常");
			} else if (result == 4) {
				sT2Builder.append("正常待机");
			} else if (result == 5) {
				sT2Builder.append("故障");
			} else {
				sError.append("状态的值不在范围内");
				sT2Builder.append("状态的值不在范围内");
			}
		} else if (command.equals("1A") && str.equalsIgnoreCase("参数类型")) {
			if (result == 1) {
				sT2Builder.append("公司信息");
			} else if (result == 2) {
				sT2Builder.append("网站信息");
			} else if (result == 3) {
				sT2Builder.append("客服电话");
			} else if (result == 4) {
				sT2Builder.append("本网点收费信息");
			} else if (result == 5) {
				sT2Builder.append("服务接口信息");
			} else if (result == 6) {
				sT2Builder.append("注意事项");
			} else if (result == 7) {
				sT2Builder.append("二维码动态首字段");
			} else {
				sError.append("参数类型的值不在范围内");
				sT2Builder.append("参数类型的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("参数类型")) {
			if (result == 2) {
				sT2Builder.append("设置充电机启停");
			} else if (result == 3) {
				sT2Builder.append("设置输出功率");
			} else if (result == 4) {
				sT2Builder.append("设置单体最高允许电压");
			} else if (result == 5) {
				sT2Builder.append("设置单体最高允许温度");
			} else if (result == 6) {
				sT2Builder.append("设置语音提示功能启停");
			} else if (result == 7) {
				sT2Builder.append("设置充电设备服务启停");
			} else {
				sError.append("参数类型的值不在范围内");
				sT2Builder.append("参数类型的值不在范围内");
			}
		} else if (str.contains("充电模块位置编号")) {
			if (result == 0) {
				sT2Builder.append("所有");
			} else if (result > 0 && result < 256) {
				sT2Builder.append(result);
			} else {
				sError.append("充电模块位置编号的值不在范围内");
				sT2Builder.append("充电模块位置编号的值不在范围内");
			}
		} else if (str.contains("0xf0")) {
			if (result == 240) {
				sT2Builder.append("结束标识");
			} else {
				sError.append("0xf0的值不在范围内");
				sT2Builder.append("0xf0的值不在范围内");
			}
		} else if (str.contains("分组标识")) {
			if (result == 85) {
				sT2Builder.append("电池故障代码标识:0x55");
			} else if (result == 102) {
				sT2Builder.append("电池系统故障代码标识:0x66");
			} else if (result == 119) {
				sT2Builder.append("BMS 故障代码标识:0x77");
			} else {
				sError.append("分组标识的值不在范围内");
				sT2Builder.append("分组标识的值不在范围内");
			}
		} else if (str.contains("0xff")) {
			if (result == 255) {
				sT2Builder.append("0xff");
			} else {
				sError.append(result);
				sT2Builder.append(result);
			}
		} else if (str.contains("充电枪位置") && str.contains("充电类型")) {
			if (result == 1) {
				sT2Builder.append("直流");
			} else if (result == 2) {
				sT2Builder.append("交流");
			} else {
				sError.append("充电类型不在范围内");
				sT2Builder.append("充电类型不在范围内");
			}
		} else if (str.equalsIgnoreCase("烟雾报警总状态") || str.equalsIgnoreCase("震动传感器报警总状态")) {
			if (result == 1) {
				sT2Builder.append("异常");
			} else if (result == 2) {
				sT2Builder.append("正常");
			} else if (result == 3) {
				sT2Builder.append("故障");
			} else if (result == 255) {
				sT2Builder.append("无状态值");
			} else {
				sError.append("状态的值不在范围内");
				sT2Builder.append("状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("风扇总状态") || str.equalsIgnoreCase("空调总状态") || str.equalsIgnoreCase("加热器总状态")) {
			if (result == 1) {
				sT2Builder.append("停止");
			} else if (result == 2) {
				sT2Builder.append("运行");
			} else if (result == 3) {
				sT2Builder.append("故障");
			} else if (result == 255) {
				sT2Builder.append("无状态值");
			} else {
				sError.append("状态的值不在范围内");
				sT2Builder.append("状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("充电模块位置 编号")) {
			if (result == 0) {
				sT2Builder.append("所有");
			} else if (result <= 255 && result >= 1) {
				sT2Builder.append(result);
			} else {
				sError.append("充电模块位置 编号不在范围内");
				sT2Builder.append("充电模块位置 编号不在范围内");
			}
		} else if (str.equalsIgnoreCase("查询单体数据类型")) {
			if (result == 1) {
				sT2Builder.append("查询电压");
			} else if (result == 2) {
				sT2Builder.append("查询温度");
			} else {
				sError.append("查询单体数据类型不在范围内");
				sT2Builder.append("查询单体数据类型不在范围内");
			}
		} else if (str.contains("充电枪状态")) {
			if (result == 1) {
				sT2Builder.append("在");
			} else if (result == 0) {
				sT2Builder.append("空");
			} else {
				sError.append("充电枪状态不在范围内");
				sT2Builder.append("充电枪状态不在范围内");
			}
		} else if (str.contains("充电枪工作状态")) {
			if (result == 1) {
				sT2Builder.append("充电");
			} else if (result == 2) {
				sT2Builder.append("静置");
			} else if (result == 3) {
				sT2Builder.append("休眠");
			} else if (result == 4) {
				sT2Builder.append("故障");
			} else if (result == 255) {
				sT2Builder.append("无");
			} else {
				sError.append("充电枪工作状态不在范围内");
				sT2Builder.append("充电枪工作状态不在范围内");
			}
		} else if (str.contains("配电开关") || str.contains("配电保险")) {
			if (result == 2) {
				sT2Builder.append("开");
			} else if (result == 1) {
				sT2Builder.append("闭");
			} else {
				sError.append("值不在范围内");
				sT2Builder.append("值不在范围内");
			}
		}
		// else if(str.equalsIgnoreCase("是否正常结束")){
		else if (str.contains("是否正常结束")) {
			if (result == 1) {
				sT2Builder.append("正常结束");
			} else if (result == 2) {
				sT2Builder.append("异常结束");
			} else if (result == 3) {
				sT2Builder.append("紧急停机按钮停机故障，充电机停止");
			} else if (result == 4) {
				sT2Builder.append("CC1 故障");
			} else if (result == 5) {
				sT2Builder.append("BMS 故障，充电机停止");
			} else if (result == 6) {
				sT2Builder.append("BMS 错误，充电机停止");
			} else if (result == 7) {
				sT2Builder.append("单体电池电压过高，充电机停止");
			} else if (result == 8) {
				sT2Builder.append("单体电池温度过高，充电机停止");
			} else if (result == 9) {
				sT2Builder.append("输入过压，充电机停止");
			} else if (result == 10) {
				sT2Builder.append("输入欠压，充电机停止");
			} else if (result == 11) {
				sT2Builder.append("输入缺相，充电机停止");
			} else if (result == 12) {
				sT2Builder.append("输出短路，充电机停止");
			} else if (result == 13) {
				sT2Builder.append("内部故障（充电机自检故障），充电机停止");
			} else if (result == 14) {
				sT2Builder.append("输出过压，充电机停止");
			} else if (result == 15) {
				sT2Builder.append("直流输出断路，充电机停止");
			} else if (result == 16) {
				sT2Builder.append("系统故障停机");
			} else if (result == 17) {
				sT2Builder.append("充电系统故障停机");
			} else if (result == 18) {
				sT2Builder.append("充电模块故障停机");
			} else if (result == 19) {
				sT2Builder.append("电池故障停机 ");
			} else if (result == 80) {
				sT2Builder.append("远程停机");
			} else if (result == 81) {
				sT2Builder.append("刷卡停机");
			} else if (result == 82) {
				sT2Builder.append("用户通过订单号验证，控制充电机停止");
			} else {
				sError.append("值不在范围内");
				sT2Builder.append("值不在范围内");
			}
		} else if (str.equalsIgnoreCase("通信模式")) {
			if (result == 1) {
				sT2Builder.append("应答模式");
			} else if (result == 2) {
				sT2Builder.append("上报模式");
			} else {
				sError.append("通信模式的值不在范围内");
				sT2Builder.append("通信模式的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("数据管理模式")) {
			if (result == 1) {
				sT2Builder.append("查询");
			} else if (result == 2) {
				sT2Builder.append("自动擦除");
			} else if (result == 153) {
				sT2Builder.append("清空");
			} else {
				sError.append("数据管理模式的值不在范围内");
				sT2Builder.append("数据管理模式的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("数据管理操作")) {
			if (result == 1) {
				sT2Builder.append("查询");
			} else if (result == 2) {
				sT2Builder.append("设置");
			} else {
				sError.append("数据管理操作的值不在范围内");
				sT2Builder.append("数据管理操作的值不在范围内");
			}
		}
		// 李红修改:2015.7.14;
		// else if(str.equalsIgnoreCase("充电满策略")){
		else if (str.contains("充电满策略")) {
			if (result == 1) {
				sT2Builder.append("自定义时间充电");
			} else if (result == 2) {
				sT2Builder.append("自定义电度数充电");
			} else if (result == 3) {
				sT2Builder.append("自定义金额");
			} else if (result == 4) {
				sT2Builder.append("自然充电");
			} else {
				sError.append("充电满策略不在范围内");
				sT2Builder.append("充电满策略不在范围内");
			}
		} else if (str.equalsIgnoreCase("预约操作")) {
			if (result == 1) {
				sT2Builder.append("预约确认");
			} else if (result == 2) {
				sT2Builder.append("预约取消");
			} else if (result == 3) {
				sT2Builder.append("预约终止");
			} else {
				sError.append("预约操作的值不在范围内");
				sT2Builder.append("预约操作的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("数据类型")) {
			if (result == 0) {
				sT2Builder.append("所有历史数据");
			} else if (result == 1) {
				sT2Builder.append("历史充电记录");
			} else if (result == 2) {
				sT2Builder.append("历史事件");
			} else if (result == 3) {
				sT2Builder.append("历史告警");
			} else if (result == 4) {
				sT2Builder.append("历史充电过程记录");
			} else {
				sError.append("数据类型的值不在范围内");
				sT2Builder.append("数据类型的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("交易类型")) {
			if (result == 1) {
				sT2Builder.append("换电服务（2块）");
			} else if (result == 2) {
				sT2Builder.append("换电服务（4）块");
			} else if (result == 16) {
				sT2Builder.append("充电服务");
			} else if (result == 32) {
				sT2Builder.append("自然充满");
			} else if (result == 48) {
				sT2Builder.append("按时间");
			} else if (result == 64) {
				sT2Builder.append("按电量");
			} else {
				sError.append("交易类型的值不在范围内");
				sT2Builder.append("交易类型的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("设置结果")) {
			if (result == 1) {
				sT2Builder.append("成功");
			} else if (result == 0) {
				sT2Builder.append("失败");
			} else {
				sError.append("交易类型的值不在范围内");
				sT2Builder.append("交易类型的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("处理状态")) {
			if (result == 1) {
				sT2Builder.append("成功");
			} else if (result == 2) {
				sT2Builder.append("失败");
			} else {
				sError.append("交易类型的值不在范围内");
				sT2Builder.append("交易类型的值不在范围内");
			}
		} else if (str.contains("SOH")) {
			if (result == 255) {
				sT2Builder.append("无");
			} else {
				sT2Builder.append(result);
			}
		} else if (str.contains("电池箱序号")) {
			if (result == 255) {
				sT2Builder.append("无");
			} else {
				sT2Builder.append(result);
			}
		} else if (str.contains("充电枪位置充电机状态")) {
			if (result == 1) {
				sT2Builder.append("充电");
			} else if (result == 0) {
				sT2Builder.append("停机");
			} else if (result == 9) {
				sT2Builder.append("预约锁定");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("查询类型")) {
			if (result == 1) {
				sT2Builder.append("系统故障状态");
			} else if (result == 2) {
				sT2Builder.append("充电系统故障状态");
			} else if (result == 3) {
				sT2Builder.append("充电模块故障");
			} else if (result == 4) {
				sT2Builder.append("电池故障");
			} else if (result == 18) {
				sT2Builder.append("充电过程记录代码");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("检测模式")) {
			if (result == 1) {
				sT2Builder.append("倒车雷达");
			} else if (result == 2) {
				sT2Builder.append("未定义");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("检测状态")) {
			if (result == 0) {
				sT2Builder.append("空闲");
			} else if (result == 1) {
				sT2Builder.append("占用");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("检测参数类型")) {
			if (result == 1) {
				sT2Builder.append("距离");
			} else if (result == 2) {
				sT2Builder.append("未定义");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("预约模式")) {
			if (result == 1) {
				sT2Builder.append("协商");
			} else if (result == 2) {
				sT2Builder.append("强制");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else {
			if (str.contains("模块数量") || str.contains("充电枪数量") || str.contains("参数个数") || str.contains("模块个数")
					|| str.contains("电池个数") || str.contains("记录条数") || str.contains("记录数") || str.contains("SOH")) {
				if (result == 255) {
					sT2Builder.append("无");
				} else {
					sT2Builder.append(result);
				}
			} else {
				sT2Builder.append(result);
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;
	}

	// 一个字节转成10进制整型的str，加个错误判断。

	public static String[] unsignByteToInt(byte data, String str) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		int result = 0;
		int hightestBit = ((data >>> 7) & 0x01);
		result = (data & 0x7F);
		if (1 == hightestBit) {
			result = result | 0x80;
		}
		// 对该字节的值做判断:
		if (str.equalsIgnoreCase("工作状态")) {
			if (result == 1) {
				sT2Builder.append("管理状态");
			} else if (result == 2) {
				sT2Builder.append("服务状态");
			} else if (result == 3) {
				sT2Builder.append("故障状态");
			} else if (result == 9) {
				sT2Builder.append("初始化状态");
			} else {
				sError.append("工作状态的值不在范围内");
				sT2Builder.append("工作状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("输入电源状态")) {
			if (result == 1) {
				sT2Builder.append("正常");
			} else if (result == 2) {
				sT2Builder.append("异常");
			} else {
				sError.append("输入电源状态值不在范围内");
				sT2Builder.append("输入电源状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("执行控制")) {
			if (result == 1) {
				sT2Builder.append("立即执行");
			} else if (result == 2) {
				sT2Builder.append("空闲执行");
			} else if (result == 3) {
				sT2Builder.append("加电执行");
			} else {
				sError.append("执行控制的值不在范围内");
				sT2Builder.append("执行控制的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("确认升级")) {
			if (result == 0) {
				sT2Builder.append("开始升级");
			} else if (result == 1) {
				sT2Builder.append("异常，不执行升级");
			} else {
				sError.append("确认升级的值不在范围内");
				sT2Builder.append("确认升级的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备1")) {
			if (result == 1) {
				sT2Builder.append("环境板未配置");
			} else if (result == 2) {
				sT2Builder.append("环境板未检测到");
			} else if (result == 3) {
				sT2Builder.append("环境板工作正常");
			} else if (result == 4) {
				sT2Builder.append("环境板正常待机");
			} else if (result == 5) {
				sT2Builder.append("环境板故障");
			} else {
				sError.append("环境板状态的值不在范围内");
				sT2Builder.append("环境板状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备2")) {
			if (result == 1) {
				sT2Builder.append("POS机未配置");
			} else if (result == 2) {
				sT2Builder.append("POS机未检测到");
			} else if (result == 3) {
				sT2Builder.append("POS机工作正常");
			} else if (result == 4) {
				sT2Builder.append("POS机正常待机");
			} else if (result == 5) {
				sT2Builder.append("POS机故障");
			} else {
				sError.append("POS机状态的值不在范围内");
				sT2Builder.append("POS机状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备3")) {
			if (result == 1) {
				sT2Builder.append("电表未配置");
			} else if (result == 2) {
				sT2Builder.append("电表未检测到");
			} else if (result == 3) {
				sT2Builder.append("电表工作正常");
			} else if (result == 4) {
				sT2Builder.append("电表正常待机");
			} else if (result == 5) {
				sT2Builder.append("电表故障");
			} else {
				sError.append("电表的值不在范围内");
				sT2Builder.append("电表的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备4")) {
			if (result == 1) {
				sT2Builder.append("UPS未配置");
			} else if (result == 2) {
				sT2Builder.append("UPS未检测到");
			} else if (result == 3) {
				sT2Builder.append("UPS工作正常");
			} else if (result == 4) {
				sT2Builder.append("UPS正常待机");
			} else if (result == 5) {
				sT2Builder.append("UPS故障");
			} else {
				sError.append("UPS状态的值不在范围内");
				sT2Builder.append("UPS状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备5")) {
			if (result == 1) {
				sT2Builder.append("充电系统未配置");
			} else if (result == 2) {
				sT2Builder.append("充电系统未检测到");
			} else if (result == 3) {
				sT2Builder.append("充电系统工作正常");
			} else if (result == 4) {
				sT2Builder.append("充电系统正常待机");
			} else if (result == 5) {
				sT2Builder.append("充电系统故障");
			} else {
				sError.append("充电系统状态的值不在范围内");
				sT2Builder.append("充电系统状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备6")) {
			if (result == 1) {
				sT2Builder.append("显示屏幕未配置");
			} else if (result == 2) {
				sT2Builder.append("显示屏幕未检测到");
			} else if (result == 3) {
				sT2Builder.append("显示屏幕工作正常");
			} else if (result == 4) {
				sT2Builder.append("显示屏幕正常待机");
			} else if (result == 5) {
				sT2Builder.append("显示屏幕故障");
			} else {
				sError.append("显示屏幕状态的值不在范围内");
				sT2Builder.append("显示屏幕状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("下游设备7") || str.equalsIgnoreCase("下游设备8")) {
			if (result == 1) {
				sT2Builder.append("未配置");
			} else if (result == 2) {
				sT2Builder.append("未检测到");
			} else if (result == 3) {
				sT2Builder.append("工作正常");
			} else if (result == 4) {
				sT2Builder.append("正常待机");
			} else if (result == 5) {
				sT2Builder.append("故障");
			} else {
				sError.append("状态的值不在范围内");
				sT2Builder.append("状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("参数类型")) {
			if (result == 2) {
				sT2Builder.append("设置充电机启停");
			} else if (result == 3) {
				sT2Builder.append("设置输出功率");
			} else if (result == 4) {
				sT2Builder.append("设置单体最高允许电压");
			} else if (result == 5) {
				sT2Builder.append("设置单体最高允许温度");
			} else {
				sError.append("参数类型的值不在范围内");
				sT2Builder.append("参数类型的值不在范围内");
			}
		} else if (str.contains("充电模块位置编号")) {
			if (result == 0) {
				sT2Builder.append("所有");
			} else if (result > 0 && result < 256) {
				sT2Builder.append(result);
			} else {
				sError.append("充电模块位置编号的值不在范围内");
				sT2Builder.append("充电模块位置编号的值不在范围内");
			}
		} else if (str.contains("0xf0")) {
			if (result == 240) {
				sT2Builder.append("结束标识");
			} else {
				sError.append("0xf0的值不在范围内");
				sT2Builder.append("0xf0的值不在范围内");
			}
		} else if (str.contains("分组标识")) {
			if (result == 85) {
				sT2Builder.append("电池故障代码标识:0x55");
			} else if (result == 102) {
				sT2Builder.append("电池系统故障代码标识:0x66");
			} else if (result == 119) {
				sT2Builder.append("BMS 故障代码标识:0x77");
			} else {
				sError.append("分组标识的值不在范围内");
				sT2Builder.append("分组标识的值不在范围内");
			}
		} else if (str.contains("0xff")) {
			if (result == 255) {
				sT2Builder.append("0xff");
			} else {
				sError.append(result);
				sT2Builder.append(result);
			}
		} else if (str.contains("充电枪位置") && str.contains("充电类型")) {
			if (result == 1) {
				sT2Builder.append("直流");
			} else if (result == 2) {
				sT2Builder.append("交流");
			} else {
				sError.append("充电类型不在范围内");
				sT2Builder.append("充电类型不在范围内");
			}
		} else if (str.equalsIgnoreCase("烟雾报警总状态") || str.equalsIgnoreCase("震动传感器报警总状态")) {
			if (result == 1) {
				sT2Builder.append("异常");
			} else if (result == 2) {
				sT2Builder.append("正常");
			} else if (result == 3) {
				sT2Builder.append("故障");
			} else if (result == 255) {
				sT2Builder.append("无状态值");
			} else {
				sError.append("状态的值不在范围内");
				sT2Builder.append("状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("风扇总状态") || str.equalsIgnoreCase("空调总状态") || str.equalsIgnoreCase("加热器总状态")) {
			if (result == 1) {
				sT2Builder.append("停止");
			} else if (result == 2) {
				sT2Builder.append("运行");
			} else if (result == 3) {
				sT2Builder.append("故障");
			} else if (result == 255) {
				sT2Builder.append("无状态值");
			} else {
				sError.append("状态的值不在范围内");
				sT2Builder.append("状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("充电模块位置 编号")) {
			if (result == 0) {
				sT2Builder.append("所有");
			} else if (result <= 255 && result >= 1) {
				sT2Builder.append(result);
			} else {
				sError.append("充电模块位置 编号不在范围内");
				sT2Builder.append("充电模块位置 编号不在范围内");
			}
		} else if (str.equalsIgnoreCase("查询单体数据类型")) {
			if (result == 1) {
				sT2Builder.append("查询电压");
			} else if (result == 2) {
				sT2Builder.append("查询温度");
			} else {
				sError.append("查询单体数据类型不在范围内");
				sT2Builder.append("查询单体数据类型不在范围内");
			}
		} else if (str.contains("充电枪状态")) {
			if (result == 1) {
				sT2Builder.append("在");
			} else if (result == 0) {
				sT2Builder.append("空");
			} else {
				sError.append("充电枪状态不在范围内");
				sT2Builder.append("充电枪状态不在范围内");
			}
		} else if (str.contains("充电枪工作状态")) {
			if (result == 1) {
				sT2Builder.append("充电");
			} else if (result == 2) {
				sT2Builder.append("静置");
			} else if (result == 3) {
				sT2Builder.append("休眠");
			} else if (result == 4) {
				sT2Builder.append("故障");
			} else if (result == 255) {
				sT2Builder.append("无");
			} else {
				sError.append("充电枪工作状态不在范围内");
				sT2Builder.append("充电枪工作状态不在范围内");
			}
		} else if (str.contains("配电开关") || str.contains("配电保险")) {
			if (result == 2) {
				sT2Builder.append("开");
			} else if (result == 1) {
				sT2Builder.append("闭");
			} else {
				sError.append("值不在范围内");
				sT2Builder.append("值不在范围内");
			}
		}
		// else if(str.equalsIgnoreCase("是否正常结束")){
		else if (str.contains("是否正常结束")) {
			if (result == 1) {
				sT2Builder.append("正常结束");
			} else if (result == 2) {
				sT2Builder.append("异常结束");
			} else if (result == 3) {
				sT2Builder.append("紧急停机按钮停机故障，充电机停止");
			} else if (result == 4) {
				sT2Builder.append("CC1 故障");
			} else if (result == 5) {
				sT2Builder.append("BMS 故障，充电机停止");
			} else if (result == 6) {
				sT2Builder.append("BMS 错误，充电机停止");
			} else if (result == 7) {
				sT2Builder.append("单体电池电压过高，充电机停止");
			} else if (result == 8) {
				sT2Builder.append("单体电池温度过高，充电机停止");
			} else if (result == 9) {
				sT2Builder.append("输入过压，充电机停止");
			} else if (result == 10) {
				sT2Builder.append("输入欠压，充电机停止");
			} else if (result == 11) {
				sT2Builder.append("输入缺相，充电机停止");
			} else if (result == 12) {
				sT2Builder.append("输出短路，充电机停止");
			} else if (result == 13) {
				sT2Builder.append("内部故障（充电机自检故障），充电机停止");
			} else if (result == 14) {
				sT2Builder.append("输出过压，充电机停止");
			} else if (result == 15) {
				sT2Builder.append("直流输出断路，充电机停止");
			} else if (result == 16) {
				sT2Builder.append("系统故障停机");
			} else if (result == 17) {
				sT2Builder.append("充电系统故障停机");
			} else if (result == 18) {
				sT2Builder.append("充电模块故障停机");
			} else if (result == 19) {
				sT2Builder.append("电池故障停机 ");
			} else if (result == 80) {
				sT2Builder.append("远程停机");
			} else if (result == 81) {
				sT2Builder.append("刷卡停机");
			} else if (result == 82) {
				sT2Builder.append("用户通过订单号验证，控制充电机停止");
			} else {
				sError.append("值不在范围内");
				sT2Builder.append("值不在范围内");
			}
		} else if (str.equalsIgnoreCase("通信模式")) {
			if (result == 1) {
				sT2Builder.append("应答模式");
			} else if (result == 2) {
				sT2Builder.append("上报模式");
			} else {
				sError.append("通信模式的值不在范围内");
				sT2Builder.append("通信模式的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("数据管理模式")) {
			if (result == 1) {
				sT2Builder.append("查询");
			} else if (result == 2) {
				sT2Builder.append("自动擦除");
			} else if (result == 153) {
				sT2Builder.append("清空");
			} else {
				sError.append("数据管理模式的值不在范围内");
				sT2Builder.append("数据管理模式的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("数据管理操作")) {
			if (result == 1) {
				sT2Builder.append("查询");
			} else if (result == 2) {
				sT2Builder.append("设置");
			} else {
				sError.append("数据管理操作的值不在范围内");
				sT2Builder.append("数据管理操作的值不在范围内");
			}
		}
		// 李红修改:2015.7.14;
		// else if(str.equalsIgnoreCase("充电满策略")){
		else if (str.contains("充电满策略")) {
			if (result == 1) {
				sT2Builder.append("自定义时间充电");
			} else if (result == 2) {
				sT2Builder.append("自定义电度数充电");
			} else if (result == 3) {
				sT2Builder.append("自定义金额");
			} else if (result == 4) {
				sT2Builder.append("自然充电");
			} else {
				sError.append("充电满策略不在范围内");
				sT2Builder.append("充电满策略不在范围内");
			}
		} else if (str.equalsIgnoreCase("预约操作")) {
			if (result == 1) {
				sT2Builder.append("预约确认");
			} else if (result == 2) {
				sT2Builder.append("预约取消");
			} else if (result == 3) {
				sT2Builder.append("预约终止");
			} else {
				sError.append("预约操作的值不在范围内");
				sT2Builder.append("预约操作的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("数据类型")) {
			if (result == 0) {
				sT2Builder.append("所有历史数据");
			} else if (result == 1) {
				sT2Builder.append("历史充电记录");
			} else if (result == 2) {
				sT2Builder.append("历史事件");
			} else if (result == 3) {
				sT2Builder.append("历史告警");
			} else if (result == 4) {
				sT2Builder.append("历史充电过程记录");
			} else {
				sError.append("数据类型的值不在范围内");
				sT2Builder.append("数据类型的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("交易类型")) {
			if (result == 1) {
				sT2Builder.append("换电服务（2块）");
			} else if (result == 2) {
				sT2Builder.append("换电服务（4）块");
			} else if (result == 16) {
				sT2Builder.append("充电服务");
			} else if (result == 32) {
				sT2Builder.append("自然充满");
			} else if (result == 48) {
				sT2Builder.append("按时间");
			} else if (result == 64) {
				sT2Builder.append("按电量");
			} else {
				sError.append("交易类型的值不在范围内");
				sT2Builder.append("交易类型的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("设置结果")) {
			if (result == 1) {
				sT2Builder.append("成功");
			} else if (result == 0) {
				sT2Builder.append("失败");
			} else {
				sError.append("交易类型的值不在范围内");
				sT2Builder.append("交易类型的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("处理状态")) {
			if (result == 1) {
				sT2Builder.append("成功");
			} else if (result == 2) {
				sT2Builder.append("失败");
			} else {
				sError.append("交易类型的值不在范围内");
				sT2Builder.append("交易类型的值不在范围内");
			}
		} else if (str.contains("SOH")) {
			if (result == 255) {
				sT2Builder.append("无");
			} else {
				sT2Builder.append(result);
			}
		} else if (str.contains("电池箱序号")) {
			if (result == 255) {
				sT2Builder.append("无");
			} else {
				sT2Builder.append(result);
			}
		} else if (str.contains("充电枪位置充电机状态")) {
			if (result == 1) {
				sT2Builder.append("充电");
			} else if (result == 0) {
				sT2Builder.append("停机");
			} else if (result == 9) {
				sT2Builder.append("预约锁定");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("查询类型")) {
			if (result == 1) {
				sT2Builder.append("系统故障状态");
			} else if (result == 2) {
				sT2Builder.append("充电系统故障状态");
			} else if (result == 3) {
				sT2Builder.append("充电模块故障");
			} else if (result == 4) {
				sT2Builder.append("电池故障");
			} else if (result == 18) {
				sT2Builder.append("充电过程记录代码");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("检测模式")) {
			if (result == 1) {
				sT2Builder.append("倒车雷达");
			} else if (result == 2) {
				sT2Builder.append("未定义");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("检测状态")) {
			if (result == 0) {
				sT2Builder.append("空闲");
			} else if (result == 1) {
				sT2Builder.append("占用");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("检测参数类型")) {
			if (result == 1) {
				sT2Builder.append("距离");
			} else if (result == 2) {
				sT2Builder.append("未定义");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else if (str.contains("预约模式")) {
			if (result == 1) {
				sT2Builder.append("协商");
			} else if (result == 2) {
				sT2Builder.append("强制");
			} else {
				sError.append("参数值不在范围内");
				sT2Builder.append("参数值不在范围内");
			}
		} else {
			if (str.contains("模块数量") || str.contains("充电枪数量") || str.contains("参数个数") || str.contains("模块个数")
					|| str.contains("电池个数") || str.contains("记录条数") || str.contains("记录数") || str.contains("SOH")) {
				if (result == 255) {
					sT2Builder.append("无");
				} else {
					sT2Builder.append(result);
				}
			} else {
				sT2Builder.append(result);
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;

	}

	// byte转成ＧＢ2312汉字:
	// 2015-11-26 15:44:07 采用0x00补充的，转成字符串要扔掉，否则出错
	public static String byteToGB(byte[] bsrc) throws UnsupportedEncodingException {
		byte[] bs = new byte[bsrc.length];
		for (int i = 0; i < bsrc.length; i++) {
			if (bsrc[i] == 0x00)
				bs[i] = ' ';
			bs[i] = bsrc[i];
		}
		String sdes = new String(bs, "GB2312");
		sdes = sdes.trim();
		return sdes;
	}

	public static String byteToGB(byte[] bsrc, String ChineseEncoding) throws UnsupportedEncodingException {
		byte[] bs = new byte[bsrc.length];
		for (int i = 0; i < bsrc.length; i++) {
			if (bsrc[i] == 0x00)
				bs[i] = ' ';
			bs[i] = bsrc[i];
		}
		String sdes = new String(bs, ChineseEncoding);
		sdes = sdes.trim();
		return sdes;
	}

	// 判断字符串是否有中文:
	public static boolean checkuserid(String userid) {
		boolean results;
		String test;
		test = "[\\u4E00-\\u9FA5]+";
	
		Pattern p = Pattern.compile(test);
		Matcher m = p.matcher(userid);
		// StringBuffer sb = new StringBuffer();
		results = m.find();
		return results;
	}

	//////////////////////////////////////////////////////////////
	/////////// 无线车载相关的解码函数:
	public static String[] SecunsignByteToInt(byte data, String str) {
		String[] strResult = new String[2];
		StringBuilder sT2Builder = new StringBuilder();
		StringBuilder sError = new StringBuilder();
		int result = 0;
		int hightestBit = ((data >>> 7) & 0x01);
		result = (data & 0x7F);
		if (1 == hightestBit) {
			result = result | 0x80;
		}
		// 对该字节的值做判断:
		if (str.equalsIgnoreCase("GPS有效标示")) {
			if (result == 65) {
				sT2Builder.append("有效");
			} else if (result == 86) {
				sT2Builder.append("无效");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("GPS有效标示的值不在范围内");
				sT2Builder.append("GPS有效标示的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("南北纬度标示")) {
			if (result == 78) {
				sT2Builder.append("北纬");
			} else if (result == 83) {
				sT2Builder.append("南纬");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("南北纬度标示值不在范围内");
				sT2Builder.append("南北纬度标示的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("东西经度标示")) {
			if (result == 69) {
				sT2Builder.append("东经");
			} else if (result == 87) {
				sT2Builder.append("西经");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("东西经度标示的值不在范围内");
				sT2Builder.append("东西经度标示的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("当前信号质量")) {
			if (result >= 0 && result <= 31) {
				sT2Builder.append(result);
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("当前信号质量的值不在范围内");
				sT2Builder.append("当前信号质量的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("通信模式")) {
			if (result == 1) {
				sT2Builder.append("应答模式");
			} else if (result == 2) {
				sT2Builder.append("上报模式");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("通信模式的值不在范围内");
				sT2Builder.append("通信模式的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("制动状态")) {
			if (result == 1) {
				sT2Builder.append("制动状态");
			} else if (result == 2) {
				sT2Builder.append("非制动状态");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("制动状态的值不在范围内");
				sT2Builder.append("制动状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("驻车制动状态")) {
			if (result == 1) {
				sT2Builder.append("制动状态");
			} else if (result == 2) {
				sT2Builder.append("非制动状态");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("驻车制动状态的值不在范围内");
				sT2Builder.append("驻车制动状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("安全线信号")) {
			if (result == 255) {
				sT2Builder.append("缺省值0xFF");
			} else {
				sT2Builder.append(result);
			}
		} else if (str.equalsIgnoreCase("电机驱动器温度信号")) {
			if (result == 255) {
				sT2Builder.append("缺省值0xFF");
			} else {
				sT2Builder.append(result);
			}
		} else if (str.equalsIgnoreCase("电池继电器状态")) {
			if (result == 1) {
				sT2Builder.append("开启");
			} else if (result == 0) {
				sT2Builder.append("闭合");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("电池继电器状态的值不在范围内");
				sT2Builder.append("电池继电器状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("车辆动力类型")) {
			if (result == 1) {
				sT2Builder.append("纯电动");
			} else if (result == 2) {
				sT2Builder.append("混合动力");
			} else if (result == 3) {
				sT2Builder.append("燃料");
			} else if (result == 4) {
				sT2Builder.append("汽油");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("车辆动力类型的值不在范围内");
				sT2Builder.append("车辆动力类型的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("ABS工作状态")) {
			if (result == 1) {
				sT2Builder.append("运行");
			} else if (result == 0) {
				sT2Builder.append("未运行");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("ABS工作状态不在范围内");
				sT2Builder.append("ABS工作状态不在范围内");
			}
		} else if (str.equalsIgnoreCase("空调工作状态")) {
			if (result == 1) {
				sT2Builder.append("开");
			} else if (result == 0) {
				sT2Builder.append("停");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("空调工作状态的值不在范围内");
				sT2Builder.append("空调工作状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("动力系统状态")) {
			if (result == 1) {
				sT2Builder.append("危险状态(疏散人群)");
			} else if (result == 2) {
				sT2Builder.append("故障状态(靠边停车,呼维修人员)");
			} else if (result == 3) {
				sT2Builder.append("跛行状态(可继续行驶至维修站)");
			} else if (result == 4) {
				sT2Builder.append("一般故障(暂不处理,完成)");
			} else if (result == 0) {
				sT2Builder.append("无故障");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("动力系统状态的值不在范围内");
				sT2Builder.append("动力系统状态的值不在范围内");
			}
		} else if (str.equalsIgnoreCase("钥匙档位信息")) {
			if (result == 1) {
				sT2Builder.append("OFF档位");
			} else if (result == 2) {
				sT2Builder.append("ACC档位");
			} else if (result == 3) {
				sT2Builder.append("ON档位");
			} else if (result == 4) {
				sT2Builder.append("发动档");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("钥匙档位信息不在范围内");
				sT2Builder.append("钥匙档位信息不在范围内");
			}
		} else if (str.equalsIgnoreCase("0xf0")) {
			if (result == 240) {
				sT2Builder.append("ID结束标识");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("0xf0不在范围内");
				sT2Builder.append("0xf0不在范围内");
			}
		} else if (str.contains("动力回路继电器状态")) {
			if (result == 1) {
				sT2Builder.append("开启");
			} else if (result == 0) {
				sT2Builder.append("闭合");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("动力回路继电器状态不在范围内");
				sT2Builder.append("动力回路继电器状态状态不在范围内");
			}
		} else if (str.contains("参数类型")) {
			if (result == 1) {
				sT2Builder.append("电压");
			} else if (result == 2) {
				sT2Builder.append("温度");
			} else if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sError.append("参数类型的值不在范围内");
				sT2Builder.append("参数类型的值不在范围内");
			}
		} else if (str.contains("结束分隔符0xff")) {
			sT2Builder.append("结束分隔符0xff");
		} else if (str.contains("电池故障代码标识0x55")) {
			sT2Builder.append("电池故障代码标识0x55");
		} else if (str.contains("电池系统故障代码标识0x66")) {
			sT2Builder.append("电池系统故障代码标识0x66");
		} else if (str.contains("BMS故障代码标识0x77")) {
			sT2Builder.append("BMS故障代码标识0x77");
		} else if (str.equalsIgnoreCase("加速器踏板状态")) {
			result = result * 4;
			float f = (float) result / 10;
			sT2Builder.append(f);
			sT2Builder.append("%");
		} else {
			if (result == 255) {
				sT2Builder.append("缺省值FF");
			} else {
				sT2Builder.append(result);
			}
		}
		strResult[0] = sError.toString();
		strResult[1] = sT2Builder.toString();
		return strResult;

	}

	public static void main(String[] args) throws IOException {
		

		byte[] b0 = {0x00,0x20,0x00,0x00};
		byte[] b1 = {0x00,0x00,0x10,0x00};
		byte[] b2 = {0x00,0x00,0x40,0x00};
		byte[] b3 = {0x00,0x00,(byte)0x80,0x00};
		 List<String> sl=systemFaultToStr(b0,3);
		
		 StringBuilder sBuilder = new StringBuilder();
		 for(String ss:sl){

		 sBuilder.append(ss).append("#");

		 };

		
	}

}
