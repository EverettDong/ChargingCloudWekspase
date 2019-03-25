package com.cpit.icp.collect.dao;

import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.NONE)
public class RechargeRecordDaoTest {

    @Autowired
    MonRechargeRecordDao monRechargeRecordDao;

/*    @Value("${ds.url}")
    private String URL;*/

    @Test
    public void testAdd(){
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

        //调用MonRechargeRecodeDao的添加方法
        monRechargeRecordDao.addDto(monRechargeRecord);
    }

    @Test
    public void testUpdate(){
        MonRechargeRecordDto monRechargeRecord = new MonRechargeRecordDto();
        monRechargeRecord.setDeviceNo("098765432456789");
        monRechargeRecord.setOperatorId("5273");
        monRechargeRecord.setRechargeType("1");
        monRechargeRecord.setCardId("23456789");
        monRechargeRecord.setChargerCode("5273");
        monRechargeRecord.setVin("5273");
        monRechargeRecord.setPlateNumber("5273");
        monRechargeRecord.setStartSoc("5273");
        monRechargeRecord.setEndSoc("5273");
        monRechargeRecord.setAh("5273");
        monRechargeRecord.setKwh("5273");
        monRechargeRecord.setChargeTime("5273");
        monRechargeRecord.setStrategy("5273");
        monRechargeRecord.setStrategyParam("123456789");
        monRechargeRecord.setNormalEnd("5273");
        monRechargeRecord.setStartTime("20180809");
        monRechargeRecord.setTraceTime("20180807");
        monRechargeRecord.setEndTime("20180907");
        monRechargeRecord.setStartKwh("5273");
        monRechargeRecord.setPlatTransFlowNum("5273");
        monRechargeRecord.setChargeBookNo("5273");
        monRechargeRecord.setSerialNo("5273");
        monRechargeRecord.setChargeSource("5273");

        //调用dao进行更新
        monRechargeRecordDao.addDto(monRechargeRecord);
    }


    @Test
    public void testGet(){
        String deviceNo = "0102030405060708";
        String cardId = "13722354122";
        String startTime = "20180809";
        String platTransFlowNum = "5273";
       // MonRechargeRecordDto byDeviceNo = monRechargeRecordDao.getByDeviceNo(deviceNo,cardId,startTime,platTransFlowNum);
        List<MonRechargeRecordDto> list = monRechargeRecordDao.queryRecent5Record("6806131000022806");
       System.out.println(list.size());
    }
   

 /*   @Test
    public void test() {
        System.out.println("--------------------");
        System.out.println(URL);
    }*/


}
