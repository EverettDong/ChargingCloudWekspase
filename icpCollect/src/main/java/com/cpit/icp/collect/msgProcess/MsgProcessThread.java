package com.cpit.icp.collect.msgProcess;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cpit.common.SpringContextHolder;
import com.cpit.icp.collect.coderDecoder.messageParse.configurable.ConfigureDecode;
import com.cpit.icp.collect.utils.cache.DeviceCacheUtil;
import com.cpit.icp.dto.collect.GateRouteInfoDto;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;

/**
 * 处理一条报文
 * 类名称：MsgProcessThread
 * 类描述：
 * 创建人：zhangqianqian
 * 创建时间：2018年8月2日 下午2:08:52
 * 修改人：
 * 修改时间：
 * 修改备注：
 *
 * @version 1.0.0
 */
public class MsgProcessThread implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(MsgProcessThread.class);
    private MsgOfPregateDto obj = null;
    private DeviceCacheUtil deviceCacheUtil;

    public MsgProcessThread(MsgOfPregateDto dto) {
        this.obj = dto;
        deviceCacheUtil = SpringContextHolder.getBean(DeviceCacheUtil.class);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            logger.info("new thread start.");
            logger.debug("new thread start." + obj.toString());
            process(obj);
        } catch (Exception e) {
            // TODO Auto-generated catch block

            logger.error(e.getMessage());
        }

    }


    private void process(MsgOfPregateDto obj) {
        logger.info("thread process begin");
        GateRouteInfoDto gateDto = deviceCacheUtil.getGateRouteInfo(obj.getDeviceNo());
        if (gateDto != null) {
            String version = gateDto.getDeviceVersion();
            if (version != null &&
                    !version.equalsIgnoreCase("") &&
                    !version.equalsIgnoreCase(obj.getMsgVersion())) {
                obj.setMsgVersion(version);
                logger.info("version in gateInfo doesnot equals version in Msg.");
            }
        }
        String code = obj.getMsgCode();
        String version = obj.getMsgVersion();
        logger.info("process code is: " + code + " process version is: " + version);

        Map<List<String>, MsgProcessInterface> processMap = ParseMsgProcessorXml.getInstance().getVersionMap(version);
        if (!processMap.isEmpty()) {
            // if(!proMap.isEmpty()){
            Iterator iter = processMap.entrySet().iterator();

            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                List<String> codes = (List<String>) entry.getKey();
                MsgProcessInterface val = (MsgProcessInterface) entry.getValue();

                if (codes.contains(code)) {
                    try {

                        val.msgProcess(obj);


                    } catch (Throwable e) {
                        logger.error("process error", e);

                    }

                }
            }
        }

    }
}
