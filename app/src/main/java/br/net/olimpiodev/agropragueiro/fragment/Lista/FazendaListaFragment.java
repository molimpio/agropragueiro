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
import br.net.olimpiodev.agropragueiro.adapter.FazendaAdapter;
import br.net.olimpiodev.agropragueiro.fragment.Cadastro.FazendaCadastroFragment;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;

public class FazendaListaFragment extends Fragment {

    private RecyclerView rvFazendas;
    private TextView tvListaVazia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fazenda_lista, container, false);
        rvFazendas = view.findViewById(R.id.rv_fazendas);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_fazenda);

        FloatingActionButton fabCadastroFazenda = view.findViewById(R.id.fab_cadastro_fazenda);
        fabCadastroFazenda.setOnClickListener(view1 -> {
            FazendaCadastroFragment fdf = new FazendaCadastroFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.frg_principal, fdf).commit();
        });
        startRecyclerView(view);
        return view;
    }

    private void startRecyclerView(View view) {
        RealmResults<Fazenda> fazendas = Realm.getDefaultInstance().where(Fazenda.class)
                .equalTo("ativo", true)
                .findAll().sort("nome");

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFazendas.setLayoutManager(layoutManager);

        FazendaAdapter fazendaAdapter = new FazendaAdapter(fazendas);
        rvFazendas.setAdapter(fazendaAdapter);

        fazendaAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_fc) {
                final Fazenda fazenda = fazendas.get(position);
                opcoes(fazenda);
            }
        });

        if (Objects.requireNonNull(rvFazendas.getAdapter()).getItemCount() == 0) {
            tvListaVazia.setVisibility(view.VISIBLE);
        } else {
            tvListaVazia.setVisibility(view.GONE);
        }
    }

    private void opcoes(final Fazenda fazenda) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_fazenda_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(
                    OPCOES, 3, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
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
                        }
                    });

            String ok = getResources().getString(R.string.ok);
            String cancelar = getResources().getString(R.string.cancelar);

            builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Log.i("item", ""+i);
                }
            });

            builder.setNegativeButton(cancelar, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    Utils.showMessage(getContext(), "", 3);
                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Log.i("erro", ex.getMessage());
        }
    }

}
