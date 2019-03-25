package com.cpit.icp.pregateway.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import com.cpit.icp.pregateway.message.impl.RetransmissionEngine;
import com.cpit.icp.pregateway.message.dto.MsgOfPregateDto;

/**
 * 接收 0x05,0x6c报文，没收到回复多次重发
 * @author jinzhiwei
 *
 */
//@EnableBinding(value = {ReMsgInput.class})
public class reMsgReceiver {
	
	
	@Autowired
	RetransmissionEngine engine;
	
	private final static Logger logger = LoggerFactory.getLogger(reMsgReceiver.class);

	/**
	 * 创建一个新的实例 BillMsgReceiver.
	 *
	 */
	public reMsgReceiver() {
	}
	

    @StreamListener(ReMsgInput.RE_INPUT)
	public void receive(Object reMsg,com.rabbitmq.client.Channel channel) {
		try {
			engine.launch((MsgOfPregateDto) reMsg);
		} catch (Exception e) {
			logger.info("BillMsgReceiver:" + e);
		}
	}
	
//	@StreamListener(ReMsgInput.RE_INPUT)
//	public void receive(Message reMsg,com.rabbitmq.client.Channel channel) {
//		try {
//			engine.launch((MsgOfPregateDto) reMsg.getMessageProperties());
//		} catch (Exception e) {
//			channel.basicNack(reMsg.getMessageProperties().getDeliveryTag(), false, true);
//			logger.info("BillMsgReceiver:" + e);
//		}
//	}

}
