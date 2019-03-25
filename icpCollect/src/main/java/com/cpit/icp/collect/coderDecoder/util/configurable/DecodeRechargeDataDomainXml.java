package com.cpit.icp.collect.coderDecoder.util.configurable;


import java.io.IOException;
import java.lang.reflect.Constructor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cpit.common.SpringContextHolder;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.coderDecoder.DataContentDto;


/**
 * 充电桩协议数据域解析
 * 
 * @author maming
 *
 */
public class DecodeRechargeDataDomainXml {
private final static Logger logger = LoggerFactory.getLogger(DecodeRechargeDataDomainXml.class);
	
	
	private static final String assembleClassPath = "com.cpit.icp.collect.coderDecoder.common.configurable.";
	private Map<String, List<DataContentDto>> allOrderdata = null;
private static  CacheUtil cacheUtil = SpringContextHolder.getBean("cacheUtil");
	public DecodeRechargeDataDomainXml() {
		this.allOrderdata = new HashMap<String, List<DataContentDto>>();
	}

	public static String[] ParseData(byte[] dataContent, byte commandId,String version, String chineseEncoding ,String deviceNo) throws IOException {

		SAXReader sax = new SAXReader();
		Document document = null;
		String[] str =new String[2];
		
		String command=Integer.toHexString(commandId).toUpperCase();
		if(command.length()==1){
			command="0"+command;
		}
		command=command.substring(command.length()-2, command.length());
		
		if(command.equals("35")){
			str=Decode0x35DataContent.getInstance().get35DataContent(dataContent,version, chineseEncoding,deviceNo);
			return str;
		}
		if(command.equals("3D")){
			str=Decode0x3DDataContent.getInstance().get3DDataContent(dataContent,version, chineseEncoding,deviceNo);
			return str;
		}
		if(command.equals("97")){
			str=Decode0x97DataContent.get0x97DataContent(dataContent,version, chineseEncoding);
			return str;
		}
	/**	if(command.equals("15")){
			str=Decode0x15DataContent.get0x15DataContent(dataContent,version, chineseEncoding);
			return str;
		}
	*/	
		if(version.equals("3.5")){
			if(command.equals("1A")){
				str = Decode0x1ADataContent.get0x1ADataContent(dataContent,chineseEncoding);
				return str;
			}else if(command.equals("12")){
				str = Decode0x12DataContent.get0x12DataContent(dataContent,chineseEncoding);
				return str;
			}
		}
		
		try {
			
			//document = sax
			//		.read(DecodeRechargeDataDomainXml.class.getResourceAsStream("/conf/configurable/charging_"+version+"/designcharge.xml"));
			// xml rotocolset
			document = cacheUtil.getParseMsgXml(version);
			if(document==null) {
				logger.error("get ParseMsgXml is null.");
				str[1]="get ParseMsgXml is null";
				return str;
			}
			
			String expectedId = "";
			Element orderEle = null;
			Element rootEle = document.getRootElement();
			
			for (Iterator iter = rootEle.elementIterator(); iter.hasNext();) {
				// protocol
				orderEle = (Element) iter.next();
				expectedId = orderEle.attributeValue("id");
				expectedId=expectedId.substring(expectedId.length()-2, expectedId.length());
				if (command.equals(expectedId)){
					break;
				}else{
					expectedId = "";
				}
			}
			// 找到对应的xml
			if (!"".equals(expectedId)) {
				StringBuffer contentResultBuffer = new StringBuffer();
				StringBuffer contentErrResult = new StringBuffer();
				Class cls = Class.forName(assembleClassPath+orderEle.getName());
				Class[] paramTypes = { Element.class ,StringBuffer.class, StringBuffer.class,byte[].class,String.class};
				Object[] params = { orderEle ,contentResultBuffer,contentErrResult,dataContent,chineseEncoding}; // 方法传入的参�?
				Constructor con = cls.getConstructor(paramTypes); 
				con.newInstance(params);
				str[0]=contentErrResult.toString();
				str[1]=contentResultBuffer.toString();
				
				return str;
			}
		} catch (Exception e) {
			logger.error(""+e.getMessage());
		}
		return str;
		
	}

	// 获取报文参数组；
	public Map<String, List<DataContentDto>> getOrderDataContent() {
		return this.allOrderdata;
	}


	
}
