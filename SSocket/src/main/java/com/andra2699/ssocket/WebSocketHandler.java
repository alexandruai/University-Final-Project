package com.andra2699.ssocket;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;
import org.json.JSONArray;

public class WebSocketHandler extends WebSocketListener {

    @Override public void onOpen(WebSocket webSocket, Response response) {
        // nothing
    }

    @Override public void onMessage(WebSocket webSocket, String text) {
        JSONArray arr = new JSONArray(text);
        Main.hwlayer.mutate(sm -> {
            for (int j = 0; j < 4; j++) {
                sm.set(j, arr.getBoolean(j));
            }
        });
    }


    @Override public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
        Main.resetSocket.release();
    }

    @Override public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        t.printStackTrace();
        webSocket.close(1000, null);
    }

}
