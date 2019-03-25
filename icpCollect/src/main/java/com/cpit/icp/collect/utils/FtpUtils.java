/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.billing.impl.ftp
 * 文件名：FtpUtils.java
 * 版本信息：1.0.0
 * 日期：2018年8月6日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.icp.collect.utils;

import com.alibaba.fastjson.JSONObject;
import com.cpit.icp.collect.ftp.ConfigProperties;
import com.cpit.icp.collect.service.MsgRecOfflineData;
import com.cpit.icp.collect.service.OfflineDownload;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.text.SimpleDateFormat;
import java.util.*;

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
public class FtpUtils {

    private final static Logger logger = LoggerFactory.getLogger(FtpUtils.class);

    @Autowired
    private ReadTxt2Json readTxt2Json;
    /**
     * 获取FTPClient对象
     * @param ftpHost FTP主机服务器
     * @param ftpPassword FTP 登录密码
     * @param ftpUserName FTP登录用户名
     * @param ftpPort FTP端口 默认为21
     * @return
     */
    public FTPClient getFTPClient(String ftpHost, String ftpPassword,
                                  String ftpUserName, int ftpPort) {
        FTPClient ftpClient = null;
        try {
            ftpClient = new FTPClient();
            ftpClient.connect(ftpHost, ftpPort);// 连接FTP服务器
            ftpClient.login(ftpUserName, ftpPassword);// 登陆FTP服务器
            //ftpClient.setControlEncoding("UTF-8"); // 中文支持
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                logger.info("未连接到FTP，用户名或密码错误。");
                ftpClient.disconnect();
            } else {
                logger.info("FTP连接成功。");
            }
        } catch (SocketException e) {
            //e.printStackTrace();
            logger.info("FTP的IP地址可能错误，请正确配置。");
        } catch (IOException e) {
            //e.printStackTrace();
            logger.info("FTP的端口错误,请正确配置。");
        }
        return ftpClient;
    }

    /** * 下载文件 *
     * @param
     * @param
     * @return */
    public  boolean downloadFile(FTPClient ftpClient, long CurrentSystem,String ftpHost, String ftpPassword,
                                 String ftpUserName, int ftpPort){
        boolean flag = false;
        OutputStream os = null;
        FTPClient collectionftpClient = null;
        try {
            //ftpClient.changeWorkingDirectory(PrepositionPath);
            //logger.info("目录切换成功，开始下载文件"+PrepositionPath);
            //initPrepositionFtpClient();
            FTPFile[] ftpFiles = ftpClient.listFiles();
            if(ftpFiles == null || ftpFiles.length == 0){
                logger.info("can not read  files in ./pregate");
                return flag;
            }
            for(FTPFile file : ftpFiles){
                String fileName = file.getName();
                String beforeLastFileName = fileName.substring(22,36);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date parse = dateFormat.parse(beforeLastFileName);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(parse);
                long FileTime = calendar.getTimeInMillis();
                if(CurrentSystem - FileTime > 3*60*1000){
                    //初始化采集网关ftp
                    collectionftpClient = this.getFTPClient(ftpHost, ftpPassword, ftpUserName, ftpPort);
                    //collectionftpClient.changeWorkingDirectory(collectionPath);
                    os = collectionftpClient.storeFileStream(fileName);
                    logger.info("离线文件："+fileName);
                    ftpClient.retrieveFile(fileName, os);
                    //删除下载完的离线文件
                    this.deleteFile(ftpClient,fileName);
                    try {
                        os.flush();
                        os.close();
                    } catch (Exception e) {
                        logger.error("can not close os stream", e);
                    }
                }
            }
            flag = true;
        } catch (Exception e) {
            logger.error("下载文件失败，没有离线文件",e);
        } finally {
            try {
                os.close();
            } catch (Exception e) {
            }

            try {
                ftpClient.disconnect();
            } catch (Exception e) {
            }


            try {
                collectionftpClient.disconnect();
            } catch (Exception e) {
            }
        }
        return flag;
    }

        /** * 删除文件 *
         * @param
         * @param filename 要删除的文件名称 *
         * @return */
        public boolean deleteFile(FTPClient ftpClient,String filename){
            boolean flag = false;
            try {
                //ftpClient.changeWorkingDirectory(prepositionPath);
                ftpClient.deleteFile(filename);
                flag = true;
                logger.info("删除文件成功");
            } catch (Exception e) {
                logger.error("删除文件失败",e);
            }
            return flag;
        }

    /**
     * @Author donghaibo
     * @Description定时遍历解析本地文件夹下面的所有文件,并插入数据库
     * @Date
     * @Param
     * @return
     **/
    public List<MsgOfPregateDto> readTheLocal(String ftpHost, String ftpPassword,
                                              String ftpUserName, int ftpPort){
        List<MsgOfPregateDto> msgOfPregateDtoList = new ArrayList<MsgOfPregateDto>();
        try {
            //初始化业务网关ftp
            //initCollectionFtpClient();
            FTPClient collectFtpClient = this.getFTPClient(ftpHost, ftpPassword, ftpUserName, ftpPort);
            //获取当前文件夹下面的所有文件
            logger.info("切换目录成功，获取当前文件夹下面的所有文件");
            FTPFile[] files = collectFtpClient.listFiles();
            String path = collectFtpClient.printWorkingDirectory();
            List<FTPFile> fileList = new ArrayList<>(Arrays.asList(files));
            for(int i = fileList.size()-1; i>=0; i--){
                FTPFile offlineFile = fileList.get(i);
                if(offlineFile.isFile()){
                    String offlineFileName = offlineFile.getName();
                    //JSONObject offlineObject = readTxt2Json.readText2Json(collectFtpClient.printWorkingDirectory()+"/"+offlineFileName);
                    JSONObject offlineObject = readTxt2Json.readText2Json(collectFtpClient,offlineFileName,path);
                    if (offlineObject != null){
                        MsgOfPregateDto msgOfPregateDto = new MsgOfPregateDto();
                        msgOfPregateDto.setDeviceNo((String)offlineObject.get("deviceNo"));
                        msgOfPregateDto.setMsgCode((String)offlineObject.get("msgCode"));
                        msgOfPregateDto.setMsgVersion((String)offlineObject.get("msgVersion"));
                        msgOfPregateDto.setPreGateIp((String)offlineObject.get("preGateIp"));
                        msgOfPregateDto.setPreGatePort((String)offlineObject.get("preGatePort"));
                        msgOfPregateDto.setMsgData((String)offlineObject.get("msgData"));
                        logger.info(msgOfPregateDto.toString());
                        msgOfPregateDtoList.add(msgOfPregateDto);
                        //删除解析完的文件
                        //boolean b = this.deleteFile(collectFtpClient,offlineFileName,CollectionPath);
                    }else {
                        logger.info("文件没有解析成功");
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Local folder is empty, no offline files.",e);
        }
        return msgOfPregateDtoList;
    }

}
