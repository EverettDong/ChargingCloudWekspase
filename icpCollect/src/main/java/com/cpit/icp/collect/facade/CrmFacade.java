package com.cpit.icp.collect.facade;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cpit.common.Dispatcher;
import com.cpit.icp.dto.common.ResultInfo;

@Service
public class CrmFacade {
	private final static Logger logger = LoggerFactory.getLogger(CrmFacade.class);

	@Value("${icp.crm.url}")
	private String crmURL;
	@Autowired
	private RestTemplate crmTemplate;

	@Value("${charging.pile.address}")
	private String chargingAddress;

	@Autowired
	private RestTemplate chargingTemplate;

	public ResultInfo getUserByCardId(String cardId) {
		try {
			logger.debug("call crm inf,getUserByCardId " + cardId);
			// String url = crmURL+"/getUserByCardId";
			String url = chargingAddress + "/getUserByCardId";
			return (ResultInfo) new Dispatcher(chargingTemplate).doPost(url, ResultInfo.class, cardId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("call crm inf getUserByCardId error, " + e.getMessage());
			ResultInfo result = new ResultInfo();
			result.setResult(ResultInfo.FAIL);
			return result;
		}

	}

	public ResultInfo getCarVinByNo(String carNo) {
		try {
			logger.debug("getCarVinByNo restful begins.");
			String url = chargingAddress + "/getCarVinByNo";
			return (ResultInfo) new Dispatcher(chargingTemplate).doPost(url, ResultInfo.class, carNo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("call crm inf  getCarVinByNo error, " + e.getMessage());
			ResultInfo result = new ResultInfo();
			result.setResult(ResultInfo.FAIL);
			return result;
		}

	}
}
