package com.cpit.icp.collect.dao;

import com.cpit.icp.collect.rabbitConfig.RabbitMsgSender;
import com.cpit.icp.collect.RabbitMqTest.HelloSender;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName RabbitMQTest
 * @Description TODO
 * @Author ptne-imc
 * @Date 2018/9/6 13:49
 * @Version 1.0.0
 * @Copyright (c) 2018.09.06普天信息技术有限公司-版权所有
 **/

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.NONE)
public class RabbitMQTest {

    @Autowired
    private RabbitMsgSender rabbitMsgSender;

    @Autowired
    private HelloSender helloSender;

    @Test
    public void rabbitTest() {
        MonRechargeRecordDto monRechargeRecord = new MonRechargeRecordDto();
        monRechargeRecord.setDeviceNo("0102030405060708");
        monRechargeRecord.setOperatorId("5273");
        monRechargeRecord.setRechargeType("1");
        monRechargeRecord.setCardId("13722354122");
        monRechargeRecord.setChargerCode("5273");
        monRechargeRecord.setVin("5273");
        monRechargeRecord.setPlateNumber("5273");
        monRechargeRecord.setStartSoc("5273");
        monRechargeRecord.setEndSoc("5273");
        monRechargeRecord.setAh("5273");
        monRechargeRecord.setKwh("5273");
        monRechargeRecord.setChargeTime("5273");
        monRechargeRecord.setStrategy("5273");
        monRechargeRecord.setStrategyParam("5273");
        monRechargeRecord.setNormalEnd("5273");
        monRechargeRecord.setStartTime("20180809");
        monRechargeRecord.setTraceTime("20180807");
        monRechargeRecord.setEndTime("20180907");
        monRechargeRecord.setStartKwh("5273");
        monRechargeRecord.setPlatTransFlowNum("5273");
        monRechargeRecord.setChargeBookNo("5273");
        monRechargeRecord.setSerialNo("5273");
        monRechargeRecord.setChargeSource("5273");
        rabbitMsgSender.send(monRechargeRecord);
    }

    @Test
    public void hello(){
        helloSender.send();
    }
}
