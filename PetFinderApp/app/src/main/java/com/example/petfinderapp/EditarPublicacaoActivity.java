package com.example.petfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.petfinderapp.model.Publicacao;
import com.google.gson.Gson;

public class EditarPublicacaoActivity extends AppCompatActivity {
    private ImageView photoImageView;
    private EditText editTextNome;
    private Spinner spinnerPorte;
    private EditText editTextIdade;
    private EditText editTextVacinas;
    private Spinner spinnerCastrado;
    private Spinner spinnerGenero;
    private Spinner spinnerEspecie;
    private EditText editTextDescricao;
    private TextView msgRetorno;
    private Button buttonCancelar;
    private Button buttonEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_publicacao);

        photoImageView = findViewById(R.id.photoImageView);
        editTextNome = findViewById(R.id.editTextNome);
        spinnerPorte = findViewById(R.id.spinnerPorte);
        editTextIdade = findViewById(R.id.editTextIdade);
        editTextVacinas = findViewById(R.id.editTextVacinas);
        spinnerCastrado = findViewById(R.id.spinnerCastrado);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        spinnerEspecie = findViewById(R.id.spinnerEspecie);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        msgRetorno = findViewById(R.id.msgRetorno);
        buttonCancelar = findViewById(R.id.buttonCancelar);
        buttonEditar = findViewById(R.id.buttonEditar);

        ArrayAdapter<CharSequence> porteAdapter = ArrayAdapter.createFromResource(this,
                R.array.portePet, android.R.layout.simple_spinner_item);
        porteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPorte.setAdapter(porteAdapter);

        ArrayAdapter<CharSequence> especieAdapter = ArrayAdapter.createFromResource(this,
                R.array.especiePet, android.R.layout.simple_spinner_item);
        especieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspecie.setAdapter(especieAdapter);

        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(this,
                R.array.generoPet, android.R.layout.simple_spinner_item);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(generoAdapter);

        ArrayAdapter<CharSequence> castradoAdapter = ArrayAdapter.createFromResource(this,
                R.array.castradoPet, android.R.layout.simple_spinner_item);
        castradoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCastrado.setAdapter(castradoAdapter);

        Intent intent = getIntent();
        String json = intent.getStringExtra("publicacaoJson");
        Gson gson = new Gson();
        Publicacao publicacao = gson.fromJson(json, Publicacao.class);

        Glide.with(this)
                .load(publicacao.getImagem())
                .placeholder(R.drawable.cachorro)
                .into(photoImageView);
        editTextNome.setText(publicacao.getNomePet());




    }

}
