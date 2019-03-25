package com.cpit.icp.collect.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.OriginalDataStoreDao;
import com.cpit.icp.dto.collect.OriginalDataStoreDto;

@Service
public class OriginalDataStoreMgmt {
	private final static Logger logger = LoggerFactory.getLogger(OriginalDataStoreMgmt.class);
@Autowired OriginalDataStoreDao originalDataStoreDao;
public void addDto(OriginalDataStoreDto dto){
	originalDataStoreDao.addDto(dto);
}
}
