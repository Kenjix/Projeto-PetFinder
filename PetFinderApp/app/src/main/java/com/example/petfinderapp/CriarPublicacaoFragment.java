package com.example.petfinderapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
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

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petfinderapp.model.Publicacao;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

public class CriarPublicacaoFragment extends Fragment {
    private static final int REQUEST_STORAGE_PERMISSION = 100;
    //D
    private final String url = "http://192.168.100.6:8000/api/cadastroPublicacao";
    //G
    //private final String url = "http://192.168.0.115:8000/api/cadastroPublicacao";
    //Spinner é o ComboBox
    Spinner spinnerPorte, spinnerCastrado, spinnerEspecie, spinnerGenero;
    private ImageView photoImageView;
    private EditText editTextNome, editTextIdade, editTextVacinas, editTextDescricao;
    private Button buttonCancelar, buttonCadastrar;
    private TextView msgRetorno;
    private SharedPreferences preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        long idUsuario = preferences.getLong("idUsuario", 0);

        View view = inflater.inflate(R.layout.fragment_criar_publicacao, container, false);

        spinnerPorte = view.findViewById(R.id.spinnerPorte);
        spinnerCastrado = view.findViewById(R.id.spinnerCastrado);
        spinnerEspecie = view.findViewById(R.id.spinnerEspecie);
        spinnerGenero = view.findViewById(R.id.spinnerGenero);

        editTextNome = view.findViewById(R.id.editTextNome);
        editTextIdade = view.findViewById(R.id.editTextIdade);
        editTextVacinas = view.findViewById(R.id.editTextVacinas);
        editTextDescricao = view.findViewById(R.id.editTextDescricao);

        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        buttonCadastrar = view.findViewById(R.id.buttonCadastrar);

        photoImageView = view.findViewById(R.id.photoImageView);
        msgRetorno = view.findViewById(R.id.msgRetorno);

        Button selectPhotoButton = view.findViewById(R.id.selectPhotoButton);
        selectPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verificar permissão de acesso à galeria
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //solicita permissão
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

        ArrayAdapter<CharSequence> especieAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.especiePet, android.R.layout.simple_spinner_item);
        especieAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspecie.setAdapter(especieAdapter);

        ArrayAdapter<CharSequence> generoAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.generoPet, android.R.layout.simple_spinner_item);
        generoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(generoAdapter);

        ArrayAdapter<CharSequence> castradoAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.castradoPet, android.R.layout.simple_spinner_item);
        castradoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCastrado.setAdapter(castradoAdapter);

        //evento botao cancelar
        buttonCancelar.setOnClickListener(clickView -> {
            //cria uma Intent para iniciar a atividade principal
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            //finalizar a activity atual
            getActivity().finish();
        });

        //evento botao cadastrar
        buttonCadastrar.setOnClickListener(clickView -> {
            msgRetorno.setText("");
            //pega os valores digitados nos campos de texto do XML
            String nomePet = editTextNome.getText().toString();
            String porte = spinnerPorte.getSelectedItem().toString();
            int idade = -1;
            try {
                idade = Integer.parseInt(editTextIdade.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace(); // Ou qualquer outra lógica para tratar o erro
            }
            String vacinas = editTextVacinas.getText().toString();
            boolean castrado = (spinnerCastrado.getSelectedItem().toString() != null && spinnerCastrado.getSelectedItem().toString().equals("Sim")) ? true :
                    (spinnerCastrado.getSelectedItem().toString() != null && spinnerCastrado.getSelectedItem().toString().equals("Não")) ? false : null;
            String genero = spinnerGenero.getSelectedItem().toString();
            String especie = spinnerEspecie.getSelectedItem().toString();
            String descricao = editTextDescricao.getText().toString();
            //validacoes dos campos
            if (nomePet.isEmpty()) {
                editTextNome.requestFocus();
                editTextNome.setError("Campo obrigatório!");
                return;
            } else if(porte.equals("Selecione")){
                msgRetorno.setText("Selecione um porte.");
                return;
            } else if(idade == -1){
                editTextIdade.requestFocus();
                editTextIdade.setError("Campo obrigatório!");
                return;
            } else if(spinnerCastrado.getSelectedItem().toString().equals("Selecione")) {
                msgRetorno.setText("Selecione se o pet é castrado");
                return;
            } else if(genero.equals("Selecione")) {
                msgRetorno.setText("Selecione o genero");
                return;
            } else if(especie.equals("Selecione")) {
                msgRetorno.setText("Selecione a espécie");
                return;
            } else if (photoImageView.getDrawable() == null) {
                msgRetorno.setText("Selecione uma foto do pet");
                return;
            }
            JSONObject jsonObject = new JSONObject();
            /*//converte a imagem em Base64
            Bitmap imageBitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT);*/

            //redimenciona 800x600 e converte para base64
            Bitmap imageBitmap = ((BitmapDrawable) photoImageView.getDrawable()).getBitmap();
            int width = imageBitmap.getWidth();
            int height = imageBitmap.getHeight();
            float scaleWidth = ((float) 800) / width;
            float scaleHeight = ((float) 600) / height;
            float scale = Math.min(scaleWidth, scaleHeight);
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);
            Bitmap resizedBitmap = Bitmap.createBitmap(imageBitmap, 0, 0, width, height, matrix, true);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            String base64Image = Base64.encodeToString(byteArray, Base64.DEFAULT);

            Publicacao publicacao = new Publicacao(descricao, nomePet, genero, especie, porte, idade, vacinas, castrado, idUsuario);
            msgRetorno.setText("DESC: " + publicacao.getDescricao() + "\nnome pet: " + publicacao.getNomePet() + "\ngenero: " + publicacao.getGenero()
                    + "\nespecie: " + publicacao.getEspecie() + "\nporte: " + publicacao.getPorte() + "\nidade: " + publicacao.getIdade()
                    + "\nvacinas: " + publicacao.getVacinas() + "\nCastado: " + publicacao.isCastrado() + "\nUID: " + idUsuario + "\nIMG: " + base64Image);
            try {
                jsonObject.put("nomePet", publicacao.getNomePet());
                jsonObject.put("porte", publicacao.getPorte());
                jsonObject.put("idade", publicacao.getIdade());
                jsonObject.put("vacinas", publicacao.getVacinas());
                jsonObject.put("castrado", publicacao.isCastrado());
                jsonObject.put("genero", publicacao.getGenero());
                jsonObject.put("especie", publicacao.getEspecie());
                jsonObject.put("descricao", publicacao.getDescricao());
                jsonObject.put("user_id", publicacao.getUserId());
                jsonObject.put("image", base64Image);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //request
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                msgRetorno.setText(response.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //verifica o código de resposta do servidor
                            if (error.networkResponse != null && error.networkResponse.statusCode != 200) {
                                try {
                                    String responseBody = new String(error.networkResponse.data, "utf-8");
                                    JSONObject responseJson = new JSONObject(responseBody);

                                    //verifica se a resposta contém o objeto "errors"
                                    if (responseJson.has("errors")) {
                                        JSONObject errors = responseJson.getJSONObject("errors");
                                        //itera pelos campos de erro e obtém a primeira mensagem de erro
                                        Iterator<String> keys = errors.keys();
                                        if (keys.hasNext()) {
                                            String field = keys.next();
                                            JSONArray fieldErrors = errors.getJSONArray(field);

                                            //obtem a primeira mensagem de erro
                                            if (fieldErrors.length() > 0) {
                                                String msgErro = fieldErrors.getString(0);
                                                msgRetorno.setText(msgErro);
                                            }
                                        }
                                    }
                                } catch (UnsupportedEncodingException | JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                //outros erros
                                msgRetorno.setText("Erro código 21. Contate o suporte");
                            }
                        }
                    });
            RequestQueue fila = Volley.newRequestQueue(getContext());
            fila.add(request);
        });
        return view;
    }
    //imagem
    ActivityResultContract<String, Uri> getContent = new ActivityResultContracts.GetContent();
    ActivityResultLauncher<String> launcher = registerForActivityResult(getContent, new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri result) {
            //resultado da seleção de imagem
            if (result != null) {
                //URI da imagem selecionada
                photoImageView.setImageURI(result);
            }
        }
    });
    public void selectPhotoClick() {
        launcher.launch("image/*");
    }
}