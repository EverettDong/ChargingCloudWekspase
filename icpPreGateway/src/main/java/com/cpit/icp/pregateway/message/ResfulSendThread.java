package com.cpit.icp.pregateway.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.pregateway.message.dto.MessageDto;
import com.cpit.icp.pregateway.message.impl.MessageMgmt;
import com.cpit.icp.pregateway.util.MessageInfo;

/**
 * 上行报文发送线程
 * @author admin
 *
 */
public class ResfulSendThread implements Runnable {

private final static Logger logger = LoggerFactory.getLogger(ResfulSendThread.class);
//    @Resource
//	private MessageMgmt mgmt;
	
   private MessageDto mpd;
	
	
	
	private MessageMgmt mgmt;
	
	public ResfulSendThread(MessageDto mpd,MessageMgmt mgmt) {
    	this.mpd = mpd;
    	this.mgmt = mgmt;
    }
    @Override
	public void run() {
    	logger.debug("上行报文线程开始" + mpd.getMessage());
    	
		try {
	
			if (MessageInfo.Storage.contains(mpd.getCommand())) {
				//logger.info("上行报文入库 message= " + mpd.getMessage());
				mgmt.addMessage(mpd);
			}

			mgmt.dealMessage(mpd.getDevice_ip(), mpd.getDevice_port(), mpd.getVersion(), mpd.getDevice_no(),
					mpd.getCommand(), mpd.getMessage());

		} catch (Exception e) {
			logger.error("send download message error!", e);
			logger.error("send download message error,message "+ mpd.getMessage());
		}

	}
}
