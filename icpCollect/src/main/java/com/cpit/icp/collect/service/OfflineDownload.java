package com.cpit.icp.collect.service;

import com.cpit.icp.collect.ftp.ConfigProperties;
import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import com.cpit.icp.collect.utils.FtpUtils;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName OfflineDownload
 * @Description TODO
 * @Author ptne-imc
 * @Date 2018/9/12 10:51
 * @Version 1.0.0
 * @Copyright (c) 2018.09.12普天信息技术有限公司-版权所有
 **/
@Service
public class OfflineDownload {

    private final static Logger logger = LoggerFactory.getLogger(OfflineDownload.class);

    @Autowired
    FtpUtils ftp;

    @Autowired
    MsgRecOfflineData msgRecOfflineData;

    @Autowired
    private ConfigProperties properties;

    /**
     * @Author donghaibo
     * @Description 执行初始化服务器，下载文件，并且将得到的数据插入数据库
     * @Date
     * @Param
     * @return
     **/
    public boolean OfflineFileDownload(String ftpHost, String ftpPassword,
                                       String ftpUserName, int ftpPort) {
        //生成当次日程执行开始时间
        long CurrentSystem = System.currentTimeMillis();
        //获取ftpClient对象
        FTPClient ftpClient = ftp.getFTPClient(ftpHost, ftpPassword, ftpUserName, ftpPort);
        //获取登录采集网关ftp的连接信息
        //下载文件
        boolean b = ftp.downloadFile(ftpClient, CurrentSystem,properties.getCollectionHostname(),
                properties.getCollectionPassword(),properties.getCollectionUsername(),properties.getCollectionPort());
        if (!b) {
            return false;
        }else{
            //遍历解析本地文件
            List<MsgOfPregateDto> msgOfPregateDtos = ftp.readTheLocal(properties.getCollectionHostname(),
                    properties.getCollectionPassword(),properties.getCollectionUsername(),properties.getCollectionPort());
            if(msgOfPregateDtos == null || msgOfPregateDtos.size() == 0){
                logger.info("Local offline file is empty.");
                return false;
            }
            for (MsgOfPregateDto msgOfPregateDto:
                    msgOfPregateDtos) {
                if(msgOfPregateDto != null){
                    //将离线数据插入数据库
                    msgRecOfflineData.receivedOfflineData(msgOfPregateDto);
                }else{
                    return false;
                }
            }
        }
        return true;
    }
}
