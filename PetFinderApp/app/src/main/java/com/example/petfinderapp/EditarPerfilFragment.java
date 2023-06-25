package com.example.petfinderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.makeramen.roundedimageview.RoundedDrawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
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
    private RadioButton radioButtonMasc,  radioButtonFem;

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
        radioButtonMasc = rootView.findViewById(R.id.radioButtonMasc);
        radioButtonFem = rootView.findViewById(R.id.radioButtonFem);

        desabilitaBotoes();

        //sessao
        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        Long userId = preferences.getLong("userId", -1);
        String authToken = preferences.getString("auth_token", null);

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
                    case R.id.radioButtonOutros:
                    case R.id.radioButtonFem:
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
        url = getResources().getString(R.string.base_url) + "/api/users/" +userId;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + authToken);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
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
                            if(user.getAvatar() != null){
                                imageViewPerfil.setTag(2);
                            }
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
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.remove("userId");
                            editor.remove("username");
                            editor.remove("avatar");
                            editor.remove("authToken");
                            editor.commit();
                            Intent intent = new Intent(getActivity(), Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers;
            }
        };
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
        try {
            String nome = editTextNome.getText().toString().trim();
            String telefone = editTextCel.getText().toString().trim();
            String dataNasc = editTextDataNasc.getText().toString().trim();
            String formatoEntrada = "dd/MM/yyyy";
            String formatoSaida = "yyyy-MM-dd";
            SimpleDateFormat formatadorEntrada = new SimpleDateFormat(formatoEntrada);
            SimpleDateFormat formatadorSaida = new SimpleDateFormat(formatoSaida);
            Date data = formatadorEntrada.parse(dataNasc);
            String dataFormatada = formatadorSaida.format(data);
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
                Bitmap bitmap = ((BitmapDrawable) imageViewPerfil.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String avatar = Base64.encodeToString(byteArray, Base64.DEFAULT);

                String senhaConfirmacao = editPassword.getText().toString().trim();
                long idUsuario = preferences.getLong("userId", 0);
                String authToken = preferences.getString("auth_token", null);
                String genero = "";
                if (radioButtonMasc.isChecked()) {
                    genero = "M";
                } else if(radioButtonFem.isChecked()){
                    genero = "F";
                } else {
                    genero = "O";
                }
                if (!TextUtils.isEmpty(senhaConfirmacao)) {
                    atualizarUsuario(nome, telefone, dataFormatada, genero, senhaConfirmacao, avatar, idUsuario, authToken);
                }
            });
            builder.setNegativeButton("Cancelar", null);
            AlertDialog dialog = builder.create();
            dialog.show();
        }catch (ParseException e){

        }
    }

    private void atualizarUsuario(String nome, String telefone, String dataNasc, String genero, String senhaConfirmacao, String avatar, long idUsuario, String authToken) {
        String url = getResources().getString(R.string.base_url) + "/api/users/" + idUsuario;
        JSONObject jsonObject = new JSONObject();
        try {
            int imageAtual = Integer.parseInt(imageViewPerfil.getTag().toString());
            if(imageAtual == 1) {
                jsonObject.put("name", nome);
                jsonObject.put("telefone", telefone.replaceAll("[^0-9]", ""));
                jsonObject.put("dataNasc", dataNasc);
                jsonObject.put("genero", genero);
                jsonObject.put("password", senhaConfirmacao);
            } else {
                jsonObject.put("name", nome);
                jsonObject.put("telefone", telefone.replaceAll("[^0-9]", ""));
                jsonObject.put("dataNasc", dataNasc);
                jsonObject.put("genero", genero);
                jsonObject.put("password", senhaConfirmacao);
                jsonObject.put("avatar", avatar);
            }
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
                        desabilitaBotoes();
                        try {
                            String message = response.getString("message");
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            JSONObject userData = response.getJSONObject("user");
                            long id = userData.getLong("id");
                            String name = userData.getString("name");
                            String telefone = userData.getString("telefone");
                            String dataNasc = userData.getString("dataNasc");
                            String genero = userData.getString("genero");
                            String avatar = userData.getString("avatar_link");
                            Usuario user = new Usuario(id, name, dataNasc, genero, telefone, avatar);
                            textNomeUser.setText(user.getName());
                            MainActivity mainActivity = (MainActivity) getActivity();
                            if (mainActivity != null) {
                                mainActivity.atualizaHeader(user.getName(), user.getAvatar());
                            }
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putLong("userId", user.getId());
                            editor.putString("username", user.getName());
                            editor.putString("avatar", user.getAvatar());
                            editor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        desabilitaBotoes();
                        if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                            msgErro.setText("Senha inválida.");
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
    }

    ActivityResultContract<String, Uri> getContent = new ActivityResultContracts.GetContent();
    ActivityResultLauncher<String> launcher = registerForActivityResult(getContent, new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            // Resultado da seleção de imagem
            if (result != null) {
                // URI da imagem selecionada
                Glide.with(getActivity())
                        .load(result)
                        .placeholder(R.drawable.fotoperfil)
                        .into(imageViewPerfil);
            }
        }
    });
    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), imageViewPerfil);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editImageOption:
                        launcher.launch("image/*");
                        imageViewPerfil.setTag(2);
                        return true;
                    case R.id.removeImageOption:
                        imageViewPerfil.setImageResource(R.drawable.fotoperfil);
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
