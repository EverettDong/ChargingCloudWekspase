package com.cpit.icp.collect.coderDecoder.common.configurable;

import java.lang.reflect.Method;
import java.util.Map;

import org.dom4j.Element;

//import com.cpit.common.SpringContextHolder;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;
import com.cpit.icp.collect.coderDecoder.util.configurable.ConfigTool;
//import com.cpit.icp.collect.utils.CacheUtil;

/**
 * 基本类型
 * @author maming
 *
 */
public class item  extends AssembleClass{
	

	private static final String LOG = item.class.getName();
	

	public item(Element element, StringBuffer buffer,StringBuffer errbuffer,byte[] dataContent,String ChineseEncoding,ParseIndex pos,Map<String,Object> cacheMap) {
		
		super(element, buffer,errbuffer,dataContent,ChineseEncoding,pos,cacheMap);
	}

	/* 
	 * 处理属性含义
	 */
	@Override
	public void procAttrMeanings() {
		String descValue = this.element.attributeValue("desc");
		if(descValue != null){
			this.setAttrMeanings(descValue);
		}else{
			this.setAttrMeanings(this.getAttrHexValue());
		}
	}

	/* 
	 * 处理属性值
	 */
	@Override
	public void procAttrHexValue() {
		int len = Integer.valueOf(this.element.attributeValue("length"));
		String name = this.element.attributeValue("name");
		byte[] eleBytes = ConfigTool.subBytes(this.receivedContent, this.elementIndex.index,len );
		try {
			//取得元素类型
			String type = "_"+this.element.attributeValue("type");
			Class cls = Class.forName(assembleClassPath+"type");
			Method method = cls.getMethod(type, byte[].class,String.class); 
			String strValue = (String)method.invoke(null, eleBytes,ChineseEncoding);
			String unit = this.element.attributeValue("unit");
			if(unit != null){
				//1011 del 
				strValue=strValue+unit;
			}
			this.setAttrHexValue(strValue);
			//元素游标后移
			this.elementIndex.index = this.elementIndex.index + len;
			//游标类型记录在缓存中
			String isCursor = this.element.attributeValue("isCursor");
			if(isCursor != null){
				//CacheUtils.put(this.getAttrName(), strValue);
				//cacheUtil.setKeyValue(this.getAttrName(), strValue);
				assembleCache.put(this.getAttrName(), strValue);
				if("255".equals(strValue)){
					this.setAttrHexValue("数组长度不合理");
					//CacheUtils.put(this.getAttrName(), "0");
					//cacheUtil.setKeyValue(this.getAttrName(), "0");
					assembleCache.put(this.getAttrName(), "0");
				}
			}
		} catch (Exception e) {
			LoggerOperator.err(LOG, "item error! name="+name, e);
		}
	}
	
	@Override
	public void procAttrHexStr() {
		int len = Integer.valueOf(this.element.attributeValue("length"));
		byte[] eleBytes = ConfigTool.subBytes(this.receivedContent, this.elementIndex.index, len);
		this.setAttrHexStr(CommFunction.byteArrayToHexStr(eleBytes));
	}
}
