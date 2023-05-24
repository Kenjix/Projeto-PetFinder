package com.example.petfinderapp;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Transition;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.petfinderapp.model.Publicacao;
import com.example.petfinderapp.model.PublicacaoAdapter;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InicioFragment extends Fragment {

    //D
    //private final String url = "http://187.52.53.112/api/publicacoes";

    //G
    //private final String url = "http://192.168.0.115:8000/api/publicacoes";
    //WEB
    private final String url = "http://187.52.53.112:8013/api/publicacoes";

    private PublicacaoAdapter adapter;
    private RecyclerView rvPublicacao;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);

        List<Publicacao> publicacoes = obterPublicacoes();
        rvPublicacao = rootView.findViewById(R.id.recycler_view_inicio);
        rvPublicacao.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PublicacaoAdapter(publicacoes);
        rvPublicacao.setAdapter(adapter);
        return rootView;
    }

    private List<Publicacao> obterPublicacoes() {
        List<Publicacao> publicacoes = new ArrayList<>();
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        //cria uma solicitação GET para a URL da API
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        //processar a resposta JSON e cria objeto Publicacao
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonPublicacao = response.getJSONObject(i);
                                long id = jsonPublicacao.getLong("id");
                                String descricao = jsonPublicacao.getString("descricao");
                                String nomePet = jsonPublicacao.getString("nomePet");
                                String genero = jsonPublicacao.getString("genero");
                                String especie = jsonPublicacao.getString("especie");
                                String porte = jsonPublicacao.getString("porte");
                                String idade = jsonPublicacao.getString("idade");
                                String vacinas = jsonPublicacao.getString("vacinas");
                                String castradoStr = jsonPublicacao.getString("castrado");
                                boolean castrado = castradoStr.equals("1") ? true : false;
                                String imagem = jsonPublicacao.getString("image_link");
                                long userId = jsonPublicacao.getLong("user_id");

                                JSONObject jsonUser = jsonPublicacao.getJSONObject("user");
                                String userName = jsonUser.getString("name");
                                String userAvatar = jsonUser.getString("avatar");
                                Usuario user = new Usuario(userId, userName, userAvatar);

                                Publicacao publicacao = new Publicacao(id, descricao, nomePet, genero, especie, porte, idade, vacinas, castrado, imagem, user);
                                publicacoes.add(publicacao);
                            }
                            //atualiza o adaptador com a lista de publicações obtida
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        // Adicione a solicitação à RequestQueue
        requestQueue.add(jsonArrayRequest);
        return publicacoes;
    }
}


