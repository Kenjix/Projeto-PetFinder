package com.example.petfinderapp.model;

public class Publicacao {

    private int id;
    private String descricao;
    private String nomePet;
    private String genero;
    private String tipo;
    private String porte;
    private int idade;
    private String vacinas;
    private String Castrado;
    private String imagem;
    private int userId;

    public Publicacao(int id, String descricao, String nomePet, String genero, String tipo, String porte, int idade, String vacinas, String castrado, int userId) {
        this.id = id;
        this.descricao = descricao;
        this.nomePet = nomePet;
        this.genero = genero;
        this.tipo = tipo;
        this.porte = porte;
        this.idade = idade;
        this.vacinas = vacinas;
        Castrado = castrado;
        this.userId = userId;
    }

    public Publicacao(int id, String descricao, String nomePet, String genero, String tipo, String porte, int idade, String vacinas, String castrado, String imagem, int userId) {
        this.id = id;
        this.descricao = descricao;
        this.nomePet = nomePet;
        this.genero = genero;
        this.tipo = tipo;
        this.porte = porte;
        this.idade = idade;
        this.vacinas = vacinas;
        Castrado = castrado;
        this.imagem = imagem;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getNomePet() {
        return nomePet;
    }

    public String getGenero() {
        return genero;
    }

    public String getTipo() {
        return tipo;
    }

    public String getPorte() {
        return porte;
    }

    public int getIdade() {
        return idade;
    }

    public String getVacinas() {
        return vacinas;
    }

    public String getCastrado() {
        return Castrado;
    }

    public String getImagem() {
        return imagem;
    }

    public int getUserId() {
        return userId;
    }
}
