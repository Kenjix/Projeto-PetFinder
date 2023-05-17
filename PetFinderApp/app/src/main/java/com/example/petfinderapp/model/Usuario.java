package com.example.petfinderapp.model;

public class Usuario {
    private String nome;
    private String email;
    private String password;
    private int nivelAcesso;


    public Usuario(String nome, String email, String password, int nivelAcesso) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.nivelAcesso = nivelAcesso;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getNivelAcesso() {
        return nivelAcesso;
    }
}
