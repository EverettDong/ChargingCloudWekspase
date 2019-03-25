package com.cpit.icp.collect.dao;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.dto.collect.OriginalDataStoreDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class OriginalDataStoreDaoTest {
@Autowired OriginalDataStoreDao dao;

@Test
public void testAdd(){
	OriginalDataStoreDto dto = new OriginalDataStoreDto();
	
	dto.setDeviceNo("112233");
	dto.setMessCode("0x01");
	dto.setMessType(1);
	dto.setMessData("faf50102040507080280004552");
	dto.setInTime(TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR));
	dto.setParsedData("aaaaaaaaaaaaaa");
	dto.setInDay(1);
	dao.addDto(dto);
	
}
@Test 
public void testGetMonth() {
	System.out.println(getMonthByIntime("2018-10-24 00:00:12"));
}
private int getMonthByIntime(String intime) {
	Calendar cal=Calendar.getInstance();
	Date d = TimeConvertor.stringTime2Date(intime, TimeConvertor.FORMAT_MINUS_24HOUR);
	cal.setTime(d);
	int month = cal.get(Calendar.MONTH)+1;
	return month;
}

}
