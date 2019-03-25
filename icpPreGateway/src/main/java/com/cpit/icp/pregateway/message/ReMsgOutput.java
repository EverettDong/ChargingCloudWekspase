package com.cpit.icp.pregateway.message;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;


/**
 * 重发消息发送处理
 * @author jinzhiwei
 *
 */
@Component
public interface ReMsgOutput {
	
	String RE_OUTPUT = "reOutput";

	@Output(RE_OUTPUT)
	MessageChannel myOutput();

}
