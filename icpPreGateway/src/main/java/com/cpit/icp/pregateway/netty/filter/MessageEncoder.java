package com.cpit.icp.pregateway.netty.filter;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Object> {

	 
		public MessageEncoder() {
		}
	 
		@Override
		protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
			@SuppressWarnings("resource")
			ByteBufOutputStream writer = new ByteBufOutputStream(out);
			byte[] body = (byte[])msg;
			writer.write(body);
		
		}


}
