package com.cpit.icp.collect.controller;

import com.cpit.icp.collect.ftp.ConfigProperties;
import com.cpit.icp.collect.service.OfflineDownload;
import com.cpit.icp.collect.utils.FtpUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName FtpOfflineDownload
 * @Description TODO
 * @Author donghaibo
 * @Date 2018/9/12 10:39
 * @Version 1.0.0
 * @Copyright (c) 2018.09.12普天信息技术有限公司-版权所有
 **/
@RestController
public class FtpOfflineDownload {

    private final static org.slf4j.Logger logger = LoggerFactory.getLogger(FtpOfflineDownload.class);

    @Autowired
    private ConfigProperties properties;

    @Autowired
    private OfflineDownload offlineDownload;

    @RequestMapping(value = "/FtpOfflineFile")
    public boolean FtpOfflineFile(){
        boolean b = offlineDownload.OfflineFileDownload(properties.getPrepositionHostname(),properties.getPrepositionPassword(),
                properties.getPrepositionUsername(),properties.getPrepositionPort());
            if(!b){
            logger.info("离线文件下载失败");
            return false;
        }
        logger.info("离线文件下载成功");
        return true;
    }
}
