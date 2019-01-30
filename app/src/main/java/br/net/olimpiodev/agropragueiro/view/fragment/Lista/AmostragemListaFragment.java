package br.net.olimpiodev.agropragueiro.view.fragment.Lista;


import android.app.AlertDialog;
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

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.AmostragemAdapter;
import br.net.olimpiodev.agropragueiro.contracts.AmostragemListaContrato;
import br.net.olimpiodev.agropragueiro.model.Amostragem;
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.presenter.AmostragemListaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.fragment.Cadastro.AmostragemCadastroFragment;

public class AmostragemListaFragment extends Fragment
        implements AmostragemListaContrato.AmostragemListaView {

    private RecyclerView rvAmostragem;
    private TextView tvListaVazia;
    private AmostragemListaContrato.AmostragemListaPresenter presenter;
    private AmostragemAdapter amostragemAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amostragem_lista, container, false);
        getAmostragens();
        setupView(view);
        setupRecyclerView();
        return view;
    }

    private void getAmostragens() {
        try {
            int talhaoId = 0;
            Bundle bundle = this.getArguments();

            if (bundle != null) {
                String keyBunle = getResources().getString(R.string.amostragem_param);
                TalhaoFazenda tf = (TalhaoFazenda) bundle.getSerializable(keyBunle);
                talhaoId = tf.getIdTalhao();
            }

            presenter = new AmostragemListaPresenter(this, getContext());
            presenter.getAmostragens(talhaoId);
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_args_amostragem), 0);
        }
    }

    private void setupView(View view) {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(getString(R.string.amostragens));

            rvAmostragem = view.findViewById(R.id.rv_amostragem);
            tvListaVazia = view.findViewById(R.id.tv_lista_vazia_amostragem);

            FloatingActionButton fabCadastroAmostragem = view.findViewById(R.id.fab_cadastro_amostragem);
            fabCadastroAmostragem.setOnClickListener(view1 -> openCadastro(null));
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_carregar_view_amostragem), 0);
        }
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvAmostragem.setLayoutManager(layoutManager);
        amostragemAdapter = new AmostragemAdapter();
        rvAmostragem.setAdapter(amostragemAdapter);
    }

    private void openCadastro(AmostragemTalhao amostragem) {
        try {
            AmostragemCadastroFragment acf = new AmostragemCadastroFragment();

            if (amostragem != null) {
                Bundle bundle = new Bundle();
                bundle.putSerializable(getResources().getString(R.string.amostragem_param), amostragem);
                acf.setArguments(bundle);
            }
            getFragmentManager().beginTransaction().replace(R.id.frg_principal, acf).commit();
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_cadastro_amostragem), 0);
        }
    }

    private void opcoesDialog(final AmostragemTalhao amostragem) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_amostragem_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 3, (dialog, item) -> {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        openMapa(amostragem, false);
                        break;
                    case 1:
                        openMapa(amostragem, true);
                        break;
                    case 2:
                        dialog.dismiss();
                        openCadastro(amostragem);
                        break;
                    case 3:
                        //excluir
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
            Utils.showMessage(getContext(), getString(R.string.erro_abrir_opcoes_amostragem), 0);
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
    public void openMapa(AmostragemTalhao amostragem, boolean coletarDados) {
        try {

        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_carregar_dados_mapa), 0);
        }
//        Intent mapaPontosIntent = new Intent(getContext(), MapaPontosActivity.class);
//        mapaPontosIntent.putExtra(getResources().getString(R.string.amostragem_param), amostragem.getIdAmostragem());
//
//        new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... voids) {
//                final AppDatabase db = Room.databaseBuilder(getContext(),
//                        AppDatabase.class, AppDatabase.DB_NAME).build();
//                Talhao talhao = db.talhaoDao().getTalhaoById(amostragem.getIdTalhao());
//
//                if (talhao.getContorno() != null) {
//                    mapaPontosIntent.putExtra(getResources().getString(R.string.contorno_param), talhao.getContorno());
//
//                    List<PontoAmostragem> pontoAmostragem = db.pontoAmostragemDao()
//                            .getPontosAmostragemByAmostragemId(amostragem.getIdAmostragem());
//
//                    Gson gson = new Gson();
//                    String pontos = gson.toJson(pontoAmostragem);
//
//                    mapaPontosIntent.putExtra(getResources().getString(R.string.amostragem_pontos), pontos);
//                    mapaPontosIntent.putExtra(getResources().getString(R.string.amostragem_id_param), amostragem.getIdAmostragem());
//                    mapaPontosIntent.putExtra(getString(R.string.coletar_dados), coletarDados);
//
//                    startActivity(mapaPontosIntent);
//                }
//                return null;
//            }
//        }.execute();
    }

    @Override
    public void exibirListaVazia() {
        tvListaVazia.setVisibility(View.VISIBLE);
    }

}
