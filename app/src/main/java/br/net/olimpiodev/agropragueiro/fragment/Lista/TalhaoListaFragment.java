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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.activity.MapaActivity;
import br.net.olimpiodev.agropragueiro.adapter.TalhaoAdapter;
import br.net.olimpiodev.agropragueiro.fragment.Cadastro.TalhaoCadastroFragment;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class TalhaoListaFragment extends Fragment {

    private AppDatabase db;
    private RecyclerView rvTalhoes;
    private TextView tvListaVazia;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talhao_lista, container, false);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        rvTalhoes = view.findViewById(R.id.rv_talhoes);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_talhao);

        FloatingActionButton fabCadastroTalhao = view.findViewById(R.id.fab_cadastro_talhao);
        fabCadastroTalhao.setOnClickListener(view1 -> openCadastro(null));

        bundle = this.getArguments();

        GetTalhoes getTalhoes = new GetTalhoes();
        getTalhoes.execute();

        return view;
    }

    private void startRecyclerView(List<TalhaoFazenda> talhoes) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTalhoes.setLayoutManager(layoutManager);

        TalhaoAdapter talhaoAdapter = new TalhaoAdapter(talhoes);
        rvTalhoes.setAdapter(talhaoAdapter);

        talhaoAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_tc) {
                final TalhaoFazenda talhaoFazenda = talhoes.get(position);
                    opcoes(talhaoFazenda);
            }
        });
    }

    private void openCadastro(TalhaoFazenda talhaoFazenda) {
        TalhaoCadastroFragment tcf = new TalhaoCadastroFragment();

        if (talhaoFazenda != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(getResources().getString(R.string.talhao_param), talhaoFazenda);
            tcf.setArguments(bundle);
        }

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.frg_principal, tcf).commit();
    }

    private void openMapa(TalhaoFazenda talhaoFazenda) {
        Intent mapaIntent = new Intent(getContext(), MapaActivity.class);
        mapaIntent.putExtra("talhaoId", talhaoFazenda.getIdTalhao());

        if (talhaoFazenda.getContorno() != null) {
            mapaIntent.putExtra("contorno", talhaoFazenda.getContorno());
        }
        startActivity(mapaIntent);
    }

    private void opcoes(final TalhaoFazenda talhaoFazenda) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_talhao_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 3, (dialog, item) -> {
                        switch (item) {
                            case 0:
                                dialog.dismiss();
                                openMapa(talhaoFazenda);
                                break;
                            case 1:
                                // amostragens
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
    private class GetTalhoes extends AsyncTask<Void, Void, List<TalhaoFazenda>> {

        @Override
        protected List<TalhaoFazenda> doInBackground(Void... voids) {
            List<TalhaoFazenda> talhoes = null;

            try {
                if (bundle != null) {
                    String keyBundle = getResources().getString(R.string.fazenda_param);
                    FazendaCliente fc = (FazendaCliente) bundle.getSerializable(keyBundle);
                    talhoes = db.talhaoDao().getTalhoesByFazendaID(true, fc.getIdFazenda());
                } else {
                    talhoes = db.talhaoDao().getTalhoesFazenda(true);
                }
            } catch (Exception ex) {
                Utils.logar(ex.getMessage());
            }
            return talhoes;
        }

        @Override
        protected void onPostExecute(List<TalhaoFazenda> talhaoFazendas) {
            if (talhaoFazendas.size() == 0) {
                tvListaVazia.setVisibility(View.VISIBLE);
            } else {
                tvListaVazia.setVisibility(View.GONE);
                startRecyclerView(talhaoFazendas);
            }
        }
    }

}
