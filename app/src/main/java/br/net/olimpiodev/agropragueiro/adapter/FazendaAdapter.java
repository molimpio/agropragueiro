package br.net.olimpiodev.agropragueiro.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.dao.ClienteDao;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaAdapter extends RecyclerView.Adapter<FazendaAdapter.FazendaViewHolder> {

    private List<Fazenda> fazendas;
    private static FazendaAdapter.ItemClickListener clickListener;

    public FazendaAdapter(List<Fazenda> fazendas) {
        this.fazendas = fazendas;
    }

    @NonNull
    @Override
    public FazendaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fazenda_card, viewGroup, false);
        return new FazendaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FazendaViewHolder fazendaViewHolder, int position) {
        Fazenda fazenda = fazendas.get(position);
        String cidadeEstado = "Cidade: " + fazenda.getCidade() + " - UF: " + fazenda.getUf();
        String area = "Área(ha): " + String.valueOf(Utils.round(fazenda.getAreaHa(), 2));

        fazendaViewHolder.nome.setText(fazenda.getNome());
        fazendaViewHolder.cliente.setText(fazenda.getCliente().getNome());
        fazendaViewHolder.nomeCidadeEstado.setText(cidadeEstado);
        fazendaViewHolder.area.setText(area);
    }

    @Override
    public int getItemCount() {
        return fazendas.size();
    }

    public void setClickListener(FazendaAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public class FazendaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nome;
        final TextView cliente;
        final TextView nomeCidadeEstado;
        final TextView area;
        final Button btOpcoes;

        FazendaViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.tv_nome_fc);
            cliente = view.findViewById(R.id.tv_cliente_fc);
            nomeCidadeEstado = view.findViewById(R.id.tv_cidade_uf_fc);
            area = view.findViewById(R.id.tv_area_fc);

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


