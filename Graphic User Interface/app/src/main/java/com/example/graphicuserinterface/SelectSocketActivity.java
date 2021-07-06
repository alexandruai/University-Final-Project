package com.example.graphicuserinterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.graphicuserinterface.objects.Socket;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectSocketActivity extends AppCompatActivity {

    public static final int ADD_Socket = 0;
    public static final int REQUEST_CODE = 200;
    public static final String Switch_State = "switch";
    public static final int REQUEST_CODE_Switch = 300;
    private Button btnDelogare;
    private Button btnAdaugaPrizaNoua;
    private Button btnSchimbaStarePriza;
    List<Socket> socketList = new ArrayList<>();
    List<String> socketsStrings = new ArrayList<>();
    private ListView listView;
    private ArrayAdapter<String> adapter;
    int poz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_socket);
        btnSchimbaStarePriza = findViewById(R.id.btnSwitchState);
        btnAdaugaPrizaNoua = findViewById(R.id.btnPrizaNoua);
        btnDelogare = findViewById(R.id.btnDelogare);
        listView = findViewById(R.id.listViewSockets);

        for(Socket socket : socketList){
            socketsStrings.add(socket.toString());
        }
        adapter = new ArrayAdapter<>(getApplicationContext(),
                android.R.layout.simple_list_item_1,
                socketsStrings);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                poz = position;
            }
        });

        btnSchimbaStarePriza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SwitchSocketStateActivity.class);
                intent.putExtra(Switch_State, (Serializable) socketList.get(poz));
                startActivityForResult(intent, REQUEST_CODE_Switch);
            }
        });

        btnAdaugaPrizaNoua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSocketActivity.class);
                startActivityForResult(intent, ADD_Socket);
            }
        });

        btnDelogare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSocketActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Socket socket = (Socket) data.getSerializableExtra(AddSocketActivity.ADD_Socket);
            if(socket != null){
                socketList.add(socket);
                socketsStrings.add(socket.toString());
                adapter.notifyDataSetChanged();
            }
        }
    }
}