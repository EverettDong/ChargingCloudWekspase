package com.cpit.icp.collect.msgProcess;

import cn.hutool.core.bean.BeanUtil;
import com.cpit.icp.dto.charging.ChargeCard;
import com.cpit.icp.dto.charging.ChargePileDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.cpit.common.JsonUtil;
import com.cpit.icp.collect.facade.BillingFacade;
import com.cpit.icp.collect.facade.CrmFacade;
import com.cpit.icp.collect.facade.ResourceFacade;
import com.cpit.icp.dto.billing.finance.BfCenterTransT;
import com.cpit.icp.dto.billing.recharge.RechargeBeginResponse;
import com.cpit.icp.dto.common.ResultInfo;
import com.cpit.icp.dto.resource.BrBatterycharge;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 调用外部接口
 *
 * @author zhangqianqian
 */
@Service
public class CallOuterInf {
    @Autowired
    BillingFacade billingFacade;
    @Autowired
    CrmFacade crmFacade;
    @Autowired
    ResourceFacade resFacade;
    private final static Logger logger = LoggerFactory.getLogger(CallOuterInf.class);


    /**
     * crm 比较密码
     *
     * @param cardId
     * @param password
     * @return
     */
    public boolean accountAuth(String cardId, String password) {
        logger.info("accountAuth begins" + cardId + " " + password);
        ChargeCard chargeCard = this.getCrmUserByCard(cardId);
        if (chargeCard == null) {
            logger.info("getCrmUserByCard is null");
            return false;
        }
        String userPass = chargeCard.getPassword();
        if (userPass.equalsIgnoreCase(password)) {
            return true;
        } else {
            return false;
        }

    }
    
    /**
     * @Author donghaibo
     * @Description //TODO
     * @Date  
     * @Param [cardId]
     * @return ChargeCard
     **/
    public ChargeCard getCrmUserByCard(String cardId) {
        logger.info("getCrmUserByCard begins");
        // String /getUserByCardId

        ResultInfo result = crmFacade.getUserByCardId(cardId);
        if (result.getResult() == ResultInfo.FAIL) {
            logger.info("getUserByCardId failed." + result.getErrorMsg());
            return null;
        } else {
            try {
                /*String json = JsonUtil.beanToJson(result.getData());
                ChargeCard chargeCard = (ChargeCard) JsonUtil.jsonToBean(json, ChargeCard.class);*/
                ChargeCard chargeCard = BeanUtil.toBean(result.getData(), ChargeCard.class);
                return chargeCard;
            } catch (Exception e) {
                logger.error("convert to obj ex: " + e.getMessage());
                return null;
            }


        }

    }

    /**
     * 获取车辆信息 for 0x65
     */
    public String getCarInfo(String carNo) {
        ResultInfo result = crmFacade.getCarVinByNo(carNo);
        if (result == null) {
            logger.info("getCarVin restful null");
            return null;
        }
        if (result.getResult() == ResultInfo.OK) {

            try {
                logger.info("getCarVin result ok");

                //	String json = JsonUtil.beanToJson(result.getData());

                String vin = (String) result.getData();

                return vin;


            } catch (Exception e) {
                logger.error("jsontoBean usercar get ex:", e);
                return null;
            }

        } else {
            logger.error("getCarVin restful failed.");
            return null;
        }

    }

    /**
     * 暂时不用，默认都合法
     *
     * @param code
     * @return
     */
    public boolean getBatteryInfo(String code) {
        return true;
    }


    public RechargeBeginResponse getAccountInfo(String cardNo) {
        logger.info("call billing inf: " + cardNo);
        ResultInfo result = billingFacade.accountAuthen(cardNo);
        if (result.getResult() == ResultInfo.OK) {
            logger.info("call billing inf response ok,convert RechargeBeginResponse obj");
            try {
                String json = JsonUtil.beanToJson(result.getData());

                RechargeBeginResponse response = (RechargeBeginResponse) JsonUtil.jsonToBean(json,
                        RechargeBeginResponse.class);

                // RechargeBeginResponse response = (RechargeBeginResponse)result.getData();
                return response;
            } catch (Exception e) {
                logger.error("convert RechargeBeginResponse obj failed");
                return null;
            }

        } else {
            logger.info("call billing inf response,failed.");
            return null;
        }

    }

    /**
     * 获取流程序号 for0x6B
     *
     * @param cardNo
     * @return
     */
    public boolean isCardNet(String cardNo) {
        logger.info("call outerInf ,getuserBycardId " + cardNo);
        ResultInfo result = crmFacade.getUserByCardId(cardNo);
        if (result.getResult() == ResultInfo.FAIL) {
            logger.info("getUserByCardId failed." + result.getErrorMsg());
            return false;
        } else {
            try {
                logger.info("user exist");

                String json = JsonUtil.beanToJson(result.getData());

                ChargeCard chargeCard = (ChargeCard) JsonUtil.jsonToBean(json, ChargeCard.class);

                logger.info("user statusCD: " + chargeCard.getStatus());
                if (Integer.parseInt(chargeCard.getStatus()) == 0) {
                    return true;
                } else {
                    return false;
                }
            } catch (Exception e) {
                logger.error("isCardNet ex:", e);
                return false;
            }


        }
    }

    /**
     * 查询归属充电站信息
     *
     * @return
     */
    @Cacheable(cacheNames = "collect-chargeStation", key = "#root.caches[0].name+#deviceNo")
    public String getChargeStation(String deviceNo) {
        logger.info("getChargeStation");
        ResultInfo result = resFacade.getDeviceNoInfo(deviceNo);

        if (result.getResult() == ResultInfo.OK) {
            logger.info("call outer inf response ok");
            Map<String, Serializable> map = new HashMap<String, Serializable>();
            map = (Map<String, Serializable>) (result.getData());
            try {
                logger.debug("json mkList begins");
                List<BrBatterycharge> list = JsonUtil.mkList((List) map.get("infoList"), BrBatterycharge.class);
                if (!list.isEmpty() && list.size() != 0) {
                    logger.info("begin to convert to BrBatterycharge");
                    BrBatterycharge b = null;
                    b = (BrBatterycharge) list.get(0);

                    if (b == null) {
                        logger.info("convert to BrBatterycharge failed");

                    } else {
                        logger.info("convert to BrBatterycharge ok");
                        // String stationName = b.getBrStationDetail().getStationName();
                        String chargeName = b.getBrStationDetail().getStationName();
                        logger.info("chargeName " + chargeName);
                        return chargeName;
                    }

                } else {
                    logger.info("map get infoList is null ,return null");

                }
            } catch (Exception e) {
                logger.error("convert error . " + e.getMessage());

            }

        } else {
            logger.info("call outer inf response failed.");

        }
        return null;
    }

    /**
     * @return
     * @Author donghaibo
     * @Description 查询充电站归属信息
     * @Date
     * @Param
     **/
    @Cacheable(cacheNames = "collect-chargeStation", key = "#root.caches[0].name+#deviceNo")
    public String getStationCharing(String chargingSn) {
        logger.info("getChargeStation is chargingsn :");
        try {
            ResultInfo result = resFacade.getDeviceNoInfo(chargingSn);
            if (result.getResult() == ResultInfo.OK) {
                //String json = JsonUtil.beanToJson(result.getData());
                //ChargePileDetail chargePileDetail = (ChargePileDetail) JsonUtil.jsonToBean(json, ChargePileDetail.class);
                ChargePileDetail chargePileDetail = BeanUtil.toBean(result.getData(), ChargePileDetail.class);
                String stationName = chargePileDetail.getStationName();
                logger.info("stationName is :");
                return stationName;
            }
        } catch (Exception e) {
            logger.error(" getChargeStation error :");
        }
        return null;
    }

    /**
     * @Author donghaibo
     * @Description //TODO 
     * @Date  
     * @Param [dto]
     * @return void
     **/
    public void insertBfCenterTrans(BfCenterTransT dto) {
        logger.info("call billing inf: " + dto.toString());

        ResultInfo result = billingFacade.insertBfCenterTransT(dto);
        if (result.getResult() == ResultInfo.OK) {
            logger.info("call billing inf response ok,insert BfCenterTransT ok");

        } else {
            logger.info("call billing inf response,insert BfCenterTransT failed.");

        }
    }

    public BigDecimal queryTotalBalance(String cardId) {
        logger.info("call billing queryTotalBalance");

        ResultInfo result = billingFacade.queryAccountBalance(cardId);
        if (result.getResult() == ResultInfo.OK) {
            Map<String, Object> map;
            //map = (Map<String, Object>) (result.getData());
            try {
                //String json = JsonUtil.beanToJson(result.getData());
                //ChargeCard chargeCard = (ChargeCard) JsonUtil.jsonToBean(json, ChargeCard.class);
                ChargeCard chargeCard = BeanUtil.toBean(result.getData(), ChargeCard.class);
                //	 map =(Map<String, Object>) JsonUtil.jsonToBean(json, Map.class);
                if (chargeCard != null) {
                    BigDecimal totalBalance = chargeCard.getAccount().getBalance();
                    logger.info("get totalBalance :");
                    return totalBalance;
                } else {
                    logger.info("get BfAcctBalanceT is null");
                    return null;
                }


            } catch (Exception e) {
                logger.error("totalBalance json2obj: ", e);
                return null;
            }
        } else {
            logger.info("call billing queryTotalBalance failed");
            return null;
        }
    }

    public int getDeviceStatus(String deviceNo) {
        logger.info("getDeviceStatus from res");
        try {
            ResultInfo result = resFacade.getDeviceNoInfo(deviceNo);
            if (result.getResult() == ResultInfo.OK) {
                logger.info("call resFacade getDeviceNoInfo response ok");
                Map<String, Serializable> map = new HashMap<String, Serializable>();
                map = (Map<String, Serializable>) (result.getData());


                logger.debug("json mkList begins");
                List<BrBatterycharge> list = JsonUtil.mkList((List) map.get("infoList"), BrBatterycharge.class);
                if (!list.isEmpty() && list.size() != 0) {
                    logger.debug("begin to convert to BrBatterycharge");
                    BrBatterycharge b = null;
                    b = (BrBatterycharge) list.get(0);

                    if (b == null) {
                        logger.error("convert to BrBatterycharge failed");
                        return -1;
                    } else {
                        logger.debug("convert to BrBatterycharge ok");
                        //String stationName =  b.getBrStationDetail().getStationName();
                        int status = b.getStatus();
                        logger.info("get status " + status);
                        return status;
                    }

                } else {
                    logger.error("map get infoList is null ,return null");
                    return -1;
                }


            } else {
                logger.error("call resFacade getDeviceNoInfo response fail");
                return -1;
            }
        } catch (Exception e) {
            logger.error("convert error . " + e.getMessage());


            return -1;
        }
    }

    /**
     * @return
     * @Author donghaibo
     * @Description //获取充电桩状态
     * @Date
     * @Param
     **/
    public int getChargeStatus(String deviceNo) {
        logger.info("getChargeStatus deviceNo is :");
        try {
            ResultInfo result = resFacade.getDeviceNoInfo(deviceNo);
            if (result.getResult() == ResultInfo.OK) {
                //String json = JsonUtil.beanToJson(result.getData());
                //ChargePileDetail chargePileDetail = (ChargePileDetail) JsonUtil.jsonToBean(json, ChargePileDetail.class);
                ChargePileDetail chargePileDetail = BeanUtil.toBean(result.getData(), ChargePileDetail.class);
                if (chargePileDetail == null) {
                    logger.error("convert to BrBatterycharge failed");
                    return -1;
                } else {
                    int status = Integer.parseInt(chargePileDetail.getStatus());
                    logger.info("chargeStatus is :" + status);
                    return status;
                }
            } else {
                logger.error("call resFacade getDeviceNoInfo response fail");
                return -1;
            }
        } catch (Exception e) {
            logger.error("convert error . " + e.getMessage());
            return -1;
        }

    }
}
