package com.cpit.icp.collect.msgProcess;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgThreadPool {
	
	private final static Logger logger = LoggerFactory.getLogger(MsgThreadPool.class);
	
	private static ExecutorService msgProThreadPool = null;
	public static ExecutorService getThreadPool(){
		if(msgProThreadPool==null){
			msgProThreadPool = Executors.newFixedThreadPool(50);
		}
		return 	msgProThreadPool;
	}

}
