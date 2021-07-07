package com.example.graphicuserinterface;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.graphicuserinterface.objects.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SelectSocketActivity extends AppCompatActivity {

    public static final int ADD_CODE = 0;
    public static final String Switch_State = "switch";
    public static final int REQUEST_CODE_Switch = 200;
    ListView listView;
    List<Socket> socketList;
    int poz;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String SAVE_SOCKET_LIST = "saveSockets";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_socket);
        Button btnSchimbaStarePriza = findViewById(R.id.btnSwitchState);
        Button btnAdaugaPrizaNoua = findViewById(R.id.btnPrizaNoua);
        Button btnDelogare = findViewById(R.id.btnDelogare);
        socketList = new ArrayList<Socket>();
        listView = findViewById(R.id.listViewSockets);
        preferences = getApplicationContext().getSharedPreferences(SAVE_SOCKET_LIST, Context.MODE_PRIVATE);
        editor = preferences.edit();
        socketList = getList();

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
                startActivityForResult(intent, REQUEST_CODE_Switch);
            }
        });

        btnAdaugaPrizaNoua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddSocketActivity.class);
                startActivityForResult(intent, ADD_CODE);
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
    protected void onStart() {
        super.onStart();
        ArrayAdapter<Socket>  adapter = new ArrayAdapter<Socket>(this,
                android.R.layout.simple_dropdown_item_1line,
                socketList);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_CODE && resultCode == RESULT_OK && data != null) {
            ArrayAdapter<Socket>  adapter = new ArrayAdapter<Socket>(this,
                    android.R.layout.simple_dropdown_item_1line,
                    socketList);
            listView.setAdapter(adapter);
            Socket socket = (Socket) data.getSerializableExtra(AddSocketActivity.ADD_Socket);
            if(socket != null){
                socketList.add(socket);
                Toast.makeText(SelectSocketActivity.this, socket.toString(), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
                setList("socketList", socketList);

            }
        }
    }

    public <Socket> void setList(String key, List<Socket> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        set(key, json);
    }

    public void set(String key, String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public List <Socket> getList(){
        List<Socket> arrayItems = new ArrayList<Socket>();
        String serializedObject = preferences.getString(SAVE_SOCKET_LIST, null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Socket>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

}