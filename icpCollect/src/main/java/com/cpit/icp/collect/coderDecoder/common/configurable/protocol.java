package com.cpit.icp.collect.coderDecoder.common.configurable;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;

/**
 * 协议类型
 * @author maming
 *
 */
public class protocol extends AssembleClass{
	private final static Logger logger = LoggerFactory.getLogger(protocol.class);
	private Map<String,Object> cacheMap;
	public protocol(Element element, StringBuffer buffer,StringBuffer errbuffer,byte[] dataContent,String ChineseEncoding) {
		super(element, buffer,dataContent,ChineseEncoding);
		this.cacheMap = new HashMap<String,Object>();
		//终端到平台中心的报文
		if(element.attributeValue("direction").equals("ts")){
			ParseIndex pos = new ParseIndex();
			for ( Iterator iterInner = element.elementIterator(); iterInner.hasNext(); ) { 
				Element elementInner = (Element) iterInner.next();
				try {
					Class cls = Class.forName(assembleClassPath+elementInner.getName());
					Class[] paramTypes = { Element.class ,StringBuffer.class ,StringBuffer.class,byte[].class, String.class,ParseIndex.class,Map.class};
					Object[] params = { elementInner ,buffer ,errbuffer,dataContent,ChineseEncoding,pos,cacheMap}; // 方法传入的参数
					Constructor con = cls.getConstructor(paramTypes); 
					con.newInstance(params);
				} catch (Exception e) {
					logger.error("protocol parse error!",e);
				}
			}
			logger.debug("inner cacheMap set null.");
			cacheMap=null;
		}
		
		
	}

	@Override
	public void procAttrMeanings() {
	}

	@Override
	public void procAttrHexValue() {
	}

	@Override
	public void procAttrHexStr() {
	}
	
}
