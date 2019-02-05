package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.FazendaListaContrato;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaListaPresenter implements FazendaListaContrato.FazendaListaPresenter {

    private FazendaListaContrato.FazendaListaView view;
    private Context context;

    public FazendaListaPresenter(FazendaListaContrato.FazendaListaView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getFazendas() {
        try {
            new AsyncTask<Void, Void, List<FazendaCliente>>() {
                @Override
                protected List<FazendaCliente> doInBackground(Void... voids) {
                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
                    return db.fazendaDao().getFazendasCliente(true);
                }

                @Override
                protected void onPostExecute(List<FazendaCliente> fazendaClientes) {
                    if (fazendaClientes != null) {
                        if (fazendaClientes.size() == 0) view.exibirListaVazia();
                        else view.listarFazendas(fazendaClientes);
                    }
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_get_fazendas), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
