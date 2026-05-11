package com.senai.predictguard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.senai.predictguard.model.request.LoginRequest;
import com.senai.predictguard.model.response.LoginResponse;
import com.senai.predictguard.network.RetrofitClient;
import com.senai.predictguard.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout    tilEmail, tilPassword;
    private TextInputEditText  etEmail, etPassword;
    private MaterialButton     btnLogin;
    private ProgressBar        progressBar;
    private SessionManager     sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {
                    Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    v.setPadding(sys.left, sys.top, sys.right, sys.bottom);
                    return insets;
                }
        );

        sessionManager = new SessionManager(this);

        // Se já tem sessão ativa, vai direto para o Main
        if (sessionManager.isLogado()) {
            irParaMain();
            return;
        }

        tilEmail    = findViewById(R.id.layoutEmail);
        tilPassword = findViewById(R.id.layoutPassword);
        etEmail     = findViewById(R.id.etEmail);
        etPassword  = findViewById(R.id.etPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> realizarLogin());
    }

    private void realizarLogin() {
        String email = etEmail.getText() != null ? etEmail.getText().toString().trim() : "";
        String senha = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        // ── Validações ──────────────────────────────────────────────────
        boolean valido = true;
        tilEmail.setError(null);
        tilPassword.setError(null);

        if (TextUtils.isEmpty(email)) {
            tilEmail.setError("E-mail é obrigatório");
            valido = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.setError("E-mail inválido");
            valido = false;
        }

        if (TextUtils.isEmpty(senha)) {
            tilPassword.setError("Senha é obrigatória");
            valido = false;
        } else if (senha.length() < 3) {
            tilPassword.setError("Senha deve ter pelo menos 3 caracteres");
            valido = false;
        }

        if (!valido) return;

        // ── Chamada à API ────────────────────────────────────────────────
        setLoading(true);

        LoginRequest request = new LoginRequest(email, senha);
        RetrofitClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                setLoading(false);

                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse body = response.body();

                    if (body.isSucesso() && body.getDados() != null) {
                        sessionManager.salvarSessao(
                                body.getDados().getToken(),
                                body.getDados().getUsuario()
                        );
                        irParaMain();
                    } else {
                        Toast.makeText(LoginActivity.this,
                                body.getMensagem() != null ? body.getMensagem() : "Erro ao fazer login",
                                Toast.LENGTH_SHORT).show();
                    }

                } else if (response.code() == 401) {
                    tilPassword.setError("E-mail ou senha incorretos");

                } else if (response.code() == 403) {
                    Toast.makeText(LoginActivity.this,
                            "Acesso não autorizado para o app mobile.",
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Erro no servidor (" + response.code() + "). Tente novamente.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                setLoading(false);
                Toast.makeText(LoginActivity.this,
                        "Sem conexão com o servidor. Verifique o IP e tente novamente.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void irParaMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
        btnLogin.setText(loading ? "Entrando..." : "Entrar");
    }
}