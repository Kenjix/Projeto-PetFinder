package com.example.petfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class EditarPublicacao extends AppCompatActivity {
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

    }

}
