package com.cpit.icp.collect.coderDecoder.util.configurable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.collect.coderDecoder.common.configurable.type;

import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
//import com.cpit.testplatform.modules.test.entity.HistroyFault;
import com.cpit.icp.collect.msgProcess.ParseReponseXml;

public class Decode0x97DataContent {
	
	private final static Logger logger = LoggerFactory.getLogger(Decode0x97DataContent.class);
	
	public static String[] get0x97DataContent(byte[] dataContent,  String version, String chineseEncoding) {
		
		String[] str =new String[2];
		StringBuffer sucsb=new StringBuffer();
		StringBuffer errsb=new StringBuffer();
		try {
		
		///StringBuffer sucsb=new StringBuffer();
		//StringBuffer errsb=new StringBuffer();
		int pos=0;
		
		String name="";
		String faultCode="";
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
		
		data=ConfigTool.subBytes(dataContent, pos,8);
		sucsb.append("充电桩编号:"+CommFunction.byteArrayToHexStr2(data)+":"+type._hex(data, chineseEncoding)+";");
		pos+=8;
		
		data=ConfigTool.subBytes(dataContent, pos,1);
		String querrytype = MessgeTransfer.unsignByteToInt(data[0], "查询类型")[1];
		sucsb.append("查询类型:"+CommFunction.byteArrayToHexStr2(data)+":"+querrytype+";");		
		pos+=1;
		
		data=ConfigTool.subBytes(dataContent, pos,1);
		int recordCount = CommFunction.unsignByteToInt(data[0]);
		sucsb.append("记录数:"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
		pos+=1;
		
		for (int i = 1; i <=recordCount; i++) {
			
			data=ConfigTool.subBytes(dataContent, pos,4);
			sucsb.append("记录序号"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=4;
			data=ConfigTool.subBytes(dataContent, pos,2);
			sucsb.append("时间年"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=2;
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("月"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=1;
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("日"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=1;
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("时"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=1;
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("分"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=1;
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("秒"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=1;
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("预留"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+CommFunction.byteArrayToHexStr2(data)+";");
			pos+=1;
			data=ConfigTool.subBytes(dataContent, pos,1);
			sucsb.append("模块编号"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			//pos+=1;
		//	data=ConfigTool.subBytes(dataContent, pos,1);
			//sucsb.append("模块编号"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			
			data=ConfigTool.subBytes(dataContent, pos,4);
			if (querrytype.equalsIgnoreCase("系统故障状态")) {
				faultCode=MessgeTransfer.SystemFaultbyteToHexStr(data, 2)[1];
			} else if (querrytype.equalsIgnoreCase("充电系统故障状态")) {
				faultCode=(MessgeTransfer.ChargeSystemFaultbyteToHexStr(data, 2))[1];
			} else if (querrytype.equalsIgnoreCase("充电模块故障")) {
				faultCode=MessgeTransfer.ModuleFaultbyteToHexStr(data, 2)[1];
			} else if (querrytype.equalsIgnoreCase("电池故障")) {
				faultCode=MessgeTransfer.BatterySystemFaultbyteToHexStr(data, 2)[1];
			}
			sucsb.append("故障代码"+i+":"+CommFunction.byteArrayToHexStr2(data)+":"+faultCode+";");
			pos+=4;
		}
		
		str[0] = errsb.toString();
		str[1] = sucsb.toString();
		return str;
		} catch (Exception e) {
			
			logger.error("ParseData 97 error!"+e.getMessage());
			str[0] = "字段解析不正确";
			str[1] = sucsb.toString();
			return str;
		}
	}
}
