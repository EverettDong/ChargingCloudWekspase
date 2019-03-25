package com.cpit.icp.collect.msgProcess;

/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.pregateway.util
 * 文件名：ResfulUtil.java
 * 版本信息：1.0.0
 * 日期：2018年8月10日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */


import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

/**
 * 类名称：ResfulUtil 类描述： 创建人：jinzhiwei 创建时间：2018年8月10日 下午2:23:19 修改人： 修改时间： 修改备注：
 * 
 * @version 1.0.0
 */
public class ResfulUtil {

	// private RestTemplate restTemplate = null;

	// public ResfulUtil(RestTemplate restTemplate){
	// this.restTemplate = restTemplate;
	// }
	
	//  ip  发给业务网关
	public static String resfulIp;
    //  port 发给业务网关
	public static String resfulPort;
	
	/**
	 * 本机ip
	 * @return
	 */
	public static String getIp(){
		InetAddress address;
		try {
			address = InetAddress.getLocalHost();
			String hostAddress = address.getHostAddress();   
			return hostAddress;
		} catch (UnknownHostException e) {
			return null;
		}	
	}	

	/**
	 * Json提交
	 * 
	 * @param url
	 * @param responseClz
	 * @param jsonObject
	 * @return
	 */
	public static Object doPost(String url, Class responseClz, String jsonStr) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
		HttpEntity<String> entity = new HttpEntity<String>(jsonStr, headers);
		return restTemplate.postForObject(url, entity, responseClz);
	}

}
