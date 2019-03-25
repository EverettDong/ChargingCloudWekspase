package com.cpit.icp.pregateway.message.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cpit.icp.pregateway.message.dao.MessageDao;
import com.cpit.icp.pregateway.message.dto.DeviceOfflineMessage;
import com.cpit.icp.pregateway.message.dto.MessageDto;
import com.cpit.icp.pregateway.message.dto.MsgOfPregateDto;
import com.cpit.icp.pregateway.util.MessageInfo;
import com.cpit.icp.pregateway.util.ResfulUtil;

@Service
public class MessageMgmt {

	private final static Logger logger = LoggerFactory.getLogger(MessageMgmt.class);

//	@Value("${resful_url}")
//	private String resfulUrl;

	@Autowired
	private MessageDao messageDao;


	public void addMessage(MessageDto dto) {
		 messageDao.addMessage(dto);
	}

	public List<MessageDto> getAllmessage() {
		return messageDao.getAllmessage();
	}

	/**
	 * 
	 * dealMessage(发消息,持久化79、7D报文)
	 * 
	 * @param inetAddress
	 * @param version
	 * @param code
	 * @param command
	 * @param data
	 *            void
	 * @exception @since
	 *                1.0.0
	 */
	public void dealMessage(String ip,int port, String version, String code, String command, String  msgData) {
		
		logger.debug("dealMessage code:" +code +",msgData="+ msgData);
		int stste = 1;

		MsgOfPregateDto msgdto = new MsgOfPregateDto(code,command, version, ResfulUtil.resfulIp,
				ResfulUtil.resfulPort, msgData);

		String jsonStr = JSON.toJSONString(msgdto);
		boolean sendflag = sendUpMessage(command, code, jsonStr);
		Date date = new Date();
		if (!sendflag) {
			stste = 0;

		}
		if (command.equals(MessageInfo.msg_0x79)||command.equals(MessageInfo.msg_0x7D)||
				command.equals(MessageInfo.msg_0x73)||command.equals(MessageInfo.msg_0x78)) {
			logger.info("入库充电报文=" + msgData);
			DeviceOfflineMessage dom = new DeviceOfflineMessage(version, MessageInfo.type1, code,
					ip, String.valueOf(port), msgData, stste, date);
			try {
				messageDao.insertDeviceOfflineMessage(dom);
			} catch (Exception ex) {
				logger.error("error in insertDeviceOfflineMessage,code = " + code + ",msgData = " + msgData, ex);
			}
		}
	}
 
	
	/**
	 * 发送上行消息到业务网关
	 * @param command
	 * @param code
	 * @param jsonStr
	 * @return
	 */
	public boolean sendUpMessage(String command, String code, String jsonStr) {
		logger.info("sendUpMessage开始====" + jsonStr);
		try {
			String url = ResfulUtil.resfulUrl + "/msgRecievedPreGate";
			//String url = resfulUrl + "/receiveDownMessage";
//			ResultInfo reStr = (ResultInfo)ResfulUtil.doPost(url, String.class, jsonStr);
//			if (ResultInfo.FAIL == reStr.getResult()) {
//				return false;
//			}
			ResfulUtil.doPost(url, String.class, jsonStr);
			return true;
		} catch (Exception ex) {
			logger.error("error in sendUpMessage,code = " + code + ",command = " + command);
			logger.error("error in sendUpMessage", ex);
			
		}
		return false;
	}
	
	
	/**
	 * 发送断连设备code给业务网关
	 * @param command
	 * @param code
	 * @return
	 */
	public boolean sendLostConnectMessage(String code) {

		try {
			String url = ResfulUtil.resfulUrl + "/reportDisConnection";
//			ResultInfo reStr = (ResultInfo) ResfulUtil.doPost(url, String.class, code);
//			if (ResultInfo.FAIL == reStr.getResult()) {
//				return false;
//			}
			ResfulUtil.doPost(url, String.class, code);
			return true;
		} catch (Exception ex) {
			logger.error("error in sendLostConnectMessage,code = " + code);
			logger.error("error in sendLostConnectMessage", ex);
			
		}
		return false;
	}
	
//	/**
//	 * 
//	 * @param dto
//	 */
//	public void dealUploadMessage(MessageDto dto，MessageMgmt mgmt) {
//		
//		try{
//			ExecutorService resfulThreadPool = ResfulThreadPool.getThreadPool();
//			resfulThreadPool.execute(new ResfulSendThread(dto,mgmt) );
//		}catch(Exception e){
//			logger.error("dealMessage error!", e);
//		}
//		
//	}

}
