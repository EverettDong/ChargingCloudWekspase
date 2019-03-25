package com.cpit.icp.collect.coderDecoder.util;






import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggerOperator {

	/**
	 * 
	 * @param loggerName
	 * @param message
	 *            写入的日志消息
	 */
	public static void debug(String loggerName, String message) {
		Logger log = LoggerFactory.getLogger(loggerName);
		log.debug(message);
	}

	/**
	 * 
	 * @param loggerName
	 * @param message
	 *            写入的日志消息
	 * @param t
	 *            记录抛出的异常信息。
	 */

	public static void debug(String loggerName, String message, Throwable t) {
		Logger log = LoggerFactory.getLogger(loggerName);
		log.debug(message, t);
	}

	/**
	 * 
	 * @param loggerName
	 * @param message
	 *            写入的日志消息
	 */
	public static void info(String loggerName, String message) {
		Logger log = LoggerFactory.getLogger(loggerName);
		log.info(message);
	}

	/**
	 * 
	 * @param loggerName
	 * @param message
	 *            写入的日志消息
	 * @param t
	 *            记录抛出的异常信息。
	 */
	public static void info(String loggerName, String message, Throwable t) {
		Logger log = LoggerFactory.getLogger(loggerName);
		log.info(message, t);
	}

	/**
	 * 
	 * @param loggerName
	 * @param message
	 *            写入的日志消息
	 */
	public static void warn(String loggerName, String message) {
		Logger log = LoggerFactory.getLogger(loggerName);
		log.warn(message);

	}

	/**
	 * 
	 * @param loggerName
	 * @param message
	 *            写入的日志消息
	 * @param t
	 *            记录抛出的异常信息。
	 */
	public static void warn(String loggerName, String message, Throwable t) {
		Logger log = LoggerFactory.getLogger(loggerName);
		log.warn(message, t);

	}

	/**
	 * 
	 * @param loggerName
	 * @param message
	 *            写入的日志消息
	 */
	public static void err(String loggerName, String message) {
		Logger log = LoggerFactory.getLogger(loggerName);
		log.error(message);

	}

	/**
	 * 
	 * @param loggerName
	 * @param message
	 *            写入的日志消息
	 * @param t
	 *            记录抛出的异常信息。
	 */
	public static void err(String loggerName, String message, Throwable t) {
		Logger log = LoggerFactory.getLogger(loggerName);
		log.error(message, t);

	}


}
