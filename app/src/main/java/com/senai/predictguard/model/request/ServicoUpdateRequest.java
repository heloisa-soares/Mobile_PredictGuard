package com.senai.predictguard.model.request;

import com.google.gson.annotations.SerializedName;

public class ServicoUpdateRequest {

    @SerializedName("servico_status")
    private String servicoStatus;

    private String observacao;

    public ServicoUpdateRequest(String servicoStatus, String observacao) {
        this.servicoStatus = servicoStatus;
        this.observacao = observacao;
    }

    public String getServicoStatus() { return servicoStatus; }
    public String getObservacao()    { return observacao; }
}