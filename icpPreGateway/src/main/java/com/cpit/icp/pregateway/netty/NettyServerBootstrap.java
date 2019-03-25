package com.cpit.icp.pregateway.netty;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.pregateway.netty.filter.MessageDecoder;
import com.cpit.icp.pregateway.netty.filter.MessageEncoder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
/**
 * netty启动
 * @author admin
 *
 */
public class NettyServerBootstrap {

	private final static Logger logger = LoggerFactory.getLogger(NettyServerBootstrap.class);

	private Integer port;
	private SocketChannel socketChannel;

	public NettyServerBootstrap(Integer port) throws Exception {
		this.port = port;
		bind(port);
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public SocketChannel getSocketChannel() {
		return socketChannel;
	}

	public void setSocketChannel(SocketChannel socketChannel) {
		this.socketChannel = socketChannel;
	}
    /**
     * 绑定端口 启动netty服务器端
     * @param serverPort
     * @throws Exception
     */
	private void bind(int serverPort) throws Exception {
		// 连接处理group
		EventLoopGroup boss = new NioEventLoopGroup();
		// 事件处理group
		EventLoopGroup worker = new NioEventLoopGroup();
		ServerBootstrap bootstrap = new ServerBootstrap();
		// 绑定处理group
		bootstrap.group(boss, worker);
		bootstrap.channel(NioServerSocketChannel.class);
		// 保持连接数
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024 * 1024);
		// 有数据立即发送
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		// 保持连接
		bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
		// 处理新连接
		bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				// 增加任务处理
				ChannelPipeline p = sc.pipeline();
				//读超时时间、写超时时间、读写超时时间
				p.addLast(new IdleStateHandler(300, 0, 0 , TimeUnit.SECONDS));
				p.addLast(new MessageDecoder(), new MessageEncoder(), new NettyServerHandler());
				
			}
		});

		ChannelFuture f = bootstrap.bind(serverPort).sync();
		if (f.isSuccess()) {
			logger.info("long connection started success");
		} else {
			logger.error("long connection started fail");
		}
	}

}
