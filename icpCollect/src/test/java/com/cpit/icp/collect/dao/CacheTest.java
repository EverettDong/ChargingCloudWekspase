package com.cpit.icp.collect.dao;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.collect.MonChargingProcessDataDto;
import com.cpit.icp.dto.collect.MonChargingStatus4App;
import com.cpit.icp.dto.collect.MonFaultRecordDto;
import com.cpit.icp.dto.collect.MonSequenceDto;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class,webEnvironment=WebEnvironment.NONE)
public class CacheTest {
@Autowired CacheUtil cacheUtil;

	
	
	
	

	

	
	
	
	@Test
	public void testUpdateDto() throws Exception{
		MonChargingStatus4App data = cacheUtil.getStatusData("0102");
		System.out.println(data.toString());
		data.setEnabled("YES");
		data.setIsAC("yes");
		cacheUtil.setStatusData(data);
		MonChargingStatus4App ndata = cacheUtil.getStatusData("0102");
		System.out.println(ndata.toString());
		ndata.setIsDC("no");
		ndata.setEnabled("no");
		cacheUtil.setStatusData(ndata);
		MonChargingStatus4App n1data = cacheUtil.getStatusData("0102");
		System.out.println(n1data.toString());
		
	}
	
	@Test
	public void testFaultRedis() {
		MonFaultRecordDto dto = new MonFaultRecordDto();
		dto.setDeviceNo("0103");
		dto.setDeviceVersion("3.4");
		dto.setFaultData("0004");
		dto.setInDate("2018/11/26 16:47:00");
		dto.setFaultLevel("1级");
		
		cacheUtil.upFaultData("0103", dto);
		
		
		List<MonFaultRecordDto> list = cacheUtil.getFaultData("0103");
		MonFaultRecordDto dto1 = new MonFaultRecordDto();
		dto1.setDeviceNo("0103");
		dto1.setDeviceVersion("3.4");
		dto1.setFaultData("0004");
		dto1.setInDate("2018/11/26 16:47:00");
		dto1.setFaultLevel("1级");
		cacheUtil.upFaultData("0103",dto1);
		
		List<MonFaultRecordDto> list1 = cacheUtil.getFaultData("0103");
		System.out.println(list1.size());
	}
	
	@Test
	public void testXmlParse() {
		String version = "3.4";
		Document document = cacheUtil.getProcConfigXml(version);
		
		System.out.println(document==null);
	}
	
	@Test
	public void testDecodeCache() {
		String key="31-充电枪数量";
		cacheUtil.setKeyValue(key, 2);
		System.out.println(cacheUtil.getKeyValue(key));
		cacheUtil.removeKeyValue(key);
		System.out.println(cacheUtil.getKeyValue(key));
	}
	
	@Test
	public void testSequenceData() {
		for(int i=0;i<3;i++) {
			MonSequenceDto sequenceDto = cacheUtil.getSequence("01");
					System.out.println(sequenceDto.toString());
		}
		cacheUtil.initSequenceData("01");
		MonSequenceDto sequenceDto = cacheUtil.getSequence("01");
		System.out.println(sequenceDto.toString());
		
		/*sequenceDto.getFlowNum();
		sequenceDto.getSequenceNum();
		System.out.println(sequenceDto.toString());
		
	cacheUtil.upSequenceData("01", sequenceDto);
	MonSequenceDto sequenceDto1 = cacheUtil.getSequenceData("01");
		System.out.println(sequenceDto1.toString());
		*
		*/
		
	}
	
}
