package com.cpit.icp.collect.coderDecoder.common.configurable;

import java.util.Iterator;
import java.util.Map;

import org.dom4j.Element;

import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;
import com.cpit.icp.collect.coderDecoder.util.configurable.ConfigTool;

/**
 * 枚举类型
 * 
 * 结构固定，包含enumvalue和enum
 * 
 * @author maming
 *
 */
public class enumitem extends AssembleClass {
	
	private static final String LOG = enumitem.class.getName();
	
	private Element findedEnumElement = null;
	
	public enumitem(Element element, StringBuffer buffer,StringBuffer errbuffer, byte[] dataContent, String ChineseEncoding,ParseIndex pos,Map<String,Object> cacheMap) {
		super(element, buffer, errbuffer,dataContent,ChineseEncoding, pos,cacheMap);
	}

	@Override
	public void procAttrMeanings() {
		if(findedEnumElement != null){
			this.setAttrMeanings(findedEnumElement.attributeValue("desc"));
		}else{
			this.setAttrMeanings("状态的值不在范围内");
		}
	}

	@Override
	public void procAttrHexValue() {
		int len = Integer.valueOf(this.element.attributeValue("length"));
		String name = this.element.attributeValue("name");
		String type = this.element.attributeValue("type");
		byte[] eleBytes = ConfigTool.subBytes(this.receivedContent, this.elementIndex.index, len);
		
		String enumKey="";
		
		if(type==null){
			eleBytes = CommFunction.reserveByteArray(eleBytes);
			enumKey = CommFunction.byteArrayToInt(eleBytes)+"";
		}else{
			if(type.equals("hex")){
				enumKey = CommFunction.byteArrayToHexStr(eleBytes);
			}
			if(type.equals("ascii")){
				enumKey = new String(CommFunction.bytesToAscii(eleBytes));
			}
			if(type.equals("int")){
				eleBytes = CommFunction.reserveByteArray(eleBytes);
				enumKey = CommFunction.byteArrayToInt(eleBytes)+"";
			}
		}
		
		boolean isFindEnumKey = false;
		try {
			for (Iterator iterInner = element.elementIterator("enumvalue"); iterInner.hasNext();) {
				Element elementInner = (Element) iterInner.next();
				for (Iterator secondLevelInner = elementInner.elementIterator("enum"); secondLevelInner.hasNext();) {
					Element elementSecondLevelInner = (Element) secondLevelInner.next();
					String v = elementSecondLevelInner.getStringValue();
					if (enumKey.equals(v)) {
						isFindEnumKey = true;
						findedEnumElement = elementSecondLevelInner;
						this.setAttrHexValue(elementSecondLevelInner.attributeValue("desc"));
						break;
					}
				}
			}
		} catch (Exception e) {
			LoggerOperator.err(LOG, "enumitem procAttrHexValue error! name="+name, e);
		}
		if(!isFindEnumKey){
			this.setAttrHexValue("状态的值不在范围内");
		}
		//元素游标后移
		this.elementIndex.index = this.elementIndex.index + len;

	}

	@Override
	public void procAttrHexStr() {
		int len = Integer.valueOf(this.element.attributeValue("length"));
		byte[] eleBytes = ConfigTool.subBytes(this.receivedContent, this.elementIndex.index, len);
		this.setAttrHexStr(CommFunction.byteArrayToHexStr(eleBytes));
	}
}
