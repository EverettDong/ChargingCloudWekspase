package com.cpit.icp.register.main;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;


@SpringBootApplication
@EnableEurekaServer
public class Application {
    private final static Logger logger = LoggerFactory.getLogger(Application.class) ; 

  	public static void main(String[] args) {
        String profile = null;
    		if(args != null && args.length != 0){
    			for(String s : args){
    				if(s.indexOf("--spring.profiles.active") != -1){
    					profile = s.split("=")[1];
    					break;
    				}
    			}
    		}

    		if(profile == null){
    			System.out.println("usage: java -jar icpRegisterServer.jar --spring.profiles.active=peer1[2]");
    			return;

    		}

    		SpringApplication app = new SpringApplication(Application.class);
    		app.setBannerMode(Banner.Mode.OFF);
    		app.run(args);
    		System.out.println("start register "+profile+" processing job...");
    		logger.info("start register "+profile+" processing job...");
  	}
}
