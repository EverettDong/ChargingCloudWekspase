package com.cpit.icp.collect.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonFaultRecordDao;
import com.cpit.icp.dto.collect.MonFaultRecordDto;

@Service
public class MonFaultRecordMgmt {
@Autowired MonFaultRecordDao dao;
public boolean addDto(MonFaultRecordDto dto) {
	dao.addDto(dto);
	return true;
}
public List<MonFaultRecordDto> list(MonFaultRecordDto dto){
	return dao.list(dto);
}
}
