package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.AmostragemListaContrato;
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class AmostragemListaPresenter implements AmostragemListaContrato.AmostragemListaPresenter {

    private AmostragemListaContrato.AmostragemListaView view;
    private Context context;
    private AppDatabase db;

    public AmostragemListaPresenter(AmostragemListaContrato.AmostragemListaView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getAmostragens(int talhaoId) {
        try {
            new AsyncTask<Void, Void, List<AmostragemTalhao>>(){
                @Override
                protected List<AmostragemTalhao> doInBackground(Void... voids) {
                    List<AmostragemTalhao> amostragens;
                    if (talhaoId != 0) {
                        amostragens = db.amostragemDao().getAmostragensByTalhaoId(true, talhaoId);
                    } else {
                        amostragens = db.amostragemDao().getAmostragensTalhao(true);
                    }
                    return amostragens;
                }

                @Override
                protected void onPostExecute(List<AmostragemTalhao> amostragens) {
                    if (amostragens != null) {
                        if (amostragens.size() == 0) view.exibirListaVazia();
                        else view.listarAmostragens(amostragens);
                    }
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_buscar_amostragens), 0);
        }
    }

    @Override
    public void destroyView() {

    }
}
