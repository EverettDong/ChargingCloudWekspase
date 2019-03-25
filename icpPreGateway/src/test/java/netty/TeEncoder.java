package netty;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class TeEncoder extends MessageToByteEncoder<Object> {

	 
		public TeEncoder() {
		}
	 
		@Override
		protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
	 
			@SuppressWarnings("resource")
			ByteBufOutputStream writer = new ByteBufOutputStream(out);
			byte[] body = (byte[])msg;
	
			writer.write(body);
		
		}


}
