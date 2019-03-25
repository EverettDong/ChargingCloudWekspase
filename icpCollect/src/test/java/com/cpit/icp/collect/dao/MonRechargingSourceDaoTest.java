package com.cpit.icp.collect.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.MonRechargingSourceDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class MonRechargingSourceDaoTest {
@Autowired MonRechargingSourceDao dao;

@Test
public void testAdd(){
	MonRechargingSourceDto dto = new MonRechargingSourceDto();
	dto.setDeviceNo("0102030405060708");
	dto.setChargeSource(1);
	
	dao.addDto(dto);
}


@Test
public void testGet(){
	MonRechargingSourceDto dto = dao.getByDeviceno("0102030405060708");
	System.out.println(dto.toString());
}

@Test
public void testUp(){
	MonRechargingSourceDto dto = new MonRechargingSourceDto();
	dto.setDeviceNo("0102030405060708");
	dto.setChargeSource(3);
	
	dao.updateDto(dto);
}
}
