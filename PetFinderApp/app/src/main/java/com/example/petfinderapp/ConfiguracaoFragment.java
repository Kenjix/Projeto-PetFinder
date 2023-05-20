package com.example.petfinderapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ConfiguracaoFragment extends Fragment {

    public ConfiguracaoFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuracao, container, false);

        // Find views by ID
        TextView txtEditProfile = view.findViewById(R.id.txtEditProfile);
        Switch switchNotifications = view.findViewById(R.id.switchNotifications);
        TextView txtDeleteAccount = view.findViewById(R.id.txtDeleteAccount);

        // Set up click listeners
        txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on Edit Profile
                Toast.makeText(getActivity(), "Editar Perfil", Toast.LENGTH_SHORT).show();
            }
        });

        switchNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on Notifications Switch
                if (switchNotifications.isChecked()) {
                    Toast.makeText(getActivity(), "Notificações ativadas", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Notificações desativadas", Toast.LENGTH_SHORT).show();
                }
            }
        });

        txtDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle click on Delete Account
                Toast.makeText(getActivity(), "Excluir Conta", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
