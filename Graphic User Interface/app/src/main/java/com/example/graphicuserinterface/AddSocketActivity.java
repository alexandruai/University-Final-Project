package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graphicuserinterface.exception.WrongCodeException;
import com.example.graphicuserinterface.objects.Socket;
import com.example.graphicuserinterface.requests.Rest;

import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddSocketActivity extends AppCompatActivity {

    EditText edIdNewSocket;
    Button btnAddIdSocket;
    Button btnBack;
    String addSocket = "Adauga Stare Priza";
    TextView tvStarePRiza;
    Executor executor;
    Rest rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_socket);
        edIdNewSocket = findViewById(R.id.etIdSocket);
        btnAddIdSocket = findViewById(R.id.btnAdauga);
        btnBack = findViewById(R.id.btnBack);
        tvStarePRiza = findViewById(R.id.tvAddSocket);
        tvStarePRiza.setText(addSocket);
        executor = ContextCompat.getMainExecutor(this);
        rest = new Rest(getIntent().getStringExtra("Token"));

        btnAddIdSocket.setOnClickListener(v -> {

            if(edIdNewSocket.getText().toString().isEmpty()){
                edIdNewSocket.setError("Introduceti id priza fizica!");
            } else {

                String newId = edIdNewSocket.getText().toString();
                CompletableFuture<Boolean> isNewIdAdded = rest.addDevice(newId);
                isNewIdAdded.thenAcceptAsync(aBoolean -> {
                    if(aBoolean){
                        Toast.makeText(AddSocketActivity.this,
                                "S-a introdus noua priza",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(AddSocketActivity.this,
                                "Nu s-a putut adauga noua priza",
                                Toast.LENGTH_SHORT).show();
                }, executor);
            }
        });

        btnBack.setOnClickListener(v -> finish());
        
    }

}