package com.cpit.icp.collect.dao;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.stereotype.Component;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName QuartzService
 * @Description 自定义消息通道
 * @Author ptne-imc
 * @Date 2018/9/4 10:46
 * @Version 1.0.0
 * @Copyright (c) 2018.09.07普天信息技术有限公司-版权所有
 **/
@Component
public interface MySinkerInfo {

    @Output("myOutput")
    MessageChannel myOutput();
}
