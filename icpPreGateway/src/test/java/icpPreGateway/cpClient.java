package icpPreGateway;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.cpit.icp.pregateway.util.CommFunction;

import filter.ByteArrayCodecFactory;
import filter.cpClientHandler;




public class cpClient {

	
	private String ip ="10.3.92.40";
	private int port = 9999;
	
	public IoConnector creatClient(){  
        IoConnector connector=new NioSocketConnector();   
        connector.setConnectTimeoutMillis(30000);   
        connector.getFilterChain().addLast("mycoder", new ProtocolCodecFilter(new ByteArrayCodecFactory()));
        //connector.getFilterChain().addLast("mycoder", new ProtocolCodecFilter(new MyFactory()));
		connector.setHandler(new cpClientHandler());
        return connector;  
    }  
	
	public IoSession getIOSession(IoConnector connector){  
        ConnectFuture future = connector.connect(new InetSocketAddress(ip, port));   
        future.awaitUninterruptibly();   
        IoSession session = null;  
        try{  
            session = future.getSession();  
        }catch(Exception e){  
            e.printStackTrace();  
        }  
        return session;  
    } 
	
	public void sendMsg(IoSession session,byte[] b){ 
		
		System.out.println("=============");
        session.write(b);  
    } 

	public static void main(String[] args) throws InterruptedException {
		System.out.println("==开始=");
		cpClient client = new cpClient();
		IoConnector ioConnector = client.creatClient();
		IoSession ioSession= client.getIOSession(ioConnector);
		//10
		String str10 = "FA F5 3F 80 59 10 01 02 03 04 05 08 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5D";
		byte[] b10 = CommFunction.hexString2ToBytes(str10);
		client.sendMsg(ioSession, b10);
		TimeUnit.SECONDS.sleep(5);
		String str79 = "FA F5 8A 80 81 79 00 00 00 00 01 36 38 30 36 31 33 31 35 30 30 30 30 30 32 36 35 31 32 30 30 30 30 30 30 30 30 30 30 30 30 35 32 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 32 01 02 03 04 05 06 07 08 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 35 20 20 20 20 20 20 20 20 34 34 00 00 00 09 F5 00 00 00 01 01 00 51 DF 07 04 11 09 20 11 FF DF 07 04 11 09 24 16 FF DF 07 04 11 09 24 18 FF B8 01 00 00 C2 01 00 00 4F";
		byte[] b79 = CommFunction.hexString2ToBytes(str79);
		client.sendMsg(ioSession, b79);
		TimeUnit.SECONDS.sleep(5);
		String str58 = "FA F5 09 80 D4 58 00 00 00 00 D4 00 2C";
		byte[] b58 = CommFunction.hexString2ToBytes(str58);
		client.sendMsg(ioSession, b58);
		TimeUnit.SECONDS.sleep(5);

		client.sendMsg(ioSession, b58);
		TimeUnit.SECONDS.sleep(5);

		client.sendMsg(ioSession, b58);
		
		for (int i = 0; i < 500; i++) {

		}
//		String strc2 = "FA F5 3F 80 59 3F 80 59";
//		byte[] e2 = CommFunction.hexString2ToBytes(strc2);
//		client.sendMsg(ioSession, e2);
//		for (int i = 0; i < 500; i++) {
//
//		}
		//发送0x10报文
//		//String str="FA F5 75 35 59 01 02 03 04 05 06 03 00 DB 07 0C 14 0D 0D 0D 2C 01 1b 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 02 01 80";
//		String str = "FA F5 3F 80 59 10 01 02 03 04 05 06 03 00 31 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 20 35 03 02 02 01 3D 00 00 00 0A 00 00 00 02 00 00 00 DF 07 04 0A 0F 00 0B FF DF 07 04 0A 0F 01 2C FF 5B";
//		byte[] b = CommFunction.hexString2ToBytes(str);
////		//发送0x79报文
//		String str79="FA F5 8A 80 81 79 00 00 00 00 01 36 38 30 36 31 33 31 35 30 30 30 30 30 32 36 35 31 32 30 30 30 30 30 30 30 30 30 30 30 30 35 32 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 30 32 01 02 03 04 05 06 07 08 4C 48 42 31 33 54 33 44 37 43 52 31 33 30 38 33 35 20 20 20 20 20 20 20 20 34 34 00 00 00 09 F5 00 00 00 01 01 00 51 DF 07 04 11 09 20 11 FF DF 07 04 11 09 24 16 FF DF 07 04 11 09 24 18 FF B8 01 00 00 C2 01 00 00 4F";
//		//System.out.println("===" + Arrays.toString(CommFunction.asciiStringToBytes(str)));
//		byte[] c = CommFunction.hexString2ToBytes(str79);
//		client.sendMsg(ioSession, b);	
//		for (int i = 0; i < 500; i++) {
//
//		}
//		client.sendMsg(ioSession, c);	
		
	}
	
}




