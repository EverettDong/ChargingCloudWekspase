package com.cpit.icp.collect.coderDecoder.common.configurable;

import java.lang.reflect.Constructor;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;
//import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.LoggerFactory;

import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;
import com.cpit.icp.collect.utils.cache.CacheUtil;

//import com.cpit.icp.collect.coderDecoder.util.ParseDecodeXml;
//import com.thinkgem.jeesite.common.utils.CacheUtils;
//import com.cpit.icp.collect.utils.CacheUtil;
import org.slf4j.Logger;

/**
 * @author maming 
 */
public class array extends AssembleClass {
	private final static Logger logger = LoggerFactory.getLogger(array.class);


	private static final String LOG = array.class.getName();
	
	public array(Element element, StringBuffer buffer, StringBuffer errbuffer,byte[] receivedContent,String ChineseEncoding,ParseIndex index,Map<String,Object> cacheMap) {
	super(element, buffer, errbuffer,receivedContent,ChineseEncoding,index,cacheMap);
		String arraySizeName = element.attributeValue("size");
		String arraySize = null;
		//
		if(arraySizeName.startsWith("$")&&arraySizeName.endsWith("$")){
			arraySizeName = arraySizeName.substring(1, arraySizeName.length()-1);
		//	arraySize = (String)cacheUtil.get(arraySize);
			arraySize = (String)assembleCache.get(arraySizeName);
		}
		if(arraySize!=null&&!arraySize.isEmpty()){
			String  curFlag = element.attributeValue("cur");
			for(int i = 0;i< Integer.valueOf(arraySize);i++){
			//	CacheUtils.put(curFlag, i);
				//cacheUtil.put(curFlag, i+1);
				assembleCache.put(curFlag, i+1);
				//
				for ( Iterator iterInner = element.elementIterator(); iterInner.hasNext(); ) { 
					Element elementInner = (Element) iterInner.next();
					try {
						Class cls = Class.forName(assembleClassPath+elementInner.getName());
						Class[] paramTypes = { Element.class ,StringBuffer.class ,StringBuffer.class ,byte[].class ,String.class ,ParseIndex.class,Map.class};
						Object[] params = { elementInner ,buffer,errbuffer,receivedContent,ChineseEncoding,index,cacheMap};
						Constructor con = cls.getConstructor(paramTypes); 
						con.newInstance(params);
					} catch (Exception e) {
						LoggerOperator.err(LOG, "array error!",e);
					}
				}
				
				
			}
			
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
