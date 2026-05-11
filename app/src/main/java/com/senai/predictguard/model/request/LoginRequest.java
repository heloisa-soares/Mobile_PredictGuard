package com.senai.predictguard.model.request;

public class LoginRequest {

    private String email;
    private String senha;
    private String canal;

    public LoginRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
        this.canal = "mobile"; // ← obrigatório pelo backend
    }

    public String getEmail()  { return email; }
    public String getSenha()  { return senha; }
    public String getCanal()  { return canal; }
}