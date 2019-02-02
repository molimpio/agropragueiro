package br.net.olimpiodev.agropragueiro.view.activity.Lista;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.AmostragemAdapter;
import br.net.olimpiodev.agropragueiro.contracts.AmostragemListaContrato;
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.presenter.AmostragemListaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.activity.MapaPontosActivity;
import br.net.olimpiodev.agropragueiro.view.fragment.Cadastro.AmostragemCadastroFragment;

public class AmostragemListaActivity extends AppCompatActivity
        implements AmostragemListaContrato.AmostragemListaView  {

    private RecyclerView rvAmostragem;
    private TextView tvListaVazia;
    private AmostragemListaContrato.AmostragemListaPresenter presenter;
    private AmostragemAdapter amostragemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amostragem_lista);

        getAmostragens();
        setupView();
        setupRecyclerView();
    }

    private void getAmostragens() {
        try {
            int talhaoId = 0;
//            Bundle bundle = this.getArguments();
//
//            if (bundle != null) {
//                String keyBunle = getResources().getString(R.string.amostragem_param);
//                TalhaoFazenda tf = (TalhaoFazenda) bundle.getSerializable(keyBunle);
//                talhaoId = tf.getIdTalhao();
//            }

            presenter = new AmostragemListaPresenter(this, AmostragemListaActivity.this);
//            presenter = new AmostragemListaPresenter(this, getApplicationContext());
            presenter.getAmostragens(talhaoId);
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_args_amostragem), 0);
        }
    }

    private void setupView() {
        try {
//            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull())
//                    .getSupportActionBar()).setTitle(getString(R.string.amostragens));

            rvAmostragem = findViewById(R.id.rv_amostragem);
            tvListaVazia = findViewById(R.id.tv_lista_vazia_amostragem);

            FloatingActionButton fabCadastroAmostragem = findViewById(R.id.fab_cadastro_amostragem);
            fabCadastroAmostragem.setOnClickListener(view1 -> openCadastro(null));
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_carregar_view_amostragem), 0);
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvAmostragem.setLayoutManager(layoutManager);
        amostragemAdapter = new AmostragemAdapter();
        rvAmostragem.setAdapter(amostragemAdapter);
    }

    private void openCadastro(AmostragemTalhao amostragem) {
        try {
//            AmostragemCadastroFragment acf = new AmostragemCadastroFragment();
//
//            if (amostragem != null) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(getResources().getString(R.string.amostragem_param), amostragem);
//                acf.setArguments(bundle);
//            }
//            getFragmentManager().beginTransaction().replace(R.id.frg_principal, acf).commit();
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_cadastro_amostragem), 0);
        }
    }

    private void openListaPontosAmostragem(AmostragemTalhao amostragem) {
        try {
            Intent pontosAmostragemIntent = new Intent(this, PontoAmostragemListaActivity.class);
            pontosAmostragemIntent.putExtra(getString(R.string.amostragem_id_param), amostragem.getIdAmostragem());
            startActivity(pontosAmostragemIntent);
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_abrir_lista_pontos), 0);
        }
    }

    private void opcoesDialog(final AmostragemTalhao amostragem) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_amostragem_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 5, (dialog, item) -> {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        presenter.openMapa(amostragem, false);
                        break;
                    case 1:
                        presenter.openMapa(amostragem, true);
                        break;
                    case 2:
                        dialog.dismiss();
                        openCadastro(amostragem);
                        break;
                    case 3:
                        //excluir
                        break;
                    case 4:
                        dialog.dismiss();
                        openListaPontosAmostragem(amostragem);
                        break;
                }
            });

            builder.setNegativeButton(getString(R.string.cancelar), (dialogInterface, i) ->
                    dialogInterface.dismiss()
            );

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_abrir_opcoes_amostragem), 0);
        }
    }

    @Override
    public void listarAmostragens(List<AmostragemTalhao> amostragens) {
        amostragemAdapter.setAmostragens(amostragens);
        amostragemAdapter.setClickListener(((position, view) -> {
            if (view.getId() == R.id.btn_opoes_ac) {
                final AmostragemTalhao amostragemTalhao = amostragens.get(position);
                opcoesDialog(amostragemTalhao);
            }
        }));
    }

    @Override
    public void openMapa(AmostragemTalhao amostragem, Talhao talhao,
                         List<PontoAmostragem> pontoAmostragens, boolean coletarDados) {
        try {
            Intent mapaPontosIntent = new Intent(this, MapaPontosActivity.class);
            mapaPontosIntent.putExtra(getResources().getString(R.string.contorno_param), talhao.getContorno());
            mapaPontosIntent.putExtra(getResources().getString(R.string.amostragem_id_param), amostragem.getIdAmostragem());
            mapaPontosIntent.putExtra(getString(R.string.coletar_dados), coletarDados);

            if (!talhao.getContorno().isEmpty()) {
                if (pontoAmostragens.size() > 0) {
                    Gson gson = new Gson();
                    String pontos = gson.toJson(pontoAmostragens);
                    mapaPontosIntent.putExtra(getResources().getString(R.string.amostragem_pontos), pontos);
                }
                startActivity(mapaPontosIntent);
            } else {
                Utils.showMessage(this, getString(R.string.amostragem_sem_contorno), 0);
            }


        } catch (Exception ex) {
            Utils.showMessage(this, getString(R.string.erro_carregar_dados_mapa), 0);
        }
    }

    @Override
    public void exibirListaVazia() {
        tvListaVazia.setVisibility(View.VISIBLE);
    }
}
