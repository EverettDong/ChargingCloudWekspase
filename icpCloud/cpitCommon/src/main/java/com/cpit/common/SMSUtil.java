package com.cpit.common;


import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;

public class SMSUtil {

	private final static Logger logger = LoggerFactory.getLogger(SMSUtil.class);
	
	private static SMSUtil instance = null;
	
	private static final String QUEUE_NAME = "sms_queue";
	
	private static AmqpTemplate amqpTemplate = null;
	
	private SMSUtil(){
		amqpTemplate = SpringContextHolder.getBean("rabbitTemplate");
	}
	
	public static synchronized SMSUtil getInstance(){
		if(instance == null){
			instance = new SMSUtil();
		}
		return instance;
	}
	
	public void send(String telphone, String smsText, Integer smsType, Integer acceptWay) {
		logger.info("send begin,telphone:" +telphone+",smsText:"+smsText+",smsType:"+smsType+",acceptWay:"+acceptWay);
        try {
        	Map<String, String> map = new HashMap<String, String>();
        	map.put("telphone", telphone);
        	if(smsText!=null&&smsText!="") {
        		map.put("smsText", smsText);
        	}
        	map.put("smsType", String.valueOf(smsType));
        	map.put("acceptWay", String.valueOf(acceptWay));
            amqpTemplate.convertAndSend(QUEUE_NAME, map);
        } catch (AmqpException e) {
            logger.error("send RabbitMQ error", e);
        }
    }
}
