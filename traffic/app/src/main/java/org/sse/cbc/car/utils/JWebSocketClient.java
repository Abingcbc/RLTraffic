package org.sse.cbc.car.utils;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

import static androidx.constraintlayout.widget.Constraints.TAG;

/**
 * Created by ABINGCBC
 * on 2020-04-29
 */
public class JWebSocketClient extends WebSocketClient {

    public JWebSocketClient(URI serverUri) {
        super(serverUri, new Draft_6455());
    }


    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.e(TAG, "WebSocket connected");
    }

    @Override
    public void onMessage(String message) {

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.d(TAG, "WebSocket closed");
    }

    @Override
    public void onError(Exception ex) {

    }
}
