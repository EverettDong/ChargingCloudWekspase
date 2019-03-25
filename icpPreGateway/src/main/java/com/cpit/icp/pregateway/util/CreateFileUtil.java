/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.icp.pregateway.util
 * 文件名：CreateFileUtil.java
 * 版本信息：1.0.0
 * 日期：2018年8月6日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.icp.pregateway.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类名称：CreateFileUtil 类描述： 创建人：jinzhiwei 创建时间：2018年8月6日 下午4:35:19 修改人： 修改时间：
 * 修改备注：
 * 
 * @version 1.0.0
 */
public class CreateFileUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(FtpUtil.class);

	/**
	 * 生成.json格式文件
	 */
	public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
		// 标记文件生成是否成功
		boolean flag = true;

		// 拼接文件完整路径
		String fullPath = filePath + File.separator + fileName + ".json";

		// 生成json格式文件
		try {
			// 保证创建一个新文件
			File file = new File(fullPath);
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();

			// 将格式化后的字符串写入文件
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(jsonString);
			write.flush();
			write.close();
		} catch (Exception e) {
			flag = false;
			logger.error("writejsonFile error,fileName = " + fileName);
		}

		// 返回是否成功的标记
		return flag;
	}

}
