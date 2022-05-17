package com.scorpio.web.service.impl;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.scorpio.web.controller.WechatyController;
import com.scorpio.web.service.ScoketService;
import com.scorpio.web.service.WechatyService;
import com.scorpio.web.vo.request.SendMessageVO;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @Author: Scorpio
 */
@Service
public class ScoketServiceImpl implements ScoketService {

    private static SocketIOServer socketIOServer = null;

    @Resource
    private WechatyController wechatyController;

    @Resource
    private WechatyService wechatyService;

    @PostConstruct
    public void postConstruct(){
        Configuration configuration = new Configuration();
        configuration.setHostname("127.0.0.1");
        configuration.setPort(9999);

        socketIOServer = new SocketIOServer(configuration);

        socketIOServer.addEventListener("getContactList", String.class, (client, data, ackRequest) -> {
            ackRequest.sendAckData(JSON.toJSONString(wechatyController.getContactList()));
        });

        socketIOServer.addEventListener("getMessageList", String.class, (client, data, ackRequest) -> {
            ackRequest.sendAckData(JSON.toJSONString(wechatyController.getMessageList(data)));
        });

        socketIOServer.addEventListener("sendMessage", SendMessageVO.class, (client, data, ackRequest) -> {
            wechatyService.sendText(data.getContent(), data.getId());
            ackRequest.sendAckData("success");
        });


        socketIOServer.start();
    }

    @Override
    public SocketIOServer getSocketIOServer() {
        return socketIOServer;
    }
}
