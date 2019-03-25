/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.pregateway.util
 * 文件名：FtpUtil.java
 * 版本信息：1.0.0
 * 日期：2018年8月9日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.icp.pregateway.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.icp.pregateway.message.impl.MessageMgmt;

/**
 * 类名称：FtpUtil 类描述： 创建人：jinzhiwei 创建时间：2018年8月9日 下午6:24:51 修改人： 修改时间： 修改备注：
 * 
 * @version 1.0.0
 */
public class FtpUtil {

	private final static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	// pregate ip
	public static String pregateIp;
	// 接收上行tcp包端口
	public static int upMessagePort;
	// ftp登录用户名
	public static String userName;
	// ftp登录密码
	public static String userPassword;
	// ftp地址
	public static String server;
	// ftpport
	public static int port;
	// ftp的目录
	public static String path = "home/icp/pregate";

	/**
	 * 
	 * writeFile(这里用一句话描述这个方法的作用) (这里描述这个方法适用条件 – 可选)
	 * 
	 * @param fileContent
	 * @param fileName
	 *            void
	 * @exception @since
	 *                1.0.0
	 */
	public static boolean writeFile(String fileContent, String fileName) {

		FTPClient ftpClient = new FTPClient(); 
		try {
			InputStream is = null;
			// 1.输入流
			is = new ByteArrayInputStream(fileContent.getBytes());
			// 2.连接服务器
			ftpClient.connect(server, port);
			// 3.登录ftp
			ftpClient.login(userName, userPassword);
			// 4.指定写入的目录
			ftpClient.changeWorkingDirectory(path);
			// 5.写操作
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.storeFile(new String(fileName.getBytes("utf-8"), "iso-8859-1"), is);
			is.close();
			return true;
		} catch (Exception e) {			
			logger.error("writeFile error,fileName = " + fileName);
			return false;
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.disconnect();
				} catch (Exception e) {
					logger.error("disconnect error,fileName = " + fileName);
				}
			}
		}
		
	}
	

}
