package com.cpit.icp.pregateway.message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MsgThreadPool {
	
	
private final static Logger logger = LoggerFactory.getLogger(MsgThreadPool.class);
	
	private static ExecutorService msgThreadPool = null;
	public static ExecutorService getThreadPool(){
		if(msgThreadPool==null){
			msgThreadPool = Executors.newFixedThreadPool(20);
		}
		return 	msgThreadPool;
	}

}
