package com.example.petfinderapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;


public class CadastroUsuario extends AppCompatActivity {

    EditText editNome, editEmail, editDataNasc, editCelular, editSenha, editRepitaSenha;
    RadioButton radioButtonMasc, radioButtonFem, radioButtonOutros;
    Button buttonCadastro, ok_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editNome = findViewById(R.id.editNome);
        editEmail = findViewById(R.id.editEmail);
        editDataNasc = findViewById(R.id.editDataNasc);
        editCelular = findViewById(R.id.editCelular);
        editSenha = findViewById(R.id.editSenha);
        editRepitaSenha = findViewById(R.id.editRepitaSenha);
        buttonCadastro = findViewById(R.id.buttonCadastro);

        buttonCadastro.setOnClickListener(view -> {
            String nome = editNome.getText().toString().trim();
            String email = editEmail.getText().toString().trim();

            if (nome.isEmpty()) {
                editNome.setError("Campo obrigatório!");
                return;
            } else if(email.isEmpty()){
                editEmail.setError("Campo obrigatório!");
                return;
            }
        });

    }

}