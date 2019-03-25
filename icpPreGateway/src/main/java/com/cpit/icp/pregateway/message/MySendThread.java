package com.cpit.icp.pregateway.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.pregateway.message.dto.MsgOfPregateDto;
import com.cpit.icp.pregateway.netty.NettyChannelMap;
import com.cpit.icp.pregateway.util.CommFunction;

import io.netty.channel.Channel;
/**
 * 下行报文发送线程
 * @author admin
 *
 */
public class MySendThread implements Runnable {
	
	private final static Logger logger = LoggerFactory.getLogger(MySendThread.class);
	
	private MsgOfPregateDto mpd;
	
	public MySendThread(MsgOfPregateDto mpd) {
    	this.mpd = mpd;
    }
    @Override
    public void run() {
    	
    	String code =mpd.getDeviceNo();
    	String msgData =mpd.getMsgData();
    	logger.debug("send thread begin,code=" + code +",msgData=" + msgData);
    	//String msgVersion = mpd.getMsgVersion();
        //不需要处理设备编码了 
    	// if(msgVersion.equals(MessageInfo.msg_version34))
        //   msgData=msgData.substring(code.length(), msgData.length());
    	
		try {
			byte[] data = CommFunction.hexStringToBytes(msgData);
			Channel channel = NettyChannelMap.get(code);			
			if (channel != null && channel.isActive()) {
				channel.writeAndFlush(data);
				logger.info("send download message success,code=" + code +",msgData=" + msgData);
			}else {
				//移除失效连接
				NettyChannelMap.removebyId(code);
				logger.warn("send download message failed,channel not exist,code=" + code +",msgData=" + msgData);
			}

		} catch (Exception e) {
			logger.error("send download message error!"+",msgData=" + msgData, e);
		}
    	

    }



}
