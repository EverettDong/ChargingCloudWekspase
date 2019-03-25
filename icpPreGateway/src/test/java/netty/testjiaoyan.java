package netty;


import com.cpit.icp.pregateway.util.CommFunction;

public class testjiaoyan {
	
	public static void main(String[] args) throws Exception {

//		String str10 = "10 01 02 03 04 05 06 07 08 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF";
//		byte[] b10 = CommFunction.hexString2ToBytes(str10);
		String str58 = "58 00 00 00 00 D4 00";
		byte[] b58 = CommFunction.hexString2ToBytes(str58);
		int ff=CommFunction.sumValidityValue(b58);
		System.out.println(ff);
 
		
		
	}

}
