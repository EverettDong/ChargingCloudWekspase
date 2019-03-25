/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.collect.msgInterface
 * 文件名：MsgReceived.java
 * 版本信息�?.0.0
 * 日期�?018�?�?�?
 * Copyright (c) 2018普天信息技术有限公�?版权所�?
 */
package com.cpit.icp.collect.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.cpit.common.SpringContextHolder;
import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import com.cpit.icp.collect.msgProcess.ConstructData;
import com.cpit.icp.collect.msgProcess.MsgDataConvertor;
import com.cpit.icp.collect.msgProcess.MsgMap;
import com.cpit.icp.collect.msgProcess.MsgQueue;
import com.cpit.icp.dto.collect.MonChargingStatus4App;
import com.cpit.icp.dto.collect.MonSequenceDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.common.ResultInfo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import com.cpit.icp.collect.msgProcess.MsgRecievedObservable;
import com.cpit.icp.collect.msgProcess.MsgSendThread;
import com.cpit.icp.collect.utils.cache.CacheUtil;


/**
 * 类名称：MsgReceived
 * 类描述：
 * 创建人：zhangqianqian
 * 创建时间上午10:37:24
 * 修改人：
 * 修改时间
 * 修改备注
 * @version 1.0.0
 */
@Api(tags = "msg接收接口")
@RestController
@RequestMapping(method={RequestMethod.POST})
public class MsgReceived {
	private final static Logger logger = LoggerFactory.getLogger(MsgReceived.class);

	@Autowired MsgDataConvertor msgDataConvertor;
	@Autowired GateRouteInfoMgmt gateRouteInfoMgmt;
	@Autowired CacheUtil cacheUtil;

	/**
	 
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
    @ApiOperation(value = "接收前置网关上行上报数据", response=ResultInfo.class)
	@RequestMapping(value = "/msgRecievedPreGate")
	public ResultInfo msgRecievedPreGate(
			@RequestBody String tsData
			){
    	logger.info("msgRecievedPreGate.");
    	logger.info("msgRecievedPreGate data "+tsData);
    	ResultInfo result = new ResultInfo();
		MsgOfPregateDto obj = msgDataConvertor.preGateStrToObj(tsData);
		if(obj ==null) {
			logger.error("pregateStrtoObj is null");
			result.setResult(0);
			return result;
		}
		MsgRecievedObservable.getInstance().addData(obj);
    	
  
		
		result.setResult(1);
		return result;
		
	}
    @ApiOperation(value = "断连设备上报接口", response=ResultInfo.class)
  	@RequestMapping(value = "/reportDisConnection")
	public ResultInfo reportDisConnection( 
			@RequestBody String deviceNo) {
		
    	logger.info("disConnection DeviceNo: "+deviceNo);
    	boolean b =gateRouteInfoMgmt.deviceDisConn(deviceNo);
    	logger.debug("disConnection "+b);
    	setCPStatusDis(deviceNo);
    	
    	logger.info("reset redis sequenceDto");
        cacheUtil.initSequenceData(deviceNo);
    	
    	ResultInfo result = new ResultInfo();
		result.setResult(1);
		return result;
	}
    
   
	/**
	 * 断连的时候，enable isTakeUp isOnline
	 * @param deviceNo
	 */
    private void  setCPStatusDis(String deviceNo) {
    	MonChargingStatus4App statusData = cacheUtil.getStatusData(deviceNo);
    	statusData.setEnabled("no");
    	statusData.setIsOnline("no");
    	statusData.setIsTakeUp("no");
    	
    	cacheUtil.setStatusData(statusData);
    }

	
}
