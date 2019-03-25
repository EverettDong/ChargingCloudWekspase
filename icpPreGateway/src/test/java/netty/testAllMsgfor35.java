package netty;

import java.util.concurrent.TimeUnit;

import com.cpit.icp.pregateway.util.CommFunction;
import com.cpit.icp.pregateway.util.Decode;

public class testAllMsgfor35 {
	
	
	public static void main(String[] args) throws Exception {
		NettyClientBootstrap bootstrap = new NettyClientBootstrap(9999, "10.3.92.40");
        //3.5版本的报文
		try {
		String str1B = "FA F5 75 35 D4 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 1B 01 02 03 04 05 06 07 08 34 33 32 31 31 32 33 34 35 36 41 42 43 44 45 46 47 48 49 4A 01 02 03 00 05 02 00 00 DB 07 0C 14 0D 0D 0D 2C DA 07 0C 14 0D 0D 0D 2D 01 02 03 04 05 06 07 08 31 32 33 34 35 36 37 38 39 38 39 3C 3D 3E 3F 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F 50 51 01 02 03 00 02 F5";
		byte[] b1B = CommFunction.hexString2ToBytes(str1B);
		bootstrap.getSocketChannel().writeAndFlush(b1B); 
//		TimeUnit.SECONDS.sleep(1);
		String str7D = "FA F5 c1 35 02 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 7D 01 00 02 00 B3 36 35 34 33 32 31 37 38 39 39 38 37 36 35 34 33 34 31 32 37 32 31 31 39 38 30 30 31 32 30 30 36 36 37 38 39 32 31 34 31 32 37 32 31 31 39 38 30 30 31 32 30 30 36 36 37 38 39 31 31 39 c1 d4 41 42 31 32 33 34 36 37 01 12 13 14 12 20 16 39 38 30 30 31 32 01 11 12 13 11 18 19 12 13 14 00 12 25 01 13 14 15 13 19 20 12 13 35 00 25 32 01 14 15 16 14 18 12 12 13 15 00 12 23 24 15 12 13 11 12 13 14 05 05 00 01 DB 07 0C 14 0D 0D 0D FF DB 07 0C 14 0D 0D 0D FF DB 07 0C 14 0D 0D 0D FF 12 13 00 00 25 21 00 00 01 48";
		byte[] b7D = CommFunction.hexString2ToBytes(str7D);
		bootstrap.getSocketChannel().writeAndFlush(b7D);
//		TimeUnit.SECONDS.sleep(1);
		String str58 = "FA F5 1A 35 D4 01 02 03 04 05 06 07 08 DB 07 0C 14 0D 0D 0D 2C 01 58 00 01 00 02 11 22 8E";
		byte[] b58 = CommFunction.hexString2ToBytes(str58);
		bootstrap.getSocketChannel().writeAndFlush(b58); 
		
		Byte version = b1B[3];
		String info1 = Decode.DecodePackageStyle(version, b1B);
		System.out.println(info1);
//		String str = "1B 01 02 03 04 05 06 07 08 34 33 32 31 31 32 33 34 35 36 41 42 43 44 45 46 47 48 49 4A 01 02 03 00 05 02 00 00 DB 07 0C 14 0D 0D 0D 2C DA 07 0C 14 0D 0D 0D 2D 01 02 03 04 05 06 07 08 31 32 33 34 35 36 37 38 39 38 39 3C 3D 3E 3F 41 42 43 44 45 46 47 48 49 4A 4B 4C 4D 4E 4F 50 51 01 02 03 00 02";
//		byte[] b = CommFunction.hexString2ToBytes(str);
//		int validnum = CommFunction.sumValidityValue(b);
//		System.out.println(validnum);
		Byte version2 = b7D[3];
		String info2 = Decode.DecodePackageStyle(version2, b7D);
		System.out.println(info2);
		Byte version3 = b58[3];
		String info3 = Decode.DecodePackageStyle(version, b58);
		System.out.println(info3);
		} catch (Exception ex) {
          ex.printStackTrace();
		}
 


}

}
