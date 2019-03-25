package com.cpit.icp.pregateway.mina.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.pregateway.util.CommFunction;

public class MyDecoder extends CumulativeProtocolDecoder {
	// 解码打印日志信息
	private final static Logger log = LoggerFactory.getLogger(MyDecoder.class);

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {

		// System.out.println(session.getId()+"::"+in.toString()+"::"+out.toString());
		// System.out.println("size::"+in.remaining());
		if (in.remaining() < 4)// 用来当拆包时候剩余长度小于4的时候的保护
		{
			return false;
		}
		in.mark();// 标记当前位置，以便reset

		if (in.get(0) != -6 || in.get(1) != -11)// 用来当拆包时候剩余长度小于4的时候的保护
		{
			in.reset();
			return false;
		}

		// FAF50F8000090102000001

		log.info("in.remaining------ : " + in.remaining());
		// System.out.println("in.remaining : "+in.remaining());
		if (in.remaining() > 0) {// 有数据时，读取前8字节判断消息长度
			byte sizeBytes;
			int pos = in.position();
			sizeBytes = in.get(pos + 2);
			// System.out.println("sizeBytes-----:"+CommFunction.byteToHexStr(sizeBytes));
			int size = CommFunction.unsignByteToInt(sizeBytes) + 4;
			log.info("size : " + size);
			in.reset();
			if (size > in.remaining()) {// 如果消息内容不够，则重置，相当于不读取size
				return false;// 父类接收新数据，以拼凑成完整数据
			} else {
				byte[] bytes = new byte[size];
				in.get(bytes, 0, size);
				// System.out.println("bytes----- : "+CommFunction.bytes2HexStr(bytes, " "));
				// 把字节转换为Java对象的工具类
				// PackageData pack = packetComponent.getDataFromBuffer(IoBuffer.wrap(bytes));
				out.write(bytes);
				if (in.remaining() > 0) {// 如果读取内容后还粘了包，就让父类再重读 一次，进行下一次解析

					return true;
				}
			}
		}
		return false;// 处理成功，让父类进行接收下个包

	}
}