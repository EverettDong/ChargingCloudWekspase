package com.cpit.icp.collect.dao;

import com.cpit.icp.collect.impl.MonRechargingInfoMgmt;
import com.cpit.icp.collect.main.Application;

import com.cpit.icp.dto.collect.MonRechargingInfoDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName MonRecharingInfoTest
 * @Description TODO
 * @Author donghaibo
 * @Date 2018/9/11 18:49
 * @Version 1.0.0
 * @Copyright (c) 2018.09.11普天信息技术有限公司-版权所有
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MonRecharingInfoTest {

    @Autowired
    MonRechargingInfoMgmt monRechargingInfoImp;

    @Test
    public void addTest(){
        MonRechargingInfoDto monRechargingInfoDto = new MonRechargingInfoDto();
        monRechargingInfoDto.setDeviceNo("5237");
        monRechargingInfoDto.setOperatorId("5237");
        monRechargingInfoDto.setPosition("5237");
        monRechargingInfoDto.setRechargeType("直流");
        monRechargingInfoDto.setKwh("5000");
        monRechargingInfoDto.setAh("40000");
        monRechargingInfoDto.setRechargeTime("20180910");
        monRechargingInfoDto.setStartSoc("5273");
        monRechargingInfoDto.setCurrentKwh("6000");
        monRechargingInfoDto.setCurrentSoc("7000");
        monRechargingInfoDto.setInDate("20180910151941");

        boolean b = monRechargingInfoImp.addDto(monRechargingInfoDto);
        System.out.println(b);
    }

    @Test
    public void getTest(){
        String deviceNo = "5237";
        String position = "5237";
        String inDate = "20180910151941";
        MonRechargingInfoDto byDeviceNoAP = monRechargingInfoImp.getByDeviceNoAP(deviceNo, position, inDate);

        System.out.println(byDeviceNoAP);
    }
}
