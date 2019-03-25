package com.cpit.icp.pregateway.netty;

import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.cpit.icp.pregateway.message.MsgFilter;
import com.cpit.icp.pregateway.message.ResfulSendThread;
import com.cpit.icp.pregateway.message.ResfulThreadPool;
import com.cpit.icp.pregateway.message.dto.ExceptionDto;
import com.cpit.icp.pregateway.message.dto.MessageDto;
import com.cpit.icp.pregateway.message.impl.ExceptionMgmt;
import com.cpit.icp.pregateway.message.impl.MessageMgmt;
import com.cpit.icp.pregateway.util.CommFunction;
import com.cpit.icp.pregateway.util.DateUtil;
import com.cpit.icp.pregateway.util.Decode;
import com.cpit.icp.pregateway.util.MessageInfo;

import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.channel.SimpleChannelInboundHandler;
/**
 * netty接收报文核心类
 * @author admin
 *
 */
//@Sharable
@Component
public class NettyServerHandler  extends SimpleChannelInboundHandler<Object> {
	
	private final static Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    
	@Autowired
	private MessageMgmt mgmt;
	@Autowired
	private ExceptionMgmt emgmt;
	@Autowired
	private RedisTemplate redisTemplate;
	
	private static NettyServerHandler nettyServerHandler;

    @PostConstruct
    public void init() {
    	nettyServerHandler = this;
    	nettyServerHandler.mgmt=this.mgmt;
    	nettyServerHandler.emgmt=this.emgmt;
    	nettyServerHandler.redisTemplate=this.redisTemplate;
    }
	
	//设备编码
	private String code;
	
	private String ip_port;

	private String command;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		byte[] data = (byte[]) msg;
		String msgt = CommFunction.bytesToHexStr(data);
		Byte version = data[3];
		InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIP = insocket.getAddress().getHostAddress();
        int port = insocket.getPort();
        String key = clientIP + MessageInfo.underline + port;
		if (Decode.is0x58(version, data)) {
			logger.debug("前置网关收到上行心跳报文=" + CommFunction.bytes2HexStr(data, " "));
			logger.debug("桩ip=" + key);
		}
		else {
			logger.info("前置网关收到上行报文=" + CommFunction.bytes2HexStr(data, " "));
			logger.info("桩ip=" + key);
		}
	
		
        // 接入鉴权判断
        // 1.判断ip_port 在不在map中 map 设备编码/ip_port      
        
        // 过滤不正确的保文 ：报文长度、长度域、检验和是否正确
		
		String info = Decode.DecodePackageStyle(version, data);
        //1.报文格式校验
		if (info.length() != 0) {
			nettyServerHandler.emgmt.addException(new ExceptionDto(clientIP, port, MessageInfo.type1,
					msgt, info));
			return;
		}
		
		String ver = Decode.getVersion(version);
		//2.签到报文校验
		if (!MessageInfo.ipcode.containsKey(key)) {
			// 2.不在的话，说明是第一次接入 验证是不是10/1b保文
			if (!Decode.is0x10(data)) {
				logger.warn("第一次不是10报文!");
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
					// 4.在的话将 map添加 设备编码/ip_port
					MessageInfo.ipcode.put(key, code);
					ip_port = MessageInfo.iptab.get(code);
				}
				//缓存设备连接<设备号，连接>
				NettyChannelMap.add(code, ctx.channel());
			}
		} else if (Decode.is0x10(data)){
			code = Decode.Decode10Package(data);
			logger.info("设备编码:" + code);
			if (!MessageInfo.iptab.containsKey(code)) {
				ip_port = key;
				MessageInfo.iptab.put(code, ip_port);
				MessageInfo.ipcode.put(key, code);

			} else {
				// 4.在的话将 map添加 设备编码/ip_port
				MessageInfo.ipcode.put(key, code);
				MessageInfo.iptab.put(code, ip_port);
			}
			//缓存设备连接<设备号，连接>
			NettyChannelMap.add(code, ctx.channel());

		}
		code = MessageInfo.ipcode.get(key);
		command =Decode.getCommand(data) ;
		
		//3.重发报文回复处理
        if(command.equals(MessageInfo.msg_0x15)||command.equals(MessageInfo.msg_0x7A)) {
        	reMsgDeal(code,command, data);
        }
        //4.流量超限报文过滤
		if(MsgFilter.countMsgFlow(nettyServerHandler.redisTemplate,code,key,command, data)) {
			return;
		}
		//5.心跳报文 直接回
		if (Decode.is0x58(version, data)) {
			logger.debug("心跳message code=" + code + ",message=" + CommFunction.bytes2HexStr(data, " "));
			try {
				byte[] message = CommFunction.deal58Message(data, version);
				if (ctx.channel() != null && ctx.channel().isActive()) {
					ctx.channel().writeAndFlush(message);
					// logger.debug("send 48心跳 message success,code=" + code +",msgData=" +
					// CommFunction.bytes2HexStr(message, " "));
				} else {
					// 移除失效连接
					NettyChannelMap.removebyId(code);
					logger.warn("send 48心跳 message failed,channel not exist,code=" + code);
				}

			} catch (Exception e) {
				logger.error("send 48心跳 message error!", e);
			}

			return;
		}
		//6.指定报文过滤 过滤部分上报频次高的报文
        if(MsgFilter.filterList.contains(command) &&
			MsgFilter.DecodePackageStyle(nettyServerHandler.redisTemplate, code, command, version, data)) {
			return;
        }
        
		
		// 桩重启应答报文
		if (Decode.is0x53(data)) {
			ctx.channel().close();
			logger.info("0x53桩重启应答报文code=" + code);
		}
		
		MessageDto dto = new MessageDto();
		dto.setDevice_no(code);
		dto.setDevice_ip(clientIP);
		dto.setMessage(msgt);
		dto.setDevice_port(port);
		dto.setType(MessageInfo.type1);
		dto.setCommand(command);
		dto.setVersion(ver);
		dto.setMonth(DateUtil.getMonth());
		dto.setData(data);
		try{
			ExecutorService resfulThreadPool = ResfulThreadPool.getThreadPool();
			resfulThreadPool.execute(new ResfulSendThread(dto,nettyServerHandler.mgmt) );
		}catch(Exception e){
			logger.error("dealuploadMessage error!", e);
		}
	}
 
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("exceptionCaught ="+ cause);
		if (cause.getMessage().equalsIgnoreCase("Connection reset by peer")) {
			ctx.channel().close();
			logger.error("无法支持更多连接");
//			NettyChannelMap.removebyChannel(ctx.channel());
//			mgmt.sendLostConnectMessage(code);
		}
			
	}
    
	/**
     * 客户端与服务端断开连接时调用
     */
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		 InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		 int port = insocket.getPort();
		 String ip =insocket.getAddress().getHostAddress();
		 String key= ip + MessageInfo.underline +  port;
		 String code2= MessageInfo.ipcode.get(key);
		 NettyChannelMap.removebyChannel(ctx.channel());	
		//剔除ip code映射
		 MessageInfo.iptab.remove(code2);
		 MessageInfo.ipcode.remove(key);
		 logger.info("服务端断开与客户端连接key=" + key);
		 logger.info("服务端断开与客户端连接code=" + code2);
		 if(code2 != null && code2.length() >= 0) {
			 nettyServerHandler.mgmt.sendLostConnectMessage(code2); 
		 }
		 
        
	}
	

	/**
     * 客户端与服务端创建连接的时候调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
		 int port = insocket.getPort();
		 String ip =insocket.getAddress().getHostAddress();
		 String key= ip + MessageInfo.underline +  port;
    	logger.info("客户端与服务端连接开始...ip=" +key);
    }
 
 
//	@Override
//	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
//		
//	}
	
	@Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        super.userEventTriggered(ctx, evt);
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
                //关闭连接
                ctx.channel().close();
                logger.info("长期没收到桩推送数据，断开连接ip=" + insocket.getAddress().getHostAddress());
                
            } 
        }
    }
	
	/**
	 * 特殊处理需要重发0x05、0x6C报文的回复报文
	 * 收到清redis缓存
	 * @param code
	 * @param command
	 * @param Receive
	 */
	public void  reMsgDeal(String code,String command,byte[] Receive){
		logger.info("reMsgDeal code="+ code);
		try {
			String idandSN = Decode.getidandSN(Receive);
			String key = code + MessageInfo.underline + command + MessageInfo.underline + idandSN;
			Object obj = nettyServerHandler.redisTemplate.opsForValue().get(key);
			if (obj != null) {
				nettyServerHandler.redisTemplate.delete(key);
			}
		} catch (Exception e) {
			logger.error("reMsgDeal error!", e);
		}
		
	}


}
