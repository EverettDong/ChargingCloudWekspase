package icpPreGateway;

import com.cpit.icp.pregateway.util.MessageInfo;

public class testcode {
	
	
	public static void main(String[] args) {
		
		String code ="0102030405060308";
    	String msgData ="0102030405060308FAF58A808179000000000136383036313331353030303030323635313230F";

    	msgData=msgData.substring(code.length(), msgData.length());
		
		 System.out.println("收到桩的信息:"+msgData);
	

	}

}
