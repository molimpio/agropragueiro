package br.net.olimpiodev.agropragueiro.view.fragment.Lista;

import android.app.AlertDialog;
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

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.ClienteAdapter;
import br.net.olimpiodev.agropragueiro.contracts.ClienteListaContrato;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.presenter.ClienteListaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.fragment.Cadastro.ClienteCadastroFragment;

public class ClienteListaFragment extends Fragment implements ClienteListaContrato.ClienteListaView {

    private RecyclerView rvClientes;
    private TextView tvListaVazia;
    private ClienteListaContrato.ClienteListaPresenter presenter;
    private ClienteAdapter clienteAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cliente_lista, container, false);
        setupView(view);
        setupRecyclerView();
        presenter = new ClienteListaPresenter(this, getContext());
        presenter.getClientes();
        return view;
    }

    private void setupView(View view) {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(getString(R.string.clientes));

            rvClientes = view.findViewById(R.id.rv_clientes);
            tvListaVazia = view.findViewById(R.id.tv_lista_vazia_cliente);

            FloatingActionButton fabCadastroCliente = view.findViewById(R.id.fab_cadastro_cliente);
            fabCadastroCliente.setOnClickListener(view1 -> openCadastro(null));
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_view_clientes), 0);
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvClientes.setLayoutManager(layoutManager);
        clienteAdapter = new ClienteAdapter();
        rvClientes.setAdapter(clienteAdapter);
    }

    private void openCadastro(Cliente cliente) {
        try {
            ClienteCadastroFragment cdf = new ClienteCadastroFragment();

            if (cliente != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.cliente_param), cliente);
                cdf.setArguments(bundle);
            }

            getFragmentManager().beginTransaction().replace(R.id.frg_principal, cdf).commit();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_cadastro_clientes), 0);
        }
    }

    private void openListaFazendas(Cliente cliente) {
        try {
            FazendaListaFragment flf = new FazendaListaFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.cliente_param), cliente);
            flf.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frg_principal, flf).commit();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_lista_fazendas_cliente), 0);
        }
    }

    public void opcoesDialog(final Cliente cliente) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_cliente_card);
            final String dialogTitle = getString(R.string.titulo_opcoes_card);

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

            builder.setNegativeButton(getString(R.string.cancelar), (dialogInterface, i) -> dialogInterface.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_opcoes_cliente), 0);
        }
    }

    @Override
    public void listarClientes(List<Cliente> clientes) {
        clienteAdapter.setClientes(clientes);
        clienteAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_cc) {
                final Cliente cliente = clientes.get(position);
                opcoesDialog(cliente);
            }
        });
    }

    @Override
    public void exibirListaVazia() {
        tvListaVazia.setVisibility(View.VISIBLE);
    }

    @Override
    public void exibirError(String mensagem) {
        Utils.showMessage(getContext(), mensagem, 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }
}
