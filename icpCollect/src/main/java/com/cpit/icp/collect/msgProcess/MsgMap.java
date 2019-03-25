package com.cpit.icp.collect.msgProcess;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
//import org.apache.mina.core.session.IoSession;

import com.cpit.icp.dto.collect.MonSequenceDto;

//import com.cpit.testplatform.modules.testsuite.comm.SockMess;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;

public class MsgMap {
	
	private static MsgMap instance = null;
	
	//private static Map<IoSession, Queue> msgRecievedMap = new ConcurrentHashMap<IoSession, Queue>();//定义线程安全的队�?

	//private static Map<String, Queue<SockMess>> sendDMap = new ConcurrentHashMap<String,Queue<SockMess>>();
	//private static Map<Byte,Byte> responseMap = new ConcurrentHashMap<Byte,Byte>();;
	private static Map<String,Map<Byte,Byte>> responseMap = new ConcurrentHashMap<String,Map<Byte,Byte>>();;
	//private static Map<Byte,Byte> reportMap = new ConcurrentHashMap<Byte,Byte>();
	private static Map<String,Map<Byte,Byte>> reportMap = new ConcurrentHashMap<String,Map<Byte,Byte>>();;
	private static Map<String,Map<Byte,Byte>> reportMapReverse = new ConcurrentHashMap<String,Map<Byte,Byte>>();;
//	private static Map<Byte,Byte> reportMapReverse = new ConcurrentHashMap<Byte,Byte>();//下发错误报文的时候查
	
	//下发报文是否收到 key为ip+编码
	private static Map<String,Boolean> replyMap = new ConcurrentHashMap<String,Boolean>();

	private  static List<String> commTestIp = Collections.synchronizedList(new ArrayList<String>());
//----2018 new add by zhangqq
	private static Map<String,List<String>> stNeedReply = new ConcurrentHashMap<String,List<String>>();
private static List<String> tsNeedStore =   Collections.synchronizedList(new ArrayList<String>());
/**
 * cardId,deviceNo
 * 开始充电set，结束和进度查询的时候，根据cardId getdeviceNo，再做操作。
 */

/**
 * chargeOrderNo,device.开始充电时set
 */
private static Map<String,String> chargePoleInHLHTUse = new ConcurrentHashMap<String,String>();//<cardId,deviceNo>


//private static Map<String,String> codePairTS = new ConcurrentHashMap<String,String>();//(70,69)(15,05)
//private static Map<String ,Integer> codeType = new ConcurrentHashMap<String,Integer>();//1 st;2 ts
private static Map<String,String> chargingSource = new ConcurrentHashMap<String,String>();//(device,1)

private static Map<String,Map<String,String>> codePairTSVer= new ConcurrentHashMap<String,Map<String,String>>();//(3.4,(0x70=0x69)
private static Map<String,Map<String,Integer>> codeTypeVer= new ConcurrentHashMap<String,Map<String,Integer>>();

private static Map<String,MonSequenceDto> cpFlowNum =  new ConcurrentHashMap<String,MonSequenceDto>();//(01020304,(1,2))deviceNo,msgNum,sequenceNUm



public static void setCpFlowNum(Map<String, MonSequenceDto> cpFlowNum) {
	MsgMap.cpFlowNum = cpFlowNum;
}

public static Map<String, Map<String, Integer>> getCodeTypeVer() {
	return codeTypeVer;
}

public static void setCodeTypeVer(Map<String, Map<String, Integer>> codeTypeVer) {
	MsgMap.codeTypeVer = codeTypeVer;
}

public static Map<String, Map<String, String>> getCodePairTSVer() {
	return codePairTSVer;
}

public static void setCodePairTSVer(Map<String, Map<String, String>> codePairTSVer) {
	MsgMap.codePairTSVer = codePairTSVer;
}



public static Map<String, String> getChargingSource() {
	return chargingSource;
}

public static void setChargingSource(Map<String, String> chargingSource) {
	MsgMap.chargingSource = chargingSource;
}
	public static List<String> getTsNeedStore() {
	return tsNeedStore;
}



	public static void setTsNeedStore(List<String> tsNeedStore) {
		MsgMap.tsNeedStore = tsNeedStore;
	}

	public static Map<String, List<String>> getStNeedReply() {
	return stNeedReply;
}

public static void setStNeedReply(Map<String, List<String>> stNeedReply) {
	MsgMap.stNeedReply = stNeedReply;
}

	private MsgMap(){
		//Map<Byte,Byte> m = ;
		responseMap.put(VERSION_3_4, new ConcurrentHashMap<Byte,Byte>()) ;
		responseMap.put(VERSION_3_5, new ConcurrentHashMap<Byte,Byte>()) ;
		
		reportMap.put(VERSION_3_4, new ConcurrentHashMap<Byte,Byte>()) ;
		reportMap.put(VERSION_3_5, new ConcurrentHashMap<Byte,Byte>()) ;
		
		reportMapReverse.put(VERSION_3_4, new ConcurrentHashMap<Byte,Byte>()) ;
		reportMapReverse.put(VERSION_3_5, new ConcurrentHashMap<Byte,Byte>()) ;
		
	}
	
	public synchronized static MsgMap getInstance(){
		if(instance == null){
			instance = new MsgMap();
		}
		return instance;
	}

	public  List<String> getCommTestIp() {
		return commTestIp;
	}

	public static void setCommTestIp(List<String> commTestIp) {
		MsgMap.commTestIp = commTestIp;
	}
	public Map<Byte, Byte> getResponseMap(String version) {
		return responseMap.get(version);
	}
	public void setResponseMap(Map<Byte, Byte> resMap) {
		
	}
	public Map<Byte, Byte> getReportMap(String version) {
		return reportMap.get(version);
	}
	public void setReportMap(Map<Byte, Byte> reportMap) {
	//	this.reportMap = reportMap;
	}
	
//	public Map<IoSession,Queue> getMsgRECMap(){
//		return msgRecievedMap;
//	}
//	public Map<String,Queue<SockMess>> getSendDMap(){
//		return sendDMap;
//	}
//	public void setSendDMap(Map<String, Queue<SockMess>> sendDMap) {
//		this.sendDMap = sendDMap;
//	}

	public Map<String, Boolean> getReplyMap() {
		return replyMap;
	}

	public void setReplyMap(Map<String, Boolean> replyMap) {
		this.replyMap = replyMap;
	}
	public static Map<Byte, Byte> getReportMapReverse(String version) {
		
		return reportMapReverse.get(version);
	}

	public static void setReportMapReverse(Map<Byte, Byte> reportMapReverse) {
		//MsgMap.reportMapReverse = reportMapReverse;
	}

//	public static Map<String, String> getCodePairTS(String version) {
//		return codePairTSVer.get(version);
//	}

	

	public static Map<String, Integer> getCodeType(String version) {
		return codeTypeVer.get(version);
	}

	public static Map<String, String> getChargePoleInHLHTUse() {
		return chargePoleInHLHTUse;
	}

	public static void setChargePoleInHLHTUse(Map<String, String> chargePoleInHLHTUse) {
		MsgMap.chargePoleInHLHTUse = chargePoleInHLHTUse;
	}

	

}
