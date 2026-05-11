package com.senai.predictguard.model.response;

import com.senai.predictguard.model.Maquina;
import java.util.List;

public class MaquinaListResponse {

    private boolean sucesso;
    private List<Maquina> dados;
    private Paginacao paginacao;

    public boolean isSucesso()        { return sucesso; }
    public List<Maquina> getDados()   { return dados; }
    public Paginacao getPaginacao()   { return paginacao; }

    public static class Paginacao {
        private int total;
        public int getTotal() { return total; }
    }
}