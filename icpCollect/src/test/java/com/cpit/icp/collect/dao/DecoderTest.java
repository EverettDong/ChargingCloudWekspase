/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.collect.dao
 * 文件名：DecoderTest.java
 * 版本信息：1.0.0
 * 日期：2018年7月27日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.icp.collect.dao;

import static com.cpit.icp.collect.utils.Consts.CODE_MODE;
import static com.cpit.icp.collect.utils.Consts.PHRASE;

import java.io.IOException;
import java.util.Random;

import org.apache.xmlbeans.impl.schema.StscChecker;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.coderDecoder.DecodedMsgDto;
import com.cpit.icp.collect.coderDecoder.common.configurable._type;
import com.cpit.icp.collect.coderDecoder.common.configurable.deviceInfo;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;

import com.cpit.icp.collect.coderDecoder.util.exception.ReceiveDataException;

import com.cpit.icp.collect.coderDecoder.util.configurable.DecodeRechargeDataDomainXml;
//import com.cpit.testplatform.modules.lzy.uTest;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureDecode;

/**
 * 类名称：DecoderTest
 * 类描述：
 * 创建人：zhangqianqian
 * 创建时间：2018年7月27日 下午2:58:27
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.NONE)
public class DecoderTest {
    ConfigureDecode ConfigureDecode = new ConfigureDecode();

    String equipName = "cpit";
    String ChineseEncoding = "GB2312";
    String deviceNo = "0102030405060708";

    @Test
    public void testDecoder() {
        System.out.println("testDecoder");
    }


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
    public void decode0x1B() {

        String str = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
        //System.out.println(ConfigureDecode.Decode10PackageNoStorage(uTest.hexString2ToBytes(str)));
String str10="FA F5 3F 80 39 10 01 03 0F 02 0B 0A 16 11 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 80 00 00 00 01 00 00 00 80 00 00 00 80 E3 07 01 12 00 00 00 FF E3 07 01 12 00 00 00 FF";
        String str2 = "FA F5 75 35 59 01 02 03 04 05 06 03 00 DB 07 0C 14 0D 0D 0D 2C 01 1b 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 02 01 80";
        //System.out.println(ConfigureDecode.Decode10PackageNoStorage(uTest.hexString2ToBytes(str2)));
String strs ="FA F5 3F 80 59 10 01 02 03 04 05 06 07 08 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 66";
        String str3 = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
      //  deviceInfo d=   ConfigureDecode.Decode10MsgStyle(hexString2ToBytes(strs));
//System.out.println(d.toString());
        String str4 = "FA F5 75 35 59 01 02 03 04 05 06 03 00 DB 07 0C 14 0D 0D 0D 2C 01 1b 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 6C 69 61 6E 67 7A 68 69 79 75 61 6E 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 0A 0F 01 02 01 16";
      
        String str_1B="FA F5 75 35 D4 01 01 01 01 02 10 23 37 DB 07 0C 14 0D 0D 0D 2C 01 1B 01 01 01 01 02 10 23 37 21 22 23 24 31 32 33 34 35 36 41 42 43 44 45 46 47 48 49 4A 01 02 03 00 05 02 00 00 DB 07 0C 14 0D 0D 0D 2C DA 07 0C 14 0D 0D 0D 2D DA 07 0C 14 0D 0D 1D 2D 31 32 33 34 35 36 37 38 39 38 39 3C 3D 3E 3F 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F 50 51 01 02 03 00 01 41";
         ConfigureDecode.Decode10Package(hexString2ToBytes(str10), equipName, 2);
    }

    @Test
    public void decode0x3C() {
        String str = "00 00 00 00 01 01 02 00 00 00 00 00 00 00 00 1A 00 19 01 05 FF FF 01 09 0E A8 0C 0E 00 00 00 00 01 03";//报文数据域
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x3C, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    @Test
    public void decode0x1c() {
        String str = "FA F5 1B 35 0C 01 32 fb ff 07 01 18 01 E1 07 01 12 09 34 37 42 02 1C 01 00 02 00 ff 60 30 ae";
        try {
            ConfigureDecode.DecodeReportPackageContent(hexString2ToBytes(str), equipName, 0, 3, ChineseEncoding,deviceNo, "3.5");
        } catch (ReceiveDataException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void DecodePackageStyle() throws ReceiveDataException {

        String str = "FA F5 09 80 59 58 01 02 03 04 05 06 33";
        System.out.println(ConfigureDecode.DecodePackageStyle(false, hexString2ToBytes(str), equipName, 0));

        String str2 = "FA F5 75 35 59 01 02 03 04 05 06 03 00 DB 07 0C 14 0D 0D 0D 2C 01 1b 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 02 01 80";
        // System.out.println(ConfigureDecode.DecodePackageStyle(false,hexString2ToBytes(str2),equipName,0));
    }

    @Test
    public void decode0x31() {
        String str = "00 00 00 00 01 FF FF 02 04 00 00 FF FF 13 01 00 00 01 00 00 00 00 FF FF FF FF FF 01";//报文数据域
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x31, "3.4", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }
    @Test
    public void decode0x34_data() {
        String str = "00 00 00 00 01 01 02 00 00 00 00 00 00 00 00 01 01 01 01 01 01 01 01 00 01 00 02 00 02 00 01 01";//报文数据域
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x34, "3.4", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }
    @Test
    public void decode0x11() {
        String str = "01 02 03 04 05 06 07 08 02 02 00 00 04 01 01 01 01 01 01 01 01";//报文数据域
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x11, "3.4", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    @Test
    public void decode0x3A() {
        String str = "00 00 00 00 01"
        		+ " 01 "
        		+ "03 "
        		+ "02 04 00 00 FF FF 02 00 "
        		+ "00 00 "
        		+ "00 00 "
        		+ "00 00 "
        		+ "00 FF "
        		+ "FF "
        		+ "FF "
        		+ "FF FF "
        		+ "01 01 "
        		+ "01 "
        		+ "02 "
        		+ "01 "
        		+ "02 "
        		+ "00 01";//报文数据域
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x3A, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    @Test
    public void decode0x39() {
        String str = "00 00 00 00 01 01 00 00 00 00 01 00 05 05 B5 08 00 00";//报文数据域
     String str1 = "FA F5 15 80 00 39 01 02 01 02 01 01 01 00 00 01 00 01 01 02 00 00 00 03 4A";  
     String strs ="FA F5 22 80 6B 39 00 00 00 00 02 01 01 00 00 01 44 00 34 34 BA 01 00 00 02 ff ff ff ff 44 ff 34 36 BA ff ff ff 07";
        String[] str2 = null;
        DecodedMsgDto decodecMsgDto = null;
        try {
        	decodecMsgDto=   ConfigureDecode.DecodeReportPackageContent(hexString2ToBytes(strs), "name", 1, 3, "", "3.4",deviceNo);
         str2= decodecMsgDto.getDecodedMsg();
        	//str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x39, "3.4", ChineseEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    @Test
    public void decode0x70() {
        String str = getRandomHexString(4) + " " + getRandomAscii(16) + " 01 " + getRandomAscii(20) + " 01 02 " + getRandomAscii(55) + " " + getRandomTime()
                + " " + getRandomAscii(16) + " 01 " + getRandomAscii(12) + " " + getRandomHexString(8);// 报文数据域
        System.out.println("0x70解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(
                    hexString2ToBytes(str), (byte) 0x70, "3.5",
                    ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    @Test
    public void decode0x71() {
        //String str = getRandomHexString(4) + " " + getRandomAscii(16) + " " + "40 " + getRandomTime();
    	//String str="00 00 00 00 36 38 30 36 31 33 31 39 30 30 30 30 30 32 36 38 20 D7 07 01 01 00 00 34 FF ";
    	String str ="FA F5 20 80 15 71 00 00 00 00 36 38 30 36 31 33 31 39 30 30 30 30 30 32 36 38 20 D7 07 01 01 00 00 34 FF D6";
    String str51="FA F5 0B 80 0B 51 01 02 02 00 00 00 00 00 56";
    	
    	System.out.println("0x71解码前:" + str);
        String[] str2 = null;
        DecodedMsgDto decodecMsgDto = null;
        try {
        	decodecMsgDto =  ConfigureDecode.DecodeReportPackageContent(hexString2ToBytes(str51), "name", 1, 3, "", "3.4");
         str2=decodecMsgDto.getDecodedMsg();
        	//DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x71, "3.5", ChineseEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    //0x72	包含充电枪位置充/放电类型
    @Test
    public void decode0x72() {
        String str = getRandomHexString(4) + " 02 01 02 " + getRandomHexString(8) + " " + getRandomHexString(8) + " " + getRandomTime();//报文数据域
        System.out.println("0x72解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x72, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }
    
 
    @Test
    public void decode0x74() {
        String str = getRandomHexString(5) + " " + getRandomAscii(16) + " 01";//报文数据域
        System.out.println("0x74解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x74, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    //0x75
    @Test
    public void decode0x75() {
        String str = getRandomHexString(4) + " 01 " + getCarstr() + " " + getRandomTime();//报文数据域
        System.out.println("0x75解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x75, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    //0x7A
    @Test
    public void decode0x7A() {
        String str = getRandomHexString(4) + " " + getRandomAscii(16) + " 01 " + getRandomAscii(20) + " 01 02 " + getRandomAscii(55) + " " + getRandomTime() + " 01 01 " + getRandomHexString(8);//报文数据域
        System.out.println("0x7A解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x7A, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    //0x7B
    @Test
    public void decode0x7B() {
        String str = getRandomHexString(4) + " " + getRandomAscii(16) + " " + getRandomHexString(8) + getRandomTime() + " " + getRandomHexString(8);//报文数据域
        System.out.println("0x7B解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x7B, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    @Test
    public void decode0x7E() {
        String str = getRandomHexString(4) + " " + getRandomHexString(1) + " " + getRandomAscii(16) + " " + getRandomAscii(15) + " " + getRandomHexString(5) + " " + getRandomHexString(8) + " " +
                getRandomHexString(4) + getRandomTime() + getRandomTimeStamp();
        System.out.print("0x7E解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x7E, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
        System.out.print(str2[0]);
    }

    @Test
    public void decode0x41() {
        String str = getRandomHexString(4) + " " + getRandomHexString(4) + " " + getRandomHexString(4) + " " + getRandomHexString(4) + " " + getRandomHexString(4) + " " + getRandomHexString(4);
        System.out.print("0x41解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x41, "3.4", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
        System.out.print(str2[0]);

    }

    @Test
    public void decode0x48() {
        String str = getRandomHexString(4) + "" + getRandomHexString(2);
        System.out.print("0x48解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x48, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
        System.out.print(str2[2]);
    }

    @Test
    public void decode0x40() {
        String str = getRandomHexString(4) + " 01 00 01 00 01 00 01 00 01 00 01 00 01 00 ";
        System.out.print("0x40解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x40, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    @Test
    public void decode0x4B() {
        String str = getRandomHexString(4) + " " + getRandomHexString(16) + " " + getRandomHexString(2);
        System.out.print("0x4B解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x4B, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }


    @Test
    public void decode0x42() {
        String str = getRandomHexString(4) + " " + getRandomTime();
        System.out.print("0x42解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x42, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
        System.out.print(str2[2]);
    }

    @Test
    public void decode0x53() {
        String str = getRandomHexString(4) + " 01";
        System.out.print("0x42解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x53, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
        System.out.print(str2[2]);
    }


    @Test
    public void decode0x44() {
        String str = getRandomHexString(4) + " " + getRandomTime();
        System.out.print("0x44解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x44, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @return
     * @Author donghaibo
     * @Description 智能终端请求时间同步命令
     * @Date
     * @Param
     **/
    @Test
    public void decode0x54() {
        String str = getRandomHexString(4) + " " + getRandomHexString(8);
        System.out.print("0x54解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x54, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }


    @Test
    public void decode0x55() {
        String str = getRandomHexString(4) + " " + getRandomHexString(3) + " " + getRandomHexString(3) + " 01";
        System.out.print("0x55解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x55, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }


    @Test
    public void decode0x56() {
        String str = getRandomHexString(4) + " 01 00 01 01";
        System.out.print("0x56解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x56, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @return
     * @Author donghaibo
     * @Description 智能终端当前配置参数查询应答
     * @Date
     * @Param
     **/
    @Test
    public void decode0x57() {
        String str = getRandomHexString(4) + " " + getRandomHexString(2) + " 02 " + getRandomHexString(4) + " " + getRandomHexString(16) + " " +
                getRandomHexString(2) + " " + getRandomTime() + " " + getRandomHexString(8) + " 01 02 01 " + getRandomAscii(4) + " " + getRandomHexString(2) + " " +
                getRandomAscii(100) + " 02 45 03 60 00 ";
        System.out.print("0x57解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x57, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }


    /**
     * @return
     * @Author donghaibo
     * @Description 心跳信息上报指令
     * @Date
     * @Param
     **/
    @Test
    public void decode0x58() {
        String str = getRandomHexString(4) + " 01 02 ";
        System.out.print("0x58解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x58, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    @Test
    public void decode0x59() {
        String str = getRandomHexString(4) + " 01 02 03 00 ";
        System.out.print("0x59解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x59, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    @Test
    public void decode0x05() {
        String str = getRandomHexString(4) + " 03 02 89 " + getRandomHexString(2) + " 03 04 02 03 08 ";
        System.out.print("0x05解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x05, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    @Test
    public void decode0x06() {
        String str = getRandomHexString(4) + " 04";
        System.out.print("0x06解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x06, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @return
     * @Author donghaibo
     * @Description 协议3.5充电设备当前总体状态应答
     * @Date
     * @Param
     **/
    @Test
    public void decode10x31() {
        String str = getRandomHexString(4) + " 01 02 03 05 " + getRandomHexString(4) + " 01 " + getRandomHexString(8) + " 02 02 02 01 01 05 ";
        System.out.print("0x31解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x31, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @return
     * @Author donghaibo
     * @Description 充/放电模块当前电池信息查询应答/上报,3.5协议
     * @Date
     * @Param
     **/
    @Test
    public void decode10x3A() {
        String str = getRandomHexString(4) + " 20 01 02 " + getRandomHexString(8) + " " + getRandomHexString(8) + " 03 02" + getRandomHexString(4) + " 01 02 03 04 01 FF 04 FF 01 " + getRandomHexString(8) + " " + getRandomHexString(8) + " 02 FF" + getRandomHexString(4) + " 04 02 FF 05 06 09 FF 01 00 " + getRandomHexString(8) + " " + getRandomHexString(8) + " FF 01" + getRandomHexString(4) + " 01 02 FF 04 00 " + getRandomHexString(2);
        System.out.print("0x3A解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x3A, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    @Test
    public void decode0x33() {
        String str = getRandomHexString(4) + " 03 01 " + getRandomHexString(6) + " 02 02 " + getRandomHexString(6) + " 02 01 01 " + getRandomHexString(6) + " 02 03 ";
        System.out.print("0x33解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x33, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @return
     * @Author donghaibo
     * @Description 充/放电模块当前充电查询应答/上报
     * @Date
     * @Param
     **/
    @Test
    public void deocde0x39() {
        String str = getRandomHexString(4) + " 02 02 " + getRandomHexString(6) + " 00 FF " + getRandomHexString(4);
        System.out.print("0x39解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x39, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @return
     * @Author donghaibo
     * @Description充/放电模块当前状态信息查询应答
     * @Date
     * @Param
     **/
    @Test
    public void deocde0x3c() {
        String str = getRandomHexString(4) + " 02 02 01 04" + getRandomHexString(8) + " " + getRandomHexString(8) + " " + getRandomHexString(8) + " 00 ";
        System.out.print("0x3c解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x3c, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }


    /**
     * @return
     * @Author donghaibo
     * @Description电池单体当前信息查询应答/上报
     * @Date
     * @Param
     **/
    @Test
    public void decode0x36() {
        String str ="00 00 00 00"+ " 02 02 01 " + getRandomHexString(8) + " 01 ";
        System.out.print("0x36解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x36, "3.4", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @return
     * @Author donghaibo
     * @Description只能终端当前配电信息查询应答/上报
     * @Date
     * @Param
     **/
    @Test
    public void decode0x3B() {
        String str = getRandomHexString(4) + " 02 03 " + getRandomHexString(50) + " " + getRandomHexString(4) + " " + getRandomHexString(12) + " " + getRandomHexString(8) + " 02 01";
        System.out.print("0x3B解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x3B, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    //---------------------------------------------------0x60------------------------------------------------

    /**
     * @return
     * @Author donghaibo
     * @Description 账户查询应答
     * @Date
     * @Param
     **/
    @Test
    public void decode0x69() {
        String str = getRandomHexString(4) + " " + getRandomAscii(16) + " " + getRandomHexString(20) + " 00 00 05 " + getRandomHexString(40) + " " + getRandomHexString(30) + " " +
                getRandomHexString(36) + " " + getRandomAscii(15) + " " + getRandomHexString(20) + " FF FF FF FF FF FF FF FF" + getRandomTime() + " " + getRandomHexString(8);
        System.out.print("0x69解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x69, "3.4", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @Author donghaibo
     * @Description 服务类型申请指令应答
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0x61() {
        String str = getRandomHexString(4) +" "+ getRandomAscii(16) +" 20 "+ getRandomTime() +" "+ getRandomAscii(15) +" "+ getRandomAscii(20);
        System.out.print("0x61解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x61, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @Author donghaibo
     * @Description电池认证查询应答
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0x62() {
        String str = getRandomHexString(4) +" 02 01 03 "+ getRandomHexString(8) + " 06";
        System.out.print("0x62解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x62, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @Author donghaibo
     * @Description充/放电记录上报应答
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0x68() {
        String str = getRandomHexString(4) +" 01 "+ getRandomAscii(16) +" "+ getRandomHexString(30) +" "+ getRandomAscii(12) +" "+
                getRandomHexString(36) +" "+ getRandomAscii(15) +" "+ getRandomAscii(20) +" "+ getRandomTime();
        System.out.print("0x68解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x68, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @Author donghaibo
     * @Description充/放电记录查询指令应答
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0x64() {
        String str = getRandomHexString(4) +" 01 "+ getRandomAscii(16) +" 02 "+ getRandomHexString(8) +" 40 50 "+getRandomHexString(4) +" "+getRandomHexString(4) +" 01 02 03 01 "+getRandomTime()+" "+getRandomTime()+ " "+getRandomTime();
        System.out.print("0x64解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x64, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);

    }

    /**
     * @Author donghaibo
     * @Description车牌认证查询指令应答
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0x65() {
        String str = getRandomHexString(4)+" 01 "+getCarstr()+" "+getRandomAscii(17)+" "+getRandomTime();
        System.out.print("0x65解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x65, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @Author donghaibo
     * @Description合法用户认证通过指令
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0x6c() {
        String str = getRandomHexString(4)+" "+getRandomAscii(16)+" "+getRandomHexString(8)+" "+getRandomHexString(30)+" "+getRandomHexString(48)+" "+getRandomAscii(15)+" "+getRandomTime()+" 02 02"+getRandomHexString(16);
        System.out.print("0x6c解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x6c, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @Author donghaibo
     * @Description服务类别认证信息上报应答
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0x6B() {
        String str = getRandomHexString(4)+" "+getRandomAscii(16)+" "+getRandomAscii(8)+" "+getRandomTime()+""+getRandomHexString(8);
        System.out.print("0x6B解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x6B, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @Author donghaibo
     * @Description充/放电服务终止上报应答
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0x6E() {
        String str = getRandomHexString(4)+" 01 "+getRandomAscii(16)+" "+getRandomAscii(15)+" "+getRandomTime();
        System.out.print("0x6E解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x6E, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    /**
     * @Author donghaibo
     * @Description 智能终端充电预约操作命令
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0xC1() {
        String str = getRandomHexString(4)+" "+getRandomAscii(20)+" "+getRandomTime()+" "+getRandomTime()+" 01 20 03 "+getCarstr()+" "+getRandomAscii(16)+" 01 "+getRandomHexString(20);
        System.out.print("0xC1解码前:" + str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0xC1, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    /**
     * @Author donghaibo
     * @Description 智能终端充电预约查询命令
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0xC2(){
        String str = getRandomHexString(4);
        System.out.print("0xC2解码前:"+str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0xC2, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(str2[1]);
        System.out.println(str2[0]);
    }

    /**
     * @Author donghaibo
     * @Description 智能终端充电车位数据查询命令
     * @Date
     * @Param
     * @return
     **/
    @Test
    public void decode0xC3(){
        String str = getRandomHexString(4);
        System.out.print("0xC3解码前:"+str);
        String[] str2 = null;
        try {
            str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0xC3, "3.5", ChineseEncoding,deviceNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(str2[1]);
    }

    public String getRandomHexString(Integer num) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < num - 1; i++) {
            result.append(getRandomMaxString(255)
                    + " ");
        }
        result.append(getRandomMaxString(255));
        return result.toString();
    }

    public String getRandomTime() {
        String timeStr = getRandomHexString(2) + " " + getRandomMaxString(12) + " "
                + getRandomMaxString(29) + " "
                + getRandomMaxString(24) + " "
                + getRandomMaxString(59) + " "
                + getRandomMaxString(59) + " " + "FF";
        return timeStr;

    }

    public String getRandomTimeStamp() {
        String timeStr =
                getRandomMaxString(24) + " "
                        + getRandomMaxString(59) + " "
                        + getRandomMaxString(59);
        return timeStr;

    }

    public String getRandomDate() {
        String timeStr =
                getRandomMaxString(99) + " " + getRandomMaxString(20) + " " +
                        getRandomMaxString(12) + " "
                        + getRandomMaxString(28);
        return timeStr;
    }

    public String getRandomMaxString(Integer max) {
        String string = Integer.toHexString(new Random().nextInt(max));
        if (string.length() == 1) {
            string = "0" + string;
        }
        return string;
    }


    public String getRandomAscii(Integer num) {

        StringBuffer result = new StringBuffer();
        for (int i = 0; i < num - 1; i++) {
            Integer aInteger = new Random().nextInt(74) + 48;
            if ((aInteger >= 58 && aInteger <= 64) || (aInteger >= 91 && aInteger <= 96)) {
                num++;
                continue;
            }
            result.append(Integer.toHexString(aInteger) + " "
            );
        }
        result.append(Integer.toHexString(new Random().nextInt(74) + 48));
        return result.toString();
    }

    public String getCarstr() {
        return "C1 D4 41 42 31 32 33 34";
    }

    public String getCarstr2(String carStr) {
        return getHex(carStr, 8, "carstr");
    }

    public String getHex(String name, Integer num, String stype) {
        String string = "";
        switch (stype) {
            case "ascii":
                string = CommFunction.byteArrayToHexStr2(_type._ascii(name, num, "11"));
                break;
            case "int":
                string = CommFunction.byteArrayToHexStr2(_type._int(name, num, "11"));
                break;
            case "kwhint":
                string = CommFunction.byteArrayToHexStr2(_type._kwhint(name, num, "11"));
                break;
            case "carstr":
                string = CommFunction.byteArrayToHexStr2(_type._carstr(name, num, "11"));
                break;
            case "gb":
                string = CommFunction.byteArrayToHexStr2(_type._GB(name, num, "GB2312"));
                break;
            default:
                break;
        }
        return string;
    }
    
    
    /**zqq add
     * 
     */
    @Test
    public void decode0x79() {
  //  	String str ="FA F5 8A 80 00 79 01 02 01 02 01 06 06 06 06 07 07 07 07 08 08 08 08 09 09 09 09 50 48 49 56 48 57 49 48 49 54 51 57 48 48 48 50 48 49 56 48 57 49 48 49 54 52 48 48 48 48 48 48 48 48 49 01 02 03 04 05 06 07 08 48 48 48 49 50 51 52 53 54 55 49 50 51 52 53 54 55 00 01 02 03 04 05 06 07 01 01 01 02 01 02 00 00 00 01 01 01 02 01 E2 07 09 0a 0a 0a 0a ff E2 07 09 0a 0a 0a 0a ff E2 07 09 0a 0a 0a 0a ff 00 00 00 01 00 00 00 04 41";
  //  String str ="FA F5 8A 80 81 79 00 00 00 00 01 36 38 30 36 31 33 31 35 30 30 30 30 30 32 36 35 31 32 30 30 30 30 30 30 30 30 30 30 30 30 35 32 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 32 01 02 03 04 05 06 07 08 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 35 20 20 20 20 20 20 20 20 34 34 00 00 00 09 F5 00 00 00 01 01 00 51 DF 07 04 11 09 20 11 FF DF 07 04 11 09 24 16 FF DF 07 04 11 09 24 18 FF B8 01 00 00 C2 01 00 00 4F";
  // String str="FA F5 79 80 00 79 01 02 01 02 01 06 06 06 06 07 07 07 07 08 08 08 08 09 09 09 09 00 00 00 00 01 01 01 01 01 01 01 01 01 01 01 01 01 02 01 02 03 04 05 06 07 08 48 48 48 49 50 51 52 53 54 55 49 50 51 52 53 54 55 00 01 02 03 04 05 06 07 01 01 01 02 01 02 00 00 00 01 01 01 02 01 E2 07 09 0a 0a 0a 0a ff E2 07 09 0a 0a 0a 0a ff E2 07 09 0a 0a 0a 0a ff 00 00 00 01 00 00 00 04 ED"; 	
 //   String str="FA F5 5D 80 00 79 01 02 01 02 01 12 23 32 34 01 02 01 02 03 04 05 06 07 08 48 48 48 49 50 51 52 53 54 55 49 50 51 52 53 54 55 00 01 02 03 04 05 06 07 01 01 01 02 01 02 00 00 00 01 01 01 02 01 E2 07 09 0a 0a 0a 0a ff E2 07 09 0a 0a 0a 0a ff E2 07 09 0a 0a 0a 0a ff 00 00 00 01 00 00 00 04 04";	
 // String str ="FA F5 8A 80 81 79 00 00 00 00 01 34 38 30 36 31 33 31 35 30 30 30 30 30 32 36 35 31 32 30 30 30 30 30 30 30 30 30 30 30 30 35 32 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 32 01 02 03 04 05 06 07 08 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 35 A9 BE 20 20 20 20 32 33 34 44 12 00 05 09 F5 00 00 00 01 01 00 02 DF 07 04 11 09 20 11 FF DF 07 04 11 09 24 16 FF DF 07 04 11 09 24 18 FF B8 01 00 00 C2 01 00 00 71";
   String str="FA F5 8A 80 81 79 00 00 00 00 01 36 38 30 36 31 33 31 35 30 30 30 30 30 32 36 35 31 32 30 30 30 30 30 30 30 30 30 30 30 30 35 32 30 31 38 30 31 30 31 31 32 32 33 34 35 30 30 30 30 30 32 01 02 03 04 05 06 07 08 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 35 20 20 20 20 20 20 20 20 34 34 00 00 00 09 F5 00 00 00 01 01 00 51 DF 07 04 11 09 20 11 FF DF 07 04 11 09 24 16 FF DF 07 04 11 09 24 18 FF B8 01 00 00 C2 01 00 00 6B";
    	String[] str2 = null;
    	try {
    	 //  String[] str2 = null;
    	DecodedMsgDto	decodecMsgDto = null;

    	decodecMsgDto=   ConfigureDecode.DecodeReportPackageContent(hexString2ToBytes(str), "name", 1, 3, "", "3.4");
        str2= decodecMsgDto.getDecodedMsg();    
    	//  str2 = DecodeRechargeDataDomainXml.ParseData(, (byte) 0x79, "3.4", ChineseEncoding);
           } catch (Exception e) {
               e.printStackTrace();
           }
           System.out.println(str2[1]);
           System.out.println(str2[0]);
    }
   
    @Test
    public void decode0x3B_all() {
    //	String str="FA F5 23 80 00 34 01 02 01 02 01 01 01 00 00 00 01 00 00 00 02 00 00 00 03 00 00 00 04 00 01 00 02 00 03 00 04 01 52";
 // String str ="FA F5 23 80 6C 34 00 00 00 00 01 01 02 00 00 00 00 00 00 00 00 1A 00 19 01 05 FF FF 01 09 0E A8 0C 0E 00 04 00 02 4F";
   String str ="00 00 00 00 03 01 "
   		+ "44 54 44 44 44 44 44 44 44 44 44 54 44 44 44 44 44 44 44 44 44 54 44 44 44 44 44 44 44 44 44 54 44 44 44 44 44 44 44 44 44 54 44 44 44 44 44 44 44 44"
   		+ "20 20 20 20 20 20 20 20 20 20 20 20 20 20 00 01 00 00 00 00 00 00 00 00 00 02 ";
   		
    	String[] str2 = null;
      	try {
      	 //  String[] str2 = null;
            

      	  // str2=   ConfigureDecode.DecodeReportPackageContent(hexString2ToBytes(str), "name", 1, 3, "", "3.4");
                str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0x3B, "3.4", ChineseEncoding,deviceNo);
             } catch (Exception e) {
                 e.printStackTrace();
             }
      	
      	System.out.println(str2[0]==null);
       	System.out.println(str2[0].equalsIgnoreCase(null));
    	System.out.println(str2[0].equalsIgnoreCase(""));
    	System.out.println(!str2[0].equalsIgnoreCase(""));
             System.out.println(str2[1]);
             System.out.println(str2[0]);
    }
    
    @Test
    public void decode0x34() {
    //	String str="FA F5 23 80 00 34 01 02 01 02 01 01 01 00 00 00 01 00 00 00 02 00 00 00 03 00 00 00 04 00 01 00 02 00 03 00 04 01 52";
  String str ="FA F5 23 80 6C 34 00 00 00 00 01 01 02 00 00 00 00 00 00 00 00 1A 00 19 01 05 FF FF 01 09 0E A8 0C 0E 00 04 00 02 4F";
    	  String[] str2 = null;
      	try {
      	 //  String[] str2 = null;
      	 	DecodedMsgDto	decodecMsgDto = null;

      	 	decodecMsgDto=   ConfigureDecode.DecodeReportPackageContent(hexString2ToBytes(str), "name", 1, 3, "", "3.4");
           str2= decodecMsgDto.getDecodedMsg();  
      	   //  str2 = DecodeRechargeDataDomainXml.ParseData(, (byte) 0x79, "3.4", ChineseEncoding);
             } catch (Exception e) {
                 e.printStackTrace();
             }
      	
      	System.out.println(str2[0]==null);
       	System.out.println(str2[0].equalsIgnoreCase(null));
    	System.out.println(str2[0].equalsIgnoreCase(""));
    	System.out.println(!str2[0].equalsIgnoreCase(""));
             System.out.println(str2[1]);
             System.out.println(str2[0]);
    }
    
    @Test
    public void decode0xD1() {
    //	String str="FA F5 23 80 00 34 01 02 01 02 01 01 01 00 00 00 01 00 00 00 02 00 00 00 03 00 00 00 04 00 01 00 02 00 03 00 04 01 52";
 String str="00 00 00 00 30 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 e2 07 01 01 01 00 00 ff e2 07 01 02 01 00 00 ff 01 00 01 39 39 39 39 38 38 38 38 30 30 30 30 31 31 31 31 32 32 32 32 33 33 33 33 01 00 00 00 00 00 00 00 00 00 00 11 11 11 11 11 11 11 11 11 11";
    	  String[] str2 = null;
      	try {
      	 //  String[] str2 = null;
            

      	   //str2=   ConfigureDecode.DecodeReportPackageContent(hexString2ToBytes(str), "name", 1, 3, "", "3.4");
             str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str), (byte) 0xD1, "3.4", ChineseEncoding,deviceNo);
             } catch (Exception e) {
                 e.printStackTrace();
             }
      	
      	System.out.println(str2[0]==null);
       	System.out.println(str2[0].equalsIgnoreCase(null));
    	System.out.println(str2[0].equalsIgnoreCase(""));
    	System.out.println(!str2[0].equalsIgnoreCase(""));
             System.out.println(str2[1]);
             System.out.println(str2[0]);
    }
    
    @Test
    public void decodeCommon() {
    	String str26 ="FA F5 0B 80 06 26 01 02 03 04 05 01 00 05 3B";
    	String str97 ="01 02 03 04 00 00 00 00 01 02 03 04 01 01 00 00 00 01 e2 07 0a 01 01 00 00 ff 00 00 00 00 00";
   	  
    //	String str98="00 00 00 00 00 00 00 00 02 02 03 02 12 01 00 00 00 01 e2 07 0a 01 00 02 02 08 07 06 05 04 03 02 01 01 00 01 00 01 00 01 00 01 00 01 01 01 02 01 00 02 00 02 00 02 00 01 01 00 00 00 00 00 00 00 00 00 00 00 00 03 00 03 00 03 00 03 00 03 00 03 02 02 00 04 00 04 00 04 00 04 00 04 02 03 00 04 00 00 00 10 00 10 00 10 08 08 08 08 08 08 08 08 07 07 07 07 61 61 61 61 61 61 61 61 61 61 61 61 61 61 61 61 61 01 08 07 06 05 04 03 02 01";
    	
    	String str17="00 00 00 00 01 01 02 01 02 01 02 01 02 00 01 00 01 02 01 02 01 02 01 02 00 00 01 01 01 01 00 01";
    	String[] str2 = null;
    	String str31="FA F5 1F 80 EF 31 01 02 03 04 02 07 10 03 04 01 02 01 03 01 08 00 00 00 00 00 00 00 01 01 01 01 01 01 71";
    	String str7D ="FA F5 C1 35 D4 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 7D 01 02 03 04 05 06 07 08 00 01 02 03 04 05 06 07 08 09 10 11 12 13 14 15 16 17 18 19 00 01 02 03 04 05 06 07 01 02 03 04 05 06 07 08 09 00 00 01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06 01 02 03 04 05 06 07 08 09 00 01 02 03 01 02 03 04 05 06 07 08 09 00 01 02 03 01 02 03 04 05 06 07 08 09 00 01 02 03 01 02 03 04 05 06 07 08 09 00 01 02 03 01 02 03 04 01 02 03 04 05 06 07 08 09 00 01 02 03 04 01 02 03 04 05 06 07 08 09 00 01 02 03 04 05 06 07 08 09 00 E7";
    	//String str70="FA F5 20 80 15 71 00 00 00 00 36 38 30 36 31 33 31 39 30 30 30 30 30 32 36 38 20 D7 07 01 01 00 00 34 FF D6";
    	String str71="FA F5 26 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 71 01 01 01 01 01 01 01 01 01 00 00 00 00 00 00 00 00 00 7A";
    	String str12_35="fa f5 cd 35 00 08 07 06 05 04 03 02 01 DB 07 03 07 0D 0D 0D 2C 01 12 00 00 00 00 04 01 00 00 00 00 00 00 00 01 00 00 01 00 00 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 ff 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 02 03 04 05 06 07 07 06 05 04 03 02 01 53";
    	String str15 ="FA F5 1d 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 15 01 00 02 00 0A 02 01 00 00 25";
    	String str3A ="FA F5 38 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 3A 01 00 02 00 01 B3 01 08 07 06 05 04 03 02 01 01 02 03 04 05 06 06 05 FE 08 03 04 01 02 FA DD FE AC 07 FE 00 cc3A 01 00 02 00 01 B3 01 08 07 06 05 04 03 02 01 01 02 03 04 05 06 06 05 FE 08 03 04 01 02 FA DD FE AC 07 FE 00 cc";
    	
    	String str1E ="FA F5 6B 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 1E 01 00 02 00 02 B3 11 42 11 10 10 24 10 12 12 44 12 23 01 10 22 10 33 10 44 10 45 01 02 00 01 02 03 04 05 06 08 09 02 02 03 04 11 12 23 35 03 22 23 12 20 44 45 10 23 11 12 12 35 03 24 46 23 35 22 48 21 35 03 02 01 09 08 07 06 05 04 03 02 03 02 03 04 05 12 25 32 CA"; 
    	String str1F="FA F5 70 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 1F 01 00 02 00 02 31 32 33 34 35 36 31 32 33 34 35 36 31 32 33 34 35 36 08 07 06 05 04 03 02 01 31 31 33 34 08 02 28 31 32 33 34 05 06 07 08 09 f0 B3 31 32 33 34 35 36 31 32 33 34 35 36 31 32 33 34 35 31 32 33 34 35 36 37 38 39 DC D4 B4 03 12 09 25 01 11 12 13 14 07 16 17 08 F0 D6";
    	String str3C="FA F5 3E 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 3C 01 00 02 00 01 B3 2D 12 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 12 23 12 32 12 20 01 00 03 03 31 32 33 34 35 35 36 37 85";
    	String str98="FA F5 91 80 D0 98 01 02 01 00 01 02 03 04 05 06 07 08 12 01 01 00 00 00 DF 07 04 0E 10 10 26 01 02 03 04 05 06 07 08 01 01 94 11 08 07 BC 0C 3C 09 00 00 00 03 34 34 01 21 0E BC 0C 15 00 01 02 03 DE 07 07 06 58 02 40 0D 01 6A 01 0E 00 72 01 74 0E 08 02 3C 0F 6E 00 08 08 D7 0D 3C 0F FC 0D 1F 0E 64 0F 42 43 1E 00 00 00 00 00 00 00 00 1A 00 16 01 05 FF FF 01 00 00 00 00 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 01 01 19 16 01 05 FF FF FF 06";
    	String str96="FA F5 1E 80 01 96 00 00 00 00 01 02 03 04 05 06 07 09 01 01 01 02 03 04 DF 07 05 12 0D 37 1E FF 01 26";
    	String str7A ="FA F5 77 80 01 7A 00 00 00 00 36 38 30 36 31 33 31 35 30 30 30 30 30 32 36 35 01 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 36 36 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 DF 07 04 11 09 0E 14 FF 01 02 00 00 00 00 00 00 00 00 9A";
    	String str79="FA F5 8A 80 81 79 00 00 00 00 01 36 38 30 36 31 33 31 30 30 30 30 32 32 31 36 31 31 32 30 30 30 30 30 30 30 30 30 30 30 30 35 32 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 32 01 05 15 02 0b 10 22 17 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 35 c1 d4 41 42 31 32 33 34 34 44 12 00 05 09 1D 06 00 00 01 01 00 01 E2 07 0B 0F 09 0A 11 FF E2 07 0B 0F 09 24 16 FF E2 07 0B 0F 09 20 18 FF E8 03 00 00 D0 07 00 00 C1";		
    	String str35 ="fa f5 32 80 00 35 00 00 00 00 01 01 55 01 04 00 02 00 00 00 01 08 00 01 00 00 ff 66 01 00 01 01 00 00 00 02 01 00 00 ff 77 01 10 00 01 00 01 20 00 01 00 01 ff b2";
    	String str3D="fa f5 3B 35 00 00 00 00 00 00 00 00 01 e2 07 01 01 00 00 00 00 00 3D 00 00 00 00 01 02 03 01 00 00 00 00 00 00 00 00 01 00 00 00 01 01 02 00 00 00 04 01 00 01 00 00 00 08 01 00 01 00 ff 58";
    	String str70_gen="FA F5 92 80 8F 70 01 00 02 00 33 31 33 31 33 31 33 31 33 31 33 31 33 31 33 31 01 31 31 31 31 32 32 32 32 33 33 20 20 20 20 20 20 20 20 20 20 01 00 35 35 35 35 35 35 35 35 35 35 20 20 20 20 20 20 20 20 20 20 36 36 36 36 36 36 20 20 20 20 20 20 20 20 20 37 37 37 37 37 37 37 37 37 37 20 20 20 20 20 20 20 20 20 20 E2 07 0B 1C 0B 00 00 FF 31 31 31 31 31 31 20 20 20 20 20 20 20 20 20 20 01 31 2E 31 20 20 20 20 20 20 20 20 20 00 00 00 00 00 00 00 00 14";
    String str79_gen="FA F5 92 80 8F 79 01 00 02 00 01 33 31 33 31 33 31 33 31 33 31 33 31 33 31 33 31 61 31 35 20 20 20 20 20 20 20 20 20 20 20 20 62 32 30 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 00 00 00 00 01 02 03 04 63 61 72 31 37 20 20 20 20 20 20 20 20 20 20 20 20 38 20 20 20 20 20 20 20 01 02 0C 00 0C 00 0C 00 00 00 04 00 00 01 E2 07 0B 1C 0B 00 00 FF E2 07 0B 1C 0B 00 0A FF E2 07 0B 1C 0B 00 08 FF 0D 00 0E 00 00 00 00 00 00 00 00 00 00 00 00 00 AF";
    String str_1B="FA F5 75 35 D4 01 01 01 01 02 10 23 37 DB 07 0C 14 0D 0D 0D 2C 01 1B 01 01 01 01 02 10 23 37 21 22 23 24 31 32 33 34 35 36 41 42 43 44 45 46 47 48 49 4A 01 02 03 00 05 02 00 00 DB 07 0C 14 0D 0D 0D 2C DA 07 0C 14 0D 0D 0D 2D DA 07 0C 14 0D 0D 1D 2D 31 32 33 34 35 36 37 38 39 38 39 3C 3D 3E 3F 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F 50 51 01 02 03 00 01 41";
    //String str70="FA F5 92 80 31 70 00 00 00 00 36 38 30 36 31 33 31 30 30 30 30 32 32 37 38 38 01 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 00 00 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 DF 07 04 11 09 0E 14 FF 4A 79 49 FD FD 06 42 FD 45 FD 37 FD 53 2A FD FD 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 fc";
    String str70="FA F5 A3 35 02 01 08 11 01 0A 10 22 16 E2 07 0B 1D 0E 10 1E 00 25 70 00 00 00 00 36 38 30 36 31 33 31 30 30 30 30 32 32 31 33 37 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 E2 07 0B 1D 0E 10 1E FF 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 F5";
   String str770="FA F5 92 80 31 70 00 00 00 00 36 38 30 36 31 33 31 39 30 30 30 32 34 38 37 37 01 31 32 33 34 35 36 37 38 39 30 31 32 33 34 35 36 37 38 39 30 00 00 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 E3 07 01 11 05 05 38 31 31 31 20 20 20 20 20 20 20 20 20 20 20 20 20 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 1A";
    String str68="FA F5 91 80 8E 68 00 00 00 00 02 76 31 30 30 30 30 31 38 35 30 30 36 38 32 36 38 FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF FF 31 30 3B 30 2E 30 31 3B 32 35 35 20 30 2E 30 30 00 00 00 00 00 00 00 00 30 2E 30 30 00 00 00 00 00 00 00 00 30 2E 30 30 00 00 00 00 00 00 00 00 39 35 31 39 39 36 33 35 34 20 20 20 20 20 20 38 38 38 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 E3 07 01 08 0F 1C 28 FF FB";
    String str779="FA F5 8A 80 39 79 00 00 00 00 02 36 38 30 36 31 33 31 39 30 30 30 32 32 39 31 35 E3 07 00 01 02 01 01 04 02 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 05 15 02 0B 10 22 19 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 FF FF 00 01 00 02 00 00 00 01 01 00 01 51 E3 07 01 15 0D 0D 0D FF E3 07 01 15 0D 0D 0D FF E3 07 01 15 0D 0D 0D FF 00 00 00 01 00 00 00 02 E3";
    
    
    try {
      	 //  String[] str2 = null;
            
      	   //   str2 = DecodeRechargeDataDomainXml.ParseData(hexString2ToBytes(str17), (byte) 0x17, "3.4", ChineseEncoding);
    	DecodedMsgDto	decodecMsgDto = null;
    	decodecMsgDto=   ConfigureDecode.DecodeReportPackageContent(hexString2ToBytes(str779), "0x79", 1, 3, "", "0000000001020304","3.4");
    	str2 =decodecMsgDto.getDecodedMsg();
    	
    	System.out.println(str2[0]);
       System.out.println(str2[1]);
      	 
             } catch (Exception e) {
                 e.printStackTrace();
             }

    }
}
