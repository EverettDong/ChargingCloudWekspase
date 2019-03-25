package com.cpit.icp.collect.coderDecoder.util.configurable;
import com.cpit.icp.collect.coderDecoder.common.configurable.type;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;

public class Decode0x12DataContent {
	
	private static final String LOG = Decode0x12DataContent.class.getName();
	
	public static String[] get0x12DataContent(byte[] dataContent, String chineseEncoding) {
		
		String[] str =new String[2];
		StringBuffer sucsb=new StringBuffer();
		StringBuffer errsb=new StringBuffer();
		try {
		
			
			int pos=0;
			
			byte[] data=ConfigTool.subBytes(dataContent, pos,2);
			sucsb.append("报文编号:"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=2;
			
			data=ConfigTool.subBytes(dataContent, pos,2);
			sucsb.append("指令序号:"+CommFunction.byteArrayToHexStr2(data)+":"+type._int(data,chineseEncoding)+";");
			pos+=2;
			
			data=ConfigTool.subBytes(dataContent, pos,1);
			int paramType=Integer.parseInt(type._int(data,chineseEncoding));
			sucsb.append("参数类型:"+CommFunction.byteArrayToHexStr2(data)+":");
		    if(paramType == 3){
		    	sucsb.append("客服电话;");
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,180);		
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding).trim()+";");	
			}else if(paramType == 4){
				sucsb.append("本网点收费信息;");
				pos+=1;
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(ConfigTool.subBytes(dataContent, pos,180))+":");		
				data=ConfigTool.subBytes(dataContent, pos,1);			
				sucsb.append("时段名称:"+CommFunction.byteArrayToHexStr2(data)+":");
				int timeType=Integer.parseInt(type._int(data,chineseEncoding));
				switch(timeType) 
				{ 
				case 1: 
					sucsb.append("尖;"); 
				break; 
				case 2: 
					sucsb.append("峰;");
				break;  
				case 3: 
					sucsb.append("平;");
				break; 
				case 4: 
					sucsb.append("谷;");
				break; 
				case 5: 
					sucsb.append("单一定价, 全时段;");
				break; 
				default: 
					errsb.append("时段名称值不正确,不在允许范围内;");
					break; 
				}
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,7);				
				sucsb.append("服务费:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding)+";");
				pos+=7;
				data=ConfigTool.subBytes(dataContent, pos,1);	
				// 计费规则总数
				int ruleSum=Integer.parseInt(type._int(data,chineseEncoding));
				sucsb.append("当前计费规则时段总数:"+CommFunction.byteArrayToHexStr2(data)+":"+ruleSum+";");
				pos+=1;
				for(int i=0;i<ruleSum;i++){		
					data=ConfigTool.subBytes(dataContent, pos,3);
					sucsb.append("计费开始时间"+(i+1)+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._timeStamp(data,chineseEncoding)+";");
					pos+=3;
					data=ConfigTool.subBytes(dataContent, pos,3);
					sucsb.append("计费结束时间"+(i+1)+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._timeStamp(data,chineseEncoding)+";");
					pos+=3;
				}
				data=ConfigTool.subBytes(dataContent, pos,7);
				sucsb.append("电价:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding)+";");
				pos+=7;
				data=ConfigTool.subBytes(dataContent, pos,7);
				sucsb.append("备用价格:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding)+";");
				pos+=7;
				data=ConfigTool.subBytes(dataContent, pos,1);
				sucsb.append("结束标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._byte(data,chineseEncoding)+";");
				
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,1);
				sucsb.append("查询结果:"+CommFunction.byteArrayToHexStr2(data)+":"+type._byte(data,chineseEncoding)+" ");
				int resType=Integer.parseInt(type._int(data,chineseEncoding));
				switch(resType) 
				{ 
				case 0: 
					sucsb.append("成功;"); 
				break; 
				case 1: 
					sucsb.append("失败;");
				break;  
				
				default: 
					errsb.append("查询结果值不正确,不在允许范围内;");
					break; 
				}
			} else if(paramType == 7){
				sucsb.append("当前动态验证码;");
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,180);		
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._hex(data,chineseEncoding).trim()+";");	
			} else if(paramType == 8){
				sucsb.append("广告灯箱开启关闭时间;");
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,180);		
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding).trim()+";");	
			} else if(paramType == 9){
				sucsb.append("电价性质;");
				pos+=1;
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(ConfigTool.subBytes(dataContent, pos,180))+":");	
				data=ConfigTool.subBytes(dataContent, pos,1);
				sucsb.append(CommFunction.byteArrayToHexStr2(data)+":");
				int electriPrice=Integer.parseInt(type._int(data,chineseEncoding));
				switch(electriPrice) 
				{ 
				case 1: 
					sucsb.append("一般工商业用电（不满1千伏）;"); 
				break; 
				case 2: 
					sucsb.append("一般工商业用电（1-10千伏）;");
				break;  
				case 3: 
					sucsb.append("大工业用电（1-10千伏）;");
				break; 
				default: 
					errsb.append("参数信息值不正确,不在允许范围内;");
					break; 
				}
				pos+=1;
			}else if(paramType == 10){
				sucsb.append("二维码动态首字段;");
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,180);		
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding).trim()+";");	
			}else{
				if(paramType == 1){
					sucsb.append("公司信息;"); 
				}else if(paramType == 2){
					sucsb.append("网站信息;");
				}else if(paramType == 5){
					sucsb.append("服务接口信息;");
				}else if(paramType == 6){
					sucsb.append("注意事项;");
				}else{
					errsb.append("参数类型值不正确,不在允许范围内;");
				}
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,180);		
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._GB(data,chineseEncoding).trim()+";");		
			}
			
			return str;
		} catch (Exception e) {
			LoggerOperator.err(LOG, "ParseData 12 error!",e);
			str[0] ="报文数据不正确";
			str[1] = sucsb.toString();
			return str;
		}
	}
}
