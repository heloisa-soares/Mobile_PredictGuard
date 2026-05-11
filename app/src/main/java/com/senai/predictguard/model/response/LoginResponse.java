package com.senai.predictguard.model.response;

import com.senai.predictguard.model.Usuario;

public class LoginResponse {

    private boolean sucesso;
    private String mensagem;
    private String erro;
    private LoginDados dados;

    public boolean isSucesso()      { return sucesso; }
    public String getMensagem()     { return mensagem; }
    public String getErro()         { return erro; }
    public LoginDados getDados()    { return dados; }

    public static class LoginDados {
        private String token;
        private Usuario usuario;

        public String getToken()      { return token; }
        public Usuario getUsuario()   { return usuario; }
    }
}