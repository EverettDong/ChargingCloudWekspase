package netty;

import com.cpit.icp.pregateway.util.CommFunction;

/**
 * 测试3.5版本 1E 1F 3A报文过滤
 * @author admin
 *
 */
public class test1EMsg {
	
	public static void main(String[] args) throws Exception {
		NettyClientBootstrap bootstrap = new NettyClientBootstrap(9999, "10.3.92.40");
        //3.5版本的报文
		String str1B = "FA F5 75 35 D4 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 1B 01 02 03 04 05 06 07 08 34 33 32 31 31 32 33 34 35 36 41 42 43 44 45 46 47 48 49 4A 01 02 03 00 05 02 00 00 DB 07 0C 14 0D 0D 0D 2C DA 07 0C 14 0D 0D 0D 2D 01 02 03 04 05 06 07 08 31 32 33 34 35 36 37 38 39 38 39 3C 3D 3E 3F 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F 50 51 01 02 03 00 02 F5";
		byte[] b1B = CommFunction.hexString2ToBytes(str1B);
		bootstrap.getSocketChannel().writeAndFlush(b1B); 
		
		String str1E = "FA F5 6B 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 1E 01 00 02 00 02 B3 11 42 11 10 10 24 10 12 12 44 12 23 01 10 22 10 33 10 44 10 45 01 02 00 01 02 03 04 05 06 08 09 02 02 03 04 11 12 23 35 03 22 23 12 20 44 45 10 23 11 12 12 35 03 24 46 23 35 22 48 21 35 03 02 01 09 08 07 06 05 04 03 02 03 02 03 04 05 12 25 32 CA";
		byte[] b1E = CommFunction.hexString2ToBytes(str1E);
		bootstrap.getSocketChannel().writeAndFlush(b1E);
		bootstrap.getSocketChannel().writeAndFlush(b1E);
		bootstrap.getSocketChannel().writeAndFlush(b1E);
		bootstrap.getSocketChannel().writeAndFlush(b1E);
		//再发18
		String str1F = "FA F5 70 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 1F 01 00 02 00 02 31 32 33 34 35 36 31 32 33 34 35 36 31 32 33 34 35 36 08 07 06 05 04 03 02 01 31 31 33 34 08 02 28 31 32 33 34 05 06 07 08 09 f0 B3 31 32 33 34 35 36 31 32 33 34 35 36 31 32 33 34 35 31 32 33 34 35 36 37 38 39 DC D4 B4 03 12 09 25 01 11 12 13 14 07 16 17 08 F0 D6";
		byte[] b1F = CommFunction.hexString2ToBytes(str1F);
		bootstrap.getSocketChannel().writeAndFlush(b1F);
		bootstrap.getSocketChannel().writeAndFlush(b1F);
		
		String str3A = "FA F5 38 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 3A 01 00 02 00 01 B3 01 08 07 06 05 04 03 02 01 01 02 03 04 05 06 06 05 FE 08 03 04 01 02 FA DD FE AC 07 FE 00 cc3A 01 00 02 00 01 B3 01 08 07 06 05 04 03 02 01 01 02 03 04 05 06 06 05 FE 08 03 04 01 02 FA DD FE AC 07 FE 00 cc";
		byte[] b3A = CommFunction.hexString2ToBytes(str3A);
		bootstrap.getSocketChannel().writeAndFlush(b3A);
		bootstrap.getSocketChannel().writeAndFlush(b3A);
		bootstrap.getSocketChannel().writeAndFlush(b3A);
		

	}

}
