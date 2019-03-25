package com.cpit.icp.pregateway.mina.filter;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.cpit.icp.pregateway.util.CommFunction;

public class MyEnCoder extends ProtocolEncoderAdapter {

	// 编码 将数据包转成字节数组
	@Override
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {

		byte[] dataBytes = (byte[]) message;
		
		IoBuffer buffer = IoBuffer.allocate(2048);
		buffer.setAutoExpand(true);
		buffer.put(dataBytes);
		buffer.flip();
		out.write(buffer);
		out.flush();
		buffer.free();
	}
}