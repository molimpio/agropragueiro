package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.PontoAmostragemListaContrato;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistroInfo;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class PontoAmostragemListaPresenter implements PontoAmostragemListaContrato.PontoAmostragemListaPresenter {

    private PontoAmostragemListaContrato.PontoAmostragemListaView view;
    private Context context;
    private AppDatabase db;

    public PontoAmostragemListaPresenter(PontoAmostragemListaContrato.PontoAmostragemListaView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getPontosAmostragens(int amostragemId) {
        try {
            new AsyncTask<Void, Void, List<PontoAmostragemRegistroInfo>>(){
                @Override
                protected List<PontoAmostragemRegistroInfo> doInBackground(Void... voids) {
                    return db.pontoAmostragemRegistroDao().getPontosAmostragemByAmostragemId(amostragemId);
                }

                @Override
                protected void onPostExecute(List<PontoAmostragemRegistroInfo> pontoAmostragens) {
                    if (pontoAmostragens != null) {
                        if (pontoAmostragens.size() == 0) view.exibirListaVazia();
                        else view.listarPontoAmostragens(pontoAmostragens);
                    }
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_abrir_lista_pontos), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
