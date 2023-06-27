package com.example.petfinderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petfinderapp.model.Usuario;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;


public class Login extends AppCompatActivity {
    private String url = "";
    private EditText editUser, editPassword;
    private TextView erroLogin;
    private Button buttonLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        url = getResources().getString(R.string.base_url) + "/api/auth/login";

        editUser = findViewById(R.id.editUser);
        editPassword = findViewById(R.id.editPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        erroLogin = findViewById(R.id.erroLogin);
        progressBar = findViewById(R.id.progressBar);

        SharedPreferences sharedPreferences = getSharedPreferences("sessao", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String sessao = sharedPreferences.getString("auth_token", "");

        if (!sessao.isEmpty()) {
            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

        buttonLogin.setOnClickListener(view -> {
            erroLogin.setText("");
            String username = editUser.getText().toString();
            String password = editPassword.getText().toString();

            if (username.isEmpty()) {
                editUser.setError("Campo obrigatório!");
                return;
            } else if (password.isEmpty()) {
                editPassword.setError("Campo obrigatório!");
                return;
            } else {
                buttonLogin.setEnabled(false);
                progressBar.setVisibility(View.VISIBLE);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    JSONObject userObject = response.getJSONObject("user");
                                    long id = userObject.getInt("id");
                                    String nome = userObject.getString("name");
                                    String email = userObject.getString("email");
                                    String dataNasc = userObject.getString("dataNasc");
                                    String genero = userObject.getString("genero");
                                    String telefone = userObject.getString("telefone");
                                    String avatar = userObject.getString("avatar");
                                    int nivelAcesso = userObject.getInt("nivelAcesso");
                                    String authToken = userObject.getString("auth_token");

                                    Usuario user = new Usuario(id, nome, email);
                                    //salva sessao do usuário em SharedPreferences
                                    editor.putLong("userId", user.getId());
                                    editor.putString("username", user.getName());
                                    editor.putString("avatar", user.getAvatar());
                                    editor.putString("auth_token", authToken);
                                    editor.commit();
                                    progressBar.setVisibility(View.INVISIBLE);
                                    buttonLogin.setEnabled(true);
                                    //login bem-sucedido, redireciona para a próxima tela
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressBar.setVisibility(View.INVISIBLE);
                                buttonLogin.setEnabled(true);
                                NetworkResponse networkResponse = error.networkResponse;
                                erroLogin.setText("Erro código 10. Contate o suporte");
                                if (networkResponse != null && networkResponse.data != null) {
                                    String errorMessage = new String(networkResponse.data, StandardCharsets.UTF_8);
                                    try {
                                        JSONObject errorJson = new JSONObject(errorMessage);
                                        String message = errorJson.getString("message");
                                        message = message.replace("\\n", "\n");
                                        erroLogin.setText(message);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        erroLogin.setText("Erro de autenticação");
                                    }
                                } else {
                                    erroLogin.setText("Erro código 10. Contate o suporte");
                                }
                            }
                        });
                request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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