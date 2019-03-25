package com.cpit.icp.collect.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.msgProcess.CallOuterInf;
import com.cpit.icp.collect.msgProcess.MsgMap;
import com.cpit.icp.collect.service.AppChargingProcessImp;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.MonChargingProcDataApp;
import com.cpit.icp.dto.collect.MonChargingProcessDataDto;
import com.cpit.icp.dto.collect.MonChargingStatus4App;
import com.cpit.icp.dto.collect.MonRechargingInfoDto;
import com.cpit.icp.dto.common.ErrorMsg;
import com.cpit.icp.dto.common.ResultInfo;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

//import static com.cpit.icp.collect.utils.Consts.CP_STATUS_OFF;
import static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_APP;
import static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_HLHT;
import static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_CARD;
import static com.cpit.icp.collect.utils.Consts.TIME_GAP;
/**
 * app charging inf
 * @author zhangqianqian 
 *
 */
@Api(tags = "充电过程接口")
@RestController
@RequestMapping(method={RequestMethod.POST})
public class AppChargingProcessInf {
	private final static Logger logger = LoggerFactory.getLogger(AppChargingProcessInf.class);
	Map<String, String> chargePoleInUse;
	
	@Autowired AppChargingProcessImp appChargingProcessImp;
	@Autowired CallOuterInf callOuterInf;
	@Autowired CacheUtil cacheUtil;
	//开始充电接口

	@ApiOperation(value = "开始充电", response=ResultInfo.class)
	@RequestMapping(value = "/beginToCharge")
	public ResultInfo beginToCharge(
			 @RequestParam(value = "deviceNo") String deviceNo,
			 @RequestParam(value = "cardUserId") String cardUserId ,
			 @RequestParam(value = "chargeMode") String chargeMode,
			 @RequestParam(value = "planChargeTime") String planChargeTime,
			 @RequestParam(value = "planChargePower") String planChargePower,
			 @RequestParam(value = "password") String password
			) {
		logger.info("beginToCharge begins.");

		long currentTime =TimeConvertor.getCurrentTime();
		long time = cacheUtil.getCPStartChargeTime(deviceNo);
		
		
		long timeGap = TimeConvertor.timeGap(	time,currentTime);
		logger.info("timeGap: "+ timeGap);
		if(timeGap >0 && timeGap<TIME_GAP) {
			logger.info("sendBeginCharge time Gap <2 mins");
			ResultInfo result = new ResultInfo();
			result.setResult(ResultInfo.FAIL);
			result.setData("time Gap < 2mins.");
			return result;
		}else {
			logger.info("beginToCharge ");
			String chargeOrderNo =appChargingProcessImp.beginToCharge(deviceNo,cardUserId,chargeMode,planChargeTime,planChargePower,password,CHARGE_SRC_APP);
			ResultInfo result = new ResultInfo();
			if(chargeOrderNo==null) {
				logger.info("get  chargeOrder No is null");
				result.setResult(ResultInfo.FAIL);
				result.setData(new ErrorMsg(5,"get chargeOrderNo is null"));
				return result;
			}
			
			
			result.setResult(ResultInfo.OK);
			result.setData(chargeOrderNo);
			cacheUtil.setAppCardChargeNo(chargeOrderNo, cardUserId);
			return result;
		}
		
		
	}
	//充电进度查询接口.
	/**
	 * 
	 * @param chargeOrderNo 充电订单流水号  hlht用来和设备关联
	 * @param userId 用户id
	 * @param cardUserId ，卡号，app用来和设备关联
	 * @param type 2 App，/3 hlht
	 * @return
	 */
	@ApiOperation(value = "充电进度查询接口", response=ResultInfo.class)
	@RequestMapping(value = "/queryChargingStatus")
	public ResultInfo queryChargingStatus(
			 @RequestParam(value = "chargeOrderNo") String chargeOrderNo ,
			 @RequestParam(value = "userId") String userId ,
			 @RequestParam(value = "cardUserId") String cardUserId,
			 @RequestParam(value = "type") String type
) {
		logger.info("queryChargingStatus begins: "+chargeOrderNo+ " "+userId+" "+cardUserId+" "+type);
		String deviceNo;
		ResultInfo result = new ResultInfo();
		String chargeOrderNoApp;
		if(type.equalsIgnoreCase(CHARGE_SRC_APP)) {
			logger.debug("request from app");
			//Map<String, String> chargePoleInUse = MsgMap.getInstance().getChargePoleInAppUse();
			//logger.info("chargePoleInUse: "+chargePoleInUse.toString());
		deviceNo=cacheUtil.getAppCardDeviceNo(cardUserId);
		chargeOrderNoApp=cacheUtil.getAppCardChargeNo(cardUserId);
		if(deviceNo==null||deviceNo.equalsIgnoreCase("")) {
			logger.info("device is not is charing..");
			result.setResult(ResultInfo.FAIL);
			result.setData("donot get related deviceNo,"+cardUserId+"no chargingProcessData.");
			return result;
		}
			logger.info("deviceNo is : "+deviceNo);
			MonChargingProcessDataDto statusData =appChargingProcessImp.queryChargingProcData(deviceNo);
			if(statusData==null) {
				logger.info("get null data.");
				result.setResult(ResultInfo.FAIL);
				result.setData("no chargingProcessData.");
				return result;
			}
			
MonChargingProcDataApp appChargingProcData = new MonChargingProcDataApp();
			logger.debug("revert chargingData for app.");
			appChargingProcData = getByMonChargingProcData(statusData);
			//查询crm信息。
			logger.debug("query crm");
			//String stationInfo = callOuterInf.getChargeStation(deviceNo);
			String stationInfo = callOuterInf.getStationCharing(deviceNo);

			appChargingProcData.setChargeStationInfo(stationInfo);
			appChargingProcData.setCardId(cardUserId);
			if(chargeOrderNoApp==null||chargeOrderNoApp.equalsIgnoreCase("")) {
				logger.info("chargeOrderNoApp is null ");
			}
			
			appChargingProcData.setChargeOrderNo(chargeOrderNoApp);
			appChargingProcData.setDeviceNo(deviceNo);
			result.setData(appChargingProcData);
			result.setResult(ResultInfo.OK);
			return result;
			
		}else if(type.equalsIgnoreCase(CHARGE_SRC_HLHT) ) {
			logger.debug("request from hlht");
			
			Map<String,String> chargePoleInUse = MsgMap.getInstance().getChargePoleInHLHTUse();
			if(chargePoleInUse.containsKey(chargeOrderNo)) {
				 deviceNo = chargePoleInUse.get(chargeOrderNo);
			}else {
				logger.debug("device is not is charing..");
				result.setResult(ResultInfo.FAIL);
				result.setData("no related deviceNo "+chargeOrderNo+"no chargingProcessData.");
				return result;
			}
			
			MonChargingProcessDataDto statusData =appChargingProcessImp.queryChargingProcData(deviceNo);
			if(statusData==null) {
				logger.debug("get null data.");
				result.setResult(ResultInfo.FAIL);
				result.setData("no chargingProcessData.");
				return result;
			}
			result.setData(statusData);
			result.setResult(ResultInfo.OK);
			return result;
			
		}else {
			logger.debug("type is wrong."+type);
			result.setResult(ResultInfo.FAIL);
			result.setData("type is wrong.");
			return result;
		}
		
	}
	//充电解锁接口-暂时用不到20181108
	@ApiOperation(value = "充电解锁接口", response=ResultInfo.class)
	@RequestMapping(value = "/chargePoleUnlock")
	public ResultInfo chargePoleUnlock(
			@RequestParam(value = "chargeOrderNo") String chargeOrderNo ,
			@RequestParam(value = "UserId") String UserId ,
			@RequestParam(value = "DeviceNo")   String DeviceNo  
) {
		logger.info("chargePoleUnlock: "+DeviceNo+" "+chargeOrderNo+" "+UserId);
	if(chargeOrderNo==null||chargeOrderNo.equalsIgnoreCase("")) {
		ResultInfo result = new ResultInfo();
		result.setData("received chargeOrder is null");
		result.setResult(ResultInfo.FAIL);
		return result;
	}
		String unlockCode = appChargingProcessImp.unlockChargePole(chargeOrderNo);
		ResultInfo result = new ResultInfo();
		result.setData(unlockCode);
		result.setResult(ResultInfo.OK);
		return result;
	}
	//结束充电接口
	@ApiOperation(value = "结束充电接口", response=ResultInfo.class)
	@RequestMapping(value = "/endOfCharge")
	public ResultInfo endOfCharge(
			@RequestParam(value = "chargeOrderNo") String chargeOrderNo ,
			@RequestParam(value = "cardUserId") String cardUserId 
			
) {
		logger.info("endOfCHarge"+chargeOrderNo+" "+ cardUserId);
		appChargingProcessImp.endCharge(CHARGE_SRC_APP,cardUserId);
		
		ResultInfo result = new ResultInfo();
		result.setResult(ResultInfo.OK);
		return result;
	}
	//查询充电桩状态
	@ApiOperation(value = "查询充电桩状态", response=ResultInfo.class)
	@RequestMapping(value = "/queryChargePoleStatus")
	public ResultInfo queryChargePoleStatus(
			@RequestParam(value = "deviceNo") String deviceNo) {
	
		logger.info("queryChargePoleStatus :"+deviceNo);
		ResultInfo result = new ResultInfo();
		//String status =
		MonChargingStatus4App appStatusData = cacheUtil.getStatusData(deviceNo);
	
		
			result.setResult(ResultInfo.OK);
			result.setData(appStatusData);
	
		return result;
		}

	@ApiOperation(value = "查询充电桩状态", response=ResultInfo.class)
	@RequestMapping(value = "/queryChargePoleStatusMap")
	public ResultInfo queryChargePoleStatusMap(
			@RequestParam(value = "deviceNolist") List<String> deviceNolist) {
	
		logger.info("queryChargePoleStatusMap listSize :"+deviceNolist.size());
		ResultInfo result = new ResultInfo();
		//String status =
		//MonChargingStatus4App appStatusData = cacheUtil.getStatusData(deviceNo);
	List<MonChargingStatus4App> resultList = new ArrayList<MonChargingStatus4App>();
		int listSize = deviceNolist.size();
	for(int i=0;i<listSize;i++) {
		String deviceNo = deviceNolist.get(i);
		logger.info("deviceNo: "+ deviceNo +" i: "+i);
		MonChargingStatus4App appStatusData = cacheUtil.getStatusData(deviceNo);
		if(appStatusData!=null) {
			resultList.add(appStatusData);
		}
	}
	logger.info("queryChargePoleStatusMap resultList size is: "+resultList.size());
		
		result.setResult(ResultInfo.OK);
			result.setData(resultList);
	
		return result;
		}

	
	

	private MonChargingProcDataApp getByMonChargingProcData(MonChargingProcessDataDto statusData) {
		logger.debug("getByMonChargingProcData begins.");
		MonChargingProcDataApp appProcData = new MonChargingProcDataApp();
		appProcData.setCurrentA(statusData.getCurrentA());
		appProcData.setCurrentSoc(statusData.getCurrentSoc());
		appProcData.setVoltageA(statusData.getVoltageA());
		
		String statusD = statusData.getChargeStatus();
		if(statusD==null||statusD.equalsIgnoreCase("")) {
			appProcData.setChargeState(-1);
		}else {
		appProcData.setChargeState(Integer.parseInt(statusData.getChargeStatus()));
		appProcData.setGunConnStatus(statusData.getGunConnStatus());
		}
		return appProcData;
	}
	
	
	
	
	
}
