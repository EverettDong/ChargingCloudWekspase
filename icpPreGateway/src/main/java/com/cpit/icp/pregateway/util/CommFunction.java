package com.cpit.icp.pregateway.util;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 协议数据转化工具类
 * 
 * @author LIANGZHIYUAN
 * 
 */
public class CommFunction {

	/**
	 * 16进制字符集
	 */
	private static char[] cHEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	private static StringBuilder result = new StringBuilder();

	public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static long init = 1404955893L;

	/**
	 * int To HexStr
	 * 
	 * @param dec
	 * @return
	 */
	public static String intToHexStr(int dec) {
		return Integer.toHexString(dec).toUpperCase();

	}

	/**
	 * Integer型转成字节数组<br>
	 * 数组0存放的是高字节， 数组3存放的是低字节
	 * 
	 * @param data
	 * @return byte[]
	 */
	public static byte[] intToBytes(int data) {
		byte[] bs = new byte[4];
		for (int i = 0; i < 4; i++) {
			bs[i] = (byte) (data >>> ((3 - i) * 8) & 0xFF);
		}
		return bs;
	}

	/**
	 * 整数转换成一个无符号的字节 整数范围是0~255
	 * 
	 * @param value
	 * @return byte
	 */
	public static byte intToUnsignByte(int value) {
		if ((value >= 0) && (value <= 255)) {
			return intToBytes(value)[3];
		} else {
			throw new IllegalArgumentException("整数值超过0~255的范围");
		}
	}

	public static String byteToHexStr2(byte data) {
		String temp = byteToHexStr(data);
		return temp.length() > 1 ? temp.substring((temp.length() - 2)) : "0" + temp;

	}

	public static String SumCheck2(byte[] CheckData) {
		String s = intToHexStr(CommFunction.sumValidityValue(CheckData));
		return s.length() > 1 ? s.substring((s.length() - 2)) : "0" + s;
	}

	/**
	 * 计算和校验值 <br>
	 * 如果data为 null 或者 长度为空,则返回固定值-1 <br>
	 * 
	 * @param data
	 *            待计算的字节数组
	 * @return int 和校验值
	 */
	public static int sumValidityValue(byte[] data) {
		int value = -1;
		if (data == null || data.length == 0) {
			return value;
		}
		value = 0;
		for (int i = 0; i < data.length; i++) {
			value += unsignByteToInt(data[i]);
		}
		return value;
	}

	public static String byteArrayToHexStr2(byte[] data) {
		//
		result.setLength(0);
		for (int i = 0; i < data.length; i++) {
			byte tmp = (byte) (data[i] & 0xFF);
			char hi = cHEX[((tmp & 0xF0) >>> 4)];// 高4位
			char low = cHEX[(tmp & 0x0F)];// 低4位

			result.append(hi);
			result.append(low);
			if (i != (data.length - 1)) {
				result.append(" ");
			}
		}
		return result.toString();
	}

	/**
	 * Short型转成字节数组<br>
	 * 数组0存放的是高字节， 数组1存放的是低字节
	 * 
	 * @param data
	 * @return byte[]
	 */
	public static byte[] shortToBytes(int data) {
		byte[] bs = new byte[2];
		bs[0] = (byte) (data >>> 8);
		bs[1] = (byte) (data & 0xFF);
		return bs;

	}
	
	/**
	 * Short型转成字节数组<br>
	 * 只返回低字节
	 * @param data
	 * @return byte[]
	 */
	public static byte[] shortToLowBytes(int data) {
		byte[] bs = new byte[1];
		bs[0] = (byte) (data & 0xFF);
		return bs;

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

	/**
	 * Byte转Bit
	 */
	public static String byteToBit(byte b) {
		return "" + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1) + (byte) ((b >> 5) & 0x1)
				+ (byte) ((b >> 4) & 0x1) + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1) + (byte) ((b >> 1) & 0x1)
				+ (byte) ((b >> 0) & 0x1);
	}

	/**
	 * 字节转成Hex字符串
	 * 
	 * @param data
	 * @return String
	 */
	public static String byteToHexStr(byte data) {
		result.setLength(0);
		byte tmp = (byte) (data & 0xFF);
		char hi = cHEX[((tmp & 0xF0) >>> 4)];// 高4位
		char low = cHEX[(tmp & 0x0F)];// 低4位
		result.append(hi);
		result.append(low);
		return result.toString();
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
	 * 二进制byte转化为BCD编码的byte
	 * 
	 * @param data
	 * @return
	 */
	public static byte byteConvertToBCD(byte data) {
		int intdata = CommFunction.unsignByteToInt(data);
		if (intdata > 99) {
			return CommFunction.intToUnsignByte(255);
		}
		byte hight = CommFunction.intToUnsignByte(intdata / 10);
		byte low = CommFunction.intToUnsignByte(intdata % 10);
		return (byte) ((hight << 4) | (low));
	}

	/**
	 * 字节转化成二进制0，1形式的字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String byteConvertToStr(byte data) {
		result.setLength(0);
		int tmpData = unsignByteToInt(data);
		int tmp = 0;
		for (int i = 0; i < 8; i++) {
			tmp = (tmpData >>> (7 - i)) & 0x1;
			result.append(tmp);
		}
		return result.toString();
	}

	/**
	 * 将长度小于等于4的 byte数组转成整数 数组0 表示高位 有符号位转化
	 * 
	 * @param data
	 * @return int
	 */
	public static int bytesToInt(byte[] data) {
		if (data == null || data.length > 4 || data.length == 0) {
			return 0;
		}
		int value = 0;
		int sign = data[0] >>> 7;
		int tmp = data[0] & 0x7F;
		if (data.length == 4) {
			value = (tmp << 24) + (unsignByteToInt(data[1]) << 16) + (unsignByteToInt(data[2]) << 8)
					+ (unsignByteToInt(data[3]));
			value = (sign << 31) | value;
		} else if (data.length == 3) {
			value = (tmp << 16) + (unsignByteToInt(data[1]) << 8) + (unsignByteToInt(data[2]));
			value = (sign << 23) | value;
		} else if (data.length == 2) {
			value = (tmp << 8) + (unsignByteToInt(data[1]));
			value = (sign << 15) | value;
		} else {
			value = unsignByteToInt(data[0]);
		}
		return value;
	}

	/**
	 * 把长度为2的byte数组转成无符号整数
	 * 
	 * @param data
	 * @return int
	 */
	public static int bytesToUnsignInt(byte[] data) {
		if (data == null || data.length != 2) {
			return 0;
		}
		int value = 0;
		value = value | (unsignByteToInt(data[0]) << 8);
		value = value | unsignByteToInt(data[1]);
		return value;
	}

	public static byte[] hexString2ToBytes(String str) {
		String[] strs = str.split(" ");
		byte[] bytes = new byte[strs.length];
		for (int i = 0; i < strs.length; i++) {
			bytes[i] = hexStringToByte(strs[i]);
		}
		return bytes;
	}

	/**
	 * 字节数组转成Hex字符串
	 * 
	 * @param data,split
	 * @return String
	 */
	public static String bytes2HexStr(byte[] data, String split) {
		StringBuilder result1 = new StringBuilder();
		result1.setLength(0);
		for (int i = 0; i < data.length; i++) {
			byte tmp = (byte) (data[i] & 0xFF);
			char hi = cHEX[((tmp & 0xF0) >>> 4)];// 高4位
			char low = cHEX[(tmp & 0x0F)];// 低4位
			result1.append(hi);
			result1.append(low);
			if (i != (data.length - 1)) {
				result1.append(split);
			}
		}
		return result1.toString();
	}

	/**
	 * 字节数组转成Hex字符串
	 * 
	 * @param data
	 * @return String
	 */
	public static String bytesToHexStr(byte[] data) {
		StringBuilder result2 = new StringBuilder();
		result2.setLength(0);
		for (int i = 0; i < data.length; i++) {
			byte tmp = (byte) (data[i] & 0xFF);
			char hi = cHEX[((tmp & 0xF0) >>> 4)];// 高4位
			char low = cHEX[(tmp & 0x0F)];// 低4位
			result2.append(hi);
			result2.append(low);
		}
		return result2.toString();
	}

	/**
	 * Convert byte[] to hex string.这里我们可以将byte转换成int，然后利用Integer
	 * .toHexString(byte)来转换成16进制字符串。
	 *
	 * @param src
	 *            byte[] data
	 * @return hex string
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (byte element : src) {
			int v = element & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}

	public static char[] bytesToAscii(byte[] src) {
		char[] cdes = new char[src.length];
		// lihong modify 2015.7.27
		int templen = src.length;
		int icount = 0;
		int jcount = 0;
		for (int k = 0; k < templen; k++) {
			Byte sbyte = src[k];
			if (sbyte.equals((byte) 0xFF)) {
				icount++;
				cdes[k] = 'F';
			}
		}
		if (icount >= templen) {
			return cdes;
		}
		for (int j = 0; j < templen; j++) {
			Byte sbyte = src[j];
			if (sbyte.equals((byte) 0x00)) {
				jcount++;
				cdes[j] = ' ';
				src[j] = 0x20;
			}
		}
		if (jcount >= templen) {
			cdes[0] = '0';
			return cdes;
		}
		///////////////////////////////
		for (int i = 0; i < src.length; i++) {
			cdes[i] = byteToAsciiChar(src[i]);
		}
		return cdes;
	}

	public static byte[] converLongToBytes(long l) {
		byte[] b = new byte[8];
		b = java.lang.Long.toString(l).getBytes();
		return b;
	}

	public static long converBytesToLong(byte[] b) {
		long l = 0L;
		l = java.lang.Long.parseLong(new String(b));
		return l;
	}

	/**
	 * ascii字符转成字节
	 * 
	 * @param ch
	 *            字符
	 * @return byte 字节
	 */
	public static byte asciiCharToByte(char ch) {
		return (byte) ch;
	}

	/**
	 * ascii字符组成的字符串转换成字节数组
	 * 
	 * @param asciiStr
	 * @return byte[] 8个字节的字节数组
	 */
	public static byte[] asciiStringToBytes(String asciiStr) {
		return CommFunction.asciiStringToBytes(asciiStr, 8);
	}

	/**
	 * ascii字符组成的字符串转换成字节数组
	 * 
	 * @param asciiStr
	 * @param num
	 *            返回的字节数
	 * @return byte[]
	 */
	public static byte[] asciiStringToBytes(String asciiStr, int num) {
		if (num <= 0 || asciiStr == null || asciiStr.length() <= 0) {
			return null;
		}
		String tmp = asciiStr;
		if (tmp.length() > num) {
			tmp = tmp.substring(0, num);
		} else if (tmp.length() < num) {
			int s = num - tmp.length();
			String sub = "";
			for (int i = 0; i < s; i++) {
				sub += " ";
			}
			tmp = asciiStr + sub;
		}
		int length = tmp.length();
		byte[] result = new byte[length];
		for (int i = 0; i < num; i++) {
			result[i] = CommFunction.asciiCharToByte(tmp.charAt(i));
		}
		return result;
	}

	/**
	 * 10进制串转为BCD码
	 *
	 * @param 10进制字符串
	 * @return byte[]形式的BCD码
	 */
	public static byte[] mobileNum2BCD(String asc) {
		asc = "0" + asc;
		int mod = asc.length() % 2;
		int len = mod != 0 ? ("0" + asc).length() : asc.length();
		byte abt[] = new byte[len];
		if (len >= 2)
			len /= 2;
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		for (int p = 0; p < asc.length() / 2; p++) {
			if (abt[2 * p] >= '0' && abt[2 * p] <= '9')
				j = abt[2 * p] - '0';
			else if (abt[2 * p] >= 'a' && abt[2 * p] <= 'z')
				j = abt[2 * p] - 'a' + 0x0a;
			else
				j = abt[2 * p] - 'A' + 0x0a;
			if (abt[2 * p + 1] >= '0' && abt[2 * p + 1] <= '9')
				k = abt[2 * p + 1] - '0';
			else if (abt[2 * p + 1] >= 'a' && abt[2 * p + 1] <= 'z')
				k = abt[2 * p + 1] - 'a' + 0x0a;
			else
				k = abt[2 * p + 1] - 'A' + 0x0a;
			int a = (j << 4) + k;
			byte b = (byte) a;
			bbt[p] = b;
		}
		return bbt;
	}

	/**
	 * 16进制串转换为byte字节：
	 *
	 * @param hexString
	 *            the hex string
	 * @return byte
	 */
	public static byte hexStringToByte(String hexString) {
		hexString = hexString.toUpperCase();
		if (hexString.length() == 1)
			hexString = "0" + hexString;
		int length = hexString.length() / 2 == 0 ? 1 : hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		Byte d = null;
		int pos = 0;
		byte hByte = (byte) (charToByte(hexChars[pos]) << 4);
		byte lByte = charToByte(hexChars[pos + 1]);
		d = (byte) (hByte | lByte);
		return d;
	}

	/**
	 * 16进制串转换为byte流
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString) {
		hexString = hexString.toUpperCase();
		if (hexString.length() == 1)
			hexString = "0" + hexString;
		int length = hexString.length() / 2 == 0 ? 1 : hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			byte hByte = (byte) (charToByte(hexChars[pos]) << 4);
			byte lByte = charToByte(hexChars[pos + 1]);
			d[i] = (byte) (hByte | lByte);
		}
		return d;
	}

	/**
	 * 16进制串转换为固定长度byte流，不足左侧补零
	 * 
	 * @param hexString
	 * @return byte[]
	 */
	public static byte[] hexStringToBytes(String hexString, int size) {
		hexString = hexString.toUpperCase();
		if ((hexString.length() == 1) || ((hexString.length()) % 2 != 0)) {
			hexString = "0" + hexString;
		}
		int length = hexString.length() / 2 == 0 ? 1 : hexString.length() / 2;
		if (length < size) {
			int iremain = size - length;
			for (int k = 0; k < iremain; k++) {
				hexString = "00" + hexString;
			}
		}
		char[] hexChars = hexString.toCharArray();
		byte[] d = new byte[size];
		for (int i = 0; i < size; i++) {
			int pos = i * 2;
			byte hByte = (byte) (charToByte(hexChars[pos]) << 4);
			byte lByte = charToByte(hexChars[pos + 1]);
			d[i] = (byte) (hByte | lByte);
		}
		return d;
	}

	/**
	 * 16进制串转换为byte字节：
	 *
	 * @param hexString
	 *            the hex string
	 * @return byte[]
	 */
	public static byte strToByte(String hexString) {
		hexString = hexString.toUpperCase();
		if (hexString.length() == 1)
			hexString = "0" + hexString;
		int length = hexString.length() / 2 == 0 ? 1 : hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		Byte d = null;
		int pos = 0;
		byte hByte = (byte) (charToByte(hexChars[pos]) << 4);
		byte lByte = charToByte(hexChars[pos + 1]);
		d = (byte) (hByte | lByte);
		return d;
	}

	/**
	 * 将字符串转为定长字节数组
	 * 
	 * @param str
	 *            字符串
	 * @param arrayLen
	 *            字节数组长度
	 * @return 字节数组
	 */
	public static byte[] strToBytes(String str, int arrayLen) {
		byte[] byteArray = new byte[arrayLen];
		byte[] strArray = str.getBytes();
		System.arraycopy(strArray, 0, byteArray, 0, strArray.length);
		return byteArray;
	}

	/**
	 * Bit转Byte
	 */
	public static byte BitStrToByte(String byteStr) {
		int re, len;
		if (null == byteStr) {
			return 0;
		}
		len = byteStr.length();
		if (len != 4 && len != 8) {
			return 0;
		}
		if (len == 8) {// 8 bit处理
			if (byteStr.charAt(0) == '0') {// 正数
				re = Integer.parseInt(byteStr, 2);
			} else {// 负数
				re = Integer.parseInt(byteStr, 2) - 256;
			}
		} else {// 4 bit处理
			re = Integer.parseInt(byteStr, 2);
		}
		return (byte) re;
	}

	/**
	 * BCD编码的byte转化到二进制byte
	 * 
	 * @param data
	 * @return
	 */
	public static byte BCDConvertToByte(byte data) {
		byte hight = (byte) ((data >>> 4) & 0x0F);
		byte low = (byte) (data & 0x0F);
		if (hight > 9 || low > 9) {
			return data;
		}
		int intHight = CommFunction.unsignByteToInt(hight);
		int intLow = CommFunction.unsignByteToInt(low);
		return CommFunction.intToUnsignByte(intHight * 10 + intLow);
	}

	/**
	 * 字符转byte
	 * 
	 * @param c
	 *            char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String byteToCH(byte[] bsrc) throws UnsupportedEncodingException {
		return byteToCH(bsrc, "GB2312");
	}

	public static String byteToCH(byte[] bsrc, String ChineseEncoding) throws UnsupportedEncodingException {
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

	public static byte[] longstr2Tobytes(String str, int size) {
		Long l = Long.parseLong(str);
		String hexStr = l.toHexString(l);
		hexStr = CommFunction.padLeft(hexStr, size, "0");
		byte[] b = CommFunction.hexStringToBytes(hexStr, size);
		return CommFunction.reserveBytes(b);
	}

	public static byte[] longTobytes(Long l, int size) {
		// Long l = Long.parseLong(str);
		String hexStr = l.toHexString(l);
		hexStr = CommFunction.padLeft(hexStr, size, "0");
		byte[] b = CommFunction.hexStringToBytes(hexStr, size);
		return CommFunction.reserveBytes(b);
	}

	// 占一个字节的10进制字符串转成byte：
	public static byte strTobytes(String str) {
		Integer i = Integer.parseInt(str);
		String hexStr = i.toHexString(i);
		return CommFunction.hexStringToByte(hexStr);
	}

	/**
	 * 从一个byte[]数组中截取一部分
	 * 
	 * @param src
	 * @param begin
	 * @param count
	 * @return
	 */
	public static byte[] subBytes(byte[] src, int begin, int count) {
		byte[] bs = new byte[count];
		for (int i = begin; i < begin + count; i++)
			bs[i - begin] = src[i];
		return bs;
	}

	/**
	 * 字节数组反序
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] reserveBytes(byte[] source) {
		byte[] ret = new byte[source.length];
		for (int i = 0; i < source.length; i++) {
			ret[source.length - 1 - i] = source[i];
		}
		return ret;
	}

	public static Date timeStamp(byte[] src) throws ParseException {
		long time = CommFunction.bytesToInt(CommFunction.reserveBytes(src));
		return new Date((time + init) * 1000);
	}

	/**
	 * 字符串补全
	 * 
	 * @param src
	 * @param len
	 * @param pad
	 * @return
	 */
	public static String padLeft(String src, int len, String pad) {
		StringBuffer des = new StringBuffer("");
		int srclen = src.length();
		int padlen = len - srclen;
		for (int i = 0; i < padlen; i++) {
			des = des.append(pad);
		}
		des.append(src);
		return des.toString();
	}

	// 判断字符串是否有中文：
	public static boolean checkstr(String str) {
		boolean results;
		String test = "[\\u4E00-\\u9FA5]+";
		Pattern p = Pattern.compile(test);
		Matcher m = p.matcher(str);
		results = m.find();
		return results;
	}

	public static Date strToDate(String millisecond) {
		Date d;
		Long millSec = Long.parseLong(millisecond);
		long interval = (millSec + init) * 1000;
		d = new Date(interval);
		return d;
	}
	
	/*
	 * 处理58心跳报文
	 */
	public static byte[]  deal58Message(byte[] data,Byte version) {
		int len =5;
		if(version.equals((byte) 0x35)){
			len=22;
		}		
		byte[] head = new byte[len];
		byte[] body = new byte[6];
		ArraysN.copy(head, 0, data, 0, len);
		ArraysN.copy(body, 0, data, len+1, 6);
		
		byte[] rebody=ArraysN.addBytes(MessageInfo.msg_0x48, body);
		
		int validnum = CommFunction.sumValidityValue(rebody);
		
		byte[] valid = CommFunction.shortToLowBytes(validnum);
		
		rebody=ArraysN.addBytes(rebody, valid);
		
		byte[] message =ArraysN.addBytes(head, rebody);
		return message;
	}

}
