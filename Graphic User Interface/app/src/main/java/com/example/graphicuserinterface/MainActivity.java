package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    String header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumeUtilizator = findViewById(R.id.etUserName);
        etParola = findViewById(R.id.etPassword);
        tvAutentificare = findViewById(R.id.tvAutentificare);
        btnLogare = findViewById(R.id.btnAutentificare);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnLogare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputNumeUtilizator = etNumeUtilizator.getText().toString();
                String inputParola = etParola.getText().toString();


                if (inputNumeUtilizator.isEmpty() || inputParola.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Introduceti credentiale!", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("username", inputNumeUtilizator);
                        jsonObject.put("password", inputParola);
                    } catch (JSONException e) {
                        System.out.println("Json error");
                        e.printStackTrace();
                    }

                    Thread thread = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try  {
                                validare(jsonObject);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    thread.start();
                }
            }
        });
    }

    private void validare(JSONObject jsonContent) throws WrongCodeException {
        OkHttpClient client = new OkHttpClient.Builder().build();
        RequestBody body = RequestBody.create(MediaType.get("application/json"), String.valueOf(jsonContent));
        Request request = new Request.Builder()
                .url("https://andra.lucaci32u4.xyz/api/login")
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String msg = String.valueOf(response.code());
            System.out.println(msg);
            Log.i("Code: ", String.valueOf(response.code()));
            response.close();
            if (response.code() != 200) {

                Log.e("WrongCode ", msg);
                System.out.println(msg);
                throw new WrongCodeException();
            } else {
                assert response.body() != null;
                header = response.body().toString();
                Intent intent = new Intent(MainActivity.this, SelectSocketActivity.class);
                startActivity(intent);

            }
        } catch (IOException exception) {
           String msg = exception.getMessage();
            System.out.println(msg);
            throw new WrongCodeException();
        }
    }
}