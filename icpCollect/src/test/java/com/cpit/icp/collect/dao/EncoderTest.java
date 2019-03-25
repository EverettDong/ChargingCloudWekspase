package com.cpit.icp.collect.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.common.Encodes;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureEncode;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.main.Application;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class EncoderTest {
	ConfigureEncode configEncode=new ConfigureEncode();
	String deviceCode = "0102030405060708";
	String ChineseEncoding = "GB2312";
	
	@Test
	public void encodePackageContent07() throws IOException {
		List<String> dataContent = new ArrayList<String>();	
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add("12");
		dataContent.add("1");
		String commandId = "0x07";
		byte[] list = configEncode.ParseData(dataContent, commandId,"3.4",deviceCode,ChineseEncoding);
		System.out.println(commandId+"::"+CommFunction.byteArrayToHexStr2(list));
	}
	@Test
	public void encodePackageContent05() throws IOException {
		List<String> dataContent = new ArrayList<String>();	
		
		dataContent.add("65535");
		dataContent.add("65534");
		dataContent.add("3");
		dataContent.add("4");
		dataContent.add("5");
		dataContent.add("6");
		
		String commandId = "0x05";
		byte[] list = configEncode.ParseData(dataContent, commandId,"3.4",deviceCode,ChineseEncoding);
		System.out.println(commandId+"::"+CommFunction.byteArrayToHexStr2(list));
	}
	
	@Test
	public void encodePackageContentE0() throws IOException {
		List<String> dataContent = new ArrayList<String>();	
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add("2");
		dataContent.add("3");
		String commandId = "0xE0";
		byte[] list = configEncode.ParseData(dataContent, commandId,"3.4",deviceCode,ChineseEncoding);
		System.out.println(commandId+"::"+CommFunction.byteArrayToHexStr2(list));
	}
	
	@Test
	public void encodePackageContent69() throws IOException {
		List<String> dataContent = new ArrayList<String>();
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add(getPutInRandomHex(16));//卡号 16位字符串
		dataContent.add("王大锤"); //客户姓名
		dataContent.add("101256"); //响应吗6位
		dataContent.add("飒飒的是否撒地方");//响应吗描述
		dataContent.add("LSJE12835CS110420,京B1234,C1B1");//客户号
		dataContent.add("100.00");
		dataContent.add("100.00");
		dataContent.add("90.00");
		dataContent.add("90.00");
		dataContent.add("5b574aa07590ad2"); //中心交易流水号 15位
		dataContent.add("35496878994664166446");// BOSS出单机构流水号20位
		dataContent.add("120");//本次允许充电电量
		dataContent.add("120");//本次允许充电时间
		dataContent.add("2015-08-13 12:12:12"); //交易日期时间
		dataContent.add(getPutInRandomHex(8));//报文认证码	
		String commandId = "0x69";
		byte[] list = configEncode.ParseData(dataContent, commandId, "3.4",
				deviceCode, ChineseEncoding);
		System.out.println("3.5:" + "0x69::" + CommFunction.byteArrayToHexStr2(list));
	}
	
	@Test
	public void encodePackageContent61() throws IOException {
		List<String> dataContent = new ArrayList<String>();
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add(getPutInRandomHex(16));
		dataContent.add("0x01");
		dataContent.add("2015-01-01 01:01:01");
		dataContent.add(getPutInRandomHex(15));
		dataContent.add(getPutInRandomHex(20));
		String commandId = "0x61";
		byte[] list = configEncode.ParseData(dataContent, commandId, "3.5",
				deviceCode, ChineseEncoding);
		System.out.println(
				"3.5:" + "0x61::" + CommFunction.byteArrayToHexStr2(list));
	}
	
	@Test
	public void encodePackageContent62() throws IOException {
		List<String> dataContent = new ArrayList<String>();
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add("3"); //模块个数
		dataContent.add("1");  //充电枪位置1充/放电类型
		dataContent.add("2");  //电池个数
		dataContent.add("1"); //电池箱编码
		dataContent.add("2"); //电池类型
		dataContent.add(getPutInRandomHex(8));  //电池箱编码
		dataContent.add("4"); //电池类型
		dataContent.add("2015-08-13 12:12:12"); //交易日期时间
		String commandId = "0x62";
		byte[] list = configEncode.ParseData(dataContent, commandId, "3.5",
				deviceCode, ChineseEncoding);
		System.out.println(
				"3.5:" + "0x62::" + CommFunction.byteArrayToHexStr2(list));
	}
	
	@Test
	public void encodePackageContent6C() throws IOException {
		List<String> dataContent = new ArrayList<String>();	
		dataContent.add("32767");
		dataContent.add("65535");
		dataContent.add("122222222333331");
		dataContent.add("ab0f9e2b");
		//dataContent.add("aaa,bbb,ccc");
		dataContent.add("lsje12835cs110420,京B1234,CIBI");
		dataContent.add("100.11");
		dataContent.add("100.11");
		dataContent.add("90.11");
		dataContent.add("90.11");
		dataContent.add("22222222333331");
		dataContent.add("35496878994664166446");
		dataContent.add("2015-08-10 10:12:13");
		dataContent.add("0x15");
		dataContent.add("20");
		dataContent.add("255");
		dataContent.add("11111111");
		dataContent.add("300f9e2b");
		String commandId = "0x6C";
		byte[] list = configEncode.ParseData(dataContent, commandId,"3.4",deviceCode,ChineseEncoding);
		System.out.println(commandId+"::"+CommFunction.byteArrayToHexStr2(list));
	}
	@Test
	public void encodePackageContent64() throws IOException {
		List<String> dataContent = new ArrayList<String>();
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add("3");  //充电枪位置1充/放电类型
		dataContent.add(getPutInRandomHex(16));//卡号 16位字符串
		dataContent.add("2"); //记录条数
		dataContent.add(getPutInRandomHex(8));//电池BMS编码
		dataContent.add("1"); //开始充/放电SOC
		dataContent.add("2"); //结束充/放电SOC
		dataContent.add("1"); //本次累计充/放电量  安时
		dataContent.add("2"); //本次累计充/放电能   千万时
		dataContent.add("2"); //充/放电时间长度 秒
		dataContent.add("1"); //充满策略
		dataContent.add("1"); //充满策略参数
		dataContent.add("1"); //是否正常结束
		dataContent.add("2015-08-13 12:12:12"); //开始日期时间
		dataContent.add("2015-08-13 12:12:12"); //结束日期时间
		dataContent.add("2015-08-13 12:12:12"); //交易开始日期时间

		dataContent.add(getPutInRandomHex(8));//电池BMS编码
		dataContent.add("1"); //开始充/放电SOC
		dataContent.add("2"); //结束充/放电SOC
		dataContent.add("1"); //本次累计充/放电量  安时
		dataContent.add("2"); //本次累计充/放电能   千万时
		dataContent.add("2"); //充/放电时间长度 秒
		dataContent.add("1"); //充满策略
		dataContent.add("1"); //充满策略参数
		dataContent.add("1"); //是否正常结束
		dataContent.add("2015-08-13 12:12:12"); //开始日期时间
		dataContent.add("2015-08-13 12:12:12"); //结束日期时间
		dataContent.add("2015-08-13 12:12:12"); //交易开始日期时间
		String commandId = "0x64";
		byte[] list = configEncode.ParseData(dataContent, commandId, "3.5",
				deviceCode, ChineseEncoding);
		System.out.println(
				"3.5:" + "0x64::" + CommFunction.byteArrayToHexStr2(list));
	}

	//0x65 车牌认证查询指令应答	包含//充电枪位置1充/放电类型 
	@Test
	public void encodePackageContent65() throws IOException {
		List<String> dataContent = new ArrayList<String>();
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add("2"); //充电枪位置1充电类型
		dataContent.add("粤BA1234"); //车辆车牌编码
		dataContent.add(""); //车辆VIN
		dataContent.add("2015-08-13 12:12:12"); //交易日期时间
		String commandId = "0x65";
		byte[] list = configEncode.ParseData(dataContent, commandId, "3.5",
				deviceCode, ChineseEncoding);
		System.out.println(
				"3.5:" + "0x65::" + CommFunction.byteArrayToHexStr2(list));
	}
	@Test
	public void encodePackageContent68() throws IOException {
		List<String> dataContent = new ArrayList<String>();
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add("3");  //充电枪位置1充/放电类型
		dataContent.add(getPutInRandomHex(16));//卡号 16位字符串
		dataContent.add("LSJE12835CS110420,京B1234,C1B1");//客户号
		dataContent.add("10;1.2;56");//本次充/放电结算时间和电量
		dataContent.add("1.03");//本次充/放电结算电费
		dataContent.add("1.23");//本次充/放电服务费
		dataContent.add("2.06");//本次充/放电结算总金额
		dataContent.add("5b574aa07590ad2"); //中心交易流水号 15位
		dataContent.add("35496878994664166446");// BOSS出单机构流水号20位
		dataContent.add("2015-08-13 12:12:12"); //交易日期时间

		String commandId = "0x68";
		byte[] list = configEncode.ParseData(dataContent, commandId, "3.5",
				deviceCode, ChineseEncoding);
		System.out.println(
				"3.5:" + "0x68::" + CommFunction.byteArrayToHexStr2(list));
	}
	 
	public String getPutInRandomHex(Integer num) {
		StringBuffer sBuffer = new StringBuffer();
		for (int i = 0; i < num; i++) {

			sBuffer.append(Integer.toHexString(new Random().nextInt(16)));
		}
		return sBuffer.toString();
	}
	
	@Test
	public void encodePackageContent10() throws IOException {
		List<String> dataContent = new ArrayList<String>();
		dataContent.add("6806131900029907");
		dataContent.add("12");
		dataContent.add("1");
		dataContent.add("2");
		dataContent.add("1");
		dataContent.add("1");
		dataContent.add("2018-9-6 18:00:00");
		dataContent.add("2018-9-6 18:00:00");
		

		String commandId = "0x10";
		byte[] list = configEncode.ParseData(dataContent, commandId, "3.4",
				deviceCode, ChineseEncoding);
		//System.out.println(list.toString());
		System.out.println(
				"3.4:" + "0x10::" + CommFunction.byteArrayToHexStr2(list));
	}
	


}
