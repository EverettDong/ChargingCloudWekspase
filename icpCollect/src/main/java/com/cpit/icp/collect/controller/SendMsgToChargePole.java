package com.cpit.icp.collect.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import com.cpit.icp.collect.msgProcess.ConstructDatagram;
import com.cpit.icp.collect.msgProcess.ConstructMsgUtil;
import com.cpit.icp.collect.service.UnionChargeProcessImp;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.collect.uiSend.UiSendDto;

@RestController
@RequestMapping(method={RequestMethod.POST})
public class SendMsgToChargePole {
	
	@Autowired UnionChargeProcessImp unionChargeProcessImp;
	private final static Logger logger = LoggerFactory.getLogger(SendMsgToChargePole.class);
	@RequestMapping(value = "/sendMsgToCp")
public void sendMsgToCp(@RequestBody UiSendDto uiSendDto) {
	logger.info("sendMsgToCp");
	List<String> deviceNoList  = uiSendDto.getDeviceNoList();
	String msgCode = uiSendDto.getMsgCode();
	List<String> values = uiSendDto.getMsgValues();
	String sendTime= uiSendDto.getUiSendTime();
	if(deviceNoList.size()==0 || deviceNoList.isEmpty()) {
		logger.error("uiSendDto deviceNoList is empty,send no msg.");
		return;
	}
	if(msgCode==null || msgCode.equalsIgnoreCase("")) {
		logger.error("uiSendDto msgCode is empty,send no msg.");
		return ;
	}
	if(values.isEmpty()||values.size()==0  ) {
		logger.error("uiSendDto values is empty,send no msg.");
		return ;
	}
	if(sendTime==null || sendTime.equalsIgnoreCase("")) {
		logger.error("uiSendDto sendTime is empty,send no msg.");
		return ;
	}
		unionChargeProcessImp.UIMsgProcess(deviceNoList, msgCode, values,sendTime);
	
}

}
