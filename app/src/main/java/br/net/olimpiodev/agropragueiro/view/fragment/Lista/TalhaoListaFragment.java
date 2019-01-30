package br.net.olimpiodev.agropragueiro.view.fragment.Lista;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.TalhaoAdapter;
import br.net.olimpiodev.agropragueiro.contracts.TalhaoListaContrato;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.presenter.TalhaoListaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.activity.MapaActivity;
import br.net.olimpiodev.agropragueiro.view.fragment.Cadastro.TalhaoCadastroFragment;

import static android.app.Activity.RESULT_OK;

public class TalhaoListaFragment extends Fragment implements TalhaoListaContrato.TalhaoListaView {

    private RecyclerView rvTalhoes;
    private TextView tvListaVazia;
    private TalhaoListaContrato.TalhaoListaPresenter presenter;
    private TalhaoAdapter talhaoAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talhao_lista, container, false);
        getTalhoes();
        setupView(view);
        setupRecyclerView();
        return view;
    }

    private void getTalhoes() {
        try {
            int fazendaId = 0;
            Bundle bundle = this.getArguments();

            if (bundle != null) {
                String keyBundle = getString(R.string.fazenda_param);
                FazendaCliente fc = (FazendaCliente) bundle.getSerializable(keyBundle);
                fazendaId = fc.getIdFazenda();
            }

            presenter = new TalhaoListaPresenter(this, getContext());
            presenter.getTalhoes(fazendaId);
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_ler_argumentos_talhao), 0);
        }
    }

    private void setupView(View view) {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(getString(R.string.talhoes));

            rvTalhoes = view.findViewById(R.id.rv_talhoes);
            tvListaVazia = view.findViewById(R.id.tv_lista_vazia_talhao);

            FloatingActionButton fabCadastroTalhao = view.findViewById(R.id.fab_cadastro_talhao);
            fabCadastroTalhao.setOnClickListener(view1 -> openCadastro(null));
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_view_talhao), 0);
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTalhoes.setLayoutManager(layoutManager);
        talhaoAdapter = new TalhaoAdapter();
        rvTalhoes.setAdapter(talhaoAdapter);
    }

    private void openCadastro(TalhaoFazenda talhaoFazenda) {
        try {
            TalhaoCadastroFragment tcf = new TalhaoCadastroFragment();

            if (talhaoFazenda != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(getResources().getString(R.string.talhao_param), talhaoFazenda);
                tcf.setArguments(bundle);
            }

            getFragmentManager().beginTransaction().replace(R.id.frg_principal, tcf).commit();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_cadastro_talhao), 0);
        }
    }

    private void openListaAmostragens(TalhaoFazenda talhaoFazenda) {
        try {
            AmostragemListaFragment alf = new AmostragemListaFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(getResources().getString(R.string.amostragem_param), talhaoFazenda);
            alf.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frg_principal, alf).commit();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_lista_amostragens_talhao), 0);
        }
    }

    private void opcoesDialog(final TalhaoFazenda talhaoFazenda) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_talhao_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 4, (dialog, item) -> {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        presenter.openMapa(talhaoFazenda.getIdTalhao());
                        break;
                    case 1:
                        dialog.dismiss();
                        openListaAmostragens(talhaoFazenda);
                        break;
                    case 2:
                        dialog.dismiss();
                        openCadastro(talhaoFazenda);
                        break;
                    case 3:
                        //excluir
                        break;
                }
            });

            builder.setNegativeButton(getString(R.string.cancelar), (dialogInterface, i)
                    -> dialogInterface.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_opcoes_talhao), 0);
        }
    }

    @Override
    public void listarTalhoes(List<TalhaoFazenda> talhoes) {
        talhaoAdapter.setTalhoes(talhoes);
        talhaoAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_tc) {
                final TalhaoFazenda talhaoFazenda = talhoes.get(position);
                opcoesDialog(talhaoFazenda);
            }
        });
    }

    @Override
    public void exibirListaVazia() {
        tvListaVazia.setVisibility(View.VISIBLE);
    }

    @Override
    public void openMapa(Talhao talhao) {
        try {
            Intent mapaIntent = new Intent(getContext(), MapaActivity.class);
            mapaIntent.putExtra(getString(R.string.talhao_id_param), talhao.getId());

            if (!talhao.getContorno().isEmpty()) {
                mapaIntent.putExtra(getResources().getString(R.string.contorno_param), talhao.getContorno());
            }

            startActivity(mapaIntent);
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_carregar_dados_mapa), 0);
        }
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
