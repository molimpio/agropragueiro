package br.net.olimpiodev.agropragueiro.view.activity.Lista;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.TalhaoAdapter;
import br.net.olimpiodev.agropragueiro.contracts.TalhaoCadastroContrato;
import br.net.olimpiodev.agropragueiro.contracts.TalhaoListaContrato;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.presenter.TalhaoCadastroPresenter;
import br.net.olimpiodev.agropragueiro.presenter.TalhaoListaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.activity.MapaActivity;

public class TalhaoListaActivity extends AppCompatActivity
        implements TalhaoListaContrato.TalhaoListaView,
        TalhaoCadastroContrato.TalhaoCadastroView {

    private RecyclerView rvTalhoes;
    private TextView tvListaVazia;
    private TalhaoListaContrato.TalhaoListaPresenter presenter;
    private TalhaoCadastroContrato.TalhaoCadastroPresenter presenterCadastro;
    private TalhaoAdapter talhaoAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talhao_lista);

        presenter = new TalhaoListaPresenter(this, TalhaoListaActivity.this);
        presenter.getTalhoes();
        presenterCadastro = new TalhaoCadastroPresenter(this, TalhaoListaActivity.this);
        setupView();
        setupRecyclerView();
    }

    private void setupView() {
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.talhoes));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            rvTalhoes = findViewById(R.id.rv_talhoes);
            tvListaVazia = findViewById(R.id.tv_lista_vazia_talhao);

            FloatingActionButton fabCadastroTalhao = findViewById(R.id.fab_cadastro_talhao);
            fabCadastroTalhao.setOnClickListener(view1 -> openCadastro(null));
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_view_talhao), 0);
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvTalhoes.setLayoutManager(layoutManager);
        talhaoAdapter = new TalhaoAdapter();
        rvTalhoes.setAdapter(talhaoAdapter);
    }

    private void openCadastro(TalhaoFazenda talhaoFazenda) {
        try {
            presenterCadastro.exibirView(talhaoFazenda);
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_abrir_cadastro_talhao), 0);
        }
    }

    private void opcoesDialog(final TalhaoFazenda talhaoFazenda) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_talhao_card);
            final String dialogTitle = getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 4, (dialog, item) -> {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        presenter.openMapa(talhaoFazenda.getIdTalhao());
                        break;
                    case 1:
                        dialog.dismiss();
                        openCadastro(talhaoFazenda);
                        break;
                    case 2:
                        //excluir
                        dialog.dismiss();
                        break;
                }
            });

            builder.setNegativeButton(getString(R.string.cancelar), (dialogInterface, i)
                    -> dialogInterface.dismiss());

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_abrir_opcoes_talhao), 0);
        }
    }

    @Override
    public void listarTalhoes(List<TalhaoFazenda> talhoes) {
        if (talhoes.size() == 0) rvTalhoes.setVisibility(View.GONE);
        else rvTalhoes.setVisibility(View.VISIBLE);

        talhaoAdapter.setTalhoes(talhoes);
        talhaoAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_tc) {
                final TalhaoFazenda talhaoFazenda = talhoes.get(position);
                opcoesDialog(talhaoFazenda);
            }
        });
    }

    @Override
    public void openMapa(Talhao talhao, int qtdeAmostragemByTalhaoId) {
        try {

            Intent mapaIntent = new Intent(this, MapaActivity.class);
            mapaIntent.putExtra(getString(R.string.talhao_id_param), talhao.getId());
            mapaIntent.putExtra(getString(R.string.qtde_amostragem_by_talhao), qtdeAmostragemByTalhaoId);

            if (!talhao.getContorno().isEmpty()) {
                mapaIntent.putExtra(getResources().getString(R.string.contorno_param), talhao.getContorno());
            }

            startActivityForResult(mapaIntent, Utils.COD_CONTORNO_CADASTRADO);

        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_carregar_dados_mapa), 0);
        }
    }

    @Override
    public void atualizarAdapter(List<TalhaoFazenda> talhoes) {
        if (talhoes.size() == 0) rvTalhoes.setVisibility(View.GONE);
        else rvTalhoes.setVisibility(View.VISIBLE);

        listarTalhoes(talhoes);
    }

    @Override
    public void exibirListaVazia() {
        rvTalhoes.setVisibility(View.GONE);
        tvListaVazia.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1) {
            presenter.getTalhoes();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }
}
