package com.cpit.icp.collect.msgProcess;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.dto.collect.coderDecoder.CheckResultDto;
import com.cpit.icp.dto.collect.coderDecoder.UIDataContentDto;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;

import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.Encodes;
import com.cpit.icp.collect.utils.cache.CacheUtil;
//import com.cpit.testplatform.modules.testsuite.comm.SockMess;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_1;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_2;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import com.cpit.icp.dto.collect.msgProcess.ResponseDataDto;

@Service
public class ConstructData {
	private final static Logger logger = LoggerFactory.getLogger(ConstructData.class);
	@Autowired ConstructMsgUtil constructMsgUtil;
	@Autowired ConstructDatagram constructDatagram;
	@Autowired CacheUtil cacheUtil;
	String version34 = "34";
	String version35 = "35";
	ParseReponseXml parsexml34 = new ParseReponseXml(version34);
	ParseReponseXml parsexml35 = new ParseReponseXml(version35);
	Map<String, List<ResponseDataDto>> myui=new HashMap<String,List<ResponseDataDto>>();
	Map<String, List<ResponseDataDto>> myui34=new HashMap<String,List<ResponseDataDto>>();
	Map<String, List<ResponseDataDto>> myui35=new HashMap<String,List<ResponseDataDto>>();
	/**
	 * 
	 * @param version 设备版本号
	 * @param flag 报文编号
	 * @param equimentCode 设备编码
	 * @param receivedData 设备上报的数据
	 * @return
	 * @throws Exception
	 */
public List<UIDataContentDto> construct(String version,byte flag,String equimentCode,byte[] receivedData,String codeMode)throws Exception
{

	
	if(version.equals(VERSION_3_4))
	{
	//	Document document = cacheUtil.getResponseConfigXml(version);
	//	myui =  parsexml34.ParseXmlFie(document);
	}else if(version.equals(VERSION_3_5))
	{	Document document = cacheUtil.getResponseConfigXml(version);
		myui =  parsexml35.ParseXmlFie(document);
	}else if(version.equals(VERSION_3_1))
	{	
}else if(version.equals(VERSION_3_2))
{	
}
	else
	{
		logger.debug( "codeVersion is wrong!");
		return null;
	}

	switch(flag)
	{
	case 0x62: 
		return constructDatagram.construct62(version,receivedData);
	case 0x65:
		return constructDatagram.construct65(version,receivedData,codeMode);
	case 0x6d:
		return constructDatagram.construct69(version,receivedData,equimentCode);
	case 0x69:
		return constructDatagram.construct69(version,receivedData,equimentCode);	
	case 0x64:
		return constructDatagram.construct64N(version,receivedData);
	case 0x6B:
		return constructDatagram.construct6B(version,receivedData);
	case 0x60:
		return constructDatagram.construct60(version,receivedData,equimentCode);
	case 0x61:
		return constructDatagram.construct61(receivedData);
	}

	
		String mesCode = CodeTransfer.byteToHexStr(flag, true);
		List<ResponseDataDto> list = myui.get(mesCode);
		List<UIDataContentDto> uiList = new ArrayList<UIDataContentDto>();
		for(ResponseDataDto rDto:list)
		{
			UIDataContentDto uiDto = new UIDataContentDto();
			uiDto.setOrder(rDto.getOrder());
			uiDto.setName(rDto.getName());
			String value =null;
		
		if(rDto.getSrc().equals("1")){//从收到的报文中去取数据
			 value=this.byteArray2String(receivedData, Integer.parseInt(rDto.getSrcPos()), Integer.parseInt(rDto.getSize()), rDto.getType(),version);
		
		}else if(rDto.getSrc().equals("2")){//根据size构造随机数
			value = this.getRandom();
			
		}else if(rDto.getSrc().equals("3")){//有对象，需要从对象中取数据
			
		}else if(rDto.getSrc().equals("4")){//充电设备编码
			value = equimentCode;
		}else if(rDto.getSrc().equals("5")){//获得当前时间
			value = this.getCurrentDate();
		}else if(rDto.getSrc().equals("6")){//读取默认值
				value = rDto.getValue();
		}else if(rDto.getSrc().equals("7")) {//serialNo for 3.5
			 value=this.byteArray2String(receivedData, Integer.parseInt(rDto.getSrcPos()), Integer.parseInt(rDto.getSize()), rDto.getType(),codeMode);
		String serialNo = constructMsgUtil.processSerialNo(value);
		logger.info("new serialNo : "+serialNo);
		value = serialNo;
		}
		else{
			value = null;
		}
		uiDto.setValue(value);
		
		uiList.add(uiDto);
		}
		return uiList;

}

public List<UIDataContentDto> construct09(String version,String deviceNo){
	List<UIDataContentDto> list = new ArrayList<UIDataContentDto>();
	String order = "0x09";
	UIDataContentDto userId = new UIDataContentDto();
	userId.setOrder(order);
	userId.setName("用户Id");
	UIDataContentDto serialNo = new UIDataContentDto();
	serialNo.setOrder(order);
	serialNo.setName("指令序号");
	UIDataContentDto chargePoleNum = new UIDataContentDto();
	chargePoleNum.setOrder(order);
	chargePoleNum.setName("充电桩编码");
	chargePoleNum.setValue(deviceNo);
	byte[] bytes =Encodes.decodeHex(deviceNo);
	byte[] b1 = new byte[4];
	byte[] b2 = new byte[4];
	for(int i=0;i<b1.length;i++) {
		b1[i]= bytes[i];
		b2[i]= bytes[i+4];
		
	}
	userId.setValue(CodeTransfer.byteArrayToHexStr(b1));
	serialNo.setValue(CodeTransfer.byteArrayToHexStr(b2));
	list.add(userId);
	list.add(serialNo);
	list.add(chargePoleNum);
	return list;
	
}

public CheckResultDto constructWrongData(String version,MsgOfPregateDto msgDto,String name) throws Exception{

	
	return constructDatagram.constructWrongData(version,msgDto, name);
}

private String byteArray2String(byte[] data, int start, int len, String type,String codeMode) {
	byte[] dataConvert = new byte[len];
	for (int i = 0; i < len; i++) {
		dataConvert[i] = data[start + i];
	}
	String s = null;
	switch (type) {
	case "int":// 用户id，指令序号
		s = MessgeTransfer.bytesTostr(dataConvert);
		break;
	case "byte":// 一个字节 to 整型
		s = Integer.toString(CommFunction.unsignByteToInt(dataConvert[0]));
		break;
	case "hex":// 多个字节to十六进制str
		s = CommFunction.byteArrayToHexStr(dataConvert);
		break;
	case "ascii":
		StringBuilder mysb = new StringBuilder();
		s = mysb.append(CommFunction.bytesToAscii(dataConvert)).toString();
		break;

	case "time": // time
		s = MessgeTransfer.TimebyteTostr(dataConvert);
		break;
	case "carstr":// car no
		s = MessgeTransfer.carCodebyteT0str(dataConvert,codeMode);
		break;
		
	case "timeStamp":
		s = MessgeTransfer.TimeStampbyteTostr2(dataConvert);
	}
	return s;
}

private String getRandom() {
	Random random1 = new Random(100);
	return String.valueOf(random1.nextInt(100));
}
private String getCurrentTime()
{
	Date d = new Date();

	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	
	 byte[] bs = MessgeTransfer.TimeTobytes(sf.format(d));
	 String s =CommFunction.byteArrayToHexStr(bs);
	

	 return s;
	}
private String getCurrentDate()
{
	Date d = new Date();

	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

	return sf.format(d);
	
}

public static void main(String[] args){
	ConstructData cd = new ConstructData();
	cd.getCurrentTime();
	}
}
