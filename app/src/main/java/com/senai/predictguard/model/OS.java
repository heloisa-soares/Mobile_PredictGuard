package com.senai.predictguard.model;

import com.google.gson.annotations.SerializedName;

public class OS {

    @SerializedName("id")
    private int id;

    @SerializedName("maquina_id")
    private int maquinaId;

    @SerializedName("usuario_responsavel_id")
    private int usuarioResponsavelId;

    @SerializedName("usuario_solicitante_id")
    private int usuarioSolicitanteId;

    // "tipo" é o TÍTULO da OS: ex. "Manutenção Preditiva"
    @SerializedName("tipo")
    private String tipo;

    // Status: "Solicitado" | "Em Andamento" | "Concluído"
    @SerializedName("servico_status")
    private String servicoStatus;

    @SerializedName("descricao")
    private String descricao;

    // Observação preenchida pelo técnico
    @SerializedName("observacao")
    private String observacao;

    @SerializedName("data_alerta")
    private String dataAlerta;

    @SerializedName("data_criacao")
    private String dataCriacao;

    @SerializedName("data_encerramento")
    private String dataEncerramento;

    // Getters
    public int getId()                    { return id; }
    public int getMaquinaId()             { return maquinaId; }
    public int getUsuarioResponsavelId()  { return usuarioResponsavelId; }
    public int getUsuarioSolicitanteId()  { return usuarioSolicitanteId; }
    public String getTipo()               { return tipo; }
    public String getServicoStatus()      { return servicoStatus; }
    public String getDescricao()          { return descricao; }
    public String getObservacao()         { return observacao; }
    public String getDataAlerta()         { return dataAlerta; }
    public String getDataCriacao()        { return dataCriacao; }
    public String getDataEncerramento()   { return dataEncerramento; }

    // Setters
    public void setServicoStatus(String s) { this.servicoStatus = s; }
    public void setObservacao(String o)    { this.observacao = o; }
}