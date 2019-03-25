package com.cpit.icp.collect.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.impl.MonOverallStateMgmt;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.MonOverallStateDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class MonOverallStateTest {
	String deviceNo ="0102030405060708";
@Autowired	MonOverallStateMgmt monOverallStateMgmt;
@Test
public void testGet() {

	MonOverallStateDto dto =monOverallStateMgmt.getByDeviceNo(deviceNo);
	System.out.println("ssss");
	System.out.println("moduelNum "+dto.getChargemodNum());
}
@Test
public void testAdd() {
	MonOverallStateDto dto = new MonOverallStateDto();
	dto.setDeviceNo(deviceNo);
	dto.setChargemodNum("10");
	dto.setGunAmount("1");
	dto.setInDate(TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR));
	monOverallStateMgmt.addDto(dto);
}
}
