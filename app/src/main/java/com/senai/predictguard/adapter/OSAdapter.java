package com.senai.predictguard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.senai.predictguard.R;
import com.senai.predictguard.model.OS;

import java.util.List;

public class OSAdapter extends RecyclerView.Adapter<OSAdapter.OSViewHolder> {

    public interface OnOSActionListener {
        void onVerDetalhes(OS servico);
    }

    private final List<OS> listaOS;
    private final OnOSActionListener listener;

    public OSAdapter(List<OS> listaOS, OnOSActionListener listener) {
        this.listaOS  = listaOS;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OSViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_os, parent, false);
        return new OSViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OSViewHolder holder, int position) {
        holder.bind(listaOS.get(position), listener);
    }

    @Override
    public int getItemCount() { return listaOS.size(); }

    public void atualizarLista(List<OS> novaLista) {
        listaOS.clear();
        listaOS.addAll(novaLista);
        notifyDataSetChanged();
    }

    static class OSViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvTitulo;
        private final TextView tvStatus;
        private final TextView tvDescricao;
        private final Button   btnDetalhes;

        public OSViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo   = itemView.findViewById(R.id.tvTituloOS);
            tvStatus   = itemView.findViewById(R.id.tvStatusOS);
            tvDescricao = itemView.findViewById(R.id.tvDescricaoOS);
            btnDetalhes = itemView.findViewById(R.id.btnDetalhesOS);
        }

        public void bind(OS servico, OnOSActionListener listener) {
            tvTitulo.setText(servico.getTipo() != null ? servico.getTipo() : "Sem título");
            tvDescricao.setText(servico.getDescricao() != null ? servico.getDescricao() : "Sem descrição");

            String status = servico.getServicoStatus() != null ? servico.getServicoStatus() : "Solicitado";
            tvStatus.setText(status);

            // Cor dinâmica por status
            int cor;
            switch (status) {
                case "Em Andamento": cor = ContextCompat.getColor(itemView.getContext(), R.color.status_andamento); break;
                case "Concluído":    cor = ContextCompat.getColor(itemView.getContext(), R.color.status_concluido); break;
                default:             cor = ContextCompat.getColor(itemView.getContext(), R.color.status_solicitado); break;
            }
            tvStatus.setBackgroundColor(cor);

            btnDetalhes.setOnClickListener(v -> listener.onVerDetalhes(servico));
        }
    }
}