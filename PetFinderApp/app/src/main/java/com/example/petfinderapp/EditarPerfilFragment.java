package com.example.petfinderapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.petfinderapp.model.PhoneMaskWatcher;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EditarPerfilFragment extends Fragment {

    private final String url = "http://187.52.53.112/api/atualizarUsuario";
    private EditText editTextNome, editTextCel, textNomeUser;
    private Button buttonSalvar;
    private TextView msgErro;
    ImageView imageEditarNome, imageEditarCelular;

    String nome, telefone;

    private SharedPreferences preferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        buttonSalvar = rootView.findViewById(R.id.buttonSalvar);
        msgErro = rootView.findViewById(R.id.msgErro);
        editTextNome = rootView.findViewById(R.id.editTextNome);
        editTextCel = rootView.findViewById(R.id.editTextCel);
        imageEditarNome = rootView.findViewById(R.id.imageEditarNome);
        imageEditarCelular = rootView.findViewById(R.id.imageEditarTel);

        buttonSalvar.setOnClickListener(view -> confirmarAtualizacao());

        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        long idUsuario = preferences.getLong("idUsuario", 0);
        String nomeAtual = preferences.getString("username", "");


        String celularAtual = ""; // Substitua pelo valor atual do celular do usuário
        editTextNome.setText(nomeAtual);
        editTextCel.setText(celularAtual);

        // Desabilite o EditText
        editTextNome.setEnabled(false);
        editTextCel.setEnabled(false);

        // Adicione um OnClickListener ao ImageView para habilitar o EditText quando clicado
        imageEditarNome.setOnClickListener(v -> {
            editTextNome.setEnabled(true);
            imageEditarNome.setEnabled(false);
            imageEditarCelular.setEnabled(true);

            if(editTextCel.isEnabled()){
                editTextCel.setEnabled(false);
            }
        });

        imageEditarCelular.setOnClickListener(v -> {
            editTextCel.setEnabled(true);
            imageEditarCelular.setEnabled(false);
            imageEditarNome.setEnabled(true);

            if(editTextNome.isEnabled()){
                editTextNome.setEnabled(false);
            }
        });

        return rootView;
    }

    private void confirmarAtualizacao() {
        String nome = editTextNome.getText().toString().trim();
        String telefone = editTextCel.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            editTextNome.requestFocus();
            msgErro.setError("Preencha o seu nome!");
            return;
        } else if (TextUtils.isEmpty(telefone)) {
            editTextCel.requestFocus();
            msgErro.setError("Preencha seu celular!");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmação de Atualização");
        builder.setMessage("Para confirmar a atualização do seu nome ou telefone, digite sua senha:");
        builder.setCancelable(false);
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_password, null);
        EditText editPassword = dialogView.findViewById(R.id.editSenha);
        builder.setView(dialogView);
        builder.setPositiveButton("Confirmar", (dialogInterface, i) -> {
            String senhaConfirmacao = editPassword.getText().toString().trim();
            if (!TextUtils.isEmpty(senhaConfirmacao)) {
                atualizarUsuario(nome, telefone, senhaConfirmacao);
            }
        });
        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void atualizarUsuario(String nome, String telefone, String senhaConfirmacao) {
        JSONObject jsonObject = new JSONObject();
        Usuario user = new Usuario(nome, telefone.replaceAll("[^0-9]", ""), senhaConfirmacao);
        try {
            jsonObject.put("name", user.getName());
            jsonObject.put("telefone", user.getTelefone());
            jsonObject.put("password", user.getPassword()); // Adicione a senha do usuário ao JSON
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Sucesso ao enviar os dados para o backend
                        Toast.makeText(requireActivity(), "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                        //aqui tem que redirecionar para o fragment de configuração
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Verifica o código de resposta do servidor
                        if (error.networkResponse != null && error.networkResponse.statusCode != 200) {
                            try {
                                String responseBody = new String(error.networkResponse.data, "utf-8");
                                JSONObject responseJson = new JSONObject(responseBody);
                                // Verifica se a resposta contém o objeto "message"
                                if (responseJson.has("message")) {
                                    String errorMessage = responseJson.getString("message");
                                    msgErro.setText(errorMessage);
                                }
                            } catch (UnsupportedEncodingException | JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            // Outros erros
                            msgErro.setText("Erro código 20. Contate o suporte");
                        }
                    }
                }
        );

        // Adiciona a requisição à fila do Volley
        RequestQueue fila = Volley.newRequestQueue(requireActivity());
        fila.add(request);
    }
}
