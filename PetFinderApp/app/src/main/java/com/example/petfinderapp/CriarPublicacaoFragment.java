package com.example.petfinderapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class CriarPublicacaoFragment extends Fragment {

    //Spinner é o ComboBox
    Spinner spinnerPorte, spinnerCastrado, spinnerTipo, spinnerGenero;
    private EditText editTextNome, editTextIdade, editTextVacinas, editTextDescricao;
    private ImageView imagePet;
    private Button buttonAddFoto, buttonRemoveFoto, buttonCancelar, buttonCadastrar;
    public CriarPublicacaoFragment() {
        // Required empty public constructor
    }

    public static CriarPublicacaoFragment newInstance(String param1, String param2) {
        CriarPublicacaoFragment fragment = new CriarPublicacaoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_criar_publicacao, container, false);


        //esse arrayadaper esta refereciando os itens do combobox que estão no Values/String.xml
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

        return view;
    }
}