package com.cpit.icp.collect.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.OriginalDataStoreDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.DEFINED_PORT)
public class MonDBServiceTest {
@Autowired MonDBService monDBService;

@Test
public void testGetOriginalNum() {
	String start="2018-10-22 00:00:00";
	String end="2018-10-22 00:10:00";
	int count =monDBService.getOriginalDataNums(start,end);
	System.out.println(count);
}
@Test
public void testGetOriginalData() {
	String start="2018-10-22 00:00:00";
	String end="2018-10-22 00:10:00";
	monDBService.getAndInsert(start, end);
}
@Test
public void testGetDayTime() {
	monDBService.getDayDate("2018-10-22 17:00:00");
}
@Test
public void testDataTransfer() {
	String time = "2018-10-23 03:00:00";
	monDBService.originalDataTransfer(time);
}

@Test
public void testGetDay() {
	System.out.println(getDayByIntime("2018-02-28 00:00:00"));
}
private int getDayByIntime(String intime) {
	Calendar cal=Calendar.getInstance();
	Date d = TimeConvertor.stringTime2Date(intime, TimeConvertor.FORMAT_MINUS_24HOUR);
	cal.setTime(d);
	return cal.get(Calendar.DAY_OF_MONTH);
}
@Test
public void testMonRechargeRecordMgmt() {
	List<String> deviceNoList = new ArrayList<String>();
	deviceNoList.add("0102030405060708");
	deviceNoList.add("0101");
	String startT  ="2015-1-1";
	String endT ="2015-4-18";
	List<MonRechargeRecordDto> list = monDBService.getSumKwhByDeviceNo(deviceNoList,startT,endT);
	System.out.println(list.isEmpty());
	System.out.println(list==null);
	System.out.println(list.size());
	MonRechargeRecordDto dto = (MonRechargeRecordDto)list.get(0);
	System.out.println(dto==null);

}

}
