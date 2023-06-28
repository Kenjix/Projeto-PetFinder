package com.example.petfinderapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.petfinderapp.model.DatePickerDialog;
import com.example.petfinderapp.model.Publicacao;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
    private Button selectPhotoButton;
    private SharedPreferences preferences;
    String url = "";
    private static final int REQUEST_STORAGE_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_publicacao);

        preferences = getSharedPreferences("sessao", MODE_PRIVATE);
        long idUsuario = preferences.getLong("userId", 0);
        String authToken = preferences.getString("auth_token", null);

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
        selectPhotoButton = findViewById(R.id.selectPhotoButton);
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verifica permissao de acesso a galeria
                if (ContextCompat.checkSelfPermission(EditarPublicacaoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //solicita permissão
                    ActivityCompat.requestPermissions(EditarPublicacaoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
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
            editar(publicacao.getId(), idUsuario, authToken);
        });

        //evento botao cancelar
        buttonCancelar.setOnClickListener(clickView -> {
            onBackPressed();
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

    private void editar(long idPublicacao, long idUsuario, String authToken) {
        url = getResources().getString(R.string.base_url) + "/api/publicacao/" + idPublicacao;
        msgRetorno.setText("");
        //pega os valores digitados nos campos de texto do XML
        String nomePet = editTextNome.getText().toString();
        String porte = spinnerPorte.getSelectedItem().toString();
        String idade = editTextIdade.getText().toString();
        String vacinas = editTextVacinas.getText().toString();
        boolean castrado = false;
        String selectedItem = spinnerCastrado.getSelectedItem().toString();
        if (selectedItem.equals("Sim")) {
            castrado = true;
        } else if (selectedItem.equals("Não")) {
            castrado = false;
        }
        String genero = spinnerGenero.getSelectedItem().toString();
        String especie = spinnerEspecie.getSelectedItem().toString();
        String descricao = editTextDescricao.getText().toString();
        //validacoes dos campos
        if (nomePet.isEmpty()) {
            editTextNome.requestFocus();
            editTextNome.setError("Campo obrigatório!");
            return;
        } else if (porte.equals("Selecione")) {
            msgRetorno.setText("Selecione um porte.");
            return;
        } else if (idade.isEmpty()) {
            editTextIdade.requestFocus();
            editTextIdade.setError("Campo obrigatório!");
            return;
        } else if (selectedItem.equals("Selecione")) {
            msgRetorno.setText("Selecione se o pet é castrado");
            return;
        } else if (genero.equals("Selecione")) {
            msgRetorno.setText("Selecione o genero");
            return;
        } else if (especie.equals("Selecione")) {
            msgRetorno.setText("Selecione a espécie");
            return;
        } else if (descricao.isEmpty()) {
            editTextDescricao.requestFocus();
            editTextDescricao.setError("Campo obrigatório!");
            return;
        } else if (photoImageView.getDrawable() == null) {
            msgRetorno.setText("Selecione uma foto do pet");
            return;
        }
        JSONObject jsonObject = new JSONObject();
        Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        Publicacao publicacao = new Publicacao(descricao, nomePet, genero, especie, porte, idade, vacinas, castrado, idUsuario);
        try {
            jsonObject.put("nomePet", publicacao.getNomePet());
            jsonObject.put("porte", publicacao.getPorte());
            jsonObject.put("idade", publicacao.getIdade());
            jsonObject.put("vacinas", publicacao.getVacinas());
            jsonObject.put("castrado", publicacao.isCastrado());
            jsonObject.put("genero", publicacao.getGenero());
            jsonObject.put("especie", publicacao.getEspecie());
            jsonObject.put("descricao", publicacao.getDescricao());
            jsonObject.put("user_id", publicacao.getUserId());
            jsonObject.put("image_path", base64Image);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //request
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + authToken);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(EditarPublicacaoActivity.this, "Dados atualizados com sucesso.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(EditarPublicacaoActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //verifica o código de resposta do servidor
                        if (error.networkResponse != null && error.networkResponse.statusCode != 200) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject responseJson = new JSONObject(responseBody);

                                //verifica se a resposta contém o objeto "errors"
                                if (responseJson.has("errors")) {
                                    JSONObject errors = responseJson.getJSONObject("errors");
                                    //itera pelos campos de erro e obtém a primeira mensagem de erro
                                    Iterator<String> keys = errors.keys();
                                    if (keys.hasNext()) {
                                        String field = keys.next();
                                        JSONArray fieldErrors = errors.getJSONArray(field);

                                        //obtem a primeira mensagem de erro
                                        if (fieldErrors.length() > 0) {
                                            String msgErro = fieldErrors.getString(0);
                                            msgRetorno.setText(msgErro);
                                        }
                                    }
                                }
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else if (error.networkResponse != null && error.networkResponse.statusCode != 401) {
                            Intent intent = new Intent(EditarPublicacaoActivity.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            //outros erros
                            msgRetorno.setText("Erro código 21. Contate o suporte");
                        }
                    }

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
        RequestQueue fila = Volley.newRequestQueue(this);
        fila.add(request);
    }
}
