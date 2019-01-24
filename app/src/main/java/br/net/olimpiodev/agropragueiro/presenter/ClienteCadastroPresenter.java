package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.ClienteCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Usuario;

public class ClienteCadastroPresenter implements ClienteCadastroContrato.ClienteCadastroPresenter {

    private ClienteCadastroContrato.ClienteCadastroView view;
    private Context context;

    public ClienteCadastroPresenter(ClienteCadastroContrato.ClienteCadastroView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void cadastrar(Cliente cliente) {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
                    List<Usuario> usuario = db.usuarioDao().getUsuario();
                    cliente.setUsuarioId(usuario.get(0).getId());
                    if (cliente.getId() == 0) db.clienteDao().insert(cliente);
                    else db.clienteDao().update(cliente);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    view.showMessage("", 1);
                }
            }.execute();
        } catch (Exception ex) {
            view.showMessage(context.getString(R.string.erro_cadastrar_cliente), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
