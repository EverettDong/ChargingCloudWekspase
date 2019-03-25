package netty;

import java.util.concurrent.TimeUnit;

import com.cpit.icp.pregateway.util.CommFunction;

public class test53Msg {
	
	public static void main(String[] args) throws Exception {
		NettyClientBootstrap bootstrap = new NettyClientBootstrap(9999, "10.3.92.40");

		String str10 = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
		byte[] b10 = CommFunction.hexString2ToBytes(str10);
		bootstrap.getSocketChannel().writeAndFlush(b10); 
		
		TimeUnit.SECONDS.sleep(5);
		String str53 = "FA F5 08 80 78 53 01 02 03 00 01 5A";
		byte[] b53 = CommFunction.hexString2ToBytes(str53);
		bootstrap.getSocketChannel().writeAndFlush(b53); 
		TimeUnit.SECONDS.sleep(5);
		bootstrap.getSocketChannel().writeAndFlush(b10); 
	}
}
