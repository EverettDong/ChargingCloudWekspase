package com.cpit.icp.pregateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cpit.icp.pregateway.message.MsgFilter;
import com.cpit.icp.pregateway.util.FtpUtil;
import com.cpit.icp.pregateway.util.MessageInfo;
//import com.cpit.icp.pregateway.mina.ServerHandler;
//import com.cpit.icp.pregateway.mina.filter.MyFactory;
import com.cpit.icp.pregateway.util.ParamConfigInfo;
import com.cpit.icp.pregateway.util.ResfulUtil;

@Configuration
public class Init {

	private final static Logger logger = LoggerFactory.getLogger(Init.class);

	@Value("${collect_port}")
	private int collectPort;
	
	@Value("${collect_ip}")
	private String collectIp;
	
	@Value("${resful_url}")
	private String resfulUrl;

	@Value("${ne_storage}")
	private String ne_storage;

	@Value("${must_send}")
	private String must_send;

	@Value("${must_send35}")
	private String must_send35;

	@Value("${ftp_ip}")
	private String ftp_ip;

	@Value("${ftp_port}")
	private int ftp_port;

	@Value("${ftp_name}")
	private String ftp_name;
	
	@Value("${server.port}")
	private String resful_port;

	@Value("${ftp_password}")
	private String ftp_password;
	
	@Value("${filter_msg}")
	private String filter_msg;
	
	@Value("${msg_34_0x17_switch}")
	private String msg_34_0x17_switch;
	
	@Value("${msg_34_0x17_interval}")
	private int msg_34_0x17_interval;
	
	@Value("${msg_34_0x18_switch}")
	private String msg_34_0x18_switch;
	
	@Value("${msg_34_0x18_interval}")
	private int msg_34_0x18_interval;
	
	@Value("${msg_34_0x32_switch}")
	private String msg_34_0x32_switch;
	
	@Value("${msg_34_0x32_interval}")
	private int msg_34_0x32_interval;
	
	@Value("${msg_34_0x33_switch}")
	private String msg_34_0x33_switch;
	
	@Value("${msg_34_0x33_interval}")
	private int msg_34_0x33_interval;
	
	@Value("${msg_34_0x39_switch}")
	private String msg_34_0x39_switch;
	
	@Value("${msg_34_0x39_interval}")
	private int msg_34_0x39_interval;
	
	@Value("${msg_34_0x3A_switch}")
	private String msg_34_0x3A_switch;
	
	@Value("${msg_34_0x3A_interval}")
	private int msg_34_0x3A_interval;
	
	@Value("${msg_34_0x3B_switch}")
	private String msg_34_0x3B_switch;
	
	@Value("${msg_34_0x3B_interval}")
	private int msg_34_0x3B_interval;
	
	@Value("${msg_35_0x1E_switch}")
	private String msg_35_0x1E_switch;
	
	@Value("${msg_35_0x1E_interval}")
	private int msg_35_0x1E_interval;
	
	@Value("${msg_35_0x1F_switch}")
	private String msg_35_0x1F_switch;
	
	@Value("${msg_35_0x1F_interval}")
	private int msg_35_0x1F_interval;
	
	@Value("${msg_35_0x39_switch}")
	private String msg_35_0x39_switch;
	
	@Value("${msg_35_0x39_interval}")
	private int msg_35_0x39_interval;
	
	@Value("${msg_35_0x3A_switch}")
	private String msg_35_0x3A_switch;
	
	@Value("${msg_35_0x3A_interval}")
	private int msg_35_0x3A_interval;
	
	@Value("${msg_34_0x3B_switch}")
	private String msg_35_0x3B_switch;
	
	@Value("${msg_34_0x3B_interval}")
	private int msg_35_0x3B_interval;
	
	@Value("${re_msg_num}")
	private int reSendNum;
	
	@Value("${re_msg_Redis}")
	private int reSendRedis;


	@Bean
	public String initMap() {
		logger.info("initMap start....");
		
		MessageInfo.reSendNum=10 > reSendNum ? reSendNum:10;
		MessageInfo.reSendRedis=20 > reSendRedis ? reSendRedis:20;
		
		MsgFilter.msg_34_0x17_switch=msg_34_0x17_switch;
		MsgFilter.msg_34_0x17_interval=msg_34_0x17_interval;
		MsgFilter.msg_34_0x18_switch=msg_34_0x18_switch;
		MsgFilter.msg_34_0x18_interval=msg_34_0x18_interval;
		MsgFilter.msg_34_0x32_switch=msg_34_0x32_switch;
		MsgFilter.msg_34_0x32_interval=msg_34_0x32_interval;
		MsgFilter.msg_34_0x33_switch=msg_34_0x33_switch;
		MsgFilter.msg_34_0x33_interval=msg_34_0x33_interval;
		MsgFilter.msg_34_0x39_switch=msg_34_0x39_switch;
		MsgFilter.msg_34_0x39_interval=msg_34_0x39_interval;
		MsgFilter.msg_34_0x3A_switch=msg_34_0x3A_switch;
		MsgFilter.msg_34_0x3A_interval=msg_34_0x3A_interval;
		MsgFilter.msg_34_0x3B_switch=msg_34_0x3B_switch;
		MsgFilter.msg_34_0x3B_interval=msg_34_0x3B_interval;
		
		MsgFilter.msg_35_0x1E_switch=msg_35_0x1E_switch;
		MsgFilter.msg_35_0x1E_interval=msg_35_0x1E_interval;
		MsgFilter.msg_35_0x1F_switch=msg_35_0x1F_switch;
		MsgFilter.msg_35_0x1F_interval=msg_35_0x1F_interval;
		MsgFilter.msg_35_0x39_switch=msg_35_0x39_switch;
		MsgFilter.msg_35_0x39_interval=msg_35_0x39_interval;
		MsgFilter.msg_35_0x3A_switch=msg_35_0x3A_switch;
		MsgFilter.msg_35_0x3A_interval=msg_35_0x3A_interval;
		MsgFilter.msg_35_0x3B_switch=msg_35_0x3B_switch;
		MsgFilter.msg_35_0x3B_interval=msg_35_0x3B_interval;

		MessageInfo.deviceMsgThreshold = Integer.parseInt(ParamConfigInfo.props.getProperty("device.Msg.Threshold"));
		FtpUtil.server = ftp_ip;
		FtpUtil.port = ftp_port;
		FtpUtil.userName = ftp_name;
		FtpUtil.userPassword = ftp_password;
//		FtpUtil.pregateIp = ResfulUtil.getIp() +"_"+collectPort;
		FtpUtil.pregateIp = collectIp +"_"+collectPort;
		FtpUtil.upMessagePort =collectPort;
		
		ResfulUtil.resfulIp =collectIp;
		ResfulUtil.resfulPort=resful_port;
		ResfulUtil.resfulUrl=resfulUrl;
		
		String[] strfilter = filter_msg.split(",");
		for (int i = 0; i < strfilter.length; i++) {
			MsgFilter.filterList.add(strfilter[i]);
		}

		String[] strs = ne_storage.split(",");
		for (int i = 0; i < strs.length; i++) {
			MessageInfo.Storage.add(strs[i]);
		}
		String[] strstate = must_send.split(",");
		for (int i = 0; i < strstate.length; i++) {
			MessageInfo.StatesMsg.add(strstate[i]);
		}
		String[] strstate35 = must_send35.split(",");
		for (int i = 0; i < strstate35.length; i++) {
			MessageInfo.StatesMsg.add(strstate35[i]);
		}

		return "";
	}

	// 前置网关server
//	@Bean(name = "CollectServer")
//	public String startCollectServer() throws IOException {
//		System.out.println("PreGateWay start....");
//		logger.info("PreGateWay start....");
//		IoAcceptor acceptor = new NioSocketAcceptor();
//		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
//		// acceptor.getFilterChain().addLast("mycoder", new ProtocolCodecFilter(new
//		// ByteArrayCodecFactory()));
//		acceptor.getFilterChain().addLast("mycoder", new ProtocolCodecFilter(new MyFactory()));
//		acceptor.setHandler(getServerHandler());
//		acceptor.getSessionConfig().setReadBufferSize(2048);
//		if (ParamConfigInfo.props.getProperty("pole_both_idle") != null) {
//			acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,
//					Integer.parseInt(ParamConfigInfo.props.getProperty("pole_both_idle"))); // 读写都空闲时间:30秒
//		}
//		if (ParamConfigInfo.props.getProperty("pole_read_idle") != null) {
//			acceptor.getSessionConfig().setIdleTime(IdleStatus.READER_IDLE,
//					Integer.parseInt(ParamConfigInfo.props.getProperty("pole_read_idle")));// 读(接收通道)空闲时间:40秒
//		}
//		if (ParamConfigInfo.props.getProperty("pole_write_idle") != null) {
//			acceptor.getSessionConfig().setIdleTime(IdleStatus.WRITER_IDLE,
//					Integer.parseInt(ParamConfigInfo.props.getProperty("pole_write_idle")));// 写(发送通道)空闲时间:50秒
//		}
//		acceptor.bind(new InetSocketAddress(10000));
//
//		return "";
//	}
//
//	@Bean
//	public ServerHandler getServerHandler() throws IOException {
//		return new ServerHandler();
//	}

}
