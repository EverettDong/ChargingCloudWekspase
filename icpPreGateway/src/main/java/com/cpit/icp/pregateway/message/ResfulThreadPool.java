package com.cpit.icp.pregateway.message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResfulThreadPool {
	
	private static ExecutorService resfulThreadPool = null;
	public static ExecutorService getThreadPool(){
		if(resfulThreadPool==null){
			resfulThreadPool = Executors.newFixedThreadPool(30);
		}
		return 	resfulThreadPool;
	}

}
