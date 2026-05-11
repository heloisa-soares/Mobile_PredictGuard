package com.senai.predictguard.model.request;

public class UsuarioUpdateRequest {

    private String nome;
    private String telefone;

    // NOTA: O backend atual só aceita "telefone" e "senha" no PUT /usuarios/:id
    // Para que "nome" seja atualizado, o backend precisa incluir "nome"
    // no UPDATE do UsuarioModel. Isso é uma melhoria simples no backend.
    public UsuarioUpdateRequest(String nome, String telefone) {
        this.nome = nome;
        this.telefone = telefone;
    }

    public String getNome()     { return nome; }
    public String getTelefone() { return telefone; }
}