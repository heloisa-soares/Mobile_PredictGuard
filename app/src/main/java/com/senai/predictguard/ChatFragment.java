package com.senai.predictguard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.senai.predictguard.adapter.MaquinaAdapter;
import com.senai.predictguard.model.Maquina;
import com.senai.predictguard.model.response.MaquinaListResponse;
import com.senai.predictguard.network.RetrofitClient;
import com.senai.predictguard.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {

    private RecyclerView    recyclerMaquinas;
    private ProgressBar     progressBar;
    private MaquinaAdapter  maquinaAdapter;
    private List<Maquina>   listaMaquinas = new ArrayList<>();
    private SessionManager  sessionManager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sessionManager   = new SessionManager(requireContext());
        recyclerMaquinas = view.findViewById(R.id.recyclerMaquinas);
        progressBar      = view.findViewById(R.id.progressBar);

        maquinaAdapter = new MaquinaAdapter(listaMaquinas);
        recyclerMaquinas.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerMaquinas.setAdapter(maquinaAdapter);

        buscarMaquinas();
    }

    private void buscarMaquinas() {
        progressBar.setVisibility(View.VISIBLE);

        RetrofitClient.getApiService()
                .getMaquinas(sessionManager.getBearerToken(), 1, 50)
                .enqueue(new Callback<MaquinaListResponse>() {

                    @Override
                    public void onResponse(Call<MaquinaListResponse> call,
                                           Response<MaquinaListResponse> response) {
                        progressBar.setVisibility(View.GONE);

                        if (response.isSuccessful() && response.body() != null) {
                            MaquinaListResponse body = response.body();
                            if (body.isSucesso() && body.getDados() != null) {
                                maquinaAdapter.atualizarLista(body.getDados());
                            }
                        } else {
                            Toast.makeText(requireContext(),
                                    "Erro ao carregar máquinas.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MaquinaListResponse> call, Throwable t) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(),
                                "Sem conexão com o servidor.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}