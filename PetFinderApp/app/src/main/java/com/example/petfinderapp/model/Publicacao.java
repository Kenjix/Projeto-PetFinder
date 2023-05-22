package com.example.petfinderapp.model;
public class Publicacao {
    private long id;
    private String descricao;
    private String nomePet;
    private String genero;
    private String especie;
    private String porte;
    private String idade;
    private String vacinas;
    private boolean castrado;
    private String imagem;
    private long userId;
    private Usuario user;

    public Publicacao(String descricao, String nomePet, String genero, String especie, String porte, String idade, String vacinas, boolean castrado, long userId) {
        this.descricao = descricao;
        this.nomePet = nomePet;
        this.genero = genero;
        this.especie = especie;
        this.porte = porte;
        this.idade = idade;
        this.vacinas = vacinas;
        this.castrado = castrado;
        this.userId = userId;
    }

    public Publicacao(long id, String descricao, String nomePet, String genero, String especie, String porte, String idade, String vacinas, boolean castrado, String imagem, Usuario user) {
        this.id = id;
        this.descricao = descricao;
        this.nomePet = nomePet;
        this.genero = genero;
        this.especie = especie;
        this.porte = porte;
        this.idade = idade;
        this.vacinas = vacinas;
        this.castrado = castrado;
        this.imagem = imagem;
        this.user = user;
    }

    public long getId() {
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

    public String getEspecie() {
        return especie;
    }

    public String getPorte() {
        return porte;
    }

    public String getIdade() {
        return idade;
    }

    public String getVacinas() {
        return vacinas;
    }

    public boolean isCastrado() {
        return castrado;
    }
    public String getImagem() {
        return imagem;
    }
    public long getUserId() {
        return userId;
    }
    public Usuario getUser() {
        return user;
    }
}
