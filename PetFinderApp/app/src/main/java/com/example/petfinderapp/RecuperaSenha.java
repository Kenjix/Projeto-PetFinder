package com.example.petfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class RecuperaSenha extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recupera_senha);

    }

    public void LoginClick(View view) {
        Intent intent = new Intent(RecuperaSenha.this, Login.class);
        startActivity(intent);
    }
}