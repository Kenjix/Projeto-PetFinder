package com.example.petfinderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private TextView userSession;
    private ShapeableImageView imageViewPerfil;
    private SharedPreferences preferences;

    public void atualizaHeader(String nome, String avatar) {
        if (nome != null) {
            userSession.setText(nome);
        }
        if (avatar != null) {
            Glide.with(this)
                    .load(avatar)
                    .placeholder(R.drawable.fotoperfil)
                    .into(new DrawableImageViewTarget(imageViewPerfil));
        }
    }

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
        String avatar = preferences.getString("avatar", "");
        View headerView = navigationView.getHeaderView(0);
        userSession = headerView.findViewById(R.id.userSession);
        imageViewPerfil = headerView.findViewById(R.id.imageViewPerfil);

        if (username.isEmpty()) {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish();
        } else { //Se o username já foi salvo, exibe o conteudo
            userSession.setText(username);
            Glide.with(this)
                    .load(avatar)
                    .placeholder(R.drawable.fotoperfil)
                    .into(new DrawableImageViewTarget(imageViewPerfil));
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
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MinhasConversasFragment()).commit();
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
                getSupportActionBar().setTitle("Minhas Publicações");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MinhasPublicacoesFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportActionBar().setTitle("Configurações");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ConfiguracaoFragment()).commit();
                break;
            case R.id.nav_logout:
                String url = getResources().getString(R.string.base_url) + "/api/auth/logout";
                String authToken = preferences.getString("auth_token", "");
                //request
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + authToken);
                StringRequest request = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                String message = "";
                                try {
                                    JSONObject jsonResponse = new JSONObject(response);
                                    message = jsonResponse.getString("message");
                                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.remove("userId");
                                    editor.remove("username");
                                    editor.remove("avatar");
                                    editor.remove("auth_token");
                                    editor.commit();
                                    finish();
                                    startActivity(getIntent());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                String errorMessage = "";
                                if (error.networkResponse != null && error.networkResponse.data != null) {
                                    try {
                                        String errorData = new String(error.networkResponse.data, "UTF-8");
                                        JSONObject errorJson = new JSONObject(errorData);
                                        errorMessage = errorJson.getString("error");
                                    } catch (UnsupportedEncodingException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return headers;
                    }
                };
                Volley.newRequestQueue(this).add(request);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}