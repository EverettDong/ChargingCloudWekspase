package com.cpit.icp.collect.coderDecoder.util;






//import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
//import java.text.DecimalFormat;
//import java.text.NumberFormat;
//import java.util.ArrayList;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

/**
 * 协议数据转化工具类
 * 
 * @author muqing
 * 
 */
public class CommFunction {

	public static final String SEPARATOR = ">";
	public static final String CHARSET = "UTF-16BE";
	public static final String PACKAGE_HEADER_BYTE = "D7";
	private static final int MOBILE_NUM_BYTES_LENGTH = 6;
	private static final int BYTE_LENGTH = 1;

	/**
	 * 16进制字符集
	 */
	private static char[] cHEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static Map<Character, Integer> hexMap = new HashMap<Character, Integer>();
	static {
		hexMap.put('0', 0);
		hexMap.put('1', 1);
		hexMap.put('2', 2);
		hexMap.put('3', 3);
		hexMap.put('4', 4);
		hexMap.put('5', 5);
		hexMap.put('6', 6);
		hexMap.put('7', 7);
		hexMap.put('8', 8);
		hexMap.put('9', 9);
		hexMap.put('A', 10);
		hexMap.put('B', 11);
		hexMap.put('C', 12);
		hexMap.put('D', 13);
		hexMap.put('E', 14);
		hexMap.put('F', 15);
	}

	//private static StringBuilder result = new StringBuilder();
	//private static List<String>  commandNameList=Lists.newArrayList(); 


	/**
	 * Integer型转成字节数组<br>
	 * 数组0存放的是高字节， 数组3存放的是低字节
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] intToByteArray(int data) {
		byte[] bs = new byte[4];
		for (int i = 0; i < 4; i++) {
			bs[i] = (byte) (data >>> ((3 - i) * 8) & 0xFF);
		}
		return bs;
	}
	/**
	 * Integer型转成字节数组<br>
	 * 数组0存放的是高字节， 数组3存放的是低字节
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] intTo2ByteArray(int data) {
		byte[] bs = new byte[2];
		for (int i = 0; i < 2; i++) {
			bs[i] = (byte) (data >>> ((1 - i) * 8) & 0xFF);
		}
		return bs;
	}
	/**
	 * Short型转成字节数组<br>
	 * 数组0存放的是高字节， 数组1存放的是低字节
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] shortToByteArray(short data) {
		
		byte[] bs = new byte[2];

		bs[0] = (byte) (data >>> 8);
		bs[1] = (byte) (data & 0xFF);
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
	 * 整数转换成一个无符号的字节 整数范围是0~255
	 * 
	 * @param value
	 * @return byte
	 */
	public static byte intToUnsignByte(int value) {
		if ((value >= 0) && (value <= 255)) {
			return intToByteArray(value)[3];
		} else {
			throw new IllegalArgumentException("整数值超过0~255的范围");
		}
	}
	
	public static byte[] intToUnsignByte(String value) {
		int i = Integer.parseInt(value);
		byte[] by = new byte[1];
		by[0] = intToUnsignByte(i);
		return by;
	}
	/**
	 * 字节数组转成Hex字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String byteArrayToHexStr1(byte[] data) {
		//
		
		StringBuilder result = new StringBuilder();
		result.setLength(0);
		for (int i = 0; i < data.length; i++) {
			byte tmp = (byte) (data[i] & 0xFF);
			char hi = cHEX[((tmp & 0xF0) >>> 4)];// 高4位
			char low = cHEX[(tmp & 0x0F)];// 低4位

			result.append(hi);
			result.append(low);
			if (i != (data.length - 1)) {
				result.append("-");
			}
		}
		return result.toString();
	}

	
	public static String byteArrayToHexStr2(byte[] data) {
		//
		StringBuilder result = new StringBuilder();
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
	 * 字节数组转成Hex字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String byteArrayToHexStr(byte[] data) {
		//
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
	
	/**
	 * 字节转成Hex字符串
	 * 
	 * @param data
	 * @return String
	 */
	public static String byteToHexStr(byte data) {
		StringBuilder result = new StringBuilder();
		result.setLength(0);
		byte tmp = (byte) (data & 0xFF);
		char hi = cHEX[((tmp & 0xF0) >>> 4)];// 高4位
		char low = cHEX[(tmp & 0x0F)];// 低4位

		result.append(hi);
		result.append(low);
		return result.toString();

	}
	/**
	 * return 0x
	 * @param data
	 * @return
	 */
	public static String byteToPreHexStr(byte data) {
		String temp = byteToHexStr(data);
		StringBuilder sb = new 	StringBuilder();
		sb.append("0x");
		sb.append(temp);
		return sb.toString();
	}

	public static String byteToHexStr2(byte data) {
		 String temp = byteToHexStr(data);
		 return temp.length()>1?temp.substring((temp.length()-2)):"0"+temp;
		
	}
	/**
	 * 将长度小于等于4的 byte数组转成整数 数组0 表示高位 有符号位转化
	 * 
	 * @param data
	 * @return int
	 */
	public static int byteArrayToInt(byte[] data) {
		if (data == null || data.length > 4 || data.length == 0) {
			return 0;
		}
		int value = 0;
		int sign = data[0] >>> 7;
		int tmp = data[0] & 0x7F;

		if (data.length == 4) {
			value = (tmp << 24) + (unsignByteToInt(data[1]) << 16)
					+ (unsignByteToInt(data[2]) << 8)
					+ (unsignByteToInt(data[3]));
			value = (sign << 31) | value;
		} else if (data.length == 3) {
			value = (tmp << 16) + (unsignByteToInt(data[1]) << 8)
					+ (unsignByteToInt(data[2]));
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
	public static int byteArrayToUnsignInt(byte[] data) {
		if (data == null || data.length != 2) {
			return 0;
		}
		int value = 0;
		value = value | (unsignByteToInt(data[0]) << 8);
		value = value | unsignByteToInt(data[1]);
		return value;
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
	 * BCD编码的byte转化到二进制byte
	 * 
	 * @param data
	 * @return
	 */
	public static byte bcdConvertToByte(byte data) {
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
	 * 字节转化成二进制0，1形式的字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String byteConvertToStr(byte data) {
		StringBuilder result = new StringBuilder();
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
	 * 字节转化成Ascii字符
	 * 
	 * @param b
	 * @return char
	 */
	public static char byteToAsciiChar(byte b) {
		return (char) (unsignByteToInt(b));
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



	/**
	 * 从指定字节中取出索引index的bit值
	 * 
	 * @param b
	 *            字节
	 * @param index
	 *            索引
	 * @return int bit值
	 */
	public static int getBitValueFromByte(byte b, int index) {
		int value = 0;
		int tmp = unsignByteToInt(b);
		value = (tmp >>> index) & 0x1;
		return value;
	}

	/**
	 * 比较两个字节是否相等
	 * 
	 * @param srcByte
	 * @param desByte
	 * @return boolean
	 */
	public static boolean compareByte(byte srcByte, byte desByte) {
		int srcInt = unsignByteToInt(srcByte);
		int desInt = unsignByteToInt(desByte);
		return (srcInt == desInt) ? true : false;

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

	public static char[] bytesToAscii(byte[] src){
		char[] cdes=new char[src.length];
		//lihong modify 2015.7.27
        int templen=src.length;
        int icount=0;
        int jcount=0;
        for(int k=0;k<templen;k++){
        	Byte sbyte=src[k];
        	if(sbyte.equals((byte)0xFF)){
        	  icount++;
        	  cdes[k]='F';
        	}       	
        }
        if(icount>=templen){
        	return cdes;
        }
        for(int j=0;j<templen;j++){
        	Byte sbyte=src[j];
        	if(sbyte.equals((byte)0x00)){
        	  jcount++;
        	  cdes[j]=' ';
        	  src[j]=0x20;
        	}       	
        }
        if(jcount>=templen){
        	cdes[0]='0';
        	return cdes;
        }
        ///////////////////////////////
		for(int i=0;i<src.length;i++){
			cdes[i]=byteToAsciiChar(src[i]);
		}
		return cdes;
	}
	/**
	 * ascii字符组成的字符串转换成字节数组
	 * 
	 * @param asciiStr
	 * @return byte[] 4个字节的字节数组
	 */
	public static byte[] asciiStringToByteArray(String asciiStr) {
		return CommFunction.asciiStringToByteArray(asciiStr, 4);
	}

	/**
	 * ascii字符组成的字符串转换成字节数组
	 * 
	 * @param asciiStr
	 * @param num
	 *            返回的字节数
	 * @return byte[]
	 */
	public static byte[] asciiStringToByteArray(String asciiStr, int num) {
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
			tmp = asciiStr+sub;
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
	 * 16进制串转换为byte流
	 *
	 * @param hexString the hex string
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
	public static byte[] hexStringToBytes(String hexString,int size) {
		hexString = hexString.toUpperCase();
		if ((hexString.length() == 1)||((hexString.length())%2!=0)){
			hexString = "0" + hexString;}       
		int length = hexString.length() / 2 == 0 ? 1 : hexString.length() / 2;
		if(length<size){
			int iremain=size-length;
			for(int k=0;k<iremain;k++){
				hexString="00"+hexString;
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
	 * @param hexString the hex string
	 * @return byte[]
	 */
	public static byte hexStringToByte(String hexString) {
		hexString = hexString.toUpperCase();
		if (hexString.length() == 1)
			hexString = "0" + hexString;
		int length = hexString.length() / 2 == 0 ? 1 : hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		Byte d=null;
		int pos = 0;
		byte hByte = (byte) (charToByte(hexChars[pos]) << 4);
		byte lByte = charToByte(hexChars[pos + 1]);
		d = (byte) (hByte|lByte);
		return d;
	}
	
	
	/**
	 * param: 0x79 with pre"0x"
	*/
	public static byte preHexStringToByte(String preHexString) {
	String newMsgCode = preHexString;
		if(preHexString.contains("0x")) {
			newMsgCode=	preHexString.substring(preHexString.indexOf("0x")+2, preHexString.length());
	
		}
		return hexStringToByte(newMsgCode);
	}
	

	/**
	 * 字符转byte
	 *
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	/**
	 * Convert byte[] to hex
	 * string.这里我们可以将byte转换成int，然后利用Integer
	 * .toHexString(byte)来转换成16进制字符串。
	 *
	 * @param src byte[] data
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
	
	/**
	 * 浮点数除法，采用BigDecimal进行计算，（return f1/f2）
	 * @param f1 
	 * @param f2
	 * @return float f1/f2
	 */
	public static float floatDivide(float f1,float f2){
		BigDecimal decimalF1=new BigDecimal(Float.valueOf(f1).toString());
		BigDecimal decimalF2=new BigDecimal(Float.valueOf(f2).toString());
		return decimalF1.divide(decimalF2).floatValue();
	}
    //encode检验和：
   public static String EncodeSumCheck(byte[]  CheckData) {
		String s=intToHexStr(CommFunction. sumValidityValue(CheckData));
		if(s.length()>1){
		s=s.substring((s.length()-2));
		}
		else { 
			s="0"+s;				
		}
	   return s;
   }
   //decode检验和：
   public static String SumCheck(byte[]  CheckData) {
		String s=intToHexStr(CommFunction. sumValidityValue(CheckData));
		s=s.substring((s.length()-2));
	   return s;
   }
   public static String SumCheck2(byte[]  CheckData) {
		String s = intToHexStr(CommFunction.sumValidityValue(CheckData));
	    return s.length()>1?s.substring((s.length()-2)):"0"+s;
  }
	/**
	 * 字节数组反序
	 * 
	 * @param source
	 * @return
	 */      
	public static byte[] reserveByteArray(byte[] source) {
		byte[] ret = new byte[source.length];
		for (int i = 0; i < source.length; i++) {
			ret[source.length - 1 - i] = source[i];
		}
		return ret;
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
	
	//记录到日志里：
			/**
			 * 记录字节数组
			 * 
			 * @param bytes
			 */
			public static StringBuilder LogByteArray(byte[] bytes) {

				StringBuilder sbByteArray = new StringBuilder();
							
				for (int i = 0; i < bytes.length; i++) {

					int byt = bytes[i] & 0xff;
					String value = Integer.toHexString(byt).toUpperCase();
					if (value.length() == 1) {
						value = "0" + value;
					}

					sbByteArray = sbByteArray.append(value + " ");

				}
				
				return sbByteArray;
			}	
			
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
			 * 将字符串转为定长字节数组
			 * 
			 * @param str
			 *            字符串
			 * @param arrayLen
			 *            字节数组长度
			 * @return 字节数组
			 */
			public static byte[] strToByte(String str, int arrayLen) {
				byte[] byteArray = new byte[arrayLen];
				byte[] strArray = str.getBytes();
				System.arraycopy(strArray, 0, byteArray, 0, strArray.length);
				return byteArray;

			}
			public static List<String> parseSrcstrToList(String commandIds) {
			
				List<String>  commandNameList=Lists.newArrayList(); 
				commandNameList =Lists.newArrayList(); 
				if (commandIds != null){
					String[] ids = StringUtils.split(commandIds, "#%#");
					commandNameList=Lists.newArrayList(ids);
				}
				return commandNameList;
			}

			/**
			 * Byte转Bit
			 */
			public static String byteToBit(byte b) {
				return "" +(byte)((b >> 7) & 0x1) + 
				(byte)((b >> 6) & 0x1) + 
				(byte)((b >> 5) & 0x1) + 
				(byte)((b >> 4) & 0x1) + 
				(byte)((b >> 3) & 0x1) + 
				(byte)((b >> 2) & 0x1) + 
				(byte)((b >> 1) & 0x1) + 
				(byte)((b >> 0) & 0x1);
			}

			/**
			 * Bit转Byte
			 */
			public static byte BitToByte(String byteStr) {
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
				} else {//4 bit处理
					re = Integer.parseInt(byteStr, 2);
				}
				return (byte) re;
			}
	public static void main(String[] args) {
		short s = 32623;
		byte[] b=CommFunction.shortToByteArray(s);
	
		int i = 65535;
		byte[] iArr = CommFunction.intTo2ByteArray(i);
	
	}

}



