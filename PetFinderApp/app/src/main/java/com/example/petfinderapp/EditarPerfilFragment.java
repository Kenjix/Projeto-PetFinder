package com.example.petfinderapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.petfinderapp.model.PhoneMaskWatcher;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EditarPerfilFragment extends Fragment {

    //D
    //private final String url = "http://192.168.100.6:8000/api/cadastro";
    //G
    private final String url = "http://192.168.0.115:8000/api/cadastro";
    private EditText editNome, editEmail, editCelular;
    private Button buttonSalvar;
    private TextView msgErro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        editNome = rootView.findViewById(R.id.editNome);
        editEmail = rootView.findViewById(R.id.editEmail);
        editCelular = rootView.findViewById(R.id.editCelular);
        buttonSalvar = rootView.findViewById(R.id.buttonSalvar);
        msgErro = rootView.findViewById(R.id.msgErro);
        editCelular.addTextChangedListener(new PhoneMaskWatcher(editCelular));

        buttonSalvar.setOnClickListener(view -> editarUsuario());

        return rootView;
    }

    private void editarUsuario() {
        String nome = editNome.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String telefone = editCelular.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            editNome.requestFocus();
            editNome.setError("Campo obrigatório!");
            return;
        } else if (TextUtils.isEmpty(email)) {
            editEmail.requestFocus();
            editEmail.setError("Campo obrigatório!");
            return;
        } else if (TextUtils.isEmpty(telefone)) {
            editCelular.requestFocus();
            editCelular.setError("Campo obrigatório!");
            return;
        }

        JSONObject jsonObject = new JSONObject();
        Usuario user = new Usuario(nome, email, telefone.replaceAll("[^0-9]", ""));
        try {
            jsonObject.put("name", user.getName());
            jsonObject.put("email", user.getEmail());
            jsonObject.put("password", user.getPassword());
            jsonObject.put("telefone", user.getTelefone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    buttonSalvar.setEnabled(false);
                    final int delayMilissegundos = 1000; //intervalo de atualização em milissegundos
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        int segundosRestantes = 5;

                        @Override
                        public void run() {
                            msgErro.setText("Salvo com sucesso! \nRedirecionando em " + segundosRestantes + " segundos...");
                            segundosRestantes--;

                            if (segundosRestantes >= 0) {
                                handler.postDelayed(this, delayMilissegundos);
                            } else {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    }, delayMilissegundos);
                },
                error -> {
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
                                        msgErro.setText(emailErrorMessage);
                                    }
                                }
                                //verifica se a chave "name" existe nos erros
                                if (errors.has("name")) {
                                    JSONArray passwordErrors = errors.getJSONArray("name");
                                    if (passwordErrors.length() > 0) {
                                        String nameErrorMessage = passwordErrors.getString(0);
                                        msgErro.setText(nameErrorMessage);
                                    }
                                }
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        //outros erros
                        msgErro.setText("Erro código 20. Contate o suporte");
                    }
                }
        );
        //adiciona a requisição à fila do Volley
        RequestQueue fila = Volley.newRequestQueue(requireActivity());
        fila.add(request);
    }
}
