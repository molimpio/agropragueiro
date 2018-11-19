package br.net.olimpiodev.agropragueiro.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.ClienteAdapter;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;

public class ClienteListaFragment extends Fragment {

    private RecyclerView rvClientes;
    private TextView tvListaVazia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cliente_lista, container, false);
        rvClientes = view.findViewById(R.id.rv_clientes);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_cliente);
        FloatingActionButton fabCadastroCliente = view.findViewById(R.id.fab_cadastro_cliente);
        fabCadastroCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClienteDialogFragment cdf = new ClienteDialogFragment();
                cdf.show(getFragmentManager(), "frag");
            }
        });
        startRecyclerView(view);
        return view;
    }

    private void startRecyclerView(View view) {
        RealmResults<Cliente> clientes = Realm.getDefaultInstance().where(Cliente.class)
                .equalTo("ativo", true)
                .findAll().sort("nome");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvClientes.setLayoutManager(layoutManager);

        ClienteAdapter clienteAdapter = new ClienteAdapter(clientes);
        rvClientes.setAdapter(clienteAdapter);

        clienteAdapter.setClickListener(new ClienteAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (view.getId() == R.id.btn_opoes_cc) {
                    final Cliente cliente = clientes.get(position);
                    opcoes(cliente);
                }
            }
        });

        if (Objects.requireNonNull(rvClientes.getAdapter()).getItemCount() == 0) {
            tvListaVazia.setVisibility(view.VISIBLE);
        } else {
            tvListaVazia.setVisibility(view.GONE);
        }
    }

    private void opcoes(final Cliente cliente) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_cliente_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(
                    OPCOES, 3, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            switch (item) {
                                case 1:
                                    // ver fazendas
                                    break;
                                case 2:
                                    // editar
                                    break;
                                case 3:
                                    // excluir
                                    break;
                            }
                        }
                    });

            String ok = getResources().getString(R.string.ok);
            String cancelar = getResources().getString(R.string.cancelar);

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Log.i("item", "" + i);
                }
            });

            builder.setNegativeButton(cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Utils.showMessage(getContext(), "", 3);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Log.i("erro", ex.getMessage());
        }
    }

}
