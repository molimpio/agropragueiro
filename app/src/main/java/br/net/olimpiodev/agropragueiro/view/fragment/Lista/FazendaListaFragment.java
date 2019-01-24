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
import br.net.olimpiodev.agropragueiro.adapter.FazendaAdapter;
import br.net.olimpiodev.agropragueiro.contracts.FazendaListaContrato;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;
import br.net.olimpiodev.agropragueiro.presenter.FazendaListaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.fragment.Cadastro.FazendaCadastroFragment;

public class FazendaListaFragment extends Fragment implements FazendaListaContrato.FazendaListaView {

    private RecyclerView rvFazendas;
    private TextView tvListaVazia;
    private FazendaListaContrato.FazendaListaPresenter presenter;
    private FazendaAdapter fazendaAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fazenda_lista, container, false);
        getFazendas();
        setupView(view);
        setupRecyclerView();
        return view;
    }

    private void getFazendas() {
        try {
            int clienteId = 0;
            Bundle bundle = this.getArguments();

            if (bundle != null) {
                String keyBundle = getString(R.string.cliente_param);
                Cliente c = (Cliente) bundle.getSerializable(keyBundle);
                clienteId = c.getId();
            }

            presenter = new FazendaListaPresenter(this, getContext());
            presenter.getFazendas(clienteId);
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_argumentos_fazenda), 0);
        }
    }

    private void setupView(View view) {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(getString(R.string.fazendas));

            rvFazendas = view.findViewById(R.id.rv_fazendas);
            tvListaVazia = view.findViewById(R.id.tv_lista_vazia_fazenda);

            FloatingActionButton fabCadastroFazenda = view.findViewById(R.id.fab_cadastro_fazenda);
            fabCadastroFazenda.setOnClickListener(view1 -> openCadastro(null));
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_view_fazendas), 0);
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFazendas.setLayoutManager(layoutManager);
        fazendaAdapter = new FazendaAdapter();
        rvFazendas.setAdapter(fazendaAdapter);
    }

    private void openCadastro(FazendaCliente fazenda) {
        try {
            FazendaCadastroFragment fdf = new FazendaCadastroFragment();

            if (fazenda != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(getString(R.string.fazenda_param), fazenda);
                fdf.setArguments(bundle);
            }

            getFragmentManager().beginTransaction().replace(R.id.frg_principal, fdf).commit();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_cadastro_fazendas), 0);
        }
    }

    private void openListaTalhoes(FazendaCliente fazenda) {
        try {
            TalhaoListaFragment tlf = new TalhaoListaFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(getString(R.string.fazenda_param), fazenda);
            tlf.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frg_principal, tlf).commit();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_lista_talhoes_fazenda), 0);
        }
    }

    private void opcoesDialog(final FazendaCliente fazenda) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_fazenda_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 3, (dialog, item) -> {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        openListaTalhoes(fazenda);
                        break;
                    case 1:
                        dialog.dismiss();
                        openCadastro(fazenda);
                        break;
                    case 2:
                        // excluir
                        break;
                }
            });

            builder.setNegativeButton(getString(R.string.cancelar), (dialogInterface, i)
                    -> dialogInterface.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_opcoes_fazenda), 0);
        }
    }

    @Override
    public void listarFazendas(List<FazendaCliente> fazendas) {
        fazendaAdapter.setFazendas(fazendas);
        fazendaAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_fc) {
                final FazendaCliente fazendaCliente = fazendas.get(position);
                opcoesDialog(fazendaCliente);
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
