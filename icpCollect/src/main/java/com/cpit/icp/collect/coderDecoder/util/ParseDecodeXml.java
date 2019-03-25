package com.cpit.icp.collect.coderDecoder.util;

//import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import java.util.Set;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.cpit.icp.dto.collect.coderDecoder.DataContentDto;
//import com.util.logmgt.CommandParser;
public class ParseDecodeXml {
	
	private final static String CONF_FILE_PATH = ".\\conf\\test.xml";
	private static final String LOG=ParseDecodeXml.class.getName();
	private Map<String,List<DataContentDto>> allOrderdata=null;	
	public ParseDecodeXml() {
		this.allOrderdata=new HashMap<String,List<DataContentDto>>();
	}

	
	/*public Map<String,List<DataContentDto>> ParseXmlFie()throws IOException{
		
		SAXReader sax = new SAXReader();
		Document document = null;
		try {
			//document = sax.read(CONF_FILE_PATH);
			document =sax.read(ParseDecodeXml.class.getResourceAsStream(
					"/conf/Decode.xml"));
		} catch (Exception e) {
			LoggerOperator.err(LOG,"read document error!");
		}
		Element rootEle = document.getRootElement();
		for(Iterator iter=rootEle.elementIterator();iter.hasNext();){
			  Element OrderEle=(Element)iter.next();
			  String sOrder=OrderEle.attributeValue("name");	
			  List<DataContentDto> allDatadto=new ArrayList<DataContentDto>();
		  try{
			for(Iterator iterInner=OrderEle.elementIterator();iterInner.hasNext();){
				DataContentDto dataContentDto=new DataContentDto();
				Element ParameterEle=(Element)iterInner.next();
				String strname = ParameterEle.attributeValue("name");
				String strtype = ParameterEle.attributeValue("type");		
				String strSize = ParameterEle.attributeValue("size");
				dataContentDto.setParaName(strname);
				dataContentDto.setParaSize(strSize);
				dataContentDto.setParaType(strtype);
				allDatadto.add(dataContentDto);
			}
		  }catch(Exception ex){
			  LoggerOperator.err(LOG,"parse xml error!");
		 }
		  allOrderdata.put(sOrder, allDatadto);
		}
          return allOrderdata;
	}*/
	/*public Map<String, List<DataContentDto>> ParseXmlFie() throws IOException {

		SAXReader sax = new SAXReader();
		Document document = null;
		try {
			// document = sax.read(CONF_FILE_PATH);
			document = sax.read(ParseDecodeXml.class
					.getResourceAsStream("/conf/Decode.xml"));
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
					String str=ParameterEle.attributeValue("name");
					String strsize=ParameterEle.attributeValue("size");
					if(ParameterEle.attributeValue("name").equalsIgnoreCase("list"))
					{   
						for(int irepeat=0;irepeat<Integer.parseInt(strsize);irepeat++){
						for(Iterator itersubpara=ParameterEle.elementIterator();itersubpara.hasNext();){
							DataContentDto subdataContentDto = new DataContentDto();	
							Element subParaterEle=(Element) itersubpara.next();
							int icount=irepeat+1;
							String srcstrname=subParaterEle.attributeValue("name");							
							String strname = subParaterEle.attributeValue("name")+icount;
							String strtype = subParaterEle.attributeValue("type");
							String strSize = subParaterEle.attributeValue("size");					
							subdataContentDto.setParaName(strname);
							subdataContentDto.setParaSize(strSize);
							subdataContentDto.setParaType(strtype);
							allDatadto.add(subdataContentDto);
						}
						}
					}
					else {
					  String strname = ParameterEle.attributeValue("name");
					  String strtype = ParameterEle.attributeValue("type");
					  String strSize = ParameterEle.attributeValue("size");					
					  dataContentDto.setParaName(strname);
					  dataContentDto.setParaSize(strSize);
					  dataContentDto.setParaType(strtype);
					  allDatadto.add(dataContentDto);
					}
				}
			} catch (Exception ex) {
				LoggerOperator.err(LOG, "parse xml error!");
			}
			allOrderdata.put(sOrder, allDatadto);
		}
		return allOrderdata;
	}*/
	
	public Map<String, List<DataContentDto>> ParseXmlFie() throws IOException {

		SAXReader sax = new SAXReader();
		Document document = null;
		try {
			// document = sax.read(CONF_FILE_PATH);
			document = sax.read(ParseDecodeXml.class
					.getResourceAsStream("/conf/Decode.xml"));
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
					String str=ParameterEle.attributeValue("name");
					String strsize=ParameterEle.attributeValue("size");
					if(ParameterEle.attributeValue("name").equalsIgnoreCase("list"))
					{   
						for(int irepeat=0;irepeat<Integer.parseInt(strsize);irepeat++){
						for(Iterator itersubpara=ParameterEle.elementIterator();itersubpara.hasNext();){
							DataContentDto subdataContentDto = new DataContentDto();	
							Element subParaterEle=(Element) itersubpara.next();	
							String sublistsize=subParaterEle.attributeValue("size");
							if(subParaterEle.attributeValue("name").equalsIgnoreCase("sublist")){
								for(int inextrepeat=0;inextrepeat<Integer.parseInt(sublistsize);inextrepeat++){
									for(Iterator iternextsubpara=subParaterEle.elementIterator();iternextsubpara.hasNext();){
										DataContentDto nextsubdataContentDto = new DataContentDto();	
										Element nextsubParaterEle=(Element) iternextsubpara.next();
										int inextcount=inextrepeat+1;																
										String strname = nextsubParaterEle.attributeValue("name")+inextcount;
										String strtype = nextsubParaterEle.attributeValue("type");
										String strSize = nextsubParaterEle.attributeValue("size");					
										nextsubdataContentDto.setParaName(strname);
										nextsubdataContentDto.setParaSize(strSize);
										nextsubdataContentDto.setParaType(strtype);
										allDatadto.add(nextsubdataContentDto); 
									}
								}
							}
							else{
								int icount=irepeat+1;
								String strname = subParaterEle.attributeValue("name")+icount;
								String strtype = subParaterEle.attributeValue("type");
								String strSize = subParaterEle.attributeValue("size");					
								subdataContentDto.setParaName(strname);
								subdataContentDto.setParaSize(strSize);
								subdataContentDto.setParaType(strtype);
								allDatadto.add(subdataContentDto);
							}
						}
						}
					}
					else {
					  String strname = ParameterEle.attributeValue("name");
					  String strtype = ParameterEle.attributeValue("type");
					  String strSize = ParameterEle.attributeValue("size");					
					  dataContentDto.setParaName(strname);
					  dataContentDto.setParaSize(strSize);
					  dataContentDto.setParaType(strtype);
					  allDatadto.add(dataContentDto);
					}
				}
			} catch (Exception ex) {
				LoggerOperator.err(LOG, "parse xml error!");
			}
			allOrderdata.put(sOrder, allDatadto);
		}
		return allOrderdata;
	}
	//获取报文参数组；
	public Map<String, List<DataContentDto>> getOrderDataContent() {
		return this.allOrderdata;
	}
	
}

