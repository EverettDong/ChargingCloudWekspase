package com.cpit.icp.pregateway.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.support.MessageBuilder;

import com.cpit.icp.pregateway.message.dto.MsgOfPregateDto;

@EnableBinding(ReMsgOutput.class)
public class ReMsgSender {
	
	
	private final static Logger logger = LoggerFactory.getLogger(ReMsgSender.class);

    @Autowired
    private ReMsgOutput mySend;

    /**
     * 重新放入消息队列
     * @param reMsg
     */
    public void send(MsgOfPregateDto reMsg) {
    	mySend.myOutput().send(MessageBuilder.withPayload(reMsg).build());
    	logger.info("msg put in mq again." + reMsg);
    }

}
