package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.TalhaoCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Talhao;

public class TalhaoCadastroPresenter implements TalhaoCadastroContrato.TalhaoCadastroPresenter {

    private TalhaoCadastroContrato.TalhaoCadastroView view;
    private Context context;
    private AppDatabase db;

    public TalhaoCadastroPresenter(TalhaoCadastroContrato.TalhaoCadastroView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getFazendas() {
        try {
            new AsyncTask<Void, Void, List<ChaveValor>>() {
                @Override
                protected List<ChaveValor> doInBackground(Void... voids) {
                    return db.fazendaDao().getFazendasDropDown(true);
                }

                @Override
                protected void onPostExecute(List<ChaveValor> fazendas) {
                    view.startSpinners(fazendas);
                }
            }.execute();
        } catch (Exception ex) {
            view.showMessage(context.getString(R.string.erro_carregar_dados_cadastro_talhao), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void cadastrar(Talhao talhao) {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if (talhao.getId() == 0) db.talhaoDao().insert(talhao);
                    else db.talhaoDao().update(talhao);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    view.showMessage("", 1);
                }
            }.execute();
        } catch (Exception ex) {
            view.showMessage(context.getString(R.string.erro_cadastrar_talhao), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
