package netty;

import com.cpit.icp.pregateway.util.CommFunction;

public class testAllMsg {
	
	public static void main(String[] args) throws Exception {
		NettyClientBootstrap bootstrap = new NettyClientBootstrap(9999, "10.3.92.49");
        //3.4版本的报文
		String str10 = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
		byte[] b10 = CommFunction.hexString2ToBytes(str10);
		bootstrap.getSocketChannel().writeAndFlush(b10); 
		
		String str79 = "FA F5 8A 80 81 79 00 00 00 00 01 36 38 30 36 31 33 31 35 30 30 30 30 30 32 36 35 31 32 30 30 30 30 30 30 30 30 30 30 30 30 35 32 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 32 01 02 03 04 05 06 07 08 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 35 20 20 20 20 20 20 20 20 34 34 00 00 00 09 F5 00 00 00 01 01 00 51 DF 07 04 11 09 20 11 FF DF 07 04 11 09 24 16 FF DF 07 04 11 09 24 18 FF B8 01 00 00 C2 01 00 00 4F";
		byte[] b79 = CommFunction.hexString2ToBytes(str79);
		bootstrap.getSocketChannel().writeAndFlush(b79);
		
		String str58 = "FA F5 09 80 D4 58 00 00 00 00 D4 00 2C";
		byte[] b58 = CommFunction.hexString2ToBytes(str58);
		bootstrap.getSocketChannel().writeAndFlush(b58); 
 
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
