package com.cpit.icp.collect.coderDecoder.util.configurable;


import com.cpit.icp.collect.coderDecoder.common.configurable.type;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;

public class Decode0x15DataContent {
	
	private static final String LOG = Decode0x15DataContent.class.getName();
	
	
	
	public static String[] get0x15DataContent(byte[] dataContent, String version, String chineseEncoding) {
		
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
		int parameteType= Integer.parseInt(type._int(data,chineseEncoding));
		if(version.equalsIgnoreCase("3.1")||version.equalsIgnoreCase("3.2")) {
			sucsb.append("参数类型:"+CommFunction.byteArrayToHexStr2(data)+":"+type._parameterType31(data,chineseEncoding)+";");
			pos+=1;	
		}else {
			sucsb.append("参数类型:"+CommFunction.byteArrayToHexStr2(data)+":"+type._parameterType(data,chineseEncoding)+";");
			pos+=1;
		}
		
		
		data=ConfigTool.subBytes(dataContent, pos,1);
		int parameteNum=Integer.parseInt(type._int(data,chineseEncoding));
		sucsb.append("参数个数:"+CommFunction.byteArrayToHexStr2(data)+":"+parameteNum+";");
		
		for (int i = 1; i <=parameteNum; i++) {
			
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			int num=Integer.parseInt(type._int(data,chineseEncoding));
				if(num==0){
					sucsb.append("充电模块位置编号:所有；");
				}
				else{
				sucsb.append("充电模块位置"+i+"编号:"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
				}
			
			pos+=1;
			
			data=ConfigTool.subBytes(dataContent, pos,2);
			String controlType="";
			if(version.equals("3.4")){
				controlType=type._controlType34(data,chineseEncoding,parameteType,num);
			}
			else if(version.equals("3.5")){
				controlType=type._controlType(data,chineseEncoding,parameteType);
			}
			if(num==0){
				sucsb.append("充电模块控制参数"+CommFunction.byteArrayToHexStr2(data)+":"+controlType+";");
				break;
			}else{
				sucsb.append("充电模块"+i+"控制参数"+CommFunction.byteArrayToHexStr2(data)+":"+controlType+";");
				pos+=1;
			}

		}
		
		str[0] = errsb.toString();
		str[1] = sucsb.toString();
		return str;
		} catch (Exception e) {
			
			LoggerOperator.err(LOG, "ParseData 15 error!",e);
			str[0] = "报文数据错误";
			str[1] = sucsb.toString();
			return str;
		}
	}
}
