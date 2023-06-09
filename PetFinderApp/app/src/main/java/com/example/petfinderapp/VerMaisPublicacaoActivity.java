package com.example.petfinderapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.petfinderapp.model.Publicacao;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VerMaisPublicacaoActivity extends AppCompatActivity {

    private ImageView imagemViewFoto, imageViewPerfil;
    private TextView textNomeUser, nomePet, portePet, idadePet, vacinasPet, castradoPet, generoPet, especiePet, descricaoPet ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_mais_publicacao);

        imagemViewFoto = findViewById(R.id.imagemViewFoto);
        imageViewPerfil = findViewById(R.id.imageViewPerfil);
        textNomeUser = findViewById(R.id.textNomeUser);
        nomePet = findViewById(R.id.nomePet);
        portePet = findViewById(R.id.portePet);
        idadePet = findViewById(R.id.idadePet);
        vacinasPet = findViewById(R.id.vacinasPet);
        castradoPet = findViewById(R.id.castradoPet);
        generoPet = findViewById(R.id.generoPet);
        especiePet = findViewById(R.id.especiePet);
        descricaoPet = findViewById(R.id.descricaoPet);

        Intent intent = getIntent();
        if (intent != null) {
            //obter a String extra do Intent
            String json = getIntent().getStringExtra("publicacaoJson");
            //desserializa o json para objeto usando Gson
            Gson gson = new Gson();
            Publicacao publicacao = gson.fromJson(json, Publicacao.class);

            Glide.with(this)
                    .load(publicacao.getImagem())
                    .placeholder(R.drawable.cachorro)
                    .into(imagemViewFoto);

            Glide.with(this)
                    .load(publicacao.getUser().getAvatar())
                    .placeholder(R.drawable.fotoperfil)
                    .into(imageViewPerfil);

            textNomeUser.setText(publicacao.getUser().getName());
            nomePet.setText(publicacao.getNomePet());
            portePet.setText(publicacao.getPorte());
            idadePet.setText(publicacao.getIdade());
            vacinasPet.setText(publicacao.getVacinas());
            castradoPet.setText(publicacao.isCastrado() ? "Sim" : "Não");
            generoPet.setText(publicacao.getGenero());
            especiePet.setText(publicacao.getEspecie());
            descricaoPet.setText(publicacao.getDescricao());
        }
    }
}