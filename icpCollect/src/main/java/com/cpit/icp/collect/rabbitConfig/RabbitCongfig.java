package com.cpit.icp.collect.rabbitConfig;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName RabbitCongfig
 * @Description 队列的配置
 * @Author donghaibo
 * @Date 2018/10/23 14:07
 * @Version 1.0.0
 * @Copyright (c) 2018.10.23普天信息技术有限公司-版权所有
 **/
@Configuration
public class RabbitCongfig {

    @Bean
    public Queue Queue(){
        return new Queue("dealMsg");
    }
    
    @Bean
    public Queue charging_Queue(){
        return new Queue("icp-col-hlhtMsg");
    }
    
    
  
}
