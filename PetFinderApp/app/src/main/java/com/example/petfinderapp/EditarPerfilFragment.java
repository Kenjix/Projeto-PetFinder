package com.example.petfinderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.bumptech.glide.Glide;
import com.example.petfinderapp.model.DatePickerDialog;
import com.example.petfinderapp.model.PhoneMaskWatcher;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EditarPerfilFragment extends Fragment {
    private String url = "";
    private EditText editTextNome, editTextCel, editTextDataNasc;
    private Button buttonSalvar;
    private TextView msgErro, textNomeUser;
    private ImageView imageEditarNome, imageEditarCelular, imageEditarDataNasc, imageViewPerfil, editIcon;
    private SharedPreferences preferences;
    private RadioGroup radioGroupGenero;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editar_perfil, container, false);

        buttonSalvar = rootView.findViewById(R.id.buttonSalvar);
        msgErro = rootView.findViewById(R.id.msgErro);
        textNomeUser = rootView.findViewById(R.id.textNomeUser);
        editTextNome = rootView.findViewById(R.id.editTextNome);
        editTextCel = rootView.findViewById(R.id.editTextCel);
        editTextDataNasc = rootView.findViewById(R.id.editTextDataNasc);
        imageEditarNome = rootView.findViewById(R.id.imageEditarNome);
        imageEditarCelular = rootView.findViewById(R.id.imageEditarTel);
        imageEditarDataNasc = rootView.findViewById(R.id.imageEditarDataNasc);
        imageViewPerfil = rootView.findViewById(R.id.imageViewPerfil);
        radioGroupGenero = rootView.findViewById(R.id.radioGroupGenero);
        editIcon = rootView.findViewById(R.id.editIcon);

        desabilitaBotoes();

        //sessao
        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        Long userId = preferences.getLong("userId", -1);

        //listeners
        editTextCel.addTextChangedListener(new PhoneMaskWatcher(editTextCel));

        buttonSalvar.setOnClickListener(view -> confirmarAtualizacao());
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
        imageViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });
        radioGroupGenero.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButtonMasc:
                        desabilitaBotoes();
                        break;
                    case R.id.radioButtonFem:
                        desabilitaBotoes();
                        break;
                    case R.id.radioButtonOutros:
                        desabilitaBotoes();
                        break;
                }
            }
        });

        imageEditarNome.setOnClickListener(v -> {
            if(editTextNome.isEnabled()){
                editTextNome.setEnabled(false);
            } else {
                editTextNome.requestFocus();
                editTextNome.setEnabled(true);
                editTextCel.setEnabled(false);
                editTextDataNasc.setEnabled(false);
            }
        });

        imageEditarCelular.setOnClickListener(v -> {
            if(editTextCel.isEnabled()){
                editTextCel.setEnabled(false);
            } else {
                editTextCel.requestFocus();
                editTextCel.setEnabled(true);
                editTextNome.setEnabled(false);
                editTextDataNasc.setEnabled(false);
            }
        });

        imageEditarDataNasc.setOnClickListener(v -> {
            editTextCel.setEnabled(false);
            editTextNome.setEnabled(false);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), editTextDataNasc, 2);
            datePickerDialog.showDatePickerDialog();
        });

        //request
        url = getResources().getString(R.string.base_url) + "/api/getUser/" +userId;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject userObject = response.getJSONObject("user");
                            long id = userObject.getLong("id");
                            String nome = userObject.getString("name");
                            String dataNasc = userObject.getString("dataNasc");
                            String genero = userObject.getString("genero");
                            String telefone = userObject.getString("telefone");
                            String avatar = userObject.getString("avatar_link");
                            Usuario user = new Usuario(id, nome, dataNasc, genero, telefone, avatar);

                            Glide.with(getActivity())
                                    .load(user.getAvatar())
                                    .placeholder(R.drawable.fotoperfil)
                                    .into(imageViewPerfil);
                            editTextNome.setText(user.getName());
                            textNomeUser.setText(user.getName());
                            editTextCel.setText(user.getTelefone());
                            if(genero.equals("M")){
                                radioGroupGenero.check(R.id.radioButtonMasc);
                            } else if(genero.equals("F")){
                                radioGroupGenero.check(R.id.radioButtonFem);
                            } else {
                                radioGroupGenero.check(R.id.radioButtonOutros);
                            }
                            String formatoEntrada = "yyyy-MM-dd";
                            String formatoSaida = "dd/MM/yyyy";
                            SimpleDateFormat formatadorEntrada = new SimpleDateFormat(formatoEntrada);
                            SimpleDateFormat formatadorSaida = new SimpleDateFormat(formatoSaida);
                            Date data = formatadorEntrada.parse(user.getDataNasc());
                            String dataFormatada = formatadorSaida.format(data);
                            editTextDataNasc.setText(dataFormatada);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "ERRO", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
        return rootView;
    }

    //desabilita os campos
    private void desabilitaBotoes() {
        editTextNome.setEnabled(false);
        editTextCel.setEnabled(false);
        editTextDataNasc.setEnabled(false);
    }

    private void confirmarAtualizacao() {
        String nome = editTextNome.getText().toString().trim();
        String telefone = editTextCel.getText().toString().trim();

        if (TextUtils.isEmpty(nome)) {
            editTextNome.requestFocus();
            editTextNome.setError("Preencha o seu nome!");
            return;
        } else if (TextUtils.isEmpty(telefone)) {
            editTextCel.requestFocus();
            editTextCel.setError("Preencha seu celular!");
            return;
        } else if (telefone.length() < 15) {
            editTextCel.setEnabled(true);
            editTextCel.requestFocus();
            editTextCel.setError("Número de celular incompleto!");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmação de Atualização");
        builder.setMessage("Para confirmar a atualização de seus dados, digite sua senha:");
        builder.setCancelable(false);
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_password, null);
        EditText editPassword = dialogView.findViewById(R.id.editSenha);
        builder.setView(dialogView);
        builder.setPositiveButton("Confirmar", (dialogInterface, i) -> {
            String senhaConfirmacao = editPassword.getText().toString().trim();
            long idUsuario = preferences.getLong("userId", 0);
            if (!TextUtils.isEmpty(senhaConfirmacao)) {
                atualizarUsuario(nome, telefone, senhaConfirmacao, idUsuario);
            }
        });
        builder.setNegativeButton("Cancelar", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void atualizarUsuario(String nome, String telefone, String senhaConfirmacao, long idUsuario) {
        String url = "http://192.168.100.2/api/atualizarUser" + idUsuario;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name", nome);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //request
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {


                    }
                });
        RequestQueue fila = Volley.newRequestQueue(getContext());
        fila.add(request);
    }



    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), imageViewPerfil);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editImageOption:
                        // Ação para editar imagem
                        Toast.makeText(getActivity(), "Editar imagem selecionado - TAG: " + imageViewPerfil.getTag(), Toast.LENGTH_SHORT).show();
                        imageViewPerfil.setTag(2);
                        return true;
                    case R.id.removeImageOption:
                        // Ação para remover imagem
                        Toast.makeText(getActivity(), "Remover imagem selecionado - TAG: " + imageViewPerfil.getTag(), Toast.LENGTH_SHORT).show();
                        imageViewPerfil.setTag(getResources().getInteger(R.integer.image_tag));
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
}
