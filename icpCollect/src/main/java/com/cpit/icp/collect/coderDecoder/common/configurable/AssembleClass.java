package com.cpit.icp.collect.coderDecoder.common.configurable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Element;
//import org.springframework.beans.factory.annotation.Autowired;

import com.cpit.common.SpringContextHolder;
import com.cpit.icp.collect.utils.cache.CacheUtil;

//import com.thinkgem.jeesite.common.utils.CacheUtils;

/**
 * 可配置元素基类
 * 
 * @author maming
 *
 */
public abstract class AssembleClass {
	
	//protected CacheUtil cacheUtil = SpringContextHolder.getBean(CacheUtil.class);
	
	static final String assembleClassPath = "com.cpit.icp.collect.coderDecoder.common.configurable.";
	byte[] receivedContent;
	// 属性名称
	private String attrName = null;
	// 属性16进制值
	private String attrHexStr = null;
	// 属性16进制值
	private String attrHexValue = null;
	// 属性含义
	private String attrMeanings = null;
	
	protected String ChineseEncoding = null;
	
	private StringBuffer buffer;
	
	private StringBuffer errbuffer ;
	
	Element element;
	
	protected Map<String,Object> assembleCache;
	
	/**
	 * 位置游标
	 */
	ParseIndex elementIndex;
	public AssembleClass(Element element, StringBuffer buffer,StringBuffer errbuffer,byte[] receivedContent,String ChineseEncoding,ParseIndex index,Map<String,Object> cacheMap) {
		this.assembleCache= cacheMap;
		this.elementIndex = index;
		this.receivedContent = receivedContent;
		this.buffer = buffer;
		this.errbuffer=errbuffer;
		this.ChineseEncoding =ChineseEncoding;
		attrName = element.attributeValue("name");
		this.element = element;
		//替换变量
		if(attrName!=null){
			if(attrName.indexOf("#i#") != -1 
				|| attrName.indexOf("#j#") != -1 
				|| attrName.indexOf("#k#") != -1){
				int pos = attrName.indexOf("#");
				String flag = attrName.substring(pos+1, pos+2);
			//	String toReplaceChar = String.valueOf(cacheUtil.get(flag));
				String toReplaceChar = String.valueOf(assembleCache.get(flag));
				attrName = attrName.replace("#"+flag+"#", toReplaceChar);
			}
		}
		procAttrHexStr();
		procAttrHexValue();
		procAttrMeanings();
		buildStringBuffer();
	}
	
	
	private void buildStringBuffer() {
		if(attrName == null ){
			return;
		}
		if(attrHexStr == null || attrMeanings == null){
			errbuffer.append(attrName+"解析错误;");
		}
		/*if(attrName.indexOf("故障")>-1){
			String[] str=attrMeanings.split("#");
			if(str[0]==null||str[0].equals("")){
				errbuffer.append(str[0]);
			}
			this.attrMeanings=str[1];
		}*/
		if(attrMeanings.equals("状态的值不在范围内")){
			errbuffer.append(attrName+"状态的值不在范围内;");
		}
		buffer.append(attrName+":"+attrHexStr+":"+attrMeanings+";");
	//	buffer.append(attrName+":"+attrHexValue+";");
		
	}
	

	public AssembleClass(Element element, StringBuffer buffer,byte[] receivedContent, String ChineseEncoding) {
		
		this.receivedContent = receivedContent;
		attrName = element.attributeValue("name");
		procAttrHexValue();
		procAttrMeanings();
	}

	public abstract void procAttrMeanings();
	public abstract void procAttrHexStr();
	public abstract void procAttrHexValue();

	public String getAttrHexStr() {
		return attrHexStr;
	}
	public void setAttrHexStr(String attrHexStr) {
		this.attrHexStr = attrHexStr;
	}
	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrHexValue() {
		return attrHexValue;
	}

	public void setAttrHexValue(String attrHexValue) {
		this.attrHexValue = attrHexValue;
	}

	public String getAttrMeanings() {
		return attrMeanings;
	}

	public void setAttrMeanings(String attrMeanings) {
		this.attrMeanings = attrMeanings;
	}
	public StringBuffer getBuffer() {
		return buffer;
	}
	public void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}	
	
}
