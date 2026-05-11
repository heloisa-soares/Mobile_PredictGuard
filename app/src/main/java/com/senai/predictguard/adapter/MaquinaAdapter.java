package com.senai.predictguard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.senai.predictguard.R;
import com.senai.predictguard.model.Maquina;

import java.util.List;

public class MaquinaAdapter extends RecyclerView.Adapter<MaquinaAdapter.MaquinaViewHolder> {

    private final List<Maquina> listaMaquinas;

    public MaquinaAdapter(List<Maquina> listaMaquinas) {
        this.listaMaquinas = listaMaquinas;
    }

    @NonNull
    @Override
    public MaquinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_maquina, parent, false);
        return new MaquinaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MaquinaViewHolder holder, int position) {
        holder.bind(listaMaquinas.get(position));
    }

    @Override
    public int getItemCount() { return listaMaquinas.size(); }

    public void atualizarLista(List<Maquina> novaLista) {
        listaMaquinas.clear();
        listaMaquinas.addAll(novaLista);
        notifyDataSetChanged();
    }

    static class MaquinaViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvNome;
        private final TextView tvSetor;
        private final TextView tvStatus;
        private final TextView tvCriticidade;

        public MaquinaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNome        = itemView.findViewById(R.id.tvNomeMaquina);
            tvSetor       = itemView.findViewById(R.id.tvSetorMaquina);
            tvStatus      = itemView.findViewById(R.id.tvStatusMaquina);
            tvCriticidade = itemView.findViewById(R.id.tvCriticidadeMaquina);
        }

        public void bind(Maquina maquina) {
            tvNome.setText(maquina.getNome() != null ? maquina.getNome() : "-");
            tvSetor.setText(maquina.getSetor() != null ? "Setor: " + maquina.getSetor() : "-");
            tvCriticidade.setText(maquina.getNivelCriticidade() != null
                    ? "Criticidade: " + maquina.getNivelCriticidade() : "-");

            String saude = maquina.getStatusSaude() != null ? maquina.getStatusSaude() : "Ok";
            tvStatus.setText(saude);

            int cor = "Alerta".equals(saude)
                    ? ContextCompat.getColor(itemView.getContext(), R.color.alerta_red)
                    : ContextCompat.getColor(itemView.getContext(), R.color.ok_green);
            tvStatus.setBackgroundColor(cor);
        }
    }
}