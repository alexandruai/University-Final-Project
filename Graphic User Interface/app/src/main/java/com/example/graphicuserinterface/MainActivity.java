package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

   private EditText etNumeUtilizator, etParola;
   private TextView tvAutentificare;
   private Button btnLogare;
   private String userName = "Admin";
   private String parola = "12345";
   private boolean isValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etNumeUtilizator = findViewById(R.id.etUserName);
        etParola = findViewById(R.id.etPassword);
        tvAutentificare = findViewById(R.id.tvAutentificare);
        btnLogare = findViewById(R.id.btnAutentificare);

        btnLogare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String inputNumeUtilizator = etNumeUtilizator.getText().toString();
                String inputParola = etParola.getText().toString();

                if(inputNumeUtilizator.isEmpty() || inputParola.isEmpty()){
                    Toast.makeText( MainActivity.this, "Introduceti credentiale!", Toast.LENGTH_SHORT).show();
                } else {
                    
                    isValid = validare(inputNumeUtilizator, inputParola);
                    
                    if(!isValid){
                        Toast.makeText(MainActivity.this, "Credentiale incorecte! Incercati din nou!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Credentiale corecte!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(MainActivity.this, SocketActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean validare(String nume, String password){
        if(nume.equals(userName) && password.equals(parola)) {
            return true;
        }
        return false;
    }
}