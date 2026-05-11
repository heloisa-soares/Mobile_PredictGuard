package com.senai.predictguard.model;

import com.google.gson.annotations.SerializedName;

public class Maquina {

    @SerializedName("id")
    private int id;

    @SerializedName("nome")
    private String nome;

    @SerializedName("cod_registro")
    private String codRegistro;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("setor")
    private String setor;

    @SerializedName("nivel_criticidade")
    private String nivelCriticidade;

    @SerializedName("status_operacional")
    private String statusOperacional;

    // "Ok" ou "Alerta" — campo principal de saúde
    @SerializedName("status_saude")
    private String statusSaude;

    @SerializedName("temperatura_limite_c")
    private double temperaturaLimiteC;

    @SerializedName("aceleracao_limite_g")
    private double aceleracaoLimiteG;

    @SerializedName("imagem")
    private String imagem;

    // Getters
    public int getId()                  { return id; }
    public String getNome()             { return nome; }
    public String getCodRegistro()      { return codRegistro; }
    public String getTipo()             { return tipo; }
    public String getSetor()            { return setor; }
    public String getNivelCriticidade() { return nivelCriticidade; }
    public String getStatusOperacional(){ return statusOperacional; }
    public String getStatusSaude()      { return statusSaude; }
    public double getTemperaturaLimiteC(){ return temperaturaLimiteC; }
    public double getAceleracaoLimiteG(){ return aceleracaoLimiteG; }
    public String getImagem()           { return imagem; }
}