package com.cpit.icp.collect.msgProcess;

import static com.cpit.icp.collect.utils.Consts.VERSION_3_5;
import static com.cpit.icp.collect.utils.Consts.VERSION_3_1;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.hutool.core.bean.BeanUtil;
import com.cpit.icp.collect.utils.Consts;
import com.cpit.icp.dto.charging.ChargePileDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.cpit.common.Encodes;
import com.cpit.common.JsonUtil;
import com.cpit.common.SpringContextHolder;
import com.cpit.common.TimeConvertor;
import com.cpit.icp.collect.coderDecoder.common.configurable.deviceInfo;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureDecode;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureEncode;
import com.cpit.icp.collect.facade.ResourceFacade;
import com.cpit.icp.collect.impl.GateRouteInfoMgmt;
import com.cpit.icp.collect.service.MonDBService;
import com.cpit.icp.collect.service.MsgSend;
import com.cpit.icp.collect.utils.CodeTransfer;
import com.cpit.icp.collect.utils.cache.CacheUtil;
import com.cpit.icp.collect.utils.cache.DeviceCacheUtil;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
import com.cpit.icp.dto.collect.MonChargingStatus4App;
import com.cpit.icp.dto.collect.MonSequenceDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;
import com.cpit.icp.dto.common.ResultInfo;
import com.cpit.icp.dto.resource.BrBatterycharge;



/**
 * register
 *
 * @author zhangqianqian
 */
public class RegisterMsgProImp implements MsgProcessInterface {
    private final static Logger logger = LoggerFactory.getLogger(RegisterMsgProImp.class);
    private GateRouteInfoMgmt gateRouteInfoImp;

    private ConfigureEncode configureEncode;
    private ConfigureDecode configureDecode;
    private MsgSend msgSend;
    private MonDBService monDBService;

    private String receivedTime;
    private CacheUtil cacheUtil;
    private DeviceCacheUtil deviceCacheUtil;

    private ResourceFacade resFacade;


//	private MonSequenceDto monSequenceDto;

    {
        configureDecode = SpringContextHolder.getBean(ConfigureDecode.class);
        configureEncode = SpringContextHolder.getBean(ConfigureEncode.class);
        gateRouteInfoImp = SpringContextHolder.getBean(GateRouteInfoMgmt.class);

        msgSend = SpringContextHolder.getBean(MsgSend.class);
        monDBService = SpringContextHolder.getBean(MonDBService.class);

        cacheUtil = SpringContextHolder.getBean(CacheUtil.class);
        deviceCacheUtil = SpringContextHolder.getBean(DeviceCacheUtil.class);
        resFacade = SpringContextHolder.getBean(ResourceFacade.class);
    }


    @Override
    public void msgProcess(Object obj) {
        // TODO Auto-generated method stub
        doMsgProcess((MsgOfPregateDto) obj);
    }


    private void doMsgProcess(MsgOfPregateDto dto) {
        logger.info(" register doMsgProcess : ");
        receivedTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
        deviceInfo deviceInfoData = configureDecode.Decode10MsgStyle(Encodes.decodeHex(dto.getMsgData()));
        if (deviceInfoData.getErrorMsg() != null) {
            logger.info("decode10 wrong");

            return;
        }
        if (!deviceInfoData.getDeviceCode().equalsIgnoreCase(dto.getDeviceNo())) {
            String deviceNo = deviceInfoData.getDeviceCode();
            dto.setDeviceNo(deviceNo);
            logger.info("deviceNo in gateway not equals in msg.");
        }
        String[] decodedData = configureDecode.Decode10Package(Encodes.decodeHex(dto.getMsgData()), "", 1);
        storeOriginalData(dto, decodedData);
        String deviceNo = deviceInfoData.getDeviceCode();
        logger.debug(" store 10 data success");

        boolean isRes = false;
        //String versionInRes = null;
        String deviceVersion = null;
        int chargeType = -1;
        ChargePileDetail bbCharge = checkResource(dto.getDeviceNo());
        if (bbCharge == null) {
            logger.info("checkinResource get null.");
            isRes = false;
        } else {
            //versionInRes =bbCharge.getBackCommunicatVs();
            deviceVersion = convertVersionInRes("7");

            //获取充电机类型
            chargeType = Integer.parseInt(bbCharge.getChargeType());
            isRes = true;

        }

        logger.info("deviceNo" + dto.getDeviceNo() + "  " + deviceVersion + " " + isRes);


        if (isRes) {

            upCPStatusData(deviceNo, chargeType);
            if (deviceVersion != null &&
                    !deviceVersion.equalsIgnoreCase("") &&
                    !deviceVersion.equalsIgnoreCase(dto.getMsgVersion())) {
                dto.setMsgVersion(deviceVersion);
                logger.info("changeVersion to versionInRes" + deviceVersion);
            }
            logger.info("begin to update GateRouteInfo");
            GateRouteInfoDto gateRouteInfo = new GateRouteInfoDto();
            gateRouteInfo.setDeviceNo(deviceNo);
            gateRouteInfo.setPreGateIp(dto.getPreGateIp());
            gateRouteInfo.setPreGatePort(dto.getPreGatePort());
            gateRouteInfo.setChangeTime(TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR));
            gateRouteInfo.setDeviceVersion(dto.getMsgVersion());
            gateRouteInfoImp.updateGateRouteInfo(gateRouteInfo);

            deviceCacheUtil.upGateRouteInfo(deviceNo, gateRouteInfo);

            if (dto.getMsgVersion().equalsIgnoreCase(VERSION_3_5)) {
                logger.info("set deviceCodeMode is redis");
                String currentCM = deviceCacheUtil.getDeviceCodeMode(dto.getDeviceNo());
                String cmInMsg = deviceInfoData.getChineseEncoding();
                logger.info("currentCM : " + currentCM + " cmInMsg: " + cmInMsg);
                if (currentCM == null || !currentCM.equalsIgnoreCase(cmInMsg)) {
                    deviceCacheUtil.setDeviceCodeMode(deviceNo, cmInMsg);
                    logger.info("device:" + dto.getDeviceNo() + " update codeMode success");
                }

            }
            if (dto.getMsgVersion().equalsIgnoreCase(VERSION_3_1)) {
                logger.info("3.1 does not reply 10Msg,return");
                return;
            }

            sendRegisterReply(dto);

        } else {
            logger.info("device is not in res,notifyPreGateWay");
            msgSend.msgSendToPreG(dto, true);

        }

    }


    private void sendRegisterReply(MsgOfPregateDto dto) {
        logger.info("begin to reply,get sequenceData");

        MonSequenceDto monSequenceDto = cacheUtil.getSequence(dto.getDeviceNo());
        logger.info("sequencedata: " + monSequenceDto.toString());
        byte[] sendData = configureEncode.encode09PackageContent(dto.getDeviceNo(), "", dto.getMsgVersion(), monSequenceDto);
        logger.debug("reply data contruct");
        MsgOfPregateDto reply = new MsgOfPregateDto();
        reply.setDeviceNo(dto.getDeviceNo());
        reply.setMsgCode("0x09");
        reply.setMsgVersion(dto.getMsgVersion());
        reply.setPreGateIp(dto.getPreGateIp());
        reply.setPreGatePort(dto.getPreGatePort());
        String replyData = CodeTransfer.byteArrayToHexStr(sendData);
        reply.setMsgData(replyData);
        logger.info("reply of register storeIn database.");
        String currentTime = TimeConvertor.getDate(TimeConvertor.FORMAT_MINUS_24HOUR);
        monDBService.storeOriginalData(reply, 1, 0, "", currentTime, "");
        msgSend.msgSendToPreG(reply, false);
    }

    private BrBatterycharge checkinResource(String deviceNo) {
        logger.info("check deviceNo exist." + deviceNo);
        ResultInfo result = resFacade.getDeviceNoInfo(deviceNo);
        if (result.getResult() == ResultInfo.OK) {
            logger.info("call outer inf response ok");
            Map<String, Serializable> map = new HashMap<String, Serializable>();
            map = (Map<String, Serializable>) (result.getData());
            try {
                logger.debug("json mkList begins");
                List<BrBatterycharge> list = JsonUtil.mkList((List) map.get("infoList"), BrBatterycharge.class);
                if (!list.isEmpty() && list.size() != 0) {
                    logger.debug("begin to convert to BrBatterycharge");
                    BrBatterycharge b = null;
                    b = (BrBatterycharge) list.get(0);

                    if (b == null) {
                        logger.info("convert to BrBatterycharge failed");
                        return null;
                    } else {
                        logger.debug("convert to BrBatterycharge ok");

                        return b;
                    }

                } else {
                    logger.info("map get infoList is null ,return null");
                    return null;
                }
            } catch (Exception e) {
                logger.info("convert error . " + e.getMessage());
                return null;
            }
        } else {
            logger.info("call outer inf response failed.");
            return null;
        }


    }

    /**
     * @Author donghaibo
     * @Description //调用资源获取协议版本号
     * @Date
     * @Param
     * @return
     **/
    private ChargePileDetail checkResource(String deviceNo) {
        logger.info("check deviceNo exist." + deviceNo);
        ResultInfo result = resFacade.getDeviceNoInfo(deviceNo);
        if (result.getResult() == ResultInfo.OK) {
            logger.info("call outer inf response ok");
            ChargePileDetail chargePileDetail = BeanUtil.toBean(result.getData(), ChargePileDetail.class);
            if (chargePileDetail == null) {
                logger.info("convert to BrBatterycharge failed");
                return null;
            } else {
                logger.debug("convert to BrBatterycharge ok");
                return chargePileDetail;
            }
        }
        logger.info("call outer inf response failed.");
        return null;
    }

    private boolean storeOriginalData(MsgOfPregateDto dto, String[] decodedData) {
        logger.info("storeOriginalData begins:");
        if (decodedData.length != 2) {
            logger.info("decoded data is wrong.");
            return false;
        }
        int tr;
        String trc;

        if (!decodedData[0].equalsIgnoreCase("")) {
            tr = 1;
            trc = decodedData[0];
        } else {
            tr = 0;
            trc = null;
        }

        monDBService.storeOriginalData(dto, 2, tr, trc, receivedTime, decodedData[1]);
        logger.info("storeOriginalData ends.");
        return true;
    }

    /**
     * enabled isonline yes/ is ac交流/is dc直流
     *
     * @param deviceNo chargeType (value="充电机类型",example = "1 交流充电机\n" +
     *                 "2 直流充电机\n" +
     *                 "3 交直流一体机\n" +
     *                 "4 充放电一体机 直流")
     */
    private void upCPStatusData(String deviceNo, int chargeType) {
        logger.info("upCPStatus " + deviceNo);

        logger.debug("chargeType " + chargeType);
        MonChargingStatus4App statusData = cacheUtil.getStatusData(deviceNo);
        statusData.setEnabled("yes");
        statusData.setIsOnline("yes");
        if (chargeType == 1 || chargeType == 3) {
            statusData.setIsAC("yes");
        }
        if (chargeType == 2 || chargeType == 3 || chargeType == 4) {
            statusData.setIsDC("yes");
        }
        chargeType = -1;
        cacheUtil.setStatusData(statusData);


    }

    private String convertVersionInRes(String versionInRes) {
        String version = null;
        if (versionInRes == null || versionInRes.equalsIgnoreCase("")) {
            logger.info("versionInRes is null,return default 3.4");
            return "3.4";
        }
        switch (versionInRes) {
            case "1":
                version = "2.6";
                break;
            case "2":
                version = "2.8";
                break;
            case "3":
                version = "3.0";
                break;
            case "4":
                version = "3.2";
                break;
            case "5":
                version = "3.4";
                break;
            case "6":
                version = "3.1";
                break;
            case "7":
                version = "3.5";
                break;
            default:
                version = "3.4";
                break;

        }
        return version;
    }

}
