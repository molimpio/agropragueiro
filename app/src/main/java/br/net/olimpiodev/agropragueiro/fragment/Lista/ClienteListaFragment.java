package br.net.olimpiodev.agropragueiro.fragment.Lista;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.service.ClienteService;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteListaFragment extends Fragment {

    private AppDatabase db;
    private RecyclerView rvClientes;
    private TextView tvListaVazia;
    private ClienteService clienteService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cliente_lista, container, false);

        db = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        rvClientes = view.findViewById(R.id.rv_clientes);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_cliente);

        FloatingActionButton fabCadastroCliente = view.findViewById(R.id.fab_cadastro_cliente);
        fabCadastroCliente.setOnClickListener(view1 -> clienteService.openCadastro(null));

        GetClientes getClientes = new GetClientes();
        getClientes.execute();

        clienteService = new ClienteService(getContext(), getFragmentManager());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Clientes");

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
                clienteService.opcoes(cliente);
            }
        });
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
