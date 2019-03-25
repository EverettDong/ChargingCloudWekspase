package com.cpit.icp.collect.facade;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cpit.common.Dispatcher;
import com.cpit.common.JsonUtil;
import com.cpit.icp.dto.common.ResultInfo;
import com.cpit.icp.dto.resource.BrBatterycharge;

@Service
public class ResourceFacade {
    private final static Logger logger = LoggerFactory.getLogger(ResourceFacade.class);

    @Value("${icp.resource.url}")
    private String resourceURL;

    @Value("${charging.pile.address}")
    private String chargingAddress;

    @Autowired
    private RestTemplate resTemplate;

    @Autowired
    private RestTemplate chargingTemplate;

    /**
     * 调用资源接口，返回充电桩归属充电站、充电桩类型。
     *
     * @param deviceNo
     * @return
     */
    public ResultInfo getDeviceNoInfo(String deviceNo) {
        logger.debug("call outer inf ,getDeviceNoInfo " + deviceNo);
        try {
            String url = chargingAddress + "/getLinkedBrBatterychargeInfo/-1/0";

            //BrBatterycharge bb = new BrBatterycharge();
            //ChargePileDetail chargePileDetail = new ChargePileDetail();
            //chargePileDetail.setSn(deviceNo);
            //bb.setBatteryCode(deviceNo);
            //String param;
            //param = JsonUtil.beanToJson(chargePileDetail);
            return (ResultInfo) new Dispatcher(chargingTemplate).doPost(url, ResultInfo.class, deviceNo);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error("error in addBrCardDetailInfo", e);
            return new ResultInfo(ResultInfo.FAIL, e.getMessage());
        }

    }
}
