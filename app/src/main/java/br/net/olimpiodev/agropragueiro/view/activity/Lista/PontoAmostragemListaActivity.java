package br.net.olimpiodev.agropragueiro.view.activity.Lista;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.PontoRegistroAdapter;
import br.net.olimpiodev.agropragueiro.contracts.PontoAmostragemListaContrato;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistroInfo;
import br.net.olimpiodev.agropragueiro.presenter.PontoAmostragemListaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class PontoAmostragemListaActivity extends AppCompatActivity
        implements PontoAmostragemListaContrato.PontoAmostragemListaView {

    private RecyclerView rvPontos;
    private TextView tvListaVazia;
    private PontoAmostragemListaContrato.PontoAmostragemListaPresenter presenter;
    private PontoRegistroAdapter pontoRegistroAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto_amostragem_lista);

        getArgumentos();
        setupView();
        setupRecyclerView();
    }

    private void getArgumentos() {
        try {
            if (getIntent().hasExtra(getString(R.string.amostragem_id_param))) {
                int amostragemId = (int) getIntent().getSerializableExtra(getString(R.string.amostragem_id_param));
                presenter = new PontoAmostragemListaPresenter(this, PontoAmostragemListaActivity.this);
                presenter.getPontosAmostragens(amostragemId);
            }
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_param_pontos), 0);
        }
    }

    private void setupView() {
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.pontos_amostragem));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            rvPontos = findViewById(R.id.rv_ponto_amostragem);
            tvListaVazia = findViewById(R.id.tv_lista_vazia_ponto_amostragem);

        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_abrir_lista_pontos), 0);
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvPontos.setLayoutManager(layoutManager);
        pontoRegistroAdapter = new PontoRegistroAdapter();
        rvPontos.setAdapter(pontoRegistroAdapter);
    }

    @Override
    public void listarPontoAmostragens(List<PontoAmostragemRegistroInfo> pontosAmostragens) {
        pontoRegistroAdapter.setPontos(pontosAmostragens);
        pontoRegistroAdapter.setClickListener((((position, view) -> {
            if (view.getId() == R.id.btn_opoes_ponto_amostragem) {
                final PontoAmostragemRegistroInfo ponto = pontosAmostragens.get(position);

                String pontoString = "Ponto: " + ponto.getIdPontoAmostragemRegistro();

                String detalhes = "Praga: " + ponto.getPraga() + "  |  "
                        + "Qtde: " + ponto.getQtde() + "  |  "
                        + "Dano: " + ponto.getDano();

                Intent fotoIntent = new Intent(this, FotoPontoListaActivity.class);
                fotoIntent.putExtra(getString(R.string.ponto_registro_id), ponto.getIdPontoAmostragemRegistro());
                fotoIntent.putExtra(getString(R.string.ponto_registro_detalhes), detalhes);
                fotoIntent.putExtra(getString(R.string.ponto_registro_string), pontoString);

                startActivity(fotoIntent);
            }
        })));
    }

    @Override
    public void exibirListaVazia() {
        tvListaVazia.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
