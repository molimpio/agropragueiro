package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.AmostragemCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.Amostragem;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class AmostragemCadastroPresenter implements AmostragemCadastroContrato.AmostragemCadastroPresenter {

    private AmostragemCadastroContrato.AmostragemCadastroView view;
    private Context context;
    private AppDatabase db;

    public AmostragemCadastroPresenter(AmostragemCadastroContrato.AmostragemCadastroView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getTalhoes() {
        new AsyncTask<Void, Void, List<ChaveValor>>() {
            @Override
            protected List<ChaveValor> doInBackground(Void... voids) {
                return db.talhaoDao().getTalhoesDropDown(true);
            }

            @Override
            protected void onPostExecute(List<ChaveValor> talhoes) {
                view.startSpinners(talhoes);
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void cadastrar(Amostragem amostragem) {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if (amostragem.getId() == 0) db.amostragemDao().insert(amostragem);
                    else db.amostragemDao().update(amostragem);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Utils.showMessage(context, "", 1);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_cadastrar_amostragem), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
