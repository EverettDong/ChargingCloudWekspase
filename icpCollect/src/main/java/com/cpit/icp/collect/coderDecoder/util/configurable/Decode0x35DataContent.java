package com.cpit.icp.collect.coderDecoder.util.configurable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;

import com.cpit.common.SpringContextHolder;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.common.configurable.type;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
import com.cpit.icp.collect.msgProcess.ConstructDatagram;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.MonFaultRecordDto;

public class Decode0x35DataContent {
	
	private final static Logger logger = LoggerFactory.getLogger(Decode0x35DataContent.class);
	private static Decode0x35DataContent decode0x35 = null;
	private  CacheUtil cacheUtil;
	public static Decode0x35DataContent getInstance() {
		if(decode0x35 == null) {
			decode0x35 = new Decode0x35DataContent();
		}
		return decode0x35;
	}
	
	private Decode0x35DataContent() {
		cacheUtil =  SpringContextHolder.getBean(CacheUtil.class);
	}
	
	
	
	public  String[] get35DataContent(byte[] dataContent, String version, String chineseEncoding,String deviceNo) {
		
		String[] str =new String[2];
		StringBuffer sucsb=new StringBuffer();
		StringBuffer errsb=new StringBuffer();
		
		try {
		
		
		int pos=0;
		
		String name="";
		if(version.equals("3.4")){
			name="用户ID:";
		}
		else if(version.equals("3.5")){
			name="报文编号:";
		}
		
		byte[] data=ConfigTool.subBytes(dataContent, pos,2);
		sucsb.append(name+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
		pos+=2;
		
		data=ConfigTool.subBytes(dataContent, pos,2);
		sucsb.append("指令序号:"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
		pos+=2;
		
		data=ConfigTool.subBytes(dataContent, pos,1);
		int chargeNum=Integer.parseInt(type._int(data,chineseEncoding));
		sucsb.append("充电枪数量:"+CommFunction.byteArrayToHexStr2(data)+":"+chargeNum+";");
		
		for (int i = 1; i <=chargeNum; i++) {
			
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			if (version.equals("3.5")) {
				name="充/放电类型:";
				sucsb.append("充电枪位置"+i+name+CommFunction.byteArrayToHexStr2(data)+":"+type._cdqtype(data,chineseEncoding)+";");
			}else if (version.equals("3.4")) {
				name="充电类型:";
				sucsb.append("充电枪位置"+i+name+CommFunction.byteArrayToHexStr2(data)+":"+type._cdqtype(data,chineseEncoding)+";");
			}
			
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("分组标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._groupID(data,chineseEncoding)+";");
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("电池总故障等级:"+CommFunction.byteArrayToHexStr2(data)+":"+type._faultRank(data,chineseEncoding)+";");
			pos+=1;
			
			int index=pos;
			boolean flag=true;
			int codeNum=1;
			
			data=ConfigTool.subBytes(dataContent, index,2);
			String code=type._faultRank(data,chineseEncoding);
			index+=2;
			if(code.equals("无告警")&&dataContent[index]==-1){
				
			}else{
				while(flag){
					int happenNum=Integer.parseInt(type._int(ConfigTool.subBytes(dataContent,index,1),chineseEncoding));
					index+=1;
					index+=happenNum*2;
					if(dataContent[index]==-1){
						flag=false;						
					}else{
						codeNum++;
						index+=2;
					}
				}
			}
			
			for(int j=1;j<=codeNum;j++){
				
				data=ConfigTool.subBytes(dataContent, pos,2);
				List<String> faultStr = MessgeTransfer.batterySystemFaultbyteToHexStr(data,1);
			//	String[] codeStr=
			//	if(faultStr.get(0)!=null&&!faultStr.get(0).isEmpty()){
			//		errsb.append("电池故障代码"+j+":"+codeStr[0].toString()+";");
			//	}
				StringBuilder sb = new StringBuilder();
				
				for(String s:faultStr){
					if(s.equals("电池系统故障状态值不正确"))
					{
						errsb.append("电池故障代码"+j+":"+s+";");
						continue;
					}
					if(!s.equals("无告警")) {
						MonFaultRecordDto faultDto = new MonFaultRecordDto();
						faultDto.setDeviceVersion(version);
						faultDto.setFaultSrcMsgCode("0x35");
						faultDto.setInDate(TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR));
						faultDto.setParsedFaultData(s);
						faultDto.setFaultData(CommFunction.bytesToHexString(data));
						faultDto.setFaultLevel("1级");
						faultDto.setDeviceNo(deviceNo);
						upData(deviceNo,faultDto);	
					}
					
					sb.append(s);
				}
				sucsb.append("电池故障代码"+j+":"+CommFunction.byteArrayToHexStr2(data)+":"+sb.toString()+";");
				pos+=2;
				
				if(!faultStr.get(0).equals("无告警")){
					data=ConfigTool.subBytes(dataContent, pos,1);
					int happenPointNum=Integer.parseInt(type._int(data,chineseEncoding));
					sucsb.append("电池故障代码"+j+"发生位置总数:"+CommFunction.byteArrayToHexStr2(data)+":"+happenPointNum+";");
					pos+=1;
					
					for(int k=1;k<=happenPointNum;k++){
						data=ConfigTool.subBytes(dataContent, pos,2);
						sucsb.append("电池故障代码"+j+"故障位置"+k+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
						pos+=2;
					}
				}else{
					data=ConfigTool.subBytes(dataContent, pos,1);
					int happenPointNum=Integer.parseInt(type._int(data,chineseEncoding));
					sucsb.append("电池故障代码"+j+"发生位置总数:"+CommFunction.byteArrayToHexStr2(data)+":"+happenPointNum+";");
					pos+=1;
				}
			}
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("结束标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._groupID(data,chineseEncoding)+";");
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("分组标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._groupID(data,chineseEncoding)+";");
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);			
			sucsb.append("电池系统总故障等级:"+CommFunction.byteArrayToHexStr2(data)+":"+type._faultRank(data,chineseEncoding)+";");
			pos+=1;
			
			
			int index2=pos;
			boolean flag2=true;
			int codeNum2=1;
			
			data=ConfigTool.subBytes(dataContent,index2,2);
			String code2=type._faultRank(data,chineseEncoding);
			index2+=2;
			if(code2.equals("无告警")&&dataContent[index2]==-1){
				
			}else{
				while(flag2){
					int happenNum2=Integer.parseInt(type._int(ConfigTool.subBytes(dataContent,index2,1),chineseEncoding));
					index2+=1;
					index2+=happenNum2*2;
					if(dataContent[index2]==-1){
						flag2=false;						
					}else{
						codeNum2++;
						index2+=2;
					}
				}
			}
			
			for(int j=1;j<=codeNum2;j++){
				
				data=ConfigTool.subBytes(dataContent, pos,2);
				List<String> faultStr = MessgeTransfer.batterySystemFaultbyteToHexStr(data,1);
				//	String[] codeStr=
				//	if(faultStr.get(0)!=null&&!faultStr.get(0).isEmpty()){
				//		errsb.append("电池故障代码"+j+":"+codeStr[0].toString()+";");
				//	}
					StringBuilder sb = new StringBuilder();
					
					for(String s:faultStr){
						if(s.equals("电池系统故障状态值不正确"))
						{
							errsb.append("电池故障代码"+j+":"+s+";");
							continue;
						}
						if(!s.equals("无告警")) {
							MonFaultRecordDto faultDto = new MonFaultRecordDto();
							faultDto.setDeviceVersion(version);
							faultDto.setFaultSrcMsgCode("0x35");
							faultDto.setInDate(TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR));
							faultDto.setParsedFaultData(s);
							faultDto.setFaultData(CommFunction.bytesToHexString(data));
							faultDto.setFaultLevel("1级");
							faultDto.setDeviceNo(deviceNo);
							upData(deviceNo,faultDto);
						}
					
						sb.append(s);
					}
			
				sucsb.append("电池系统总故障代码"+j+":"+CommFunction.byteArrayToHexStr2(data)+":"+sb.toString()+";");
				pos+=2;
				
				if(!faultStr.get(0).equals("无告警")){
					data=ConfigTool.subBytes(dataContent, pos,1);
					int happenPointNum=Integer.parseInt(type._int(data,chineseEncoding));
					sucsb.append("故障代码"+j+"发生位置总数:"+CommFunction.byteArrayToHexStr2(data)+":"+happenPointNum+";");
					pos+=1;
					
					for(int k=1;k<=happenPointNum;k++){
						data=ConfigTool.subBytes(dataContent, pos,2);
						sucsb.append("故障代码"+j+"故障位置"+k+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
						pos+=2;
					}
			     }else{
			    	 data=ConfigTool.subBytes(dataContent, pos,1);
						int happenPointNum=Integer.parseInt(type._int(data,chineseEncoding));
						sucsb.append("故障代码"+j+"发生位置总数:"+CommFunction.byteArrayToHexStr2(data)+":"+happenPointNum+";");
						pos+=1;
			     }
			}
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("结束标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._groupID(data,chineseEncoding)+";");
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("分组标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._groupID(data,chineseEncoding)+";");
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("BMS故障总故障等级:"+CommFunction.byteArrayToHexStr2(data)+":"+type._faultRank(data,chineseEncoding)+";");
			pos+=1;
			
			
			int index3=pos;
			boolean flag3=true;
			int codeNum3=1;
			
			data=ConfigTool.subBytes(dataContent,index3,2);
			String code3=type._faultRank(data,chineseEncoding);
			index3+=2;
			if(code3.equals("无告警")&&dataContent[index3]==-1){
				
			}else{
				while(flag3){
					int happenNum3=Integer.parseInt(type._int(ConfigTool.subBytes(dataContent,index3,1),chineseEncoding));
					index3+=1;
					index3+=happenNum3*2;
					if(dataContent[index3]==-1){
						flag3=false;						
					}else{
						codeNum3++;
						index3+=2;
					}
				}
			}
			
			for(int j=1;j<=codeNum3;j++){
				
				data=ConfigTool.subBytes(dataContent, pos,2);
				//String[] codeStr=MessgeTransfer.BatterySystemFaultbyteToHexStr(data,1);
				//if(codeStr[0]!=null&&!codeStr[0].isEmpty()){
				//	errsb.append("BMS故障代码"+j+":"+codeStr[0].toString()+";");
				//}
				List<String> faultStr= MessgeTransfer.batterySystemFaultbyteToHexStr(data, 1);
				StringBuilder sb = new StringBuilder();
				
				for(String s:faultStr){
					if(s.equals("电池系统故障状态值不正确"))
					{
						errsb.append("电池故障代码"+j+":"+s+";");
						continue;
					}
					if(!s.equals("无告警")) {
						MonFaultRecordDto faultDto = new MonFaultRecordDto();
						faultDto.setDeviceVersion(version);
						faultDto.setFaultSrcMsgCode("0x35");
						faultDto.setInDate(TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR));
						faultDto.setParsedFaultData(s);
						faultDto.setFaultData(CommFunction.bytesToHexString(data));
						faultDto.setFaultLevel("1级");
						faultDto.setDeviceNo(deviceNo);
						upData(deviceNo,faultDto);
					}
				
					
					sb.append(s);
				}
				
				sucsb.append("BMS故障代码"+j+":"+CommFunction.byteArrayToHexStr2(data)+":"+sb.toString()+";");
				pos+=2;
				if(!faultStr.get(0).equals("无告警")){
					data=ConfigTool.subBytes(dataContent, pos,1);
					int happenPointNum=Integer.parseInt(type._int(data,chineseEncoding));
					sucsb.append("BMS故障代码"+j+"发生位置总数:"+CommFunction.byteArrayToHexStr2(data)+":"+happenPointNum+";");
					pos+=1;
					for(int k=1;k<=happenPointNum;k++){
						data=ConfigTool.subBytes(dataContent, pos,2);
						sucsb.append("BMS故障代码"+j+"故障位置"+k+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
						pos+=2;
					}
				}else{
					data=ConfigTool.subBytes(dataContent, pos,1);
					int happenPointNum=Integer.parseInt(type._int(data,chineseEncoding));
					sucsb.append("BMS故障代码"+j+"发生位置总数:"+CommFunction.byteArrayToHexStr2(data)+":"+happenPointNum+";");
					pos+=1;
				}
			}
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("结束标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._groupID(data,chineseEncoding)+";");
			
		}
		
		str[0] ="";
		str[1] = sucsb.toString();
		return str;
		} catch (Exception e) {
			
			logger.error("ParseData 35 error!");
			str[0] ="报文数据不正确";
			str[1] = sucsb.toString();
			return str;
		}
	}
	
	
	
	private  void upData(String deviceNo,MonFaultRecordDto faultData) {
		logger.info("up faultData redis");
		cacheUtil.upFaultData(deviceNo,faultData);
	}
}
