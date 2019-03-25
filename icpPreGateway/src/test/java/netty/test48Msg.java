package netty;

import com.cpit.icp.pregateway.util.ArraysN;
import com.cpit.icp.pregateway.util.CommFunction;
import com.cpit.icp.pregateway.util.MessageInfo;

public class test48Msg {
	
public static void main(String[] args) throws Exception {
		
		
		String str58 = "FA F5 09 80 D4 58 00 00 00 00 D4 00 2C";
		byte[] data = CommFunction.hexString2ToBytes(str58);
		
		byte[] head = new byte[5];
		byte[] body = new byte[6];
		ArraysN.copy(head, 0, data, 0, 5);
		ArraysN.copy(body, 0, data, 6, 6);
		
		byte[] rebody=ArraysN.addBytes(MessageInfo.msg_0x48, body);
		
		int validnum = CommFunction.sumValidityValue(rebody);
		
		byte[] valid = CommFunction.shortToLowBytes(validnum);
		
		rebody=ArraysN.addBytes(rebody, valid);
		
		byte[] message =ArraysN.addBytes(head, rebody);
		
		
		System.out.println("==" + CommFunction.bytes2HexStr(message, " "));

	}

}
