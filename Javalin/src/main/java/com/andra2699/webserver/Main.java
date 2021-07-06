package com.andra2699.webserver;

import com.andra2699.ApplicationContext;
import com.andra2699.data.DataRepository;
import com.andra2699.data.entities.Device;
import com.andra2699.data.entities.User;
import io.javalin.Javalin;

import java.util.Optional;
import java.util.UUID;

public class Main {

    public static void main(String[] args) {

        /*Javalin serverApp = Javalin.create().start(8981);
        //Restapi
        serverApp.get("/", ctx -> { ctx.result("Se conecteaza utilizatorul...");});
        serverApp.get("/credentials/", ctx -> { ctx.result("Introduceti credentialele...");
            System.out.println("Introduceti credentialele");});

        //Websocket priza
        serverApp.ws("/", ws -> {
            ws.onConnect( ctx -> System.out.println("Connected"));

            ws.onClose( ctx -> System.out.println("Connection closed..."));
        });

        //verificare restapi
        serverApp.get("/websocketclient/", ctx -> {});*/


        ApplicationContext applicationContext = new ApplicationContext();

        applicationContext.getRestEndpoint().startWebserver();

        DataRepository data = applicationContext.getDataRepositoryFactory().create();

        if (false) {
            User u1 = new User(applicationContext,"lucaci32u4", "aaa123");
            data.beginTransaction();
            data.save(u1);
            data.commitTransaction();
        }

        if (false) {

            data.beginTransaction();
            data.save(new Device(applicationContext, "dfgfdhfg"));
            data.save(new Device(applicationContext, "adfghjk"));
            data.save(new Device(applicationContext, "qwyuiop"));
            data.save(new Device(applicationContext, "zxcvbnm"));
            data.commitTransaction();
        }

        data.close();

    }

}
