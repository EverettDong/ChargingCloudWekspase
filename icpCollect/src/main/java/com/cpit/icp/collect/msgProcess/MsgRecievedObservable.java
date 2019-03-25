package com.cpit.icp.collect.msgProcess;

import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;


/**
 * 
 * 类名称：MsgRecievedObservable
 * 类描述：
 * 创建人：zhangqianqian
 * 创建时间：2018年8月2日 下午3:13:29
 * 修改人：
 * 修改时间：
 * 修改备注：
 * @version 1.0.0
 */

public class MsgRecievedObservable extends Observable{
	private final static Logger logger = LoggerFactory.getLogger(MsgRecievedObservable.class);
	
	private static MsgRecievedObservable msgRecievedObservable=null;
	
	private MsgRecievedObservable(){
		this.addObserver(new MsgRecievedObserver());
	}
	
	public static MsgRecievedObservable getInstance() {
		if(msgRecievedObservable == null) {
			msgRecievedObservable = new MsgRecievedObservable();
		}
		return msgRecievedObservable;
	}
	
	public synchronized void addData(MsgOfPregateDto obj){
		MsgQueue.add(obj);
		
		this.setChanged();
		this.notifyObservers();
	}
	
}
