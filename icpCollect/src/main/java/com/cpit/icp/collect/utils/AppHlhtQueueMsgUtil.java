package com.cpit.icp.collect.utils;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;

import com.cpit.common.SMSUtil;
import com.cpit.common.SpringContextHolder;
import com.cpit.icp.dto.collect.hlht.HlhtChargingProcDto;

/**
 * app hlht msg Util send into Queue
 * @author zhangqianqian
 *
 */
public class AppHlhtQueueMsgUtil {
private final static Logger logger = LoggerFactory.getLogger(AppHlhtQueueMsgUtil.class);
	
	private static AppHlhtQueueMsgUtil instance = null;
	
	private static final String QUEUE_NAME = "icp-col-hlhtMsg";
	
	private static AmqpTemplate amqpTemplate = null;
	
	private AppHlhtQueueMsgUtil(){
		amqpTemplate = SpringContextHolder.getBean("rabbitTemplate");
	}
	
	public static synchronized AppHlhtQueueMsgUtil getInstance(){
		if(instance == null){
			instance = new AppHlhtQueueMsgUtil();
		}
		return instance;
	}
	
	public void send(HlhtChargingProcDto obj) {
		logger.info("send begin " );
        try {
        	
            amqpTemplate.convertAndSend(QUEUE_NAME, obj);
        } catch (AmqpException e) {
            logger.error("send RabbitMQ error", e);
        }
    }
}
