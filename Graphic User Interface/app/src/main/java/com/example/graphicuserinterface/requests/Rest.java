package com.example.graphicuserinterface.requests;

import android.util.Log;

import com.example.graphicuserinterface.MainActivity;
import com.example.graphicuserinterface.exception.WrongCodeException;
import com.example.graphicuserinterface.objects.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Rest implements Serializable {

    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static final OkHttpClient client = new OkHttpClient.Builder().cache(null).build();
    String token;

    public Rest(String token) {
        this.token = token;
    }

    public Rest() {
        this("");
    }

    public String getToken() {
        return token;
    }

    public CompletableFuture<Boolean> login(String username, String password){

        return CompletableFuture.supplyAsync( () -> {
            JSONObject jsonCredentials = new JSONObject();
            try {
                jsonCredentials.put("username", username);
                jsonCredentials.put("password", password);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            RequestBody body = RequestBody.create(MediaType.get("application/json"), jsonCredentials.toString());
            Request request = new Request.Builder()
                    .url("https://andra.lucaci32u4.xyz/api/login")
                    .post(body)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Log.i("Code", String.valueOf(response.code()));
                if(response.code() != 200){
                    Log.i("Wrong Code", String.valueOf(response.code()));
                    response.close();
                    return false;
                } else{
                    token = response.body().string();
                    response.close();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        } , executor);

    }

    public CompletableFuture<Boolean> addDevice(String newDeviceId){
        return CompletableFuture.supplyAsync(() -> {
            RequestBody body = RequestBody.create(MediaType.get("application/json"), "");
            Request request = new Request.Builder()
                    .url("https://andra.lucaci32u4.xyz/api/add/" + newDeviceId)
                    .addHeader("TokenAuth", token)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();
                if(response.code() != 200){
                    Log.e("Wrong Code Add Route", String.valueOf(response.code()));
                    response.close();
                    return false;
                } else {
                    response.close();
                    return true;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

        }, executor);
    }

    public CompletableFuture<List<Socket>> getSocketList(){
        return CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url("https://andra.lucaci32u4.xyz/api/list")
                    .addHeader("TokenAuth", token)
                    .get()
                    .build();

            try {
                List<Socket> listaSocketServer = new ArrayList<Socket>();
                Response getListResponse = client.newCall(request).execute();
                if( getListResponse.code() == 200) {
                    JSONArray arrayId = new JSONArray(getListResponse.body().string());
                    for(int i = 0; i < arrayId.length(); i++){
                        String idSocketState = arrayId.getString(i);
                        Request requestState = new Request.Builder()
                                .url("https://andra.lucaci32u4.xyz/api/socket/" + idSocketState)
                                .addHeader("TokenAuth", token)
                                .get()
                                .build();

                        Response responseState = client.newCall(requestState).execute();
                        if(responseState.code() != 200){
                            Log.e("Wrong Code State", String.valueOf(responseState.code()));
                            responseState.close();
                            return null;
                        }

                        JSONArray socketStates = new JSONArray(responseState.body().string());
                        responseState.close();
                        boolean[] states = new boolean[4];
                        for( int j = 0; j < 4; j++){
                            states[j] = socketStates.getBoolean(j);
                        }
                        Socket newSocket = new Socket(arrayId.getString(i), states);
                        listaSocketServer.add(newSocket);
                    }
                    getListResponse.close();
                    return listaSocketServer;
                }
                return null;

            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null;
            }
        }, executor);
    }



    public CompletableFuture<Boolean> postStateList(Socket socket){
        return CompletableFuture.supplyAsync(() -> {
            JSONArray jsonStates = new JSONArray();
            for(int i =0; i < 4; i++){
                jsonStates.put(socket.getStateIndex(i));
            }
            RequestBody body = RequestBody.create(MediaType.get("application/json"), jsonStates.toString());
            Request request = new Request.Builder()
                    .url("https://andra.lucaci32u4.xyz/api/socket/" + socket.getId())
                    .addHeader("TokenAuth", token)
                    .post(body)
                    .build();
            try {
                Response responseSocketState = client.newCall(request).execute();
                if( responseSocketState.code() == 200){

                    responseSocketState.close();
                    return true;
                }
                responseSocketState.close();
                return false;

            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }, executor);
    }

}
