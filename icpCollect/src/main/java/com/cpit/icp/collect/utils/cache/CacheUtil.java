package com.cpit.icp.collect.utils.cache;

import static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_CARD;
import static com.cpit.icp.collect.utils.Consts.FILENAME_V_34;
import static com.cpit.icp.collect.utils.Consts.FILENAME_V_35;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.util.configurable.DecodeRechargeDataDomainXml;
import com.cpit.icp.collect.msgProcess.MsgDataConvertor;
import com.cpit.icp.collect.msgProcess.MsgProcessInterface;
import com.cpit.icp.collect.msgProcess.ParseMsgProcessorXml;
import com.cpit.icp.collect.msgProcess.ParseReponseXml;
import com.cpit.icp.dto.billing.finance.BfCenterTransT;
import com.cpit.icp.dto.collect.MonChargingProcessDataDto;
import com.cpit.icp.dto.collect.MonChargingStatus4App;
import com.cpit.icp.dto.collect.MonFaultRecordDto;
import com.cpit.icp.dto.collect.MonSequenceDto;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Component
public class CacheUtil {
	
	private static int i=0;
	private final static Logger logger = LoggerFactory.getLogger(CacheUtil.class);
//-------------collect-matchcode userd for uisendMsg match
	@Cacheable(cacheNames= "collect-matchCode", key="#root.caches[0].name+#key")
	public String  getMatchCode(String key) {
	return null;	
	}
	@CachePut(cacheNames= "collect-matchCode", key="#root.caches[0].name+#key")
	public String  setMatchCode(String key,String  currentT) {
	return currentT;	
	}
	@CacheEvict(cacheNames= "collect-matchCode", key="#root.caches[0].name+#key")
	public void  delMatchCode(String key) {
	
	}
	//-------------collect-matchcode 
	

	//========================used with redis cache begin
	//convert str to obj,used for 79/78
	@Cacheable(cacheNames= "collect-convertObj", key="#root.caches[0].name+#key")
	public Object getKeyValue(String key){
	logger.info("call redis ,getKeyValue in collect-decodeData");
		return null;
	}
	@CachePut(cacheNames= "collect-convertObj", key="#root.caches[0].name+#key")
	public Object setKeyValue(String key,Object value){
	return value;
	}
	@CacheEvict(cacheNames= "collect-convertObj", key="#root.caches[0].name+#key")
	public void removeKeyValue(String key) {
		logger.info("remove collect-decodeData "+key);
	}
	

	//-------------<deviceNo,faultData>
	@Cacheable(cacheNames= "collect-faultData", key="#root.caches[0].name+#deviceNo")
	public List<MonFaultRecordDto> getFaultData(String deviceNo){
		if(faultMap.get(deviceNo) == null){
			faultMap.put(deviceNo, new ArrayList<MonFaultRecordDto>());
		}
		return faultMap.get(deviceNo);
	}
	private ConcurrentHashMap<String,List<MonFaultRecordDto>> faultMap = new ConcurrentHashMap<String,List<MonFaultRecordDto>>();
	@CachePut(cacheNames= "collect-faultData", key="#root.caches[0].name+#deviceNo")
	public void upFaultData(String deviceNo,MonFaultRecordDto faultData){
		if(faultMap.get(deviceNo) == null){
			faultMap.put(deviceNo, new ArrayList<MonFaultRecordDto>());
		}
		
		List<MonFaultRecordDto> list = faultMap.get(deviceNo);
	list.add(faultData);
		
	}
	
	@CacheEvict(cacheNames="collect-faultData",key="#root.caches[0].name+#deviceNo")
	public synchronized void rmFaultData(String deviceNo){
		faultMap.remove(deviceNo);
	}
	
	//-------deviceNo,faultdto ends
	
	private ConcurrentHashMap<String,List<String>> convertParamMap = new ConcurrentHashMap<String,List<String>>();
	@Cacheable(cacheNames= "collect-convertParamData", key="#root.caches[0].name+#key")
	public List<String> getParamData(String key){
		if(convertParamMap.get(key) == null){
			convertParamMap.put(key, new ArrayList<String>());
		}
		return convertParamMap.get(key);
	}
	@CachePut(cacheNames= "collect-convertParamData", key="#root.caches[0].name+#key")
	public void upParamData(String key,String value){
		if(convertParamMap.get(key) == null){
			convertParamMap.put(key, new ArrayList<String>());
		}
		
		List<String> list = convertParamMap.get(key);
	list.add(value);
		
	}
	
	
	//---bfData <centerTransId,bfDatadto>
	@CachePut(cacheNames= "collect-bfData", key="#root.caches[0].name+#dto.centerTransId")
	public BfCenterTransT upBfData(BfCenterTransT dto){
		return dto;
		
	}
	@Cacheable(cacheNames= "collect-bfData", key="#root.caches[0].name+#centerTransId")
	public BfCenterTransT getBfData(String centerTransId){
		return new BfCenterTransT();
		
	}
	@CacheEvict(cacheNames= "collect-bfData", key="#root.caches[0].name+#centerTransId")
	public void delBfData(String centerTransId){
		
		
	}
	//---bfData <centerTransId,bfDatadto> ends
	
	//---------------------------------------------------xml data
	@Cacheable(cacheNames= "collect-xmlObjStore", key="#root.caches[0].name+#version")
	public Document getObjStoreXml(String version){
		SAXReader sax = new SAXReader();
		Document document=null;
		String fileName=null;
		
				try {

					document = sax
							.read(CacheUtil.class.getResourceAsStream("/conf/msgStore/" + version + "/msgStore.xml"));
				} catch (Exception e) {
					logger.error("read document error!");
				}
		return document;
	}
	
	
	@Cacheable(cacheManager="redisCacheManager", cacheNames= "collect-xmlParseMsg", key="#root.caches[0].name+#version")
	public Document getParseMsgXml(String version) {
		Document document=null;
		SAXReader sax = new SAXReader();
		try {
			document = sax
					.read(CacheUtil.class.getResourceAsStream("/conf/configurable/charging_"+version+"/designcharge.xml"));
		return document;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getParseMsgXml: "+e.getMessage());
			
			return null;
		}
	
	
	}
	@CacheEvict(cacheManager="redisCacheManager", cacheNames= "collect-xmlParseMsg", key="#root.caches[0].name+#version")
	public void delParseMsgXml(String version) {
		
	}
	@Cacheable(cacheManager="redisCacheManager", cacheNames= "collect-xmlProcessConfig", key="#root.caches[0].name+#version")
	public Document getProcConfigXml(String version) {
		Document document=null;
		
		SAXReader sax = new SAXReader();
		
		try {
			document = sax
					.read(CacheUtil.class.getResourceAsStream("/conf/msgProcess/" + version + "/msgProcessor.xml"));
		return document;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("getProcessConfigXml: "+e.getMessage());
			
			return null;
		}
	
	
	}
	@CacheEvict(cacheManager="redisCacheManager", cacheNames= "collect-xmlProcessConfig", key="#root.caches[0].name+#version")
	public void delProcConfigXml(String version) {
	
	}
	@Cacheable(cacheManager="redisCacheManager", cacheNames= "collect-xmlResponseConfig", key="#root.caches[0].name+#version")
	public Document getResponseConfigXml(String version) {
		SAXReader sax = new SAXReader();
		Document document = null;
		
		try {
			document = sax.read(CacheUtil.class.getResourceAsStream("/conf/msgResponse/"+version+"/msgResponse.xml"));
		return document;
		} catch (DocumentException e) {                                     
			logger.error("getResponseConfigXml",e);
			return null;
		}
	}
	@CacheEvict(cacheManager="redisCacheManager", cacheNames= "collect-xmlResponseConfig", key="#root.caches[0].name+#version")
	public void delResponseConfigXml(String version) {
	
	}
	

	
	//---------------------------xmldata ends
	private ConcurrentHashMap<String,MonSequenceDto> sequenceMap = new  ConcurrentHashMap<String,MonSequenceDto>();
	public MonSequenceDto getSequence(String deviceNo) {
		MonSequenceDto dto = getSequenceData(deviceNo);
		MonSequenceDto dtoN =new MonSequenceDto();
		int flowNum = dto.getFlowNum();
		flowNum++;
		if(flowNum>65535) {
			flowNum =1;
		}
		int sequenceNum = dto.getSequenceNum();
		sequenceNum++;
		if(sequenceNum>32767) {
			sequenceNum=1;
		}
		dtoN.setFlowNum(flowNum);
		dtoN.setSequenceNum(sequenceNum);
		upSequenceData(deviceNo,dtoN);
		return dto;
	}
	@Cacheable(cacheNames= "collect-sequenceData", key="#root.caches[0].name+#deviceNo")
	private MonSequenceDto getSequenceData(String deviceNo) {
		if(!sequenceMap.containsKey(deviceNo)) {
			sequenceMap.put(deviceNo, new MonSequenceDto());
		}
				
		
		return sequenceMap.get(deviceNo);
	}
	@CachePut(cacheNames= "collect-sequenceData", key="#root.caches[0].name+#deviceNo")
	private void upSequenceData(String deviceNo,MonSequenceDto dto) {
		sequenceMap.put(deviceNo, dto);
	}
	/**
	 * dto里面的数据清0.
	 * @param deviceNo
	 * @return
	 */
	@CachePut(cacheNames= "collect-sequenceData", key="#root.caches[0].name+#deviceNo")
	public void initSequenceData(String deviceNo) {
		sequenceMap.put(deviceNo, new MonSequenceDto());
	}

	
	@Cacheable (cacheNames ="collect-chargeSrc",key="#root.caches[0].name+#key")
	public String getChargeSrc(String key) {
		return CHARGE_SRC_CARD;
	}
	@CachePut(cacheNames ="collect-chargeSrc",key="#root.caches[0].name+#key")
	public String setChargeSrc(String key,String src) {
		return src;
	}
	//--------------------xml codepairTS info
	@Cacheable (cacheNames ="collect-codePairTS",key="#root.caches[0].name+#version")
	public Map<String,String> getCodePairTS(String version) {
		Map<String,String> map = new HashMap<String,String>();
		return map;
	}
	@CachePut(cacheNames ="collect-codePairTS",key="#root.caches[0].name+#version")
	public Map<String,String> setCodePairTS(String version,Map<String,String> codePairTSMap) {
		
		return codePairTSMap;
	}
	
	
	
	
	
	//----------------------------------------app cache
		//<cardid,deviceNo>
		@CachePut(cacheNames= "collect-appCardDeviceNo", key="#root.caches[0].name+#cardId")
		public String setAppCardDeviceNo(String deviceNo,String cardId){
			return deviceNo;
			
		}
		@CacheEvict(cacheNames= "collect-appCardDeviceNo", key="#root.caches[0].name+#cardId")
		public void delAppCardDeviceNo(String cardId){
			
			
		}
		@Cacheable(cacheNames= "collect-appCardDeviceNo", key="#root.caches[0].name+#cardId")
		public String getAppCardDeviceNo(String cardId){
			return null;
			
		}
		//<deviceNo,startTime>
		@Cacheable(cacheNames= "collect-appStartChargeTime",key="#root.caches[0].name+#deviceNo")
		public long getCPStartChargeTime(String deviceNo) {
			long time = TimeConvertor.getCurrentTime();
			return time;
		}
		@CachePut(cacheNames= "collect-appStartChargeTime",key="#root.caches[0].name+#key")
		public long upCPStartChargeTime(String key) {
			long time = TimeConvertor.getCurrentTime();
			return time;
		}
		@CacheEvict(cacheNames= "collect-appStartChargeTime",key="#root.caches[0].name+#key")
		public void delCPStartChargeTime(String key) {
			
		}
		
		// <appcarduserId,chargeNo>
		@CachePut(cacheNames= "collect-appCardChargeNo", key="#root.caches[0].name+#cardId")
		public String setAppCardChargeNo(String chargeNo,String cardId){
			return chargeNo;
			
		}
		@CacheEvict(cacheNames= "collect-appCardChargeNo", key="#root.caches[0].name+#cardId")
		public void delAppCardChargeNo(String cardId){
			
			
		}
		@Cacheable(cacheNames= "collect-appCardChargeNo", key="#root.caches[0].name+#cardId")
		public String getAppCardChargeNo(String cardId){
			return null;
			
		}
		
		@CachePut(cacheNames= "collect-appDeviceNoCard", key="#root.caches[0].name+#deviceNo")
		public String setAppRelatedCard(String deviceNo,String cardId){
			return cardId;
			
		}
		@Cacheable(cacheNames= "collect-appDeviceNoCard", key="#root.caches[0].name+#deviceNo")
		public String getAppRelatedCard(String deviceNo){
			return null;
			
		}
		@CacheEvict(cacheNames="collect-appDeviceNoCard",key="#root.caches[0].name+#deviceNo")
		public void delAppRelatedCard(String deviceNo) {
			
		}

		//=<deviceNo,appstatusDto>
		@Cacheable(cacheNames= "collect-statusData",key="#root.caches[0].name+#deviceNo")
	public MonChargingStatus4App getStatusData(String deviceNo ) {
			MonChargingStatus4App data = new MonChargingStatus4App();
			data.setDeviceNo(deviceNo);
			return data;
		}
		
		@CachePut(cacheNames= "collect-statusData",key="#root.caches[0].name+#statusData.deviceNo")
	public MonChargingStatus4App setStatusData(MonChargingStatus4App statusData) {
		
			logger.info("statusData: "+statusData.toString());
			return statusData;
		}
		
		//<deviceNo,processDatadto>
		@Cacheable(cacheNames= "collect-processData", key="#root.caches[0].name+#deviceNo")
		public MonChargingProcessDataDto getCPProcessData(String deviceNo){
			MonChargingProcessDataDto dto = new MonChargingProcessDataDto();
			dto.setDeviceNo(deviceNo);
			return dto;
		}
		
		@CachePut(cacheNames= "collect-processData", key="#root.caches[0].name+#procData.deviceNo")
		public MonChargingProcessDataDto upCPProcessData(MonChargingProcessDataDto procData){
		
			return procData;
		}
		
		
		//--------------------------------------app cache ends
	
		
		
		//-----------xml
	
}
