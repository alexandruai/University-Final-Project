package com.andra2699.ssocket;

import com.andra2699.ssocket.hardware.HardwareLayer;
import com.andra2699.ssocket.hardware.RealHardwareLayer;
import com.andra2699.ssocket.hardware.VirtualHardwareLayer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static final Semaphore resetSocket = new Semaphore(0);
    public static HardwareLayer hwlayer;

    public static void main(String [] args) throws InterruptedException {
        try {
            hwlayer = new RealHardwareLayer(4);
        } catch (Exception e) {
            hwlayer = new VirtualHardwareLayer(4);
        }

        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.MILLISECONDS).build();

        while (true) {
            Request request = new Request.Builder()
                    .url("wss://andra.lucaci32u4.xyz/api/updates")
                    .build();

            client.newWebSocket(request, new WebSocketHandler());
            resetSocket.acquire();
        }


    }

}
