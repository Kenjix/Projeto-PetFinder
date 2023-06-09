package com.example.petfinderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.petfinderapp.model.MinhasPublicacoesAdapter;
import com.example.petfinderapp.model.Publicacao;
import com.example.petfinderapp.model.PublicacaoAdapter;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MinhasPublicacoesFragment extends Fragment implements PublicacaoAdapter.OnImageClickListener {
    private String url = "";
    private ImageView imageViewPerfil;
    private TextView textNomeUser;
    private SharedPreferences preferences;
    private MinhasPublicacoesAdapter adapter;
    private RecyclerView rvPublicacao;
    private List<Publicacao> publicacoes = new ArrayList<>();


    public MinhasPublicacoesFragment() {
    }

    @Override
    public void onImageClick(int position) {
        Publicacao publicacao = publicacoes.get(position);

        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        String authToken = preferences.getString("auth_token", null);
        Long usuarioId = preferences.getLong("userId", 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Confirmação");
        builder.setMessage("Deseja mesmo excluir está publicação?");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", (dialogInterface, i) -> {

            //request para desabilita publicacao
            url = getResources().getString(R.string.base_url) + "/api/publicacao/" + publicacao.getId();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "application/json");
            headers.put("Authorization", "Bearer " + authToken);
            StringRequest request = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Toast.makeText(getActivity(), "Publicação excluída com sucesso.", Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getActivity(), "Erro ao excluir a publicação.", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            requestQueue.add(request);
        });
        builder.setNegativeButton("Cancelar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_minhas_publicacoes, container, false);
        textNomeUser = rootView.findViewById(R.id.textNomeUser);
        imageViewPerfil = rootView.findViewById(R.id.imageViewPerfil);

        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        String authToken = preferences.getString("auth_token", null);
        Long usuarioId = preferences.getLong("userId", 0);
        if (usuarioId == 0) {
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

        List<Publicacao> publicacoes = obterPublicacoes(authToken, usuarioId);
        rvPublicacao = rootView.findViewById(R.id.recycler_view_minha_publicacao);
        rvPublicacao.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new MinhasPublicacoesAdapter(publicacoes, this::onImageClick);
        rvPublicacao.setAdapter(adapter);
        return rootView;
    }

    private List<Publicacao> obterPublicacoes(String authToken, Long usuarioId) {
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        url = getResources().getString(R.string.base_url) + "/api/publicacao/" + usuarioId;
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + authToken);
        //cria uma solicitacao GET para a URL da API
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //processar a resposta JSON e cria objeto Publicacao
                        try {
                            JSONArray dataArray = response.getJSONArray("data");

                            for (int i = 0; i < dataArray.length(); i++) {
                                JSONObject jsonData = dataArray.getJSONObject(i);
                                JSONObject jsonPublicacao = jsonData.getJSONObject("publicacao");
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

                                JSONObject jsonUser = jsonData.getJSONObject("user");
                                long userId = jsonUser.getLong("id");
                                String userName = jsonUser.getString("name");
                                String generoUser = jsonUser.getString("genero");
                                String dataNasc = jsonUser.getString("dataNasc");
                                String telefone = jsonUser.getString("telefone");
                                String avatar = jsonUser.getString("avatar_link");
                                textNomeUser.setText(userName);
                                if (avatar != null) {
                                    Glide.with(getActivity())
                                            .load(avatar)
                                            .placeholder(R.drawable.fotoperfil)
                                            .into(imageViewPerfil);
                                }
                                Usuario user = new Usuario(userId, userName, dataNasc, generoUser, telefone, avatar);
                                Publicacao publicacao = new Publicacao(id, descricao, nomePet, genero, especie, porte, idade, vacinas, castrado, imagem, user);
                                publicacoes.add(publicacao);
                            }
                            //atualiza o adaptador com a lista de publicações obtida
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            Toast.makeText(getContext(), "Falha ao consultar publicações", Toast.LENGTH_SHORT).show();
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
        //adiciona a solicitacao a fila
        requestQueue.add(request);
        return publicacoes;
    }
}