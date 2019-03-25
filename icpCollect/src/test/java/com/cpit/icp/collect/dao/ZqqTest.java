package com.cpit.icp.collect.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.common.JsonUtil;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.msgProcess.ConstructDatagram;
import com.cpit.icp.collect.utils.Exceptions;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.crm.User;
@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class ZqqTest {
	@Autowired ConstructDatagram  constructMsg;
	
	
	public void testi() {
		int j=0;
		j = testj(j);
		System.out.println("  "+j);
	}
	public int  testj(int j) {
		j++;
		return j;
	}
	public void test() {
		long inTime = System.currentTimeMillis();
	
		System.out.println(inTime);


			while ((System.currentTimeMillis() - inTime)<10000) {
				int interval = (int) (System.currentTimeMillis() - inTime);
				if((interval == 2000)
						){
					System.out.println(System.currentTimeMillis()+"  "+inTime);
				}
				else{
					//System.out.println(new Date());
				}
			}
		

	}
	
	public void test1() throws Exception {
		long inTime = System.currentTimeMillis();
	
		System.out.println(inTime);


			while ((System.currentTimeMillis() - inTime)<10000) {
			Thread.sleep(2000);
			System.out.println(System.currentTimeMillis() +" "+inTime);
			int interval = (int)(System.currentTimeMillis() - inTime);
			if(interval == 4000){
				break;
			}
			}
		

	}
	
	public void test2() {
	
	}
	public  String byteArrayToHexStr(byte[] data) {
		 StringBuilder result = new StringBuilder();
		 char[] cHEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
					'9', 'A', 'B', 'C', 'D', 'E', 'F' };
		result.setLength(0);
		for (int i = 0; i < data.length; i++) {
			byte tmp = (byte) (data[i] & 0xFF);
			char hi = cHEX[((tmp & 0xF0) >>> 4)];
			char low = cHEX[(tmp & 0x0F)];

			result.append(hi);
			result.append(low);
		}
		return result.toString();
	}
	public  byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw Exceptions.unchecked(e);
		}
	}

	@Test
	public void testMap() {
		Map<String,String> testMap = new HashMap<String,String>();
		testMap.put("dd", "111");
		System.out.println(testMap.get("dd"));
		testMap.put("dd", "222");
		System.out.println(testMap.get("dd"));
	}
	
	@Test
	public void testParam() {
		MyObj o = new MyObj(false);
		int i=setParam(o);
		System.out.println(o.b+"****"+i);
	}
	
	class MyObj {
		boolean b = false;
		public MyObj(boolean b) {
			this.b = b;
		}
	}
	
	private   int setParam(MyObj b) {
		b.b = true;
		return 0;
	}
	

	
	@Test
	public void testObjIsFull() {
		String jsonStr = getJsonString();
		MsgOfPregateDto dto;
		try {
			dto = (MsgOfPregateDto) JsonUtil.jsonToBean(jsonStr, MsgOfPregateDto.class);
			String deviceNo = dto.getDeviceNo();
			if(deviceNo==null) {
				System.out.println("##");
			}
			if(deviceNo.equalsIgnoreCase("")) {
				System.out.println("**");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	private String getJsonString() {
		MsgOfPregateDto dto = new MsgOfPregateDto();
		dto.setDeviceNo("");
		dto.setMsgCode("");
		dto.setMsgData("");
		dto.setMsgVersion("");
		dto.setPreGateIp("");
		dto.setPreGatePort("");
		try {
			return JsonUtil.beanToJson(dto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	@Test
	public void testNull() {
		String s = new String() ;
		if(s==null) {
			System.out.println("null");
		}
		if(s.equals(null)) {
			System.out.println("equal null");
		}
		if(s.equalsIgnoreCase("")) {
			System.out.println("equal * ");
		}
		
	}
}
