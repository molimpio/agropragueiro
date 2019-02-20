package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.TalhaoListaContrato;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class TalhaoListaPresenter implements TalhaoListaContrato.TalhaoListaPresenter {

    private TalhaoListaContrato.TalhaoListaView view;
    private Context context;
    private AppDatabase db;
    private int qtdeAmostragemByTalhaoId;

    public TalhaoListaPresenter(TalhaoListaContrato.TalhaoListaView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
        this.qtdeAmostragemByTalhaoId = 0;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getTalhoes() {
        try {
            new AsyncTask<Void, Void, List<TalhaoFazenda>>() {
                @Override
                protected List<TalhaoFazenda> doInBackground(Void... voids) {
                    return db.talhaoDao().getTalhoesFazenda(true);
                }

                @Override
                protected void onPostExecute(List<TalhaoFazenda> talhaoFazendas) {
                    if (talhaoFazendas != null) {
                        if (talhaoFazendas.size() == 0) view.exibirListaVazia();
                        else view.listarTalhoes(talhaoFazendas);
                    }
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_buscar_talhoes), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void openMapa(int talhaoId) {
        try {
            new AsyncTask<Void, Void, Talhao>() {
                @Override
                protected Talhao doInBackground(Void... voids) {
                    qtdeAmostragemByTalhaoId = db.amostragemDao().getQtdemostragemByTalhaoId(talhaoId);
                    return db.talhaoDao().getTalhaoById(talhaoId);
                }

                @Override
                protected void onPostExecute(Talhao talhao) {
                    view.openMapa(talhao, qtdeAmostragemByTalhaoId);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_get_talhao_by_id), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }

}
