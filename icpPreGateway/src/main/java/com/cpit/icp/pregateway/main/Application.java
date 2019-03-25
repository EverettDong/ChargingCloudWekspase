package com.cpit.icp.pregateway.main;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.cpit.icp.pregateway.netty.NettyServerBootstrap;
import com.cpit.icp.pregateway.util.FtpUtil;

//单独部署的运用

@SpringBootApplication
@EnableTransactionManagement
@EnableHystrix
@MapperScan(basePackages = "com.cpit.icp.pregateway.message.dao", annotationClass = com.cpit.common.MyBatisDao.class)
@ComponentScan(basePackages = { "com.cpit" })
public class Application {
	private final static Logger logger = LoggerFactory.getLogger(Application.class);
	
	
	public static void main(String[] args) {
		System.setProperty("spring.devtools.restart.enabled", "true");
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		logger.warn("start icp preGateway processing job...pregateIp="+FtpUtil.pregateIp+",upMessagePort=" + FtpUtil.upMessagePort);
		try {
			new NettyServerBootstrap(FtpUtil.upMessagePort);
		} catch (Exception e) {
			logger.error("start NettyServerBootstrap error",e);
			
		}

	}

}
