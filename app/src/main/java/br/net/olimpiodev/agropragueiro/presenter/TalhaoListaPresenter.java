package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.TalhaoListaContrato;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;

public class TalhaoListaPresenter implements TalhaoListaContrato.TalhaoListaPresenter {

    private TalhaoListaContrato.TalhaoListaView view;
    private Context context;

    public TalhaoListaPresenter(TalhaoListaContrato.TalhaoListaView view, Context context) {
        this.view = view;
        this.context = context;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getTalhoes(int fazendaId) {
        try {
            new AsyncTask<Void, Void, List<TalhaoFazenda>>() {
                @Override
                protected List<TalhaoFazenda> doInBackground(Void... voids) {
                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
                    List<TalhaoFazenda> talhoes;
                    if (fazendaId != 0) {
                        talhoes = db.talhaoDao().getTalhoesByFazendaID(true, fazendaId);
                    } else {
                        talhoes = db.talhaoDao().getTalhoesFazenda(true);
                    }
                    return talhoes;
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
            view.exibirError(context.getString(R.string.erro_buscar_talhoes));
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
