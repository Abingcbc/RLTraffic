package org.tongji.traffic.dispatch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jdk.nashorn.internal.ir.debug.JSONWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tongji.traffic.dispatch.domain.TrafficOrder;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/order/{timestamps}")
public class AndroidWSService {
    public static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("timestamps") String timestamps) {
        if (session != null) {
            sessionMap.put(timestamps, session);
            log.info("android " + timestamps + " connect success!!!");
        }
    }

    @OnClose
    public void onClose(@PathParam("timestamps") String timestamps) {
        sessionMap.remove(timestamps);
        log.info("android " + timestamps + " connect close!!!");
    }

    @OnMessage
    public void onMessage(String carId) {
    }

    public void sendMessage(TrafficOrder trafficOrder) {
        try {
            log.info("start order to " + trafficOrder.getReqId());
            for (Map.Entry<String, Session> session: sessionMap.entrySet()) {
                if (session.getValue().isOpen()) {
                    log.info(session.getKey());
                    session.getValue().getBasicRemote().sendText(new ObjectMapper()
                            .writeValueAsString(trafficOrder));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
