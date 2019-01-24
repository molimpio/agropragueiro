package br.net.olimpiodev.agropragueiro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;

public class FazendaAdapter extends RecyclerView.Adapter<FazendaAdapter.FazendaViewHolder> {

    private List<FazendaCliente> fazendas;
    private static FazendaAdapter.ItemClickListener clickListener;

    public FazendaAdapter() {
        this.fazendas = new ArrayList<>();
    }

    @NonNull
    @Override
    public FazendaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fazenda_card, viewGroup, false);
        return new FazendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FazendaViewHolder fazendaViewHolder, int position) {
        FazendaCliente fazenda = fazendas.get(position);
        String cidadeEstado = "Cidade: " + fazenda.getCidadeFazenda() + " - UF: " + fazenda.getUfFazenda();

        fazendaViewHolder.nome.setText(fazenda.getNomeFazenda());
        fazendaViewHolder.cliente.setText(fazenda.getNomeCliente());
        fazendaViewHolder.nomeCidadeEstado.setText(cidadeEstado);
    }

    @Override
    public int getItemCount() {
        return fazendas.size();
    }

    public void setClickListener(FazendaAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public void setFazendas(List<FazendaCliente> fazendas) {
        this.fazendas = fazendas;
        notifyDataSetChanged();
    }

    public class FazendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nome;
        final TextView cliente;
        final TextView nomeCidadeEstado;
        final Button btOpcoes;

        FazendaViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.tv_nome_fc);
            cliente = view.findViewById(R.id.tv_cliente_fc);
            nomeCidadeEstado = view.findViewById(R.id.tv_cidade_uf_fc);

            btOpcoes = view.findViewById(R.id.btn_opoes_fc);
            btOpcoes.setOnClickListener(this);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null)
                clickListener.onItemClick(getAdapterPosition(), view);
        }
    }

    public interface ItemClickListener {
        void onItemClick(int position, View view);
    }

}


