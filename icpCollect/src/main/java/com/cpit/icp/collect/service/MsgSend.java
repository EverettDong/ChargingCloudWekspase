package com.cpit.icp.collect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.msgProcess.ConstructData;
import com.cpit.icp.collect.msgProcess.MsgSendThread;
import com.cpit.icp.collect.msgProcess.MsgThreadPool;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
@Service
public class MsgSend {
	private final static Logger logger = LoggerFactory.getLogger(MsgSend.class);
	/**
	 * msg send to PreGateWay
	
	 *void
	 * @exception 
	 * @since  1.0.0
	 */
public void msgSendToPreG(MsgOfPregateDto dto,boolean notify){
	
	logger.debug(dto.toString());
	
	
	// 调用preGateWay
	MsgThreadPool.getThreadPool().execute(new MsgSendThread(dto,notify));
}
}
