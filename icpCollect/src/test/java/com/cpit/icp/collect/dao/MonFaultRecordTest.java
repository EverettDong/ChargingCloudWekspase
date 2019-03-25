package com.cpit.icp.collect.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.MonFaultRecordDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class MonFaultRecordTest {
@Autowired MonFaultRecordDao dao;
@Test
public void testAdd() {
	MonFaultRecordDto dto = new MonFaultRecordDto();
	dto.setDeviceNo("0102030405060708");
	dto.setDeviceVersion("3.5");
	dto.setFaultData("01000000");
	dto.setFaultDef("100001");
	dto.setFaultLevel("1");
	dto.setFaultSrcMsgCode("0x31");
	dto.setParsedFaultData("实体：一级-整机系统	二级-内部过温；故障分类：风扇故障  ");
	dao.addDto(dto);
}
}
