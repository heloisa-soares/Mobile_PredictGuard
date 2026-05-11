package com.senai.predictguard;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.senai.predictguard.model.request.UsuarioUpdateRequest;
import com.senai.predictguard.model.response.GenericResponse;
import com.senai.predictguard.network.RetrofitClient;
import com.senai.predictguard.util.SessionManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragment extends Fragment {

    private TextInputLayout   tilNome, tilTelefone, tilEmail;
    private TextInputEditText etNome, etTelefone, etEmail;
    private MaterialButton    btnSalvar, btnSair;
    private ProgressBar       progressBar;
    private SessionManager    sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        tilNome     = view.findViewById(R.id.tilNome);
        tilTelefone = view.findViewById(R.id.tilTelefone);
        tilEmail    = view.findViewById(R.id.tilEmail);
        etNome      = view.findViewById(R.id.etNome);
        etTelefone  = view.findViewById(R.id.etTelefone);
        etEmail     = view.findViewById(R.id.etEmail);
        btnSalvar   = view.findViewById(R.id.btnSalvar);
        btnSair     = view.findViewById(R.id.btnSair);
        progressBar = view.findViewById(R.id.progressBar);

        preencherDados();

        btnSalvar.setOnClickListener(v -> validarESalvar());
        btnSair.setOnClickListener(v -> confirmarSaida());
    }

    private void preencherDados() {
        etNome.setText(sessionManager.getUserNome());
        etEmail.setText(sessionManager.getUserEmail());
        etTelefone.setText(sessionManager.getUserTelefone());

        // E-mail é somente leitura
        etEmail.setFocusable(false);
        etEmail.setClickable(false);
        if (tilEmail != null) tilEmail.setHint("E-mail (não editável)");
    }

    private void validarESalvar() {
        String nome     = etNome.getText()     != null ? etNome.getText().toString().trim()     : "";
        String telefone = etTelefone.getText() != null ? etTelefone.getText().toString().trim() : "";

        boolean valido = true;
        tilNome.setError(null);
        tilTelefone.setError(null);

        if (TextUtils.isEmpty(nome)) {
            tilNome.setError("Nome é obrigatório");
            valido = false;
        } else if (nome.length() < 3) {
            tilNome.setError("Nome deve ter pelo menos 3 caracteres");
            valido = false;
        }

        if (TextUtils.isEmpty(telefone)) {
            tilTelefone.setError("Telefone é obrigatório");
            valido = false;
        } else if (telefone.replaceAll("[^0-9]", "").length() < 10) {
            tilTelefone.setError("Telefone inválido (mínimo 10 dígitos)");
            valido = false;
        }

        if (!valido) return;

        salvarPerfil(nome, telefone);
    }

    private void salvarPerfil(String nome, String telefone) {
        progressBar.setVisibility(View.VISIBLE);
        btnSalvar.setEnabled(false);

        int userId = sessionManager.getUserId();
        UsuarioUpdateRequest request = new UsuarioUpdateRequest(nome, telefone);

        RetrofitClient.getApiService()
                .updateUsuario(sessionManager.getBearerToken(), userId, request)
                .enqueue(new Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call,
                                           Response<GenericResponse> response) {
                        progressBar.setVisibility(View.GONE);
                        btnSalvar.setEnabled(true);

                        if (response.isSuccessful()) {
                            // Atualiza os dados locais na sessão
                            sessionManager.atualizarPerfil(nome, telefone);
                            Toast.makeText(requireContext(),
                                    "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(),
                                    "Erro ao atualizar perfil.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        btnSalvar.setEnabled(true);
                        Toast.makeText(requireContext(),
                                "Sem conexão com o servidor.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void confirmarSaida() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Sair")
                .setMessage("Deseja realmente sair da conta?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    sessionManager.limparSessao();
                    Intent intent = new Intent(requireActivity(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .setNegativeButton("Não", null)
                .show();
    }
}