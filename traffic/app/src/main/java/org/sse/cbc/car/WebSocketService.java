package org.sse.cbc.car;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.alibaba.idst.nls.nlsclientsdk.transport.javawebsocket.JWebSocketClient;

public class WebSocketService extends Service {

    public JWebSocketClient client;
    private WebSocketClientBinder clientBinder = new WebSocketClientBinder();

    public WebSocketService() {
    }

    class WebSocketClientBinder extends Binder {
        public WebSocketService getService() {
            return WebSocketService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return clientBinder;
    }
}
