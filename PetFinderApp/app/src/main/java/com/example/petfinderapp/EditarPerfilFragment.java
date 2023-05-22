package com.example.petfinderapp;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.example.petfinderapp.model.PhoneMaskWatcher;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class EditarPerfilFragment extends Fragment {

    private final String url = "http://192.168.0.115:8000/api/atualizarUsuario";
    private EditText editNome, editCelular;
    private Button buttonSalvar;
    private TextView msgErro;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        editNome = rootView.findViewById(R.id.editNome);
        editCelular = rootView.findViewById(R.id.editCelular);
        buttonSalvar = rootView.findViewById(R.id.buttonSalvar);
        msgErro = rootView.findViewById(R.id.msgErro);
        editCelular.addTextChangedListener(new PhoneMaskWatcher(editCelular));

        buttonSalvar.setOnClickListener(view -> confirmarAtualizacao());

        // Preencher automaticamente os campos de nome e telefone
        Bundle bundle = getArguments();
        if (bundle != null) {
            String nome = bundle.getString("nome");
            String telefone = bundle.getString("telefone");
            if (!TextUtils.isEmpty(nome)) {
                editNome.setText(nome);
            }
            if (!TextUtils.isEmpty(telefone)) {
                editCelular.setText(telefone);
            }
        }

        return rootView;
    }

    private void confirmarAtualizacao() {
        String nome = editNome.getText().toString().trim();
        String telefone = editCelular.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            editNome.requestFocus();
            editNome.setError("Campo obrigatório!");
            return;
        } else if (TextUtils.isEmpty(telefone)) {
            editCelular.requestFocus();
            editCelular.setError("Campo obrigatório!");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmação de Atualização");
        builder.setMessage("Para confirmar a atualização do seu nome e telefone, digite sua senha:");
        builder.setCancelable(false);
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_password, null);
        EditText editPassword = dialogView.findViewById(R.id.editSenha);
        builder.setView(dialogView);
        builder.setPositiveButton("Confirmar", (dialogInterface, i) -> {
            String senhaConfirmacao = editPassword.getText().toString().trim();
            if (!TextUtils.isEmpty(senhaConfirmacao)) {
                atualizarUsuario(nome, telefone);
            }
        });
        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void atualizarUsuario(String nome, String telefone) {
        JSONObject jsonObject = new JSONObject();
        Usuario user = new Usuario(nome, telefone.replaceAll("[^0-9]", ""));
        try {
            jsonObject.put("name", user.getName());
            jsonObject.put("telefone", user.getTelefone());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                response -> {
                    // Sucesso ao enviar os dados para o backend
                    Toast.makeText(requireActivity(), "Dados atualizados com sucesso!", Toast.LENGTH_SHORT).show();
                    // Redirecionar para a tela ConfiguracoesFragment
                    // ...
                },
                error -> {
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
        );

        // Adiciona a requisição à fila do Volley
        RequestQueue fila = Volley.newRequestQueue(requireActivity());
        fila.add(request);
    }
}
