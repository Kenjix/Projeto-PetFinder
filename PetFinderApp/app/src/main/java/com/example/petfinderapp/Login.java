package com.example.petfinderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    private EditText editUser, editPassword;
    private TextView erroLogin;
    private Button buttonLogin;
    private String url = "http://192.168.100.6/codigo/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        erroLogin = findViewById(R.id.erroLogin);

        SharedPreferences sharedPreferences = getSharedPreferences("sessao", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String sessao = sharedPreferences.getString("username", "");

        if (!sessao.isEmpty()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        buttonLogin.setOnClickListener(view -> {
            String username = editUser.getText().toString();
            String password = editPassword.getText().toString();

            if (username.isEmpty()) {
                editUser.setError("Campo obrigatório!");
                return;
            } else if (password.isEmpty()) {
                editPassword.setError("Campo obrigatório!");
                return;
            } else {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", username);
                    jsonObject.put("senha", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    int resultado = response.getInt("resultado");
                                    int nivelAcesso = response.getInt("nivelAcesso");
                                    String nome = response.getString("nome");
                                    if (resultado == 0) {
                                        //Salva o nome do usuário em SharedPreferences
                                        editor.putString("username", nome);
                                        editor.putInt("nivelAcesso", nivelAcesso);
                                        editor.commit();
                                        // Login bem-sucedido, redirecionar para a próxima tela
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (resultado == 1) {
                                        erroLogin.setText("Senha inválida");
                                    } else if (resultado == 2) {
                                        erroLogin.setText("Email não cadastrado");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                erroLogin.setText("Erro ao consultar: " + error.getMessage());
                                //Toast.makeText(Login.this, "Erro ao consultar: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                //adiciona a requisição à fila do Volley
                RequestQueue fila = Volley.newRequestQueue(this);
                fila.add(request);
            }
        });
    }

    public void CadastroClick(View view) {
        Intent intent = new Intent(Login.this, CadastroUsuario.class);
        startActivity(intent);
    }

    public void ResetSenhaClick(View view) {
        Intent intent = new Intent(Login.this, RecuperaSenha.class);
        startActivity(intent);
    }
}