package br.net.olimpiodev.agropragueiro.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.PrevisaoTempoDados;

public class PrevisaoTempoDadosAdapter extends RecyclerView.Adapter<PrevisaoTempoDadosAdapter.PrevisaoTempoDadosViewHolder> {

    private List<PrevisaoTempoDados> previsaoTempoDados;
    private Context context;

    public PrevisaoTempoDadosAdapter(List<PrevisaoTempoDados> dados) {
        this.previsaoTempoDados = dados;
    }

    @NonNull
    @Override
    public PrevisaoTempoDadosViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.previsao_tempo_card, viewGroup, false);
        return new PrevisaoTempoDadosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PrevisaoTempoDadosAdapter.PrevisaoTempoDadosViewHolder previsaoTempoDadosViewHolder, int position) {
        PrevisaoTempoDados previsaoTempo = previsaoTempoDados.get(position);

        String data = "Data: " + previsaoTempo.getDia();
        String min = "Mínima: " + previsaoTempo.getMin() + "°";
        String max = "Máxima: " + previsaoTempo.getMax() + "°";

        previsaoTempoDadosViewHolder.ivPrevisaoTempo.setImageDrawable(context.getDrawable(previsaoTempo.getImagemId()));
        previsaoTempoDadosViewHolder.tvDia.setText(data);
        previsaoTempoDadosViewHolder.tvMin.setText(min);
        previsaoTempoDadosViewHolder.tvMax.setText(max);
        previsaoTempoDadosViewHolder.tvCondicao.setText(previsaoTempo.getCondicao());
    }

    @Override
    public int getItemCount() {
        return previsaoTempoDados.size();
    }

    public class PrevisaoTempoDadosViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivPrevisaoTempo;
        final TextView tvDia;
        final TextView tvMin;
        final TextView tvMax;
        final TextView tvCondicao;

        PrevisaoTempoDadosViewHolder(View view) {
            super(view);
            ivPrevisaoTempo = view.findViewById(R.id.iv_previsao_tempo);
            tvDia = view.findViewById(R.id.tv_dia);
            tvMin = view.findViewById(R.id.tv_min);
            tvMax = view.findViewById(R.id.tv_max);
            tvCondicao = view.findViewById(R.id.tv_condicao);
        }
    }
}
