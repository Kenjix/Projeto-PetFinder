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
import android.widget.SearchView;

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
        CheckBox checkBoxCachorro = view.findViewById(R.id.checkBoxCachorro);
        CheckBox checkBoxGato = view.findViewById(R.id.checkBoxGato);
        CheckBox checkBoxOutros = view.findViewById(R.id.checkBoxOutros);
        CheckBox checkBoxMacho = view.findViewById(R.id.checkBoxMacho);
        CheckBox checkBoxFemea = view.findViewById(R.id.checkBoxFemea);
        CheckBox checkBoxPequeno = view.findViewById(R.id.checkBoxPequeno);
        CheckBox checkBoxMedio = view.findViewById(R.id.checkBoxMedio);
        CheckBox checkBoxGrande = view.findViewById(R.id.checkBoxGrande);
        CheckBox checkBoxSim = view.findViewById(R.id.checkBoxSim);
        CheckBox checkBoxNao = view.findViewById(R.id.checkBoxNao);
        Button buttonBuscarPerso = view.findViewById(R.id.buttonBuscarPerso);
        buttonBuscarPerso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCachorroSelected = checkBoxCachorro.isChecked();
                boolean isGatoSelected = checkBoxGato.isChecked();
                boolean isOutrosSelected = checkBoxOutros.isChecked();
                boolean isMachoSelected = checkBoxMacho.isChecked();
                boolean isFemeaSelected = checkBoxFemea.isChecked();
                boolean isPequenoSelected = checkBoxPequeno.isChecked();
                boolean isMedioSelected = checkBoxMedio.isChecked();
                boolean isGrandeSelected = checkBoxGrande.isChecked();
                boolean isSimSelected = checkBoxSim.isChecked();
                boolean isNaoSelected = checkBoxNao.isChecked();

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
                //castrado não está no banco
                //urlParameters += "&sim=" + (isSimSelected ? "1" : "0");
                //urlParameters += "&nao=" + (isNaoSelected ? "1" : "0");

                String apiUrl = baseUrl + urlParameters;

                preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
                String authToken = preferences.getString("auth_token", null);
                List<Publicacao> publicacoes = new ArrayList<>();
                adapter = new PublicacaoAdapter(publicacoes,null);
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + authToken);
                //cria uma solicitacao GET para a URL da API
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

//                                    Intent intent = new Intent(getActivity(), ResultadoBuscarFragment.class);
//                                    intent.putExtra("publicacoes", (Serializable) publicacoes);
//                                    startActivity(intent);

                                    Intent intent = new Intent(getActivity(), ResultadoBuscarFragment.class);
                                    intent.putExtra("publicacoes", (Serializable) publicacoes);

                                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .replace(R.id.fragment_container, new ResultadoBuscarFragment())
                                            .commit();
                                    //atualiza o adaptador com a lista de publicações obtida
                                    adapter.notifyDataSetChanged();
                                } catch (JSONException e) {
                                    System.out.println("deu ruim");
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("deu ruim 2");
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