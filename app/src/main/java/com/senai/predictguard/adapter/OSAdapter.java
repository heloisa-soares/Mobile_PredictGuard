package com.senai.predictguard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.senai.predictguard.R;
import com.senai.predictguard.model.OS;

import java.util.List;

public class OSAdapter extends RecyclerView.Adapter<OSAdapter.OSViewHolder> {

    public interface OnOSActionListener {
        void onVerDetalhes(OS servico);
    }

    private final List<OS>            listaOS;
    private final OnOSActionListener  listener;

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

        private final TextView       tvTitulo;
        private final TextView       tvStatus;
        private final TextView       tvDescricao;
        private final MaterialButton btnDetalhes;
        // ✅ FIX: referência ao CardView que envolve o status badge
        private final CardView       cardStatusBadge;

        public OSViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitulo        = itemView.findViewById(R.id.tvTituloOS);
            tvStatus        = itemView.findViewById(R.id.tvStatusOS);
            tvDescricao     = itemView.findViewById(R.id.tvDescricaoOS);
            btnDetalhes     = itemView.findViewById(R.id.btnDetalhesOS);
            cardStatusBadge = itemView.findViewById(R.id.cardStatusBadge); // ✅
        }

        public void bind(OS servico, OnOSActionListener listener) {
            tvTitulo.setText(servico.getTipo() != null ? servico.getTipo() : "Sem título");
            tvDescricao.setText(servico.getDescricao() != null
                    ? servico.getDescricao() : "Sem descrição");

            String statusApi = servico.getServicoStatus() != null
                    ? servico.getServicoStatus() : "solicitado";

            // ✅ FIX: define texto, cor de fundo do CardView e cor do texto
            String statusDisplay;
            int bgColor;
            int textColor;

            switch (statusApi) {
                case "em_andamento":
                    statusDisplay = "Em Andamento";
                    bgColor   = 0xFFE3F2FD; // azul claro
                    textColor = 0xFF1565C0; // azul escuro
                    break;
                case "concluido":
                    statusDisplay = "Concluído";
                    bgColor   = 0xFFE8F5E9; // verde claro
                    textColor = 0xFF2E7D32; // verde escuro
                    break;
                case "cancelado":
                    statusDisplay = "Cancelado";
                    bgColor   = 0xFFFFEBEE; // vermelho claro
                    textColor = 0xFFC62828; // vermelho escuro
                    break;
                default: // solicitado
                    statusDisplay = "Solicitado";
                    bgColor   = 0xFFFFF4E5; // laranja claro (cor original)
                    textColor = 0xFFFF9800; // laranja (cor original)
                    break;
            }

            tvStatus.setText(statusDisplay);
            tvStatus.setTextColor(textColor);

            // ✅ FIX: muda a cor do CardView (não do TextView)
            if (cardStatusBadge != null) {
                cardStatusBadge.setCardBackgroundColor(bgColor);
            }

            btnDetalhes.setOnClickListener(v -> listener.onVerDetalhes(servico));
        }
    }
}