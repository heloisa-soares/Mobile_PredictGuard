package com.senai.predictguard;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.senai.predictguard.adapter.OSAdapter;
import com.senai.predictguard.model.OS;
import com.senai.predictguard.model.request.ServicoUpdateRequest;
import com.senai.predictguard.model.response.GenericResponse;
import com.senai.predictguard.model.response.ServicoListResponse;
import com.senai.predictguard.network.RetrofitClient;
import com.senai.predictguard.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView    tvWelcomeTec;
    private TextView    tvDemandas;
    private TextView    tvTipoUsuario;
    private RecyclerView recyclerOS;
    private ProgressBar  progressBar;

    private OSAdapter     osAdapter;
    private List<OS> listaOS = new ArrayList<>();
    private SessionManager sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        // ── Vinculação de views (IDs do activity_home.xml existente) ──────
        tvWelcomeTec  = view.findViewById(R.id.tvWelcomeTec);
        tvDemandas    = view.findViewById(R.id.tvDemandas);
        tvTipoUsuario = view.findViewById(R.id.tvTipoUsuario);
        recyclerOS    = view.findViewById(R.id.rvDemands);
        progressBar   = view.findViewById(R.id.progressBar);

        // ── Exibir dados do usuário logado ───────────────────────────────
        exibirDadosUsuario();

        // ── Configurar RecyclerView ───────────────────────────────────────
        osAdapter = new OSAdapter(listaOS, this::abrirDialogDetalhes);
        recyclerOS.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerOS.setAdapter(osAdapter);

        // ── Buscar demandas da API ────────────────────────────────────────
        buscarServicos();
    }

    private void exibirDadosUsuario() {
        String nome = sessionManager.getUserNome();
        String tipo = sessionManager.getUserTipo();

        if (tvWelcomeTec != null) {
            tvWelcomeTec.setText("Olá, " + nome + "!");
        }
        if (tvTipoUsuario != null) {
            tvTipoUsuario.setText("tecnico".equals(tipo) ? "Técnico" : "Administrador");
        }
    }

    private void buscarServicos() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getApiService()
                .getServicos(sessionManager.getBearerToken(), 1, 50)
                .enqueue(new Callback<ServicoListResponse>() {

                    @Override
                    public void onResponse(Call<ServicoListResponse> call,
                                           Response<ServicoListResponse> response) {
                        if (progressBar != null) progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            ServicoListResponse body = response.body();

                            if (body.isSucesso() && body.getDados() != null) {
                                osAdapter.atualizarLista(body.getDados());

                                // Atualiza o contador de demandas
                                int total = body.getPaginacao() != null
                                        ? body.getPaginacao().getTotal()
                                        : body.getDados().size();

                                if (tvDemandas != null) {
                                    tvDemandas.setText("Você tem " + total + " demandas");
                                }
                            }

                        } else if (response.code() == 401) {
                            Toast.makeText(requireContext(),
                                    "Sessão expirada. Faça login novamente.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(),
                                    "Erro ao carregar demandas.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServicoListResponse> call, Throwable t) {
                        if (progressBar != null) progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(),
                                "Sem conexão com o servidor.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ── Dialog de detalhes + atualização de status da OS ─────────────────
    private void abrirDialogDetalhes(OS servico) {
        View dialogView = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_os_detalhes, null);

        TextView tvId          = dialogView.findViewById(R.id.tvDialogOSId);
        TextView tvTipo        = dialogView.findViewById(R.id.tvDialogOSTipo);
        TextView tvDesc        = dialogView.findViewById(R.id.tvDialogOSDescricao);
        TextView tvSolicitante = dialogView.findViewById(R.id.tvDialogOSSolicitante);
        TextView tvData        = dialogView.findViewById(R.id.tvDialogOSData);
        Spinner  spStatus      = dialogView.findViewById(R.id.spDialogOSStatus);
        EditText etObservacao  = dialogView.findViewById(R.id.etDialogOSObservacao);

        // Preenche os dados
        tvId.setText("OS #" + servico.getId());
        tvTipo.setText(servico.getTipo() != null ? servico.getTipo() : "-");
        tvDesc.setText(servico.getDescricao() != null ? servico.getDescricao() : "Sem descrição");
        tvSolicitante.setText("Solicitante ID: " + servico.getUsuarioSolicitanteId());
        tvData.setText(servico.getDataCriacao() != null
                ? servico.getDataCriacao().substring(0, 10) : "-");

        if (servico.getObservacao() != null) {
            etObservacao.setText(servico.getObservacao());
        }

        // Spinner de status
        String[] statusOptions = {"Solicitado", "Em Andamento", "Concluído"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, statusOptions);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spStatus.setAdapter(statusAdapter);

        // Seleciona o status atual
        if (servico.getServicoStatus() != null) {
            for (int i = 0; i < statusOptions.length; i++) {
                if (statusOptions[i].equals(servico.getServicoStatus())) {
                    spStatus.setSelection(i);
                    break;
                }
            }
        }

        new AlertDialog.Builder(requireContext())
                .setTitle("Detalhes da OS")
                .setView(dialogView)
                .setPositiveButton("Salvar", (dialog, which) -> {
                    String novoStatus = spStatus.getSelectedItem().toString();
                    String observacao = etObservacao.getText().toString().trim();
                    atualizarServico(servico.getId(), novoStatus, observacao);
                })
                .setNegativeButton("Fechar", null)
                .show();
    }

    private void atualizarServico(int servicoId, String novoStatus, String observacao) {
        ServicoUpdateRequest request = new ServicoUpdateRequest(novoStatus, observacao);

        RetrofitClient.getApiService()
                .updateServico(sessionManager.getBearerToken(), servicoId, request)
                .enqueue(new Callback<GenericResponse>() {

                    @Override
                    public void onResponse(Call<GenericResponse> call,
                                           Response<GenericResponse> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(requireContext(),
                                    "OS atualizada com sucesso!", Toast.LENGTH_SHORT).show();
                            buscarServicos(); // Recarrega a lista
                        } else {
                            Toast.makeText(requireContext(),
                                    "Erro ao atualizar OS.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenericResponse> call, Throwable t) {
                        Toast.makeText(requireContext(),
                                "Sem conexão.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}