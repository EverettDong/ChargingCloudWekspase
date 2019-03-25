package com.cpit.icp.collect.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.uiSend.UiSendDto;
import com.cpit.icp.dto.common.ResultInfo;

@RestController
@RequestMapping(method={RequestMethod.POST})
public class UpXMLConfigData {
	private final static Logger logger = LoggerFactory.getLogger(UpXMLConfigData.class);
	@Autowired CacheUtil cacheUtil;
	
	//versionName 3.4/3.5 
	@RequestMapping(value = "/upProcessXml")
	public ResultInfo upProcessXml(@RequestParam(value = "versionName") String versionName ) {
		logger.info("upProcessXml "+versionName);
		cacheUtil.delProcConfigXml(versionName);
		return new ResultInfo(ResultInfo.OK);
	}
	
	
	@RequestMapping(value = "/upParseXml")
	public ResultInfo upParseXml(@RequestParam(value = "versionName") String versionName ) {
		logger.info("upParseXml "+versionName);
		cacheUtil.delParseMsgXml(versionName);
		return new ResultInfo(ResultInfo.OK);
	}
	
	
}
