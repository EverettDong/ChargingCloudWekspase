package com.cpit.icp.collect.controller;

import com.cpit.icp.collect.service.HuhtServiceImp;
import com.cpit.icp.collect.utils.AppHlhtQueueMsgUtil;
import com.cpit.icp.collect.utils.ResultInfoCode;
import com.cpit.icp.dto.collect.HlhtConnectorStatusDto;
import com.cpit.icp.dto.collect.MonChargingProcessDataDto;
import com.cpit.icp.dto.collect.MonChargingStartsDto;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.MonRechargingInfoDto;
import com.cpit.icp.dto.collect.hlht.HlhtChargingProcDto;
import com.cpit.icp.dto.common.ErrorMsg;
import com.cpit.icp.dto.common.ResultInfo;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.cpit.icp.dto.common.ErrorMsg.*;

import static com.cpit.icp.collect.utils.Consts.hlht_methodName_qu_start;
import static com.cpit.icp.collect.utils.Consts.hlht_methodName_qu_stop;
/**
 * @ProjectName 新能源智能云平台
 * @ClassName HlhtChargingProcessImp
 * @Description TODO
 * @Author donghaibo
 * @Date 2018/9/28 14:47
 * @Version 1.0.0
 * @Copyright (c) 2018.09.28普天信息技术有限公司-版权所有
 **/
@RestController
public class HlhtChargingProcessInf {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(HlhtChargingProcessInf.class);

    @Autowired
    private HuhtServiceImp huhtServiceImp;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * @Author donghaibo
     * @Description 请求设备认证
     * @Date
     * @Param
     * @return 资源不存在，占用，离线，故障，返回失败，其他成功。
     **/
    @RequestMapping(value = "/coll/queryEquipAuth")
    public ResultInfo queryEquipAuth(@RequestParam(value = "EquipAuthSeq") String StartChargeSeq,
                                 @RequestParam(value = "ConnectorID") String ConnectorID){
        logger.info("Device request authentication started"+StartChargeSeq+ConnectorID);
        ResultInfo resultInfo = null;
        try {
            //查询缓存，查看设备状态是是否插枪
            String chargeState = (String) redisTemplate.opsForValue().get("ConnectorID");
            if(chargeState.equals(ResultInfoCode.free)){
                //充电机离线
                resultInfo = new ResultInfo(ResultInfo.OK,chargeState);
            }else if(chargeState.equals(ResultInfoCode.offline)){
                //充电机空闲
                resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_MISS_PARAM,"0:充电机离线"));
            }else if(chargeState.equals(ResultInfoCode.occupy)){
                //充电占用
                resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_WRONG_PARAM,"3:充电机占用"));
            }else if(chargeState.equals(ResultInfoCode.makeAnAppointment)){
                //充电机以备预约
                resultInfo = new ResultInfo(ResultInfo.OK,new ErrorMsg(ERR_SYSTEM_ERROR,"4:充电机预约"));
            }else if(chargeState.equals(ResultInfoCode.fault)){
                //充电机故障
                resultInfo = new ResultInfo(ResultInfo.OK,new ErrorMsg(ERR_BIZ_ERROR,"255:充电机故障"));
            }
            logger.info("Request device authentication successful");
        } catch (Exception e) {
            logger.error("queryStartCharge error"+e);
            resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_SYSTEM_ERROR, e.getMessage()));
        }
        return resultInfo;
    }

    /**
     * @Author donghaibo
     * @Description 请求开启充电的接口
     * @Date
     * @Param
     * @return
     **/
    @RequestMapping(value = "/coll/queryStartCharge")
    public ResultInfo queryStartCharge(@RequestParam(value = "StartChargeSeq") String StartChargeSeq,
                                       @RequestParam(value = "ConnectorID") String ConnectorID,
                                       @RequestParam(value = "QRCode") String QRCode,
                                       @RequestParam(value = "OperatorID") String OperatorID){

        logger.info("The request starts charging."+ConnectorID);
        ResultInfo resultInfo = null;
        HlhtChargingProcDto obj = new HlhtChargingProcDto();
        obj.setMethodName(hlht_methodName_qu_start);
        obj.setStartChargeSeq(StartChargeSeq);
        obj.setConnectorID(ConnectorID);
        obj.setQRCode(QRCode);
        AppHlhtQueueMsgUtil.getInstance().send(obj);
        resultInfo = new ResultInfo(ResultInfo.OK,null);
        return resultInfo;
        
    }


    /**
     * @Author donghaibo
     * @Description 查询充电状态
     * @Date
     * @Param
     * @return
     **/
    @RequestMapping(value = "/coll/queryEquipChargeStatus")
    public ResultInfo queryEquipChargeStatus(@RequestParam(value = "StartChargeSeq")String StartChargeSeq,
                                             @RequestParam(value = "ConnectorID")String ConnectorID){
        logger.info("The charging status is started. The order number is:"+StartChargeSeq);
        ResultInfo resultInfo = null;
        try {
            //查询当前设备的状态
            String EquipmentState = (String) redisTemplate.opsForValue().get("ConnectorID");
            //根据订单号查询具体订单的充电状态
            MonChargingStartsDto monChargingStartsDto =
                    huhtServiceImp.getChargeStarte(StartChargeSeq);
            if(monChargingStartsDto != null){
                //补全返回信息
                monChargingStartsDto.setStartChargeSeq(StartChargeSeq);
                monChargingStartsDto.setConnectorID(ConnectorID);
                monChargingStartsDto.setConnectorStatus(EquipmentState);
                monChargingStartsDto.setStartChargeSeqStat(ResultInfoCode.ChargeStartOrder);
                resultInfo = new ResultInfo(ResultInfo.OK,monChargingStartsDto);
            }
        } catch (Exception e) {
            logger.error("Query charging status abnormal"+e);
            resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_SYSTEM_ERROR,e.getMessage()));
        }
        return resultInfo;
    }


    /**
     * @Author donghaibo
     * @Description 停止充电
     * @Date
     * @Param
     * @return
     **/
    @RequestMapping(value = "/coll/queryStopCharge")
    public ResultInfo queryStopCharge(@RequestParam(value = "StartChargeSeq") String StartChargeSeq,
                                      @RequestParam(value = "ConnectorID") String ConnectorID){
        logger.info("When the request stops charging, the order number is:"+StartChargeSeq);
        ResultInfo resultInfo = null;
       
        HlhtChargingProcDto obj = new HlhtChargingProcDto();
        obj.setMethodName(hlht_methodName_qu_stop);
        obj.setStartChargeSeq(StartChargeSeq);
        obj.setConnectorID(ConnectorID);
     
        AppHlhtQueueMsgUtil.getInstance().send(obj);
        resultInfo = new ResultInfo(ResultInfo.OK,null);
        return resultInfo;
        
        
        
      
    }
   /**
    * 
    * @param deviceNoList
    * @param startTime yyyy-mm-dd 
    * @param endTime yyyy-mm-dd
    * @return
    */
    @RequestMapping(value = "/coll/queryChargePoleKwh")
    public ResultInfo queryChargePoleKwh(@RequestParam(value = "deviceNoList") List<String> deviceNoList,
                                      @RequestParam(value = "startTime") String startTime,
                                      @RequestParam(value = "endTime") String endTime){
        logger.info("queryChargePoleKwh,param is:"+deviceNoList.toString()+" "+startTime+" "+endTime);
        ResultInfo result =new ResultInfo();
        if(deviceNoList.isEmpty() || deviceNoList.size()==0) {
        	result.setResult(ResultInfo.FAIL);
        	result.setData("param deviceNoList is null");
        }else {
        	List<MonRechargeRecordDto> list = huhtServiceImp.queryChargePoleKwh(deviceNoList, startTime, endTime);
        	result.setData(list);
        	result.setResult(ResultInfo.OK);
        }
      
        return result;
    }
    /**
     * 
     * @param deviceNoList

     * @return
     */
     @RequestMapping(value = "/coll/queryChargePoleStatus")
     public ResultInfo queryChargePoleStatus(@RequestParam(value = "deviceNoMap") Map<String,List<String>> deviceNoMap
                                      ){
         logger.info("queryChargePoleStatus,param is:"+deviceNoMap.size());
         ResultInfo result =new ResultInfo();
         if(deviceNoMap.isEmpty() || deviceNoMap.size()==0) {
         	result.setResult(ResultInfo.FAIL);
         	result.setData("param deviceNoList is null");
         }else {
         Map<String,List<HlhtConnectorStatusDto>> map = huhtServiceImp.queryChargePoleStatus(deviceNoMap);
         	result.setData(map);
         	result.setResult(ResultInfo.OK);
         }
       
         return result;
     }
    
    
}
