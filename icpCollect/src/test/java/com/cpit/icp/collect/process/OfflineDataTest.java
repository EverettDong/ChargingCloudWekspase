package com.cpit.icp.collect.process;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.service.MsgRecOfflineData;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class OfflineDataTest {
@Autowired MsgRecOfflineData msgRecOfflineData;


@Test
public void testOffline() {
	String deviceNo = "010203040506070809";
	String msgData ="FAF53F80591001020304050603003120202020202020202020202020202020202035030202013D0000000A00000002000000DF07040A0F000BFFDF07040A0F012CFF5B";
	String preGateIp = "10.3.1.1";
	String preGatePort = "1000";
	String version = "3.4";
	String mesCode = "0x10";
	
	MsgOfPregateDto dto = new MsgOfPregateDto(deviceNo,mesCode,msgData,version,preGateIp,preGatePort);
	
	
	msgRecOfflineData.receivedOfflineData(dto);
}
}
