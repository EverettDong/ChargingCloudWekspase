package com.cpit.icp.collect.coderDecoder.common.configurable;





import com.cpit.icp.collect.coderDecoder.util.ArraysN;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;


/**
 * user for encode
 * 数据类型处理
 */
public class _type {
	
	/**
	 * 16进制字符集
	 */
	private static char[] cHEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'A', 'B', 'C', 'D', 'E', 'F' };
	
	public static byte[] _byte(String value,int size,String ChineseEncoding) {
       return CommFunction.hexStringToBytes(value,size);
    }
	public static byte[] _cdqtype(String value,int size,String ChineseEncoding) {
	       return CommFunction.hexStringToBytes(value,size);
	    }
	
	public static byte[] _time(String value,int size,String ChineseEncoding) {
		return MessgeTransfer.TimeTobytes(value);
	}
	public static byte[] _timeN(String value,int size,String ChineseEncoding) {
		return MessgeTransfer.TimeTobytes(value);
	}
	
	public static byte[] _date(String value,int size,String ChineseEncoding) {
		return MessgeTransfer.DateTobytes(value);
	}
	
	public static byte[] _int(String value,int size,String ChineseEncoding) {
		if(size==1){
			return CommFunction.intToUnsignByte(value);
		}
		if(size==2){
			return MessgeTransfer.IntstrTobytes(value);
		}else{
			return MessgeTransfer.longstr2Tobytes(value,size);
		}
    }
	
	public static byte[] _int2(String value,int size,String ChineseEncoding) {
		
		return CommFunction.intToUnsignByte(value);
    }
	
	public static byte[] _ascii(String value,int size,String ChineseEncoding){
		return CommFunction.asciiStringToByteArray(value,size);
	}
	public static byte[] _ascII(String value,int size,String ChineseEncoding){
		return CommFunction.asciiStringToByteArray(value,size);
	}
	
	public static byte[] _GB(String value,int size,String ChineseEncoding){
		byte[] bsrcdata = new byte[size];
		for(int iGB=0;iGB<size;iGB++){
			bsrcdata[iGB]=0x20;
		}
		byte[] btempasc=MessgeTransfer.isCS(value,ChineseEncoding);
		ArraysN.copy(bsrcdata, 0, btempasc,0,btempasc.length);
		return bsrcdata;
	}
	
	public static byte[] _amount(String value,int size,String ChineseEncoding){
		return MessgeTransfer.AmountstrTobytes(value,size);
	}
	
	public static byte[] _carstr(String value,int size,String ChineseEncoding){
		return MessgeTransfer.CarCodeTobytes(value,ChineseEncoding);
	}
	
	public static byte[] _kwhint(String value,int size,String ChineseEncoding){
		if(size==4){
			byte[] bsrcdata = new byte[size];
			byte[] temp = MessgeTransfer.kwhIntstrTobytes(value);
			ArraysN.copy(bsrcdata, 0, temp,0,temp.length);
			return bsrcdata;
		}else{
			return MessgeTransfer.kwhIntstrTobytes(value);
		}
	}
	
	public static byte[] _hex(String value,int size,String ChineseEncoding){
		return MessgeTransfer.mulitystrTohexbytes(value,size);
	}
	
	public static byte[] _ip(String value,int size,String ChineseEncoding){
		return MessgeTransfer.IpstrTobytes(value);
	}
	
	public static byte[] _timeStamp(String value,int size,String ChineseEncoding){
		return MessgeTransfer.StrToTimeStampbyte(value);
	}
}

