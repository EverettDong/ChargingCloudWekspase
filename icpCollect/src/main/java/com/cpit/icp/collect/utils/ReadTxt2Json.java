package com.cpit.icp.collect.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName ReadTxt2Json
 * @Description TODO
 * @Author donghaibo
 * @Date 2018/9/10 16:04
 * @Version 1.0.0
 * @Copyright (c) 2018.9.10普天信息技术有限公司-版权所有
 **/
@Service
public class ReadTxt2Json {
    private final static Logger logger = LoggerFactory.getLogger(ReadTxt2Json.class);



    /**
     * @Author donghaibo
     * @Description //读取txt文件内容,(json格式数据)
     * @Date
     * @Param
     * @return
     **/
    public JSONObject readText2Json(FTPClient collectFtpClient,String offlineFileName,String path){
        //File file = new File(path);
        String jsonStr = "";
        InputStream inputStream = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        try {
            String path2 = path + "/" + offlineFileName;
            collectFtpClient.enterLocalPassiveMode();
            inputStream = collectFtpClient.retrieveFileStream(new String(path2.getBytes("GBK"),"ISO-8859-1"));
            isr = new InputStreamReader(inputStream,"UTF-8");
            br  = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null){
                jsonStr += line;
            }
            return JSONObject.parseObject(jsonStr);
        } catch (Exception e) {
            logger.error("error in readText2Json",e);
            return null;
        } finally {
            if(br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            if(isr != null){
                try {
                    isr.close();
                } catch (IOException e) {
                    //e.printStackTrace();
                }
            }
            if (inputStream != null){
                try {
                    inputStream.close();
                    collectFtpClient.completePendingCommand();
                } catch (IOException e) {
                    //logger.error("error in readText2Json",e);
                }
            }
        }
    }
}
