package com.cpit.icp.collect.coderDecoder.util.configurable;
import org.bouncycastle.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.collect.coderDecoder.common.configurable.type;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;
import com.cpit.icp.collect.service.MonDBService;
public class Decode0x1ADataContent {
	
	private final static Logger logger = LoggerFactory.getLogger(Decode0x1ADataContent.class);
	
	public static String[] get0x1ADataContent(byte[] dataContent, String chineseEncoding) {
		
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
			int rp = pos;
			if(paramType == 3){
				sucsb.append("客服电话;");
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,100);		
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding).trim()+";");		
			}else if(paramType == 4){
				sucsb.append("本网点收费信息;");
				pos+=1;
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(ConfigTool.subBytes(dataContent, pos,100))+":");		
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
				sucsb.append("计费规则总数:"+CommFunction.byteArrayToHexStr2(data)+":"+ruleSum+";");
				pos+=1;
				for(int i=0;i<ruleSum;i++){
					data=ConfigTool.subBytes(dataContent, pos,2);	
					StringBuffer yue = new StringBuffer();
					String y =Integer.toBinaryString(Integer.parseInt(type._int(data,chineseEncoding)));	
					if(y.equals("0")){
						errsb.append("计费规则"+(i+1)+"执行月份值不正确;");
					}else{
						for(int m=12;m>=1;m--){
							String month = y.substring(m-1, m);
							if(month.equals("1")){
								yue.append(13-m).append(",");
							}
						}		
						if(yue.length()>0){
							yue.deleteCharAt(yue.length()-1);
						}
					}			
					sucsb.append("计费规则"+(i+1)+"执行月份:"+CommFunction.byteArrayToHexStr2(data)+":"+yue.toString()+";");
					pos+=2;
					data=ConfigTool.subBytes(dataContent, pos,1);
					// 计费规则1时段总数
					int chargingRuleSum = Integer.parseInt(type._int(data,chineseEncoding));
					sucsb.append("计费规则"+(i+1)+"时段总数:"+CommFunction.byteArrayToHexStr2(data)+":"+chargingRuleSum+";");
					pos+=1;
					for(int j=0;j<chargingRuleSum;j++){
						data=ConfigTool.subBytes(dataContent, pos,3);
						sucsb.append("计费开始时间"+(j+1)+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._timeStamp(data,chineseEncoding)+";");
						pos+=3;
						data=ConfigTool.subBytes(dataContent, pos,3);
						sucsb.append("计费结束时间"+(j+1)+":"+CommFunction.byteArrayToHexStr2(data)+":"+type._timeStamp(data,chineseEncoding)+";");
						pos+=3;
					}
				}
				data=ConfigTool.subBytes(dataContent, pos,7);
				sucsb.append("电价:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding)+";");
				pos+=7;
				data=ConfigTool.subBytes(dataContent, pos,7);
				sucsb.append("备用价格:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding)+";");
				pos+=7;
				data=ConfigTool.subBytes(dataContent, pos,1);
				sucsb.append("结束标识:"+CommFunction.byteArrayToHexStr2(data)+":"+type._byte(data,chineseEncoding)+";");
			} else if(paramType == 7){
				sucsb.append("二维码动态首字段;");
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,100);		
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding).trim()+";");	
			} else if(paramType == 8){
				sucsb.append("广告灯箱开启关闭时间;");
				pos+=1;
				data=ConfigTool.subBytes(dataContent, pos,100);	
				byte[] startD = ConfigTool.subBytes(data, 0, 3);
				String s_startD = type._ascIITime(startD, chineseEncoding);
				byte[] b_comma = ConfigTool.subBytes(data, 3, 1);
				String s_comma= type._ascII(b_comma, chineseEncoding);
				byte[] b_end = ConfigTool.subBytes(data, 4, 3);
				String s_end = type._ascIITime(b_end, chineseEncoding);
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+s_startD+s_comma+s_end+";");		
				//sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._ascII(data,chineseEncoding).trim()+";");		
			} else if(paramType == 9){
				sucsb.append("电价性质;");
				pos+=1;
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(ConfigTool.subBytes(dataContent, pos,100))+":");	
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
				data=ConfigTool.subBytes(dataContent, pos,100);		
				sucsb.append("参数信息:"+CommFunction.byteArrayToHexStr2(data)+":"+type._GB(data,chineseEncoding).trim()+";");						
			}	
			pos=rp+101;
			data=ConfigTool.subBytes(dataContent, pos,2);	
			sucsb.append("设置结果:"+CommFunction.byteArrayToHexStr2(data)+":");
			String result = type._int(data, chineseEncoding);
			if(result.equals("0")){
				sucsb.append("成功;");
			}else if(result.equals("1")){
				sucsb.append("失败;");
			}else{
				errsb.append("设置结果值不正确,不在允许范围内;");
			}
			str[0] ="";
			str[1] = sucsb.toString();
			return str;
		} catch (Exception e) {
			logger.error("ParseData 1A error!",e);
			str[0] ="报文数据不正确";
			str[1] = sucsb.toString();
			return str;
		}
	}
}
