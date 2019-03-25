package com.cpit.icp.collect.msgProcess;

//import org.apache.mina.core.session.IoSession;
import org.springframework.stereotype.Service;

//import com.cpit.testplatform.modules.sockComm.sockMsgDto.SockData;
//import com.cpit.testplatform.modules.testsuite.comm.SockMess;

public interface MsgProcessInterface {
//	public void msgProcess(IoSession session,SockMess sockData);
	public void msgProcess(Object obj);
}
