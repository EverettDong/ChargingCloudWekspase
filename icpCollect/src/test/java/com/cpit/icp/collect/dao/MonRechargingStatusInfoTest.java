package com.cpit.icp.collect.dao;

import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.MonRechargingStatusInfoDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.NONE)
public class MonRechargingStatusInfoTest {

    @Autowired
    MonRechargingStatusInfoDao monRechargingStatusInfoDao;

    @Test
    public void TestAdd(){
        MonRechargingStatusInfoDto monRechargingStatusInfoDto = new MonRechargingStatusInfoDto();
        monRechargingStatusInfoDto.setDeviceNo("122124343");
        monRechargingStatusInfoDto.setOperatorId("5273");
        monRechargingStatusInfoDto.setSerialNo("5273");
        monRechargingStatusInfoDto.setGunAmount("5273");
        monRechargingStatusInfoDto.setPosition("-3");
        monRechargingStatusInfoDto.setRechargeType("donghaibo");
        monRechargingStatusInfoDto.setModuleId("5273");
        monRechargingStatusInfoDto.setBmsCode("5273");
        monRechargingStatusInfoDto.setVol("5273");
        monRechargingStatusInfoDto.setTem("5273");
        monRechargingStatusInfoDto.setOutputPower("adsdasca");
        monRechargingStatusInfoDto.setErrorCode("5273");
        monRechargingStatusInfoDto.setChargerStatus("04");
        monRechargingStatusInfoDto.setInDate("20180710");
        monRechargingStatusInfoDto.setExeOrganStatus("03");

        monRechargingStatusInfoDao.addDto(monRechargingStatusInfoDto);
    }

    @Test
    public void TestUpdate(){
        MonRechargingStatusInfoDto monRechargingStatusInfoDto = new MonRechargingStatusInfoDto();
        monRechargingStatusInfoDto.setDeviceNo("1234567787654");
        monRechargingStatusInfoDto.setOperatorId("5273");
        monRechargingStatusInfoDto.setSerialNo("5273");
        monRechargingStatusInfoDto.setGunAmount("5678");
        monRechargingStatusInfoDto.setPosition("-1");
        monRechargingStatusInfoDto.setRechargeType("donghaibo");
        monRechargingStatusInfoDto.setModuleId("5273");
        monRechargingStatusInfoDto.setBmsCode("5273");
        monRechargingStatusInfoDto.setVol("5273");
        monRechargingStatusInfoDto.setTem("5273");
        monRechargingStatusInfoDto.setOutputPower("wueoiwque");
        monRechargingStatusInfoDto.setErrorCode("45678");
        monRechargingStatusInfoDto.setChargerStatus("02");
        monRechargingStatusInfoDto.setInDate("20180706");
        monRechargingStatusInfoDto.setExeOrganStatus("01");

        monRechargingStatusInfoDao.updateDto(monRechargingStatusInfoDto);
    }

    @Test
    public void TestGet(){
        String deviceNo = "1234567890";
        String position = "-1";
        String bmsCode = monRechargingStatusInfoDao.getBmsCodeByDeviceNoAP(deviceNo,position);
        System.out.print(bmsCode);
    }

    @Test
    public void TestGetMonRechargingStatusInfo(){
        String deviceNo = "1234567890";
        String position = "-1";
        String inDate ="2018/9/11";
        MonRechargingStatusInfoDto byDeviceNoAP = monRechargingStatusInfoDao.getByDeviceNoAP(deviceNo,position,inDate);
       System.out.print(byDeviceNoAP.toString());
    }
}
