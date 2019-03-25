package com.cpit.icp.collect.msgProcess;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cpit.common.SpringContextHolder;
import com.cpit.icp.collect.rabbitConfig.RabbitMsgSender;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.uiSend.UISendMsgResDto;


public class UIMsgResMonitorThread implements Runnable{
	private final static Logger logger = LoggerFactory.getLogger(MsgSendThread.class);
	private String cacheKey;
	
	
	private CacheUtil cacheUtil;
	private RabbitMsgSender rabbitMsgSender;
	public UIMsgResMonitorThread(String key) {
		cacheUtil = SpringContextHolder.getBean(CacheUtil.class);
		rabbitMsgSender = SpringContextHolder.getBean(RabbitMsgSender.class);
		this.cacheKey=key;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			Thread.currentThread().sleep(15000);
			logger.info("UIMsgResMonitorThread run"+cacheKey);
			String inTime=cacheUtil.getMatchCode(cacheKey);
			if(inTime!=null) {
				logger.info("UIMsgResMonitorThread "+cacheKey+" intime "+inTime);
				cacheUtil.delMatchCode(cacheKey);
				// notify ui.
				UISendMsgResDto dto = new UISendMsgResDto();
				String[]  ss= cacheKey.split("_");
			dto.setDeviceNo(ss[0]);
			dto.setMsgCodeSt(ss[1]);
			dto.setResult("0");
			dto.setUiSendTime(inTime);
			
			rabbitMsgSender.sendUIMsgRes(dto);
			logger.info("senUiMsgRes success.");
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error("UIMsgResMonitorThread run error",e);
		}
		
	}

}
