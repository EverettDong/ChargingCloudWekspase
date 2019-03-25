package com.cpit.icp.collect.rabbitConfig;

import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.uiSend.UISendMsgResDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName HelloSender
 * @Description 发
 * @Author donghaibo
 * @Date 2018/10/23 14:12
 * @Version 1.0.0
 * @Copyright (c) 2018.10.23普天信息技术有限公司-版权所有
 **/
@Component
public class RabbitMsgSender {

    private final static Logger logger = LoggerFactory.getLogger(RabbitMsgSender.class);

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void send(MonRechargeRecordDto monRechargeRecord) {
        logger.info("Sender monRechargeRecord :"+monRechargeRecord.toString());
        try {
            amqpTemplate.convertAndSend("dealMsg",monRechargeRecord);
        } catch (AmqpException e) {
            logger.error("RabbitMQ abnormal:"+e);
        }
    }

    public void sendUIMsgRes(UISendMsgResDto uiSendMsgResDto) {
        logger.info("Sender uiSendMsgResDto :"+uiSendMsgResDto.toString());
        try {
            amqpTemplate.convertAndSend("bill_ui_msg",uiSendMsgResDto);
        } catch (AmqpException e) {
            logger.error("sendUIMsgRes:"+e);
        }
    }
    
    
    
}
