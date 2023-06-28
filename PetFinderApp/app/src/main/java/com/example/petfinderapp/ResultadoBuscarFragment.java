package com.example.petfinderapp;

import static android.content.Intent.getIntent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.petfinderapp.model.Publicacao;
import com.example.petfinderapp.model.PublicacaoAdapter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResultadoBuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResultadoBuscarFragment extends Fragment implements PublicacaoAdapter.OnImageClickListener{

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private SharedPreferences preferences;
    private RecyclerView rvPublicacao;
    private PublicacaoAdapter adapter;
    private List<Publicacao> publicacoes;
    private List<Long> favoritos = new ArrayList<>();
    private String url = "";

    public ResultadoBuscarFragment() {
        // Required empty public constructor
    }

//    public static ResultadoBuscarFragment newInstance(String param1, String param2) {
//        ResultadoBuscarFragment fragment = new ResultadoBuscarFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    public static ResultadoBuscarFragment newInstance(List<Publicacao> publicacoes) {
        ResultadoBuscarFragment fragment = new ResultadoBuscarFragment();
        Bundle args = new Bundle();
        args.putSerializable("publicacoes", (Serializable) publicacoes);
        System.out.println("pub resultado size:" + publicacoes.size());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if (getArguments() != null) {
            publicacoes = (List<Publicacao>) getArguments().getSerializable("publicacoes");
        }
    }

    @Override
    public void onImageClick(int position) {
        Publicacao publicacao = publicacoes.get(position);
        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        String authToken = preferences.getString("auth_token", null);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + authToken);
        if (favoritos.contains(publicacao.getId())) {
            url = getResources().getString(R.string.base_url) + "/api/favoritos/" + publicacao.getId();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            favoritos.remove(publicacao.getId());
                            adapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getContext(), "Error ao remover postagem", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            requestQueue.add(stringRequest);
        } else {
            url = getResources().getString(R.string.base_url) + "/api/favoritos/?publicacao_id=" + publicacao.getId();
            RequestQueue requestQueue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            favoritos.add(publicacao.getId());
                            adapter.notifyDataSetChanged();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // Request failed, handle the error
                            Toast.makeText(getContext(), "Error ao favoritar postagem", Toast.LENGTH_SHORT).show();
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_resultado_buscar, container, false);
        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        String authToken = preferences.getString("auth_token", null);
        Long usuarioId = preferences.getLong("userId", 0);
        if(usuarioId == 0){
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
        System.out.println("TOKEN here: " + authToken);

        //List<Publicacao> publicacoes = lista que recebe do BuscaFragment
//        if (getActivity().getIntent().hasExtra("publicacoes")) {
//            publicacoes = (List<Publicacao>) getActivity().getIntent().getSerializableExtra("publicacoes");
//        }

        System.out.println("pub resultado2 size:" + publicacoes.size());
        rvPublicacao = rootView.findViewById(R.id.recycler_view_resultado_buscar);
        rvPublicacao.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PublicacaoAdapter(publicacoes, this);
        rvPublicacao.setAdapter(adapter);
        return rootView;
    }
}