package com.cpit.icp.collect.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
/**
 * not used
 * @author zhangqianqian
 *
 */
@Api(tags = "原始数据备份")
@RestController
@RequestMapping(method={RequestMethod.POST})
public class OriginalDataBackup {
	  @RequestMapping(value = "/orignalDataBackup")
	  public boolean orignalDataBack() {
		  return true;
	  }
	  
}
