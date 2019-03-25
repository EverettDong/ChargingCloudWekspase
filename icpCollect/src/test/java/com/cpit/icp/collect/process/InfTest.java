package com.cpit.icp.collect.process;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.msgProcess.CallOuterInf;
import com.cpit.icp.collect.msgProcess.ConstructDatagram;
import com.cpit.icp.collect.service.AppChargingProcessImp;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.dto.billing.recharge.RechargeBeginResponse;
import com.cpit.icp.dto.common.ResultInfo;
import com.cpit.icp.dto.crm.User;
import com.cpit.icp.dto.resource.BrBatterycharge;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class InfTest {
@Autowired AppChargingProcessImp appImp;
@Autowired CallOuterInf callOuterInf;
@Autowired ConstructDatagram constructMsg;
@Test
public void testEncode() {
	RechargeBeginResponse accountInfo = new RechargeBeginResponse();
	accountInfo.setCardNumber("123456789");
	accountInfo.setCustomerNumber("123456,京N98M90,potevio");
	accountInfo.setProcessSerialNumber("000000");
	accountInfo.setElectricityMainAccountBalance("100.01");
	accountInfo.setElectricityMainAccountAvailableBalance("100.02");
	accountInfo.setServiceChargeAccountBalance("99.01");
	accountInfo.setServerAccountAvailableBalance("99.02");
	accountInfo.setCenterTransactionSerialNumber("6666");
	accountInfo.setBossDocumentsSerialNumber("7777");
	byte[] b =appImp.getEncodeData6C(accountInfo, "0x6C", "3.4", "", "", "0x20", "11", "1");
	//byte[] b = appImp.stopCharingV34("0102030405060708");
	System.out.println(CodeTransfer.byteArrayToHexStr(b));
}
@Test
public void testUnlock() {
	String s="abx123";
	System.out.println(appImp.unlockChargePole(s));
}

@Test
public void testBeginToStart() {
	String deviceNo ="01020";
	String userId ="1";
	String chargeMode="0x20";
	String planChargeTime="";
	String planChargePower="";
	String password ="dafadfafa";
	appImp.beginToCharge(deviceNo, userId, chargeMode, planChargeTime, planChargePower, password,"");

try {
	Thread.currentThread().sleep(1000);
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
appImp.beginToCharge(deviceNo, userId, chargeMode, planChargeTime, planChargePower, password,"");

}


@Test
public void testOuterInf() {
	String deviceNo="0101010102102206";
	ResultInfo res = getDeviceNoInfo(deviceNo);
	Map<String, Serializable> map=new HashMap<String, Serializable>();
	map= (Map<String, Serializable>)(res.getData());
	List<BrBatterycharge> list = (List<BrBatterycharge>)map.get("infoList");
	if(!list.isEmpty() && list.size()!=0) {
		BrBatterycharge b = (BrBatterycharge)list.get(0);
		System.out.println(b.toString());
		
	}
	System.out.println(res.getResult());
}

public ResultInfo getDeviceNoInfo(String deviceNo) {
	//ResultInfo result =callOuterInf.getDeviceNoInfo(deviceNo);
	return new ResultInfo();
	
}


@Test
public void testTotalBalance() {
	String cardId = "6810131000000180";
	callOuterInf.queryTotalBalance(cardId);
}



}
