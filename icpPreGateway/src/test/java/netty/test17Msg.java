package netty;

import com.cpit.icp.pregateway.util.CommFunction;
/**
 * 测试3.4版本 17 18 39报文过滤
 * @author admin
 *
 */
public class test17Msg {
	
	public static void main(String[] args) throws Exception {
		NettyClientBootstrap bootstrap = new NettyClientBootstrap(9999, "10.3.92.40");
        //3.4版本的报文
		String str10 = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
		byte[] b10 = CommFunction.hexString2ToBytes(str10);
		bootstrap.getSocketChannel().writeAndFlush(b10); 
//		bootstrap.getSocketChannel().writeAndFlush(b10);
//		bootstrap.getSocketChannel().writeAndFlush(b10);
		
		String str17 = "FA F5 24 80 61 17 00 00 00 00 01 01 F8 11 C4 09 38 0E 72 01 96 00 01 3C 0F 70 0D 72 01 6E 00 00 02 01 01 6A 01 3C 00 93";
		byte[] b17 = CommFunction.hexString2ToBytes(str17);
		bootstrap.getSocketChannel().writeAndFlush(b17);
		bootstrap.getSocketChannel().writeAndFlush(b17);
		bootstrap.getSocketChannel().writeAndFlush(b17);
		bootstrap.getSocketChannel().writeAndFlush(b17);
		//再发18
		String str18 = "FA F5 24 80 61 18 00 00 00 00 01 01 F8 11 C4 09 38 0E 72 01 96 00 01 3C 0F 70 0D 72 01 6E 00 00 02 01 01 6A 01 3C 01 95";
		byte[] b18 = CommFunction.hexString2ToBytes(str18);
		bootstrap.getSocketChannel().writeAndFlush(b18);
		bootstrap.getSocketChannel().writeAndFlush(b18);
		
		String str39 = "FA F5 15 80 6B 39 00 00 00 00 01 01 01 00 00 00 44 00 34 34 BA 01 00 00 A3";
		byte[] b39 = CommFunction.hexString2ToBytes(str39);
		bootstrap.getSocketChannel().writeAndFlush(b39);
		bootstrap.getSocketChannel().writeAndFlush(b39);
		bootstrap.getSocketChannel().writeAndFlush(b39);
		

	}
}
