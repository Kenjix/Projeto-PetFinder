package com.example.petfinderapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.petfinderapp.model.Publicacao;
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
import java.util.List;

public class BuscaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

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
                String baseUrl = getResources().getString(R.string.base_url) + "/api/publicacao/buscar?";
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

                List<Publicacao> publicacoes = new ArrayList<>();
                RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
                //cria uma solicitação GET para a URL da API
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, apiUrl, null,
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
                                        String userAvatar = jsonPublicacao.getString("avatar");

                                        JSONObject jsonUser = jsonPublicacao.getJSONObject("user");
                                        String userName = jsonUser.getString("name");
                                        Usuario user = new Usuario(userId, userName, userAvatar);

                                        Publicacao publicacao = new Publicacao(id, descricao, nomePet, genero, especie, porte, idade, vacinas, castrado, imagem, user);
                                        publicacoes.add(publicacao);
                                    }

                                    Intent intent = new Intent(getActivity(), ResultadoBuscarFragment.class);
                                    intent.putExtra("publicacoes", (Serializable) publicacoes);
                                    startActivity(intent);
                                    //atualiza o adaptador com a lista de publicações obtida
                                    //adapter.notifyDataSetChanged();
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
            }
        });

        return view;
    }
}

/*
try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        String jsonResponse = response.toString();

                        Intent intent = new Intent(getActivity(), ResultadoBuscarFragment.class);
                        intent.putExtra("jsonResponse", jsonResponse);
                        startActivity(intent);

                        Intent intent = new Intent(activity, NovaAtividade.class);
                        intent.putExtra("publicacoes", new ArrayList<>(publicacoes));
                        activity.startActivity(intent);

                    } else {
                        // Tratar erro na resposta da API
                        // Direcionar para página de erro(?)
                    }

                    connection.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Tratar falha na solicitação HTTP
                    // Direcionar para página de erro(?)
                }
 */