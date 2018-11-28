package br.net.olimpiodev.agropragueiro.fragment.Lista;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.ClienteAdapter;
import br.net.olimpiodev.agropragueiro.fragment.Cadastro.ClienteCadastroFragment;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteListaFragment extends Fragment {

    private AppDatabase db;
    private RecyclerView rvClientes;
    private TextView tvListaVazia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cliente_lista, container, false);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        rvClientes = view.findViewById(R.id.rv_clientes);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_cliente);

        FloatingActionButton fabCadastroCliente = view.findViewById(R.id.fab_cadastro_cliente);
        fabCadastroCliente.setOnClickListener(view1 -> openCadastro(null));
        GetClientes getClientes = new GetClientes();
        getClientes.execute();
        return view;
    }

    private void startRecyclerView(List<Cliente> clientes) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvClientes.setLayoutManager(layoutManager);

        ClienteAdapter clienteAdapter = new ClienteAdapter(clientes);
        rvClientes.setAdapter(clienteAdapter);

        clienteAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_cc) {
                final Cliente cliente = clientes.get(position);
                opcoes(cliente);
            }
        });
    }

    private void openCadastro(Cliente cliente) {
        ClienteCadastroFragment cdf = new ClienteCadastroFragment();

        if (cliente != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(getResources().getString(R.string.cliente_param), cliente);
            cdf.setArguments(bundle);
        }

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.frg_principal, cdf).commit();
    }

    private void openListaFazendas(Cliente cliente) {
        FazendaListaFragment flf = new FazendaListaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(getResources().getString(R.string.cliente_param), cliente);
        flf.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.frg_principal, flf).commit();
    }

    private void opcoes(final Cliente cliente) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_cliente_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 3, (dialog, item) -> {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        openListaFazendas(cliente);
                        break;
                    case 1:
                        dialog.dismiss();
                        openCadastro(cliente);
                        break;
                    case 2:
                        // excluir
                        break;
                }
            });

            String cancelar = getResources().getString(R.string.cancelar);

            builder.setNegativeButton(cancelar, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Utils.showMessage(getContext(), "", 3);
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.logar(ex.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetClientes extends AsyncTask<Void, Void, List<Cliente>> {

        @Override
        protected List<Cliente> doInBackground(Void... voids) {
            List<Cliente> clientes = db.clienteDao().getClientes(true);
            return clientes;
        }

        @Override
        protected void onPostExecute(List<Cliente> clientes) {
            if (clientes.size() == 0) {
                tvListaVazia.setVisibility(View.VISIBLE);
            } else {
                tvListaVazia.setVisibility(View.GONE);
                startRecyclerView(clientes);
            }
        }
    }
}
