/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.billing.impl
 * 文件名：Properties.java
 * 版本信息：1.0.0
 * 日期：2018年8月7日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.icp.collect.ftp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

/**
 * 类名称：Properties
 * 类描述：
 * 创建人：donghaibo
 * 创建时间：2018年9月10日
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
@Service
public class ConfigProperties {
    @Value("${pregateway.ftp.host}")
    private String PrepositionHostname;
    @Value("${pregateway.ftp.port}")
    private int PrepositionPort;
    @Value("${pregateway.ftp.user}")
    private String PrepositionUsername;
    @Value("${pregateway.ftp.password}")
    private String PrepositionPassword;

    @Value("${collect.ftp.host}")
    private String CollectionHostname;
    @Value("${collect.ftp.port}")
    private int CollectionPort;
    @Value("${collect.ftp.user}")
    private String CollectionUsername;
    @Value("${collect.ftp.password}")
    private String CollectionPassword;
    public String getPrepositionHostname() {
        return PrepositionHostname;
    }

    public void setPrepositionHostname(String prepositionHostname) {
        PrepositionHostname = prepositionHostname;
    }

    public int getPrepositionPort() {
        return PrepositionPort;
    }

    public void setPrepositionPort(int prepositionPort) {
        PrepositionPort = prepositionPort;
    }

    public String getPrepositionUsername() {
        return PrepositionUsername;
    }

    public void setPrepositionUsername(String prepositionUsername) {
        PrepositionUsername = prepositionUsername;
    }

    public String getPrepositionPassword() {
        return PrepositionPassword;
    }

    public void setPrepositionPassword(String prepositionPassword) {
        PrepositionPassword = prepositionPassword;
    }

    public String getCollectionHostname() {
        return CollectionHostname;
    }

    public void setCollectionHostname(String collectionHostname) {
        CollectionHostname = collectionHostname;
    }

    public int getCollectionPort() {
        return CollectionPort;
    }

    public void setCollectionPort(int collectionPort) {
        CollectionPort = collectionPort;
    }

    public String getCollectionUsername() {
        return CollectionUsername;
    }

    public void setCollectionUsername(String collectionUsername) {
        CollectionUsername = collectionUsername;
    }

    public String getCollectionPassword() {
        return CollectionPassword;
    }

    public void setCollectionPassword(String collectionPassword) {
        CollectionPassword = collectionPassword;
    }
}
