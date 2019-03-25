package com.cpit.icp.collect.coderDecoder.messageParse.configurable;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.dto.collect.MonSequenceDto;
import com.cpit.icp.dto.collect.coderDecoder.CheckResultDto;
import com.cpit.icp.dto.collect.coderDecoder.PackageDto;
import com.cpit.icp.dto.collect.coderDecoder.UIDataContentDto;
import com.cpit.icp.collect.coderDecoder.common.configurable._type;
import com.cpit.icp.collect.coderDecoder.util.ArraysN;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
import com.cpit.icp.collect.coderDecoder.util.configurable.Decode0x3DDataContent;
import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.utils.cache.CacheUtil;

/**
 * @author liangzhiyuan 可配置编码
 * @update 2017-01-09
 */

@Service
public class ConfigureEncode {

	private final static Logger logger = LoggerFactory.getLogger(ConfigureEncode.class);
		// 需要从中心下发的报文主要有：01 05 07 08 09 21 22 23 24 25 26 40 41 42 43 46 48 49 61 62
		// 64 65 68 69 80 86 87 88 C1 C2 E0 E1 E2
		// "01,05 07 08 09,21 22 23 24 25 26,40 41 42 43 46 48 49";
	
		private List<PackageDto> lencodePackage = null;
		//@Autowired
	//	private TestResultService systemService;
@Autowired private CacheUtil cacheUtil;
		@Autowired 
		private MonDBService monDBService;
		public ConfigureEncode() {
			this.lencodePackage = new ArrayList<PackageDto>();
		}
		
		//报文编码(外部接口)
		public List<byte[]> encodePackageContent(List<UIDataContentDto> listui,String devicename,String version,String deviceCode,String ChineseEncoding){
			logger.info( "start in encodePackageContent: ");
			if(version==null||version.length()==0){
				version = "3.4";
			}
			if(ChineseEncoding==null||ChineseEncoding.length()==0){
				ChineseEncoding = "GB2312";
			}
			Map<String,List<String>> map=new HashMap<String,List<String>>();
			List<byte[]> ll=new ArrayList<byte[]>();
			
			for (int i = 0; i < listui.size(); i++) {
				List<String> lst =new ArrayList<>();
				String key=listui.get(i).getOrder();
				String value=listui.get(i).getValue();
				if(map.containsKey(key)){
					map.get(key).add(value);
				}else{
					lst.add(value);
					map.put(key,lst);
				}
			}
			for(Map.Entry<String,List<String>> entry:map.entrySet()){    
			    
				 List<byte[]> lis =encodePackageContent(entry.getValue(), devicename, entry.getKey(),version,deviceCode,ChineseEncoding);
				 
				 if(lis!=null&&lis.size()!=0){
					 ll.add(lis.get(0));
				 }
			}  
			return ll;
		}
		
		//0x09报文编码(外部接口)
		public byte[] encode09PackageContent(String deviceCode, String devicename,String version,MonSequenceDto sequenceDto) {
			
			byte[] bytes09 = parse09(deviceCode,devicename,version,sequenceDto);
			//写日志：
			StringBuilder sf = new StringBuilder();
			sf.append("send register reply "+devicename + " ");
			sf.append(CommFunction.LogByteArray(bytes09));
			logger.info(sf.toString());
			//报文入库：
		//	insertMessage(devicename,"0x09",bytes09);
			return bytes09;
		}
		
		// 0x48报文编码(外部接口)
		public byte[] encode48PackageContent(byte[] receive,String deviceCode, String version) {
			byte[] bytes = parse48(receive,deviceCode,version);
			//写日志：
			StringBuilder sf = new StringBuilder();
			sf.append("检测中心下发报文" + "  ");
			sf.append(CommFunction.LogByteArray(bytes));
			logger.info(sf.toString());
			return bytes;
		}
		
		
		//报文编码+报文入库
		private List<byte[]> encodePackageContent(List<String> dataContent,String devicename,String commandId,String version,String deviceCode,String ChineseEncoding) {
			
			logger.info( "start in encodePackageContent: "+commandId);
			List<byte[]> lst = new ArrayList<byte[]>();
			if(dataContent == null || dataContent.isEmpty())
				return lst;
			try {
				byte[] bSenddata=ParseData(dataContent, commandId,version,deviceCode,ChineseEncoding);
			//	insertMessage(devicename,commandId,bSenddata);
				if(bSenddata==null){
					return lst;
				}
				lst.add(bSenddata);						
			} catch (Exception e) {
				logger.error("error in encodePackageContent", e);
			
				return lst;
			}
			return lst;
		}		
		//报文编码可配置框架（供uTest调用）
		public  byte[] ParseData(List<String> dataContent, String commandId, String version,String deviceCode,String ChineseEncoding) throws IOException {
			try {
				SAXReader sax = new SAXReader();
				Document document = null;
				document = cacheUtil.getParseMsgXml(version);
				if(document==null) {
					logger.error("get ParseMsgXml is null.");
					
					return new byte[0];
				}
			//	document = sax.read(ConfigureEncode.class.getResourceAsStream("/conf/configurable/charging_"+version+"/designcharge.xml"));
				String expectedId = "";
				Element orderEle = null;//protocol节点
				Element firstEle = null;//item节点
				Element secondEle = null;//array下的item节点
				int index=0;//记录节点的位置
				int arrayIndex=0;//记录array节点的位置
				int bytelenght=0;//记录byte数组的长度
				int arrayItemIndex=0;//记录byte数组的长度
				List<byte[]> lst=new ArrayList<byte[]>();//存放编码后生成的byte数组集合
				_type type = new _type();//反射类 用于处理item节点的type属性，即根据type的value调用_type类中的相应方法
				Class clazz = type.getClass(); 
				Element rootEle = document.getRootElement();
				for (Iterator iter = rootEle.elementIterator(); iter.hasNext();) {
					// protocol
					orderEle = (Element) iter.next();
					expectedId = orderEle.attributeValue("id");
					if ((commandId).equals(expectedId)){
							break;
					}else{
						expectedId="";
					}
				}
				// 找到对应的xml段
				if (!"".equals(expectedId)) {
					for (Iterator iter1 = orderEle.elementIterator(); iter1.hasNext();) {
						// item
						firstEle = (Element) iter1.next();
						if(firstEle.getName().equals("item")||firstEle.getName().equals("enumitem")){
							
							int byteSize=Integer.parseInt(firstEle.attributeValue("size"));
					        Method m1 = null;
					        if(expectedId.equals("0x0A") && index == 3){ //for 0x0A
					        	int paramIndex = Integer.valueOf(dataContent.get(2));
					        	String paramType = "GB";
					        	if( paramIndex == 4){
					        		paramType = "hex";
					        	}else if(paramIndex == 3 || paramIndex == 7 || paramIndex == 8){
					        		paramType = "ascii";
					        	}else if(paramIndex == 9){
					        		paramType = "int";
					        	}
					        	m1 = clazz.getDeclaredMethod("_"+paramType, String.class, int.class, String.class);
					        }else{
					        	m1 = clazz.getDeclaredMethod("_"+firstEle.attributeValue("type"), String.class, int.class, String.class);
					        }
					        
					        if(dataContent.get(index)==null||dataContent.get(index).length()==0){
					        	//update  20170204  针对0x41可选字段用FF占位
					        	/*String defaultvalue=firstEle.attributeValue("value");
					        	lst.add((byte[])m1.invoke(type,defaultvalue ,byteSize,ChineseEncoding));*/
					        	byte[] b = new byte[byteSize];
					        	for (int i = 0; i < byteSize; i++) {
					        		b[i] = (byte) 0xFF;
								}
					        	lst.add(b);
					        	bytelenght+=byteSize;
					        }else{
					        	String value = dataContent.get(index);
						        if(value.startsWith("0x")){
						        	value = value.substring(2);
						        }
						        lst.add((byte[])m1.invoke(type,value ,byteSize,ChineseEncoding));
					        	bytelenght+=byteSize;
					        }
					        arrayIndex++;
							index++;
						}
						if(firstEle.getName().equals("array")){
							// array
							int byteSize=Integer.parseInt(firstEle.attributeValue("size"));
							bytelenght+=byteSize;
							Method m2 = clazz.getDeclaredMethod("_"+firstEle.attributeValue("type"), String.class, int.class, String.class);
							lst.add((byte[])m2.invoke(type, dataContent.get(index),byteSize,ChineseEncoding));
							
							for(int i=0;i<Integer.parseInt(dataContent.get(arrayIndex));i++){
								arrayItemIndex=arrayIndex;
								for (Iterator iter2 = firstEle.elementIterator(); iter2.hasNext();) {
									// item
									secondEle = (Element) iter2.next();
									if(secondEle.getName().equals("item")){
										index++;
										arrayItemIndex++;
										int byte2Size=Integer.parseInt(secondEle.attributeValue("size"));
										Method m3 = clazz.getDeclaredMethod("_"+secondEle.attributeValue("type"), String.class, int.class, String.class);
										String isSeq=secondEle.attributeValue("isSeq");
										if(isSeq!=null&&isSeq.equals("true")){
											lst.add((byte[])m3.invoke(type,String.valueOf(i+1),byte2Size,ChineseEncoding));
										}else{
											if(expectedId.equals("0x62")||expectedId.equals("0x64")){
												lst.add((byte[])m3.invoke(type, dataContent.get(index),byte2Size,ChineseEncoding));
											}else{
												lst.add((byte[])m3.invoke(type, dataContent.get(arrayItemIndex),byte2Size,ChineseEncoding));
											}
										}
										bytelenght+=byte2Size;
									}
								}
							}
							index++;
						}
					}
					//构造下发的报文
					if("3.4".equals(version)||"3.1".equals(version)||"3.2".equals(version)){
						return structure34Message(lst,bytelenght,commandId,deviceCode);
					}else{
						return structure35Message(lst,bytelenght,commandId,deviceCode);
					}
				}else{
					
					logger.info( commandId+"-in ParseData::协议号不存在");
				}
			} catch (Exception e) {
				logger.error( commandId+"-in ParseData", e);
			
				return null;
			}
			return null;
		}
		//3.4报文头
		private byte[] structure34Message(List<byte[]> lst, int bytelenght, String commandId,String deviceCode) {
			byte[] bytes = new byte[bytelenght+7];
			bytes[0] = (byte) 0xFA;
			bytes[1] = (byte) 0xF5;
			//包含从 序列号域 到 校验和域 的所有字节数,需要用16进制   序列号域   命令代码   数据域    校验和域
			if(bytelenght + 3>255){
				bytes[2] = CommFunction.intToUnsignByte(255);
			}else{
				bytes[2] = CommFunction.intToUnsignByte(bytelenght + 3);
			}
			bytes[3] = (byte) 0x80;
			bytes[4] = CommFunction.intToUnsignByte(bytelenght);
			bytes[5] = CommFunction.hexStringToByte(commandId.substring(2));
			
			int index =6;
			for (int i = 0; i < lst.size(); i++) {
				byte[] lstbt=lst.get(i);
				ArraysN.copy(bytes, index, lstbt, 0, lstbt.length);
				index+=lstbt.length;
			}
			byte[] bcheck = new byte[bytelenght+1];
			ArraysN.copy(bcheck, 0, bytes, 5, bytelenght+1);
			byte[] Bchecksum = CommFunction.hexStringToBytes(CommFunction.EncodeSumCheck(bcheck));
			ArraysN.copy(bytes, bytelenght+6, Bchecksum, 0, 1);
			return bytes;
			
		}
		//3.5报文头
		private byte[] structure35Message(List<byte[]> lst, int bytelenght, String commandId, String deviceCode) {
			
			byte[] bytes = new byte[bytelenght+24];
			//起始域
			bytes[0] = (byte) 0xFA;
			bytes[1] = (byte) 0xF5;
			//包含从 序列号域 到 校验和域 的所有字节数,需要用16进制   序列号域   命令代码   数据域    校验和域
			if(bytelenght + 20>255){
				bytes[2] = CommFunction.intToUnsignByte(255);//长度域 
			}else{
				bytes[2] = CommFunction.intToUnsignByte(bytelenght + 20);//长度域 
			}
			bytes[3] = (byte) 0x35;// 版本域
			bytes[4] = CommFunction.intToUnsignByte(bytelenght);//序列号域(即数据域长度)
			byte[] code = MessgeTransfer.eightstrTobytes(deviceCode);//  充电设备编码
			ArraysN.copy(bytes, 5, code, 0, code.length);
			byte[] time = MessgeTransfer.Time2StampTobytes();//  时间戳
			ArraysN.copy(bytes, 13, time, 0, time.length);
			bytes[22] = CommFunction.hexStringToByte(commandId.substring(2));//命令代码
			
			int index= 23; 
			for (int i = 0; i < lst.size(); i++) {
				byte[] lstbt=lst.get(i);
				ArraysN.copy(bytes, index, lstbt, 0, lstbt.length);
				index+=lstbt.length;
			}
			byte[] bcheck = new byte[bytelenght+1];
			ArraysN.copy(bcheck, 0, bytes, 22, bytelenght+1);
			byte[] Bchecksum = CommFunction.hexStringToBytes(CommFunction.EncodeSumCheck(bcheck));
			ArraysN.copy(bytes, bytelenght+23, Bchecksum, 0, 1);
			return bytes;
		}
	
		//下发报文入库
		private void insertMessage(String devicename,String commandId,byte[] bSenddata){
			
			CheckResultDto ResultDto = new CheckResultDto();
			ResultDto.setCheckTime(new Date());
			ResultDto.setEquipName(devicename);
			ResultDto.setMesCode(commandId);
			ResultDto.setAnswerTime(0);
			ResultDto.setCheckContent("检测中心下发报文");
			if(bSenddata==null){
				ResultDto.setSrcMessage(" ");
				ResultDto.setErrorInfo("报文下发参数不正确");
			}else{
				ResultDto.setSrcMessage(CommFunction.LogByteArray(bSenddata).toString());
				ResultDto.setErrorInfo(" ");
			}
			ResultDto.setT1CheckResult(" ");
			ResultDto.setT2CheckResult(" ");
			ResultDto.setT3CheckResult(" ");
		//	systemService.InsertResult(ResultDto);
			monDBService.storeDB();
		}
		
		// 0x09报文编码（供uTest调用）
		public byte[] parse09(String deviceCode, String devicename,String version,MonSequenceDto sequenceDto) {
			
			byte[] bytes09 = null;
			byte[] flowNum= new byte[2];
			byte[] sequenceNum = new byte[2];
			flowNum = CommFunction.intTo2ByteArray(sequenceDto.getFlowNum());
			sequenceNum = CommFunction.intTo2ByteArray(sequenceDto.getSequenceNum());
			if("3.4".equals(version)||"3.2".equals(version)){
				bytes09 = new byte[19];
				bytes09[0] = (byte) 0xFA;
				bytes09[1] = (byte) 0xF5;
				bytes09[2] = 0x0F;
				bytes09[3] = (byte) 0x80;
				bytes09[4] = 0x01;
				bytes09[5] = 0x09;
				//报文编号
				bytes09[6] = flowNum[1];
				bytes09[7] = flowNum[0];
				//指令序号
				bytes09[8] = sequenceNum[1];
				bytes09[9] = sequenceNum[0];
				//充电设备编码
				ArraysN.copy(bytes09, 10, MessgeTransfer.eightstrTobytes(deviceCode), 0, 8);
				byte[] bcheck = new byte[13];
				ArraysN.copy(bcheck, 0, bytes09, 5, 13);
				//检验和
				byte[] Bchecksum = CommFunction.hexStringToBytes(CommFunction.EncodeSumCheck(bcheck));
				ArraysN.copy(bytes09, 18, Bchecksum, 0, 1);
			}else{
				bytes09 = new byte[36];
				bytes09[0] = (byte) 0xFA;
				bytes09[1] = (byte) 0xF5;
				bytes09[2] = (byte) 0x20;
				bytes09[3] = (byte) 0x35;
				bytes09[4] = 0x01;
				//充电设备编码
				ArraysN.copy(bytes09, 5, MessgeTransfer.eightstrTobytes(deviceCode), 0, 8);
				//时间戳
				ArraysN.copy(bytes09, 13, MessgeTransfer.Time2StampTobytes(), 0, 9);
				bytes09[22] = 0x09;
				//报文编号
				bytes09[23] = flowNum[1];
				bytes09[24] = flowNum[0];
				//指令序号
				bytes09[25] = sequenceNum[1];
				bytes09[26] = sequenceNum[0];
				//充电设备编码
				ArraysN.copy(bytes09, 27, MessgeTransfer.eightstrTobytes(deviceCode), 0, 8);
				byte[] bcheck = new byte[13];
				ArraysN.copy(bcheck, 0, bytes09, 22, 13);
				//检验和
				byte[] Bchecksum = CommFunction.hexStringToBytes(CommFunction.EncodeSumCheck(bcheck));
				ArraysN.copy(bytes09, 35, Bchecksum, 0, 1);
			}
			return bytes09;
		}	
		
		
		// 0x48报文编码（供uTest调用）
		public byte[] parse48(byte[] receive, String deviceCode,String version) {
			
			byte[] bytes48 = null;
			if("3.4".equals(version)){
				bytes48 = new byte[13];
				bytes48[0] = (byte) 0xFA;
				bytes48[1] = (byte) 0xF5;
				bytes48[2] = (byte) 0x09;
				bytes48[3] = (byte) 0x80;
				bytes48[4] = (byte) 0x02;
				bytes48[5] = (byte) 0x48;
				ArraysN.copy(bytes48, 6, receive, 0, 6);
				// 计算检验和
				byte[] bcheck = new byte[7];
				ArraysN.copy(bcheck, 0, bytes48, 5, 7);
				byte[] Bchecksum = CommFunction.hexStringToBytes(CommFunction.EncodeSumCheck(bcheck));
				ArraysN.copy(bytes48, 12, Bchecksum, 0, 1);
			}else{
				bytes48 = new byte[30];
				bytes48[0] = (byte) 0xFA;
				bytes48[1] = (byte) 0xF5;
				bytes48[2] = (byte) 0x1A;
				bytes48[3] = (byte) 0x35;
				bytes48[4] = (byte) 0x01;
				//充电设备编码
				ArraysN.copy(bytes48, 5, MessgeTransfer.eightstrTobytes(deviceCode), 0, 8);
				//时间戳
				ArraysN.copy(bytes48, 13, MessgeTransfer.Time2StampTobytes(), 0, 9);
				bytes48[22] = 0x48;
				ArraysN.copy(bytes48, 23, receive, 0, 6);
				byte[] bcheck = new byte[7];
				ArraysN.copy(bcheck, 0, bytes48, 22, 7);
				//检验和
				byte[] Bchecksum = CommFunction.hexStringToBytes(CommFunction.EncodeSumCheck(bcheck));
				ArraysN.copy(bytes48, 29, Bchecksum, 0, 1);
			}
			return bytes48;
		}	
}
