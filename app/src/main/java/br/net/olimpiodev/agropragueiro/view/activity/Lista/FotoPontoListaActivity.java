package br.net.olimpiodev.agropragueiro.view.activity.Lista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.FotoPontoAdapter;
import br.net.olimpiodev.agropragueiro.contracts.FotoPontoListaContrato;
import br.net.olimpiodev.agropragueiro.model.FotoRegistro;
import br.net.olimpiodev.agropragueiro.presenter.FotoPontoListaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FotoPontoListaActivity extends AppCompatActivity
        implements FotoPontoListaContrato.FotoPontoListaView {

    private RecyclerView rvFotos;
    private FotoPontoAdapter fotoPontoAdapter;
    private FotoPontoListaContrato.FotoPontoListaPresenter presenter;
    private int screenSize = 0;
    private String detalhes = "";
    private String pontoString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto_ponto_lista);

        setupView();
    }

    private void getFoto() {
        try {
            if (getIntent().hasExtra(getString(R.string.ponto_registro_id))) {
                int pontoRegistroId = (int) getIntent().getSerializableExtra(getString(R.string.ponto_registro_id));

                if (getIntent().hasExtra(getString(R.string.ponto_registro_detalhes))) {
                    detalhes = (String) getIntent().getSerializableExtra(getString(R.string.ponto_registro_detalhes));

                    if (getIntent().hasExtra(getString(R.string.ponto_registro_string))) {
                        pontoString = (String) getIntent().getSerializableExtra(getString(R.string.ponto_registro_string));

                        presenter = new FotoPontoListaPresenter(this, FotoPontoListaActivity.this);
                        presenter.getFotos(pontoRegistroId);
                    }
                }

            }
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_view_fotos), 0);
        }
    }

    private void setupView() {
        try {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.fotos));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            rvFotos = findViewById(R.id.rv_fotos);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            rvFotos.setLayoutManager(layoutManager);
            fotoPontoAdapter = new FotoPontoAdapter();
            rvFotos.setAdapter(fotoPontoAdapter);

        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_view_fotos), 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
             default:
                 break;
        }
        return true;
    }

    @Override
    public void listarFotos(List<FotoRegistro> fotos) {
        fotoPontoAdapter.setFotos(fotos, screenSize, detalhes, pontoString);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        screenSize = rvFotos.getMeasuredWidth();
        getFoto();
    }
}
