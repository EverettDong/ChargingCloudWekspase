package com.cpit.icp.pregateway.message;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.pregateway.message.dto.MsgOfPregateDto;

public class MsgQueue {
	private final static Logger logger = LoggerFactory.getLogger(MsgQueue.class);
	private static LinkedBlockingQueue<MsgOfPregateDto> queues = null;

	public static LinkedBlockingQueue<MsgOfPregateDto> getTaskQueue() {
		if (queues == null) {
			queues = new LinkedBlockingQueue<MsgOfPregateDto>();
			logger.info("create New MsgQueue");
		}
		return queues;
	}

	public static void add(MsgOfPregateDto obj) {
		if (queues == null)
			queues = getTaskQueue();
		queues.offer(obj);
		logger.info("add to Queue " + obj);

	}
	
//	public static MsgOfPregateDto get() {
//		MsgOfPregateDto mpd;
//		if (!queues.isEmpty()) {
//			mpd=queues.poll();
//			logger.info("get Queue " + mpd);
//			return mpd;
//		}
//		
//		return null;
//	}

}
