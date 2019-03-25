/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.common
 * 文件名：PhoneTypeVerify.java
 * 版本信息：1.0.0
 * 日期：2018年10月13日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类名称：PhoneTypeVerify
 * 类描述：
 * 创建人：hz_shangchuanxiang
 * 创建时间：2018年10月13日 上午11:44:48
 * 修改人：
 * 修改时间：
 * 修改备注：
 * @version 1.0.0
 */
public class PhoneTypeVerify {
	public static String  phoneTypeVerify(String phone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$";
        String msg = "";
        if(phone.length() != 11){
            msg = "手机号应为11位数！";
            return msg;
        }else{
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            if(!isMatch){
            	msg = "您的手机号" + phone + "是错误格式！";
            	return msg;
            }
        }
        return msg;
	}    
}
