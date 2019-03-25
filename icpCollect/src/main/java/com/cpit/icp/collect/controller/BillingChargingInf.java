package com.cpit.icp.collect.controller;

import static com.cpit.icp.dto.common.ErrorMsg.ERR_BIZ_ERROR;
import static com.cpit.icp.dto.common.ResultInfo.FAIL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.service.UnionChargeProcessImp;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.MonRechargeRecordReplyDto;
import com.cpit.icp.dto.common.ErrorMsg;
import com.cpit.icp.dto.common.ResultInfo;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * interface for billing
 * @author zhangqianqian
 *
 */
@Api(tags = "计费信息接口")
@RestController
@RequestMapping(method={RequestMethod.POST})
public class BillingChargingInf {
	private final static Logger logger = LoggerFactory.getLogger(BillingChargingInf.class);
	@Autowired UnionChargeProcessImp chargeProcessImp;
	@Autowired MonDBService monDBService;
	//查询充电记录
	@ApiOperation(value = "查询充电信息", response=ResultInfo.class)
	@RequestMapping(value = "/queryChargeRecordMsg")
	public ResultInfo queryChargeRecordMsg(
			@ApiParam(value="设备编码") @PathVariable(name="deviceNo",required=true) String deviceNo,
			@ApiParam(value="卡号") @PathVariable(name="cardId",required=true) String cardId,
			@ApiParam(value="开始充电时间") @PathVariable(name="startTime",required=true) String startTime,
			@ApiParam(value="结束充电时间") @PathVariable(name="endTime",required=true) String endTime,
			@ApiParam(value="中心交易流水") @PathVariable(name="platTransFlowNum",required=true) String platTransFlowNum) {
		ResultInfo result = new ResultInfo();
		MonRechargeRecordDto dto = monDBService.queryRechargeRecord(deviceNo, cardId, startTime, platTransFlowNum);
		if(dto!=null) {
			result.setData(dto);
			result.setResult(ResultInfo.OK);
		}else {
			result.setResult(ResultInfo.FAIL);
		}
	
		return result;
	}
	@HystrixCommand(
	        fallbackMethod = "fallbackSendCharingMsg", 
	        groupKey = "collect", commandKey = "sendCharingMsg", 
	        commandProperties = {
	            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")
	        }
)
	@ApiOperation(value = "下发计费信息", response=ResultInfo.class)
	@RequestMapping(value = "/sendCharingMsg")
	public ResultInfo sendCharingMsg(@ApiParam(value="计费信息") @RequestBody MonRechargeRecordReplyDto monRechargeRecordReplyDto) {
		logger.info("sendCharingMsg begins.");
		boolean send =chargeProcessImp.sendCharingMsg(monRechargeRecordReplyDto);
		ResultInfo result = new ResultInfo();
		if(send) {
			result.setData(ResultInfo.OK);
		}else {
			result.setData(ResultInfo.FAIL);
		}
	
		return result;
	}
	private ResultInfo fallbackSendCharingMsg(MonRechargeRecordReplyDto monRechargeRecordReplyDto) {
	       return new ResultInfo(FAIL, new ErrorMsg(ERR_BIZ_ERROR, "time out"));
	} 
	}
