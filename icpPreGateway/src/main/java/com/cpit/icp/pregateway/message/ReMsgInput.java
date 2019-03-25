package com.cpit.icp.pregateway.message;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * 重发消息接收处理
 * @author jinzhiwei
 *
 */
@Component
public interface ReMsgInput {
	
   String RE_INPUT = "reInput";
	
	@Input(RE_INPUT)
	MessageChannel msgInput();

}
