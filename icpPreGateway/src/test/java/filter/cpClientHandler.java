package filter;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.cpit.icp.pregateway.util.CommFunction;
import com.cpit.icp.pregateway.util.MessageInfo;



public class cpClientHandler implements IoHandler {

	@Override
	public void exceptionCaught(IoSession arg0, Throwable arg1)
			throws Exception {
		// TODO Auto-generated method stub
		//arg1.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession arg0, Object message) throws Exception {
		// TODO Auto-generated method stub
		byte[] data = (byte[])message;
		System.out.println("mina cpclient接受信息:"+CommFunction.bytes2HexStr(data," "));
		
	}

	@Override
	public void messageSent(IoSession arg0, Object message) throws Exception {
		// TODO Auto-generated method stub
		byte[] data = (byte[])message;
		System.out.println("cpclient发送信息===:"+CommFunction.bytes2HexStr(data," "));
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("cpclient与:"+session.getRemoteAddress().toString()+"断开连接");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("cpclient与:"+session.getRemoteAddress().toString()+"建立连接");
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		System.out.println( "cpclientIDLE " + session.getIdleCount( status ));
	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("cpclient打开连接");
	}

	@Override
	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

