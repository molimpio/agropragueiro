package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.FazendaCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Fazenda;

public class FazendaCadastroPresenter implements FazendaCadastroContrato.FazendaCadastroPresenter {

    private FazendaCadastroContrato.FazendaCadastroView view;
    private Context context;

    public FazendaCadastroPresenter(FazendaCadastroContrato.FazendaCadastroView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getClientes() {
        try {
            new AsyncTask<Void, Void, List<ChaveValor>>() {
                @Override
                protected List<ChaveValor> doInBackground(Void... voids) {
                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
                    return db.clienteDao().getClientesDropDown(true);
                }

                @Override
                protected void onPostExecute(List<ChaveValor> clientes) {
                    view.startSpinners(clientes);
                }
            }.execute();
        } catch (Exception ex) {
            view.showMessage(context.getString(R.string.erro_carregar_dados_cadastro_fazenda), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void cadastrar(Fazenda fazenda) {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
                    if (fazenda.getId() == 0) db.fazendaDao().insert(fazenda);
                    else db.fazendaDao().update(fazenda);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    view.showMessage("", 1);
                }
            }.execute();
        } catch (Exception ex) {
            view.showMessage(context.getString(R.string.erro_cadastrar_fazenda), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
