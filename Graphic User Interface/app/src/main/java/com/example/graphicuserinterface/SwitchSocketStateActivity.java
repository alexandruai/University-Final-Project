package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class SwitchSocketStateActivity extends AppCompatActivity {
    TextView tvIdSocket;
    Button btnAnotherSocket;
    Switch socketSwitch1;
    Switch socketSwitch2;
    Switch socketSwitch3;
    Switch socketSwitch4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_socket_state);
        tvIdSocket = findViewById(R.id.tvIdPriza);
        btnAnotherSocket = findViewById(R.id.btnBackSelectActivity);

        btnAnotherSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SwitchSocketStateActivity.this, SelectSocketActivity.class);
                startActivity(intent);
            }
        });

    }
}