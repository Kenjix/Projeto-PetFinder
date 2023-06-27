package com.example.petfinderapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class ConfiguracaoFragment extends Fragment {
    private String url = "";
    private SharedPreferences preferences;

    public ConfiguracaoFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_configuracao, container, false);

        TextView txtEditProfile = view.findViewById(R.id.txtEditProfile);
        TextView txtEditRedefinirSenha = view.findViewById(R.id.txtEditRedefinirSenha);
        Switch switchNotifications = view.findViewById(R.id.switchNotifications);
        TextView txtDeleteAccount = view.findViewById(R.id.txtDeleteAccount);
        preferences = requireContext().getSharedPreferences("sessao", Context.MODE_PRIVATE);
        String authToken = preferences.getString("auth_token", null);
        Long usuarioId = preferences.getLong("userId", 0);

        //listeners
        txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Substituir o fragmento atual pelo fragmento EditarPerfilFragment
                Fragment editarPerfilFragment = new EditarPerfilFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, editarPerfilFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        txtEditRedefinirSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Substituir o fragmento atual pelo fragmento EditarPerfilFragment
                Fragment redefinirSenhaFragment = new RedefinirSenhaFragment();
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, redefinirSenhaFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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


        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_confirm_password, null);
        EditText editPassword = dialogView.findViewById(R.id.editSenha);


        txtDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Confirmação");
                builder.setMessage("Deseja mesmo desabilitar sua conta?");
                builder.setCancelable(false);
                builder.setPositiveButton("Confirmar", (dialogInterface, i) -> {
                    //request para desabilitar a conta
                    url = getResources().getString(R.string.base_url) + "/api/users/" + usuarioId;
                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", "Bearer " + authToken);
                    StringRequest request = new StringRequest(Request.Method.DELETE, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(getActivity(), "Conta desativada com sucesso.", Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.remove("auth_token");
                                    editor.commit();
                                    Intent intent = new Intent(getActivity(), Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getActivity(), "Erro ao desativar a conta", Toast.LENGTH_SHORT).show();
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
        });
        return view;
    }
}
