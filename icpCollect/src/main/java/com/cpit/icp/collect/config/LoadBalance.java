package com.cpit.icp.collect.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.persistence.Basic;

/**
 * 用于调用其他微服务，参考注释
 * @author lyh
 *
 */
@Configuration
public class LoadBalance {


	@Bean
	@LoadBalanced
	public RestTemplate resTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate billingTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	@LoadBalanced
	public RestTemplate crmTemplate() {
		return new RestTemplate();
	}
	

	@Bean
	@LoadBalanced
	public RestTemplate hlhtTemplate() {
		return new RestTemplate();
	}

	@Bean
	public RestTemplate chargingTemplate(){
		return new RestTemplate();
	}
	

	
	
}
