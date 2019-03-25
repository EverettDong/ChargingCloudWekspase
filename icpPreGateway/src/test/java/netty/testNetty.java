package netty;

import com.cpit.icp.pregateway.util.CommFunction;

public class testNetty {

public static void main(String[] args) throws Exception {
		NettyClientBootstrap bootstrap = new NettyClientBootstrap(9999, "10.3.92.40");

		String str1 = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
		//String str1 = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 35 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
		byte[] b1 = CommFunction.hexString2ToBytes(str1);
		bootstrap.getSocketChannel().writeAndFlush(b1); 
 
//		while (true) {
//			TimeUnit.SECONDS.sleep(100);
//
//			String str = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
//			byte[] b = CommFunction.hexString2ToBytes(str);
//			bootstrap.getSocketChannel().writeAndFlush(b);
//
//		}

}
}
