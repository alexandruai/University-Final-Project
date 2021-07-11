package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.graphicuserinterface.exception.WrongCodeException;
import com.example.graphicuserinterface.requests.Rest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etNumeUtilizator, etParola;
    private TextView tvAutentificare;
    private Button btnLogare;
    String autentificare = "Login";
    Executor executor;
    Rest rest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumeUtilizator = findViewById(R.id.etUserName);
        etParola = findViewById(R.id.etPassword);
        tvAutentificare = findViewById(R.id.tvAutentificare);
        btnLogare = findViewById(R.id.btnAutentificare);
        executor = ContextCompat.getMainExecutor(this);
        rest = new Rest();

        tvAutentificare.setText(autentificare);

        btnLogare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputNumeUtilizator = etNumeUtilizator.getText().toString();
                String inputParola = etParola.getText().toString();

                if (inputNumeUtilizator.isEmpty() || inputParola.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Introduceti credentiale!", Toast.LENGTH_SHORT).show();
                } else {
                    CompletableFuture<Boolean> validare = rest.login(inputNumeUtilizator, inputParola);
                    validare.thenAcceptAsync( aBoolean -> {
                        if(aBoolean){
                            Intent intent = new Intent(MainActivity.this, SelectSocketActivity.class);
                            intent.putExtra("Token", rest.getToken());
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Credentiale incorecte!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } ,executor);
                }
            }
        });
    }

}