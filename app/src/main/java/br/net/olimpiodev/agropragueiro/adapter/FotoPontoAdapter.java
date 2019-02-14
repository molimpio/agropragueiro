package br.net.olimpiodev.agropragueiro.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.FotoRegistro;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FotoPontoAdapter extends RecyclerView.Adapter<FotoPontoAdapter.FotoViewHolder> {

    private List<FotoRegistro> fotos;
    private FotoPontoAdapter.ItemClickListener clickListener;
    private int screenSize = 0;
    private String detalhes = "";
    private String pontoString = "";

    public FotoPontoAdapter() { this.fotos = new ArrayList<>(); }

    @Override
    public FotoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.foto_card, viewGroup, false);
        return new FotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FotoViewHolder fotoViewHolder, int position) {

        FotoRegistro foto = fotos.get(position);
        String url = "file://" + foto.getPath();
        Picasso.get().load(url).resize(screenSize, screenSize).into(fotoViewHolder.ivFoto);

        fotoViewHolder.tvPontoFoto.setText(pontoString);
        fotoViewHolder.tvDetalheFoto.setText(detalhes);
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    public void setClickListener(FotoPontoAdapter.ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setFotos(List<FotoRegistro> fotos, int screenSize, String detalhes, String ponto) {
        this.screenSize = screenSize;
        this.detalhes = detalhes;
        this.pontoString = ponto;
        this.fotos = fotos;
        notifyDataSetChanged();
    }

    public class FotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView ivFoto;
        final TextView tvPontoFoto;
        final TextView tvDetalheFoto;

        FotoViewHolder(View view) {
            super(view);
            ivFoto = view.findViewById(R.id.iv_foto);
            tvPontoFoto = view.findViewById(R.id.tv_ponto_foto);
            tvDetalheFoto = view.findViewById(R.id.tv_detalhes_foto);
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
