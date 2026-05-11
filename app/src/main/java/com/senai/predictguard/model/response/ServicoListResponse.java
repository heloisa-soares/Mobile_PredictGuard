package com.senai.predictguard.model.response;

import com.senai.predictguard.model.OS;
import java.util.List;

public class ServicoListResponse {

    private boolean sucesso;
    private List<OS> dados;
    private Paginacao paginacao;

    public boolean isSucesso()       { return sucesso; }
    public List<OS> getDados()  { return dados; }
    public Paginacao getPaginacao()  { return paginacao; }

    public static class Paginacao {
        private int pagina;
        private int limite;
        private int total;
        private int totalPaginas;

        public int getTotal()       { return total; }
        public int getTotalPaginas(){ return totalPaginas; }
        public int getPagina()      { return pagina; }
        public int getLimite()      { return limite; }
    }
}