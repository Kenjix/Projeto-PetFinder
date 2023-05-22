package com.example.petfinderapp.model;
public class Usuario {
    private long id;
    private String name;
    private String email;
    private String password;
    private String dataNasc;
    private String genero;
    private String telefone;
    private String avatar;
    private int nivelAcesso;

    public Usuario(long id, String name, String email, String dataNasc, String genero, String telefone, String avatar, int nivelAcesso) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dataNasc = dataNasc;
        this.genero = genero;
        this.telefone = telefone;
        this.avatar = avatar;
        this.nivelAcesso = nivelAcesso;
    }

    public Usuario(String name, String email, String password, String dataNasc, String genero, String telefone) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.dataNasc = dataNasc;
        this.genero = genero;
        this.telefone = telefone;
    }
    public Usuario(String name, String email, String telefone) {
        this.name = name;
        this.email = email;
        this.telefone = telefone;
    }
    public Usuario(long id, String name, String avatar) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getDataNasc() {
        return dataNasc;
    }

    public String getGenero() {
        return genero;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getAvatar() {
        return avatar;
    }

    public int getNivelAcesso() {
        return nivelAcesso;
    }
}



