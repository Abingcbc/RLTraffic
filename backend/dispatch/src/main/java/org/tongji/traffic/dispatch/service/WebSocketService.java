package org.tongji.traffic.dispatch.service;

import org.springframework.stereotype.Service;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.server.ServerEndpoint;

@Service
@ServerEndpoint("/dispatch/{carId}")
public class WebSocketService {

    @OnOpen
    public void onOpen() {
    }

    @OnClose
    public void onClose() {

    }

    @OnMessage
    public void onMessage(String carId) {

    }
}
