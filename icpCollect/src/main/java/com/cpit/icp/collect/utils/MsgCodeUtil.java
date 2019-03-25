package com.cpit.icp.collect.utils;

import static com.cpit.icp.collect.utils.Consts.FILENAME_V_34;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.msgProcess.ConstructData;
import com.cpit.icp.collect.msgProcess.MsgMap;
import com.cpit.icp.collect.msgProcess.MsgProcessInterface;
import com.cpit.icp.collect.msgProcess.ParseMsgProcessorXml;
import com.cpit.icp.collect.msgProcess.ParseMsgStoreXml;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.MonBatteryModuleParamDto;
import com.cpit.icp.dto.collect.MonRechargingInfoDto;

@Component
public class MsgCodeUtil {
	@Autowired CacheUtil cacheUtil;
	private final static Logger logger = LoggerFactory.getLogger(MsgCodeUtil.class);

	/**
	 * 
	 * @param mesCode
	 * @return 1 st;2 ts;
	 */
public int getMessTypeByCode(String mesCode,String version) {
	int messType= 0;
	Map<String,Integer> messT = MsgMap.getInstance().getCodeType(version);
	if(messT.containsKey(mesCode)) {
		messType = messT.get(mesCode);
		logger.debug("messType "+ mesCode + " "+messType);
		return messType;
	}else {
		logger.debug("messcode is wrong");
		return 0;
	}

	
}











public static void main(String[] args) {

}
}
