package com.cpit.icp.collect.msgProcess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cpit.common.SpringContextHolder;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.cache.CacheUtil;

import static com.cpit.icp.collect.utils.Consts.VERSION_3_1;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_2;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import static com.cpit.icp.collect.utils.Consts.FILENAME_V_34;
import static com.cpit.icp.collect.utils.Consts.FILENAME_V_35;

/**
 * parse msg ProcessorXML 类名称：ParseMsgProcessorXml 类描述： 创建人：zhangqianqian
 * 创建时间：2018年8月2日 下午3:57:37 修改人： 修改时间： 修改备注：
 * 
 * @version 1.0.0
 * 
 */

public class ParseMsgProcessorXml {
	private final static Logger logger = LoggerFactory.getLogger(ParseMsgProcessorXml.class);
	
	

	//private Map<String,String> codePairTS = MsgMap.getInstance().getCodePairTS();
	//private Map<String,List<String>> stNeedReply = MsgMap.getInstance().getStNeedReply();
	//private Map<String,Integer> codeType = MsgMap.getInstance().getCodeType();
	private Map<String,Map<String,String>> codePairTSVer = MsgMap.getInstance().getCodePairTSVer();
	private Map<String,Map<String,Integer>> codeTypeVer = MsgMap.getInstance().getCodeTypeVer();
	private static Map<String, Map<List<String>, MsgProcessInterface>> processVersionMap;
	private static ParseMsgProcessorXml parseMsgProcessorXml = null;
    private List<String> tsNeedStore = MsgMap.getInstance().getTsNeedStore();
	private CacheUtil cacheUtil = SpringContextHolder.getBean(CacheUtil.class);
    public synchronized static ParseMsgProcessorXml getInstance() {
		if (parseMsgProcessorXml == null) {
			parseMsgProcessorXml = new ParseMsgProcessorXml();
		}
		return parseMsgProcessorXml;

	}


	public ParseMsgProcessorXml() {
		processVersionMap = new ConcurrentHashMap<String, Map<List<String>, MsgProcessInterface>>();
		
		processVersionMap.put(VERSION_3_1, initMsgProcessMap(VERSION_3_1));
		processVersionMap.put(VERSION_3_2, initMsgProcessMap(VERSION_3_2));
		processVersionMap.put(VERSION_3_4, initMsgProcessMap(VERSION_3_4));
		processVersionMap.put(VERSION_3_5, initMsgProcessMap(VERSION_3_5));	
	
	
	}

	public Map<List<String>, MsgProcessInterface> getVersionMap(String version) {
		//return cacheUtil.getMsgProcType(version);
		return processVersionMap.get(version);
	}

	private  Map<List<String>, MsgProcessInterface> initMsgProcessMap(String version) {
		Map<List<String>, MsgProcessInterface> processMap = new HashMap<List<String>, MsgProcessInterface>();
	//	responseMap = MsgMap.getInstance().getResponseMap(version);
		Map<String,String> codePairTS = new HashMap<String,String>();
		Map<String,Integer> codeType = new HashMap<String,Integer>();
		
		Document document = null;
		
		

			//document = sax.read(ParseMsgProcessorXml.class.getResourceAsStream("/conf/msgProcessor" + versionFile + ".xml"));
		
		document=cacheUtil.getProcConfigXml(version);
		if(document==null) {
			logger.error("processConfig Xml is null");
			return processMap;
		}
		
		Element rootEle = document.getRootElement();
		for (Iterator iter = rootEle.elementIterator(); iter.hasNext();) {
			Element OrderEle = (Element) iter.next();
			String className = OrderEle.attributeValue("id");
			String classPath = OrderEle.attributeValue("name");
			String code = OrderEle.attributeValue("code");
			String[] codes = code.split(",");
			List<String> codeList = new ArrayList<String>();
			for (String s : codes) {
				codeList.add(s);
			}

			MsgProcessInterface msgProceeImp = null;
			// String msgProcessImpPath = null;
			try {
				Class cls =  Class.forName(classPath);
				msgProceeImp = (MsgProcessInterface) cls.newInstance();
				
				for (Iterator iterInner = OrderEle.elementIterator(); iterInner.hasNext();) {

					Element ParameterEle = (Element) iterInner.next();
					String send = ParameterEle.attributeValue("send");
					String response = ParameterEle.attributeValue("response");
                    String reply = ParameterEle.attributeValue("reply");

					
					
					String sendSub = send.substring(send.indexOf("x") + 1, send.length());
					String responseSub = response.substring(send.indexOf("x") + 1, response.length());
					codePairTS.put(response, send);
					codeType.put(response, 2);
					codeType.put(send, 1);

				}
			} catch (Exception ex) {
				logger.error(ex.getMessage());
			}

			processMap.put(codeList, msgProceeImp);
			
			//stNeedReply.put(version, needReplyCode);

		}
		
		codePairTSVer.put(version, codePairTS);
		
		cacheUtil.setCodePairTS(version, codePairTS);
	codeTypeVer.put(version, codeType);
	
		return processMap;

	}
	
	
	
}
