package com.example.petfinderapp;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.petfinderapp.model.Publicacao;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;


public class CriarPublicacaoFragment extends Fragment {

    private final String url = "http://192.168.0.115:8000/api/cadastroPublicacao";
    private Activity mActivity;

    //Spinner é o ComboBox
    Spinner spinnerPorte, spinnerCastrado, spinnerTipo, spinnerGenero;
    private EditText editTextNome, editTextIdade, editTextVacinas, editTextDescricao;
    private ImageView imagePet;
    private Button buttonAddFoto, buttonRemoveFoto, buttonCancelar, buttonCadastrar;
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
        int idUsuario = sharedPreferences.getInt("idUsuario", 0);
        Log.d("ID_USUARIO", "Id do usuário logado: " + idUsuario);
        View view = inflater.inflate(R.layout.fragment_criar_publicacao, container, false);

        spinnerPorte = view.findViewById(R.id.spinnerPorte);
        spinnerCastrado = view.findViewById(R.id.spinnerCastrado);
        spinnerTipo = view.findViewById(R.id.spinnerTipo);
        spinnerGenero = view.findViewById(R.id.spinnerGenero);

        editTextNome = view.findViewById(R.id.editTextNome);
        editTextIdade = view.findViewById(R.id.editTextIdade);
        editTextVacinas = view.findViewById(R.id.editTextVacinas);
        editTextDescricao = view.findViewById(R.id.editTextDescricao);

        imagePet = view.findViewById(R.id.imagePet);
        buttonAddFoto = view.findViewById(R.id.buttonAddFoto);
        buttonRemoveFoto = view.findViewById(R.id.buttonRemoveFoto);

        buttonCancelar = view.findViewById(R.id.buttonCancelar);
        buttonCadastrar = view.findViewById(R.id.buttonCadastrar);

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

        buttonCadastrar.setOnClickListener(view ->{
            //Pega os valores digitados nos campos de texto do XML
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
                return;
            }
            //......
            //VER O QUE É NECESSARIO VALIDAR

            JSONObject jsonObject = new JSONObject();
            int imagemStatus = Integer.parseInt(imagePet.getTag().toString());
            if (imagemStatus == 1){
                Publicacao publicacao = new Publicacao(0, String descricao, String nomePet, String genero, String tipo, String porte, int idade, String vacinas, String castrado, idUsuario);
                try {
                    jsonObject.put("descricao", publicacao.getDescricao());
                    jsonObject.put("nomePet", publicacao.getNomePet());
                    jsonObject.put("genero", publicacao.getGenero());
                    jsonObject.put("tipo", publicacao.getTipo());
                    jsonObject.put("porte", publicacao.getPorte());
                    jsonObject.put("idade", publicacao.getIdade());
                    jsonObject.put("vacinas", publicacao.getVacinas());
                    jsonObject.put("castrado", publicacao.getCastrado());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            } else {
                Bitmap bitmap = ((BitmapDrawable) imagePet.getDrawable()).getBitmap();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                String encodeImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Publicacao publicacao = new Publicacao(0, String descricao, String nomePet, String genero, String tipo, String porte, int idade, String vacinas, String castrado, String imagem, idUsuario);
                try {
                    jsonObject.put("descricao", publicacao.getDescricao());
                    jsonObject.put("nomePet", publicacao.getNomePet());
                    jsonObject.put("genero", publicacao.getGenero());
                    jsonObject.put("tipo", publicacao.getTipo());
                    jsonObject.put("porte", publicacao.getPorte());
                    jsonObject.put("idade", publicacao.getIdade());
                    jsonObject.put("vacinas", publicacao.getVacinas());
                    jsonObject.put("castrado", publicacao.getCastrado());
                    jsonObject.put("imagem", publicacao.getImagem());
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
        /*buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Pega os valores digitados nos campos de texto do XML
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
                    return;
                }
                //......
                //VER O QUE É NECESSARIO VALIDAR

                JSONObject jsonObject = new JSONObject();
                Publicacao publicacao = new Publicacao(nomePet, porte, idade, vacinas, castrado, genero, tipo, descricao, idUsuario);
                try {
                    jsonObject.put("nomePet", publicacao.getNomePet());
                    jsonObject.put("porte", publicacao.getPorte());
                    jsonObject.put("idade", publicacao.getIdade());
                    jsonObject.put("vacinas", publicacao.getVacinas());
                    jsonObject.put("castrado", publicacao.getCastrado());
                    jsonObject.put("genero", publicacao.getGenero());
                    jsonObject.put("tipo", publicacao.getTipo());
                    jsonObject.put("descricao", publicacao.getDescricao());
                    jsonObject.put("userId", idUsuario);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    msgCadastro.setText(response.getString("message"));
                                    mActivity.finish();
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
            }
        });*/

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

}