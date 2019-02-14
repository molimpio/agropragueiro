package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.FotoPontoListaContrato;
import br.net.olimpiodev.agropragueiro.model.FotoRegistro;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FotoPontoListaPresenter implements FotoPontoListaContrato.FotoPontoListaPresenter {

    private FotoPontoListaContrato.FotoPontoListaView view;
    private Context context;
    private AppDatabase db;

    public FotoPontoListaPresenter(FotoPontoListaContrato.FotoPontoListaView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getFotos(int pontoRegistroId) {
        try {
            new AsyncTask<Void, Void, List<FotoRegistro>>() {
                @Override
                protected List<FotoRegistro> doInBackground(Void... voids) {
                    return db.fotoRegistroDao().getFotosByPontoRegistroId(pontoRegistroId);
                }

                @Override
                protected void onPostExecute(List<FotoRegistro> fotoRegistros) {
                    Utils.logar(fotoRegistros.toString());
                    view.listarFotos(fotoRegistros);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_buscar_fotos), 0);
        }
    }
}
