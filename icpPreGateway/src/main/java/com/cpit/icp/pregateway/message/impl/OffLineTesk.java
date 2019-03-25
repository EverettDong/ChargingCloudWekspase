package com.cpit.icp.pregateway.message.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.cpit.icp.pregateway.message.dto.DeviceOfflineMessage;
import com.cpit.icp.pregateway.message.dto.MsgOfPregateDto;
import com.cpit.icp.pregateway.util.DateUtil;
import com.cpit.icp.pregateway.util.FtpUtil;
import com.cpit.icp.pregateway.util.MessageInfo;
import com.cpit.icp.pregateway.util.ResfulUtil;

@Service
@Lazy(false)
public class OffLineTesk {
	
	private final static Logger logger = LoggerFactory.getLogger(OffLineTesk.class);
	
    @Autowired
    private DeviceOffMessMgmt deviceOffMessMgmt;

	
	// ******秒分时日月年，0 0/5表示在每个0分5分执行
		@Scheduled(cron = "0 0/5 * * * ?")
		public void executOffLineFileUpload() {
			logger.debug("executOffLineFileUpload execute:" + new Date());
			List<DeviceOfflineMessage> offlineMsg =deviceOffMessMgmt.selectByCon(FtpUtil.pregateIp);
			
			if(null != offlineMsg && offlineMsg.size() >0) {
				String command=MessageInfo.msg_0x79;
				for(DeviceOfflineMessage oMsg:offlineMsg) {
					if(!oMsg.getVersion().contains("80")) {
						command=MessageInfo.msg_0x7D;
					}
					MsgOfPregateDto msgdto = new MsgOfPregateDto(oMsg.getDevice_no(),command, oMsg.getVersion(), ResfulUtil.resfulIp,
							ResfulUtil.resfulPort, oMsg.getMessage());
					
					String fileName = oMsg.getVersion() + "_" + oMsg.getDevice_no() + "_" + DateUtil.getStrDate(oMsg.getP_time()) + ".txt";
					String jsonStr = JSON.toJSONString(msgdto);
					logger.info("write gate offline file=" + fileName);
					if(FtpUtil.writeFile(jsonStr, fileName)) {
						deviceOffMessMgmt.updateDeviceOfflineMessageById(oMsg.getId());
					}
				}
			}

		}
}
