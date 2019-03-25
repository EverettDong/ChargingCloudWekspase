package com.cpit.icp.collect.utils;

import org.springframework.stereotype.Service;

/**
 * @ProjectName 新能源智能云平台
 * @ClassName ResultInfoCode
 * @Description TODO
 * @Author donghaibo
 * @Date 2018/9/29 14:27
 * @Version 1.0.0
 * @Copyright (c) 2018.09.29普天信息技术有限公司-版权所有
 **/
@Service
public class ResultInfoCode {

    public final static String free = "1";//空闲

    public final static String offline = "0";//离线

    public final static String occupy = "3";//占用

    public final static String makeAnAppointment = "4";//预约

    public final static String fault = "255";//故障

    /**
     * @Author donghaibo
     * @Description 构造开启充电必要的参数
     * @Date
     * @Param
     * @return
     **/
    public final static String chargeMode = "0x20";//自然充满

    public final static String planChargeTime = "00";

    public final static String planChargePower = "00";

    /**
     * @Author donghaibo
     * @Description 充电订单的状态
     **/
    public final static int StartUp = 0;//启动中

    public final static int ChargeStartOrder = 3;//充电中

    public final static int Cessation = 1;//停止中
}
