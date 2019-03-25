package com.cpit.icp.pregateway.netty.filter;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.pregateway.mina.filter.MyDecoder;
import com.cpit.icp.pregateway.util.CommFunction;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class MessageDecoder extends ByteToMessageDecoder {
	private final static Logger log = LoggerFactory.getLogger(MessageDecoder.class);

	private static final int MAGIC_NUMBER = 0x0CAFFEE0;
	public MessageDecoder() {
 
	}
	/*@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 14) {
			return;
		}
		// 标记开始读取位置
		in.markReaderIndex();
 
		int magic_number = in.readInt();
 
		if (MAGIC_NUMBER != magic_number) {
			ctx.close();
			return;
		}
 
		@SuppressWarnings("unused")
		byte version = in.readByte();
 
		byte type = in.readByte();
		int squence = in.readInt();
		int length = in.readInt();
 
		if (length < 0) {
			ctx.close();
			return;
		}
 
		if (in.readableBytes() < length) {
			// 重置到开始读取位置
			in.resetReaderIndex();
			return;
		}
 
		byte[] body = new byte[length];
		in.readBytes(body);
 
		RequestInfoVO req = new RequestInfoVO();
		req.setBody(new String(body, "utf-8"));
		req.setType(type);
		req.setSequence(squence);
		out.add(req);
		
	}*/
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception { 
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
				int len;
				Byte version;
				while (true) {
					// 获取包头开始的index
					beginReader = in.readerIndex();
					// 标记包头开始的index
					in.markReaderIndex();
					// 读信息头，找到正文的大小 4字节
					byte[] header = new byte[4];
					in.readBytes(header);
					if (header[0] != -6 || header[1] != -11)// 用来当拆包时候剩余长度小于4的时候的保护
					{
						// 还原读指针
						in.readerIndex(beginReader);
						return;
					}
					bodyLength = CommFunction.unsignByteToInt(header[2]);
					version=header[3];
					// 当可读的内容，是一个完整的消息的时候，跳出循环进入下一步处理
					if (in.readableBytes() >= bodyLength) {
						len=in.readableBytes();
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
				//正常长度
			    //12/28粘包注释掉	int dlen=4 + len;
				int dlen=4 + bodyLength;
				//35版本，如长度域为FF需特殊处理
//				if(bodyLength==255) {
//					if(version.equals((byte) 0x35))
//                      dlen=len;
//				}
				byte[] data = new byte[dlen];
				in.readBytes(data);
				ByteBuf msg = Unpooled.directBuffer(dlen);
				msg.writeBytes(data);
				out.add(data);
			}
		}

}

