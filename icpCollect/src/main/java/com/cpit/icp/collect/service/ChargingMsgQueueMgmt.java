package com.cpit.icp.collect.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.msgProcess.CallHlhtInf;
import com.cpit.icp.collect.utils.AppHlhtQueueMsgUtil;
import com.cpit.icp.collect.utils.ResultInfoCode;
import com.cpit.icp.dto.common.ResultInfo;
import com.cpit.icp.dto.collect.hlht.HlhtChargingProcDto;
import com.cpit.icp.dto.common.ErrorMsg;
import static com.cpit.icp.collect.utils.Consts.hlht_methodName_qu_start;
import static com.cpit.icp.collect.utils.Consts.hlht_methodName_qu_stop;
import static com.cpit.icp.dto.common.ErrorMsg.ERR_BIZ_ERROR;
import static com.cpit.icp.dto.common.ErrorMsg.ERR_MISS_PARAM;
import static com.cpit.icp.dto.common.ErrorMsg.ERR_SYSTEM_ERROR;
import static com.cpit.icp.dto.common.ErrorMsg.ERR_WRONG_PARAM;

@Service
@RabbitListener(queues = "icp-col-hlhtMsg")
public class ChargingMsgQueueMgmt {
	private final static Logger logger = LoggerFactory.getLogger(ChargingMsgQueueMgmt.class);
	  @Autowired
	    private HuhtServiceImp huhtServiceImp;

	    @Autowired
	    RedisTemplate redisTemplate;
	    @Autowired CallHlhtInf callHlhtInf;
	
	@RabbitHandler
	   public ResultInfo process(HlhtChargingProcDto dto){
	    	logger.info("Receiver："+dto.getMethodName());
		    try {
		    	
		    	
		    	return processMsg(dto);
		    	
			} catch (Exception e) {
				logger.info("sendMessage error:"+e);
				return null;
			}
	    }
	 
	 
	 private ResultInfo processMsg(HlhtChargingProcDto dto) {
		 logger.info("processMsg");
		 ResultInfo result = new ResultInfo();
		 if(dto.getMethodName().equalsIgnoreCase(hlht_methodName_qu_start)) {
			 process_quStart(dto);
		 }else if(dto.getMethodName().equalsIgnoreCase(hlht_methodName_qu_stop)) {
			 
		 }else if(dto.getMethodName().equalsIgnoreCase("3")) {
			 
		 }else if(dto.getMethodName().equalsIgnoreCase("4")) {
			 
		 }else if(dto.getMethodName().equalsIgnoreCase("5")) {
			 
		 }else {
			 logger.error("methodName is wrong");
			 result.setResult(ResultInfo.FAIL);
			 result.setData(new ErrorMsg(2,"methodName is wrong"));
		 }
		 return result;
		 
	 }
	 
	 private ResultInfo process_quStart(HlhtChargingProcDto dto) {
		 ResultInfo resultInfo= null ;
		 try {
			 String ConnectorID = dto.getConnectorID();
			 String OperatorID = getByChargeNo(dto.getEquipAuthSeq());
	            //查询设备状态，判断是否可以开启充电
	            String chargeState = (String) redisTemplate.opsForValue().get("ConnectorID");
	            if(chargeState.equals(ResultInfoCode.free)){
	                //如果充电机空闲，可以开启充电
	                //首先构造开启充电所需报文
	                Map<String, String> userIdAndPassWord = huhtServiceImp.getUserIdAndPassWord(OperatorID);
	                if(userIdAndPassWord == null){
	                    logger.error("Get the user password blank.");
	                }
	                String userId = userIdAndPassWord.get("userId");
	                String password = userIdAndPassWord.get("password");
	                //开启充电
	               String chargeOrderNo =  huhtServiceImp.chargeState(ConnectorID, userId, password);
	                logger.info("The call to open the charging interface is successful. The charge has been switched on.");
	             Map<String,String> data = new HashMap<String,String>();
	             data.put("chargeStatus", String.valueOf(ResultInfoCode.StartUp));
	             data.put("chargeOrderNo", chargeOrderNo);
	             
	             logger.debug("push data to hlht");
	         	String startTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
	         	String IdentCode = unlockChargePole(chargeOrderNo);
	         	callHlhtInf.pushStartChargeResult(dto.getStartChargeSeq(), "2", dto.getConnectorID(), startTime, IdentCode);
	                resultInfo = new ResultInfo(ResultInfo.OK,data);
	            }else{
	                logger.info("State is not idle, can not be opened charging.");
	                resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_MISS_PARAM,"4:充电机设备占用"));
	            }
	        } catch (Exception e) {
	            logger.error("Request to start charging interface exception.");
	            resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_SYSTEM_ERROR,e.getMessage()));
	        }
	        return resultInfo;
	 }
	 
	 private ResultInfo process_quStop(HlhtChargingProcDto dto) {
		 ResultInfo resultInfo= null ;
		  try {
	            //首先根据connectorID查询当前设备的状态
	            String ChargingStart = (String) redisTemplate.opsForValue().get("ConnectorID");
	            if(ChargingStart.equals(ResultInfoCode.occupy)){
	                //当前设备正在充电中
	                //调用请求停止充电的接口
	                String userId = null;
	                boolean b = huhtServiceImp.endCharge(dto.getStartChargeSeq());
	                if (b){
	                    //返回具体的停止数据
	                    logger.info("Request to stop charging successfully."+b);
	                    resultInfo = new ResultInfo(ResultInfo.OK,ResultInfoCode.Cessation);
	                    
	                    callHlhtInf.pushStopChargeResult(dto.getStartChargeSeqStat(), "4", dto.getConnectorID(), "0", "0");
	                }
	            }else if (ChargingStart.equals(ResultInfoCode.free)){
	                //当前充电机空闲
	                resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_MISS_PARAM,"1:充电机空闲"));
	            }else if (ChargingStart.equals(ResultInfoCode.offline)){
	                //当前充电机离线
	                resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_WRONG_PARAM,"0:充电机离线"));
	            }else if (ChargingStart.equals(ResultInfoCode.makeAnAppointment)){
	                //当前充电机预约
	                resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_SYSTEM_ERROR,"4:充电机预约"));
	            }else if (ChargingStart.equals(ResultInfoCode.fault)){
	                //当前充电机故障
	                resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_BIZ_ERROR,"255:充电故障"));
	            }
	        } catch (Exception e) {
	            logger.error("Call to stop charging interface exception."+e);
	            resultInfo = new ResultInfo(ResultInfo.FAIL,new ErrorMsg(ERR_SYSTEM_ERROR,e.getMessage()));
	        }
	        return resultInfo;
	 }
	 private String getByChargeNo(String chargeNo) {
		 return null;
		 
	 }
	 
		public String unlockChargePole(String chargeOrderNo) {
			String s=null;
			int len = chargeOrderNo.length();
			if(len<6) {
				s = chargeOrderNo;
			}else {
				int pos = len-6;
				s = chargeOrderNo.substring(pos, len);
			}
			return s;
		}
		
}
