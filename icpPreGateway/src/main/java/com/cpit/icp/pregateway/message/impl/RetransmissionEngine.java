package com.cpit.icp.pregateway.message.impl;

import java.util.concurrent.TimeUnit;

//import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.cpit.icp.pregateway.message.ReMsgSender;
import com.cpit.icp.pregateway.message.dto.MsgOfPregateDto;
import com.cpit.icp.pregateway.netty.NettyChannelMap;
import com.cpit.icp.pregateway.util.CommFunction;
import com.cpit.icp.pregateway.util.Decode;
import com.cpit.icp.pregateway.util.MessageInfo;

import io.netty.channel.Channel;

/**
 * 0x05,0x6c报文重发
 * @author admin
 *
 */
@Service
public class RetransmissionEngine {
	
	private final static Logger logger = LoggerFactory.getLogger(RetransmissionEngine.class);
	
	
	@Autowired
	private RedisTemplate redisTemplate;
	@Autowired
	private ReMsgSender reMsgSender;
	
	public RetransmissionEngine() {
	}
	
	public void launch(MsgOfPregateDto mpd) {
		String code = mpd.getDeviceNo();
		String msgData = mpd.getMsgData();
		// this.mrRecordDto = (MonRechargeRecordDto) BeanUtils.cloneBean(_mrRecordDto);
		logger.info("send reMsg begin,code=" + code + ",msgData=" + msgData);
		try {
			byte[] data = CommFunction.hexStringToBytes(msgData);
			String command =Decode.getCommand(data) ;
			//用户ID和指令序号 报文匹配用
			String idandSN =Decode.getidandSN(data) ;
			Channel channel = NettyChannelMap.get(code);
			String key =  code + MessageInfo.underline + command + MessageInfo.underline + idandSN;
			//重发处理
			reSendCounter(key, mpd);
			
			if (channel != null && channel.isActive()) {
				channel.writeAndFlush(data);
				logger.info("send reMsg message success,code=" + code + ",msgData=" + msgData);
			} else {
				// 移除失效连接
				NettyChannelMap.removebyId(code);
				logger.warn("send reMsg message failed,channel not exist,code=" + code + ",msgData=" + msgData);
			}

		} catch (Exception e) {
			logger.error("send reMsg message error!", e);
		}
	}
	
	/**
	 * 重发处理
	 * @param key
	 * @param mpd
	 */
	public void reSendCounter(String key, MsgOfPregateDto mpd) {
		try {
			Object obj = redisTemplate.opsForValue().get(key);
			if (obj == null) {
				// 第一次
				if (!MessageInfo.msgReSendMap.containsKey(key)) {
					redisTemplate.opsForValue().set(key, 3, MessageInfo.reSendRedis, TimeUnit.SECONDS);
					MessageInfo.msgReSendMap.put(key, 1);
					reMsgSender.send(mpd);
				} else {
					// 缓存没了 ，map里有说明已经收到回复了，不需要重发了
					MessageInfo.msgReSendMap.remove(key);
				}

			} else {
				// 非第一次，小于指定次数继续放入队列
				Integer num = MessageInfo.msgReSendMap.get(key) + 1;
				if (num <= MessageInfo.reSendNum) {
					MessageInfo.msgReSendMap.put(key, num);
					reMsgSender.send(mpd);
				}

			}
		} catch (Exception e) {
			logger.error("send reSendCounter message error!", e);
		}
	}
	
	

}
