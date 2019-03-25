package com.cpit.icp.collect.process;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.cpit.common.JsonUtil;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
import com.cpit.icp.collect.controller.MsgReceived;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.msgProcess.ConstructMsgUtil;
//import com.cpit.icp.collect.msgProcess.ContructMsgUtil;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.Encodes;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.common.ResultInfo;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.DEFINED_PORT)
public class MsgTest {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	@Autowired ConstructMsgUtil contructMsgUtil;
	@Test
 public void testMsgReceived() {
	this.restTemplate.postForObject("/msgRecievedPreGate/123456",null, ResultInfo.class);
	//	this.restTemplate.postForObject("/test",null, null);
 }
	
	
	@Test
	public void testReceived() {
	//	String str ={"deviceNo":"0102030405060300","msgCode":"0x79","msgData":"0102030405060300FAF58A808179000000000136383036313331353030303030323635313230303030303030303030303035323030303030303030303030303030303030303201020304050607084C484231335433443743523133303833352020202020202020343400000009F500000001010051DF070411092011FFDF070411092416FFDF070411092418FFB8010000C20100004F","msgVersion":"0x80","preGateIp":"10.3.92.40","preGatePort":"25000"};
	//	String str ="FA F5 8A 80 00 79 01 02 01 02 01 06 06 06 06 07 07 07 07 08 08 08 08 09 09 09 09 50 48 49 56 48 57 49 48 49 54 51 57 48 48 48 50 48 49 56 48 57 49 48 49 54 52 48 48 48 48 48 48 48 48 49 01 02 03 04 05 06 07 08 48 48 48 49 50 51 52 53 54 55 49 50 51 52 53 54 55 00 01 02 03 04 05 06 07 01 01 01 02 01 02 00 00 00 01 01 01 02 01 E2 07 09 0a 0a 0a 0a ff E2 07 09 0a 0a 0a 0a ff E2 07 09 0a 0a 0a 0a ff 00 00 00 01 00 00 00 04 41";
		//String str="FA F5 23 80 00 34 01 02 01 02 01 01 01 00 00 00 01 00 00 00 02 00 00 00 03 00 00 00 04 00 01 00 02 00 03 00 04 01 52";
		String str ="FA F5 8A 80 81 79 00 00 00 00 01 36 38 30 36 31 33 31 35 30 30 30 30 30 32 36 35 31 32 30 30 30 30 30 30 30 30 30 30 30 30 35 32 30 31 38 30 31 30 31 31 32 32 33 34 35 30 30 30 30 30 32 01 02 03 04 05 06 07 08 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 35 20 20 20 20 20 20 20 20 34 34 00 00 00 09 F5 00 00 00 01 01 00 51 DF 07 04 11 09 20 11 FF DF 07 04 11 09 24 16 FF DF 07 04 11 09 24 18 FF B8 01 00 00 C2 01 00 00 6C";
		String[] ssplit=str.split(" ");
		StringBuilder sb = new StringBuilder();
	for(String s:ssplit) {
		sb.append(s);
	}
	System.out.println(sb);
		Map m = new HashMap();
		m.put("deviceNo", "0102030405060708");
		m.put("msgCode", "0x79");
		m.put("msgData", sb);
		m.put("msgVersion", "0x80");
		m.put("preGateIp","10.3.10.169");
		m.put("preGatePort", "26001");
		String param = JSON.toJSONString(m);

		System.out.println("param " +param);
		//this.restTemplate.postForObject("/msgRecievedPreGate/"+param,null, ResultInfo.class);
		//Thread.currentThread().sleep(1000);
		
	}
	
	@Test
	public void testConstruct() {
	byte[] b=	contructMsgUtil.genUserIdSerialNo(2147483647).get(0);
	
	int s=	CommFunction.byteArrayToInt(b);
	System.out.println(s);
	//System.out.println(CodeTransfer.byteArrayToHexStr());	
	//System.out.println(CodeTransfer.byteArrayToHexStr(contructMsgUtil.genUserIdSerialNo(2147483647).get(1)));	
	}
	
	
	@Test
	public void testT() {
	Map<String,String> map = new HashMap<String,String>();
	map.put("ss", "1");
	String s = map.get("qq");
	System.out.println(s);
	if(s==null)
		System.out.println("11");
	
	}
	
	@Test
	public void testSplitTime() {
		List<Date> list = getDayDate("2018-10-22 23:00:00");
		String strTime = "2018-10-22 02:00:00";
		
		Date d = TimeConvertor.stringTime2Date(strTime, TimeConvertor.FORMAT_MINUS_24HOUR);
	//	Date s = this.getPrevious5Min(d);
	//	this.printDate(s);
	}
	private void printDate(Date d) {
		System.out.println(TimeConvertor.date2String(d, TimeConvertor.FORMAT_MINUS_24HOUR));
	}
	private  int getTimesnight(){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return (int) (cal.getTimeInMillis()/1000);
		}
	
	private List<Date> getDayDate(String time){
		List<Date> list = new ArrayList<Date>();
		
		Date d = TimeConvertor.stringTime2Date(time, TimeConvertor.FORMAT_MINUS_24HOUR);
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		
		int hour =cal.get(Calendar.HOUR_OF_DAY);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		for(int i=0;i<24;i++) {
			cal.set(Calendar.DAY_OF_MONTH,day);
			cal.set(Calendar.HOUR_OF_DAY, hour-1);
			hour--;
			Date newD = cal.getTime();
			list.add(newD);
			//System.out.println(TimeConvertor.date2String(newD, TimeConvertor.FORMAT_MINUS_24HOUR));
		
		}
		
		
		return list;
	}
	

}
