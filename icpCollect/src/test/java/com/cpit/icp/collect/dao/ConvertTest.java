package com.cpit.icp.collect.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.common.Encodes;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
import com.cpit.icp.collect.main.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class ConvertTest {
	 public static byte[] hexString2ToBytes(String str) {
	        String[] strs = str.split(" ");
	        byte[] bytes = new byte[strs.length];
	        for (int i = 0; i < strs.length; i++) {
	            bytes[i] = hexStringToByte(strs[i]);
	        }
	        return bytes;
	    }
	 
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
	 
	 private static byte charToByte(char c) {
	        return (byte) "0123456789ABCDEF".indexOf(c);
	    }
	 
	 
	 @Test
		public void encodePackageContent79() throws IOException {
	
			
			
			String str ="01 02"+" "
			+"01 02"+" "
			+"01" +" "
			+"12 23" +" "
			+"32 34"+" "
			+"01 02" +" "
			+"01 02 03 04 05 06 07 08" +" "
			+stringToAscii("00012345671234567") +" "
			+"00 01 02 03 04 05 06 07" +" "
			+"01" +" "
			+"01" +" "
			+"01 02" +" "
			+"01 02" +" "
			+"00 00 00 01" +" "
			+"01" +" "
			+"01 02" +" "
			+"01" +" "
			+"E2 07 09 0a 0a 0a 0a ff" +" "
			+"E2 07 09 0a 0a 0a 0a ff" +" "
			+"E2 07 09 0a 0a 0a 0a ff" +" "
			+"00 00 00 01" +" "
			+"00 00 00 04" +" "
			;
					
		constructData(str,"79");
	
		}
		
		public static void constructData(String str ,String code) {
			String head ="FA F5 ";
			String ver="80 ";
			String serial="00 ";
			String msgCode=code+" ";
			
			byte[] bArr = hexString2ToBytes(str);
			byte[] bArr1 = new byte[bArr.length+1];
		
			bArr1=Arrays.copyOf(bArr,bArr.length+1);
			int blen = bArr1.length;
			bArr1[blen-1]=CommFunction.hexStringToByte(code);
			String len = CommFunction.byteToHexStr((byte) (bArr.length+3));
			String sum =CommFunction.EncodeSumCheck(bArr1);
		//	byte[] Bchecksum = CommFunction.hexStringToBytes(sum);
			
			String data= head+len+" "+ver+serial+msgCode+str+sum;
			System.out.println(data);
		}
		
		public static String stringToAscii(String value)  
		{  
		    StringBuffer sbu = new StringBuffer();  
		    char[] chars = value.toCharArray();   
		    for (int i = 0; i < chars.length; i++) {  
		        if(i != chars.length - 1)  
		        {  
		            sbu.append((int)chars[i]).append(" ");  
		        }  
		        else {  
		            sbu.append((int)chars[i]);  
		        }  
		    }  
		    return sbu.toString();  
		}
		
		
		@Test
		public void test() {
			String str ="E2 07 09 0a 0a 0a 0a ff";
			
			System.out.println(MessgeTransfer.TimebyteTostr(hexString2ToBytes(str)));
		}
		
		
		@Test
		public void encode34() {
			String str ="01 02"+" "
					+"01 02"+" "
					+"01" +" "
					+"01"+" "
					+"01"+" "
					+"00 00 00 01 00 00 00 02"+" "
					+"00 00 00 03 00 00 00 04"+" "
					+"00 01"+" "
					+"00 02"+" "
					+"00 03"+" "
					+"00 04"+" "
					+"01"+" "
					;
					
		constructData(str, "34");			
		
		}
		
		@Test
		public void encode39() {
			String str ="01 02"+" "
					+"01 02"+" "
					+"01" +" "
					+"01"+" "
					+"01 00"+" "
					+"00 01"+" "
					+"00 01"+" "
					+"01"+" "
					+"02"+" "
					+"00 00 00 03"+" "
					
					;
					
		constructData(str, "39");	
		}
}
