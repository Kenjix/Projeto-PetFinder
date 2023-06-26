package com.example.petfinderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class RedefinirSenhaFragment extends Fragment {
    private EditText senhaAntigaEditText;
    private EditText novaSenhaEditText;
    private EditText confirmaNovaSenhaEditText;
    private TextView msgErroTextView;
    private Button buttonSalvar;
    private SharedPreferences preferences;

    public RedefinirSenhaFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_redefinir_senha, container, false);

        senhaAntigaEditText = view.findViewById(R.id.senhaAntiga);
        novaSenhaEditText = view.findViewById(R.id.novaSenha);
        confirmaNovaSenhaEditText = view.findViewById(R.id.confirmaNovaSenha);
        msgErroTextView = view.findViewById(R.id.msgErro);
        buttonSalvar = view.findViewById(R.id.buttonSalvar);
        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        Long userId = preferences.getLong("userId", -1);
        String authToken = preferences.getString("auth_token", null);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senhaAntiga = senhaAntigaEditText.getText().toString();
                String novaSenha = novaSenhaEditText.getText().toString();
                String confirmaNovaSenha = confirmaNovaSenhaEditText.getText().toString();
                msgErroTextView.setText("");
                if (senhaAntiga.isEmpty()) {
                    senhaAntigaEditText.requestFocus();
                    senhaAntigaEditText.setError("Campo obrigatório");
                } else if (novaSenha.isEmpty()) {
                    novaSenhaEditText.requestFocus();
                    novaSenhaEditText.setError("Campo obrigatório");
                } else if (confirmaNovaSenha.isEmpty()) {
                    confirmaNovaSenhaEditText.requestFocus();
                    confirmaNovaSenhaEditText.setError("Campo obrigatório");
                } else {
                    if(senhaAntiga.equals(novaSenha)){
                        //verifica se a senha atual é igual a nova
                        msgErroTextView.setText("A nova senha é igual a atual.");
                        confirmaNovaSenhaEditText.requestFocus();
                        return;
                    } else if (novaSenha.equals(confirmaNovaSenha)) {
                        //verifica se as senhas correspondem
                        String url = getResources().getString(R.string.base_url) + "/api/users/redefinirSenha/" + userId;
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("password", senhaAntiga);
                            jsonObject.put("nova_senha", novaSenha);
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
                                        Toast.makeText(getActivity(), "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                                        senhaAntigaEditText.setText("");
                                        novaSenhaEditText.setText("");
                                        confirmaNovaSenhaEditText.setText("");
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        //verifica o codigo de resposta do servidor
                                        if (error.networkResponse != null && error.networkResponse.statusCode != 200) {
                                            int statusCode = error.networkResponse.statusCode;
                                            if (statusCode == 401) {
                                                msgErroTextView.setText("Credenciais inválidas");
                                            } else if (statusCode == 422) {
                                                try {
                                                    String responseBody = new String(error.networkResponse.data, "utf-8");
                                                    JSONObject responseJson = new JSONObject(responseBody);
                                                    //verifica se a resposta contem o objeto "errors"
                                                    if (responseJson.has("errors")) {
                                                        JSONObject errors = responseJson.getJSONObject("errors");
                                                        //verifica se a chave "password" existe nos erros
                                                        if (errors.has("password")) {
                                                            JSONArray passwordErrors = errors.getJSONArray("password");
                                                            if (passwordErrors.length() > 0) {
                                                                String passwordErrorMessage = passwordErrors.getString(0);
                                                                msgErroTextView.setText(passwordErrorMessage);
                                                            }
                                                        }
                                                        //verifica se a chave "nova_senha" existe nos erros
                                                        if (errors.has("nova_senha")) {
                                                            JSONArray passwordErrors = errors.getJSONArray("nova_senha");
                                                            if (passwordErrors.length() > 0) {
                                                                String passwordErrorMessage = passwordErrors.getString(0);
                                                                msgErroTextView.setText(passwordErrorMessage);
                                                            }
                                                        }
                                                    }
                                                } catch (UnsupportedEncodingException |
                                                         JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    String responseBody = new String(error.networkResponse.data, "utf-8");
                                                    JSONObject responseJson = new JSONObject(responseBody);

                                                    if (responseJson.has("message")) {
                                                        String errorMessage = responseJson.getString("message");
                                                        msgErroTextView.setText(errorMessage);
                                                    }
                                                } catch (UnsupportedEncodingException |
                                                         JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    }
                                }) {
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                return headers;
                            }
                        };
                        RequestQueue fila = Volley.newRequestQueue(getContext());
                        fila.add(request);
                    } else {
                        //senhas não correspondem, exibir mensagem de erro
                        msgErroTextView.setText("As senhas não correspondem");
                    }
                }
            }
        });
        return view;
    }
}
