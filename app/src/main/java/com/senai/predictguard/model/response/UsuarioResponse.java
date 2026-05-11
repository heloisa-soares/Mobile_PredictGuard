package com.senai.predictguard.model.response;

import com.senai.predictguard.model.Usuario;

public class UsuarioResponse {

    private boolean sucesso;
    private Usuario dados;
    private String erro;
    private String mensagem;

    public boolean isSucesso()   { return sucesso; }
    public Usuario getDados()    { return dados; }
    public String getErro()      { return erro; }
    public String getMensagem()  { return mensagem; }
}