package com.cpit.icp.collect.facade;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cpit.common.Dispatcher;
import com.cpit.common.JsonUtil;
import com.cpit.icp.dto.billing.baseconfig.BfAcctBalanceT;
import com.cpit.icp.dto.billing.finance.BfCenterTransT;
import com.cpit.icp.dto.common.ResultInfo;


@Service
public class BillingFacade {
    private final static Logger logger = LoggerFactory.getLogger(BillingFacade.class);

    @Autowired
    private RestTemplate billingTemplate;

    @Value("${icp.billing.url}")
    private String billURL;

    @Value("${charging.pile.address}")
    private String chargingAddress;

    @Autowired
    private RestTemplate chargingTemplate;

    /**
     * @Author donghaibo
     * @Description //调用账务接口查询卡余额
     * @Date
     * @Param
     * @return
     **/
    public ResultInfo accountAuthen(String cardNo) {
        try {
            logger.info("call billing inf,accountAuthen " + cardNo);

            String url = chargingAddress + "/bil/Charging_authentication/";

            return (ResultInfo) new Dispatcher(chargingTemplate).doPost(url, ResultInfo.class, cardNo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("call billing inf,accountAuthen error", e);
            return new ResultInfo(ResultInfo.FAIL, e.getMessage());
        }
    }


    /**
     * @Author donghaibo
     * @Description //调用账务接口创建订单
     * @Date
     * @Param
     * @return
     **/
    public ResultInfo insertBfCenterTransT(BfCenterTransT dto) {
        try {
            logger.debug("call billing inf,insertBfCenterTransT ");

            String url = chargingAddress + "/bil/insertBfCenterTransT/";
            String param = JsonUtil.beanToJson(dto);
            new Dispatcher(chargingTemplate).doPost(url, ResultInfo.class, param);
            return new ResultInfo(ResultInfo.OK);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("call billing inf,insertBfCenterTransT error", e);
            return new ResultInfo(ResultInfo.FAIL, e.getMessage());
        }
    }

    public ResultInfo queryAccountBalance(String cardId) {
        try {
            logger.debug("call billing inf,queryAccountBalance ");

            String url = chargingAddress + "/bil/recharge/getAcctTotalBalance";

            return (ResultInfo) new Dispatcher(chargingTemplate).doPost(url, ResultInfo.class, cardId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("call billing inf,queryAccountBalance error", e);
            return new ResultInfo(ResultInfo.FAIL, e.getMessage());
        }
    }
}
