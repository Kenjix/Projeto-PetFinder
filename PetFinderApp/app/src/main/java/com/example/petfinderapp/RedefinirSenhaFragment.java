package com.example.petfinderapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RedefinirSenhaFragment extends Fragment {

    private EditText senhaAntigaEditText;
    private EditText novaSenhaEditText;
    private EditText confirmaNovaSenhaEditText;
    private TextView msgErroTextView;
    private Button buttonSalvar;

    public RedefinirSenhaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_redefinir_senha, container, false);

        senhaAntigaEditText = view.findViewById(R.id.senhaAntiga);
        novaSenhaEditText = view.findViewById(R.id.novaSenha);
        confirmaNovaSenhaEditText = view.findViewById(R.id.confirmaNovaSenha);
        msgErroTextView = view.findViewById(R.id.msgErro);
        buttonSalvar = view.findViewById(R.id.buttonSalvar);

        buttonSalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String senhaAntiga = senhaAntigaEditText.getText().toString();
                String novaSenha = novaSenhaEditText.getText().toString();
                String confirmaNovaSenha = confirmaNovaSenhaEditText.getText().toString();

                // Verificar se as senhas correspondem
                if (novaSenha.equals(confirmaNovaSenha)) {
                    // Senhas correspondem, realizar a ação necessária
                    // ...
                    Toast.makeText(getActivity(), "Senha alterada com sucesso!", Toast.LENGTH_SHORT).show();
                } else {
                    // Senhas não correspondem, exibir mensagem de erro
                    msgErroTextView.setText("As senhas não correspondem");
                }
            }
        });

        return view;
    }
}
