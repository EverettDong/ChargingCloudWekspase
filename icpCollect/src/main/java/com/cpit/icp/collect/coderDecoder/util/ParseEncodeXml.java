package com.cpit.icp.collect.coderDecoder.util;

//import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cpit.icp.dto.collect.coderDecoder.DataContentDto;

public class ParseEncodeXml {

	private final static String CONF_FILE_PATH = ".\\conf\\Encode.xml";
	private static final String LOG = ParseEncodeXml.class.getName();
	private Map<String, List<DataContentDto>> allOrderdata = null;

	public ParseEncodeXml() {
		this.allOrderdata = new HashMap<String, List<DataContentDto>>();
	}

	public Map<String, List<DataContentDto>> ParseXmlFie() throws IOException {

		SAXReader sax = new SAXReader();
		Document document = null;
		try {
			// document = sax.read(CONF_FILE_PATH);
			document = sax.read(ParseDecodeXml.class
					.getResourceAsStream("/conf/Encode.xml"));
		} catch (Exception e) {
			LoggerOperator.err(LOG, "read document error!");
		}
		Element rootEle = document.getRootElement();
		for (Iterator iter = rootEle.elementIterator(); iter.hasNext();) {
			Element OrderEle = (Element) iter.next();
			String sOrder = OrderEle.attributeValue("name");
			List<DataContentDto> allDatadto = new ArrayList<DataContentDto>();
			try {
				for (Iterator iterInner = OrderEle.elementIterator(); iterInner
						.hasNext();) {
					DataContentDto dataContentDto = new DataContentDto();
					Element ParameterEle = (Element) iterInner.next();
					String strname = ParameterEle.attributeValue("name");
					String strtype = ParameterEle.attributeValue("type");
					String strSize = ParameterEle.attributeValue("size");
					String strValue = ParameterEle.attributeValue("value");
					dataContentDto.setParaName(strname);
					dataContentDto.setParaSize(strSize);
					dataContentDto.setParaType(strtype);
					dataContentDto.setValue(strValue);
					allDatadto.add(dataContentDto);
				}
			} catch (Exception ex) {
				LoggerOperator.err(LOG, "parse xml error!");
			}
			allOrderdata.put(sOrder, allDatadto);
		}
		return allOrderdata;
	}

	// 获取报文参数组；
	public Map<String, List<DataContentDto>> getOrderDataContent() {
		return this.allOrderdata;
	}

}
