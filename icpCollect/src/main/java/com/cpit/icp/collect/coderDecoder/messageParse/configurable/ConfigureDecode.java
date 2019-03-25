package com.cpit.icp.collect.coderDecoder.messageParse.configurable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;

//import com.cpit.testplatform.modules.datavalidation.service.TestMgmt;
import com.cpit.icp.collect.coderDecoder.common.configurable.MessageInfo;
import com.cpit.icp.collect.coderDecoder.common.configurable.deviceInfo;
import com.cpit.icp.dto.collect.coderDecoder.CheckResultDto;
import com.cpit.icp.dto.collect.coderDecoder.DecodedMsgDto;
import com.cpit.icp.dto.collect.coderDecoder.FormatTestResultDto;
import com.cpit.icp.dto.collect.coderDecoder.PackageDto;
import com.cpit.icp.collect.coderDecoder.util.*;
import com.cpit.icp.collect.coderDecoder.util.configurable.ConfigTool;
import com.cpit.icp.collect.coderDecoder.util.configurable.DecodeRechargeDataDomainXml;
import com.cpit.icp.collect.coderDecoder.util.exception.ReceiveDataException;
//import com.cpit.icp.collect.impl.OriginalDataStoreMgmt;
//simport com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.utils.CodeTransfer;
//import com.cpit.icp.collect.coderDecoder.common.*;
//import com.cpit.testplatform.modules.test.dao.TerminalDao;
import com.cpit.icp.collect.utils.cache.CacheUtil;

//import com.cpit.testplatform.modules.test.entity.HistroyChargeProcess;
//import com.cpit.testplatform.modules.test.entity.HistroyChargeRecord;
//import com.cpit.testplatform.modules.test.entity.HistroyEvent;
//import com.cpit.testplatform.modules.test.entity.HistroyFault;
//import com.cpit.testplatform.modules.test.entity.JavaBean10;
//import com.cpit.testplatform.modules.test.entity.TerminalFaultMessage;


//import com.cpit.testplatform.modules.test.service.TestResultManaService;
//import com.cpit.testplatform.modules.test.service.TestResultService;

//import com.cpit.icp.collect.coderDecoder.util.StringUtils;

/**
 * @author maming 可配置解析
 * @update liangzhiyuan
 */
@Service
public class ConfigureDecode {
	private final static Logger logger = LoggerFactory.getLogger(ConfigureDecode.class);
	//private static final String LOG = ConfigureDecode.class.getName();

	private List<PackageDto> lPackageContentDto = null;	
	
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
	private Date startDate;

	//@Autowired
	//private TestResultService systemService;
//	@Autowired MonDBService monDBService;
	
	//@Autowired
	//private TerminalDao terminaldto;
	//命令代码集合
	static byte[] allDecodeMesCommand = { 0x10, 0x11, 0x15, 0x17, 0x18, 0x31, 0x34, 0x35, 0x36, 0x39, 0x3A, 0x3B, 0x50, 0x51,
			0x52, 0x53, 0x56, 0x58, 0x59, 0x70, 0x71, 0x72, 0x74, 0x75, 0x78, 0x7A, (byte) 0x92, (byte) 0x96,
			(byte) 0xB3, (byte) 0x97, (byte) 0x98, 0x3B, (byte) 0xD1, (byte) 0xD2, (byte) 0xD3, (byte) 0xF0,
			(byte) 0xF1, (byte) 0xF2, (byte) 0x93, (byte) 0x79, (byte) 0x1A, (byte) 0x7B };
	
	private ExecutorService threadPool = Executors.newFixedThreadPool(5);//store db process thread pool

	//@Autowired
	//private TestResultManaService testResultManaService;
	public ConfigureDecode() {
		this.lPackageContentDto = new ArrayList<PackageDto>();
	}
	
	// 获取报文数组；
	public List<PackageDto> getPackateDto() {
		return this.lPackageContentDto;
	}
	
	// 解析命令代码1
	public byte DecodeCommand(byte[] Receive) {
		//解析报文(区分版本)
		Byte version = Receive[3];
		if((version.equals((byte) 0x80))){
			return Receive[5];
		}else{
			return Receive[22];
		}
	}
	// 解析命令代码2
	public String Decode2Command(byte[] Receive) {
		//解析报文(区分版本)
		Byte version = Receive[3];
		if((version.equals((byte) 0x80))){
			return CommFunction.byteToHexStr2(Receive[5]);
		}else{
			return CommFunction.byteToHexStr2(Receive[22]);
		}
	}
	
	// 校验报文格式，如果报文的格式不正确，则不再做下面的数据检验，返回错误；
	/**
	 * @param isStorage  是否入库
	 * @param Receive	 接受报文
	 * @param equipName  终端名称
	 * @param timeinterval  响应时间
	 * @return
	 */
	public FormatTestResultDto DecodePackageStyle(boolean isStorage, byte[] Receive, String equipName, int timeinterval) {
		int testResult =0;
		FormatTestResultDto formatTestResultDto = new FormatTestResultDto();
		int eqipDatalen = 0;//数据域的长度
		byte OrderCode = 0;//命令代码域
		byte[] equipmentData = null;//数据域
		
		//校验报文格式
		Byte version = Receive[3];
		String info = "";
		int templen;
		switch (version) {
		case (byte) 0x80:
			//判断报文长度是否正确
			if(Receive.length < 7){
				info=MessageInfo.validate_lenght;
				testResult=1;
				formatTestResultDto.setTestResultInfo(info);
				break;
			}
			//判断长度域是否正确
			templen = CommFunction.unsignByteToInt(Receive[2]);
			if(templen != Receive.length-4){
				info=MessageInfo.validate_lenghtDomain;
				testResult=1;
				formatTestResultDto.setTestResult(testResult);
				formatTestResultDto.setTestResultInfo(info);
				break;
			}
			//判断检验和是否正确
			byte[] tempChecksum = new byte[Receive.length-6];
			ArraysN.copy(tempChecksum, 0, Receive, 5, Receive.length-6);
			if(!(CommFunction.byteToHexStr2(Receive[Receive.length - 1])
				.equalsIgnoreCase(CommFunction.SumCheck2(tempChecksum)))){
				info=MessageInfo.validate_checkSum;
				testResult=1;
				formatTestResultDto.setTestResultInfo(info);
				break;
			}
			OrderCode = Receive[5];
			eqipDatalen = templen-3;
			equipmentData = ConfigTool.subBytes(tempChecksum, 1, tempChecksum.length-1);
			break;
		case (byte) 0x35:
			//判断报文长度是否正确
			if(Receive.length < 24){
				info=MessageInfo.validate_lenght;
				testResult=1;
				formatTestResultDto.setTestResultInfo(info);
				break;
			}
			//判断长度域是否正确
			templen = CommFunction.unsignByteToInt(Receive[2]);
			if(templen != Receive.length-4){
				testResult=1;
				info=MessageInfo.validate_lenghtDomain;
				formatTestResultDto.setTestResultInfo(info);
				break;
			}
			//判断检验和是否正确
			byte[] tempChecksum2 = new byte[Receive.length-23];
			ArraysN.copy(tempChecksum2, 0, Receive, 22, Receive.length-23);
			if(!(CommFunction.byteToHexStr2(Receive[Receive.length - 1])
				.equalsIgnoreCase(CommFunction.SumCheck2(tempChecksum2)))){
				info=MessageInfo.validate_checkSum;
				testResult =1;
				formatTestResultDto.setTestResultInfo(info);
				break;
			}
			OrderCode = Receive[22];
			eqipDatalen = templen-20;
			equipmentData = ConfigTool.subBytes(tempChecksum2, 1, tempChecksum2.length-1);
			break;
		default:
			info=MessageInfo.validate_version;
			
			formatTestResultDto.setTestResultInfo(info);
			break;
		}
		if(info.length()!=0){
			testResult=1;
			logger.debug("msgData formatTest failed: "+info);
		//	storageMessage(1,false,info,equipName, timeinterval, Receive,"00","");
			//return false;
		}
		
		// 结果入库
	//	if(isStorage){
		//	storageMessage(1,true,"",equipName, 0, Receive,Decode2Command(Receive),"");
	//	}
		//else{
			// 装包
			PackageDto pdto = new PackageDto();
			pdto.setLenthDomain(Receive[2]);
			pdto.setEqipDataLen(eqipDatalen);
			pdto.SetVerDomain(Receive[3]);
			pdto.setSerilNumDomain(Receive[4]);
			pdto.SetOrderCode(OrderCode);
			pdto.setEquipDatas(equipmentData);
			pdto.setCheckSumDomain(Receive[Receive.length - 1]);
			pdto.setMessageLen(Receive.length);
			pdto.setPackageDatas(Receive);
			this.lPackageContentDto.clear();
			this.lPackageContentDto.add(pdto);
		//}
		//return true;
			
			formatTestResultDto.setMsgCode(CodeTransfer.byteToHexStr(OrderCode,true));
			formatTestResultDto.setTestResult(testResult);
			formatTestResultDto.setPackageDto(pdto);
			return formatTestResultDto;
			
	}
	
	
	// 解析上报报文数据域的内容,T2，T3阶段测试 
	public DecodedMsgDto DecodeReportPackageContent(byte[] ReceiveBuffer, String equipName, int timeinterval,int phrase,String ChineseEncoding,String deviceNo,String... args)
			throws ReceiveDataException {
		
		logger.info( "received original Msg: " + CommFunction.LogByteArray(ReceiveBuffer).toString());
		DecodedMsgDto decodedMsgDto = new DecodedMsgDto();
		String[] res = new String[2];
		FormatTestResultDto formatTR= DecodePackageStyle(false,ReceiveBuffer, equipName,timeinterval);
		if (formatTR.getTestResult() ==1) {
			res[0]=formatTR.getTestResultInfo();
			res[1]="";
			decodedMsgDto.setDecodedMsg(res);
			decodedMsgDto.setPackageDto(formatTR.getPackageDto());
			return decodedMsgDto;
		}
		String version;
		if(args.length!=0){
			version=args[0];
		}else{
			res[0]="";
			res[1]="";
			decodedMsgDto.setDecodedMsg(res);
			decodedMsgDto.setPackageDto(formatTR.getPackageDto());
		return decodedMsgDto;	
		}
		if(ChineseEncoding==null||ChineseEncoding.length()==0){
			ChineseEncoding = "GB2312";
		}
		// 没有多个情况，一次一个报文
		//PackageDto lPackage = getPackateDto().get(0);
		
		PackageDto lPackage = formatTR.getPackageDto();
		try {
			
			String[] decodedContent = DecodeRechargeDataDomainXml.ParseData(lPackage.getEquipDatas(),lPackage.getOrderCode(),version,ChineseEncoding,deviceNo);
		//	storageMessage(phrase,true,decodedContent[0],equipName, 0, ReceiveBuffer, CommFunction.byteToHexStr2(lPackage.getOrderCode()),decodedContent[1]);
			decodedMsgDto.setDecodedMsg(decodedContent);
			decodedMsgDto.setPackageDto(formatTR.getPackageDto());
			return decodedMsgDto;
			//storeDB(version, lPackage.getOrderCode(),equipName,decodedContent);
		} catch (Exception e) {
			logger.error( "error in DecodeReportPackageContent", e);
			return null;
		}
	}
	//zqq 180731  cancel history commands store to DB
	/** zqq cancel 180731
	 * used to stored decoded info into different table, such as history, fault etc.
	 * @param decodedContent
	 
	private void storeDB(final String version, final Byte orderCode, final String equipName, final String[] decodedContent){
		threadPool.execute(new Runnable(){
			@Override
			public void run() {
				// error
				if(decodedContent == null || decodedContent[0].length()!=0){ 
					return;
				}				
				// stored historycharge
				processHistoryChargeRecord(version,orderCode,equipName,decodedContent[1]);			
				// stored historyevent
				processHistoryEvent(version,orderCode,equipName,decodedContent[1]);
				// stored historyfault
				processHistoryFault(version,orderCode,equipName,decodedContent[1]);
				// stored historychargeprocess
				processHistoryChargeProcess(version,orderCode,equipName,decodedContent[1]);
				processFaultMessage(version,orderCode,equipName,decodedContent[1]);
			}
			
		});
	}
	*/
	/* zqq cancel 180731
	private void processFaultMessage(String version,Byte orderCode,String equipName,String decodedContent){
	
	
		
		
		if(orderCode.equals((byte)0x11)){
			String faultStatus = TestMgmt.getFaultValue(decodedContent, "系统故障状态");
			byte[] bpara = CommFunction.hexStringToBytes(faultStatus.replace(" ", ""));
			byte[] bfault = CommFunction.reserveByteArray(bpara);
	//	insertTestFault(bfault,faultStatus, equipName);
		//String strFault = MessgeTransfer.SystemFaultbyteToHexStr(bpara, 3)[1];
List<String> sFault = MessgeTransfer.systemFaultToStr(bpara, 3);
for(String  s:sFault){
	 insertTestFault(bfault,s, equipName);
}
	      
		}
		if(orderCode.equals((byte)0x31)){
       String faultStatus = TestMgmt.getFaultValue(decodedContent, "充电系统故障状态");
     	byte[] bpara = CommFunction.hexStringToBytes(faultStatus.replace(" ", ""));
		byte[] bfault = CommFunction.reserveByteArray(bpara);
	
		List<String> strFault = MessgeTransfer.chargeFaultToStr(bpara, 3);
	//String strFault = MessgeTransfer.ChargeSystemFaultbyteToHexStr(bpara, 3)[1];
for(String s:strFault){
	 insertTestFault(bfault,s, equipName);
}
      
     
		}
		if((orderCode.equals((byte)0x3C))){
			String chargeGunNums =  TestMgmt.getFaultValue(decodedContent, "充电枪数量");
			for(int i=0;i<Integer.parseInt(chargeGunNums);i++){
				String chargeGunFaultStatusCode =  TestMgmt.getFaultValue(decodedContent, "充电枪位置"+i+"充/放电模块故障代码");
				byte[] bpara = CommFunction.hexStringToBytes(chargeGunFaultStatusCode.replace(" ", ""));
			byte[] bfault = CommFunction.reserveByteArray(bpara);
			List<String> strFault = MessgeTransfer.moduleFaultAnalyze(bpara, 3);
			
		for(String s:strFault){
			 insertTestFault(bfault,s, equipName);
		}
			
			}
		}
		if((orderCode.equals((byte)0x34))){
			String chargeGunNums =  TestMgmt.getFaultValue(decodedContent, "充电枪数量");
			for(int i=0;i<Integer.parseInt(chargeGunNums);i++){
				String chargeGunFaultStatusCode =  TestMgmt.getFaultValue(decodedContent, "充电枪位置"+i+"充电模块故障代码");
				byte[] bpara = CommFunction.hexStringToBytes(chargeGunFaultStatusCode.replace(" ", ""));
			byte[] bfault = CommFunction.reserveByteArray(bpara);
			List<String> strFault = MessgeTransfer.moduleFaultAnalyze(bpara, 3);
			
			for(String s:strFault){
				 insertTestFault(bfault,s, equipName);
			}
			//String strFault = MessgeTransfer.ModuleFaultbyteToHexStr(bpara, 3)[1];
			//insertTestFault(bfault,strFault, equipName);
			}
		}
		if(orderCode.equals((byte)0x35)){
			String chargeGunNums =  TestMgmt.getFaultValue(decodedContent, "充电枪数量");
			for(int i=0;i<Integer.parseInt(chargeGunNums);i++){
				
				//-------battery fault code
				List<String> batteryFaultCode =  TestMgmt.get35FaultValue(decodedContent, "电池故障代码");
				if(batteryFaultCode == null){
					LoggerOperator.info(LOG, "35 电池故障代码为空");
					return;
				}
				for(int j=0;j<batteryFaultCode.size();j++){
					byte[] bpara = CommFunction.hexStringToBytes(batteryFaultCode.get(j).replace(" ", ""));
					byte[] bfault = CommFunction.reserveByteArray(bpara);
					
					List<String> sFaults = MessgeTransfer.batterySystemFaultbyteToHexStr(bpara, 3);
					for(String strFault:sFaults){
						insertTestFault(bfault,strFault, equipName);
					}
					//String strFault = MessgeTransfer.BatterySystemFaultbyteToHexStr(bpara, 3)[1];
					
				}
				
				//------battery System fault code
				List<String> batterySystemFaultCode =  TestMgmt.get35FaultValue(decodedContent, "电池系统总故障代码");
				if(batterySystemFaultCode == null){
					LoggerOperator.info(LOG, "35 电池系统故障代码为空");
					return;
				}
				for(int j=0;j<batterySystemFaultCode.size();j++){
					byte[] bpara = CommFunction.hexStringToBytes(batterySystemFaultCode.get(j).replace(" ", ""));
					byte[] bfault = CommFunction.reserveByteArray(bpara);
					
					List<String> strFault = MessgeTransfer.batterySystemFaultbyteToHexStr(bpara, 3);
					if(strFault.size()==0){
						LoggerOperator.info(LOG, "35 get battery is null;");
						return;
					}
					for(String s:strFault){
						insertTestFault(bfault,s, equipName);
					}
					
				}
				
				//-------bms fault code
				List<String> BMSFaultCode =  TestMgmt.get35FaultValue(decodedContent, "BMS故障代码");
				if(BMSFaultCode == null){
					LoggerOperator.info(LOG, "BMS故障故障代码");
					return;
				}
				for(int j=0;j<BMSFaultCode.size();j++){
					byte[] bpara = CommFunction.hexStringToBytes(BMSFaultCode.get(j).replace(" ", ""));
					byte[] bfault = CommFunction.reserveByteArray(bpara);
					List<String> strFault = MessgeTransfer.batterySystemFaultbyteToHexStr(bpara, 3);
					if(strFault.size()==0){
						LoggerOperator.info(LOG, "35 get battery is null;");
						return;
					}
					for(String s:strFault){
						insertTestFault(bfault,s, equipName);
					}
					//String strFault = MessgeTransfer.BatterySystemFaultbyteToHexStr(bpara, 3)[1];
					//insertTestFault(bfault,strFault, equipName);
				}
				
			}
		}
		
	}
	*/
	/* zqq cancel 180731
	private void  insertTestFault(byte[] bfault,String strFault,String equipmentName){
		TerminalFaultMessage testFault = new TerminalFaultMessage();
		testFault.setTerminal_name(equipmentName);
		testFault.setMessage_time(new Date());
		testFault.setFaultCode(CommFunction.bytesToHexString(bfault));
		String[] faults = StringUtils.split(strFault, "；");
		
		if (faults.length == 9) {
			testFault.setEntity(faults[0]);
			testFault.setType(faults[1]);
			testFault.setNote(faults[2]);
			testFault.setAttribute(faults[3]);
			testFault.setIs_operation(faults[4]);
			testFault.setGrade(faults[5]);
			testFault.setTreate(faults[6]);
			testFault.setDisplay(faults[7]);
			testFault.setSysClass(faults[8]);
			testResultManaService.insertTestFault(testFault);
		
		}
	}
	*/
	/*
	zqq cancel 180731
	private void processHistoryChargeRecord(String version, Byte orderCode, String equipName, String decodedContent){
		if(!orderCode.equals((byte)0x93) && !orderCode.equals((byte)0x94)){
			return;
		}	
		HistroyChargeRecord  hisChargeRecord = new HistroyChargeRecord();
		hisChargeRecord.setMessage_time(new Date());
		hisChargeRecord.setTerminal_name(equipName);
		if(version.equals("3.4")){
			int recordCount = Integer.parseInt(TestMgmt.getValue(decodedContent, "记录数"));
			for(int i=0; i<recordCount;i++){
				hisChargeRecord.setRecordID(Integer.parseInt(TestMgmt.getValue(decodedContent, "记录序号"+i)));
				hisChargeRecord.setCharge_type(TestMgmt.getValue(decodedContent, "充电枪位置充电类型"+i));	
				hisChargeRecord.setCharge_pile_number(TestMgmt.getValue(decodedContent, "充电桩编号"+i));
				hisChargeRecord.setCard(TestMgmt.getValue(decodedContent, "卡号"+i));	
				hisChargeRecord.setCenter_transaction_serial_number(TestMgmt.getValue(decodedContent, "中心交易流水号"+i));	
				hisChargeRecord.setCharge_reservation_number(TestMgmt.getValue(decodedContent, "充电预约序号"+i));
				hisChargeRecord.setCarVIN(TestMgmt.getValue(decodedContent, "车辆VIN "+i));
				hisChargeRecord.setCarNo(TestMgmt.getValue(decodedContent, "车牌号"+i));		
				hisChargeRecord.setStart_soc(TestMgmt.getValue(decodedContent, "开始充电SOC "+i));
				hisChargeRecord.setEnd_soc(TestMgmt.getValue(decodedContent, "结束充电SOC "+i));
				hisChargeRecord.setCharge_quantity(TestMgmt.getValue(decodedContent, "本次累计充电量"+i));
				hisChargeRecord.setCharge_energy(TestMgmt.getValue(decodedContent, "本次累计充电能"+i));
				hisChargeRecord.setCharge_time(TestMgmt.getValue(decodedContent, "充电时间长度"+i));			
				hisChargeRecord.setFull_strategy(TestMgmt.getValue(decodedContent, "充电满策略"+i));
				hisChargeRecord.setIs_normal(TestMgmt.getValue(decodedContent, "是否正常结束"+i));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");  
				try {
					if(null != TestMgmt.getValue(decodedContent, "开始日期时间"+i)){
						Date date1 = sdf.parse(TestMgmt.getValue(decodedContent, "开始日期时间"+i));
						hisChargeRecord.setStart_time(date1);
					}
					if(null != TestMgmt.getValue(decodedContent, "结束日期时间"+i)){
						Date date2 = sdf.parse(TestMgmt.getValue(decodedContent, "结束日期时间"+i));
						hisChargeRecord.setEnd_time(date2);
					}		
				} catch (ParseException e) {
					LoggerOperator.err(LOG, "historychargerecord parse date error!",e);
				}  	    
				hisChargeRecord.setStart_meter_readings(TestMgmt.getValue(decodedContent, "开始电表度数"+i));
				hisChargeRecord.setEnd_meter_readings(TestMgmt.getValue(decodedContent, "结束电表度数"+i));
				testResultManaService.InsertHistoryChargeRecord(hisChargeRecord);
			}	
		}else if(version.equals("3.5")){
			hisChargeRecord.setRecordID(Integer.parseInt(TestMgmt.getValue(decodedContent, "记录序号")));
			hisChargeRecord.setCharge_type(TestMgmt.getValue(decodedContent, "充电枪位置充/放电类型"));
			hisChargeRecord.setCharge_pile_number(TestMgmt.getValue(decodedContent, "充电桩编号"));
			hisChargeRecord.setCard(TestMgmt.getValue(decodedContent, "卡号"));	
			hisChargeRecord.setCenter_transaction_serial_number(TestMgmt.getValue(decodedContent, "中心交易流水号"));	
			hisChargeRecord.setCharge_reservation_number(TestMgmt.getValue(decodedContent, "充电预约编号"));
			hisChargeRecord.setCarVIN(TestMgmt.getValue(decodedContent, "车辆VIN"));
			hisChargeRecord.setCarNo(TestMgmt.getValue(decodedContent, "车牌号"));		
			hisChargeRecord.setStart_soc(TestMgmt.getValue(decodedContent, "开始充/放电SOC"));
			hisChargeRecord.setEnd_soc(TestMgmt.getValue(decodedContent, "结束充/放电SOC"));
			hisChargeRecord.setCharge_quantity(TestMgmt.getValue(decodedContent, "本次累计充/放电量"));
			hisChargeRecord.setCharge_energy(TestMgmt.getValue(decodedContent, "本次累计充/放电能"));
			hisChargeRecord.setCharge_time(TestMgmt.getValue(decodedContent, "充/放电时间长度"));		
			hisChargeRecord.setFull_strategy(TestMgmt.getValue(decodedContent, "充电满策略"));
			hisChargeRecord.setIs_normal(TestMgmt.getValue(decodedContent, "是否正常结束"));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");  
			try {
				if(null != TestMgmt.getValue(decodedContent, "开始日期时间")){
					Date date1 = sdf.parse(TestMgmt.getValue(decodedContent, "开始日期时间"));
					hisChargeRecord.setStart_time(date1);
				}
				if(null != TestMgmt.getValue(decodedContent, "结束日期时间")){
					Date date2 = sdf.parse(TestMgmt.getValue(decodedContent, "结束日期时间"));
					hisChargeRecord.setEnd_time(date2);
				}				
			} catch (ParseException e) {
				LoggerOperator.err(LOG, "historychargerecord parse date error!",e);
			}  	    
			hisChargeRecord.setStart_meter_readings(TestMgmt.getValue(decodedContent, "开始电表度数"));
			hisChargeRecord.setEnd_meter_readings(TestMgmt.getValue(decodedContent, "结束电表度数"));
			testResultManaService.InsertHistoryChargeRecord(hisChargeRecord);
		}			
	}
	*/
	/*
	zqq cancel 180731
	
	private void processHistoryEvent(String version, Byte orderCode, String equipName, String decodedContent){
		if(!orderCode.equals((byte)0x96)){
			return;
		}	
		HistroyEvent hisEvent = new HistroyEvent();
		hisEvent.setMessage_time(new Date());
		hisEvent.setTerminal_name(equipName);	
		int recordCount = Integer.parseInt(TestMgmt.getValue(decodedContent, "记录数"));
		for(int i=0; i<recordCount; i++){
			hisEvent.setRecordID(TestMgmt.getValue(decodedContent, "记录序号"+i));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");  
			try {
				if(null != TestMgmt.getValue(decodedContent, "时间"+i)){
					Date date = sdf.parse(TestMgmt.getValue(decodedContent, "时间"+i));
					hisEvent.setEvent_time(date);
				}			
			} catch (ParseException e) {
				LoggerOperator.err(LOG, "historyevent parse date error!",e);
			}  	  
			hisEvent.setType(TestMgmt.getValue(decodedContent, "类型代码"+i));
			testResultManaService.InsertHistoryEventRecord(hisEvent);
		}		
	}
	*/
	/*
	 * zqq cancel 180731
	private void processHistoryFault(String version, Byte orderCode, String equipName, String decodedContent){
		if(!orderCode.equals((byte)0x97)){
			return;
		}	
		HistroyFault hisFault = new HistroyFault();
		hisFault.setMessage_time(new Date());
		hisFault.setTerminal_name(equipName);	
		hisFault.setQuery_type(TestMgmt.getValue(decodedContent, "查询类型"));
		int recordCount = Integer.parseInt(TestMgmt.getValue(decodedContent, "记录数"));
		for(int i=1; i<recordCount+1; i++){
			hisFault.setRecordID(TestMgmt.getValue(decodedContent, "记录序号"+i));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");  
			try {
				if(null != TestMgmt.getValue(decodedContent, "时间年"+i) && null != TestMgmt.getValue(decodedContent, "月"+i)&&
						null != TestMgmt.getValue(decodedContent, "日"+i) && null != TestMgmt.getValue(decodedContent, "时"+i)
						&& null != TestMgmt.getValue(decodedContent, "分"+i) && null != TestMgmt.getValue(decodedContent, "秒"+i)){
					String strTime = TestMgmt.getValue(decodedContent, "时间年"+i)+"-"+TestMgmt.getValue(decodedContent, "月"+i)
							+"-"+TestMgmt.getValue(decodedContent, "日"+i)+" "+TestMgmt.getValue(decodedContent, "时"+i)+"-"+
							TestMgmt.getValue(decodedContent, "分"+i)+"-"+TestMgmt.getValue(decodedContent, "秒"+i);
					Date date = sdf.parse(strTime);
					hisFault.setFault_time(date);
				}				
			} catch (ParseException e) {
				LoggerOperator.err(LOG, "historyfault parse date error!",e);
			}  	  
			hisFault.setModule_id(TestMgmt.getValue(decodedContent, "模块编号"+i));
			hisFault.setFault_code(TestMgmt.getValue(decodedContent, "故障代码"+i));
			testResultManaService.InsertHistoryFaultRecord(hisFault);
		}		
	}
	*/
	/*zqq cancel 180731
	private void processHistoryChargeProcess(String version, Byte orderCode, String equipName, String decodedContent){
		if(!orderCode.equals((byte)0x98)){
			return;
		}	
		HistroyChargeProcess hisChargeProcess = new HistroyChargeProcess();
		hisChargeProcess.setMessage_time(new Date());
		hisChargeProcess.setTerminal_name(equipName);	
		hisChargeProcess.setTerminal_id(TestMgmt.getValue(decodedContent, "充电桩编号1"));	
		int recordCount = Integer.parseInt(TestMgmt.getValue(decodedContent, "记录数"));
		if(version.equals("3.4")){
			for(int i=0; i<recordCount; i++){
				hisChargeProcess.setRecordID(Integer.parseInt(TestMgmt.getValue(decodedContent, "记录序号"+i)));
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");  
				try {
					if(null != TestMgmt.getValue(decodedContent, "时间"+i)){
						Date date = sdf.parse(TestMgmt.getValue(decodedContent, "时间"+i));
						hisChargeProcess.setCharge_time(date);
					}				
				} catch (ParseException e) {
					LoggerOperator.err(LOG, "historychargeprocess parse date error!",e);
				}  	  
				hisChargeProcess.setType(TestMgmt.getValue(decodedContent, "充电枪位置充电类型"+i));
				hisChargeProcess.setMax_voltage(TestMgmt.getValue(decodedContent, "最高允许输出电压"+i));
				hisChargeProcess.setMin_voltage(TestMgmt.getValue(decodedContent, "最低允许输出电压"+i));
				hisChargeProcess.setMax_current(TestMgmt.getValue(decodedContent, "最大允许输出电流"+i));
				hisChargeProcess.setTotal_degree(TestMgmt.getValue(decodedContent, "累计充电Kwh "+i));
				hisChargeProcess.setTotal_ah(TestMgmt.getValue(decodedContent, "累计充电Ah "+i));
				hisChargeProcess.setTotal_time(TestMgmt.getValue(decodedContent, "累计充电时间"+i));
				hisChargeProcess.setStart_soc(TestMgmt.getValue(decodedContent, "充电起始SOC "+i));
				hisChargeProcess.setCurrent_soc(TestMgmt.getValue(decodedContent, "当前SOC "+i));
				hisChargeProcess.setInsert_state(TestMgmt.getValue(decodedContent, "充电枪插入状态"+i));
				hisChargeProcess.setVoltage(TestMgmt.getValue(decodedContent, "充电电压"+i));
				hisChargeProcess.setCurrent(TestMgmt.getValue(decodedContent, "充电电流"+i));
				hisChargeProcess.setPower(TestMgmt.getValue(decodedContent, "输出功率"+i));
				hisChargeProcess.setProtocal(TestMgmt.getValue(decodedContent, "通信协议版本号"+i));
				hisChargeProcess.setPotevio_protocal(TestMgmt.getValue(decodedContent, "普天通讯协议版本"+i));
				hisChargeProcess.setBattery_capacity(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池额定容量"+i));
				hisChargeProcess.setBattery_voltage(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池额定总电压"+i));
				hisChargeProcess.setBattery_num(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池箱数 n "+i));
				hisChargeProcess.setBattery_series_num(TestMgmt.getValue(decodedContent, "整车/分箱电池串联数"+i));
				hisChargeProcess.setBattery_parallel_num(TestMgmt.getValue(decodedContent, "整车/分箱电池并联数"+i));
				hisChargeProcess.setBattery_sample_num(TestMgmt.getValue(decodedContent, "电池温度采集点数"+i));
				hisChargeProcess.setBattery_charge_voltage(TestMgmt.getValue(decodedContent, "蓄电池模块最高允许充电电压"+i));
				hisChargeProcess.setBattery_charge_current(TestMgmt.getValue(decodedContent, "最高允许充电电流"+i));
				hisChargeProcess.setCapacity(TestMgmt.getValue(decodedContent, "标称总容量"+i));
				hisChargeProcess.setCharge_voltage(TestMgmt.getValue(decodedContent, "最高允许充电总电压"+i));
				hisChargeProcess.setHigh_temperature(TestMgmt.getValue(decodedContent, "最高允许蓄电池温度"+i));
				hisChargeProcess.setSoh(TestMgmt.getValue(decodedContent, "SOH "+i));
				hisChargeProcess.setSoc(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池荷电状态"+i));
				hisChargeProcess.setTotal_voltage(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池总电压"+i));
				hisChargeProcess.setRequire_voltage(TestMgmt.getValue(decodedContent, "电池组需求总电压"+i));
				hisChargeProcess.setRequire_current(TestMgmt.getValue(decodedContent, "电池组需求总电流"+i));
				hisChargeProcess.setVoltage_measure(TestMgmt.getValue(decodedContent, "充电电压测量值"+i));
				hisChargeProcess.setCurrent_measure(TestMgmt.getValue(decodedContent, "充电电流测量值"+i));
				hisChargeProcess.setTemperature_low(TestMgmt.getValue(decodedContent, "电池组最低温度"+i));
				hisChargeProcess.setTemperature_high(TestMgmt.getValue(decodedContent, "电池组最高温度"+i));
				hisChargeProcess.setFull_time(TestMgmt.getValue(decodedContent, "估算充满时间/min "+i));
				hisChargeProcess.setBms_suspend_reason(TestMgmt.getValue(decodedContent, "BMS 中止充电原因"+i));
				hisChargeProcess.setSingle_voltage_min(TestMgmt.getValue(decodedContent, "电池单体最低电压"+i));
				hisChargeProcess.setSingle_voltage_max(TestMgmt.getValue(decodedContent, "电池单体最高电压"+i));
				hisChargeProcess.setBms_id(TestMgmt.getValue(decodedContent, "BMS ID "+i));
				hisChargeProcess.setError(TestMgmt.getValue(decodedContent, "错误数据"+i));
				hisChargeProcess.setCar_vin(TestMgmt.getValue(decodedContent, "车辆VIN "+i));
				hisChargeProcess.setNum(TestMgmt.getValue(decodedContent, "电池箱数"+i));
				int dianchixiangCount = Integer.parseInt(TestMgmt.getValue(decodedContent, "电池箱数"+i));
				for(int j=0; j<dianchixiangCount&& j<8;j++){
					setHistoryProcessBattery(hisChargeProcess,j,TestMgmt.getValue(decodedContent, "电池箱 ID "+j));				
				}
				testResultManaService.InsertHistoryProcessRecord(hisChargeProcess);
			}		
		}else if(version.equals("3.5")){
			hisChargeProcess.setRecordID(Integer.parseInt(TestMgmt.getValue(decodedContent, "记录序号1")));
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss");  
			try {
				if(null != TestMgmt.getValue(decodedContent, "时间")){
					Date date = sdf.parse(TestMgmt.getValue(decodedContent, "时间"));
					hisChargeProcess.setCharge_time(date);
				}			
			} catch (ParseException e) {
				LoggerOperator.err(LOG, "historychargeprocess parse date error!",e);
			}  	  
			hisChargeProcess.setType(TestMgmt.getValue(decodedContent, "充电枪位置充电类型"));
			hisChargeProcess.setMax_voltage(TestMgmt.getValue(decodedContent, "最高允许输出电压"));
			hisChargeProcess.setMin_voltage(TestMgmt.getValue(decodedContent, "最低允许输出电压"));
			hisChargeProcess.setMax_current(TestMgmt.getValue(decodedContent, "最大允许输出电流"));
			hisChargeProcess.setTotal_degree(TestMgmt.getValue(decodedContent, "累计充电Kwh"));
			hisChargeProcess.setTotal_ah(TestMgmt.getValue(decodedContent, "累计充电Ah"));
			hisChargeProcess.setTotal_time(TestMgmt.getValue(decodedContent, "累计充电时间"));
			hisChargeProcess.setStart_soc(TestMgmt.getValue(decodedContent, "充电起始SOC"));
			hisChargeProcess.setCurrent_soc(TestMgmt.getValue(decodedContent, "当前SOC"));
			hisChargeProcess.setInsert_state(TestMgmt.getValue(decodedContent, "充电枪插入状态"));
			hisChargeProcess.setVoltage(TestMgmt.getValue(decodedContent, "充电电压"));
			hisChargeProcess.setCurrent(TestMgmt.getValue(decodedContent, "充电电流"));
			hisChargeProcess.setPower(TestMgmt.getValue(decodedContent, "输出功率"));
			hisChargeProcess.setProtocal(TestMgmt.getValue(decodedContent, "通信协议版本号"));
			hisChargeProcess.setPotevio_protocal(TestMgmt.getValue(decodedContent, "普天通讯协议版本"));
			hisChargeProcess.setBattery_capacity(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池额定容量"));
			hisChargeProcess.setBattery_voltage(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池额定总电压"));
			hisChargeProcess.setBattery_num(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池箱数 n"));
			hisChargeProcess.setBattery_series_num(TestMgmt.getValue(decodedContent, "整车/分箱电池串联数"));
			hisChargeProcess.setBattery_parallel_num(TestMgmt.getValue(decodedContent, "整车/分箱电池并联数"));
			hisChargeProcess.setBattery_sample_num(TestMgmt.getValue(decodedContent, "电池温度采集点数"));
			hisChargeProcess.setBattery_charge_voltage(TestMgmt.getValue(decodedContent, "蓄电池模块最高允许充电电压"));
			hisChargeProcess.setBattery_charge_current(TestMgmt.getValue(decodedContent, "最高允许充电电流"));
			hisChargeProcess.setCapacity(TestMgmt.getValue(decodedContent, "标称总容量"));
			hisChargeProcess.setCharge_voltage(TestMgmt.getValue(decodedContent, "最高允许充电总电压"));
			hisChargeProcess.setHigh_temperature(TestMgmt.getValue(decodedContent, "最高允许蓄电池温度"));
			hisChargeProcess.setSoh(TestMgmt.getValue(decodedContent, "SOH"));
			hisChargeProcess.setSoc(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池荷电状态"));
			hisChargeProcess.setTotal_voltage(TestMgmt.getValue(decodedContent, "整车/分箱蓄电池总电压"));
			hisChargeProcess.setRequire_voltage(TestMgmt.getValue(decodedContent, "电池组需求总电压"));
			hisChargeProcess.setRequire_current(TestMgmt.getValue(decodedContent, "电池组需求总电流"));
			hisChargeProcess.setVoltage_measure(TestMgmt.getValue(decodedContent, "充电电压测量值"));
			hisChargeProcess.setCurrent_measure(TestMgmt.getValue(decodedContent, "充电电流测量值"));
			hisChargeProcess.setTemperature_low(TestMgmt.getValue(decodedContent, "电池组最低温度"));
			hisChargeProcess.setTemperature_high(TestMgmt.getValue(decodedContent, "电池组最高温度"));
			hisChargeProcess.setFull_time(TestMgmt.getValue(decodedContent, "估算充满时间/min"));
			hisChargeProcess.setBms_suspend_reason(TestMgmt.getValue(decodedContent, "BMS 中止充电原因"));
			hisChargeProcess.setSingle_voltage_min(TestMgmt.getValue(decodedContent, "电池单体最低电压"));
			hisChargeProcess.setSingle_voltage_max(TestMgmt.getValue(decodedContent, "电池单体最高电压"));
			hisChargeProcess.setBms_id(TestMgmt.getValue(decodedContent, "BMS ID"));
			hisChargeProcess.setError(TestMgmt.getValue(decodedContent, "错误数据"));
			hisChargeProcess.setCar_vin(TestMgmt.getValue(decodedContent, "车辆VIN"));
			hisChargeProcess.setNum(TestMgmt.getValue(decodedContent, "电池箱数"));
			int dianchixiangCount = Integer.parseInt(TestMgmt.getValue(decodedContent, "电池箱数"));
			for(int j=0; j<dianchixiangCount&& j<8;j++){
				setHistoryProcessBattery(hisChargeProcess,j,TestMgmt.getValue(decodedContent, "电池箱 ID "+j));				
			}
			testResultManaService.InsertHistoryProcessRecord(hisChargeProcess);
		}
	}
	*/
	/*zqq cancel 180731
	private void setHistoryProcessBattery(HistroyChargeProcess hisChargeProcess, int index, String batteryVal){
		switch (index) {
		case 0:
			hisChargeProcess.setBattery1(batteryVal);
			break;
		case 1:
			hisChargeProcess.setBattery2(batteryVal);
			break;
		case 2:
			hisChargeProcess.setBattery3(batteryVal);
			break;
		case 3:
			hisChargeProcess.setBattery4(batteryVal);
			break;
		case 4:
			hisChargeProcess.setBattery5(batteryVal);
			break;
		case 5:
			hisChargeProcess.setBattery6(batteryVal);
			break;
		case 6:
			hisChargeProcess.setBattery7(batteryVal);
			break;
		case 7:
			hisChargeProcess.setBattery8(batteryVal);
			break;
		default:
			break;
		}	
	}
	
	*/
	
	/**
	 * @param parse		测试阶段  1：T1; 2:T2; 3：T3;
	 * @param isRight   报文格式是否正确
	 * @param errorMsg  错误信息
	 * @param equipName 终端名称
	 * @param timeinterval  响应时间
	 * @param ReceiveBuffer 接受的报文
	 * @param msgCommand	报文协议名称    不要含0x
	 * @param content		报文解析内容
	 */
	private void storageMessage(int parse,boolean isRight, String errorMsg, 
			String equipName, int timeinterval,
			byte[] ReceiveBuffer, String msgCommand,String content) {
		CheckResultDto ResultDto = new CheckResultDto();
		ResultDto.setCheckTime(new Date());
		ResultDto.setEquipName(equipName);
		ResultDto.setMesCode("0x"+msgCommand);//
		ResultDto.setAnswerTime(timeinterval);
		ResultDto.setSrcMessage(CommFunction.LogByteArray(ReceiveBuffer).toString());
		
		switch (parse) {
		case 1:
			if(isRight==false){
				ResultDto.setCheckContent(MessageInfo.errorMsg);
				ResultDto.setT1CheckResult("T1不正确");
			}else{
				ResultDto.setCheckContent(MessageInfo.successMsg);
				ResultDto.setT1CheckResult("T1正确");
			}
			ResultDto.setT2CheckResult("T2未测试");
			ResultDto.setT3CheckResult("T3未测试");
			break;
		case 2:
			if(isRight==false){
				ResultDto.setCheckContent(MessageInfo.errorMsg);
				ResultDto.setT1CheckResult("T1不正确");
				ResultDto.setT2CheckResult("T2未测试");
				ResultDto.setT3CheckResult("T3未测试");
			}else{
				ResultDto.setCheckContent(content);
				ResultDto.setT1CheckResult("T1正确");
				if(errorMsg==null||errorMsg.length()==0){
					ResultDto.setT2CheckResult("T2正确");
					ResultDto.setT3CheckResult("T3未测试");
				}else{
					ResultDto.setT2CheckResult("T2不正确");
					ResultDto.setT3CheckResult("T3未测试");
				}
			}
			break;
		case 3:
			if(isRight==false){
				ResultDto.setCheckContent(MessageInfo.errorMsg);
				ResultDto.setT1CheckResult("T1不正确");
				ResultDto.setT2CheckResult("T2未测试");
				ResultDto.setT3CheckResult("T3未测试");
			}else{
				ResultDto.setCheckContent(content);
				ResultDto.setT1CheckResult("T1正确");
				if(errorMsg==null||errorMsg.length()==0){
					ResultDto.setT2CheckResult("T2正确");
					ResultDto.setT3CheckResult("T3正确");
				}else{
					ResultDto.setT2CheckResult("T2不正确");
					ResultDto.setT3CheckResult("T3不正确");
				}
			}
			break;
		}	
		ResultDto.setErrorInfo(errorMsg);
	//	systemService.InsertResult(ResultDto);
//	monDBService.storeDB();
	}
	
	public String[] Decode10Package(byte[] Receive,String equipName,int parse){
		String msgCommand = "00";
		deviceInfo deviceInfo = Decode10MsgStyle(Receive);
		
		if(deviceInfo.getErrorMsg()==null||deviceInfo.getErrorMsg().length()==0){
			
			//解析报文(区分版本)
			Byte version = Receive[3];
			String[] strcontent = new String[2];
			StringBuilder sfcontent = new StringBuilder();
			int index = 6;//数据域起始
			if((version.equals((byte) 0x80))){
				msgCommand = CommFunction.byteToHexStr2(Receive[5]);
			}
			if((version.equals((byte) 0x35))){
				msgCommand = CommFunction.byteToHexStr2(Receive[22]);
				index = 23;
			}
			sfcontent.append("充电桩编码:");
			byte[] des1 = new byte[8];
			ArraysN.copy(des1, 0, Receive, index, 8);
			sfcontent.append(CommFunction.LogByteArray(des1));
			sfcontent.append(":");
			sfcontent.append(CommFunction.byteArrayToHexStr(des1));
			sfcontent.append(";");
			index +=8;
			sfcontent.append("系统设备资产编码:");
			byte[] des2 = new byte[20];
			ArraysN.copy(des2, 0, Receive, index, 20);
			sfcontent.append(CommFunction.LogByteArray(des2));
			sfcontent.append(":");
			sfcontent.append(CommFunction.bytesToAscii(des2));
			sfcontent.append(";");
			index +=20;
			sfcontent.append("系统软件版本:");
			byte[] des3 = new byte[4];
			ArraysN.copy(des3, 0, Receive, index, 4);
			sfcontent.append(CommFunction.LogByteArray(des3));
			sfcontent.append(":");
			sfcontent.append(MessgeTransfer.VersionbyteTostr(des3));
			sfcontent.append(";");
			index +=4;
			sfcontent.append("启动次数:");
			byte[] des4 = new byte[4];
			ArraysN.copy(des4, 0, Receive, index, 4);
			sfcontent.append(CommFunction.LogByteArray(des4));
			sfcontent.append(":");
			sfcontent.append(MessgeTransfer.bytesToIntstr(des4));
			sfcontent.append(";");
			index +=4;
			sfcontent.append("存储空间容量:");
			byte[] des5 = new byte[4];
			ArraysN.copy(des5, 0, Receive, index, 4);
			sfcontent.append(CommFunction.LogByteArray(des5));
			sfcontent.append(":");
			sfcontent.append(MessgeTransfer.bytesToIntstr(des5));
			sfcontent.append("M");
			sfcontent.append(";");
			index +=4;
			sfcontent.append("充电桩智能终端软件已经持续运行时间:");
			byte[] des6 = new byte[4];
			ArraysN.copy(des6, 0, Receive, index, 4);
			sfcontent.append(CommFunction.LogByteArray(des6));
			sfcontent.append(":");
			sfcontent.append(MessgeTransfer.bytesToIntstr(des6));
			sfcontent.append("分钟");
			sfcontent.append(";");
			index +=4;
			sfcontent.append("最近一次启动时间:");
			byte[] des7 = new byte[8];
			ArraysN.copy(des7, 0, Receive, index, 8);
			sfcontent.append(CommFunction.LogByteArray(des7));
			sfcontent.append(":");
			sfcontent.append(MessgeTransfer.TimebyteTostr(des7));
			sfcontent.append(";");
			index +=8;
			sfcontent.append("最近一次签到时间:");
			byte[] des8 = new byte[8];
			ArraysN.copy(des8, 0, Receive, index, 8);
			sfcontent.append(CommFunction.LogByteArray(des8));
			sfcontent.append(":");
			sfcontent.append(MessgeTransfer.TimebyteTostr(des8));
			sfcontent.append(";");
			index +=8;
			if((version.equals((byte) 0x35))){
				sfcontent.append("设备软件授信配置MD5码:");
				byte[] des9 = new byte[32];
				ArraysN.copy(des9, 0, Receive, index, 32);
				sfcontent.append(CommFunction.LogByteArray(des9));
				sfcontent.append(":");
				sfcontent.append(CommFunction.bytesToAscii(des9));
				sfcontent.append(";");
				index +=32;
				sfcontent.append("设备软件授信软件长度:");
				byte[] des10 = new byte[4];
				ArraysN.copy(des10, 0, Receive, index, 4);
				sfcontent.append(CommFunction.LogByteArray(des10));
				sfcontent.append(":");
				sfcontent.append(MessgeTransfer.bytesToIntstr(des10));
				sfcontent.append(";");
				index +=4;
				sfcontent.append("智能终端中文编码方式:");
				byte[] des11 = new byte[1];
				ArraysN.copy(des11, 0, Receive, index, 1);
				sfcontent.append(CommFunction.LogByteArray(des11));
				sfcontent.append(":");
				
				int codeMode = CommFunction.unsignByteToInt(des11[0]);
				
				sfcontent.append(codeMode==1?"UTF-8":"GB2312");
				sfcontent.append(";");
			}
			strcontent[0]="";
			strcontent[1]=sfcontent.toString();
			return strcontent;
			//入库
			//storageMessage(parse,true,strcontent[0],equipName, 0, Receive, msgCommand,strcontent[1]);
			// 入table10表
			//zqq cancel 180731
			/*
			JavaBean10 table10 = new JavaBean10();
			table10.setName(equipName);
			table10.setMessageTime(new Date());
			table10.setCommand("0x"+msgCommand);
			table10.setCode(CommFunction.byteArrayToHexStr(des1));
			StringBuilder sdd = new StringBuilder();
			table10.setSysCode(sdd.append(CommFunction.bytesToAscii(des2)).toString());
			table10.setSysVersion(MessgeTransfer.VersionbyteTostr(des3));
			table10.setStartNum(MessgeTransfer.bytesToIntstr(des4) + "次");
			table10.setStorageNum(MessgeTransfer.bytesToIntstr(des5) + "M");
			table10.setRunTime(MessgeTransfer.bytesToIntstr(des6) + "分钟");
			String s = MessgeTransfer.Time97byteTostr(des7);
			try {
				startDate = format.parse(s);
				table10.setCurrStartTime(startDate);
			} catch (ParseException e1) {
				LoggerOperator.err(LOG, e1.toString());
			}
			String s1 = MessgeTransfer.Time97byteTostr(des8);
			try {
				startDate = format.parse(s1);
				table10.setCurrSignTime(startDate);
			} catch (ParseException e1) {
				LoggerOperator.err(LOG, e1.toString());
			}
			JavaBean10 temptable10 = terminaldto.queryByMessage10(equipName);
			if (temptable10 != null) {
				terminaldto.updatetable10(table10);
			} else {
				terminaldto.inserttable10(table10);
			}
			*/
		}else{
			return null;
			//storageMessage(parse,false,deviceInfo.getErrorMsg(),equipName, 0, Receive, msgCommand,"");
		}
	}
	
	// 校验10/1B报文
	public deviceInfo Decode10MsgStyle(byte[] Receive) {
		//记日志
		logger.info(" in Decode10PackageNoStorage start: " + CommFunction.byteArrayToHexStr2(Receive));
		deviceInfo deviceinfo = new deviceInfo();
		//区分版本
		Byte version = Receive[3];
		Byte command ;
		int templen ;
		
		if((version.equals((byte) 0x80))){
			//判断是否是0x10报文
			command = Receive[5];
			if(!(command.equals((byte) 0x10)))deviceinfo.setErrorMsg(MessageInfo.validate_id);
			//判断报文长度是否正确
			if(Receive.length != 67)deviceinfo.setErrorMsg(MessageInfo.validate_lenght);
			//判断长度域是否正确
			templen = CommFunction.unsignByteToInt(Receive[2]);
			if(templen != 63)deviceinfo.setErrorMsg(MessageInfo.validate_lenghtDomain);
			//判断检验和是否正确
			byte[] tempChecksum = new byte[61];
			ArraysN.copy(tempChecksum, 0, Receive, 5, 61);
			if(!(CommFunction.byteToHexStr2(Receive[Receive.length - 1])
				.equalsIgnoreCase(CommFunction.SumCheck2(tempChecksum))))deviceinfo.setErrorMsg(MessageInfo.validate_checkSum);
			deviceinfo.setVersion("3.4");
			deviceinfo.setDeviceCode(CommFunction.byteArrayToHexStr(ConfigTool.subBytes(Receive, 6, 8)));
			deviceinfo.setChineseEncoding("GB2312");
		}
		if((version.equals((byte) 0x35))){
			//判断是否是0x10报文
			command = Receive[22];
			if(!(command.equals((byte) 0x1B)))deviceinfo.setErrorMsg(MessageInfo.validate_id);
			//判断报文长度是否正确
			if(Receive.length != 121)deviceinfo.setErrorMsg(MessageInfo.validate_lenght);
			//判断长度域是否正确
			templen = CommFunction.unsignByteToInt(Receive[2]);
			if(templen != 117)deviceinfo.setErrorMsg(MessageInfo.validate_lenghtDomain);
			//判断检验和是否正确
			byte[] tempChecksum = new byte[98];
			ArraysN.copy(tempChecksum, 0, Receive, 22, 98);
			if(!(CommFunction.byteToHexStr2(Receive[Receive.length - 1])
				.equalsIgnoreCase(CommFunction.SumCheck2(tempChecksum))))deviceinfo.setErrorMsg(MessageInfo.validate_checkSum);
			deviceinfo.setVersion("3.5");
			deviceinfo.setDeviceCode(CommFunction.byteArrayToHexStr(ConfigTool.subBytes(Receive, 5, 8)));
			int chineseEncoding = Receive[Receive.length-2];
			if(chineseEncoding ==1){
				deviceinfo.setChineseEncoding("UTF-8");
			}else{
				deviceinfo.setChineseEncoding("GB2312");
			}
		}
		logger.info( " in Decode10PackageNoStorage end: " + deviceinfo.toString());
		return deviceinfo;
	}
	
	
	// 解析58报文；
	public byte[] Decode58Package(byte[] Receive, String equipName) {
		
		logger.info(" in Decode58Package start: " + CommFunction.byteArrayToHexStr2(Receive));
		//区分版本
		Byte version = Receive[3];
		Byte command ;
		byte[] des58 = new byte[6];
		int templen ;
		String info = "校验通过";
		if((version.equals((byte) 0x80))){
			//判断是否是0x10报文
			command = Receive[5];
			if(!(command.equals((byte) 0x58)))info=MessageInfo.validate_id;
			//判断报文长度是否正确
			if(Receive.length != 13)info=MessageInfo.validate_lenght;
			//判断长度域是否正确
			templen = CommFunction.unsignByteToInt(Receive[2]);
			if(templen != 9)info=MessageInfo.validate_lenghtDomain;
			//判断检验和是否正确
			byte[] tempChecksum = new byte[7];
			ArraysN.copy(tempChecksum, 0, Receive, 5, 7);
			if(!(CommFunction.byteToHexStr2(Receive[Receive.length - 1])
				.equalsIgnoreCase(CommFunction.SumCheck2(tempChecksum))))info=MessageInfo.validate_checkSum;
			ArraysN.copy(des58, 0, Receive, 6, 6);
		}
		if((version.equals((byte) 0x35))){
			//判断是否是0x10报文
			command = Receive[22];
			if(!(command.equals((byte) 0x58)))info=MessageInfo.validate_id;
			//判断报文长度是否正确
			if(Receive.length != 30)info=MessageInfo.validate_lenght;
			//判断长度域是否正确
			templen = CommFunction.unsignByteToInt(Receive[2]);
			if(templen != 26)info=MessageInfo.validate_lenghtDomain;
			//判断检验和是否正确
			byte[] tempChecksum = new byte[7];
			ArraysN.copy(tempChecksum, 0, Receive, 22, 7);
			if(!(CommFunction.byteToHexStr2(Receive[Receive.length - 1])
				.equalsIgnoreCase(CommFunction.SumCheck2(tempChecksum))))info=MessageInfo.validate_checkSum;
			ArraysN.copy(des58, 0, Receive, 23, 6);
		}
		logger.info( " in Decode58Package end: " + info);
		return des58;
	}
}
