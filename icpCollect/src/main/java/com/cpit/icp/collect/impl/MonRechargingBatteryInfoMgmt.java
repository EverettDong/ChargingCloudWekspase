package com.cpit.icp.collect.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonRechargingBatteryInfoDao;

@Service
public class MonRechargingBatteryInfoMgmt {
	private final static Logger logger = LoggerFactory.getLogger(MonRechargingBatteryInfoMgmt.class);
@Autowired MonRechargingBatteryInfoDao monRechargingBatteryInfoDao;

}
