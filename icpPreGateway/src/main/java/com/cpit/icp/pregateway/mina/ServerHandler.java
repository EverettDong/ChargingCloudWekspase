package com.cpit.icp.pregateway.mina;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.filterchain.IoFilter.NextFilter;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.cpit.icp.pregateway.message.dto.MessageDto;
import com.cpit.icp.pregateway.message.impl.ExceptionMgmt;
import com.cpit.icp.pregateway.message.impl.MessageMgmt;
import com.cpit.icp.pregateway.message.dto.ExceptionDto;
import com.cpit.icp.pregateway.util.CommFunction;
import com.cpit.icp.pregateway.util.DateUtil;
import com.cpit.icp.pregateway.util.Decode;
import com.cpit.icp.pregateway.util.MessageInfo;
import com.cpit.icp.pregateway.util.ParamConfigInfo;

@Component
public class ServerHandler implements IoHandler {

	@Autowired
	private MessageMgmt mgmt;
	@Autowired
	private ExceptionMgmt emgmt;

	@Autowired
	private RedisTemplate redisTemplate;

	// @Autowired
	// private FlowMgmt fmgmt;

	private final static Logger logger = LoggerFactory.getLogger(ServerHandler.class);
	// public static Map<String, IoSession> sessionMap = new
	// ConcurrentHashMap<String, IoSession>();
	public static Map<String, IoSession> session2Map = new ConcurrentHashMap<String, IoSession>();
	// public static Map<String, String> idMap = new ConcurrentHashMap<String,
	// String>();
	public static Map<String, IoSession> codeSession = new ConcurrentHashMap<String, IoSession>();

	// public static Map<String, CollectClient> codeClient = new
	// ConcurrentHashMap<String, CollectClient>();

	public static Map<String, IoConnector> codeConnector = new ConcurrentHashMap<String, IoConnector>();

	public static Map<String, String> idcode = new ConcurrentHashMap<String, String>();
	
	

	private String code;

	private String ip_port;

	private Object obj;

	private int flow;

	private int iflow;

	private String command;

	public static final String POLE_CODE = "pole_code";

	@Override
	public void exceptionCaught(IoSession session, Throwable exception) throws Exception {
		// arg1.printStackTrace();
		if (exception.getMessage().equalsIgnoreCase("Connection reset by peer")) {
			InetSocketAddress inetAddress = (InetSocketAddress) session.getRemoteAddress();
			String key = inetAddress.getHostString() + "+" + inetAddress.getPort();

			// Long sessionId=session.getId();
			// System.out.println("exceptionCaught::"+exception.getMessage());
			session2Map.remove(key);
			session.close(true);
			// 客户端session2
			// IoSession session2=session2Map.get(sessionId);
			if (session.getAttribute(POLE_CODE) != null) {
				codeSession.remove(session.getAttribute(POLE_CODE));
			}
			logger.info("exceptionCaught::" + key + ":" + session);
			// 关闭客户端session2
			// session2.setAttribute("teststatus", "pole_session_close");
			// session2.close(true);
			// idcode.remove(sessionId);
		}

	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {

		byte[] data = (byte[]) message;
		InetSocketAddress inetAddress = (InetSocketAddress) session.getRemoteAddress();

		// 用于测试 正式环境应注释
		// command = Decode.getCommand(data);
		// System.out.println("=========收到桩的信息:" + CommFunction.bytes2HexStr(data, " "));

		// 上行报文写入日志文件
		logger.info("sxReceived上行报文:" + inetAddress.getHostString());
		logger.info("sxMessage上行报文:" + CommFunction.bytesToHexStr(data));

		// 接入鉴权判断
		// 1.判断ip+port 在不在map中 map 设备编码/ip+port
		String key = inetAddress.getHostString() + "+" + inetAddress.getPort();

		// 过滤不正确的保文 ：报文长度、长度域、检验和是否正确
		Byte version = data[3];
		String info = Decode.DecodePackageStyle(version, data);

		if (info.length() != 0) {
			emgmt.addException(new ExceptionDto(inetAddress.getHostString(), inetAddress.getPort(), MessageInfo.type1,
					CommFunction.bytesToHexStr(data), info));
			return;
		}
		String ver = Decode.getVersion(version);
		if (!MessageInfo.ipcode.containsKey(key)) {
			// 2.不在的话，说明是第一次接入 验证是不是10/1b保文
			if (!Decode.is0x10(data)) {
				logger.error("第一次不是10报文!");
				return;
			} else {
				// 3.验证10/1b保文中的设备编码是否是规则表中
				code = Decode.Decode10Package(data);
				logger.info("设备编码:" + code);
				if (!MessageInfo.iptab.containsKey(code)) {
					// map中没有的话
					// 原代码有网关相关信息 ？
					ip_port = key;
					MessageInfo.iptab.put(code, ip_port);
					MessageInfo.ipcode.put(key, code);

				} else {
					// 4.在的话将 map添加 设备编码/ip+port
					MessageInfo.ipcode.put(key, code);
					ip_port = MessageInfo.iptab.get(code);
				}
			}
		}

		// 流量控制 正式环境应打开
//		if (MessageInfo.ipcode.containsKey(key)) {
//			code = MessageInfo.ipcode.get(key);
//			obj = redisTemplate.opsForValue().get(code);
//			flow = obj == null ? 0 : (int) obj;
//			redisTemplate.opsForValue().set(code, flow + data.length, DateUtil.getSecsToEndOfCurrentDay(),
//					TimeUnit.SECONDS);
//
//			// iflow = fmgmt.getFlowByCode(code);
//			iflow = MessageInfo.deviceMsgThreshold;
//			logger.info(code + "当日流量使用：" + flow + ",当日流量阀值：" + iflow + "m");
//			if (iflow != 0) {
//				if (flow > iflow * 1024 * 1024) {
//					// System.out.println("流量已超出！！！");
//					logger.error(code + "流量已超出！！！");
//					// session.close();
//					// 流量超出只发5.4节状态报文
//					if (!MessageInfo.StatesMsg.contains("0x" + CommFunction.byteToHexStr2(data[5]))) {
//						return;
//					}
//
//				}
//			}
//		}

		// 上行报文入库 0x1b,0x10
//		if (MessageInfo.Storage.contains("0x" + CommFunction.byteToHexStr2(data[5]))) {
//			logger.info("上行报文入库 comand= 0x"+ CommFunction.byteToHexStr2(data[5]));
//			System.out.println("上行报文入库 0x1B,0x10");
//			MessageDto dto = new MessageDto();
//			dto.setDevice_no(code);
//			dto.setDevice_ip(inetAddress.getHostString());
//			dto.setMessage(CommFunction.bytesToHexStr(data));
//			dto.setDevice_port(inetAddress.getPort());
//			dto.setType(MessageInfo.type1);
//			dto.setCommand(Decode.getCommand(data));
//			dto.setVersion(ver);
//			dto.setMonth(DateUtil.getMonth());
//			dto.setData(data);
//			mgmt.addMessage(dto);
//		}
//
//		session.setAttribute(POLE_CODE, MessageInfo.ipcode.get(key));
//		mgmt.dealMessage(inetAddress.getHostString() , inetAddress.getPort(), ver, code, Decode.getCommand(data), data);
//		IoSession session2 = null;
		// 客户端管理
		// if(session2Map.get(key)==null){
		// IoConnector ioConnector;
		// if(codeConnector.get(MessageInfo.ipcode.get(key))==null){
		// CollectClient client = new CollectClient();
		// ioConnector = client.creatClient();
		// codeConnector.put(MessageInfo.ipcode.get(key), ioConnector);
		// //添加重连机制
		// ioConnector.getFilterChain().addLast("exec", new ExecutorFilter());
		// ioConnector.getFilterChain().addFirst("reconnection", new IoFilterAdapter() {
		// @Override
		// public void sessionClosed(NextFilter nextFilter, IoSession ioSession) throws
		// Exception {
		// Object tmpStatus=ioSession.getAttribute("teststatus");
		// logger.info("sessionClosed 断线重连["+String.valueOf(tmpStatus)+"]");
		// if(tmpStatus!=null && !tmpStatus.equals("pole_session_close")){
		// for(;;){
		// try{
		// Thread.sleep(3000);
		// logger.info("sessionClosed 会话数["+ioConnector.getManagedSessionCount()+"]");
		// //logger.info("断线重连["+ioSession+"]成功");
		// IoSession sessionTmp =
		// CollectClient.getIOSession(ioConnector,ip_port.split("\\+")[0],Integer.parseInt(ip_port.split("\\+")[1]));//
		// 获取会话
		// for(int i=0;i<10;i++){
		// if(session.isConnected()){
		// logger.info("断线重连["+ sessionTmp.getRemoteAddress().toString()+"]成功");
		// sessionTmp.setAttribute(POLE_CODE, MessageInfo.ipcode.get(key));
		// session2Map.put(MessageInfo.ipcode.get(key),sessionTmp);
		// idcode.put(sessionTmp.getId() + "", MessageInfo.ipcode.get(key));
		// break;
		// }
		// Thread.sleep(1000);
		// }
		// break;
		//
		// }catch(Exception ex){
		// logger.info("重连服务器登录失败,3秒再连接一次:" + ex.getMessage());
		// //System.out.println("重连服务器登录失败,3秒再连接一次:" + ex.getMessage());
		// }
		// }
		// }
		// }
		//
		// });
		// }else{
		// ioConnector=codeConnector.get(code);
		// }
		// Thread.sleep(1000);
		// session2=
		// CollectClient.getIOSession(ioConnector,ip_port.split("\\+")[0],Integer.parseInt(ip_port.split("\\+")[1]));
		// if(null==session2){
		// logger.info("后端网关无法连接，session创建失败！");
		// //System.out.println("后端网关无法连接，session创建失败！");
		// return;
		// }
		// System.out.println("创建新会话："+session2);
		// session2.setAttribute(POLE_CODE, MessageInfo.ipcode.get(key));
		//
		// CollectClient.sendMsg(session2, data);
		// session2Map.put(key,session2);
		// idcode.put(session2.getId() + "", MessageInfo.ipcode.get(key));
		// codeSession.put(code + "", session);
		// }else{
		// session2=session2Map.get(key);
		// System.out.println("已存在会话："+session2);
		// if(!session2.isClosing()
		// && session2.isConnected()){
		// session2.write(data);
		// }else{
		// session2Map.remove(key);
		// }
		//
		//
		// }
		// 桩重启应答报文
//		if (command.equalsIgnoreCase("0x53")) {
//			session2Map.remove(key);
//			session.close(true);
//			// 客户端session2
//			// IoSession session2=session2Map.get(sessionId);
//			if (session.getAttribute(POLE_CODE) != null) {
//				codeSession.remove(session.getAttribute(POLE_CODE));
//			}
//			logger.info("0x53-----" + key + ":" + session);
//		}

	}

	@Override
	public void messageSent(IoSession arg0, Object arg1) throws Exception {

		logger.info("server发送信息" + CommFunction.bytes2HexStr((byte[]) arg1, " "));
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		logger.info("server:" + session.getRemoteAddress().toString() + "断开连接");
		InetSocketAddress inetAddress = (InetSocketAddress) session.getRemoteAddress();
		String key = inetAddress.getHostString() + "_" + inetAddress.getPort();
		session2Map.remove(key);
		MessageInfo.ipcode.remove(key);

	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		logger.info("server:" + session.getRemoteAddress().toString());
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		if (session.isClosing()) {
			return;
		}
		long idleTime = System.currentTimeMillis() - session.getLastReadTime();
		logger.info("serverHandler间隔时长:" + idleTime);
		String timeoutStr = ParamConfigInfo.props.getProperty("pole.idle.tomeout");
		if ((idleTime > Long.parseLong(timeoutStr)) && (!session.isClosing())) {
			// if(session != null && !session.isClosing()){
			InetSocketAddress inetAddress = (InetSocketAddress) session.getRemoteAddress();
			String key = inetAddress.getHostString() + "+" + inetAddress.getPort();
			Long sessionId = session.getId();
			session.close(true);
			session2Map.remove(key);
			if (session.getAttribute(POLE_CODE) != null) {
				codeSession.remove(session.getAttribute(POLE_CODE));
			}
		}

	}

	@Override
	public void sessionOpened(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputClosed(IoSession arg0) throws Exception {
		// TODO Auto-generated method stub

	}

}
