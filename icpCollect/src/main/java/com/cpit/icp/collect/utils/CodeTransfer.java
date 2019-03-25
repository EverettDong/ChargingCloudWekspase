package com.cpit.icp.collect.utils;

public class CodeTransfer {
	private static StringBuilder result = new StringBuilder();
	/**
	 * 16进制字符集
	 */
	private static char[] cHEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };
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
	 * 字符转byte
	 *
	 * @param c char
	 * @return byte
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	/**
	 * 字节数组转成Hex字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String byteArrayToHexStr(byte[] data) {
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
	 * @param prefix 
	 * @return String
	 */
	public static String byteToHexStr(byte data,boolean prefix) {
		result.setLength(0);
		byte tmp = (byte) (data & 0xFF);
		char hi = cHEX[((tmp & 0xF0) >>> 4)];// 高4位
		char low = cHEX[(tmp & 0x0F)];// 低4位

		result.append(hi);
		result.append(low);
		if(prefix){
			return "0x".concat(result.toString());
		}else
		{
			return result.toString();
		}
		

	}
	

}
