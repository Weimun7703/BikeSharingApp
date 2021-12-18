package com.example.bicycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnlogin, btnregister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnlogin= findViewById(R.id.btn_login);
        btnregister = findViewById(R.id.btn_bikerregister);



        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2bikerregister = new Intent(MainActivity.this,LoginPage.class);
                startActivity(intent2bikerregister);
            }
        });


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2login = new Intent(MainActivity.this,BikerRegisterPage.class);
                startActivity(intent2login);
            }
        });
    }
}