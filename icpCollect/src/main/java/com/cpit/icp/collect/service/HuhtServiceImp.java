package com.cpit.icp.collect.service;

import com.cpit.common.Dispatcher;
import com.cpit.icp.collect.utils.Consts;
import com.cpit.icp.collect.utils.ResultInfoCode;
import com.cpit.icp.collect.utils.StringUtils;
import com.cpit.icp.dto.collect.HlhtConnectorStatusDto;
import com.cpit.icp.dto.collect.MonChargingProcessDataDto;
import com.cpit.icp.dto.collect.MonChargingStartsDto;
import com.cpit.icp.dto.collect.MonFaultRecordDto;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.MonRechargingInfoDto;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_HLHT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName HuhtServiceImp
 * @Description TODO
 * @Author donghaibo
 * @Date 2018/9/29 16:36
 * @Version 1.0.0
 * @Copyright (c) 2018.09.29普天信息技术有限公司-版权所有
 **/
@Service
public class HuhtServiceImp {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(HuhtServiceImp.class);

    @Autowired
    private AppChargingProcessImp appChargingProcessImp;

    @Value("${icp.crm.url}")
    private String CRM_URL;

    @Autowired
    private RestTemplate crmTemplate;

    /**
     * @Author donghaibo
     * @Description 代用资源模块获取用户编码和密码
     * @OperatorID 运营商编码
     * @return
     **/
    public Map<String,String> getUserIdAndPassWord(String OperatorID){
        Map<String,String> userMap = new HashMap<String,String>();
        try {
            String url = CRM_URL+"/getAuthenticationOfOperator?OperatorID"+OperatorID;
            userMap = (Map) new Dispatcher(crmTemplate).doPost(url, Map.class, "");
            logger.info("Call CRM interface successfully.");
        } catch (Exception e) {
            logger.error("Failed to call CRM interface.");
        }
        return userMap;
    }
    
    public List<MonRechargeRecordDto> queryChargePoleKwh(List<String> deviceNoList,String startTime,String endTime){
    	return appChargingProcessImp.queryChargePoleKwh(deviceNoList,startTime,endTime);
    }
    
    public Map<String,List<HlhtConnectorStatusDto>> queryChargePoleStatus(Map<String,List<String>> deviceNoMap){
logger.debug("queryChargePoleStatus");
Map<String,List<HlhtConnectorStatusDto> > result = new HashMap<String,List<HlhtConnectorStatusDto>> ();
if(deviceNoMap ==null ||deviceNoMap.size() ==0) {
	logger.debug("deviceNoMap para is null");
	return result;
}
    	
    	for(String key:deviceNoMap.keySet()) {
    		logger.debug("key"+key);
    		List<String> deviceNo = deviceNoMap.get(key);
    		List<HlhtConnectorStatusDto> list = new ArrayList<HlhtConnectorStatusDto>();
    		if(!deviceNo.isEmpty() && deviceNo.size()!=0) {
    			logger.debug("deviceNoList size "+deviceNo.size());
    			for(String cid:deviceNo) {
    				MonChargingStartsDto state = this.getChargeStarte(cid);
    				HlhtConnectorStatusDto dto = new HlhtConnectorStatusDto();
    				dto.setConnectorID(state.getConnectorID());
    				dto.setStatus(state.getConnectorStatus());
    				dto.setLockStatus("0");
    				dto.setParkStatus("0");
    			}
    		}else {
    			continue;
    		}
    		result.put(key, list);
    	}
    	return result;
    }
    

    /**
     * @Author donghaibo
     * @Description 调用开启充电的接口
     * @Date
     * @Param
     * @return
     **/
    public String chargeState(String ConnectorID,String userId,String password){
        logger.info("Start calling the charging interface and sending the charging message.");
        //调用开启充电的接口
        try {
        String chargeOrderNo=    appChargingProcessImp.beginToCharge(ConnectorID,userId, ResultInfoCode.chargeMode,
                    ResultInfoCode.planChargeTime,ResultInfoCode.planChargePower,password, Consts.CHARGE_SRC_HLHT);
            logger.info("Start calling the interface to turn on the charge, and the successful message is successful.");
            return chargeOrderNo;
        } catch (Exception e) {
            logger.error("The call to turn on the charging interface is started, and the message failed to charge is failed."+e);
            return null;
        }
    }


    /**
     * @Author donghaibo
     * @Description 查询充电的状态
     * @Date
     * @Param
     * @return
     **/
    public MonChargingStartsDto getChargeStarte(String chargeOrderNo){
        MonChargingStartsDto monChargingStartsDto = null;
        //调用查询充电的状态的接口，获取需要的充电参数
            MonChargingProcessDataDto monChargingProcessDataDto = appChargingProcessImp.queryChargingProcData(chargeOrderNo);
            if(monChargingProcessDataDto != null){
                String currentA = monChargingProcessDataDto.getCurrentA();//获取A相电流
                String currentSoc = monChargingProcessDataDto.getCurrentSoc();//当前soc
                String endTime = monChargingProcessDataDto.getEndTime();//结束的时间
                String soc = monChargingProcessDataDto.getSoc();//电池剩余电量
                String startTime = monChargingProcessDataDto.getStartTime();//开始时间
                String totalPower = monChargingProcessDataDto.getTotalPower();//累计充电量
                String voltageA = monChargingProcessDataDto.getVoltageA();//A相电压
                String statusData = monChargingProcessDataDto.getChargeStatus();
                int hlhtStatusData =0;
                if(statusData.equalsIgnoreCase("0")) {
                	hlhtStatusData = 2;//充电中。
                }
                if(statusData.equalsIgnoreCase("1")||
                		statusData.equalsIgnoreCase("2")||
                		statusData.equalsIgnoreCase("3")||
                		statusData.equalsIgnoreCase("80")) {
                	hlhtStatusData = 4;//已结束
                }
                monChargingStartsDto = new MonChargingStartsDto();
                monChargingStartsDto.setCurrentA(currentA);
                monChargingStartsDto.setCurrentSoc(currentSoc);
                monChargingStartsDto.setEndTime(endTime);
                monChargingStartsDto.setSoc(soc);
                monChargingStartsDto.setStartTime(startTime);
                monChargingStartsDto.setTotalPower(totalPower);
                monChargingStartsDto.setVoltageA(voltageA);
                monChargingStartsDto.setStartChargeSeq(monChargingProcessDataDto.getChargeOrderNo());
                monChargingStartsDto.setStartChargeSeqStat(hlhtStatusData);
            }else {
                logger.info("查询充电充电订单状态失败，没事此订单充电信息");
            }
        return monChargingStartsDto;
    }


    /**
     * @Author donghaibo
     * @Description 调用结束充电的接口
     * @Date
     * @Param
     * @return
     **/
    public boolean endCharge(String chargeOrderNo){
        //调用结束充电的接口
        try {
            appChargingProcessImp.endCharge(CHARGE_SRC_HLHT,chargeOrderNo);
        } catch (Exception e) {
            logger.error("Stop charging failure, interface exception.");
            return false;
        }
        return true;
    }
}
