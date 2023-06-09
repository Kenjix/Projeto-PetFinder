package com.example.petfinderapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petfinderapp.model.DatePickerDialog;
import com.example.petfinderapp.model.PhoneMaskWatcher;
import com.example.petfinderapp.model.Usuario;
import com.makeramen.roundedimageview.RoundedDrawable;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CadastroUsuario extends AppCompatActivity {
    private String url = "";
    private EditText editNome, editEmail, editDataNasc, editCelular, editSenha, editRepitaSenha;
    RadioGroup radioGroupGenero;
    RadioButton radioButtonMasc, radioButtonFem, radioButtonOutros;
    private CheckBox checkTermos;
    private Button buttonCadastro;
    private TextView msgCadastro, removeImage;
    private RoundedImageView imagemPerfil;
    private TextView addImagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        url = getResources().getString(R.string.base_url) + "/api/users";

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editDataNasc = findViewById(R.id.editDataNasc);
        editCelular = findViewById(R.id.editCelular);
        editSenha = findViewById(R.id.editSenha);
        editRepitaSenha = findViewById(R.id.editRepitaSenha);
        buttonCadastro = findViewById(R.id.buttonCadastro);
        msgCadastro = findViewById(R.id.msgCadastro);
        checkTermos = findViewById(R.id.checkTermos);
        radioGroupGenero = findViewById(R.id.radioGroupGenero);
        radioButtonMasc = findViewById(R.id.radioButtonMasc);
        radioButtonFem = findViewById(R.id.radioButtonFem);
        radioButtonOutros = findViewById(R.id.radioButtonOutros);
        imagemPerfil = findViewById(R.id.imagemPerfil);
        addImagem = findViewById(R.id.addImagem);
        removeImage = findViewById(R.id.removeImage);


        ActivityResultContract<String, Uri> getContent = new ActivityResultContracts.GetContent();
        ActivityResultLauncher<String> launcher = registerForActivityResult(getContent, new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                //resultado da seleção de imagem
                if (result != null) {
                    //URI da imagem selecionada
                    imagemPerfil.setImageURI(result);
                    addImagem.setText("");
                }
            }
        });

        imagemPerfil.setOnClickListener(view -> {
            launcher.launch("image/*");
            imagemPerfil.setTag(2);
        });

        removeImage.setOnClickListener(view -> {
            imagemPerfil.setImageResource(R.drawable.background_img);
            addImagem.setText("Adicione uma imagem");
            imagemPerfil.setTag(getResources().getInteger(R.integer.image_tag));
        });

        editDataNasc.setOnClickListener(view -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CadastroUsuario.this, editDataNasc, 1);
            datePickerDialog.showDatePickerDialog();
        });

        editCelular.addTextChangedListener(new PhoneMaskWatcher(editCelular));

        buttonCadastro.setOnClickListener(view -> {
            String nome = editNome.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String dataNasc = editDataNasc.getText().toString().trim();
            String dataFormatada = "";
            SimpleDateFormat formatoEntrada = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat formatoSaida = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date data = formatoEntrada.parse(dataNasc);
                dataFormatada = formatoSaida.format(data);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String telefone = editCelular.getText().toString().trim();
            String genero = "";
            int selectedRadioButtonId = radioGroupGenero.getCheckedRadioButtonId();
            if (selectedRadioButtonId != -1) {
                if (selectedRadioButtonId == radioButtonMasc.getId()) {
                    genero = "M";
                } else if (selectedRadioButtonId == radioButtonFem.getId()) {
                    genero = "F";
                } else if (selectedRadioButtonId == radioButtonOutros.getId()) {
                    genero = "O";
                }
            }
            String senha = editSenha.getText().toString().trim();
            String repeteSenha = editRepitaSenha.getText().toString().trim();
            if (nome.isEmpty()) {
                editNome.requestFocus();
                editNome.setError("Campo obrigatório!");
                return;
            } else if (email.isEmpty()) {
                editEmail.requestFocus();
                editEmail.setError("Campo obrigatório!");
                return;
            } else if (dataNasc.isEmpty()) {
                editDataNasc.requestFocus();
                editDataNasc.setError("Campo obrigatório!");
                return;
            } else if (telefone.isEmpty()) {
                editCelular.requestFocus();
                editCelular.setError("Campo obrigatório!");
            } else if (senha.isEmpty()) {
                editSenha.requestFocus();
                editSenha.setError("Campo obrigatório!");
                return;
            } else if (repeteSenha.isEmpty()) {
                editRepitaSenha.requestFocus();
                editRepitaSenha.setError("Campo obrigatório!");
                return;
            }

            checkTermos.setChecked(true);

            if (!checkTermos.isChecked()) {
                msgCadastro.setText("Termos não aceitos");
            } else if (validaSenha(senha, repeteSenha)) {
                JSONObject jsonObject = new JSONObject();
                if(imagemPerfil.getTag().toString().equals("1")) {
                    Usuario user = new Usuario(nome, email, senha, dataFormatada, genero, telefone.replaceAll("\\D", ""));
                    try {
                        jsonObject.put("name", user.getName());
                        jsonObject.put("email", user.getEmail());
                        jsonObject.put("password", user.getPassword());
                        jsonObject.put("dataNasc", user.getDataNasc());
                        jsonObject.put("genero", user.getGenero());
                        jsonObject.put("telefone", user.getTelefone());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Drawable drawable = imagemPerfil.getDrawable();
                    RoundedDrawable roundedDrawable = (RoundedDrawable) drawable;
                    Bitmap bitmap = roundedDrawable.getSourceBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    String avatar = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    Usuario user = new Usuario(nome, email, senha, dataFormatada, genero, telefone.replaceAll("\\D", ""), avatar);
                    try {
                        jsonObject.put("name", user.getName());
                        jsonObject.put("email", user.getEmail());
                        jsonObject.put("password", user.getPassword());
                        jsonObject.put("dataNasc", user.getDataNasc());
                        jsonObject.put("genero", user.getGenero());
                        jsonObject.put("telefone", user.getTelefone());
                        jsonObject.put("avatar", user.getAvatar());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                buttonCadastro.setEnabled(false);
                                final int delayMilissegundos = 1000; //intervalo de atualização em milissegundos
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    int segundosRestantes = 5;

                                    @Override
                                    public void run() {
                                        msgCadastro.setTextColor(Color.parseColor("#8aa67b"));
                                        msgCadastro.setText("Cadastrado com sucesso! \nRedirecionando em " + segundosRestantes + " segundos...");
                                        segundosRestantes--;

                                        if (segundosRestantes >= 0) {
                                            handler.postDelayed(this, delayMilissegundos);
                                        } else {
                                            Intent intent = new Intent(CadastroUsuario.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                }, delayMilissegundos);
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

                                            //verifica se a chave "email" existe nos erros
                                            if (errors.has("email")) {
                                                JSONArray emailErrors = errors.getJSONArray("email");
                                                if (emailErrors.length() > 0) {
                                                    String emailErrorMessage = emailErrors.getString(0);
                                                    msgCadastro.setText(emailErrorMessage);
                                                }
                                            }
                                            //verifica se a chave "password" existe nos erros
                                            if (errors.has("password")) {
                                                JSONArray passwordErrors = errors.getJSONArray("password");
                                                if (passwordErrors.length() > 0) {
                                                    String passwordErrorMessage = passwordErrors.getString(0);
                                                    msgCadastro.setText(passwordErrorMessage);
                                                }
                                            }
                                            //verifica se a chave "name" existe nos erros
                                            if (errors.has("name")) {
                                                JSONArray passwordErrors = errors.getJSONArray("name");
                                                if (passwordErrors.length() > 0) {
                                                    String nameErrorMessage = passwordErrors.getString(0);
                                                    msgCadastro.setText(nameErrorMessage);
                                                }
                                            }
                                        }
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    //outros erros
                                    msgCadastro.setText("Erro código 20. Contate o suporte");
                                }
                            }
                        }
                );
                //adiciona a requisição à fila do Volley
                RequestQueue fila = Volley.newRequestQueue(this);
                fila.add(request);
            } else {
                msgCadastro.setText("A senha não confere");
            }
        });
    }
    private boolean validaSenha(String senha1, String senha2) {
        if (senha1.equals(senha2)) {
            return true;
        } else {
            return false;
        }
    }

    public void showTermsAndConditionsDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_terms_and_conditions, null);
        builder.setView(dialogView);
        //configura os botoes
        builder.setPositiveButton("ACEITAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                checkTermos.setChecked(true);
            }
        });
        builder.setNegativeButton("RECUSAR", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                checkTermos.setChecked(false);
                dialog.dismiss();
            }
        });
        builder.show();
    }
}