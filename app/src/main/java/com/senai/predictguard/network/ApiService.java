package com.senai.predictguard.network;

import com.senai.predictguard.model.request.LoginRequest;
import com.senai.predictguard.model.request.ServicoUpdateRequest;
import com.senai.predictguard.model.request.UsuarioUpdateRequest;
import com.senai.predictguard.model.response.GenericResponse;
import com.senai.predictguard.model.response.LoginResponse;
import com.senai.predictguard.model.response.MaquinaListResponse;
import com.senai.predictguard.model.response.ServicoListResponse;
import com.senai.predictguard.model.response.UsuarioResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    // ─── Autenticação ────────────────────────────────────────────────────
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    // ─── Usuários ────────────────────────────────────────────────────────
    @GET("usuarios/{id}")
    Call<UsuarioResponse> getUsuario(
            @Header("Authorization") String token,
            @Path("id") int id
    );

    @PUT("usuarios/{id}")
    Call<GenericResponse> updateUsuario(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body UsuarioUpdateRequest request
    );

    // ─── Serviços (OS / Demandas) ────────────────────────────────────────
    @GET("servicos")
    Call<ServicoListResponse> getServicos(
            @Header("Authorization") String token,
            @Query("pagina") int pagina,
            @Query("limite") int limite
    );

    @PUT("servicos/{id}")
    Call<GenericResponse> updateServico(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Body ServicoUpdateRequest request
    );

    // ─── Máquinas ────────────────────────────────────────────────────────
    @GET("maquinas")
    Call<MaquinaListResponse> getMaquinas(
            @Header("Authorization") String token,
            @Query("pagina") int pagina,
            @Query("limite") int limite
    );
}