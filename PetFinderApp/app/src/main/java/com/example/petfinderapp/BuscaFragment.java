package com.example.petfinderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.petfinderapp.model.Publicacao;
import com.example.petfinderapp.model.PublicacaoAdapter;
import com.example.petfinderapp.model.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BuscaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private SharedPreferences preferences;
    private PublicacaoAdapter adapter;
    public BuscaFragment() {
        // Required empty public constructor
    }

    public static BuscaFragment newInstance(String param1, String param2) {
        BuscaFragment fragment = new BuscaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busca, container, false);

        // Obter referências para os elementos da interface de busca
        //SearchView searchView = view.findViewById(R.id.searchView);
        RadioButton radioButtonCachorro = view.findViewById(R.id.radioButtonCachorro);
        RadioButton radioButtonGato = view.findViewById(R.id.radioButtonGato);
        RadioButton radioButtonOutros = view.findViewById(R.id.radioButtonOutros);
        RadioButton radioButtonMacho = view.findViewById(R.id.radioButtonMacho);
        RadioButton radioButtonFemea = view.findViewById(R.id.radioButtonFemea);
        RadioButton radioButtonPequeno = view.findViewById(R.id.radioButtonPequeno);
        RadioButton radioButtonMedio = view.findViewById(R.id.radioButtonMedio);
        RadioButton radioButtonGrande = view.findViewById(R.id.radioButtonGrande);
        RadioButton radioButtonSim = view.findViewById(R.id.radioButtonSim);
        RadioButton radioButtonNao = view.findViewById(R.id.radioButtonNao);
        Button buttonBuscarPerso = view.findViewById(R.id.buttonBuscarPerso);
        buttonBuscarPerso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCachorroSelected = radioButtonCachorro.isChecked();
                boolean isGatoSelected = radioButtonGato.isChecked();
                boolean isOutrosSelected = radioButtonOutros.isChecked();
                boolean isMachoSelected = radioButtonMacho.isChecked();
                boolean isFemeaSelected = radioButtonFemea.isChecked();
                boolean isPequenoSelected = radioButtonPequeno.isChecked();
                boolean isMedioSelected = radioButtonMedio.isChecked();
                boolean isGrandeSelected = radioButtonGrande.isChecked();
                boolean isSimSelected = radioButtonSim.isChecked();
                boolean isNaoSelected = radioButtonNao.isChecked();

                // Monta a url com os filtros selecionados
                String baseUrl = getResources().getString(R.string.base_url) + "/api/publicacao/buscar/lista?";
                String urlParameters = "especie";
                urlParameters += (isCachorroSelected ? "=cachorro" : "");
                urlParameters += (isGatoSelected ? "=gato" : "");
                urlParameters += (isOutrosSelected ? "=outros" : "");
                urlParameters += "&genero";
                urlParameters += (isMachoSelected ? "=macho" : "");
                urlParameters += (isFemeaSelected ? "=femea" : "");
                urlParameters += "&porte";
                urlParameters += (isPequenoSelected ? "=pequeno" : "");
                urlParameters += (isMedioSelected ? "=medio" : "");
                urlParameters += (isGrandeSelected ? "=grande" : "");
                urlParameters += "&castrado";
                urlParameters += (isSimSelected ? "=1" : "");
                urlParameters += (isNaoSelected ? "=0" : "");

                String apiUrl = baseUrl + urlParameters;

                preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
                String authToken = preferences.getString("auth_token", null);
                List<Publicacao> publicacoes = new ArrayList<>();
                adapter = new PublicacaoAdapter(publicacoes,null);
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + authToken);
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //processar a resposta JSON e cria objeto Publicacao
                                try {
                                    JSONArray publicacoesArray = response.getJSONArray("data");
                                    for (int i = 0; i < publicacoesArray.length(); i++) {
                                        JSONObject jsonData = publicacoesArray.getJSONObject(i);
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
                                        boolean castrado = castradoStr.equals("1");
                                        String imagem = jsonPublicacao.getString("image_link");

                                        JSONObject jsonUser = jsonData.getJSONObject("user");
                                        long userId = jsonUser.getLong("id");
                                        String userAvatar = jsonUser.getString("avatar_link");
                                        String userName = jsonUser.getString("name");
                                        Usuario user = new Usuario(userId, userName, userAvatar);

                                        Publicacao publicacao = new Publicacao(id, descricao, nomePet, genero, especie, porte, idade, vacinas, castrado, imagem, user);
                                        publicacoes.add(publicacao);
                                    }
                                    if(publicacoes.size() !=0) {
                                        adapter.notifyDataSetChanged();
                                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                        fragmentManager.beginTransaction()
                                                .replace(R.id.fragment_container, ResultadoBuscarFragment.newInstance(publicacoes))
                                                .commit();
                                    }else{
                                        Toast.makeText(getContext(), "Nenhuma publicação cumpre os requisitos", Toast.LENGTH_LONG).show();
                                    }

                                } catch (JSONException e) {
                                    Toast.makeText(getContext(), "Ops! Ocorreu algum erro!", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getContext(), "Ops! Ocorreu algum erro!", Toast.LENGTH_LONG).show();
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
                        }
                ) {
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return headers;
                    }
                };
                //adiciona a solicitacao a fila
                requestQueue.add(request);
            }
        });
        return view;
    }
}