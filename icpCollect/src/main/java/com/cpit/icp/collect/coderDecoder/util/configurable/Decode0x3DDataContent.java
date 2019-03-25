package com.cpit.icp.collect.coderDecoder.util.configurable;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.common.SpringContextHolder;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.common.configurable.type;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
import com.cpit.icp.collect.msgProcess.ConstructDatagram;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.MonFaultRecordDto;

public class Decode0x3DDataContent {
	private final static Logger logger = LoggerFactory.getLogger(Decode0x3DDataContent.class);
	private static Decode0x3DDataContent decode0x3d = null;
	private  CacheUtil cacheUtil;
	public static Decode0x3DDataContent getInstance() {
		if(decode0x3d == null) {
			decode0x3d = new Decode0x3DDataContent();
		}
		return decode0x3d;
	}
	
	private Decode0x3DDataContent() {
		cacheUtil =  SpringContextHolder.getBean(CacheUtil.class);
	}
	
	
	
	public  String[] get3DDataContent(byte[] dataContent, String version, String chineseEncoding,String deviceNo) {
		
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
		
		data=ConfigTool.subBytes(dataContent, pos,3);
		String faultOccurTime= type._timeStamp(data,chineseEncoding);
		sucsb.append("故障发生时间:"+CommFunction.byteArrayToHexStr2(data)+":"+faultOccurTime+";");
		pos+=3;
		
		data=ConfigTool.subBytes(dataContent, pos,1);
		int chargeNum=Integer.parseInt(type._int(data,chineseEncoding));
		sucsb.append("充电枪数量:"+CommFunction.byteArrayToHexStr2(data)+":"+chargeNum+";");
		pos+=1;
		
		data=ConfigTool.subBytes(dataContent, pos,4);
		sucsb.append("系统故障状态:"+name+CommFunction.byteArrayToHexStr2(data)+":"+type._SystemFault(data,chineseEncoding)+";");
		pos+=4;//--------
		//need add fault
		data=ConfigTool.subBytes(dataContent, pos,4);
		sucsb.append("充电系统故障状态:"+name+CommFunction.byteArrayToHexStr2(data)+":"+type._ChargeSystemFault(data,chineseEncoding)+";");
		pos+=4;//--------
		//need add fault
		
		
		for (int i = 1; i <=chargeNum; i++) {
			data=ConfigTool.subBytes(dataContent, pos,1);
			if (version.equals("3.5")) {
				name="充/放电类型:";
				sucsb.append("充电枪位置"+i+name+CommFunction.byteArrayToHexStr2(data)+":"+type._cdqtype(data,chineseEncoding)+";");
			}else if (version.equals("3.4")) {
				name="充电类型:";
				sucsb.append("充电枪位置"+i+name+CommFunction.byteArrayToHexStr2(data)+":"+type._chargeType(data,chineseEncoding)+";");
			}
			pos+=1;
			
			
			data=ConfigTool.subBytes(dataContent, pos,4);
			sucsb.append("电池故障代码:"+CommFunction.byteArrayToHexStr2(data)+":"+type._hex(data,chineseEncoding)+";");
			pos+=4;
			
		
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("电池总故障等级:"+CommFunction.byteArrayToHexStr2(data)+":"+type._faultRank(data,chineseEncoding)+";");
			pos+=1;
			
			//电池故障代码总数量
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			int faultNum = Integer.parseInt(type._int(data,chineseEncoding));
			sucsb.append("电池故障代码总数量:"+CommFunction.byteArrayToHexStr2(data)+":"+faultNum+";");
			pos+=1;
			
			int index=pos;
			boolean flag=true;
			int codeNum=1;
			/*
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
			*/
			
			for(int j=1;j<=faultNum;j++){
				
				data=ConfigTool.subBytes(dataContent, pos,4);
				List<String> faultStr = MessgeTransfer.four_batterySystemFaultbyteToHexStr(data,1);
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
					if(!s.equalsIgnoreCase("无告警")) {
						MonFaultRecordDto faultDto = new MonFaultRecordDto();
						faultDto.setDeviceVersion("3.5");
						faultDto.setFaultSrcMsgCode("0x3d");
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
				pos+=4;
				
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
			sucsb.append("未知电池故障:"+CommFunction.byteArrayToHexStr2(data)+":"+type._hex(data,chineseEncoding)+";");
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("结束标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._groupID(data,chineseEncoding)+";");
			pos+=1;
		
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
