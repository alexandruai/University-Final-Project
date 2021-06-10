package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SelectSocketActivity extends AppCompatActivity {

    private Button btnDelogare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_socket);

        btnDelogare = findViewById(R.id.btnDelogare);

        btnDelogare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectSocketActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}