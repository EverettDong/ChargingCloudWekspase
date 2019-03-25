package com.cpit.icp.collect.msgProcess;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.cpit.icp.dto.billing.finance.BfCenterTransT;
import com.cpit.icp.dto.collect.MonChargingProcessDataDto;
import com.cpit.icp.dto.collect.MonChargingStatus4App;
import com.cpit.icp.dto.collect.MonFaultRecordDto;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.coderDecoder.DecodedMsgDto;
import com.cpit.icp.dto.collect.coderDecoder.PackageDto;
import com.cpit.icp.dto.collect.coderDecoder.UIDataContentDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.service.MsgSend;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.MsgCodeUtil;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.collect.utils.cache.DeviceCacheUtil;
import com.cpit.common.Encodes;
import com.cpit.common.SpringContextHolder;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.rabbitConfig.RabbitMsgSender;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureDecode;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureEncode;
import static com.cpit.icp.collect.utils.Consts.CODE_MODE;
import static com.cpit.icp.collect.utils.Consts.PHRASE;
import  static com.cpit.icp.collect.utils.Consts.VERSION_3_1;
import  static com.cpit.icp.collect.utils.Consts.VERSION_3_2;
import  static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import  static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import  static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_CARD;
import  static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_APP;
import  static com.cpit.icp.collect.utils.Consts.CHARGE_SRC_HLHT;
///import static com.cpit.testplatform.modules.sockComm.util.Consts.VERSION_34;
//import static com.cpit.testplatform.modules.sockComm.util.Consts.TERM_DEL_FLAG;;
/**
  @author zhangqianqian
 *
 */
public class RequestMsgProImp implements MsgProcessInterface {
	private final static Logger logger = LoggerFactory.getLogger(RequestMsgProImp.class);

	private MsgMap msgMap = MsgMap.getInstance();

	private Map<String, Queue<MsgOfPregateDto>> sendMsgMap;

	private ConfigureDecode configureDecode;
	private ConfigureEncode configureEncode;
	private ConstructData constructData;
	private MonDBService monDBService;
	private MsgSend msgSend;
	private MsgCodeUtil msgCodeUtil;
	private MsgDataConvertor msgDataConvertor;
	//private String[] decodedData ;
	private String receivedTime=null;
	private CacheUtil cacheUtil;
	private DeviceCacheUtil deviceCacheUtil;
	  private RabbitMsgSender rabbitMsgSender;
	  private ConstructMsgUtil constructMsgUtil;
	  private CallOuterInf callOuterInf;
	{

		constructData = SpringContextHolder.getBean(ConstructData.class);
		configureDecode = SpringContextHolder.getBean(ConfigureDecode.class);
		configureEncode = SpringContextHolder.getBean(ConfigureEncode.class);
		msgSend = SpringContextHolder.getBean(MsgSend.class);
		monDBService = SpringContextHolder.getBean(MonDBService.class);
		msgCodeUtil = SpringContextHolder.getBean(MsgCodeUtil.class);
		//decodedData = null;
		msgDataConvertor = SpringContextHolder.getBean(MsgDataConvertor.class);
		rabbitMsgSender = SpringContextHolder.getBean(RabbitMsgSender.class);
		cacheUtil =  SpringContextHolder.getBean(CacheUtil.class);
		deviceCacheUtil =  SpringContextHolder.getBean(DeviceCacheUtil.class);
		constructMsgUtil = SpringContextHolder.getBean(ConstructMsgUtil.class);
		callOuterInf = SpringContextHolder.getBean(CallOuterInf.class);
	//	ParseMsgProcessorXml.getInstance();
	}

	@Override
	public void msgProcess(Object obj) {
		doMsgProcess((MsgOfPregateDto) obj);
	}

	private void doMsgProcess(MsgOfPregateDto dto){
	   logger.info("requestdata doMsgProcess: "+dto.getDeviceNo());
	    receivedTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		byte mesFlag=0;
		PackageDto receiveDto = null;
		DecodedMsgDto decodedMsgDto = null;
		String codeMode= null;
		if(dto.getMsgVersion().equalsIgnoreCase(VERSION_3_5)) {
			codeMode = deviceCacheUtil.getDeviceCodeMode(dto.getDeviceNo());
			if(codeMode==null) {
				logger.error("codeMode is null.");
				codeMode=CODE_MODE;
			}
		}else {
			codeMode=CODE_MODE;
		}
		logger.info("deviceNo: "+dto.getDeviceNo()+" "+dto.getMsgVersion()+ "codemode : "+codeMode);
		try {
			mesFlag = configureDecode.DecodeCommand(Encodes.decodeHex(dto.getMsgData()));
			
			logger.debug( "decodeCommand success,mesFlag is "+mesFlag);
		} catch (Exception e) {
			logger.error("decodeCommand exception:"+ e.getMessage());
			return;
		}
		 
		List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
		decodedMsgDto= decodeReportPackage(dto,codeMode);
		receiveDto = decodedMsgDto.getPackageDto();
		 
		 if(mesFlag == 0x3D) {
			logger.info("received 3d,received fault");
			boolean b =process3D("0x3D",  dto.getMsgVersion(), dto.getDeviceNo(), receivedTime);
			sendStopChargingMsg(b,dto);
			logger.debug("3D process success,begin to reply");
			
		}
		 
		 if(
				 ((mesFlag == 0x79)&&(dto.getMsgVersion()== VERSION_3_4))
				 ||((mesFlag == 0x7D)&&(dto.getMsgVersion()== VERSION_3_5))
				||((mesFlag == (byte)0x78)&&(dto.getMsgVersion().equals(VERSION_3_1)))
				||((mesFlag == (byte)0x78)&&(dto.getMsgVersion().equals(VERSION_3_2)))
				
				) {
				//添加到rabbitMQ隊列
				logger.info("received 79/78/7d.,update redis isTakeUp.");
				upCPStatusCharging(dto.getDeviceNo());
				Object stObj = msgDataConvertor.chargingStrToObj(decodedMsgDto.getDecodedMsg(), dto.getMsgCode(),dto.getMsgVersion());
					if(stObj==null) {
						logger.info("chargingStrToObj is wrong，return null obj");
						return;
						}
				MonRechargeRecordDto chargingObj = (MonRechargeRecordDto)stObj;
				chargingObj.setMsgCode(dto.getMsgCode());
				chargingObj.setDeviceNo(dto.getDeviceNo());
				String isNormalEnd = chargingObj.getNormalEnd();
				logger.info("upproc status redis.");
				this.upCPProcDataStatus(dto.getDeviceNo(),isNormalEnd);
			 
				String chargeSource = cacheUtil.getChargeSrc(dto.getDeviceNo());
				chargingObj.setChargeSource(chargeSource);
				logger.info("chargingobj: "+chargingObj.toString());
				rabbitMsgSender.send(chargingObj);
				  logger.info("added to rabbitMQ.");
				monDBService.storeObjData(stObj,dto.getMsgCode(),dto.getDeviceNo());
			//	decodedData[1] = null;
				stopCharging(dto.getDeviceNo(),chargeSource);
				logger.info("78/79/7D process over and return");
				return;
			}
	
	//	Map<String,String> codePairTs = msgMap.getCodePairTS(dto.getMsgVersion());
		
		 Map<String,String> codePairTs = cacheUtil.getCodePairTS(dto.getMsgVersion());
		if(codePairTs.isEmpty()) {
			logger.info("codePairTs is empty."+dto.getMsgVersion());
		return;
		}
		String codePair = codePairTs.get(CommFunction.byteToPreHexStr(mesFlag));
		if(codePair==null) {
			logger.info("codePair is null "+CommFunction.byteToPreHexStr(mesFlag));
			return;
					
		}
		byte codeflag = CommFunction.preHexStringToByte(codePair);
	
		try {
			logger.info("construct reponse data.");
			logger.info("receiveDto "+CommFunction.byteArrayToHexStr(receiveDto.getEquipDatas()));
		l= constructData.construct(dto.getMsgVersion(),codeflag, dto.getDeviceNo(),receiveDto.getEquipDatas(),codeMode);
		logger.info("construct reponse data success.");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error(	"construct reponse data exception",e);
			return;
		}
		logger.debug( "encode reponseMsg "+l.toString());
		
		List<byte[]> sendData = configureEncode.encodePackageContent(l, "",dto.getMsgVersion(),dto.getDeviceNo(),codeMode);
		logger.info("encode reponseMsg done.");
		if(sendData==null || sendData.size()==0) {
			logger.info("encode reponseMsg is null");
			return;
		}
		
		String msgData = CodeTransfer.byteArrayToHexStr(sendData.get(0));
		MsgOfPregateDto sendDto = new MsgOfPregateDto(dto.getDeviceNo(),codePair,msgData,dto.getMsgVersion(),dto.getPreGateIp(),dto.getPreGatePort());
		String currentT= TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
		logger.info("store sendDto begins");
		monDBService.storeOriginalData(sendDto, 1, 0, "", currentT, "");
		logger.info("msgSendToPreg begins");
		msgSend.msgSendToPreG(sendDto,false);
		
		
		if(mesFlag==0x70) {
			process70(l,dto.getDeviceNo());
		}
		
		
		logger.info("doMsgProcess ends.");
		
		}
	
	private void process70(List<UIDataContentDto> list,String deviceNo) {
		logger.info("get 0x70,insert bfCenterTransT");
		String centerFlowNum =null;
		for(UIDataContentDto uiDto:list) {
			if(uiDto.getName().equalsIgnoreCase("中心交易流水"))
				centerFlowNum= uiDto.getValue();
		}
		if(centerFlowNum.equalsIgnoreCase("")) {
			logger.info("centerFlowNum  is null");
			return ;
		}
		logger.info("centerFlowNum "+centerFlowNum);
		BfCenterTransT bfCenterTransT = getByFlowNum(centerFlowNum);
		if(bfCenterTransT==null) {
			logger.info("bfCenterTransT obj is null");
			return;
		}else {
			String s = bfCenterTransT.getCenterTransId();
			logger.info("s in redis:"+s);
			bfCenterTransT.setDeviceNo(deviceNo);
			bfCenterTransT.setStartDate(TimeConvertor.stringTime2Date(receivedTime, TimeConvertor.FORMAT_MINUS_24HOUR));
			bfCenterTransT.setEndDate(new Date());
			bfCenterTransT.setOperChannel(1);
			bfCenterTransT.setOperType(2);
			logger.info(bfCenterTransT.toString());
			callOuterInf.insertBfCenterTrans(bfCenterTransT);
			
			cacheUtil.delBfData(centerFlowNum);
		}
	}
	private void stopCharging(String deviceNo,String srcType) {
		logger.info("stopCharging : "+deviceNo+" "+srcType);
		if(srcType.equalsIgnoreCase(CHARGE_SRC_APP)) {
			String cardId = cacheUtil.getAppRelatedCard(deviceNo);
			if(cardId==null||cardId.equalsIgnoreCase("")) {
				return;
			}else {
				String relatedDeviceNo = cacheUtil.getAppCardDeviceNo(cardId);
				if(relatedDeviceNo==null||relatedDeviceNo.equalsIgnoreCase("")) {
					
				}else {
					cacheUtil.delAppCardDeviceNo(cardId);
				}
				String chargeOrderNoApp = cacheUtil.getAppCardChargeNo(cardId);
				if(chargeOrderNoApp==null||chargeOrderNoApp.equalsIgnoreCase("")) {
					
				}else {
					cacheUtil.delAppCardChargeNo(cardId);
				}
				
				cacheUtil.delAppRelatedCard(deviceNo);
				logger.info("remove cardID success");
				
			}
		}else if(srcType.equalsIgnoreCase(CHARGE_SRC_HLHT)) {
			
		}else {
			
		}
		
	}
   
/**
 * get from redis
 * @param centerFlowNum
 * @return
 */
	private BfCenterTransT getByFlowNum(String centerFlowNum) {
		// TODO Auto-generated method stub
		BfCenterTransT bfTransT =cacheUtil.getBfData(centerFlowNum);
		return bfTransT;
	}



	private DecodedMsgDto decodeReportPackage(MsgOfPregateDto dto,String codeMode) {
		
		logger.info("decodeReportPackage begins:");
		DecodedMsgDto decodedMsgDto =new DecodedMsgDto();
		try {
			
			decodedMsgDto =	configureDecode.DecodeReportPackageContent(Encodes.decodeHex(dto.getMsgData()), "a", 1, PHRASE, codeMode,dto.getDeviceNo(),
					dto.getMsgVersion());
		storeOriginalData(dto,decodedMsgDto.getDecodedMsg());
			return decodedMsgDto;

		} catch (Exception e) {
			logger.info("MessCheck:doDecodeProcess1 exception:" + e.getMessage());
		}
		return null;
	}
	/**
	 * store fault and send 05
	 * @param msgCode
	 * @param parsedMsgData
	 * @param deviceVersion
	 * @param deviceNo
	 * @param inDate
	 */
	private boolean process3D(String msgCode,String deviceVersion,String deviceNo,String inDate) {
		logger.info("check3D");
		boolean b = false;
		List<MonFaultRecordDto> list = cacheUtil.getFaultData(deviceNo);
		if(list.size()==0 || list.isEmpty()) {
			logger.info("no faultRecord in redis");
		}else {
			logger.info("get faultRecord from redis"+list.size());
			for(MonFaultRecordDto dto:list) {
				
				monDBService.setDeviceFaultInfo(dto);
				b=true;
			}
		}
		logger.info("del faultRecord in redis");
		cacheUtil.rmFaultData(deviceNo);
		
		return b;
		
	}
	private void sendStopChargingMsg(boolean isSend,MsgOfPregateDto dto) {
		logger.info("stop charging."+isSend);
		if(isSend) {
		msgSend.msgSendToPreG(constructMsgUtil.stopCharging(dto), false);
		}
	}
private boolean storeOriginalData(MsgOfPregateDto dto,String[] decodedData) {
	logger.info("storeOriginalData begins:");
	//if(decodedData.length!=2) {
	//	logger.info("decoded data is wrong.");
//		return false;
//	}

	int tr;
	String trc;

	
	if(decodedData[0].equals(null) ||decodedData[0].equals("")) {
		tr =0;//ok
		trc = null;
	}else{
		
		tr =1;//fail
		trc=decodedData[0];
	}
	int messType = msgCodeUtil.getMessTypeByCode(dto.getMsgCode(),dto.getMsgVersion());
	if(messType ==0) {
		logger.info("messType is wrong");
		return false;
	}
	monDBService.storeOriginalData(dto, messType, tr, trc, receivedTime, decodedData[1]);
	logger.info("storeOriginalData ends.");
		return true;
}

@CachePut(cacheNames= "collect-processData", key="#root.caches[0].name+#deviceNo")
public MonChargingProcessDataDto upCPProcessData(String deviceNo){
	MonChargingProcessDataDto dto = new MonChargingProcessDataDto();
	//dto.setChargeStatus(chargeStatus);//79中的充电状态。 1/2/3/80.

	return dto;
}

private void upCPStatusCharging(String deviceNo) {
	logger.info("upCPStatusCharging");
	MonChargingStatus4App statusData = cacheUtil.getStatusData(deviceNo);
	
	statusData.setIsTakeUp("no");
	cacheUtil.setStatusData(statusData);
}
private void upCPProcDataStatus(String deviceNo,String normalEnd) {
	MonChargingProcessDataDto procData =cacheUtil.getCPProcessData(deviceNo);
	
	
	procData.setChargeStatus(normalEnd);//收到状态数据时，状态是充电中。
	cacheUtil.upCPProcessData(procData);
}
}
