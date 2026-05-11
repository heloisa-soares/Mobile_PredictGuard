package com.senai.predictguard.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.senai.predictguard.model.Usuario;

public class SessionManager {

    private static final String PREF_NAME      = "predictguard_session";
    private static final String KEY_TOKEN      = "token";
    private static final String KEY_USER_ID    = "user_id";
    private static final String KEY_USER_NOME  = "user_nome";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_USER_TEL   = "user_telefone";
    private static final String KEY_USER_TIPO  = "user_tipo";
    private static final String KEY_USER_FOTO  = "user_foto";

    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        prefs  = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    /** Salva token e dados do usuário após login bem-sucedido */
    public void salvarSessao(String token, Usuario usuario) {
        editor.putString(KEY_TOKEN,      token);
        editor.putInt(KEY_USER_ID,       usuario.getId());
        editor.putString(KEY_USER_NOME,  usuario.getNome()  != null ? usuario.getNome()  : "");
        editor.putString(KEY_USER_EMAIL, usuario.getEmail() != null ? usuario.getEmail() : "");
        editor.putString(KEY_USER_TEL,   usuario.getTelefone() != null ? usuario.getTelefone() : "");
        editor.putString(KEY_USER_TIPO,  usuario.getTipo()  != null ? usuario.getTipo()  : "tecnico");
        editor.putString(KEY_USER_FOTO,  usuario.getFoto()  != null ? usuario.getFoto()  : "");
        editor.apply();
    }

    /** Limpa todos os dados (logout) */
    public void limparSessao() {
        editor.clear().apply();
    }

    /** Verifica se há sessão ativa */
    public boolean isLogado() {
        return prefs.getString(KEY_TOKEN, null) != null;
    }

    public String getToken()        { return prefs.getString(KEY_TOKEN,      ""); }
    public String getBearerToken()  { return "Bearer " + getToken(); }
    public int    getUserId()       { return prefs.getInt(KEY_USER_ID,        -1); }
    public String getUserNome()     { return prefs.getString(KEY_USER_NOME,   ""); }
    public String getUserEmail()    { return prefs.getString(KEY_USER_EMAIL,  ""); }
    public String getUserTelefone() { return prefs.getString(KEY_USER_TEL,    ""); }
    public String getUserTipo()     { return prefs.getString(KEY_USER_TIPO,   "tecnico"); }
    public String getUserFoto()     { return prefs.getString(KEY_USER_FOTO,   ""); }

    /** Atualiza nome e telefone localmente após edição no perfil */
    public void atualizarPerfil(String nome, String telefone) {
        editor.putString(KEY_USER_NOME, nome);
        editor.putString(KEY_USER_TEL, telefone);
        editor.apply();
    }
}