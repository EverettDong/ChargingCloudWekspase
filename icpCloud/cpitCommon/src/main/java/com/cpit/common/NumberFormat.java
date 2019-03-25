/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.common
 * 文件名：NumberFormat.java
 * 版本信息：1.0.0
 * 日期：2018年10月13日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名称：NumberFormat
 * 类描述：
 * 创建人：hz_shangchuanxiang
 * 创建时间：2018年10月13日 下午12:11:52
 * 修改人：
 * 修改时间：
 * 修改备注：
 * @version 1.0.0
 */
public class NumberFormat {
	public static String numberFormatVerify(String cardId){
        String regex = "^[0-9]{15}$";
        String regex1 = "^[0-9]$";
        String msg = "";
        String chr= (String) cardId.subSequence(0, 1);
        if(cardId.length()!=16){
        	msg = "充电卡号应为16位数！";
        	return msg;
        } 
        if("v".equals(chr)||Pattern.matches(regex1,chr)) {
        	String chr1= (String) cardId.subSequence(1, 16);
        	Pattern p = Pattern.compile(regex);
        	Matcher m = p.matcher(chr1);
        	boolean isMatch = m.matches();
        	if(!isMatch){
        		msg = "您的充电卡号"+cardId+"格式不对！";
        		return msg;
        	}
        }
        return msg;
	}
}
