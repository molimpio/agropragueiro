package br.net.olimpiodev.agropragueiro.fragment.Lista;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.TalhaoAdapter;
import br.net.olimpiodev.agropragueiro.fragment.Cadastro.TalhaoCadastroFragment;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;

public class TalhaoListaFragment extends Fragment {

    private RecyclerView rvTalhoes;
    private TextView tvListaVazia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talhao_lista, container, false);
        rvTalhoes = view.findViewById(R.id.rv_talhoes);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_talhao);

        FloatingActionButton fabCadastroTalhao = view.findViewById(R.id.fab_cadastro_talhao);
        fabCadastroTalhao.setOnClickListener(view1 -> {
            TalhaoCadastroFragment tcf = new TalhaoCadastroFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.frg_principal, tcf).commit();
        });
        startRecyclerView(view);
        return view;
    }

    private void startRecyclerView(View view) {
        RealmResults<Talhao> talhoes = Realm.getDefaultInstance().where(Talhao.class)
                .equalTo("ativo", true)
                .findAll().sort("nome");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvTalhoes.setLayoutManager(layoutManager);

        TalhaoAdapter talhaoAdapter = new TalhaoAdapter(talhoes);
        rvTalhoes.setAdapter(talhaoAdapter);

        talhaoAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_tc) {
                final Talhao talhao = talhoes.get(position);
                    opcoes(talhao);
            }
        });

        if (Objects.requireNonNull(rvTalhoes.getAdapter()).getItemCount() == 0) {
            tvListaVazia.setVisibility(view.VISIBLE);
        } else {
            tvListaVazia.setVisibility(view.GONE);
        }
    }

    private void opcoes(final Talhao talhao) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_talhao_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 3, (dialog, item) -> {
                        switch (item) {
                            case 1:
                                // ver talhoes
                                break;
                            case 2:
                                // editar
                                break;
                            case 3:
                                // excluir
                                break;
                        }
                    });

            String ok = getResources().getString(R.string.ok);
            String cancelar = getResources().getString(R.string.cancelar);

            builder.setPositiveButton(ok, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Log.i("item", ""+i);
            });

            builder.setNegativeButton(cancelar, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Utils.showMessage(getContext(), "", 3);
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Log.i("erro", ex.getMessage());
        }
    }

}
