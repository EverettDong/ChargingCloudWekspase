package com.cpit.icp.collect.msgProcess;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * queue 存储前端收到的报文
 * 类名称：MsgQueue
 * 类描述：
 * 创建人：zhangqianqian
 * 创建时间：2018年8月2日 下午2:08:18
 * 修改人：
 * 修改时间：
 * 修改备注：
 * @version 1.0.0
 */
public class MsgQueue {
	private final static Logger logger = LoggerFactory.getLogger(MsgQueue.class);

	private static  LinkedBlockingQueue queues = null;
	
	public static LinkedBlockingQueue getTaskQueue(){
		if(queues==null){
			queues =  new LinkedBlockingQueue();
			logger.info("create New Queue");
		}
		return queues;
	}
	
	public static void add(Object obj){
		if(queues==null)
			queues =  getTaskQueue();
		queues.offer(obj);
		logger.info("add to Queue");

}
	
	
}
