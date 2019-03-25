package com.cpit.icp.collect.msgProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.facade.HlhtFacade;
import com.cpit.icp.dto.common.ResultInfo;

@Service
public class CallHlhtInf {
	 private final static Logger logger = LoggerFactory.getLogger(CallHlhtInf.class);
	@Autowired HlhtFacade hlhtFacade;

	
	public boolean pushStartChargeResult(String StartChargeSeq,String StartChargeSeqStat,String ConnectorID,
		String startTime,String IdentCode) {
		logger.info("pushStartChargeResult");
		ResultInfo result = hlhtFacade.pushStartChargeResult(StartChargeSeq, StartChargeSeqStat, ConnectorID, startTime, IdentCode);
	if(result.getResult()==ResultInfo.OK) {
		logger.debug("push ok");
		return true;
	}else {
		logger.debug("push failed "+result.getErrorMsg().toString());
		return false;
	}
	}
	
	
	public boolean pushStopChargeResult(String StartChargeSeq,String StartChargeSeqStat,String ConnectorID,
			String SuccStat,String FailReason) {
		logger.info("pushStopChargeResult");
		ResultInfo result = hlhtFacade.pushStopChargeResult(StartChargeSeq, StartChargeSeqStat, ConnectorID, SuccStat, FailReason);
	if(result.getResult()==ResultInfo.OK) {
		logger.debug("push ok");
		return true;
	}else {
		logger.debug("push failed "+result.getErrorMsg().toString());
		return false;
	}
	}
}
