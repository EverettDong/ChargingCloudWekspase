package com.cpit.icp.collect.msgProcess;



import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cpit.common.Encodes;
import com.cpit.common.JsonUtil;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.MsgCodeUtil;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_1;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_2;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;

/**
 * convertor String decodedData to specialObj
 * 
 * @author zhangqianqian
 *
 */
@Service
public class MsgDataConvertor {
	@Autowired
	CacheUtil cacheUtil;

	@Autowired
	ConstructMsgUtil constructMsgUtil;
	private final static Logger logger = LoggerFactory.getLogger(MsgDataConvertor.class);
	private String classPath = "com.cpit.icp.dto.collect.";

	private void parseStoreXml(String msgCode,String codeVersion) {
		long startT = System.currentTimeMillis();
		String xmlName = null;
		String fieldName = null;
		String fieldType = null;
	
		Document document = cacheUtil.getObjStoreXml(codeVersion);
	
	
	
		Element rootEle = document.getRootElement();
		for (Iterator iter = rootEle.elementIterator(); iter.hasNext();) {
			Element OrderEle = (Element) iter.next();
			String className = OrderEle.attributeValue("className");
			String mesCode = OrderEle.attributeValue("dataCode");
			String tableName = OrderEle.attributeValue("tableName");
			if (!mesCode.equals(msgCode)) {
				continue;
			} else {
				setKeyValue(codeVersion+"-"+mesCode, tableName + ";" + className);

				try {

					for (Iterator iterInner = OrderEle.elementIterator(); iterInner.hasNext();) {

						Element parameterEle = (Element) iterInner.next();
						String nodeName = parameterEle.getName();
						

						String isCursor = parameterEle.attributeValue("isCursor");

						if (isCursor != null && isCursor.equalsIgnoreCase("true")) {
							//
							xmlName = parameterEle.attributeValue("name");
							fieldName = parameterEle.attributeValue("fieldName");
							fieldType = parameterEle.attributeValue("fieldType");
						
						} else if (nodeName.equalsIgnoreCase("array")) {
							for (Iterator iterInnerInner = parameterEle.elementIterator(); iterInnerInner.hasNext();) {
								Element arrayInner = (Element) iterInnerInner.next();
								String curFlag = arrayInner.attributeValue("cur");

								xmlName = arrayInner.attributeValue("name");
								if (xmlName != null) {
									if (xmlName.indexOf("#i#") != -1 || xmlName.indexOf("#j#") != -1
											|| xmlName.indexOf("#k#") != -1) {
										int pos = xmlName.indexOf("#");
										String flag = xmlName.substring(pos + 1, pos + 2);
										// String toReplaceChar = String.valueOf(cacheUtil.get(flag));
										xmlName = xmlName.replace("#" + flag + "#", "");
									}
								}

								fieldName = arrayInner.attributeValue("fieldName");
								fieldType = arrayInner.attributeValue("fieldType");

								setKeyValue(codeVersion+"-"+xmlName, fieldName + "-" + fieldType);
							}
						} else {
							xmlName = parameterEle.attributeValue("name");
							fieldName = parameterEle.attributeValue("fieldName");
							fieldType = parameterEle.attributeValue("fieldType");

						}

						setKeyValue(codeVersion+"-"+xmlName, fieldName + "-" + fieldType);

					}
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}

		}
		long endT = System.currentTimeMillis();
		logger.debug("parseTime: " + (endT - startT));
	}
	private void setKeyValue(String cacheKey,String value){
		String cacheValue= (String)cacheUtil.getKeyValue(cacheKey);
		if(cacheValue==null ||!cacheValue.equalsIgnoreCase(value)) {
			cacheUtil.setKeyValue(cacheKey, value);
		}
	}
	private Object  charging7DToObj(String mesCode,String version,String[] decodedData) {
		Object object;
		parseStoreXml(mesCode,version);
		String s = (String) cacheUtil.getKeyValue(version+"-0x7D");
		String tableName = s.substring(0, s.indexOf(";"));

		String className = s.substring(s.indexOf(";") + 1, s.length());
		Class cls;
		Class innerCls;
		
		try {
			
			cls = Class.forName(classPath + className);
			object = cls.newInstance();
			// Field[] fields = cls.getDeclaredField(name)
			String s1 = decodedData[1];
			
			if(!s1.contains(";")) {
				logger.info("decodedMsg is wrong,without;  ");
				return null;
			}
			String[] ss = s1.split(";");
			for (String item : ss) {
				if(!item.contains(":")) {
					logger.info("decodedMsg is wrong,without: ");
					return null;
				}
				
				String[] itemArray = item.split(":"); // name:hexValue:meaning
			
				String attrName;
				String attrHexValue;
				String attrMeaning;
				if(itemArray.length!=3) {
					if(itemArray.length>3) {
						logger.debug("itemArray length > 3.");
						int index = item.indexOf(":");
						int second = item.indexOf(":", index+1);
						attrName = item.substring(0,index);
						attrHexValue= item.substring(index+1,second);
						attrMeaning = item.substring(second+1,item.length());
					}else {
					logger.debug("itemArray length < 3");
					attrName =itemArray[0];
					attrHexValue =itemArray[1];
					attrMeaning ="";}
				}else {
					 attrName = itemArray[0];
					 attrHexValue = itemArray[1];
					 attrMeaning = itemArray[2];
				}
				
				
			

String cacheKey=version+"-"+attrName;
	String fieldAtt = (String) cacheUtil.getKeyValue(cacheKey);
	String fieldName ;
	if(fieldAtt==null) {
		logger.error("fieldAtt is null");
		fieldName="null";
	}else {
		fieldName = fieldAtt.substring(0, fieldAtt.indexOf("-"));
	}
	 
	if(!fieldName.equalsIgnoreCase("null")) {
		String fieldType = fieldAtt.substring(fieldAtt.indexOf("-") + 1, fieldAtt.length());
		logger.debug("fieldName: " + fieldName + " fieldType: " + fieldType);
		String fieldValue ;
		if(fieldName.equalsIgnoreCase("strategy")||
				fieldName.equalsIgnoreCase("normalEnd")||
				fieldName.equalsIgnoreCase("rechargeType")) {
			String hexValue = attrHexValue;
			fieldValue = String.valueOf(CommFunction.hexStringToByte(hexValue));
		}else {
			fieldValue = attrMeaning;
		}
		if(fieldValue.contains("kwh")) {
			fieldValue=	fieldValue.replace("kwh", "");
		}
		if(fieldValue.contains("秒")) {
			fieldValue=	fieldValue.replace("秒", "");
		}
		if(fieldValue.contains("安时")) {
			fieldValue=fieldValue.replace("安时", "");
		}
		

		logger.debug("fields " +fieldName+" " +fieldValue);

		// -------constructor field
		object = initObj(cls, object, fieldName, fieldValue,version);


	}else {
		logger.debug("fieldName is null.");
	}
	
				
			}

		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("reflect error: " + e.getMessage());

			return null;
		}

		long endT = System.currentTimeMillis();
		//logger.debug("end of parseData:" + (endT - startT));
		return object;
	}
	

	private Object reflectToObj(String mesCode,String version,String[] decodedData) {
		Object object;
		String s = (String) cacheUtil.getKeyValue(version+"-"+mesCode);
		String tableName = s.substring(0, s.indexOf(";"));

		String className = s.substring(s.indexOf(";") + 1, s.length());
		Class cls;
		//Class innerCls;
		
		try {
			
			cls = Class.forName(classPath + className);
			object = cls.newInstance();
			
			String s1 = decodedData[1];
			
			if(!s1.contains(";")) {
				logger.info("decodedMsg is wrong,without;  ");
				return null;
			}
			String[] ss = s1.split(";");
			for (String item : ss) {
				if(!item.contains(":")) {
					logger.info("decodedMsg is wrong,without: ");
					return null;
				}
				
				String[] itemArray = item.split(":"); // name:hexValue:meaning
				String attrName;
				String attrHexValue;
				String attrMeaning;
				if(itemArray.length!=3) {
					if(itemArray.length>3) {
						logger.debug("itemArray length > 3.");
						int index = item.indexOf(":");
						int second = item.indexOf(":", index+1);
						attrName = item.substring(0,index);
						attrHexValue= item.substring(index+1,second);
						attrMeaning = item.substring(second+1,item.length());
					}else {
					logger.debug("itemArray length < 3");
					attrName =itemArray[0];
					attrHexValue =itemArray[1];
					attrMeaning ="";}
				}else {
					 attrName = itemArray[0];
					 attrHexValue = itemArray[1];
					 attrMeaning = itemArray[2];
				}
				
				
			
String cacheKey=version+"-"+attrName;
				String fieldAtt = (String) cacheUtil.getKeyValue(cacheKey);
				String fieldName = fieldAtt.substring(0, fieldAtt.indexOf("-"));
				String fieldType = fieldAtt.substring(fieldAtt.indexOf("-") + 1, fieldAtt.length());
				logger.debug("fieldName: " + fieldName + " fieldType: " + fieldType);
				String fieldValue ;
				if(fieldName.equalsIgnoreCase("strategy")||
						fieldName.equalsIgnoreCase("normalEnd")||
						fieldName.equalsIgnoreCase("rechargeType")) {
					String hexValue = attrHexValue;
					fieldValue = String.valueOf(CommFunction.hexStringToByte(hexValue));
				}else {
					fieldValue = attrMeaning;
				}
				if(fieldValue.contains("秒")) {
					fieldValue=	fieldValue.replace("秒", "");
				}
				if(fieldValue.contains("安时")) {
					fieldValue=fieldValue.replace("安时", "");
				}
				logger.debug("fields " +fieldName+" " +fieldValue);

				// -------constructor field
				object = initObj(cls, object, fieldName, fieldValue,version);

				
			}

		

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("reflect error: " + e.getMessage());

			return null;
		}

		long endT = System.currentTimeMillis();
	
		return object;
	}
	
public Object chargingStrToObj(String[] decodedData, String mesCode,String version) {
	logger.info("79strToObj begins.");
	if(decodedData.length<2) {
		logger.info("charging str is wrong.");
		return null;
	}
	if(decodedData[1].equalsIgnoreCase("")) {
		logger.info("decodedata is null");
		return null;
	}
	long startT = System.currentTimeMillis();
	
	if(mesCode.equalsIgnoreCase("0x7D")) {
	
		return charging7DToObj(mesCode,VERSION_3_5,decodedData);
	}else if(mesCode.equalsIgnoreCase("0x78")&&version.equalsIgnoreCase(VERSION_3_1)) {
		parseStoreXml(mesCode,VERSION_3_1);
		return reflectToObj(mesCode,VERSION_3_1,decodedData);
	}else if(mesCode.equalsIgnoreCase("0x78")&&version.equalsIgnoreCase(VERSION_3_2)) {
		parseStoreXml(mesCode,VERSION_3_2);
		return reflectToObj(mesCode,VERSION_3_2,decodedData);
	}else	if(mesCode.equalsIgnoreCase("0x79")) {
		parseStoreXml(mesCode,VERSION_3_4);
		return reflectToObj(mesCode,VERSION_3_4,decodedData);
	}else {
		logger.error("mesCode is wrong,return null");
		return null;
	}
	

	
}


	private Object initObj(Class cls, Object object, String fieldName, String fieldValue,String version) throws Exception {
		logger.info("begin initObj." + fieldName + " " + fieldValue);
		Field field;
		PropertyDescriptor descriptor;

	
		field = cls.getDeclaredField(fieldName);
		descriptor = new PropertyDescriptor(fieldName, cls);
		

		if (field.getGenericType().toString().equals("class java.lang.String")) {
			logger.debug("String" + fieldValue + " " + field.getName());
			Method methodset = descriptor.getWriteMethod();

			methodset.invoke(object, fieldValue);
		} else if (field.getGenericType() instanceof ParameterizedType) {
			logger.debug("List" + fieldValue + " " + field.getName());
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			Class genericClazz = (Class) pt.getActualTypeArguments()[0]; //
			if (genericClazz.getName().startsWith("java.util.Date") || genericClazz.getName().startsWith("javax")
					|| genericClazz.getName().startsWith("com.sun") || genericClazz.getName().startsWith("sun")
					|| genericClazz.getName().startsWith("boolean") || genericClazz.getName().startsWith("double")
					|| genericClazz.getName().startsWith("int")) {

			}
			logger.debug("" + genericClazz);
String cacheKey=version+"-"+fieldName;
			cacheUtil.upParamData(cacheKey, fieldValue);
		

			Method methodset = descriptor.getWriteMethod();
			methodset.invoke(object, (List) cacheUtil.getParamData(cacheKey));
		} 	
		else {
		}

		return object;

	}

	public MsgOfPregateDto preGateStrToObj(String jsonStr) {
		logger.debug("begin preGateStrToObj");
		MsgOfPregateDto dto= null;
		try {
			 dto = (MsgOfPregateDto) JsonUtil.jsonToBean(jsonStr, MsgOfPregateDto.class);
			if(dto.getDeviceNo() ==null || dto.getDeviceNo().equalsIgnoreCase("")){
				logger.info("dto deviceNo is null");
				return null;
			}
			if(dto.getPreGateIp() ==null || dto.getPreGateIp().equalsIgnoreCase("")) {
				logger.info("dto preGateIp is null");
				return null;
			}
			if(dto.getPreGatePort() ==null || dto.getPreGatePort().equalsIgnoreCase("")) {
				logger.info("dto getPreGatePort is null");
				return null;
			}
			if(dto.getMsgCode() ==null || dto.getMsgCode().equalsIgnoreCase("")) {
				logger.info("dto getMsgCode is null");
				return null;
			}
			if(dto.getMsgData() ==null || dto.getMsgData().equalsIgnoreCase("")) {
				logger.info("dto getMsgData is null");
				return null;
			}
			if(dto.getMsgVersion() ==null || dto.getMsgVersion().equalsIgnoreCase("")) {
				logger.info("dto getMsgVersion is null");
				return null;
			}
			String version = constructMsgUtil.versionConvertToS(dto.getMsgVersion());
			if (version == null) {
				logger.info("convert version is null");
				return null;
			} else {
				dto.setMsgVersion(version);
			}
			return dto;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("preGateStr convert To Obj:" + e.getMessage());
			return null;
		}

	}
	
}
