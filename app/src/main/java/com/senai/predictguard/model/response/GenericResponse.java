package com.senai.predictguard.model.response;

public class GenericResponse {

    private boolean sucesso;
    private String mensagem;
    private String erro;

    public boolean isSucesso()  { return sucesso; }
    public String getMensagem() { return mensagem; }
    public String getErro()     { return erro; }
}