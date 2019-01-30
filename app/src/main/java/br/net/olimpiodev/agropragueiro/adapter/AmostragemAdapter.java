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
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;

public class AmostragemAdapter extends RecyclerView.Adapter<AmostragemAdapter.AmostragemViewHolder> {

    private List<AmostragemTalhao> amostragens;
    private static AmostragemAdapter.ItemClickListener clickListener;

    public AmostragemAdapter() {
        this.amostragens = new ArrayList<>();
    }

    @Override
    public AmostragemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.amostragem_card, viewGroup, false);
        return new AmostragemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmostragemViewHolder amostragemViewHolder, int position) {
        AmostragemTalhao amostragemTalhao = amostragens.get(position);

        String talhao = "Talhão: " + amostragemTalhao.getTalhaoNome();
        String data = "Data: " + amostragemTalhao.getData();
        String qtdePontos = "Qtde pontos: " + amostragemTalhao.getQtdePontos();

        amostragemViewHolder.nome.setText(amostragemTalhao.getNomeAmostragem());
        amostragemViewHolder.talhao.setText(talhao);
        amostragemViewHolder.qtdePontos.setText(qtdePontos);
        amostragemViewHolder.data.setText(data);
    }

    @Override
    public int getItemCount() {
        return amostragens.size();
    }

    public void setClickListener(AmostragemAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public void setAmostragens(List<AmostragemTalhao> amostragens) {
        this.amostragens = amostragens;
        notifyDataSetChanged();
    }

    public class AmostragemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nome;
        final TextView talhao;
        final TextView qtdePontos;
        final TextView data;
        final Button btOpcoes;

        AmostragemViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.tv_nome_ac);
            talhao= view.findViewById(R.id.tv_talhao_ac);
            qtdePontos = view.findViewById(R.id.tv_qtde_pontos_ac);
            data = view.findViewById(R.id.tv_data_ac);

            btOpcoes = view.findViewById(R.id.btn_opoes_ac);
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
