package com.cpit.icp.collect.impl;

import com.cpit.icp.dto.collect.MonRechargingInfoDto;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.dao.MonRechargingInfoDao;

@Service
public class MonRechargingInfoMgmt {
	  private final static Logger logger = LoggerFactory.getLogger(MonRechargingInfoMgmt.class);
	    @Autowired
	    MonRechargingInfoDao monRechargingInfoDao;

	    /**
	     * @Author donghaibo
	     * @Description 插入以及更新操作
	     * @Date
	     * @Param
	     * @return
	     **/
	    public boolean addDto(MonRechargingInfoDto dto){
	        MonRechargingInfoDto monRechargingInfoDto = this.getByDeviceNoAP(dto.getDeviceNo(), dto.getPosition(), dto.getInDate());
	        if(monRechargingInfoDto == null){
	            //执行插入操作
	            monRechargingInfoDao.addDto(dto);
	            logger.info("dto does not exist,add.");
	            return true;
	        }else{
	            //执行更新操作
	            monRechargingInfoDao.updateDto(dto);
	            logger.info("dto exists,update");
	            return true;
	        }

	    }

	    /**
	     * @Author donghaibo
	     * @Description根据主键查询
	     * @Date
	     * @Param
	     * @return
	     **/
	    public MonRechargingInfoDto getByDeviceNoAP(String deviceNo, String position,String inDate){
	        if(deviceNo == null || position == null || inDate == null){
	            return null;
	        }else{
	            return monRechargingInfoDao.getByDeviceNoAP(deviceNo,position,inDate);
	        }
	    }
	    
	    public MonRechargingInfoDto getCurrentDto(String deviceNo,String pos){
	    	return null;
	    }
}
