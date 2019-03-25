package com.cpit.icp.collect.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.GateRouteInfoDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class GateRouteInfoDaoTest {
@Autowired GateRouteInfoDao gateRouteInfoDao;
@Autowired  GateRouteInfoMgmt  gateRouteInfoMgmt;

@Test
public void testAdd(){
	GateRouteInfoDto dto = new GateRouteInfoDto();
	dto.setDeviceNo("0102030405060709");
	dto.setPreGateIp("1.1.1.1");
	dto.setPreGatePort("25001");
	gateRouteInfoDao.addDto(dto);
}

@Test
public void testGet(){
	String deviceNo="0102030405060709";
	GateRouteInfoDto d = gateRouteInfoDao.getByDeviceNo(deviceNo);
	System.out.println(d.toString());
}

@Test
public void testUpdate(){
	GateRouteInfoDto dto = new GateRouteInfoDto();
	dto.setDeviceNo("0102030405060708");
	dto.setPreGateIp("1.1.1.1");
	dto.setPreGatePort("25003");
	gateRouteInfoMgmt.updateGateRouteInfo(dto);
}

@Test
public void testDel(){
	GateRouteInfoDto dto = new GateRouteInfoDto();
	dto.setDeviceNo("0102030405060708");
	gateRouteInfoDao.delDto(dto);
}

@Test
public void testDisConn(){
	gateRouteInfoMgmt.deviceDisConn("0102030405060709");
}}