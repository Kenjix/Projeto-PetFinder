package com.example.petfinderapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.petfinderapp.model.DatePickerDialog;
import com.example.petfinderapp.model.Publicacao;
import com.google.gson.Gson;

import java.util.concurrent.atomic.AtomicReference;

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
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_publicacao);

        photoImageView = findViewById(R.id.photoImageView);
        editTextNome = findViewById(R.id.editTextNome);
        editTextIdade = findViewById(R.id.editTextIdade);
        editTextVacinas = findViewById(R.id.editTextVacinas);
        spinnerPorte = findViewById(R.id.spinnerPorte);
        spinnerCastrado = findViewById(R.id.spinnerCastrado);
        spinnerGenero = findViewById(R.id.spinnerGenero);
        spinnerEspecie = findViewById(R.id.spinnerEspecie);
        editTextDescricao = findViewById(R.id.editTextDescricao);
        msgRetorno = findViewById(R.id.msgRetorno);
        buttonCancelar = findViewById(R.id.buttonCancelar);
        buttonEditar = findViewById(R.id.buttonEditar);
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verificar permissão de acesso à galeria
                if (ContextCompat.checkSelfPermission(v.getContext(), android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //solicita permissão
                    //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                } else {
                    selectPhotoClick();
                }
            }
        });

        AtomicReference<Intent> intent = new AtomicReference<>(getIntent());
        String json = intent.get().getStringExtra("publicacaoJson");
        Gson gson = new Gson();
        Publicacao publicacao = gson.fromJson(json, Publicacao.class);

        //foto
        Glide.with(this)
                .load(publicacao.getImagem())
                .placeholder(R.drawable.cachorro)
                .into(photoImageView);
        editTextNome.setText(publicacao.getNomePet());
        editTextIdade.setText(publicacao.getIdade());
        editTextVacinas.setText(publicacao.getVacinas());
        editTextDescricao.setText(publicacao.getDescricao());

        //porte
        ArrayAdapter<CharSequence> porteAdapter = ArrayAdapter.createFromResource(this,
                R.array.portePet, android.R.layout.simple_spinner_item);
        spinnerPorte.setAdapter(porteAdapter);
        String porte = publicacao.getPorte();
        if (porte != null) {
            int index = porteAdapter.getPosition(porte);
            if (index >= 0) {
                spinnerPorte.setSelection(index);
            }
        }
        //especie
        ArrayAdapter<CharSequence> especieAdapter = ArrayAdapter.createFromResource(this,
                R.array.especiePet, android.R.layout.simple_spinner_item);
        spinnerEspecie.setAdapter(especieAdapter);
        String especie = publicacao.getEspecie();
        if (especie != null) {
            int index = especieAdapter.getPosition(especie);
            if (index >= 0) {
                spinnerEspecie.setSelection(index);
            }
        }
        //castrado
        ArrayAdapter<CharSequence> castradoAdapter = ArrayAdapter.createFromResource(this,
                R.array.castradoPet, android.R.layout.simple_spinner_item);
        spinnerCastrado.setAdapter(castradoAdapter);
        boolean castrado = publicacao.isCastrado();
        if (castrado) {
            spinnerCastrado.setSelection(1); //seleciona "Sim"
        } else {
            spinnerCastrado.setSelection(2); //seleciona "Não"
        }
        //genero
        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(this,
                R.array.generoPet, android.R.layout.simple_spinner_item);
        spinnerGenero.setAdapter(generoAdapter);
        String genero = publicacao.getGenero();
        if (genero != null) {
            int index = generoAdapter.getPosition(genero);
            if (index >= 0) {
                spinnerGenero.setSelection(index);
            }
        }


        //evento campo idade
        editTextIdade.setOnClickListener(view2 -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, editTextIdade, 3);
            datePickerDialog.showDatePickerDialog();
        });

        //evento botao editar
        buttonEditar.setOnClickListener(clickView -> {

        });

        //evento botao cancelar
        buttonCancelar.setOnClickListener(clickView -> {
            //cria uma Intent para iniciar a atividade principal
            intent.set(new Intent(this, MainActivity.class));
            intent.get().setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent.get());
            //finalizar a activity atual
            this.finish();
        });
    }

    ActivityResultContract<String, Uri> getContent = new ActivityResultContracts.GetContent();
    ActivityResultLauncher<String> launcher = registerForActivityResult(getContent, new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            //resultado da seleção de imagem
            if (result != null) {
                //URI da imagem selecionada
                photoImageView.setImageURI(result);
            }
        }
    });
    public void selectPhotoClick() {
        launcher.launch("image/*");
    }
}
