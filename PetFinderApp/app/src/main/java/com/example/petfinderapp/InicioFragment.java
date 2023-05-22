package com.example.petfinderapp;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.petfinderapp.model.Publicacao;
import com.example.petfinderapp.model.PublicacaoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InicioFragment extends Fragment {
    private final String url = "http://192.168.100.6:8000/api/publicacoes";
    //private final String url = "http://192.168.100.6:8000/api/publicacoes";

    private PublicacaoAdapter adapter;
    private RecyclerView rvPublicacao;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inicio, container, false);


        //cria uma lista de publicações (você pode buscar as publicações do banco de dados ou de uma API)
        List<Publicacao> publicacoes = obterPublicacoes();
        rvPublicacao = rootView.findViewById(R.id.recycler_view_inicio);
        rvPublicacao.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PublicacaoAdapter(publicacoes);
        rvPublicacao.setAdapter(adapter);
        //cria uma instância do PublicacaoAdapter e defina-o no RecyclerView
        PublicacaoAdapter adapter = new PublicacaoAdapter(publicacoes);
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
                        Toast.makeText(getContext(), "ok", Toast.LENGTH_SHORT).show();
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
                                boolean castrado = jsonPublicacao.getBoolean("castrado");
                                String imagem = jsonPublicacao.getString("base64_imagem");
                                long userId = jsonPublicacao.getLong("userId");

                                Publicacao publicacao = new Publicacao(id, descricao, nomePet, genero, especie, porte, idade, vacinas, castrado, imagem, userId);
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
                        Toast.makeText(getContext(), "Erro: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                });

        // Adicione a solicitação à RequestQueue
        requestQueue.add(jsonArrayRequest);

        return publicacoes;
    }
}


