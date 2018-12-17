package br.net.olimpiodev.agropragueiro.service;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.fragment.Cadastro.ClienteCadastroFragment;
import br.net.olimpiodev.agropragueiro.fragment.Lista.FazendaListaFragment;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteService {

    private Context ctx;
    private FragmentManager fm;
    private static Context contextDB;

    public ClienteService(Context context, FragmentManager fragmentManager) {
        ctx = context;
        fm = fragmentManager;
        contextDB = context;
    }

    @SuppressLint("StaticFieldLeak")
    public static class GetClientes extends AsyncTask<Void, Void, List<Cliente>> {
        @Override
        protected List<Cliente> doInBackground(Void ...voids) {
            AppDatabase db = Room.databaseBuilder(contextDB, AppDatabase.class, AppDatabase.DB_NAME).build();
            return db.clienteDao().getClientes(true);
        }
    }

    public void openCadastro(Cliente cliente) {
        ClienteCadastroFragment cdf = new ClienteCadastroFragment();

        if (cliente != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(ctx.getResources().getString(R.string.cliente_param), cliente);
            cdf.setArguments(bundle);
        }

        fm.beginTransaction().replace(R.id.frg_principal, cdf).commit();
    }

    public void openListaFazendas(Cliente cliente) {
        FazendaListaFragment flf = new FazendaListaFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ctx.getResources().getString(R.string.cliente_param), cliente);
        flf.setArguments(bundle);
        fm.beginTransaction().replace(R.id.frg_principal, flf).commit();
    }

    public void opcoes(final Cliente cliente) {
        try {
            final String[] OPCOES = ctx.getResources().getStringArray(R.array.opcoes_cliente_card);
            final String dialogTitle = ctx.getResources().getString(R.string.titulo_opcoes_card);

            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(OPCOES, 3, (dialog, item) -> {
                switch (item) {
                    case 0:
                        dialog.dismiss();
                        openListaFazendas(cliente);
                        break;
                    case 1:
                        dialog.dismiss();
                        openCadastro(cliente);
                        break;
                    case 2:
                        // excluir
                        break;
                }
            });

            String cancelar = ctx.getResources().getString(R.string.cancelar);

            builder.setNegativeButton(cancelar, (dialogInterface, i) -> {
                dialogInterface.dismiss();
                Utils.showMessage(ctx, "", 3);
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.logar(ex.getMessage());
        }
    }
}