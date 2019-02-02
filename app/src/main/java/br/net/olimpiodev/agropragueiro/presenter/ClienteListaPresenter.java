package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.ClienteListaContrato;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteListaPresenter implements ClienteListaContrato.ClienteListaPresenter {

    private ClienteListaContrato.ClienteListaView view;
    private Context context;

    public ClienteListaPresenter(ClienteListaContrato.ClienteListaView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getClientes() {
        try {
            new AsyncTask<Void, Void, List<Cliente>>() {
                @Override
                protected List<Cliente> doInBackground(Void... voids) {
                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
                    return db.clienteDao().getClientes(true);
                }

                @Override
                protected void onPostExecute(List<Cliente> clientes) {
                    if (clientes != null) {
                        if (clientes.size() == 0) view.exibirListaVazia();
                        else view.listarClientes(clientes);
                    }
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_get_clientes), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
