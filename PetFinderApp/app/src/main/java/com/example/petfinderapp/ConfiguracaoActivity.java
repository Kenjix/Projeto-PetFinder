package com.example.petfinderapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ConfiguracaoActivity extends AppCompatActivity {

    private TextView txtDeleteAccount;
    private TextView txtEditProfile;
    private Switch switchNotifications;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        txtDeleteAccount = findViewById(R.id.txtDeleteAccount);
        txtEditProfile = findViewById(R.id.txtEditProfile);
        switchNotifications = findViewById(R.id.switchNotifications);

        txtDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para excluir a conta
                Toast.makeText(ConfiguracaoActivity.this, "Conta excluída", Toast.LENGTH_SHORT).show();
            }
        });

        txtEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir tela de edição de perfil
                // Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                // startActivity(intent);
                Toast.makeText(ConfiguracaoActivity.this, "Abrir tela de edição de perfil", Toast.LENGTH_SHORT).show();
            }
        });

        switchNotifications.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Lógica para ativar/desativar notificações
            if (isChecked) {
                Toast.makeText(ConfiguracaoActivity.this, "Notificações ativadas", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ConfiguracaoActivity.this, "Notificações desativadas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
