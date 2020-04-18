package org.tongji.traffic.dispatch.service;

import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

@Component
@ServerEndpoint("/dispatch")
public class WebSocketService {

    private static Session appSession;

    @OnOpen
    public void onOpen(Session session) {
        if (session != null) {
            appSession = session;
            System.out.println("WS connect success!!!");
        }
    }

    @OnClose
    public void onClose() {
        System.out.println("WS connect close!!!");
    }

    @OnMessage
    public void onMessage(String carId) {
    }

    public void sendMessage(String timeStamp) {
        try {
            if (appSession != null) {
                System.out.println(timeStamp);
                appSession.getBasicRemote().sendText(timeStamp);
            } else {
                System.out.println("empty session");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
