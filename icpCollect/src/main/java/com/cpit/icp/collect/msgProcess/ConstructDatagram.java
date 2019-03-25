package com.cpit.icp.collect.msgProcess;

import static com.cpit.common.Encodes.encodeMD5;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_1;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_2;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_4;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.cpit.icp.dto.charging.ChargeCard;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.cpit.common.Encodes;
import com.cpit.icp.collect.coderDecoder.common.configurable.type;
import com.cpit.icp.collect.coderDecoder.util.CommFunction;
import com.cpit.icp.collect.coderDecoder.util.MessgeTransfer;
//import com.cpit.testplatform.modules.serviceinfodata.entity.Account;
//import com.cpit.testplatform.modules.serviceinfodata.entity.Battery;
//import com.cpit.testplatform.modules.serviceinfodata.entity.Car;
//import com.cpit.testplatform.modules.serviceinfodata.entity.Charge;
//import com.cpit.testplatform.modules.serviceinfodata.service.AccountService;
//import com.cpit.testplatform.modules.serviceinfodata.service.BatteryService;
//import com.cpit.testplatform.modules.serviceinfodata.service.CarService;
//import com.cpit.testplatform.modules.serviceinfodata.service.ChargeService;
//import com.cpit.testplatform.modules.sockComm.msgMonitor.MsgMap;
import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.dto.billing.baseconfig.BfAcctBalanceT;
import com.cpit.icp.dto.billing.finance.BfCenterTransT;
import com.cpit.icp.dto.billing.recharge.RechargeBeginResponse;
import com.cpit.icp.dto.collect.MonRechargeRecordDto;
import com.cpit.icp.dto.collect.coderDecoder.CheckResultDto;
import com.cpit.icp.dto.collect.coderDecoder.UIDataContentDto;
//import com.cpit.testplatform.modules.testsuite.comm.SockMess;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.crm.User;

@Service
@Scope("singleton")
public class ConstructDatagram {
    private final static Logger logger = LoggerFactory.getLogger(ConstructDatagram.class);
    @Autowired
    CallOuterInf callOuterInf;
    @Autowired
    MonDBService monDBService;
    @Autowired
    CacheUtil cacheUtil;
    @Autowired
    ConstructMsgUtil constructMsgUtil;

    // @Autowired
    // CarService carService;
    // @Autowired
    // BatteryService batteryService;
    // @Autowired
    // ChargeService chargeService;
    // @Autowired
    // AccountService accountService;
    private static int i = 0;
    private static int Type_1 = 1;// 用户id，指令序号 2字节byte
    private static int Type_2 = 2;// 单字节byte
    private static int Type_3 = 3;// 多字节byte 直接转成16进制数
    private static int Type_4 = 4;// ascii
    private static int Type_5 = 5;// time
    //private static int Type_6 = 6;// car no
    private static String name_UserId = "用户ID";
    private static String name_Code = "报文编号";
    private static String name_SerialNo = "指令序号";

    private static String order_62 = "0x62";
    private static String order_65 = "0x65";
    private static String order_61 = "0x61";
    private static String order_69 = "0x6D";
    private static String order_64 = "0x64";
    private static String order_68 = "0x68";
    private static String order_6B = "0x6B";
    // private MsgMap msgMap=MsgMap.getInstance();

    // private static Map<String, String> suiteCommGroup = new HashMap<String,
    // String>();

    public ConstructDatagram() {

    }

    public CheckResultDto constructWrongData(String version, MsgOfPregateDto sData, String name) {
        /*
         * CheckResultDto errorData = new CheckResultDto(); String mescode = null;
         * Map<Byte, Byte> reportMapReverse =msgMap.getReportMapReverse(version); String
         * sMessCode = sData.getMsgCode(); String sMessCodeSub =
         * sMessCode.substring(sMessCode.indexOf("x")+1, sMessCode.length()); byte
         * reportCode =CodeTransfer.hexStringToByte(sMessCodeSub); byte reportFlag =0;
         * if (reportMapReverse.containsKey(reportCode)) { reportFlag =
         * reportMapReverse.get(reportCode); mescode =
         * CodeTransfer.byteToHexStr(reportFlag, true);
         *
         * } logger.debug( "construct Wrong Data mescode is " + mescode);
         *
         * errorData.setCheckTime(new Date()); errorData.setEquipName(name);
         * errorData.setMesCode(mescode); ; errorData.setAnswerTime(0);
         * errorData.setSrcMessage(""); errorData.setCheckContent("");
         * errorData.setErrorInfo("未收到回应"); errorData.setT1CheckResult("T1不正确");
         * errorData.setT2CheckResult("T2不正确"); errorData.setT3CheckResult("T3不正确");
         *
         * return errorData;
         */
        return null;
    }

    private List<UIDataContentDto> construct(String wrongCode) {
        if ("51".equals(wrongCode)) {
            return this.construct51();
        } else {
            return null;
        }
    }

    private List<UIDataContentDto> construct51() {
        return null;
    }

    public List<UIDataContentDto> construct01(String userID) {
        List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
        UIDataContentDto dto1 = new UIDataContentDto();
        dto1.setOrder("01");
        dto1.setName("用户名");
        dto1.setValue(userID);

        l.add(dto1);

        UIDataContentDto dto2 = new UIDataContentDto();
        dto2.setOrder("01");
        dto2.setName("序列号");
        dto2.setValue(String.valueOf(i++));

        l.add(dto2);

        return l;
    }

    public List<UIDataContentDto> construct09(String userID) {
        List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
        UIDataContentDto dto1 = new UIDataContentDto();
        dto1.setOrder("09");
        dto1.setName("用户名");
        dto1.setValue(userID);

        l.add(dto1);

        UIDataContentDto dto2 = new UIDataContentDto();
        dto2.setOrder("09");
        dto2.setName("序列号");
        dto2.setValue(String.valueOf(i++));

        l.add(dto2);

        return l;
    }

    /*
     * 用户id，2byte；指令序号，2byte；卡号，16byte；交易类型，1byte；交易日期时间；中心交易流水，15byte；
     * BOSS出单机构流水号，20byte
     */
    public List<UIDataContentDto> construct61(byte[] data) {
        UIDataContentDto dto1, dto2, dto3, dto4, dto5, dto6, dtoTime;
        List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();

        dto1 = new UIDataContentDto();
        dto1.setOrder(order_61);
        dto1.setName(this.name_UserId);
        dto1.setValue(this.byteArray2String(data, 0, 2, Type_1));
        l.add(dto1);
        dto2 = new UIDataContentDto();
        dto2.setOrder(order_61);
        dto2.setName(this.name_SerialNo);
        dto2.setValue(this.byteArray2String(data, 2, 2, Type_1));
        l.add(dto2);

        dto3 = new UIDataContentDto();
        dto3.setOrder(order_61);
        dto3.setName("卡号");
        String cardNo = this.byteArray2String(data, 4, 16, Type_4);
        logger.debug("get cardId" + cardNo);

        dto3.setValue(cardNo);
        l.add(dto3);

        dto4 = new UIDataContentDto();
        dto4.setOrder(order_61);
        dto4.setName("交易类型");
        dto4.setValue(this.byteArray2String(data, 20, 1, Type_2));
        l.add(dto4);


        RechargeBeginResponse resdata = callOuterInf.getAccountInfo(cardNo);
        String centerFlowNum = "0";
        String bossFlowNum = "0";
        if (resdata != null) {
            centerFlowNum = resdata.getCenterTransactionSerialNumber();
            bossFlowNum = resdata.getBossDocumentsSerialNumber();
        }
        dtoTime = new UIDataContentDto();
        dtoTime.setOrder(order_61);
        dtoTime.setName("交易日期时间");
        String transTime = this.byteArray2String(data, 21, 8, Type_5);
        dtoTime.setValue(transTime);
        l.add(dtoTime);

        dto5 = new UIDataContentDto();
        dto5.setOrder(order_61);
        dto5.setName("中心交易流水");
        dto5.setValue(centerFlowNum);
        l.add(dto5);

        dto6 = new UIDataContentDto();
        dto6.setOrder(order_61);
        dto6.setName("BOSS出单机构流水号");
        dto6.setValue(bossFlowNum);
        l.add(dto6);
        //logger.info("construct61 return list: " + l.size());
        return l;
    }

    /* 用户id 2byte ，指令序号2byte，充电类型1byte，车牌编码8byte,车牌vin，当前交易时间 */
    public List<UIDataContentDto> construct65(byte[] data, String codeMode) {

        logger.debug("begin to construct65");
        List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
        UIDataContentDto dto1 = new UIDataContentDto();
        dto1.setOrder(order_65);

        dto1.setName(name_UserId);
        dto1.setValue(this.byteArray2String(data, 0, 2, Type_1));

        UIDataContentDto dto2 = new UIDataContentDto();
        dto2.setOrder(order_65);
        dto2.setName(name_SerialNo);
        dto2.setValue(this.byteArray2String(data, 2, 2, Type_1));
        UIDataContentDto dto3 = new UIDataContentDto();
        dto3.setOrder(order_65);
        dto3.setName("充电枪位置1充电类型");
        dto3.setValue(this.byteArray2String(data, 4, 1, Type_2));
        UIDataContentDto dto4 = new UIDataContentDto();
        dto4.setOrder(order_65);
        dto4.setName("车辆车牌编码");
        byte[] dataConvert = Arrays.copyOfRange(data, 5, 13);
        String car_no = MessgeTransfer.carCodebyteT0str(dataConvert, codeMode);
        //String car_no = this.byteArray2String(data, 5, 8, Type_6);
        dto4.setValue(car_no);
        UIDataContentDto dto5 = new UIDataContentDto();
        dto5.setOrder(order_65);
        dto5.setName("车辆VIN");
        // Car car = carService.getCarByName(car_no);
        logger.debug("calloutinf get carinfo.");
        String car = null;

        car = callOuterInf.getCarInfo(car_no);

        if (car == null) {
            dto5.setValue(" ");
            logger.info("construct65 car,database no record.");
        } else {
            logger.info("get car vin");
            dto5.setValue(car);
            // dto5.setValue(car.getCarvin());
        }

        l.add(dto1);
        l.add(dto2);
        l.add(dto3);
        l.add(dto4);
        l.add(dto5);
        UIDataContentDto dtoTime = new UIDataContentDto();
        dtoTime.setOrder(order_65);
        dtoTime.setName("交易日期时间");
        dtoTime.setValue(this.byteArray2String(data, 13, 8, Type_5));
        l.add(dtoTime);
        logger.info("construct65 return list: " + l.size());
        return l;

    }

    public List<UIDataContentDto> construct31_65(byte[] data, String codeMode) {

        logger.debug("begin to construct65");
        List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
        UIDataContentDto dto1 = new UIDataContentDto();
        dto1.setOrder(order_65);

        dto1.setName(name_UserId);
        dto1.setValue(this.byteArray2String(data, 0, 2, Type_1));

        UIDataContentDto dto2 = new UIDataContentDto();
        dto2.setOrder(order_65);
        dto2.setName(name_SerialNo);
        dto2.setValue(this.byteArray2String(data, 2, 2, Type_1));
        UIDataContentDto dto3 = new UIDataContentDto();
        dto3.setOrder(order_65);
        dto3.setName("充电枪编号");
        dto3.setValue(this.byteArray2String(data, 4, 1, Type_2));
        UIDataContentDto dto4 = new UIDataContentDto();
        dto4.setOrder(order_65);
        dto4.setName("车辆车牌编码");
        byte[] dataConvert = Arrays.copyOfRange(data, 5, 13);
        String car_no = MessgeTransfer.carCodebyteT0str(dataConvert, codeMode);
        //String car_no = this.byteArray2String(data, 5, 8, Type_6);
        dto4.setValue(car_no);
        UIDataContentDto dto5 = new UIDataContentDto();
        dto5.setOrder(order_65);
        dto5.setName("车辆VIN");
        // Car car = carService.getCarByName(car_no);
        logger.debug("calloutinf get carinfo.");
        String car = null;

        car = callOuterInf.getCarInfo(car_no);

        if (car == null) {
            dto5.setValue(" ");
            logger.info("construct65 car,database no record.");
        } else {
            logger.info("get car vin");
            dto5.setValue(car);
            // dto5.setValue(car.getCarvin());
        }

        l.add(dto1);
        l.add(dto2);
        l.add(dto3);
        l.add(dto4);
        l.add(dto5);
        UIDataContentDto dtoTime = new UIDataContentDto();
        dtoTime.setOrder(order_65);
        dtoTime.setName("交易日期时间");
        dtoTime.setValue(this.byteArray2String(data, 13, 8, Type_5));
        l.add(dtoTime);
        logger.info("construct65 return list: " + l.size());
        return l;

    }

    public List<UIDataContentDto> construct31_62(byte[] data) {

        logger.debug("begin to construct62...");
        List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
        UIDataContentDto dto1, dto2, dto3, dto4, dto5, dto6, dtoTime;
        dto1 = new UIDataContentDto();
        dto1.setOrder(order_62);
        dto1.setName(name_UserId);
        dto1.setValue(this.byteArray2String(data, 0, 2, Type_1));
        l.add(dto1);
        dto2 = new UIDataContentDto();
        dto2.setOrder(order_62);
        dto2.setName(name_SerialNo);
        dto2.setValue(this.byteArray2String(data, 2, 2, Type_1));
        l.add(dto2);
        dto3 = new UIDataContentDto();
        dto3.setOrder(order_62);
        dto3.setName("模块个数");
        dto3.setValue(this.byteArray2String(data, 4, 1, Type_2));
        l.add(dto3);
        dto4 = new UIDataContentDto();
        dto4.setOrder(order_62);
        dto4.setName("模块号");
        dto4.setValue(this.byteArray2String(data, 5, 1, Type_2));
        l.add(dto4);
        dto5 = new UIDataContentDto();
        dto5.setOrder(order_62);
        dto5.setName("电池个数");
        String barryNum = this.byteArray2String(data, 6, 1, Type_2);
        logger.debug(" construct62... barryNum" + barryNum);
        dto5.setValue(barryNum);
        l.add(dto5);
        int barryN = Integer.parseInt(barryNum);

        UIDataContentDto[] barryCode = new UIDataContentDto[barryN];
        UIDataContentDto[] barryType = new UIDataContentDto[barryN];
        for (int i = 0; i < barryN; i++) {
            barryCode[i] = new UIDataContentDto();
            barryCode[i].setOrder(order_62);
            barryCode[i].setName("电池箱编码");
            String code = this.byteArray2String(data, 7 + i * 8, 8, Type_3);
            barryCode[i].setValue(code);
            l.add(barryCode[i]);

            barryType[i] = new UIDataContentDto();
            barryType[i].setOrder(order_62);
            barryType[i].setName("电池类型");

            // Battery battery = batteryService.getBatteryByName(code);
            // Object battery = callOuterInf.getBatteryInfo(code);
            String type = "1";
            /*
             * if (battery == null) { type = "1"; logger.debug(
             * " construct62... barryType does not exist!" ); } else { // type =
             * battery.getBatterytype(); // logger.debug(
             * " construct62... barryType exists,value is "+type ); }
             */
            barryType[i].setValue(type);
            l.add(barryType[i]);

        }

        dtoTime = new UIDataContentDto();
        dtoTime.setOrder(order_62);
        dtoTime.setName("交易日期时间");
        dtoTime.setValue(this.byteArray2String(data, 7 + barryN * 8, 8, Type_5));
        l.add(dtoTime);

        return l;

    }

    /* 用户id，指令序号，模块个数，充电枪位置1充电类型，电池个数，{电池箱编码n，电池类型n} */
    public List<UIDataContentDto> construct62(byte[] data) {

        logger.debug("begin to construct62...");
        List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
        UIDataContentDto dto1, dto2, dto3, dto4, dto5, dto6, dtoTime;
        dto1 = new UIDataContentDto();
        dto1.setOrder(order_62);
        dto1.setName(name_UserId);
        dto1.setValue(this.byteArray2String(data, 0, 2, Type_1));
        l.add(dto1);
        dto2 = new UIDataContentDto();
        dto2.setOrder(order_62);
        dto2.setName(name_SerialNo);
        dto2.setValue(this.byteArray2String(data, 2, 2, Type_1));
        l.add(dto2);
        dto3 = new UIDataContentDto();
        dto3.setOrder(order_62);
        dto3.setName("模块个数");
        dto3.setValue(this.byteArray2String(data, 4, 1, Type_2));
        l.add(dto3);
        dto4 = new UIDataContentDto();
        dto4.setOrder(order_62);
        dto4.setName("充电枪位置充电类型");
        dto4.setValue(this.byteArray2String(data, 5, 1, Type_2));
        l.add(dto4);
        dto5 = new UIDataContentDto();
        dto5.setOrder(order_62);
        dto5.setName("电池个数");
        String barryNum = this.byteArray2String(data, 6, 1, Type_2);
        logger.debug(" construct62... barryNum" + barryNum);
        dto5.setValue(barryNum);
        l.add(dto5);
        int barryN = Integer.parseInt(barryNum);

        UIDataContentDto[] barryCode = new UIDataContentDto[barryN];
        UIDataContentDto[] barryType = new UIDataContentDto[barryN];
        for (int i = 0; i < barryN; i++) {
            barryCode[i] = new UIDataContentDto();
            barryCode[i].setOrder(order_62);
            barryCode[i].setName("电池箱编码");
            String code = this.byteArray2String(data, 7 + i * 8, 8, Type_3);
            barryCode[i].setValue(code);
            l.add(barryCode[i]);

            barryType[i] = new UIDataContentDto();
            barryType[i].setOrder(order_62);
            barryType[i].setName("电池类型");

            // Battery battery = batteryService.getBatteryByName(code);
            // Object battery = callOuterInf.getBatteryInfo(code);
            String type = "1";
            /*
             * if (battery == null) { type = "1"; logger.debug(
             * " construct62... barryType does not exist!" ); } else { // type =
             * battery.getBatterytype(); // logger.debug(
             * " construct62... barryType exists,value is "+type ); }
             */
            barryType[i].setValue(type);
            l.add(barryType[i]);

        }

        dtoTime = new UIDataContentDto();
        dtoTime.setOrder(order_62);
        dtoTime.setName("交易日期时间");
        dtoTime.setValue(this.byteArray2String(data, 7 + barryN * 8, 8, Type_5));
        l.add(dtoTime);
        logger.info("construct62 " + l.size());
        return l;

    }

    /* 用户ID，指令序号，充电枪位置类型，卡号，记录条数 从0x74中读取 */
    /*
     * 电池BMS编码，开始充电SOC，结束充电SOC，本次累计充电量，本次累计充电能，充电时间长度，充电满策略，充电满策略参数，是否正常结束，
     * 开始时间，结束时间，交易时间 从数据库中读取 term_charge
     */
    /* 17个dto 根据record查询记录条数 */
    public List<UIDataContentDto> construct64N(String version, byte[] data) throws Exception {

        logger.debug("begin to construct64N...");
        List<UIDataContentDto> list = new ArrayList<UIDataContentDto>();

        UIDataContentDto[] dtos = new UIDataContentDto[17];
        String id = this.byteArray2String(data, 0, 2, Type_1);
        String serial = byteArray2String(data, 2, 2, Type_1);
        String charge_type = byteArray2String(data, 4, 1, Type_2);
        String card = this.byteArray2String(data, 5, 16, Type_4);

        // String records = this.byteArray2String(data, 21, 1, Type_2);

        String[] names = new String[5];
        String[] name = new String[12];

        String[] values = {id, serial, charge_type, card, ""};
        if (version.equals(VERSION_3_4) || version.equalsIgnoreCase(VERSION_3_1)) {
            names = new String[]{name_UserId, name_SerialNo, "充电枪位置充电类型", "卡号", "记录条数"};
            name = new String[]{"电池BMS编码", "开始充电SOC", "结束充电SOC", "本次累计充电量", "本次累计充电能", "充电时间长度", "充电满策略", "充电满策略参数",
                    "是否正常结束", "开始日期时间", "结束日期时间", "交易开始日期时间"};
        }
        if (version.equals(VERSION_3_5)) {
            String serialNo = constructMsgUtil.processSerialNo(serial);
            values[1] = serialNo;
            logger.info("64 serialNo: " + serialNo + " " + values[1]);
            names = new String[]{name_Code, name_SerialNo, "充电枪位置充电类型", "卡号", "记录条数"};
            name = new String[]{"电池BMS编码", "开始充/放电SOC", "结束充/放电SOC", "本次累计充/放电量", "本次累计充/放电能", "充/放电时间长度", "充满策略",
                    "充满策略参数", "是否正常结束", "开始日期时间", "结束日期时间", "交易开始日期时间"};
        }

        for (int m = 0; m < 4; m++) {
            UIDataContentDto d = new UIDataContentDto();
            d.setOrder(order_64);
            d.setName(names[m]);
            d.setValue(values[m]);

            list.add(d);
        }
        // int record = Integer.parseInt(records);
        // logger.debug("record :"+ record);
        logger.info("begin to query database: " + card);
        List<MonRechargeRecordDto> chargeList = monDBService.queryRecent5Record(card);
        if ((chargeList != null) && (chargeList.size() > 0)) {
            int realSize = chargeList.size();
            String records = String.valueOf(realSize);
            logger.info("charge records : " + records);

            UIDataContentDto recordd = new UIDataContentDto();
            recordd.setOrder(order_64);
            recordd.setName("记录条数");
            recordd.setValue(records);
            list.add(recordd);
            logger.debug("iterator database Data");

            for (int i = 0; i < realSize; i++) {
                String[] vs = {chargeList.get(i).getChargerCode(), chargeList.get(i).getStartSoc(),
                        chargeList.get(i).getEndSoc(), chargeList.get(i).getAh(), chargeList.get(i).getKwh(),
                        chargeList.get(i).getChargeTime(), chargeList.get(i).getStrategy(),
                        chargeList.get(i).getStrategyParam(), chargeList.get(i).getNormalEnd(),
                        chargeList.get(i).getStartTime(), chargeList.get(i).getEndTime(),
                        chargeList.get(i).getTraceTime()};

                for (int j = 0; j < 12; j++) {
                    UIDataContentDto d = new UIDataContentDto();
                    d.setOrder(order_64);
                    d.setName(name[j]);
                    d.setValue(vs[j]);

                    list.add(d);
                }
            }

            return list;
        } else {
            logger.info("query database ,get 0 data.");
            UIDataContentDto recordd = new UIDataContentDto();
            recordd.setOrder(order_64);
            recordd.setName("记录条数");
            recordd.setValue("0");
            list.add(recordd);
            return list;
        }

    }

    public List<UIDataContentDto> construct69(String version, byte[] data, String deviceNo) {

        logger.info("begin to construct reply of 70...");
        if (version.equals(VERSION_3_2)) {
            return construct32_69(version, data);
        }
        List<UIDataContentDto> list = new ArrayList<UIDataContentDto>();
        UIDataContentDto[] dtos = new UIDataContentDto[17];
        String[] names = new String[17];
        String order_69Flag = null;
        String id = null;
        String serial = null;
        if (version.equals(VERSION_3_5)) {
            logger.info("version 3.5");
            order_69Flag = "0x6D";
            names = new String[]{this.name_UserId, this.name_SerialNo, "卡号", "客户姓名", "响应码", "响应码描述", "客户号", "电费主账户余额",
                    "电费主账户可用余额", "服务费账户余额", "服务费账户可用余额", "中心交易流水", "BOSS出单机构流水号", "本次允许充电电量", "本次允许充电时间", "交易日期时间",
                    "报文认证码"};
            id = this.byteArray2String(data, 0, 2, Type_1);
            String value = this.byteArray2String(data, 2, 2, Type_1);
            serial = constructMsgUtil.processSerialNo(value);
            logger.info("6d serialNo: " + serial);
        }
        if (version.equals(VERSION_3_4)) {
            logger.info("version 3.4");
            order_69Flag = "0x69";
            names = new String[]{this.name_UserId, this.name_SerialNo, "卡号", "客户姓名", "响应码", "响应码描述", "客户号", "电费主账户余额",
                    "电费主账户可用余额", "服务费账户余额", "服务费账户可用余额", "中心交易流水", "BOSS出单机构流水号", "本次允许充电电量", "本次允许充电时间", "交易日期时间",
                    "报文认证码"};
            id = this.byteArray2String(data, 0, 2, Type_1);
            serial = this.byteArray2String(data, 2, 2, Type_1);
        }

        String card = this.byteArray2String(data, 4, 16, Type_4);

        String msgAutCode = this.byteArray2String(data, 135, 8, Type_3);
        byte[] passwordBytes = Arrays.copyOfRange(data, 106, 122);
        String encodedPassword = encodeCodePass(passwordBytes);

        logger.info("id,serial,card " + id + " " + serial + " " + card);

        logger.info("call outerInf");
        ChargeCard chargeCard = callOuterInf.getCrmUserByCard(card);
        RechargeBeginResponse currentAc = callOuterInf.getAccountInfo(card);

        BigDecimal totalBalance = callOuterInf.queryTotalBalance(card);

        logger.info("call outerInf over");
        InnerObj69 innerObj = getResCodeFor69(chargeCard, encodedPassword, currentAc, deviceNo, totalBalance);
        String[] resCode = innerObj.getCodeRes();

        String[] values = new String[17];
        if (currentAc != null) {
            logger.info(currentAc.toString());
            values[0] = id;
            values[1] = serial;
            values[2] = card;
            values[3] = currentAc.getCustomerName();
            values[4] = resCode[0];
            values[5] = resCode[1];
            values[6] = currentAc.getCustomerNumber();
            values[7] = totalBalance.toString();
            values[8] = totalBalance.toString();
            values[9] = currentAc.getServiceChargeAccountBalance();
            values[10] = currentAc.getServerAccountAvailableBalance();
            values[11] = currentAc.getCenterTransactionSerialNumber();
            values[12] = currentAc.getBossDocumentsSerialNumber();
            values[13] = "ffffffff";
            values[14] = "ffffffff";
            values[15] = this.byteArray2String(data, 98, 8, Type_5);
            values[16] = msgAutCode;

            logger.debug("valuess:" + Arrays.toString(values));
            logger.debug("set BfCenterTrans");
            BfCenterTransT bfTransT = new BfCenterTransT();
            bfTransT.setCenterTransId(currentAc.getCenterTransactionSerialNumber());
            bfTransT.setBossSmsNo(currentAc.getBossDocumentsSerialNumber());
            bfTransT.setAuthDate(new Date());
            bfTransT.setObjId(card);
            String serviceId = "";
            int userId = 0;
            if (chargeCard != null) {
                serviceId = chargeCard.getPhone();
                userId = Integer.parseInt(chargeCard.getId());
            }
            bfTransT.setPhoneNumber(serviceId);
            bfTransT.setCustNo(String.valueOf(userId));
            logger.info("set bf:" + bfTransT.toString());
            cacheUtil.upBfData(bfTransT);
            // return null;
        } else {
            logger.info("currentAc is null,set values 0");
            String balance = "0.0";
            values[0] = id;
            values[1] = serial;
            values[2] = card;
            values[3] = "";
            values[4] = resCode[0];
            values[5] = resCode[1];
            values[6] = "";
            values[7] = balance;
            values[8] = balance;
            values[9] = balance;
            values[10] = balance;
            values[11] = "";
            values[12] = "";
            values[13] = "ffffffff";
            values[14] = "ffffffff";
            values[15] = this.byteArray2String(data, 98, 8, Type_5);
            values[16] = msgAutCode;

        }

        //

        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = new UIDataContentDto();

            dtos[i].setOrder(order_69Flag);

            dtos[i].setName(names[i]);
            dtos[i].setValue(values[i]);
            list.add(dtos[i]);
        }

        return list;

    }

    private List<UIDataContentDto> construct32_69(String version, byte[] data) {
        logger.info("begin to construct reply of 32_70...");
        List<UIDataContentDto> list = new ArrayList<UIDataContentDto>();
        UIDataContentDto[] dtos = new UIDataContentDto[17];
        String[] names = new String[17];
        String order_69Flag = null;
        String id = null;
        String serial = null;

        order_69Flag = "0x69";
        names = new String[]{this.name_UserId, this.name_SerialNo, "卡号", "客户姓名", "响应码", "响应码描述", "客户号", "电费主账户余额",
                "电费主账户可用余额", "服务费账户余额", "服务费账户可用余额", "中心交易流水", "BOSS出单机构流水号", "本次允许充电电量", "本次允许充电时间", "交易日期时间",
                "报文认证码"};
        id = this.byteArray2String(data, 0, 2, Type_1);
        serial = this.byteArray2String(data, 2, 2, Type_1);


        String card = this.byteArray2String(data, 4, 16, Type_4);

        String msgAutCode = this.byteArray2String(data, 135, 8, Type_3);
        byte[] passwordBytes = Arrays.copyOfRange(data, 106, 122);
        String encodedPassword = encodeCodePass(passwordBytes);
        logger.info("call outerInf begin:" + card);
        ChargeCard chargeCard = callOuterInf.getCrmUserByCard(card);
        RechargeBeginResponse currentAc = callOuterInf.getAccountInfo(card);
        BigDecimal totalBalance = callOuterInf.queryTotalBalance(card);
        logger.info("call outerInf over");
        String[] resCode = getResCodeFor32_69(chargeCard, encodedPassword, currentAc, totalBalance);

        String[] values = new String[17];
        if (currentAc != null) {
            logger.info(currentAc.toString());
            values[0] = id;
            values[1] = serial;
            values[2] = card;
            values[3] = currentAc.getCustomerName();
            values[4] = resCode[0];
            values[5] = resCode[1];
            values[6] = currentAc.getCustomerNumber();
            values[7] = totalBalance.toString();
            values[8] = totalBalance.toString();
            values[9] = currentAc.getServiceChargeAccountBalance();
            values[10] = currentAc.getServerAccountAvailableBalance();
            values[11] = currentAc.getCenterTransactionSerialNumber();
            values[12] = currentAc.getBossDocumentsSerialNumber();
            values[13] = "ffffffff";
            values[14] = "ffffffff";
            values[15] = this.byteArray2String(data, 98, 8, Type_5);
            values[16] = msgAutCode;

            logger.debug("valuess:" + Arrays.toString(values));
            logger.debug("set BfCenterTrans");
            BfCenterTransT bfTransT = new BfCenterTransT();
            bfTransT.setCenterTransId(currentAc.getCenterTransactionSerialNumber());
            bfTransT.setBossSmsNo(currentAc.getBossDocumentsSerialNumber());
            bfTransT.setAuthDate(new Date());
            bfTransT.setObjId(card);
            String serviceId = "";
            int userId = 0;
            if (chargeCard != null) {
                serviceId = chargeCard.getPhone();
                userId = Integer.parseInt(chargeCard.getId());
            }
            bfTransT.setPhoneNumber(serviceId);
            bfTransT.setCustNo(String.valueOf(userId));
            logger.info("set bf:" + bfTransT.toString());
            cacheUtil.upBfData(bfTransT);

        } else {
            logger.info("currentAc is null,set values 0");
            String balance = "0.0";
            values[0] = id;
            values[1] = serial;
            values[2] = card;
            values[3] = "";
            values[4] = resCode[0];
            values[5] = resCode[1];
            values[6] = "";
            values[7] = balance;
            values[8] = balance;
            values[9] = balance;
            values[10] = balance;
            values[11] = "";
            values[12] = "";
            values[13] = "ffffffff";
            values[14] = "ffffffff";
            values[15] = this.byteArray2String(data, 98, 8, Type_5);
            values[16] = msgAutCode;
        }
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = new UIDataContentDto();
            dtos[i].setOrder(order_69Flag);
            dtos[i].setName(names[i]);
            dtos[i].setValue(values[i]);
            list.add(dtos[i]);
        }

        return list;

    }

    public List<UIDataContentDto> construct6B(String version, byte[] data) throws Exception {

        logger.debug("construct 6B begin...");
        List<UIDataContentDto> list = new ArrayList<UIDataContentDto>();
        UIDataContentDto[] dtos = new UIDataContentDto[6];
        String[] names = new String[6];
        String userid = null;
        String serialNo = null;
        if (version.equals(VERSION_3_4)) {
            names = new String[]{name_UserId, name_SerialNo, "卡号", "流程序号", "交易日期时间", "报文认证码"};
            userid = this.byteArray2String(data, 0, 2, Type_1);
            serialNo = this.byteArray2String(data, 2, 2, Type_1);
        }
        if (version.equals(VERSION_3_5)) {
            names = new String[]{name_Code, name_SerialNo, "卡号", "流程序号", "交易日期时间", "报文认证码"};
            userid = this.byteArray2String(data, 0, 2, Type_1);
            String value = this.byteArray2String(data, 2, 2, Type_1);
            serialNo = constructMsgUtil.processSerialNo(value);
            logger.info("6B serialNo: " + serialNo);
        }

        String card = this.byteArray2String(data, 4, 16, this.Type_4);
        logger.debug("callOuterInf " + card);

        boolean cardNotNet = callOuterInf.isCardNet(card);
        logger.info("card: " + card + "   cardNotNet: " + cardNotNet);
        // Account currentAc = accountService.getAccountByCard(card);

        String flowCode = new String("00000000");
        if (cardNotNet) {
            flowCode = new String("FFFFFFFF");
        }
        String[] values = {userid, serialNo, card,
                flowCode,

                this.byteArray2String(data, 28, 8, Type_5), this.byteArray2String(data, 36, 8, Type_3)};
        logger.debug("construct 6B valuess:" + Arrays.toString(values));
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = new UIDataContentDto();
            dtos[i].setOrder(order_6B);
            dtos[i].setName(names[i]);
            dtos[i].setValue(values[i]);
            list.add(dtos[i]);
        }

        return list;

    }

    public List<UIDataContentDto> construct68(byte[] data) {
        logger.debug("construct 68 begin...");
        List<UIDataContentDto> list = new ArrayList<UIDataContentDto>();
        UIDataContentDto[] dtos = new UIDataContentDto[12];
        String[] names = {name_UserId, name_SerialNo, "充电枪位置充电类型", "卡号", "客户号", "本次充电结算时间和电量", "本次充电结算电费", "本次充电服务费",
                "本次累计消费总金额", "中心交易流水", "BOSS出单机构流水号", "交易日期时间"};

        String id = this.byteArray2String(data, 0, 2, Type_1);
        String serial = this.byteArray2String(data, 2, 2, Type_1);
        String type = this.byteArray2String(data, 4, 1, Type_2);
        String card = this.byteArray2String(data, 5, 16, Type_4);
        String time = this.byteArray2String(data, 84, 8, Type_5);
        String[] values = {id, serial, type, card, this.getRandom(), this.getRandom(), this.getRandom(),
                this.getRandom(), this.getRandom(), this.getRandom(), this.getRandom(), time};
        logger.debug("construct 68 valuess:" + Arrays.toString(values));
        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = new UIDataContentDto();
            dtos[i].setOrder(order_68);
            dtos[i].setName(names[i]);
            dtos[i].setValue(values[i]);
            list.add(dtos[i]);
        }
        return list;
    }

    public List<UIDataContentDto> construct62(String terminalVersion, byte[] data) {
        if (terminalVersion.equals(VERSION_3_5)) {
            logger.debug("version 35,begin to construct62...");
            List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
            UIDataContentDto dto1, dto2, dto3, dto4, dto5, dto6, dtoTime;
            dto1 = new UIDataContentDto();
            dto1.setOrder(order_62);
            dto1.setName(name_Code);
            dto1.setValue(this.byteArray2String(data, 0, 2, Type_1));
            l.add(dto1);
            dto2 = new UIDataContentDto();
            dto2.setOrder(order_62);
            dto2.setName(name_SerialNo);
            String value = this.byteArray2String(data, 2, 2, Type_1);
            String serialNo = constructMsgUtil.processSerialNo(value);
            logger.info("62 serialNo " + serialNo);
            dto2.setValue(serialNo);
            l.add(dto2);
            dto3 = new UIDataContentDto();
            dto3.setOrder(order_62);
            dto3.setName("模块个数");
            dto3.setValue(this.byteArray2String(data, 4, 1, Type_2));
            l.add(dto3);
            dto4 = new UIDataContentDto();
            dto4.setOrder(order_62);
            dto4.setName("充电枪位置1充/放电类型");
            dto4.setValue(this.byteArray2String(data, 5, 1, Type_2));
            l.add(dto4);
            dto5 = new UIDataContentDto();
            dto5.setOrder(order_62);
            dto5.setName("电池个数");
            String barryNum = this.byteArray2String(data, 6, 1, Type_2);
            logger.debug(" construct62... barryNum" + barryNum);
            dto5.setValue(barryNum);
            l.add(dto5);
            int barryN = Integer.parseInt(barryNum);

            UIDataContentDto[] barryCode = new UIDataContentDto[barryN];
            UIDataContentDto[] barryType = new UIDataContentDto[barryN];
            for (int i = 0; i < barryN; i++) {
                barryCode[i] = new UIDataContentDto();
                barryCode[i].setOrder(order_62);
                barryCode[i].setName("电池箱编码" + (i + 1));
                String code = this.byteArray2String(data, 7 + i * 8, 8, Type_3);
                barryCode[i].setValue(code);
                l.add(barryCode[i]);

                barryType[i] = new UIDataContentDto();
                barryType[i].setOrder(order_62);
                barryType[i].setName("电池类型" + (i + 1));

                // Battery battery = batteryService.getBatteryByName(code);
                // Object battery = callOuterInf.getBatteryInfo(code);
                String type = "1";// 非0表示合法
                /*
                 * if (battery == null) { type = "1"; logger.debug(
                 * " construct62... barryType does not exist!" ); } else { //type =
                 * battery.getBatterytype(); logger.debug(
                 * " construct62... barryType exists,value is "+type ); }
                 */
                barryType[i].setValue(type);
                l.add(barryType[i]);

            }

            dtoTime = new UIDataContentDto();
            dtoTime.setOrder(order_62);
            dtoTime.setName("交易日期时间");
            dtoTime.setValue(this.byteArray2String(data, 7 + barryN * 8, 8, Type_5));
            l.add(dtoTime);

            return l;
        } else if (terminalVersion.equals(VERSION_3_4)) {
            return this.construct62(data);
        } else if (terminalVersion.equals(VERSION_3_1)) {
            return this.construct31_62(data);
        } else {
            logger.error("version is wrong ,return null");
            return null;
        }

    }

    public List<UIDataContentDto> construct65(String terminalVersion, byte[] data, String codeMode) {

        if (terminalVersion.equals(VERSION_3_5)) {

            logger.debug("begin to construct65");
            List<UIDataContentDto> l = new ArrayList<UIDataContentDto>();
            UIDataContentDto dto1 = new UIDataContentDto();
            dto1.setOrder(order_65);

            dto1.setName(name_Code);
            dto1.setValue(this.byteArray2String(data, 0, 2, Type_1));

            UIDataContentDto dto2 = new UIDataContentDto();
            dto2.setOrder(order_65);
            dto2.setName(name_SerialNo);
            String value = this.byteArray2String(data, 2, 2, Type_1);
            String serialNo = constructMsgUtil.processSerialNo(value);
            logger.info("65 serialNo: " + serialNo);
            dto2.setValue(serialNo);
            UIDataContentDto dto3 = new UIDataContentDto();
            dto3.setOrder(order_65);
            dto3.setName("充电枪位置1充电类型");
            dto3.setValue(this.byteArray2String(data, 4, 1, Type_2));
            UIDataContentDto dto4 = new UIDataContentDto();
            dto4.setOrder(order_65);
            dto4.setName("车辆车牌编码");
            byte[] dataConvert = new byte[8];
            dataConvert = Arrays.copyOfRange(data, 5, 13);
            String car_no = MessgeTransfer.carCodebyteT0str(dataConvert, codeMode);
            //String car_no = this.byteArray2String(data, 5, 8, Type_6);
            dto4.setValue(car_no);
            UIDataContentDto dto5 = new UIDataContentDto();
            dto5.setOrder(order_65);
            dto5.setName("车辆VIN");
            // Car car = carService.getCarByName(car_no);

            String carVin = null;

            carVin = callOuterInf.getCarInfo(car_no);

            if (carVin == null) {
                dto5.setValue(" ");
                logger.info("construct65 car,database no record.");
            } else {
                logger.info("get car vin.");
                // logger.debug("construct65 car" + car.getCarvin());
                dto5.setValue(carVin);
            }

            l.add(dto1);
            l.add(dto2);
            l.add(dto3);
            l.add(dto4);
            l.add(dto5);
            UIDataContentDto dtoTime = new UIDataContentDto();
            dtoTime.setOrder(order_65);
            dtoTime.setName("交易日期时间");
            dtoTime.setValue(this.byteArray2String(data, 13, 8, Type_5));
            l.add(dtoTime);
            return l;

        }
        if (terminalVersion.equals(VERSION_3_4)) {
            return this.construct65(data, codeMode);
        }
        if (terminalVersion.equals(VERSION_3_1)) {
            return this.construct31_65(data, codeMode);
        }
        return null;
    }

    private String getAccountCodeDetail(String code) {
        Map<String, String> accountCode = new HashMap<String, String>();
        accountCode.put("000000", "成功交易");
        accountCode.put("000001", "用户密码不正确");
        accountCode.put("000002", "用户不合法");
        accountCode.put("000003", "用户不可用");
        accountCode.put("000004", "用户充电账户无法使用");
        accountCode.put("000005", "账户余额不足");
        accountCode.put("000006", "用户支付账户停止使用");
        accountCode.put("000007", "用户支付账户余额不明");
        accountCode.put("000009", "用户支付账户无法查询");
        accountCode.put("00000A", "用户支付账户无法使用");
        accountCode.put("100001", "设备维护，暂停使用");

        if (accountCode.containsKey(code)) {
            return accountCode.get(code);
        } else {
            return null;
        }

    }

    private UIDataContentDto getCurrentTimeDto(String order) {
        UIDataContentDto dto = new UIDataContentDto();
        dto.setOrder(order);
        dto.setName("交易时间日期");

        dto.setValue(this.getCurrentTime());
        return dto;
    }

    /* 2015-07-15-12-33-33 类型 */
    private String getCurrentTime() {

        Date d = new Date();

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        return sf.format(d);
    }

    private String getTimeByData(Date d) {

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

        return sf.format(d);
    }

    private String byteArray2String(byte[] data, int start, int len, int type) {
        byte[] dataConvert = new byte[len];
        for (int i = 0; i < len; i++) {
            dataConvert[i] = data[start + i];
        }
        String s = null;
        switch (type) {
            case 1:// 用户id，指令序号
                s = MessgeTransfer.bytesTostr(dataConvert);
                break;
            case 2:// 一个字节 to 整型
                s = Integer.toString(CommFunction.unsignByteToInt(dataConvert[0]));
                break;
            case 3:// 多个字节to十六进制str
                s = CommFunction.byteArrayToHexStr(dataConvert);
                break;
            case 4:
                StringBuilder mysb = new StringBuilder();
                s = mysb.append(CommFunction.bytesToAscii(dataConvert)).toString();
                break;

            case 5: // time
                s = MessgeTransfer.TimebyteTostr(dataConvert);
                break;

        }
        return s;
    }

    private boolean checkDeviceStatus(String deviceNo) {
        //int status = callOuterInf.getDeviceStatus(deviceNo);
        int status = callOuterInf.getChargeStatus(deviceNo);
        if (status == 1 || status == 2 || status == 3) {
            return true;
        } else {
            return false;
        }

    }

    private boolean checkAccountBalance(ChargeCard chargeCard, BigDecimal totalBalance) {

        BigDecimal b = new BigDecimal(0.0);

        logger.info("querytotalBalance : " + totalBalance);
        if (totalBalance != null) {

            if (totalBalance.compareTo(b) == 0) {
                return false;
            } else if (totalBalance.compareTo(b) == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            logger.info("get totalBalance is null");
            return false;
        }

    }

    public List<UIDataContentDto> construct60(String version, byte[] data, String deviceNo) throws Exception {

        List<UIDataContentDto> list = new ArrayList<UIDataContentDto>();
        UIDataContentDto[] dtos = new UIDataContentDto[13];
        String[] names = new String[13];
        String order_69Flag = null;
        String id = null;
        String serial = null;

        order_69Flag = "0x60";
        names = new String[]{this.name_UserId, this.name_SerialNo, "响应码", "响应码描述", "客户号", "电费主账户余额",
                "电费主账户可用余额", "服务费账户余额", "服务费账户可用余额", "中心交易流水", "BOSS出单机构流水号", "本次允许充电电量", "本次允许充电时间", "交易日期时间",
                "报文认证码"};
        id = this.byteArray2String(data, 0, 2, Type_1);
        serial = this.byteArray2String(data, 2, 2, Type_1);


        String card = this.byteArray2String(data, 4, 16, Type_4);

        String msgAutCode = this.byteArray2String(data, 135, 8, Type_3);

        //msgAutCode="2020202020202020";

        byte[] passwordBytes = Arrays.copyOfRange(data, 106, 122);
        String encodedPassword = encodeCodePass(passwordBytes);


        logger.info("call outerInf");
        ChargeCard chargeCard = callOuterInf.getCrmUserByCard(card);
        RechargeBeginResponse currentAc = callOuterInf.getAccountInfo(card);

        BigDecimal totalBalance = callOuterInf.queryTotalBalance(card);

        logger.info("call outerInf over");
        InnerObj69 innerObj = getResCodeFor69(chargeCard, encodedPassword, currentAc, deviceNo, totalBalance);
        String[] resCode = innerObj.getCodeRes();
        //	boolean resCodeStatus = innerObj.getResCodeStatus();

        String[] values = new String[13];
        if (currentAc != null) {
            logger.info(currentAc.toString());
            values[0] = id;
            values[1] = serial;

            values[2] = resCode[0];
            values[3] = resCode[1];
            values[4] = currentAc.getCustomerNumber();
            values[5] = totalBalance.toString();
            values[6] = totalBalance.toString();
            values[7] = currentAc.getServiceChargeAccountBalance();
            values[8] = currentAc.getServerAccountAvailableBalance();
            values[9] = currentAc.getCenterTransactionSerialNumber();
            values[10] = currentAc.getBossDocumentsSerialNumber();

            values[11] = this.byteArray2String(data, 98, 8, Type_5);
            values[12] = msgAutCode;

            logger.debug("valuess:" + Arrays.toString(values));
            logger.debug("set BfCenterTrans");
            BfCenterTransT bfTransT = new BfCenterTransT();
            bfTransT.setCenterTransId(currentAc.getCenterTransactionSerialNumber());
            bfTransT.setBossSmsNo(currentAc.getBossDocumentsSerialNumber());
            bfTransT.setAuthDate(new Date());
            bfTransT.setObjId(card);

            bfTransT.setPhoneNumber(chargeCard.getPhone());
            bfTransT.setCustNo(chargeCard.getId());
            logger.info("set bf:" + bfTransT.toString());
            cacheUtil.upBfData(bfTransT);
            // return null;
        } else {
            logger.info("currentAc is null,set values 0");
            String balance = "0.0";
            values[0] = id;
            values[1] = serial;

            values[2] = resCode[0];
            values[3] = resCode[1];
            values[4] = "";
            values[5] = balance;
            values[6] = balance;
            values[7] = balance;
            values[8] = balance;
            values[9] = "";
            values[10] = "";

            values[11] = this.byteArray2String(data, 98, 8, Type_5);
            values[12] = msgAutCode;

        }

        //

        for (int i = 0; i < dtos.length; i++) {
            dtos[i] = new UIDataContentDto();

            dtos[i].setOrder(order_69Flag);

            dtos[i].setName(names[i]);
            dtos[i].setValue(values[i]);
            list.add(dtos[i]);
        }

        return list;

    }

    private String[] getResCodeFor32_69(ChargeCard chargeCard, String password, RechargeBeginResponse currentAc, BigDecimal totalBalance) {
        logger.info("cal resCode.");
        boolean resCodeStatus = false;
        String[] resStr = new String[2];

        if (chargeCard == null) {
            logger.info("user is null");
            resStr[0] = RES_CODE_02;
            resStr[1] = RES_CODE_02_DESC;
            resCodeStatus = false;
            return resStr;
        }
        if (currentAc == null) {
            logger.info("currentAc is null");
            resStr[0] = RES_CODE_09;
            resStr[1] = RES_CODE_09_DESC;
            resCodeStatus = false;
            return resStr;
        }

        if (!password.equalsIgnoreCase(chargeCard.getPassword())) {
            logger.info("password is wrong.");
            resStr[0] = RES_CODE_01;
            resStr[1] = RES_CODE_01_DESC;
            resCodeStatus = false;
            return resStr;
        } else {
            if (Integer.parseInt(chargeCard.getStatus()) == 1) {
                logger.info("getServingStatus  ==1 ");
                if (!checkAccountBalance(chargeCard, totalBalance)) {
                    logger.info("accountBalance < 0.");
                    resStr[0] = RES_CODE_05;
                    resStr[1] = RES_CODE_05_DESC;
                    resCodeStatus = false;
                    return resStr;
                }

                resStr[0] = RES_CODE_00;
                resStr[1] = RES_CODE_00_DESC;
                resCodeStatus = true;
                return resStr;

            } else if (Integer.parseInt(chargeCard.getStatus()) == 2) {
                logger.info("getServingStatus  ==2 ");
                resStr[0] = RES_CODE_04;
                resStr[1] = RES_CODE_04_DESC;
                resCodeStatus = false;
                return resStr;
            } else if (Integer.parseInt(chargeCard.getStatus()) == 0) {
                logger.info("getServingStatus  ==0 ");
                resStr[0] = RES_CODE_06;
                resStr[1] = RES_CODE_06_DESC;
                resCodeStatus = false;
                return resStr;
            } else if (Integer.parseInt(chargeCard.getStatus()) == 3) {
                resStr[0] = RES_CODE_05;
                resStr[1] = RES_CODE_05_DESC;
                resCodeStatus = false;
                return resStr;
            } else {
                logger.info("getServingStatus  not equals 1/2/0/3 ");

                resCodeStatus = false;
                resStr[0] = RES_CODE_03;
                resStr[1] = RES_CODE_03_DESC;
                return resStr;
            }
        }

    }

    private InnerObj69 getResCodeFor69(ChargeCard chargeCard, String password, RechargeBeginResponse currentAc, String deviceNo, BigDecimal totalBalance) {
        logger.info("cal resCode.");
        boolean resCodeStatus = false;
        String[] resStr = new String[2];
        boolean status = checkDeviceStatus(deviceNo);
        if (!status) {
            logger.info("deviceStatus is false.");
            resStr[0] = "100001";
            resStr[1] = "设备维护、暂停使用";

            resCodeStatus = false;
            return new InnerObj69(resCodeStatus, resStr);
        }

        if (chargeCard == null) {
            logger.info("user is null");
            resStr[0] = RES_CODE_02;
            resStr[1] = RES_CODE_02_DESC;
            resCodeStatus = false;
            return new InnerObj69(resCodeStatus, resStr);
        }
        if (currentAc == null) {
            logger.info("currentAc is null");
            resStr[0] = RES_CODE_09;
            resStr[1] = RES_CODE_09_DESC;
            resCodeStatus = false;
            return new InnerObj69(resCodeStatus, resStr);
        }

        if (!password.equalsIgnoreCase(chargeCard.getPassword())) {
            logger.info("password is wrong.");
            resStr[0] = RES_CODE_01;
            resStr[1] = RES_CODE_01_DESC;
            resCodeStatus = false;
            return new InnerObj69(resCodeStatus, resStr);
        } else {
            if (Integer.parseInt(chargeCard.getStatus()) == 1) {
                logger.info("getServingStatus  ==1 ");
                if (!checkAccountBalance(chargeCard, totalBalance)) {
                    logger.info("accountBalance < 0.");
                    resStr[0] = RES_CODE_05;
                    resStr[1] = RES_CODE_05_DESC;
                    resCodeStatus = false;
                    return new InnerObj69(resCodeStatus, resStr);
                }

                resStr[0] = RES_CODE_00;
                resStr[1] = RES_CODE_00_DESC;
                resCodeStatus = true;
                return new InnerObj69(resCodeStatus, resStr);

            } else if (Integer.parseInt(chargeCard.getStatus()) == 2) {
                logger.info("getServingStatus  ==2 ");
                resStr[0] = RES_CODE_04;
                resStr[1] = RES_CODE_04_DESC;
                resCodeStatus = false;
                return new InnerObj69(resCodeStatus, resStr);
            } else if (Integer.parseInt(chargeCard.getStatus()) == 0) {
                logger.info("getServingStatus  ==0 ");
                resStr[0] = RES_CODE_06;
                resStr[1] = RES_CODE_06_DESC;
                resCodeStatus = false;
                return new InnerObj69(resCodeStatus, resStr);
            } else if (Integer.parseInt(chargeCard.getStatus()) == 3) {
                resStr[0] = RES_CODE_05;
                resStr[1] = RES_CODE_05_DESC;
                resCodeStatus = false;
                return new InnerObj69(resCodeStatus, resStr);
            } else {
                logger.info("getServingStatus  not equals 1/2/0/3 ");

                resCodeStatus = false;
                resStr[0] = RES_CODE_03;
                resStr[1] = RES_CODE_03_DESC;
                return new InnerObj69(resCodeStatus, resStr);
            }
        }

    }

    // --------------respose_code in 0x69
    private String RES_CODE_00 = "000000";
    private String RES_CODE_00_DESC = "成功交易";
    private String RES_CODE_01 = "000001";
    private String RES_CODE_01_DESC = "用户密码不正确";
    private String RES_CODE_02 = "000002";
    private String RES_CODE_02_DESC = "用户不合法";
    private String RES_CODE_03 = "000003";
    private String RES_CODE_03_DESC = "用户不可用";
    private String RES_CODE_04 = "000004";
    private String RES_CODE_04_DESC = "用户充电账户无法使用";
    private String RES_CODE_05 = "000005";
    private String RES_CODE_05_DESC = "用户余额不足";
    private String RES_CODE_06 = "000006";
    private String RES_CODE_06_DESC = "用户支付账户停止使用";
    private String RES_CODE_07 = "000007";
    private String RES_CODE_07_DESC = "用户支付账户余额不明";

    private String RES_CODE_09 = "000009";
    private String RES_CODE_09_DESC = "用户支付账户无法查询";
    private String RES_CODE_0A = "00000A";
    private String RES_CODE_0A_DESC = "用户支付账户无法使用";
    private String RES_CODE_11 = "100001";
    private String RES_CODE_11_DESC = "设备维护、暂停使用";

    private String getRandom() {
        Random random1 = new Random(100);
        return String.valueOf(random1.nextInt(100));
    }

    private String encodeCodePass(byte[] password) {
        int length = password.length;
        int validLength = 0;
        for (int i = length - 1; i >= 0; i--) {
            byte b = password[i];
            if (b == 32) {
                if (i != 0) {
                    if (password[i - 1] != 32) {
                        validLength = i;
                        break;
                    } else {
                        continue;
                    }

                } else {
                    validLength = 0;
                }

            }
        }
        byte[] validData = Arrays.copyOf(password, validLength);
        if (validData == null || validData.length == 0) {
            logger.info("received password is null.");

        }
        String pass = type._ascII(validData, null);
        String encodePass = encodeMD5(pass);
        logger.info("encodePass is " + encodePass);
        return encodePass;
    }

    class InnerObj69 {
        boolean resCodeStatus;
        String[] codeRes;

        public InnerObj69(boolean resCodeStatus, String[] codeRes) {
            this.resCodeStatus = resCodeStatus;
            this.codeRes = codeRes;
        }

        public boolean getResCodeStatus() {
            return this.resCodeStatus;
        }

        public String[] getCodeRes() {
            return this.codeRes;
        }
    }

    public static void main(String[] args) {
        String encodeText = encodeMD5("123456");
        System.out.println(encodeText);
    }
}
