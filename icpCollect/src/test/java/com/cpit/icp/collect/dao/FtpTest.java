package com.cpit.icp.collect.dao;


import com.alibaba.fastjson.JSONObject;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.service.MsgRecOfflineData;
import com.cpit.icp.collect.utils.FtpUtils;
import com.cpit.icp.collect.utils.ReadTxt2Json;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName FtpTest
 * @Description TODO
 * @Author ptne-imc
 * @Date 2018/9/10 13:44
 * @Version 1.0.0
 * @Copyright (c) 2018.9.10普天信息技术有限公司-版权所有
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class FtpTest {

    @Autowired
    FtpUtils ftp;

    @Autowired
    ReadTxt2Json readTxt2Json;

    @Autowired
    MsgRecOfflineData msgRecOfflineData;

    private String localpath = "E:\\Offlinefile";

    @Test
    public void b(){
        //初始化ftp服务器
       // ftp.initFtpClient();

        String pathname = "/home/icp/pregate";

        /** * 下载文件 *
         * @param pathname FTP服务器文件目录 *
         * @param filename 文件名称 *
         * @param localpath 下载后的文件路径 *
         * @return */
        //当前时间
        long timeMillis = System.currentTimeMillis();
        //获取ftp服务器上的离线文件
       // boolean b = ftp.downloadFile(localpath,timeMillis);
        //System.out.println(b);
        /*JSONObject jsonObject = readTxt2Json.readText2Json(localpath+"\\"+filename);
        String deviceNo = (String)jsonObject.get("deviceNo");
        String msgCode = (String)jsonObject.get("msgCode");
        String msgData = (String)jsonObject.get("msgData");
        String msgVersion = (String)jsonObject.get("msgVersion");
        String preGateIp = (String)jsonObject.get("preGateIp");
        String preGatePort = (String)jsonObject.get("preGatePort");

        System.out.println("deviceNo"+deviceNo+"---msgCode"+msgCode+"---msgData"+msgData+"---msgVersion"+msgVersion+"---preGateIp"+preGateIp+"---preGatePort"+preGatePort);*/
    }

    @Test
    public void LocalFileTest(){
        MsgOfPregateDto msgOfPregateDto = null;
        //解析本地文件
       // List<MsgOfPregateDto> msgOfPregateDtos = ftp.readTheLocal(localpath);
        /*for (MsgOfPregateDto msgOfPregateDto1:
                msgOfPregateDtos) {
            if (msgOfPregateDto != null) {
                //将离线数据插入数据库
                msgRecOfflineData.receivedOfflineData(msgOfPregateDto1);
            }
        }*/
    }
    
    /*public static void main(String[] arg) throws ParseException {
        String time = "20180828100524";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date parse = dateFormat.parse(time);
        SimpleDateFormat Format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        System.out.println(Format.format(parse));
    }
*/
}
