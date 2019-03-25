package com.cpit.icp.pregateway.message;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cpit.icp.pregateway.util.CommFunction;
import com.cpit.icp.pregateway.util.DateUtil;
import com.cpit.icp.pregateway.util.Decode;
import com.cpit.icp.pregateway.util.MessageInfo;

/**
 * 报文过滤
 * @author admin
 *
 */
@Service
public class MsgFilter {
	
	private final static Logger logger = LoggerFactory.getLogger(MsgFilter.class);
	//过滤报文列表
	public static List<String> filterList = new ArrayList<String>();
	//零交易流水号
	public static String trans0="000000000000000000000000000000";
	
	public static String msg_0x17="0x17";
	public static String msg_0x18="0x18";
	public static String msg_0x79="0x79";
	public static String msg_0x39="0x39";
	public static String msg_0x32="0x32";
	public static String msg_0x33="0x33";
	public static String msg_0x3A="0x3A";
	public static String msg_0x3B="0x3B";
	
	public static String msg_0x1E="0x1E";
	public static String msg_0x1F="0x1F";
	
	public static String msg_switch="on";
	
	
	// 3.4版本0x17报文过滤开关  时间间隔（秒）
	public static String msg_34_0x17_switch;
	public static int msg_34_0x17_interval;
	
	// 3.4版本0x18报文过滤开关 时间间隔（秒）
	public static String msg_34_0x18_switch;
	public static int msg_34_0x18_interval;
	
	// 3.4版本0x39报文过滤开关  时间间隔（秒）
	public static String msg_34_0x39_switch;
	public static int msg_34_0x39_interval;
	
	// 3.4版本0x32报文过滤开关  时间间隔（秒）
	public static String msg_34_0x32_switch;
	public static int msg_34_0x32_interval;
		
	// 3.4版本0x33报文过滤开关  时间间隔（秒）
	public static String msg_34_0x33_switch;
	public static int msg_34_0x33_interval;
	
	// 3.4版本0x3A报文过滤开关  时间间隔（秒）
	public static String msg_34_0x3A_switch;
	public static int msg_34_0x3A_interval;
	
	// 3.4版本0x3B报文过滤开关 时间间隔（秒）
	public static String msg_34_0x3B_switch;
	public static int msg_34_0x3B_interval;
	
	// 3.5版本0x17报文过滤开关 时间间隔（秒）
	public static String msg_35_0x1E_switch;
	public static int msg_35_0x1E_interval;

	// 3.5版本0x18报文过滤开关 时间间隔（秒）
	public static String msg_35_0x1F_switch;
	public static int msg_35_0x1F_interval;

	// 3.5版本0x39报文过滤开关 时间间隔（秒）
	public static String msg_35_0x39_switch;
	public static int msg_35_0x39_interval;

	// 3.5版本0x3A报文过滤开关 时间间隔（秒）
	public static String msg_35_0x3A_switch;
	public static int msg_35_0x3A_interval;

	// 3.5版本0x3B报文过滤开关 时间间隔（秒）
	public static String msg_35_0x3B_switch;
	public static int msg_35_0x3B_interval;
		
	//@Autowired
	//private RedisTemplate redisTemplate;
	/**
	 * 报文过滤，如果缓存里有 则返回true，过滤此条报文	
	 * 指定消息按时间间隔过滤，配置信息见application.properties
	 * @param redisTemplate
	 * @param code
	 * @param command
	 * @param version
	 * @param Receive
	 * @return
	 */
	public  static boolean DecodePackageStyle(RedisTemplate redisTemplate,String code,String command,Byte version, byte[] Receive) {
		Object obj;
		String key =code +  CommFunction.byteToHexStr2(version) + command;
		boolean flag =false;
		switch (version) {
		case (byte) 0x80:
			obj = redisTemplate.opsForValue().get(key);
			if (command.equals(msg_0x17) && msg_34_0x17_switch.equals(msg_switch)) {									
				if(obj == null) 
					redisTemplate.opsForValue().set(key, 1, msg_34_0x17_interval, TimeUnit.SECONDS);				
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x18) && msg_34_0x18_switch.equals(msg_switch)) {					
				if(obj == null)
					redisTemplate.opsForValue().set(key, 1, msg_34_0x18_interval, TimeUnit.SECONDS);
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x39) && msg_34_0x39_switch.equals(msg_switch)) {	
				if(obj == null) 
					redisTemplate.opsForValue().set(key, 2, msg_34_0x39_interval, TimeUnit.SECONDS);								
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x32) && msg_34_0x32_switch.equals(msg_switch)) {	
				if(obj == null) 
					redisTemplate.opsForValue().set(key, 2, msg_34_0x32_interval, TimeUnit.SECONDS);								
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x33) && msg_34_0x33_switch.equals(msg_switch)) {	
				if(obj == null) 
					redisTemplate.opsForValue().set(key, 2, msg_34_0x33_interval, TimeUnit.SECONDS);								
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x3A) && msg_34_0x3A_switch.equals(msg_switch)) {					
				if(obj == null)
					redisTemplate.opsForValue().set(key, 1, msg_34_0x3A_interval, TimeUnit.SECONDS);
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x3B) && msg_34_0x3B_switch.equals(msg_switch)) {					
				if(obj == null)
					redisTemplate.opsForValue().set(key, 1, msg_34_0x3B_interval, TimeUnit.SECONDS);
				else 
					return true;
				break;
				}
			//79特殊处理 中心交易流水号重复的过滤
			if (command.equals(msg_0x79)) {
				String transactionNUm = Decode.getTransactionFlowing(Receive);
				//交易流水号为0过滤
				if (trans0.equals(transactionNUm)) {
						return true;
				 }
			   }
			break;
		case (byte) 0x35:
			obj = redisTemplate.opsForValue().get(key);
			if (command.equals(msg_0x1E) && msg_35_0x1E_switch.equals(msg_switch)) {									
				if(obj == null) 
					redisTemplate.opsForValue().set(key, 1, msg_35_0x1E_interval, TimeUnit.SECONDS);				
				else 
					return true;	
				break;
				}
			if (command.equals(msg_0x1F) && msg_35_0x1F_switch.equals(msg_switch)) {									
				if(obj == null) 
					redisTemplate.opsForValue().set(key, 1, msg_35_0x1F_interval, TimeUnit.SECONDS);				
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x39) && msg_35_0x39_switch.equals(msg_switch)) {					
				if(obj == null)
					redisTemplate.opsForValue().set(key, 1, msg_35_0x39_interval, TimeUnit.SECONDS);
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x3A) && msg_35_0x3A_switch.equals(msg_switch)) {					
				if(obj == null)
					redisTemplate.opsForValue().set(key, 1, msg_35_0x3A_interval, TimeUnit.SECONDS);
				else 
					return true;
				break;
				}
			if (command.equals(msg_0x3B) && msg_35_0x3B_switch.equals(msg_switch)) {					
				if(obj == null)
					redisTemplate.opsForValue().set(key, 1, msg_35_0x3B_interval, TimeUnit.SECONDS);
				else 
					return true;
				break;
				}
			break;
	  }
		return false;
	}
	
	/**
	 * 报文过滤，过滤流程超标设备的指定报文
	 * 返回true时过滤报文
	 * @param redisTemplate
	 * @param code
	 * @param key
	 * @param command
	 * @param data
	 * @return
	 */
	public  static boolean countMsgFlow(RedisTemplate redisTemplate,String code,String key,String command, byte[] data) {
		Object obj;
		//当日使用流量
		int flow;
		//当日流量阀值
		int iflow;
		try {
			// 流量控制 正式环境应打开
			if (MessageInfo.ipcode.containsKey(key)) {
				obj = redisTemplate.opsForValue().get(code);
				if(obj == null) {
					flow=data.length;
				}else{					
					flow=(int) obj + data.length;
					}
				redisTemplate.opsForValue().set(code, flow ,
						DateUtil.getSecsToEndOfCurrentDay(), TimeUnit.SECONDS);
				// iflow = fmgmt.getFlowByCode(code);
				iflow = MessageInfo.deviceMsgThreshold;
				logger.debug(code + "当日流量使用：" + flow + ",当日流量阀值：" + iflow + "m");
				if (iflow != 0) {
					if (flow > iflow * 1024 * 1024) {
						//流量超出
						logger.warn(code + "Flow over！！！");
						// 流量超出只发5.4节状态报文 和心跳签到报文
						if (!MessageInfo.StatesMsg.contains(command)) {
							return true;
						}

					}
				}
			}
		} catch (Exception e) {
			logger.error("countMsgFlow error:" + e);
		}
		return false;
	}
	
	/**
	 * 取3.5报文多枪号，27是枪数量，28是第一枪，len是枪号间隔
	 * 适合0x1E,0x39,0x3A,0x3B报文
	 * @param len
	 * @param Receive
	 * @return
	 */
	public  static List<String> getMultiChargingGunNumber(int len,byte[] Receive) {
		int num= CommFunction.unsignByteToInt(Receive[27]);
		List<String> gunNumberList = new ArrayList<String>();
		String gNum;
		for(int i = 0; i < num; i++) {
			gNum= CommFunction.byteToBit(Receive[28 + i*len]).substring(2,6);
			gunNumberList.add(gNum);
		}
		return gunNumberList;
	}
}
