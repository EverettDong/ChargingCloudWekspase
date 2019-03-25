package com.cpit.icp.configserver.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;


@EnableConfigServer
@SpringBootApplication
public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class) ; 

	
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
		System.out.println("start config-server docker processing job...");
		logger.info("start config-server docker processing job...");

	}
	
}
