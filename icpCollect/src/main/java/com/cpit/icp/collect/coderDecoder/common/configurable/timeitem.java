package com.cpit.icp.collect.coderDecoder.common.configurable;

import java.util.Map;

//import java.lang.reflect.Method;

import org.dom4j.Element;

import com.cpit.icp.collect.coderDecoder.util.ArraysN;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.LoggerOperator;
import com.cpit.icp.collect.coderDecoder.util.configurable.ConfigTool;
import com.cpit.icp.collect.coderDecoder.util.configurable.HexTool;
//import com.thinkgem.jeesite.common.utils.CacheUtils;

/**
 * @author maming
 * 时间类型元素解析类
 */
public class timeitem  extends AssembleClass{
	
	private static final String LOG = timeitem.class.getName();
	
	public timeitem(Element element, StringBuffer buffer,StringBuffer errbuffer, byte[] dataContent,String ChineseEncoding,ParseIndex index,Map<String,Object> cacheMap) {
		super(element, buffer,errbuffer,dataContent,ChineseEncoding,index,cacheMap);
	}
	
	/* 
	 * 处理属性含义
	 */
	@Override
	public void procAttrMeanings() {
		int len = Integer.valueOf(this.element.attributeValue("length"));
		byte[] bsrc = ConfigTool.subBytes(this.receivedContent, this.elementIndex.index,len );
		try {
			StringBuilder sBuilder = new StringBuilder();
			byte[] byear = new byte[2];
			ArraysN.copy(byear, 0, bsrc, 0, 2);
			byear = CommFunction.reserveByteArray(byear);
			int iyear = CommFunction.byteArrayToUnsignInt(byear);
			sBuilder.append(iyear);
			sBuilder.append("-");
			sBuilder.append(CommFunction.unsignByteToInt(bsrc[2]));
			sBuilder.append("-");
			sBuilder.append(CommFunction.unsignByteToInt(bsrc[3]));
			sBuilder.append(" ");
			sBuilder.append(CommFunction.unsignByteToInt(bsrc[4]));
			sBuilder.append("-");
			sBuilder.append(CommFunction.unsignByteToInt(bsrc[5]));
			sBuilder.append("-");
			sBuilder.append(CommFunction.unsignByteToInt(bsrc[6]));
			this.setAttrMeanings(sBuilder.toString());
		} catch (Exception e) {
			LoggerOperator.err(LOG, "procAttrMeanings error!", e);
		}
	}

	/* 
	 * 处理属性值
	 */
	@Override
	public void procAttrHexValue() {
		int len = Integer.valueOf(this.element.attributeValue("length"));
		byte[] bsrc = ConfigTool.subBytes(this.receivedContent, this.elementIndex.index,len );
		try {
			this.setAttrHexValue(HexTool.bytesToString(bsrc).toString().toUpperCase());
			//元素游标后移
			this.elementIndex.index = this.elementIndex.index + len;
		} catch (Exception e) {
			LoggerOperator.err(LOG, "procAttrHexValue error!", e);
		}
	}
	
	@Override
	public void procAttrHexStr() {
		int len = Integer.valueOf(this.element.attributeValue("length"));
		byte[] eleBytes = ConfigTool.subBytes(this.receivedContent, this.elementIndex.index, len);
		this.setAttrHexStr(CommFunction.byteArrayToHexStr(eleBytes));
	}
}
