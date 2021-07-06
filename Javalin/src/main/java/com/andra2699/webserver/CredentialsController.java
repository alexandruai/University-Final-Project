package com.andra2699.webserver;

import io.javalin.http.Context;

public class CredentialsController {

   static String username = "Admin";
   static  String pwd = "12345";

    public static void primesteCredentiale(Context context) {
        String body = context.body();
       if(body.equals( username + " " + pwd)) {
            context.result("Credentiale corecte");
        } else {
            context.result("Credentiale incorecte");
        }
    }

}
