package com.example.petfinderapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MeuPerfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MeuPerfilFragment extends Fragment {

    private ImageView imageViewPerfil;
    private ImageView editIconImageView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MeuPerfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MeuPerfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MeuPerfilFragment newInstance(String param1, String param2) {
        MeuPerfilFragment fragment = new MeuPerfilFragment();
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
        View view = inflater.inflate(R.layout.fragment_meu_perfil, container, false);

        imageViewPerfil = view.findViewById(R.id.imageViewPerfil);
        editIconImageView = view.findViewById(R.id.editIcon);

        // Definir clique na imagem do perfil
        imageViewPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });

        // Definir clique no ícone de edição
        editIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu();
            }
        });

        return view;
    }

    private void showPopupMenu() {
        PopupMenu popupMenu = new PopupMenu(getActivity(), imageViewPerfil);
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.editImageOption:
                        // Ação para editar imagem
                        Toast.makeText(getActivity(), "Editar imagem selecionado", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.removeImageOption:
                        // Ação para remover imagem
                        Toast.makeText(getActivity(), "Remover imagem selecionado", Toast.LENGTH_SHORT).show();
                        return true;
                    default:
                        return false;
                }
            }
        });

        popupMenu.show();
    }
}