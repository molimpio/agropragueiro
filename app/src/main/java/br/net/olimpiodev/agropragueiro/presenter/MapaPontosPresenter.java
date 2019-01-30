package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.MapaPontosContrato;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class MapaPontosPresenter implements MapaPontosContrato.MapaPontosPresenter {

    private MapaPontosContrato.MapaPontosView view;
    private Context context;
    private GoogleMap mapa;
    private AppDatabase db;

    public MapaPontosPresenter(MapaPontosContrato.MapaPontosView view, Context context, GoogleMap mapa) {
        this.view = view;
        this.context = context;
        this.mapa = mapa;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void salvarPontos(List<PontoAmostragem> pontosAmostragem) {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    for (PontoAmostragem pontoAmostragem: pontosAmostragem) {
                        db.pontoAmostragemDao().insert(pontoAmostragem);
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Utils.showMessage(context, "", 1);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_cadastrar_pontos), 0);
        }
    }

    @Override
    public void remover() {

    }

    @Override
    public void adicionarPontos() {

    }

    @Override
    public void exibirPontos() {

    }

    @Override
    public void opcoesPonto() {

    }
}
