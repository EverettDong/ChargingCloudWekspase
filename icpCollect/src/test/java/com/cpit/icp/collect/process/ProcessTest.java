package com.cpit.icp.collect.process;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cpit.common.Encodes;
import com.cpit.common.JsonUtil;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.util.ArraysN;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
import com.cpit.icp.collect.controller.MsgReceived;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.msgProcess.CallOuterInf;
import com.cpit.icp.collect.msgProcess.ConstructMsgUtil;
import com.cpit.icp.collect.msgProcess.MsgDataConvertor;
import com.cpit.icp.collect.msgProcess.MsgMap;
import com.cpit.icp.collect.msgProcess.MsgProcessInterface;
import com.cpit.icp.collect.msgProcess.MsgProcessThread;
import com.cpit.icp.collect.msgProcess.MsgSendThread;
import com.cpit.icp.collect.msgProcess.MsgThreadPool;
import com.cpit.icp.collect.msgProcess.ParseMsgProcessorXml;
import com.cpit.icp.collect.msgProcess.RegisterMsgProImp;
import com.cpit.icp.collect.msgProcess.ReportMsgProImp;
import com.cpit.icp.collect.msgProcess.RequestMsgProImp;

import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.MsgCodeUtil;
import com.cpit.icp.collect.utils.ParseUtil;
import com.cpit.icp.dto.billing.recharge.RechargeBeginResponse;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class ProcessTest {
@Autowired MsgCodeUtil msgCodeUtil;
@Autowired MsgDataConvertor msgDataConvertor;
@Autowired MonDBService monDBService;
@Autowired ParseUtil parseUtil;
@Autowired CallOuterInf callOuterInf;
@Autowired	ConstructMsgUtil constructMsg;


//@Autowired TestService testService;

@Test
public void testRegister() {
	ParseMsgProcessorXml.getInstance();
	RegisterMsgProImp registerImp = new RegisterMsgProImp();
	String str10="FA F5 3F 80 59 10 01 01 01 01 02 10 22 06 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 81";
	String str1B="FA F5 75 35 D4 01 01 01 01 02 10 23 37 DB 07 0C 14 0D 0D 0D 2C 01 1B 01 01 01 01 02 10 23 37 21 22 23 24 31 32 33 34 35 36 41 42 43 44 45 46 47 48 49 4A 01 02 03 00 05 02 00 00 DB 07 0C 14 0D 0D 0D 2C DA 07 0C 14 0D 0D 0D 2D DA 07 0C 14 0D 0D 1D 2D 31 32 33 34 35 36 37 38 39 38 39 3C 3D 3E 3F 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F 50 51 01 02 03 00 02 42";
	
	String str10_gen="FA F5 3F 80 3C 10 68 06 13 19 00 02 99 07 31 32 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 00 00 00 01 02 00 00 00 00 00 00 01 00 00 00 01 E2 07 09 06 12 00 00 FF E2 07 09 06 12 00 00 FF 06";
	
	String str = this.testSplit(str10);
	String deviceNo = "0101010102102206";
	//String msgData ="FAF53F80591001020304050603003120202020202020202020202020202020202035030202013D0000000A00000002000000DF07040A0F000BFFDF07040A0F012CFF5B";
	String preGateIp = "10.3.1.1";
	String preGatePort = "1000";
	String version = "3.4";
	String mesCode = "0x10";
	
	MsgOfPregateDto dto = new MsgOfPregateDto(deviceNo,mesCode,str,version,preGateIp,preGatePort);
try {
	System.out.println(JsonUtil.beanToJson(dto));
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	/*
	for(int i=0;i<10;i++) {
	try {
		Thread.currentThread().sleep(2000);
		registerImp.msgProcess(dto);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	
	
}
MsgOfPregateDto dto1 = new MsgOfPregateDto();
dto1.setDeviceNo("0101010102102206");
dto1.setMsgVersion("3.4");
constructMsg.stopCharging(dto1);
*/
}

	@Test
	public void parseStoreXmlTest() {
		String[] decodedData =new String[2];
		decodedData[0] = new String("a");
		//decodedData[1] = "用户ID:0000:0;指令序号:0000:0;充电枪数量:01:1;充电枪位置充电类型:01:直流充电;充电枪位置0本次累计充电Kwh:0000:0.0kwh;充电枪位置0本次累计充电Ah:0000:0;充电枪位置0本次累计充电时间:0100:1;充电枪位置0充电起始SOC:05:05;充电枪位置0当前SOC:05:05;充电枪位置0电表度数Kwh:B5080000:22.29kwh;";
		//decodedData[1] = "充电枪位置0充电类型:01;充电枪位置0执行机构状态:00;充电枪位置0充电模块ID:0000000000000001;充电枪位置0电池BMS编码:0000000000010101;充电枪位置0充电电压:0001;充电枪位置0充电电流:0001;充电枪位置0输出功率:0001;充电枪位置0充电模块故障代码:0000;充电枪位置0充电机状态:01;充电枪位置1充电类型:02;充电枪位置1执行机构状态:00;充电枪位置1充电模块ID:0000000000000001;充电枪位置1电池BMS编码:0000000000010101;充电枪位置1充电电压:0001;充电枪位置1充电电流:0001;充电枪位置1输出功率:0001;充电枪位置1充电模块故障代码:0000;充电枪位置1充电机状态:01;";
		decodedData[1] ="用户ID:0000;指令序号:0000;充电枪数量:02;充电枪位置0充电类型:01;充电枪位置0本次累计充电Kwh:0000;充电枪位置0本次累计充电Ah:0000;充电枪位置0本次累计充电时间:0100;充电枪位置0充电起始SOC:05;充电枪位置0当前SOC:05;充电枪位置0电表度数Kwh:B5080000;充电枪位置1充电类型:02;充电枪位置1本次累计充电Kwh:2222;充电枪位置1本次累计充电Ah:2222;充电枪位置1本次累计充电时间:2222;充电枪位置1充电起始SOC:22;充电枪位置1当前SOC:22;充电枪位置1电表度数Kwh:B5082222;";
	Object obj=null;
		//	Object obj =msgDataConvertor.strConvertToObj(decodedData, "0x39");
	//	monDBService.storeObjData(obj, "0x39", "0102030405060708");
		System.out.println(obj.toString());
	}
	
	@Test
	public void dtoTest() {
		MonRechargeRecordDto dto = new MonRechargeRecordDto();
		  Class clazz = dto.getClass();

		    Field[] fields = clazz.getDeclaredFields();
		    for (Field field : fields) {
		        String key = field.getName();
		        PropertyDescriptor descriptor;
				try {
					descriptor = new PropertyDescriptor(key, clazz);
					
					  Method method = descriptor.getReadMethod();
					  Method methodset = descriptor.getWriteMethod();
					  methodset.invoke(dto, "aa");
				        Object value = method.invoke(dto);

				        System.out.println(key + ":" + value);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      

		    }
		  //  System.out.println(dto.toString());
	}
	
	@Test
	public void testParseProcessorXml() {
		//Map<List<String>, MsgProcessInterface> msp=	ParseMsgProcessorXml.getInstance().getVersionMap("3.4");
	ParseMsgProcessorXml.getInstance();
		List<String> list = MsgMap.getInstance().getTsNeedStore();
		//Map<String,String> codePairTs = MsgMap.getInstance().getCodePairTS(VERSION_3_4);
		Map<String,Integer> codeType = MsgMap.getInstance().getCodeType(VERSION_3_4);
		System.out.println(""+list.toString());
		//System.out.println(" "+ codePairTs.toString());
		System.out.println(""+codeType.toString());
		//System.out.println(msp.toString());
		//System.out.println(codePairTs.get("0x70"));
	
	}
	
	@Test
	public void testCodeTransfer() {
String code ="FAF53F80591001020304050603003120202020202020202020202020202020202035030202013D0000000A00000002000000DF07040A0F000BFFDF07040A0F012CFF5B";
		
String code1="FaF53f";	
		
		byte[] mesData=Encodes.decodeHex(code);
		
		System.out.println(CodeTransfer.byteArrayToHexStr(mesData));
	}
	
	
	@Test
	public void testThread() {

		
		String code ="FAF53F80591001020304050603003120202020202020202020202020202020202035030202013D0000000A00000002000000DF07040A0F000BFFDF07040A0F012CFF5B";
		String code79="FAF58A80007901020102010606060607070707080808080909090950484956485749484954515748484850484956485749484954524848484848484848490102030405060708484848495051525354554950515253545500010203040506070101010201020000000101010201E207090a0a0a0affE207090a0a0a0affE207090a0a0a0aff000000010000000441";
		MsgOfPregateDto dto = new MsgOfPregateDto();
		
		//byte[] mesData=Encodes.decodeHex(code);
		
		//System.out.println(CodeTransfer.byteArrayToHexStr(mesData));
		dto.setDeviceNo("0102030405060708");
		dto.setMsgCode("0x10");
		dto.setMsgVersion("3.4");
		dto.setPreGateIp("10.3.10.166");
		dto.setPreGatePort("1122");
		dto.setMsgData(code);
		String jsonStr =JSONObject.toJSONString(dto);
		System.out.println(jsonStr);
		//System.out.println(x);
		//MsgThreadPool.getThreadPool().execute(new MsgProcessThread(dto));
	//	MsgProcessThread thread = new MsgProcessThread(dto);
	//	thread.run();
		//mr.msgRecievedPreGate("test");
	}
	@Test
	public void testJsonParam() {
		
		String code79="FAF58A80007901020102010606060607070707080808080909090950484956485749484954515748484850484956485749484954524848484848484848490102030405060708484848495051525354554950515253545500010203040506070101010201020000000101010201E207090a0a0a0affE207090a0a0a0affE207090a0a0a0aff000000010000000441";
	 	Map m = new HashMap();
			m.put("deviceNo", "0102030405060708");
			m.put("msgCode", "0x79");
			m.put("msgData", code79);
			m.put("msgVersion", "0x80");
			m.put("preGateIp","10.3.10.169");
			m.put("preGatePort", "26001");
			String param = JSON.toJSONString(m);
			
			System.out.println(param);
	}
	@Test
	public void testReport() {
		ParseMsgProcessorXml.getInstance();
		ReportMsgProImp reportImp = new ReportMsgProImp();
		MsgOfPregateDto dto = new MsgOfPregateDto();
		String code34 ="FAF5238048340000000001020201030A00050713020000000000000000D608F10C19000000015D";
	//	String code39="FAF515806B390000000001010100000044003434BA010000A3";
		String code39 ="FAF515802D39000000000101FA0006000C00373C7ED303000E";
		String str31="FA F5 1F 80 EF 31 01 02 03 04 02 07 10 03 04 01 02 01 03 01 20 00 00 00 00 00 00 00 01 01 01 01 01 01 89";
	String str35="fa f5 32 80 00 35 00 00 00 00 01 01 55 01 04 00 02 00 00 00 01 08 00 01 00 00 ff 66 01 00 01 01 00 00 00 02 01 00 00 ff 77 01 10 00 01 00 01 20 00 01 00 01 ff b2";
	String str56="FA F5 1C 35 D4 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 56 00 01 02 00 01 00 00 00 5a";
	String str11="FA F5 29 35 02 01 01 01 01 02 10 23 37 DB 07 0C 14 0D 0D 0D 2C 01 11 01 01 01 01 02 10 23 37 02 80 00 00 00 01 02 03 04 03 03 01 05 19";
	String str57="FA F5 AE 35 D4 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 57 00 01 02 00 11 11 11 00 00 00 10 01 02 00 00 03 04 00 00 02 03 00 00 05 00 DB 07 0C 14 0D 0D 0D FF 01 02 03 04 05 06 07 08 01 01 02 10 50 01 04 11 27 31 32 33 34 3A 41 61 7D 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 28 29 2A 2B 00 21 22 23 01 02 03 3C 8F";
	String str3A ="FAF527802C3A010208000101010000000000000000530EC90D7E0175013736FD000000FDFD003C015E0073";
	String str98="FA F5 91 80 D0 98 01 02 01 00 01 02 03 04 05 06 07 08 12 01 01 00 00 00 DF 07 04 0E 10 10 26 01 02 03 04 05 06 07 08 01 01 94 11 08 07 BC 0C 3C 09 00 00 00 03 34 34 01 21 0E BC 0C 15 00 01 02 03 DE 07 07 06 58 02 40 0D 01 6A 01 0E 00 72 01 74 0E 08 02 3C 0F 6E 00 08 08 D7 0D 3C 0F FC 0D 1F 0E 64 0F 42 43 1E 00 00 00 00 00 00 00 00 1A 00 16 01 05 FF FF 01 00 00 00 00 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 01 01 19 16 01 05 FF FF FF 06";
	String str15 ="FA F5 1d 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 15 01 00 02 00 0A 02 01 00 00 25";
	String str3D="fa f5 3B 35 00 00 00 00 00 00 00 00 01 e2 07 01 01 00 00 00 00 00 3D 00 00 00 00 01 02 03 01 00 00 00 00 00 00 00 00 01 00 00 00 01 01 02 00 00 00 04 01 00 01 00 00 00 08 01 00 01 00 ff 58";
	//String str3D="fa f5 3B 35 00 00 00 00 00 00 00 00 01 e2 07 01 01 00 00 00 00 00 3D 00 00 00 00 01 02 03 01 00 00 00 00 00 00 00 00 01 00 00 00 01 01 02 00 00 00 04 01 00 01 00 00 00 08 01 00 01 00 ff 58";
	String str35_v35="fa f5 43 35 00 00 00 00 00 00 00 00 01 e2 07 01 01 00 00 00 00 00 35 00 00 00 00 01 01 55 01 04 00 02 00 00 00 01 08 00 01 00 00 ff 66 01 00 01 01 00 00 00 02 01 00 00 ff 77 01 10 00 01 00 01 20 00 01 00 01 ff b2";
	String str35_34="FA F5 1B 80 44 35 00 00 00 00 01 02 55 00 00 00 00 FF 66 00 00 00 00 FF 77 00 00 00 00 FF 67";
String str18="fa f5 78 80 00 18 00 00 00 00 02 01 41 42 41 42 31 31 31 31 30 30 30 30 31 31 31 31 32 00 00 00 00 00 00 00 00 03 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 02 00 00 00 00 00 00 00 03 f0 02 43 43 44 44 31 31 31 31 30 30 30 30 31 31 31 31 32 00 00 00 00 00 00 00 00 04 00 00 00 00 00 00 00 04 00 00 00 00 00 00 00 05 00 00 00 00 00 00 00 06 00 00 00 00 00 00 00 07 f0 28";
	String str = testSplit(str3A);
	
	
	dto.setDeviceNo("0000000000000001");
		dto.setMsgCode("0x3A");
		dto.setMsgVersion("3.4");
		dto.setPreGateIp("10.3.10.166");
		dto.setPreGatePort("1122");
		dto.setMsgData(str);
		String jsonStr =JSONObject.toJSONString(dto);
		System.out.println(jsonStr);
		reportImp.msgProcess(dto);
	}
	
	@Test
	public void testRequest() {
		ParseMsgProcessorXml.getInstance();
		RequestMsgProImp requestImp = new RequestMsgProImp();
		MsgOfPregateDto dto = new MsgOfPregateDto();

	String str70="FAF592808F700000018036383036313331303030303232393239013030303030303030303030303030303031313131010231313030303030303030313130303030303030303333333333333333333333333333333434343434353535353533333333333535353535E2070101000000FF31323334353620202020202020202020013130302020202020202020200000000000000000D1";
	//System.out.println(str);
		dto.setDeviceNo("0000000000000001");
		dto.setMsgCode("0x70");
		dto.setMsgVersion("3.4");
		dto.setPreGateIp("10.3.10.166");
		dto.setPreGatePort("1122");
		dto.setMsgData(str70);
	
		
		
	
		MsgOfPregateDto dto1 = new MsgOfPregateDto();

		String str701="FAF592808F700000018036383036313331303030303232393932013030303030303030303030303030303031313131010231313030303030303030313130303030303030303333333333333333333333333333333434343434353535353533333333333535353535E2070101000000FF31323334353620202020202020202020013130302020202020202020200000000000000000D1";
		//System.out.println(str);
			dto1.setDeviceNo("0000000000000001");
			dto1.setMsgCode("0x70");
			dto1.setMsgVersion("3.4");
			dto1.setPreGateIp("10.3.10.166");
			dto1.setPreGatePort("1122");
			dto1.setMsgData(str701);
			
			for(int i=0;i<10;i++) {
				requestImp.msgProcess(dto);
				requestImp.msgProcess(dto1);
			}
			

		
		
		
		
		
	}
	
	
	
	public String testSplit(String str) {
	String[] ssplit=str.split(" ");
		StringBuilder sb = new StringBuilder();
	for(String s:ssplit) {
		sb.append(s);
	}
	System.out.println(sb);
	return sb.toString();
	}
	
		
	// name:hexValue:meaning
	@Test
	public void getFaultDFromParsedMsg() {
String str="用户ID:0000:0;指令序号:0000:0;输入电源状态:01:正常;平均温度:FF:无温度传感器;外部温度:FF:无温度传感器;工作状态:02:服务状态;可充电机模块数量:04:04;输出有功功率:0000:0.0kw;输出无功功率:FFFF:缺省值FF;充电系统总体状态:02:02;充电系统故障状态:01000001:实体：一级-整机系统   二级-绝缘故障; 故障分类:绝缘电阻值小于阀值  ; 故障等级:1级  ;故障定义:I06001;#实体：一级-整机系统	二级-内部过温；故障分类：风扇故障;故障等级:3级;故障定义:I06002;#;AC/DC控制模块故障状态:00000000:无告警#;风扇总状态:FF:未定义;空调总状态:FF:未定义;加热器总状态:FF:未定义;烟雾报警总状态:FF:未定义;震动传感器报警总状态:FF:未定义;充电枪数量:01:01;";	
		String start="充电系统故障状态";
		int startIdx = str.indexOf(start);
		String end = "AC/DC控制模块故障状态";
		int endIdx = str.indexOf(end);
		String newstrs = str.substring(startIdx,endIdx);
		//去掉name和hexValue
		int firstSplit = newstrs.indexOf(":");
		int secondSplit = newstrs.indexOf(":", firstSplit+1);
		//String faultcode = newstr.substring(firstSplit,secondSplit);
		String faultcode = getSpecialStr(newstrs,":",":");
		System.out.println(faultcode);
		
		String meaning = newstrs.substring(secondSplit+1,newstrs.length());
		System.out.println(meaning);
		
		String[] split= meaning.split("#");
		for(String newstr:split) {
			System.out.println(newstr);
			
			
			
		if(newstr.contains("故障等级:")) {
			//int faultLevelP = newstr.indexOf("故障等级");
		//	String faultLevel = newstr.substring(faultLevelP+5,newstr.indexOf(";", faultLevelP));
			String faultLevel = getSpecialStr(newstr,"故障等级:",";");
			System.out.println(faultLevel);;
		}
		if(newstr.contains("故障定义:")) {
			String faultDef = getSpecialStr(newstr,"故障定义:",";");
			System.out.println(faultDef);
		}
		
		}

		
	}
	
	private String getSpecialStr(String srcStr,String posStr,String endStr) {
		int pos = srcStr.indexOf(posStr);
		int posStrLen = posStr.length();
		int end = srcStr.indexOf(endStr,pos+posStrLen);
		return srcStr.substring(pos+posStrLen, end);
	}
	
	@Test
	public void test() {
		Byte statusByte = CommFunction.hexStringToByte("20");
		 byte[] array = new byte[8];  
	        for (int i = 7; i >= 0; i--) {  
	            array[i] = (byte)(statusByte & 1);  
	            statusByte = (byte) (statusByte >> 1);  
	        } 
	        if(array[0] ==0 && array[2]==0)
		      {
		    	System.out.println("free");
		      }
		      if(array[0] ==0 && array[2]==1) {
		    	  System.out.println("charging");
		      }
	}
	@Test
	public void testGetProcessData3C() {
		String parsedMsgData="充电枪数量:02:2;充电枪位置0放电类型:02:充电枪枪号 :0,充/放电类型:交流充电;充电枪位置0执行机构状态:01:编码:充电连接器温度正常,充电枪推入不到位;充电枪位置0温度检测:04:04;充电枪位置0充/放电模块ID:A8C192E242739230:A8C192E242739230;充电枪位置0电池BMS编码:0834C5AC1C8B1A83:0834C5AC1C8B1A83;充电枪位置0充/放电电压:7E15:550.2V;充电枪位置0充/放电电流:18C8:4802.4A;充电枪位置0输出功率:480F:391.2kw;充电枪位置0充/放电模块故障代码:7F00:实体：一级-模块	二级-变压器模块,故障分类：变压器过热;故障等级:1级;故障定义:I05004;#实体：一级-模块	二级-IGBT模块,故障分类：IGBT驱动欠压;故障等级:1级;故障定义:I05003;#实体：一级-模块	二级-IGBT模块,故障分类：IGBT驱动过流;故障等级:1级;故障定义:I05003;#实体：一级-模块	二级-DC/DC模块,故障分类：DC/DC模块控制电源异常;故障等级:1级;故障定义:I05002;#实体：一级-模块	二级-PFC模块,故障分类：PFC模块控制电源异常;故障等级:1级;故障定义:I05001;#实体：一级-模块	二级-PFC模块,故障分类,PFC模块母线欠压;故障等级:1级;故障定义:I05001;#实体：一级-模块	二级-PFC模块,故障分类：PFC模块母线过压;故障等级:1级;故障定义:I05001;#;";
		String gunPos="充电枪位置";
		String gunAmount="充电枪数量:";
		String gunTeamStr = parseUtil.getTeamStr(gunAmount, parsedMsgData);
		String gunAmountS = parseUtil.getMeaningStr(gunAmount, gunTeamStr);
		int gunAmt=0;
		if(!gunAmountS.equalsIgnoreCase("")) {
			 gunAmt = Integer.parseInt(gunAmountS);
		}
		for(int i=0;i<gunAmt;i++) {
			String nameV = gunPos+i+"充/放电电压:";
			String nameElec = gunPos+i+ "充/放电电流:";
			String vTeamStr = parseUtil.getTeamStr(nameV, parsedMsgData);
			String ElecTeamStr = parseUtil.getTeamStr(nameElec, parsedMsgData);
			String cp_currentElec = parseUtil.getMeaningStr(nameElec, ElecTeamStr);
			String cp_voltage = parseUtil.getMeaningStr(nameV, vTeamStr);
		
			String key="0102030405060708"+i;
			this.printToCosole(key);
			this.printToCosole(cp_currentElec);
			this.printToCosole(cp_voltage);
			
		}
	}
	@Test
	public void testGetProcessData34() {
		String str ="充电枪位置0充电电压:0001:25.6V;";
		String strC="充电枪位置0充电电流:0002:-268.8A;";
		int index = str.indexOf(":", str.indexOf("充电电压:")+5);
		String nameV="充电电压:";
		String nameC="充电电流:";
		String substr = this.getMeaningStr(nameV, str);
		String substrC = this.getMeaningStr(nameC, strC);
		
		
		String allStr =str+strC;
		int i = allStr.indexOf(nameV);
		//String str1 =allStr.substring(allStr.indexOf(nameV), allStr.indexOf(";", allStr.indexOf(nameV)))+";";
		//String str2 =allStr.substring(allStr.indexOf(nameC), allStr.indexOf(";", allStr.indexOf(nameC)))+";";
		this.printToCosole(this.getTeamStr( nameV,allStr));
		this.printToCosole(this.getTeamStr( nameC,allStr));
		
		this.printToCosole(this.getSpecialStr(allStr, nameV, ";"));
		this.printToCosole(this.getSpecialStr(allStr, nameC, ";"));
	}

	
	
	private void printToCosole(String s) {
		System.out.println(s);
	}

	/**
	 * name:hexvalue:meaning; 根据name返回meaning字段
	 * @param nameStr
	 * @return
	 */
	public String getMeaningStr(String nameStr,String srcStr) {
		
		int index = srcStr.indexOf(":", srcStr.indexOf(nameStr)+nameStr.length());
		int end = srcStr.indexOf(";");
		return srcStr.substring(index+1, end);
		
	}
	/**
	 * 从decodedData中截取一个属性的字段 返回值为 name:hexValue:meaning;
	 * @param nameStr
	 * @param srcStr
	 * @return
	 */
	private String getTeamStr(String nameStr,String srcStr) {
		int begin = srcStr.indexOf(nameStr);
		int end = srcStr.indexOf(";", srcStr.indexOf(nameStr));
		String teamStr= srcStr.substring(begin,end)+";";
		return teamStr;
	}
	
	@Test
	public void testTimeConvert() {
		Long startTime=TimeConvertor.getCurrentTime();
long newTime = startTime-1800*1000;
		
		String cp_startTime = TimeConvertor.longtime2String(newTime, TimeConvertor.FORMAT_MINUS_24HOUR);
		this.printToCosole(TimeConvertor.getDate( TimeConvertor.FORMAT_MINUS_24HOUR));
		this.printToCosole(cp_startTime);
//	this.printToCosole(""+TimeConvertor.timeGap(startTime, newTime));
	}

	@Test
	public void TestgenMsgNum() {
	//	logger.debug("genMsgNum"+msgNum);
		int msgNum = 4;
		if(msgNum>65535) {
			msgNum=1;
		}
		StringBuilder sb = new StringBuilder();
		String hexNum = Integer.toHexString(msgNum);
		
		String hexStr = CommFunction.padLeft(hexNum, 4, "0");
		msgNum++;
		this.printToCosole(hexStr);
	
	}
	
	@Test
	public void testGenSequenceNum() {
		this.printToCosole(genMsgSequenceNum(1));
		this.printToCosole(genMsgSequenceNum(12));
		this.printToCosole(genMsgSequenceNum(352));
		this.printToCosole(genMsgSequenceNum(4095));
		this.printToCosole(genMsgSequenceNum(4096));
	}
	private String genMsgSequenceNum(int msgSequenceNum) {
		
			
			if(msgSequenceNum>4095) {
				msgSequenceNum=1;
			}
			StringBuilder sb = new StringBuilder();
			String hexNum = Integer.toHexString(msgSequenceNum);
			
			String hexStr = CommFunction.padLeft(hexNum, 4, "0");
		
			return hexStr;
		}
	
	
	@Test
	public void testSeqence() {
		CpSequenceDto cpSeq = new CpSequenceDto();
		this.printToCosole("" +cpSeq.getFlowNum());
		this.printToCosole(""+cpSeq.getSequenceNum());
		this.printToCosole(cpSeq.toString());
	}
	
	@Test
	public void testQuery5Record() {
		List<MonRechargeRecordDto> list = monDBService.queryRecent5Record("6801222233334444");
		for(MonRechargeRecordDto dto:list) {
			this.printToCosole(dto.toString());
		}
	}
	
	@Test
	public void testCompareDate() {
		Date d1 = TimeConvertor.stringTime2Date("2018-10-24 09:00:00", TimeConvertor.FORMAT_MINUS_24HOUR);
		Date d2 = TimeConvertor.stringTime2Date("2018-10-24 10:00:00", TimeConvertor.FORMAT_MINUS_24HOUR);
		
		System.out.println(d1.compareTo(d2));
		System.out.println(d2.compareTo(d1));
	}
	
	@Test
	public void testBoundary() {
		boundary(3,4);//已经是1分钟粒度
		boundary(3,2);
		boundary(3,5);
		 boundary(0,3);
		 boundary(0,5);
		 
		
		
	}
	private void boundary(int count,int grade) {
		if(count >2 && grade <4) {
			System.out.println("split");
			
		}else {
			System.out.println("do not split");
		}
	}
	@Test
	public void testNull() {
		String[] str = new String[2];
	
		System.out.println(str.length);
	System.out.println(str[0]=="");
	System.out.println(str[0]==null);
	
		System.out.println(str==null);
	}

	
	@Test
	public void testNullMap() {
		Map<String,String> testMap=new HashMap<String,String>();
		testMap.put("01", "11");
		String s = testMap.get("02");
		if(s.equals(null)) {
			System.out.println("is null");
		}
	}
	
	@Test
	public void testsplit79() {
		String s="name:hexValue:meaning:data:other";
		int index = s.indexOf(":");
		int second = s.indexOf(":", index+1);
		String name = s.substring(0,index);
String hex = s.substring(index+1,second);
String meanging = s.substring(second+1,s.length());
System.out.println(name);
System.out.println(hex);
System.out.println(meanging);



		
	}
	
	@Test
	public void testSleep() {
		System.out.println(new Date());
		try {
			Thread.currentThread().sleep(5000);
			System.out.println("****"+new Date());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testPerf() {
testRequest();
		try {
			Thread.currentThread().sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testRequest();
	}
	
	@Test
	public void testOuterInf() {
		String card = "6810131000000180";
		RechargeBeginResponse response = callOuterInf.getAccountInfo(card);
		System.out.println(response.toString());
	}
	
}
