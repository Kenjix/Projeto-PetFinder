package com.example.petfinderapp;

import static android.content.Context.MODE_PRIVATE;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CriarPublicacaoFragment extends Fragment {

    private static final int REQUEST_IMAGE_PICK = 1;

    private static final int REQUEST_STORAGE_PERMISSION = 100;

    private ImageView photoImageView;

    //D
    //private final String url = "http://192.168.100.6:8000/api/cadastroPublicacao";

    //G
    private final String url = "http://192.168.0.115:8000/api/cadastroPublicacao";

    //Spinner é o ComboBox
    Spinner spinnerPorte, spinnerCastrado, spinnerTipo, spinnerGenero;
    private EditText editTextNome, editTextIdade, editTextVacinas, editTextDescricao;
    private ImageView meuImageView;
    private Button buttonCancelar, buttonCadastrar;
    private TextView msgCadastro;

    public CriarPublicacaoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getContext();
        SharedPreferences sharedPreferences = context.getSharedPreferences("sessao", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String sessao = sharedPreferences.getString("username", "");
        //long idUsuario = sharedPreferences.getInt("idUsuario", 0);
        //Log.d("ID_USUARIO", "Id do usuário logado: " + idUsuario);
        View view = inflater.inflate(R.layout.fragment_criar_publicacao, container, false);

        spinnerPorte = view.findViewById(R.id.spinnerPorte);
        spinnerCastrado = view.findViewById(R.id.spinnerCastrado);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        spinnerGenero = view.findViewById(R.id.spinnerGenero);

        editTextNome = view.findViewById(R.id.editTextNome);
        editTextIdade = view.findViewById(R.id.editTextIdade);
        editTextVacinas = view.findViewById(R.id.editTextVacinas);
        editTextDescricao = view.findViewById(R.id.editTextDescricao);

        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        buttonCadastrar = view.findViewById(R.id.buttonCadastrar);

        photoImageView = view.findViewById(R.id.photoImageView);

        Button selectPhotoButton = view.findViewById(R.id.selectPhotoButton);
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar permissão de acesso à galeria
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    // Solicitar permissão
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_STORAGE_PERMISSION);
                } else {
                    selectPhotoClick();
                }
            }
        });

        //esse arrayadaper esta refereciando os itens do combobox que estão no Values/String.xml
        ArrayAdapter<CharSequence> porteAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.portePet, android.R.layout.simple_spinner_item);
        porteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPorte.setAdapter(porteAdapter);

        ArrayAdapter<CharSequence> tipoAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.tipoPet, android.R.layout.simple_spinner_item);
        tipoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(tipoAdapter);

        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.generoPet, android.R.layout.simple_spinner_item);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(generoAdapter);

        ArrayAdapter<CharSequence> castradoAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.castradoPet, android.R.layout.simple_spinner_item);
        castradoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCastrado.setAdapter(castradoAdapter);

        Button buttonCadastrar = view.findViewById(R.id.buttonCadastrar);
        buttonCadastrar.setOnClickListener(clickView -> {
            // Pega os valores digitados nos campos de texto do XML
            String nomePet = editTextNome.getText().toString();
            String porte = spinnerPorte.getSelectedItem().toString();
            int idade = Integer.parseInt(editTextIdade.getText().toString());
            String vacinas = editTextVacinas.getText().toString();
            String castrado = spinnerCastrado.getSelectedItem().toString();
            String genero = spinnerGenero.getSelectedItem().toString();
            String tipo = spinnerTipo.getSelectedItem().toString();
            String descricao = editTextDescricao.getText().toString();

            if (nomePet.isEmpty()) {
                editTextNome.setError("Campo obrigatório!");
            }

            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("descricao", descricao);
                jsonObject.put("nomePet", nomePet);
                jsonObject.put("genero", genero);
                jsonObject.put("tipo", tipo);
                jsonObject.put("porte", porte);
                jsonObject.put("idade", idade);
                jsonObject.put("vacinas", vacinas);
                jsonObject.put("castrado", castrado);
                jsonObject.put("user_id", 1);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // Verifica se uma imagem foi selecionada
            if (photoImageView.getDrawable() != null) {
                // Obtém o bitmap da imagem do ImageView
                Bitmap bitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
                // Converte o bitmap para base64
                String base64Image = bitmapToBase64(bitmap);
                try {
                    // Adiciona a imagem codificada no JSON
                    jsonObject.put("imagem", base64Image);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            Log.d("JSON_ENVIADO", "JSON enviado para o servidor: " + jsonObject.toString());


            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(getContext(), response.getString("resultado"), Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }
            );
            RequestQueue fila = Volley.newRequestQueue(getContext());
            fila.add(request);
        });


        return view;
    }

    public void selectPhotoClick() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == getActivity().RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            Bitmap bitmap = getBitmapFromUri(selectedImage);
            photoImageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectPhotoClick();
            } else {
                // Permissão negada, informe o usuário ou execute uma ação alternativa
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        try {
            return MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadBitmap(Bitmap bitmap) {
        try {
            JSONObject jsonObject = new JSONObject();
            String base64Image = bitmapToBase64(bitmap);
            jsonObject.put("image", base64Image);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String message = response.getString("message");
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Erro no envio da imagem", Toast.LENGTH_SHORT).show();
                        }
                    });

            Volley.newRequestQueue(requireContext()).add(request);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }
}