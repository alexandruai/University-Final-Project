package com.example.graphicuserinterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graphicuserinterface.objects.Socket;
import com.example.graphicuserinterface.requests.Rest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class SelectSocketActivity extends AppCompatActivity {

    public static final String Switch_State = "switch";
    public static final int SWITCH_CODE = 200;
    ListView listView;
    List<Socket> socketList;
    int poz;
    public static final String SAVE_SOCKET_LIST = "saveSockets";
    String selectSocket = "Selectare Priza";
    TextView tvSelect;
    Executor executor;
    Rest rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_socket);
        Button btnSchimbaStarePriza = findViewById(R.id.btnSwitchState);
        Button btnAdaugaPrizaNoua = findViewById(R.id.btnPrizaNoua);
        Button btnDelogare = findViewById(R.id.btnDelogare);
        socketList = new ArrayList<Socket>();
        listView = findViewById(R.id.listViewSockets);
        tvSelect = findViewById(R.id.tvPriza);
        tvSelect.setText(selectSocket);
        executor = ContextCompat.getMainExecutor(this);
        rest = new Rest(getIntent().getStringExtra("Token"));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                poz = position;
            }
        });

        btnSchimbaStarePriza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSocketActivity.this, SwitchSocketStateActivity.class);
                intent.putExtra(Switch_State, socketList.get(poz));
                intent.putExtra("Token", rest.getToken());
                startActivityForResult(intent, SWITCH_CODE);
            }
        });

        btnAdaugaPrizaNoua.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), AddSocketActivity.class);
            intent.putExtra("Token", rest.getToken());
            startActivity(intent);
        });


        btnDelogare.setOnClickListener(v -> {
            Intent intent = new Intent(SelectSocketActivity.this, MainActivity.class);
            startActivity(intent);
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        CompletableFuture<List<Socket>> listaServer = rest.getSocketList();
        listaServer.thenAcceptAsync(sockets -> {
            if(sockets != null){
                socketList = sockets;
                ArrayAdapter<Socket>  adapter = new ArrayAdapter<Socket>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        socketList);
                listView.setAdapter(adapter);
            }
        }, executor);
    }

    @Override
    protected void onResume() {
        super.onResume();
        CompletableFuture<List<Socket>> listaServer = rest.getSocketList();
        listaServer.thenAcceptAsync(sockets -> {
            if(sockets != null){
                socketList = sockets;
                ArrayAdapter<Socket>  adapter = new ArrayAdapter<Socket>(this,
                        android.R.layout.simple_dropdown_item_1line,
                        socketList);
                listView.setAdapter(adapter);
            }
        }, executor);
    }

}