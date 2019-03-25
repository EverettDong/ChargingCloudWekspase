package com.cpit.icp.pregateway.netty;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
/**
 * 长连接路由
 * @author admin
 *
 */
public class NettyChannelMap {
	//存netty连接
	private static Map<String,Channel> map=new ConcurrentHashMap<String, Channel>();
   
	public static void add(String clientId,Channel socketChannel){
        map.put(clientId,socketChannel);
    }
    
    public static Channel get(String clientId){
       return map.get(clientId);
    }
    
    public static void removebyId(String clientId){
        if(map.containsKey(clientId))
                map.remove(clientId);
                  
    }
    
    public static void removebyChannel(Channel socketChannel){
        for (Map.Entry entry:map.entrySet()){
            if (entry.getValue()==socketChannel){
                map.remove(entry.getKey());
            }
        }
    }

}
