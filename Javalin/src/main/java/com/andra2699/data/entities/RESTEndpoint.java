package com.andra2699.data.entities;

import com.andra2699.ApplicationContext;
import com.andra2699.data.DataRepository;
import com.andra2699.data.DataRepositoryFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsContext;
import org.eclipse.jetty.util.ajax.JSONEnumConvertor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class RESTEndpoint {
    private final Map<String, WsContext> socketIdMap = new ConcurrentHashMap<>();

    private final Javalin server;
    private final ApplicationContext appcon;
    private final DataRepositoryFactory data;

    public RESTEndpoint(ApplicationContext applicationContext) {
        this.appcon = applicationContext;
        server = Javalin.create(javalinConfig -> {
            javalinConfig.defaultContentType = "application/json";
        });
        data = appcon.getDataRepositoryFactory();
    }

    public void startWebserver() {

        /*
         * json content: { "username": "username", "password": "password" }
         * if auth ok, response token
         * else response code 403
         */
        server.post("/api/login", ctx -> {
            String username;
            String password;
            try {
                JSONObject obj = new JSONObject(ctx.body());
                username = obj.getString("username");
                password = obj.getString("password");
            } catch (JSONException e) {
                ctx.status(400); // go to hell
                return;
            }
            if (password == null || username == null) {
                ctx.status(400); // go to hell
                return;
            }
            var dr = data.create();
            var user = dr.getUserByUsername(username);
            if (user.isEmpty()) {
                ctx.status(403); // permission denied
                return;
            }
            if (!password.equals(user.get().getPassword())) {
                ctx.status(403); // permission denied
                return;
            }
            dr.beginTransaction();
            var token = user.get().regenerateToken();
            dr.commitTransaction();
            dr.close();

            ctx.result(token);
        });

        /*
         * TokenAuth header with token from /api/login
         * response: json
         *      [id1, id2, id3, ...]
         * if permission denied, 403
         */
        server.get("/api/list", ctx -> {
            var optuser = getUserAuth(ctx);
            if (optuser.isEmpty()) {
                ctx.status(403);
                return;
            }
            User user = optuser.get();

            JSONArray arr = new JSONArray();

            var dr = data.create();
            dr.getUserByID(user.getId()).ifPresent(u -> u.getDevices().forEach(d -> arr.put(d.getId())));
            dr.close();
            ctx.result(arr.toString());
        });

        /*
         * TokenAuth header with token from /api/login
         * response: nothing
         * if permission denied or device offline, 403
         */
        server.post("/api/add/:device", ctx -> {
            var id = ctx.pathParam("device");
            if (id == null || id.isEmpty() || id.isBlank()) {
                ctx.status(403);
                return;
            }
            var optuser = getUserAuth(ctx);
            if (optuser.isEmpty()) {
                ctx.status(403);
                return;
            }
            User user = optuser.get();

            var dr = data.create();
            var optdevice = dr.getDeviceByID(id);
            if (optdevice.isEmpty()) {
                dr.close();
                ctx.status(403);
                return;
            }
            dr.beginTransaction();
            dr.getUserByID(user.getId()).ifPresent(u -> {
                u.addDevice(optdevice.get());
            });
            dr.commitTransaction();
            dr.close();
        });


        /*
         * TokenAuth header with token from /api/login
         * response: [true, false, true, true] -> always 4
         * if permission denied 403
         * if device offline, set state silently (do nothing special)
         */
        server.get("/api/socket/:sockid", ctx -> {
            String sockid = ctx.pathParam("sockid");
            if (sockid.isEmpty() || sockid.isBlank()) {
                ctx.status(403);
                return;
            }
            var optuser = getUserAuth(ctx);
            if (optuser.isEmpty()) {
                ctx.status(403);
                return;
            }
            User user = optuser.get();

            var dr = data.create();
            optuser = dr.getUserByID(user.getId());
            var optdevice = dr.getDeviceByID(sockid);
            if (optuser.isEmpty() || optdevice.isEmpty() || !optuser.get().getDevices().contains(optdevice.get())) {
                ctx.status(403);
                return;
            }
            JSONArray array = new JSONArray();
            for (int i = 0; i < 4; i++) {
                array.put(optdevice.get().getState(i));
            }
            dr.close();
            ctx.result(array.toString());
        });

        /*
         * TokenAuth header with token from /api/login
         * content: [true, false, true, true] -> always 4
         * response: nothing
         * if permission denied, 403
         * if device offline, set state normally
         */
        server.post("/api/socket/:sockid", ctx -> {
            String sockid = ctx.pathParam("sockid");
            if (sockid.isEmpty() || sockid.isBlank()) {
                ctx.status(403);
                return;
            }

            String content = ctx.body();
            JSONArray arr;
            try {
                arr = new JSONArray(content);
                if (arr.length() != 4) {
                    ctx.status(403);
                    return;
                }
                for (int i = 0; i < 4; i++) {
                    arr.getBoolean(i);
                }
            } catch (JSONException je) {
                ctx.status(403);
                return;
            }

            var optuser = getUserAuth(ctx);
            if (optuser.isEmpty()) {
                ctx.status(403);
                return;
            }
            User user = optuser.get();

            var dr = data.create();
            optuser = dr.getUserByID(user.getId());
            var optdevice = dr.getDeviceByID(sockid);
            if (optuser.isEmpty() || optdevice.isEmpty() || !optuser.get().getDevices().contains(optdevice.get())) {
                ctx.status(403);
                return;
            }

            dr.beginTransaction();
            for (int i = 0; i < 4; i++) {
                optdevice.get().setState(i, arr.getBoolean(i));
            }
            dr.commitTransaction();

            dr.close();
        });

        /*
         * SocketId header contains socket id
         */
        server.ws("/api/updates", ws -> {
            ws.onConnect(ctx -> {
                String id = ctx.header("SocketId");
                if (id == null || id.isEmpty() || id.isBlank() || id.length() != 36) {
                    return;
                }
                socketIdMap.put(id, ctx);
            });
            ws.onMessage(ctx -> {
                // nothing
            });
            ws.onClose(ctx -> {
                String id = ctx.header("SocketId");
                if (id == null || id.isEmpty() || id.isBlank() || id.length() != 36) {
                    return;
                }
                socketIdMap.remove(id);
            });
        });

        server.start(5555);
    }

    public void sendState(String socketId, String json) {
        if (socketIdMap.containsKey(socketId)) {
            WsContext ws = socketIdMap.get(socketId);
            ws.send(json + "\n");
        }
    }

    private Optional<User> getUserAuth(Context ctx) {
        String header = ctx.header("TokenAuth");
        if (header == null || header.isEmpty() || header.isBlank()) {
            return Optional.empty();
        }
        var dr = data.create();
        var optuser = dr.getUserByToken(header);
        dr.close();
        return optuser;
    }

}
