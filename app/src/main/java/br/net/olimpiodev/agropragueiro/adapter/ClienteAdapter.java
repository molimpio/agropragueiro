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
import br.net.olimpiodev.agropragueiro.model.Cliente;

public class ClienteAdapter extends RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder> {

    private List<Cliente> clientes;
    private Context context;
    private static ClienteAdapter.ItemClickListener clickListener;
    private Cliente cliente;

    public ClienteAdapter(List<Cliente> clientes, Context context) {
        this.clientes = clientes;
        this.context = context;
    }

    @NonNull
    @Override
    public ClienteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cliente_card, viewGroup, false);
        return new ClienteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClienteViewHolder clienteViewHolder, int position) {
        cliente = clientes.get(position);

        String cidadeEstado = "Cidade: " + cliente.getCidade() + " - UF: " + cliente.getUf();
        clienteViewHolder.nome.setText(cliente.getNome());
        clienteViewHolder.nomeCidadeEstado.setText(cidadeEstado);
        clienteViewHolder.categoria.setText(cliente.getCategoria());
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    public void setClickListener(ClienteAdapter.ItemClickListener itemClickListener) {
        clickListener = itemClickListener;
    }

    public class ClienteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView nome;
        final TextView nomeCidadeEstado;
        final TextView categoria;
        final Button btOpcoes;

        ClienteViewHolder(View view) {
            super(view);
            nome = view.findViewById(R.id.tv_nome_cc);
            nomeCidadeEstado = view.findViewById(R.id.tv_cidade_uf_cc);
            categoria = view.findViewById(R.id.tv_categoria_cc);

            btOpcoes = view.findViewById(R.id.btn_opoes_cc);
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


