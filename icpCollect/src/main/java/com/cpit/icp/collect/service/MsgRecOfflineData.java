package com.cpit.icp.collect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cpit.icp.collect.msgProcess.MsgDataConvertor;
import com.cpit.icp.collect.msgProcess.MsgRecievedObservable;
import com.cpit.icp.dto.collect.msgProcess.MsgOfPregateDto;

/**
 * 收取离线文件的数据
 *
 * @author zhangqianqian
 */
@Service
public class MsgRecOfflineData {
    private final static Logger logger = LoggerFactory.getLogger(MsgRecOfflineData.class);
    @Autowired
    MsgDataConvertor msgDataConvertor;

    public void receivedOfflineData(MsgOfPregateDto dto) {
        logger.debug("receivedOfflineData " + dto.toString());
        //MsgOfPregateDto obj = msgDataConvertor.preGateStrToObj(str);
        MsgRecievedObservable.getInstance().addData(dto);
    }
}
