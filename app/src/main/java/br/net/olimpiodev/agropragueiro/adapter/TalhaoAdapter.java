package br.net.olimpiodev.agropragueiro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class TalhaoAdapter extends RecyclerView.Adapter<TalhaoAdapter.TalhaoViewHolder> {

    private List<TalhaoFazenda> talhoes;
    private static TalhaoAdapter.ItemClickListener clickListener;

    public TalhaoAdapter(List<TalhaoFazenda> talhoes) {
        this.talhoes = talhoes;
    }

    @Override
    public TalhaoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.talhao_card, viewGroup, false);
        return new TalhaoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TalhaoViewHolder talhaoViewHolder, int position) {
        TalhaoFazenda talhaoFazenda = talhoes.get(position);

        String area = "Área(ha): " + String.valueOf(Utils.round(talhaoFazenda.getAreaHa(), 2));

        talhaoViewHolder.nome.setText(talhaoFazenda.getNomeTalhao());
        talhaoViewHolder.fazenda.setText(talhaoFazenda.getNomeFazenda());
        talhaoViewHolder.area.setText(area);
    }

    @Override
    public int getItemCount() {
        return talhoes.size();
    }

    public void setClickListener(TalhaoAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public class TalhaoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nome;
        final TextView fazenda;
        final TextView area;
        final Button btOpcoes;

        TalhaoViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.tv_nome_tc);
            fazenda = view.findViewById(R.id.tv_fazenda_tc);
            area = view.findViewById(R.id.tv_area_tc);

            btOpcoes = view.findViewById(R.id.btn_opoes_tc);
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


