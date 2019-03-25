package com.cpit.icp.collect.facade;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.cpit.common.Dispatcher;
import com.cpit.icp.dto.common.ResultInfo;

/**
 * 
 * @author zhangqianqian
 *
 */
@Service
public class HlhtFacade {
	 private final static Logger logger = LoggerFactory.getLogger(HlhtFacade.class);
	
	 @Value("${icp.gateway.url}")
	private String hlht_URL;
	
	@Autowired
	private RestTemplate hlhtTemplate;
public ResultInfo pushStartChargeResult(String StartChargeSeq,String StartChargeSeqStat,String ConnectorID,
		String startTime,String IdentCode) {
	try {
		logger.info("pushStartChargeResult " + StartChargeSeq);
		
		String url = hlht_URL + "/push_start_charge_result?StartChargeSeq=" + StartChargeSeq
				+"StartChargeSeqStat="+StartChargeSeqStat
				+"ConnectorID="+ConnectorID
				+"startTime="+startTime
				+"IdentCode="+IdentCode
				;
		return (ResultInfo) new Dispatcher(hlhtTemplate).doPost(url, ResultInfo.class, "");

	} catch (Exception e) {
		// TODO Auto-generated catch block
		logger.error("pushStartChargeResult " + e.getMessage());
		ResultInfo result = new ResultInfo();
		result.setResult(ResultInfo.FAIL);
		return result;
	}
	
}

public ResultInfo pushStopChargeResult(
		String StartChargeSeq,String StartChargeSeqStat,String ConnectorID,
		String SuccStat,String FailReason) {
	try {
		logger.info("pushStopChargeResult " + StartChargeSeq);
		
		String url = hlht_URL + "/push_start_charge_result?StartChargeSeq=" + StartChargeSeq
				+"StartChargeSeqStat="+StartChargeSeqStat
				+"ConnectorID="+ConnectorID
				+"SuccStat="+SuccStat
				+"FailReason="+FailReason
				;
		return (ResultInfo) new Dispatcher(hlhtTemplate).doPost(url, ResultInfo.class, "");

	} catch (Exception e) {
		// TODO Auto-generated catch block
		logger.error("pushStartChargeResult " + e.getMessage());
		ResultInfo result = new ResultInfo();
		result.setResult(ResultInfo.FAIL);
		return result;
	}
	
}
}
