package com.cpit.icp.collect.msgProcess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.common.SpringContextHolder;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.coderDecoder.UIDataContentDto;
import com.cpit.icp.dto.collect.msgProcess.ResponseDataDto;
//import com.cpit.testplatform.modules.encode.util.ParseDecodeXml;
//import com.cpit.testplatform.modules.encode.util.ParseDesignCharge;

public class ParseReponseXml {
	private final static Logger logger = LoggerFactory.getLogger(ParseReponseXml.class);
	private Map<String, List<ResponseDataDto>> allOrderdata = null;
	private String codeVersion = null;
	
	public ParseReponseXml(String codeVersion) {
		this.codeVersion = codeVersion;
		this.allOrderdata = new HashMap<String, List<ResponseDataDto>>();
		
	}
	
	public Map<String, List<ResponseDataDto>> ParseXmlFie(Document document) throws IOException {
		Element firstEle = null; 
		
	if(document==null) {
		logger.error("parseResponseError,result is null");
		return allOrderdata;
	}
		Element rootEle = document.getRootElement();
		for(Iterator iter = rootEle.elementIterator(); iter.hasNext();) {
			Element OrderEle = (Element) iter.next();
			String messageMark = OrderEle.attributeValue("direction");
			if(messageMark.equalsIgnoreCase("ts")){
				continue;
			}
			String sOrder = OrderEle.attributeValue("id");
			List<ResponseDataDto> allDatadto = new ArrayList<ResponseDataDto>(); 
			if(!"".equals(sOrder)){
				for (Iterator iter1 = OrderEle.elementIterator(); iter1.hasNext();) {
					ResponseDataDto dataContentDto = new ResponseDataDto();
					    Element ParameterEle = (Element) iter1.next();
						String strname = ParameterEle.attributeValue("name");
						String strValue = ParameterEle.attributeValue("value");
						String strtype = ParameterEle.attributeValue("type");
					String strsize =  ParameterEle.attributeValue("size");
					
						String strsrc = ParameterEle.attributeValue("src");
						String strsrcPos = ParameterEle.attributeValue("srcPos");
						String strsrcObj = ParameterEle.attributeValue("srcObj");
						
						dataContentDto.setName(strname);
						dataContentDto.setOrder(sOrder);
						dataContentDto.setValue(strValue);
						dataContentDto.setSize(strsize);
						dataContentDto.setType(strtype);
						dataContentDto.setSrc(strsrc);
						dataContentDto.setSrcObj(strsrcObj);
						dataContentDto.setSrcPos(strsrcPos);
						
						allDatadto.add(dataContentDto);
						if(ParameterEle.getName().equals("array")){  
					    	for(Iterator iter2 = ParameterEle.elementIterator(); iter2.hasNext();){
					    		ResponseDataDto dataContentDto1 = new ResponseDataDto();
					    		Element   SonItemEle = (Element) iter2.next();
					    		strname = SonItemEle.attributeValue("name");
								strValue= SonItemEle.attributeValue("value");
								strtype = SonItemEle.attributeValue("type");
								strsize =  ParameterEle.attributeValue("size");
								
								 strsrc = ParameterEle.attributeValue("src");
								 strsrcPos = ParameterEle.attributeValue("srcPos");
								 strsrcObj = ParameterEle.attributeValue("srcObj");
								
								dataContentDto1.setName(strname);
								dataContentDto1.setOrder(sOrder);
								dataContentDto1.setValue(strValue);
								dataContentDto1.setType(strtype);
								dataContentDto1.setSrc(strsrc);
								dataContentDto1.setSize(strsize);
								dataContentDto1.setSrcObj(strsrcObj);
								dataContentDto1.setSrcPos(strsrcPos);
								
								allDatadto.add(dataContentDto1);
					    	}  
					   }
					 	
				  } 
			 }
			allOrderdata.put(sOrder, allDatadto);
		}
		return allOrderdata;
	}
	

}
