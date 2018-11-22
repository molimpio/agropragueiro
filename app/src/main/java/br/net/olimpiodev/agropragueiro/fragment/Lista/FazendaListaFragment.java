//package br.net.olimpiodev.agropragueiro.fragment.Lista;
//
//import android.app.AlertDialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import java.util.Objects;
//
//import br.net.olimpiodev.agropragueiro.R;
//import br.net.olimpiodev.agropragueiro.adapter.FazendaAdapter;
//import br.net.olimpiodev.agropragueiro.dao.ClienteDao;
//import br.net.olimpiodev.agropragueiro.fragment.Cadastro.FazendaCadastroFragment;
//import br.net.olimpiodev.agropragueiro.model.Cliente;
//import br.net.olimpiodev.agropragueiro.model.Fazenda;
//import br.net.olimpiodev.agropragueiro.utils.Utils;
//import io.realm.Realm;
//import io.realm.RealmResults;
//
//public class FazendaListaFragment extends Fragment {
//
//    private RecyclerView rvFazendas;
//    private TextView tvListaVazia;
//    private RealmResults<Fazenda> fazendas;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_fazenda_lista, container, false);
//        rvFazendas = view.findViewById(R.id.rv_fazendas);
//        tvListaVazia = view.findViewById(R.id.tv_lista_vazia_fazenda);
//
//        FloatingActionButton fabCadastroFazenda = view.findViewById(R.id.fab_cadastro_fazenda);
//        fabCadastroFazenda.setOnClickListener(view1 -> openCadastro(null));
//        Bundle bundle = this.getArguments();
//        getArgumentos(bundle, view);
//        return view;
//    }
//
//    private void startRecyclerView(View view) {
//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        rvFazendas.setLayoutManager(layoutManager);
//
//        FazendaAdapter fazendaAdapter = new FazendaAdapter(fazendas);
//        rvFazendas.setAdapter(fazendaAdapter);
//
//        fazendaAdapter.setClickListener((position, view1) -> {
//            if (view1.getId() == R.id.btn_opoes_fc) {
//                final Fazenda fazenda = fazendas.get(position);
//                opcoes(fazenda);
//            }
//        });
//
//        if (Objects.requireNonNull(rvFazendas.getAdapter()).getItemCount() == 0) {
//            tvListaVazia.setVisibility(view.VISIBLE);
//        } else {
//            tvListaVazia.setVisibility(view.GONE);
//        }
//    }
//
//    private void openCadastro(Fazenda fazenda) {
//        FazendaCadastroFragment fdf = new FazendaCadastroFragment();
//
//        if (fazenda != null) {
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(getResources().getString(R.string.fazenda_param), fazenda);
//            fdf.setArguments(bundle);
//        }
//
//        FragmentManager fm = getFragmentManager();
//        fm.beginTransaction().replace(R.id.frg_principal, fdf).commit();
//    }
//
//    private void opcoes(final Fazenda fazenda) {
//        try {
//            final String[] OPCOES = getResources().getStringArray(R.array.opcoes_fazenda_card);
//            final String dialogTitle = getResources().getString(R.string.titulo_opcoes_card);
//
//            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//            builder.setTitle(dialogTitle);
//
//            builder.setSingleChoiceItems(OPCOES, 3, (dialog, item) -> {
//                        switch (item) {
//                            case 0:
//                                // ver talhoes
//                                break;
//                            case 1:
//                                dialog.dismiss();
//                                openCadastro(fazenda);
//                                break;
//                            case 2:
//                                // excluir
//                                break;
//                        }
//                    });
//
//            String cancelar = getResources().getString(R.string.cancelar);
//
//            builder.setNegativeButton(cancelar, (dialogInterface, i) -> {
//                dialogInterface.dismiss();
//                Utils.showMessage(getContext(), "", 3);
//            });
//
//            AlertDialog alertDialog = builder.create();
//            alertDialog.setCanceledOnTouchOutside(true);
//            alertDialog.show();
//        } catch (Exception ex) {
//            Utils.logar(ex.getMessage());
//        }
//    }
//
//    private void getArgumentos(Bundle bundle, View view) {
//        try {
//            if (bundle != null) {
//                String keyBundle = getResources().getString(R.string.cliente_param);
//                Cliente cliente = (Cliente) bundle.getSerializable(keyBundle);
//                fazendas = ClienteDao.getFazendasByCliente(cliente);
//            } else {
//                fazendas = Realm.getDefaultInstance().where(Fazenda.class)
//                        .equalTo("ativo", true)
//                        .findAll().sort("nome");
//            }
//
//            startRecyclerView(view);
//        } catch (Exception ex) {
//            Utils.logar(ex.getMessage());
//        }
//    }
//
//}
