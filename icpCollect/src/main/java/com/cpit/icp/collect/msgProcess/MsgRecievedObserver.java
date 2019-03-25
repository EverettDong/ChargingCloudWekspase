package com.cpit.icp.collect.msgProcess;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

//import org.apache.mina.core.session.IoSession;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cpit.common.SpringContextHolder;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
/*
import com.cpit.testplatform.modules.encode.common.DataContentDto;
import com.cpit.testplatform.modules.encode.util.LoggerOperator;
import com.cpit.testplatform.modules.encode.util.ParseDecodeXml;
import com.cpit.testplatform.modules.encode.util.ParseEncodeXml;
import com.cpit.testplatform.modules.sockComm.msgProcess.*;
import com.cpit.testplatform.modules.sockComm.sockMsgDto.*;
import com.cpit.testplatform.modules.sockComm.util.CodeTransfer;
import com.cpit.testplatform.modules.testsuite.comm.SockMess;
import com.thinkgem.jeesite.common.utils.SpringContextHolder;
*/
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import static com.cpit.icp.collect.utils.Consts.FILENAME_V_34;
import static com.cpit.icp.collect.utils.Consts.FILENAME_V_35;

/**
 * observer data receiver，gen different imps 收到数据，根据不同类别，生成不同的处理类
 * 
 * @author zhangqianqian
 *
 */
public class MsgRecievedObserver implements Observer {
	private final static Logger logger = LoggerFactory.getLogger(MsgRecievedObserver.class);
	ParseMsgProcessorXml parseMsgProcessorXml = null;

	private static final String xmlPath = ".\\conf\\msgProcessor.xml";


	public MsgRecievedObserver() {
	
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// processReceivedMap(msgMap.getMsgRECMap());
		logger.debug("update..");
		processMsgQueue(MsgQueue.getTaskQueue());
	}

	public void msgReceivedProcess(MsgOfPregateDto obj) {
		logger.debug("msgReceivedProcess new thread");
		MsgThreadPool.getThreadPool().execute(new MsgProcessThread(obj));

	}


	private void processMsgQueue(LinkedBlockingQueue dataQueue) {
		// private void processReceivedMap(LinkedBlockingQueue dataQueue){
		logger.debug("processMsgQueue,get one obj");

		try {
			msgReceivedProcess((MsgOfPregateDto) dataQueue.take());
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			logger.error(e.getMessage());
		}

	}

	
}
