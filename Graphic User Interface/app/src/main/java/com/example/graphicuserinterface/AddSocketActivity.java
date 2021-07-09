package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.graphicuserinterface.exception.WrongCodeException;
import com.example.graphicuserinterface.objects.Socket;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddSocketActivity extends AppCompatActivity {

    EditText edIdNewSocket;
    Button btnAddIdSocket;
    Button btnBack;
    Intent intent;
    public static final String ADD_Socket = "addSocket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_socket);
        edIdNewSocket = findViewById(R.id.etIdSocket);
        btnAddIdSocket = findViewById(R.id.btnAdauga);
        btnBack = findViewById(R.id.btnBack);
        intent = getIntent();

        btnAddIdSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edIdNewSocket.getText().toString().isEmpty()){
                    edIdNewSocket.setError("Introduceti id priza fizica!");
                } else {
                    Socket socket = new Socket(edIdNewSocket.getText().toString());

                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try  {
                                String newId = edIdNewSocket.getText().toString();
                                postDevice(newId);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    thread.start();
                    intent.putExtra(ADD_Socket, socket);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSocketActivity.this, SelectSocketActivity.class);
                startActivity(intent);
            }
        });
        
    }

    private void postDevice(String newDeviceId){
//        OkHttpClient client = new OkHttpClient.Builder().build();
//        RequestBody body = RequestBody.create(MediaType.get("application/json"), newDeviceId);
//        Request request = new Request.Builder()
//                .url("https://andra.lucaci32u4.xyz/api/add/:device")
//                .addHeader("TokenAuth", MainActivity.header)
//                .post(body)
//                .build();
//        try {
//            Response response = client.newCall(request).execute();
//            String msg = String.valueOf(response.code());
//            System.out.println(msg);
//            Log.i("Code: ", String.valueOf(response.code()));
//            response.close();
//            if (response.code() != 200) {
//
//                Log.e("WrongCode ", msg);
//                System.out.println(msg);
//                throw new WrongCodeException();
//            } else {
//                header = response.body().toString();
//                Intent intent = new Intent(MainActivity.this, SelectSocketActivity.class);
//                startActivity(intent);
//
//            }
//        } catch (IOException exception) {
//            String msg = exception.getMessage();
//            System.out.println(msg);
//            throw new WrongCodeException();
//        }
    }
}