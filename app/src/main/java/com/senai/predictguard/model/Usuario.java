package com.senai.predictguard.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("id")
    private int id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("email")
    private String email;

    @SerializedName("telefone")
    private String telefone;

    // "tipo" = "admin" ou "tecnico" — usado como "status" na Home
    @SerializedName("tipo")
    private String tipo;

    @SerializedName("foto")
    private String foto;

    @SerializedName("data_criacao")
    private String dataCriacao;

    // Getters
    public int getId()           { return id; }
    public String getNome()      { return nome; }
    public String getEmail()     { return email; }
    public String getTelefone()  { return telefone; }
    public String getTipo()      { return tipo; }
    public String getFoto()      { return foto; }
    public String getDataCriacao() { return dataCriacao; }

    // Setters
    public void setId(int id)              { this.id = id; }
    public void setNome(String nome)       { this.nome = nome; }
    public void setEmail(String email)     { this.email = email; }
    public void setTelefone(String t)      { this.telefone = t; }
    public void setTipo(String tipo)       { this.tipo = tipo; }
    public void setFoto(String foto)       { this.foto = foto; }
}