package com.example.graphicuserinterface;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.graphicuserinterface.objects.Socket;

import java.io.Serializable;

public class AddSocketActivity extends AppCompatActivity {

    EditText edIdNewSocket;
    Button btnAddIdSocket;
    Button btnBack;
    public static final String ADD_Socket = "addSocket";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_socket);
        edIdNewSocket = findViewById(R.id.etIdSocket);
        btnAddIdSocket = findViewById(R.id.btnAdauga);
        btnBack = findViewById(R.id.btnBack);
        Intent intent = getIntent();

        btnAddIdSocket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edIdNewSocket.getText().toString().isEmpty()){
                    edIdNewSocket.setError("Introduceti id priza fizica!");
                } else {
                    String id = edIdNewSocket.getText().toString();
                    boolean[] state = {false, false, false, false};
                    Socket socket = new Socket(id, state);

                    //add restApi route catre server trimite noua priza id
                    // routa si cu stari?

                    intent.putExtra(ADD_Socket, (Serializable) socket);
                    setResult(RESULT_OK, intent);
                    finish();
                }


            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddSocketActivity.this, SelectSocketActivity.class);
                startActivity(intent);
            }
        });
        
    }
}