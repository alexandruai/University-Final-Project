package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.graphicuserinterface.objects.Socket;
import com.example.graphicuserinterface.requests.Rest;

import java.util.concurrent.Executor;

public class SwitchSocketStateActivity extends AppCompatActivity {
    TextView tvIdSocket;
    TextView tvIdSelectedSocket;
    Button btnAnotherSocket;
    Button btnSaveSocket;
    Switch socketSwitch1;
    Switch socketSwitch2;
    Switch socketSwitch3;
    Switch socketSwitch4;
    String switchState = "Schimbare Stare Priza";
    Executor executor;
    Rest rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_socket_state);
        tvIdSocket = findViewById(R.id.tvSwitchState);
        tvIdSelectedSocket = findViewById(R.id.tvIdPriza);
        btnAnotherSocket = findViewById(R.id.btnBackSelectActivity);
        btnSaveSocket = findViewById(R.id.btnSaveChanges);
        socketSwitch1 =  findViewById(R.id.switchSocket1);
        socketSwitch2 = findViewById(R.id.switchSocket2);
        socketSwitch3 = findViewById(R.id.switchSocket3);
        socketSwitch4 = findViewById(R.id.switchSocket4);
        executor = ContextCompat.getMainExecutor(this);
        rest = new Rest(getIntent().getStringExtra("Token"));

        tvIdSocket.setText(switchState);

        //Preluare obiect
        // Verificare daca obiect exista in baza de date? Mai trebuie?
        //Preluare stari gauri
        //Afisare stari in switch button
            if(getIntent().hasExtra(SelectSocketActivity.Switch_State)){
                Socket socket = (Socket) getIntent().getSerializableExtra(SelectSocketActivity.Switch_State);
                tvIdSelectedSocket.setText(socket.getId());
                socketSwitch1.setChecked(socket.getStateIndex(1));
                socketSwitch2.setChecked(socket.getStateIndex(2));
                socketSwitch3.setChecked(socket.getStateIndex(3));
                socketSwitch4.setChecked(socket.getStateIndex(4));
            }


        //Preluare valori noi switch buttons
        //Salvare socket schimbat
        //Trimitere stari noi la server
        btnSaveSocket.setOnClickListener(v -> {

        });

        btnAnotherSocket.setOnClickListener(v -> {
            finish();
        });

    }
}