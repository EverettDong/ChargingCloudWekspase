package com.cpit.icp.pregateway.message.controller;

import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cpit.icp.pregateway.message.MsgThreadPool;
import com.cpit.icp.pregateway.message.MySendThread;
import com.cpit.icp.pregateway.message.dto.MsgOfPregateDto;
import com.cpit.icp.pregateway.message.dto.ResultInfo;
import com.cpit.icp.pregateway.netty.NettyChannelMap;
import com.cpit.icp.pregateway.util.JsonUtil;
import com.cpit.icp.pregateway.util.MessageInfo;

import io.netty.channel.Channel;


/**
 * 接收业务网关下行消息
 * @author admin
 *
 */
@RestController
public class MessageReceiceController {
	
	private final static Logger logger = LoggerFactory.getLogger(MessageReceiceController.class);
    
	/**
	 * 接收业务网关的下行报文
	 * @param JsonStr
	 */
	@RequestMapping(value = "/receiveDownMessage")
	public void receiveDownMessage(@RequestBody String jsonStr){
		logger.info("receiveDownMessage jsonStr:" +jsonStr);
		ResultInfo resultInfo =new ResultInfo(ResultInfo.OK);		
		
		try{
			MsgOfPregateDto mpd = (MsgOfPregateDto)JsonUtil.jsonToBean(jsonStr, MsgOfPregateDto.class);
			ExecutorService msgProThreadPool = MsgThreadPool.getThreadPool();
			msgProThreadPool.execute(new MySendThread(mpd) );
		}catch(Exception e){
			logger.error("receiveDownMessage error!", e);
			resultInfo.setResult(ResultInfo.FAIL);
		}

	
	}
	
//	@RequestMapping(value = "/receiveDownMessage")
//	public ResultInfo receiveDownMessage(@RequestBody String jsonStr){
//		logger.info("receiveDownMessage jsonStr:" +jsonStr);
//		ResultInfo resultInfo =new ResultInfo(ResultInfo.OK);
//		try{
//			MsgOfPregateDto mpd = (MsgOfPregateDto)JsonUtil.jsonToBean(jsonStr, MsgOfPregateDto.class);
//			ExecutorService msgProThreadPool = MsgThreadPool.getThreadPool();
//			msgProThreadPool.execute(new MySendThread(mpd) );
//		}catch(Exception e){
//			logger.error("receiveDownMessage error!", e);
//			resultInfo.setResult(ResultInfo.FAIL);
//		}
//		return resultInfo;
//	
//	}
	
	/**
	 * 剔除非法设备接口
	 * @param JsonStr
	 */

//	@RequestMapping("/rejectErrorDevice")
//	public ResultInfo rejectErrorDevice(String deviceNo){		
//		logger.info("rejectErrorDevice deviceNo:" +deviceNo);
//		System.out.println("rejectErrorDevice deviceNo:" +deviceNo);
//		ResultInfo resultInfo =new ResultInfo(ResultInfo.OK);
//		
//		if (!MessageInfo.errorCodeList.contains(deviceNo))
//			MessageInfo.errorCodeList.add(deviceNo);
//		try{
//			//剔除非法接入的设备 
//			Channel channel = NettyChannelMap.get(deviceNo);
//			NettyChannelMap.removebyId(deviceNo);
//			if (channel != null && channel.isActive()) {
//				
//				channel.close();
//				logger.info("close reject device channel,deviceNo=" + deviceNo);
//			}
//	
//		}catch(Exception e){
//			logger.error("rejectErrorDevice error!", e);
//			resultInfo.setResult(ResultInfo.FAIL);
//		}
//		return resultInfo;
//	}
	@RequestMapping("/rejectErrorDevice")
	public void rejectErrorDevice(@RequestBody String deviceNo){		
		logger.info("rejectErrorDevice deviceNo:" +deviceNo);

	  //不需要维持异常设备列表
//		if (!MessageInfo.errorCodeList.contains(deviceNo))
//			MessageInfo.errorCodeList.add(deviceNo);
		try{
			//剔除非法接入的设备 
			Channel channel = NettyChannelMap.get(deviceNo);
			NettyChannelMap.removebyId(deviceNo);
			if (channel != null && channel.isActive()) {
				
				channel.close();
				logger.info("close reject device channel,deviceNo=" + deviceNo);
			}
	
		}catch(Exception e){
			logger.error("rejectErrorDevice error!", e);
		}
	}

}
