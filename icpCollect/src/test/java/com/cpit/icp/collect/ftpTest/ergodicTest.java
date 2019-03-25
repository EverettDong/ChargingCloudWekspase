package com.cpit.icp.collect.ftpTest;

import com.cpit.icp.collect.ftp.ConfigProperties;
import com.cpit.icp.collect.main.Application;
import com.cpit.icp.collect.utils.FtpUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName ergodicTest
 * @Description TODO
 * @Author donghaibo
 * @Date 2018/12/10 10:39
 * @Version 1.0.0
 * @Copyright (c) 2018.12.10普天信息技术有限公司-版权所有
 **/
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ergodicTest {

    @Autowired
    FtpUtils ftp;

    @Autowired
    ConfigProperties properties;

    @Test
    public void test(){
        ftp.readTheLocal(properties.getCollectionHostname(),properties.getCollectionPassword(),properties.getCollectionUsername(),properties.getCollectionPort());
    }
}
