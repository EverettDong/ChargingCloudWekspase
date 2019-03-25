package netty;

import java.util.List;

import com.cpit.icp.pregateway.util.CommFunction;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class TeDecoder extends ByteToMessageDecoder {

	public TeDecoder() {
 
	}
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception { 
		      System.out.println("==客户端接收");
			// 可读长度必须大于基本长度
			if (in.readableBytes() > 4) {
				// 防止socket字节流攻击
				// 防止，客户端传来的数据过大
				// 因为，太大的数据，是不合理的
				if (in.readableBytes() > 20480) {
					in.skipBytes(in.readableBytes());
				}

				// 记录包头开始的index
				int beginReader;
				int bodyLength;
				while (true) {
					// 获取包头开始的index
					beginReader = in.readerIndex();
					// 标记包头开始的index
					in.markReaderIndex();
					// 读信息头，找到正文的大小 4字节
					byte[] header = new byte[4];
					in.readBytes(header);
					bodyLength = CommFunction.unsignByteToInt(header[2]);
					// 当可读的内容，是一个完整的消息的时候，跳出循环进入下一步处理
					if (in.readableBytes() >= bodyLength) {
						break;
					} else {
						// 还原读指针
						in.readerIndex(beginReader);
						return;
					}
				}
				// 还原读指针
				in.readerIndex(beginReader);
				// 读取data数据
				byte[] data = new byte[4 + bodyLength];
				in.readBytes(data);
				ByteBuf msg = Unpooled.directBuffer(4 + bodyLength);
				msg.writeBytes(data);
				out.add(data);
			}
		}

}

