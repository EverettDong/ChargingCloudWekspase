package com.cpit.icp.collect.utils;

import org.springframework.stereotype.Component;
/**
 * 从解析后的数据decodedData(string)中再拆出具体的字段
 * @author zhangqianqian
 *
 */
@Component
public class ParseUtil {
	private String END_SEMICOLON=";";
	private String SPLIT_COLON=":";
	/**
	 * name:hexvalue:meaning; 根据name返回meaning字段
	 * @param nameStr
	 * @return
	 */
	public String getMeaningStr(String nameStr,String srcStr) {
		if(!srcStr.contains(nameStr)) {
			return null;
		}
		int index = srcStr.indexOf(SPLIT_COLON, srcStr.indexOf(nameStr)+nameStr.length());
		int end = srcStr.indexOf(END_SEMICOLON);
		return srcStr.substring(index+1, end);
		
	}
	/**
	 * 从decodedData中截取一个属性的字段 返回值为 name:hexValue:meaning;
	 * @param nameStr
	 * @param srcStr
	 * @return
	 */
	public String getTeamStr(String nameStr,String srcStr) {
		if(!srcStr.contains(nameStr)) {
			return null;
		}
		int begin = srcStr.indexOf(nameStr);
		int end = srcStr.indexOf(";", srcStr.indexOf(nameStr));
		String teamStr= srcStr.substring(begin,end)+END_SEMICOLON;
		return teamStr;
	}
	/**
	 * 返回字符串中，两个字符串之间的数据 比如 name:hexvalue:meanning 可返回 两个：之间的hexValue值。
	 * @param srcStr
	 * @param posStr
	 * @param endStr
	 * @return
	 */
	public String getMiddleStr(String srcStr,String posStr,String endStr) {
		int pos = srcStr.indexOf(posStr);
		int posStrLen = posStr.length();
		int end = srcStr.indexOf(endStr,pos+posStrLen);
		return srcStr.substring(pos+posStrLen, end);
	}
}
