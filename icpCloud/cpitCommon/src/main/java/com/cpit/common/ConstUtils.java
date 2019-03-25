/**
 * 项目名：新能源智能云平台
 * 包名：com.cpit.common
 * 文件名：ConstUtil.java
 * 版本信息：1.0.0
 * 日期：2018年9月12日
 * Copyright (c) 2018普天信息技术有限公司-版权所有
 */
package com.cpit.common;

 /**
 * 类名称：ConstUtil
 * 类描述：常量工具类
 * 创建人：hz_shangchuanxiang
 * 创建时间：2018年9月12日 下午6:02:29
 * 修改人：
 * 修改时间：
 * 修改备注：
 * @version 1.0.0
 */
public class ConstUtils {

	public static final int FUND_BALANCE_TYPE_ID = 1;//资金账本类型id编号
	public static final int GIFT_BALANCE_TYPE_ID =2; //2.赠送账本类型id编号
	public static final int SPECIAL_BALANCE_TYPE_ID =3; //3.专项账本类型id编号
	public static final int ACCOUNT_TYPES_NUMBER = 3;//账本类型总数量

	public static final String RESPONSE_CODE_0 = "000000";//成功交易
	public static final String RESPONSE_CODE_1 = "000001";//签名验证错误
	public static final String RESPONSE_CODE_2 = "100002";//时间戳过期
	public static final String RESPONSE_CODE_3 = "100003";//IP 验证错误，可能网络不稳定，请稍后再试
	public static final String RESPONSE_CODE_4 = "100004";//订单 ChkV 验证错误
	public static final String RESPONSE_CODE_5 ="100005";//交易记录已存在
	public static final String RESPONSE_CODE_6 = "100006";//商户时间错误
	public static final String RESPONSE_CODE_7 = "100007";//商户号不正确
	public static final String RESPONSE_CODE_8 = "100008";//原订单不存在
	public static final String RESPONSE_CODE_9 = "100009";//订单状态不正确，无法退货
	public static final String RESPONSE_CODE_10 = "100010"; //原交易不存在

	public static final String RESPONSE_CODE_11 ="100011";//退货金额超限
	public static final String RESPONSE_CODE_12 ="100012";//商户不支持退货
	public static final String RESPONSE_CODE_13 ="100013";//退货次数超限
	public static final String RESPONSE_CODE_14 ="100014";//恭喜！已支付成功
	public static final String RESPONSE_CODE_15 ="100015";//转入卡类型不匹配
	public static final String RESPONSE_CODE_16 ="100016";//同一卡号无法转账
	public static final String RESPONSE_CODE_17 ="100017";//原交易已冲正
	public static final String RESPONSE_CODE_18 ="100018";//卡不支持该交易，请检查商户号是否正确

	public static final String RESPONSE_CODE_19 ="100019";//目标卡不支持此卡转入
	public static final String RESPONSE_CODE_20 ="100020";//密码参数场景错误
	public static final String RESPONSE_CODE_21 ="100021";//原交易已撤销
	public static final String RESPONSE_CODE_22 ="100022";//金额与原交易金额不符
	public static final String RESPONSE_CODE_23 ="100023";//必填项为空
	public static final String RESPONSE_CODE_24 ="100024";//虚拟卡库存不足，请联系技术人员
	public static final String RESPONSE_CODE_25 ="100025";//账号未开户或子账户类型不正确
	public static final String RESPONSE_CODE_26 ="100026";//智能中心账号已开过户

	/**
	 * 充值方式
	 */
	public static final String COUNTER_NUMBER = "1";//柜面
	public static final String APP_NUMBER = "2";//app
	public static final String NETWORK_HALL_NUMBER = "3";//网厅

	/**
	 * 服务状态
	 */
	public static final Integer SERVICE_STATUS_0 = 0;//0:服务注销
	public static final Integer SERVICE_STATUS_1 = 1;//1:正常服务
	public static final Integer SERVICE_STATUS_2 = 2;//2:已挂失
	public static final Integer SERVICE_STATUS_3 = 3;//3:余额不足
	public static final Integer SERVICE_STATUS_4 = 4;//4:服务注销
}
