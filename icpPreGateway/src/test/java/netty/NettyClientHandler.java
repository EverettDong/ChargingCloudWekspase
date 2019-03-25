package netty;


import com.cpit.icp.pregateway.util.CommFunction;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class NettyClientHandler extends SimpleChannelInboundHandler<Object> {


	@Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg)  
            throws Exception {  
		System.out.println("客户端收到===" );
		byte[] data = (byte[]) msg;
		System.out.println("客户端收到报文解析===" +  data);
		System.out.println("s客户端收到===" +  CommFunction.bytes2HexStr(data, " "));
    
    }  

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			System.out.println("客户端异常===" + cause);
		}
	 
		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			
		}
	 
	 
		@Override
		public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
			
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
			// TODO Auto-generated method stub
			
		}


}
