package com.cpit.icp.collect.msgProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.web.client.TestRestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.cpit.common.SpringContextHolder;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.common.ResultInfo;

import static com.cpit.icp.collect.utils.Consts.ST_NEEDREPLY34;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import java.util.List;;

public class MsgSendThread implements Runnable {
	private final static Logger logger = LoggerFactory.getLogger(MsgSendThread.class);
	private MsgOfPregateDto msgOfPregateDto = null;
	private CacheUtil cacheUtil = null;
	private boolean notifyWrongDev;
	
	//private TestRestTemplate restTemplate;

	public MsgSendThread(MsgOfPregateDto dto,boolean wrong) {
		msgOfPregateDto = dto;
		notifyWrongDev = wrong;
		cacheUtil = SpringContextHolder.getBean(CacheUtil.class);
	//	restTemplate =  SpringContextHolder.getBean(TestRestTemplate.class);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		sendMsg(msgOfPregateDto,notifyWrongDev);

	}

	public boolean sendMsg(MsgOfPregateDto dto,boolean notify) {
		logger.debug("sendMsg begins.");
		String preGateIp = dto.getPreGateIp();
		String preGatePort = dto.getPreGatePort();

		
		String jsonStr =JSONObject.toJSONString(dto);
		
if(notify) {
			logger.debug("error device,notify");
			rejectDeviceToPreGate(preGateIp+":"+preGatePort,dto.getDeviceNo());	
		}else {
			sendMsgToPreGate(preGateIp+":"+preGatePort,jsonStr);
		}

		return true;
	}

	
	public boolean rejectDeviceToPreGate(String url,String jsonStr) {
		logger.info("rejectDeviceToPreGate" + jsonStr);
		try {
			String surl = "http://"+url + "/rejectErrorDevice";
			

			ResfulUtil.doPost(surl, String.class, jsonStr);
			return true;
		} catch (Exception ex) {
			
			logger.error("error in sendErrorDevice", ex);
			
		}
		return false;
	}
	public boolean sendMsgToPreGate(String url,String jsonStr) {
		logger.info("sendMsgToPreGate " + jsonStr);
		try {
			String surl = "http://"+url + "/receiveDownMessage";
			ResfulUtil.doPost(surl, String.class, jsonStr);
			return true;
		} catch (Exception ex) {
			
			logger.error("error in sendUpMessage", ex);
			
		}
		return false;
	}

}
