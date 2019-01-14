package br.net.olimpiodev.agropragueiro.fragment.Lista;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.activity.MapaPontosActivity;
import br.net.olimpiodev.agropragueiro.adapter.AmostragemAdapter;
import br.net.olimpiodev.agropragueiro.fragment.Cadastro.AmostragemCadastroFragment;
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class AmostragemListaFragment extends Fragment {

    private AppDatabase db;
    private RecyclerView rvAmostragem;
    private TextView tvListaVazia;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amostragem_lista, container, false);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        rvAmostragem = view.findViewById(R.id.rv_amostragem);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_amostragem);

        FloatingActionButton fabCadastroAmostragem = view.findViewById(R.id.fab_cadastro_amostragem);
        fabCadastroAmostragem.setOnClickListener(view1 -> openCadastro(null));

        bundle = this.getArguments();

        GetAmostragens getAmostragens = new GetAmostragens();
        getAmostragens.execute();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Amostragens");

        return view;
    }

    private void startRecyclerView(List<AmostragemTalhao> amostragens) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvAmostragem.setLayoutManager(layoutManager);

        AmostragemAdapter amostragemAdapter = new AmostragemAdapter(amostragens);
        rvAmostragem.setAdapter(amostragemAdapter);

        amostragemAdapter.setClickListener(((position, view) -> {
            if (view.getId() == R.id.btn_opoes_ac) {
                final AmostragemTalhao amostragemTalhao = amostragens.get(position);
                opcoes(amostragemTalhao);
            }
        }));
    }

    private void openCadastro(AmostragemTalhao amostragem) {
        AmostragemCadastroFragment acf = new AmostragemCadastroFragment();

        if (amostragem != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(getResources().getString(R.string.amostragem_param), amostragem);
            acf.setArguments(bundle);
        }
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.frg_principal, acf).commit();
    }


    @SuppressLint("StaticFieldLeak")
    private void openMapa(AmostragemTalhao amostragem, boolean coletarDados) {
        Intent mapaPontosIntent = new Intent(getContext(), MapaPontosActivity.class);
        mapaPontosIntent.putExtra(getResources().getString(R.string.amostragem_param), amostragem.getIdAmostragem());

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final AppDatabase db = Room.databaseBuilder(getContext(),
                        AppDatabase.class, AppDatabase.DB_NAME).build();
                Talhao talhao = db.talhaoDao().getTalhaoById(amostragem.getIdTalhao());

                if (talhao.getContorno() != null) {
                    mapaPontosIntent.putExtra(getResources().getString(R.string.contorno_param), talhao.getContorno());

                    List<PontoAmostragem> pontoAmostragem = db.pontoAmostragemDao()
                            .getPontosAmostragemByAmostragemId(amostragem.getIdAmostragem());

                    Gson gson = new Gson();
                    String pontos = gson.toJson(pontoAmostragem);

                    mapaPontosIntent.putExtra(getResources().getString(R.string.amostragem_pontos), pontos);
                    mapaPontosIntent.putExtra(getResources().getString(R.string.amostragem_id_param), amostragem.getIdAmostragem());
                    mapaPontosIntent.putExtra(getString(R.string.coletar_dados), coletarDados);

                    startActivity(mapaPontosIntent);
                }
                return null;
            }
        }.execute();
    }

    private void opcoes(final AmostragemTalhao amostragem) {
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

            String cancelar = getResources().getString(R.string.cancelar);

            builder.setNegativeButton(cancelar, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Utils.showMessage(getContext(), "", 3);
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.logar(ex.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetAmostragens extends AsyncTask<Void, Void, List<AmostragemTalhao>> {

        @Override
        protected List<AmostragemTalhao> doInBackground(Void... voids) {
            List<AmostragemTalhao> amostragens = null;
            try {
                if (bundle != null) {
                    String keyBunle = getResources().getString(R.string.amostragem_param);
                    TalhaoFazenda tf = (TalhaoFazenda) bundle.getSerializable(keyBunle);
                    amostragens = db.amostragemDao().getAmostragensByTalhaoId(true, tf.getIdTalhao());
                } else {
                    amostragens = db.amostragemDao().getAmostragensTalhao(true);
                }
            } catch (Exception ex) {
                Utils.logar(ex.getMessage());
            }
            return amostragens;
        }

        @Override
        protected void onPostExecute(List<AmostragemTalhao> amostragens) {
            if (amostragens.size() == 0) {
                tvListaVazia.setVisibility(View.VISIBLE);
            } else {
                tvListaVazia.setVisibility(View.GONE);
                startRecyclerView(amostragens);
            }
        }
    }
}
