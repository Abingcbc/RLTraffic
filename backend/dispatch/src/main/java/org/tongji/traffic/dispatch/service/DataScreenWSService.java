package org.tongji.traffic.dispatch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint("/dispatch/{timestamps}")
public class DataScreenWSService {

    public static ConcurrentHashMap<String, Session> sessionMap = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("timestamps") String timestamps) {
        if (session != null) {
            sessionMap.put(timestamps, session);
            log.info("web " + timestamps + " connect success!!!");
        }
    }

    @OnClose
    public void onClose(@PathParam("timestamps") String timestamps) {
        sessionMap.remove(timestamps);
        log.info("web " + timestamps + " connect close!!!");
    }

    @OnMessage
    public void onMessage(String carId) {
    }

    public void sendMessage(String timeStamp) {
        try {
            log.info("start sync to " + timeStamp);
            for (Map.Entry<String, Session> session: sessionMap.entrySet()) {
                if (session.getValue().isOpen()) {
                    log.info(session.getKey());
                    session.getValue().getBasicRemote().sendText(timeStamp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
