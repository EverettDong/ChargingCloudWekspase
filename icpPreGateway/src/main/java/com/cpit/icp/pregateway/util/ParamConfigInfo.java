package com.cpit.icp.pregateway.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParamConfigInfo {

	// private static ParamConfigInfo instance;
	private final static Logger logger = LoggerFactory.getLogger(FtpUtil.class);
	public static Properties props;
	static {
		props = new Properties();
		InputStream in = null;
		in = ParamConfigInfo.class.getClassLoader().getResourceAsStream("application.properties");
		try {
			props.load(in);
		} catch (IOException e) {
			logger.error("load Properties error" , e);
		}
	}

}
