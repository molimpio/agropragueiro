package br.net.olimpiodev.agropragueiro.fragment.Lista;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.ClienteAdapter;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.service.ClienteService;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteListaFragment extends Fragment {

    private RecyclerView rvClientes;
    private ClienteService clienteService;
    private TextView tvListaVazia;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        try {
            View view = inflater.inflate(R.layout.fragment_cliente_lista, container, false);

            rvClientes = view.findViewById(R.id.rv_clientes);
            tvListaVazia = view.findViewById(R.id.tv_lista_vazia_cliente);

            FloatingActionButton fabCadastroCliente = view.findViewById(R.id.fab_cadastro_cliente);
            fabCadastroCliente.setOnClickListener(view1 -> clienteService.openCadastro(null));

            clienteService = new ClienteService(getContext(), getFragmentManager());
            getClientes();

            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(getString(R.string.clientes));

            return view;
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_view_clientes), 0);
            return null;
        }
    }

    private void getClientes() {
        try {
            List<Cliente> clientes = clienteService.getClientes().execute().get();
            if (clientes.size() == 0) {
                tvListaVazia.setVisibility(View.VISIBLE);
            } else {
                tvListaVazia.setVisibility(View.GONE);
                startRecyclerView(clientes);
            }
        } catch (ExecutionException | InterruptedException e) {
            Utils.showMessage(getContext(), getString(R.string.erro_get_clientes), 0);
        }
    }

    private void startRecyclerView(List<Cliente> clientes) {
        try {
            if (clientes != null) {
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
            } else {
                Utils.showMessage(getContext(), getString(R.string.erro_adapter_clientes), 0);
            }
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_adapter_clientes), 0);
        }
    }
}
