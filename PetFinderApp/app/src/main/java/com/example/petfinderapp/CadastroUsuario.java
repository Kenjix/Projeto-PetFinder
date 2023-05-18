package com.example.petfinderapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;


public class CadastroUsuario extends AppCompatActivity {

    private EditText editNome, editEmail, editDataNasc, editCelular, editSenha, editRepitaSenha;
    private RadioButton radioButtonMasc, radioButtonFem, radioButtonOutros;
    private Button buttonCadastro, ok_button;
    private TextView msgCadastro;
    private String url = "http://192.168.100.6:8000/api/cadastro";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editDataNasc = findViewById(R.id.editDataNasc);
        editCelular = findViewById(R.id.editCelular);
        editSenha = findViewById(R.id.editSenha);
        editRepitaSenha = findViewById(R.id.editRepitaSenha);
        buttonCadastro = findViewById(R.id.buttonCadastro);
        msgCadastro = findViewById(R.id.msgCadastro);

        buttonCadastro.setOnClickListener(view -> {
            String nome = editNome.getText().toString().trim();
            String email = editEmail.getText().toString().trim();
            String dataNasc = editDataNasc.getText().toString().trim();
            String telefone = editCelular.getText().toString().trim();
            String genero = "";
            String senha = editSenha.getText().toString().trim();
            String repeteSenha = editRepitaSenha.getText().toString().trim();

            if (nome.isEmpty()) {
                editNome.setError("Campo obrigatório!");
                return;
            } else if(email.isEmpty()){
                editEmail.setError("Campo obrigatório!");
                return;
            } else if(senha.isEmpty()){
                editSenha.setError("Campo obrigatório!");
            } else if(repeteSenha.isEmpty()){
                editRepitaSenha.setError("Campo obrigatório!");
            }
            if(validaSenha(senha, repeteSenha)){
                JSONObject jsonObject = new JSONObject();
                Usuario user = new Usuario(nome, email, senha, dataNasc, genero, telefone);
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

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    msgCadastro.setText(response.getString("message"));
                                    finish();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                msgCadastro.setText("Erro:" + error.getMessage());

                            }
                        }
                );
                //adiciona a requisição à fila do Volley
                RequestQueue fila = Volley.newRequestQueue(this);
                fila.add(request);
            }
        });
    }


    private boolean validaSenha(String senha1, String senha2){
        if(senha1.equals(senha2)){
            return true;
        }else{
            return false;
        }
    }

}