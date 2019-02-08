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
import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistroInfo;

public class PontoRegistroAdapter extends
        RecyclerView.Adapter<PontoRegistroAdapter.PontoRegistroViewHolder> {

    private List<PontoAmostragemRegistroInfo> pontos;
    private static PontoRegistroAdapter.ItemClickListener clickListener;

    public PontoRegistroAdapter() { this.pontos = new ArrayList<>(); }

    @Override
    public PontoRegistroViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ponto_amostragem_card, viewGroup, false);
        return new PontoRegistroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PontoRegistroViewHolder pontoRegistroViewHolder, int position) {
        PontoAmostragemRegistroInfo ponto = pontos.get(position);

        String pontoString = "Ponto: " + ponto.getIdPontoRegistro();
        String amostragem = "Amostragem: " + ponto.getAmostragem();
        String pragaQtdeDano = "Praga: " + ponto.getPraga() + "  Qtde: "
                + ponto.getQtde() + "  Dano: " + ponto.getDano();
        String cliente = "Cliente: " + ponto.getCliente();
        String fazenda = "Fazenda: " + ponto.getFazenda();
        String talhao = "Talhão: " + ponto.getTalhao();

        pontoRegistroViewHolder.ponto.setText(pontoString);
        pontoRegistroViewHolder.amostragem.setText(amostragem);
        pontoRegistroViewHolder.pragaQtdeDano.setText(pragaQtdeDano);
        pontoRegistroViewHolder.cliente.setText(cliente);
        pontoRegistroViewHolder.fazenda.setText(fazenda);
        pontoRegistroViewHolder.talhao.setText(talhao);
    }

    @Override
    public int getItemCount() {
        return pontos.size();
    }

    public void setClickListener(PontoRegistroAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public void setPontos(List<PontoAmostragemRegistroInfo> pontos) {
        this.pontos = pontos;
        notifyDataSetChanged();
    }

    public class PontoRegistroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView ponto;
        final TextView amostragem;
        final TextView pragaQtdeDano;
        final TextView cliente;
        final TextView fazenda;
        final TextView talhao;
        final Button btOpcoes;

        PontoRegistroViewHolder(View view) {
            super(view);
            ponto = view.findViewById(R.id.tv_ponto_pa);
            amostragem = view.findViewById(R.id.tv_amostragem_pa);
            pragaQtdeDano = view.findViewById(R.id.tv_praga_qtde_dano_pa);
            cliente = view.findViewById(R.id.tv_cliente_pa);
            fazenda = view.findViewById(R.id.tv_fazenda_pa);
            talhao = view.findViewById(R.id.tv_talhao_pa);

            btOpcoes = view.findViewById(R.id.btn_opoes_ponto_amostragem);
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
