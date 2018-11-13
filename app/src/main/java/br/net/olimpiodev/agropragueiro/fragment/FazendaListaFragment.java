package br.net.olimpiodev.agropragueiro.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import br.net.olimpiodev.agropragueiro.adapter.FazendaAdapter;
import br.net.olimpiodev.agropragueiro.dao.FazendaDao;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaListaFragment extends Fragment {

    public FazendaListaFragment() { }

    private FazendaAdapter fazendaAdapter;
    private List<Fazenda> fazendas;
    private RecyclerView rvFazendas;
    private TextView tvListaVazia;
    private FloatingActionButton fabCadastroFazenda;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fazenda_lista, container, false);
        rvFazendas = view.findViewById(R.id.rv_fazendas);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_fazenda);
        fabCadastroFazenda = view.findViewById(R.id.fab_cadastro_fazenda);
        fabCadastroFazenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ClienteDialogFragment cdf = new ClienteDialogFragment();
//                cdf.show(getFragmentManager(), "frag");
            }
        });
        startRecyclerView(view);
        return view;
    }

    private void startRecyclerView(View view) {
        FazendaDao fazendaDao = new FazendaDao();
        fazendas = fazendaDao.listar();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFazendas.setLayoutManager(layoutManager);

        fazendaAdapter = new FazendaAdapter(fazendas, getContext());
        rvFazendas.setAdapter(fazendaAdapter);

        fazendaAdapter.setClickListener(new FazendaAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                if (view.getId() == R.id.btn_opoes_fc) {
                    final Fazenda fazenda = fazendas.get(position);
                    opcoes(fazenda);
                }
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
