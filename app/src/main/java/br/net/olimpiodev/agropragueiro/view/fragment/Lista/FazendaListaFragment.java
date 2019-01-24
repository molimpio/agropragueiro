package br.net.olimpiodev.agropragueiro.view.fragment.Lista;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
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

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.FazendaAdapter;
import br.net.olimpiodev.agropragueiro.view.fragment.Cadastro.FazendaCadastroFragment;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaListaFragment extends Fragment {

    private AppDatabase db;
    private RecyclerView rvFazendas;
    private TextView tvListaVazia;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fazenda_lista, container, false);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        rvFazendas = view.findViewById(R.id.rv_fazendas);
        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_fazenda);

        FloatingActionButton fabCadastroFazenda = view.findViewById(R.id.fab_cadastro_fazenda);
        fabCadastroFazenda.setOnClickListener(view1 -> openCadastro(null));

        bundle = this.getArguments();
        
        GetFazendas getFazendas = new GetFazendas();
        getFazendas.execute();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Fazendas");
        
        return view;
    }

    private void startRecyclerView(List<FazendaCliente> fazendas) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvFazendas.setLayoutManager(layoutManager);

        FazendaAdapter fazendaAdapter = new FazendaAdapter(fazendas);
        rvFazendas.setAdapter(fazendaAdapter);

        fazendaAdapter.setClickListener((position, view1) -> {
            if (view1.getId() == R.id.btn_opoes_fc) {
                final FazendaCliente fazendaCliente = fazendas.get(position);
                opcoes(fazendaCliente);
            }
        });
    }

    private void openCadastro(FazendaCliente fazenda) {
        FazendaCadastroFragment fdf = new FazendaCadastroFragment();

        if (fazenda != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(getResources().getString(R.string.fazenda_param), fazenda);
            fdf.setArguments(bundle);
        }

        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.frg_principal, fdf).commit();
    }

    private void openListaTalhoes(FazendaCliente fazenda) {
        TalhaoListaFragment tlf = new TalhaoListaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(getResources().getString(R.string.fazenda_param), fazenda);
        tlf.setArguments(bundle);
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.frg_principal, tlf).commit();
    }

    private void opcoes(final FazendaCliente fazenda) {
        try {
            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_fazenda_card);
            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 3, (dialog, item) -> {
                        switch (item) {
                            case 0:
                                dialog.dismiss();
                                openListaTalhoes(fazenda);
                                break;
                            case 1:
                                dialog.dismiss();
                                openCadastro(fazenda);
                                break;
                            case 2:
                                // excluir
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
    private class GetFazendas extends AsyncTask<Void, Void, List<FazendaCliente>> {

        @Override
        protected List<FazendaCliente> doInBackground(Void... voids) {
            List<FazendaCliente> fazendas = null;

            try {
                if (bundle != null) {
                    String keyBundle = getResources().getString(R.string.cliente_param);
                    Cliente c = (Cliente) bundle.getSerializable(keyBundle);
                    fazendas = db.fazendaDao().getFazendasByClienteID(true, c.getId());
                } else {
                    fazendas = db.fazendaDao().getFazendasCliente(true);
                }
            } catch (Exception ex) {
              Utils.logar(ex.getMessage());  
            }
            return fazendas;
        }

        @Override
        protected void onPostExecute(List<FazendaCliente> fazendas) {
            if (fazendas.size() == 0) {
                tvListaVazia.setVisibility(View.VISIBLE);
            } else {
                tvListaVazia.setVisibility(View.GONE);
                startRecyclerView(fazendas);
            }
        }
    }

}
