package com.example.petfinderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TextView userSession;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the ActionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Início");
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            //inicia o primeiro fragmento padrão
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InicioFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        preferences = getSharedPreferences("sessao", MODE_PRIVATE);
        String username = preferences.getString("username", "");
        int nivelAcesso = preferences.getInt("nivelAcesso", 0);
        View headerView = navigationView.getHeaderView(0);
        userSession = headerView.findViewById(R.id.userSession);

        if (username.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else { //Se o username já foi salvo, exibe o conteudo
            userSession.setText(username);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportActionBar().setTitle("Início");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new InicioFragment()).commit();
                break;
            case R.id.nav_search:
                getSupportActionBar().setTitle("Realizar Busca");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BuscaFragment()).commit();
                break;
            case R.id.nav_add_to_post:
                getSupportActionBar().setTitle("Criar Publicação");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CriarPublicacaoFragment()).commit();
                break;
            case R.id.nav_message:
                getSupportActionBar().setTitle("Minhas Conversas");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MeuPerfilFragment()).commit();
                break;
            case R.id.nav_favorite:
                getSupportActionBar().setTitle("Favoritos");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FavoritosFragment()).commit();
                break;
            case R.id.nav_donates:
                getSupportActionBar().setTitle("Doações");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DoacoesFragment()).commit();
                break;
            case R.id.nav_perfil:
                getSupportActionBar().setTitle("Meu Perfil");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MeuPerfilFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportActionBar().setTitle("Configurações");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConfiguracaoFragment()).commit();
                break;
            case R.id.nav_logout:
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("username");
                editor.remove("nivelAcesso");
                editor.remove("idUsuario");
                editor.commit();
                finish();
                startActivity(getIntent());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}