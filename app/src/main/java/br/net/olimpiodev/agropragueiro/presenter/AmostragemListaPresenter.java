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
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.model.Talhao;
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void openMapa(AmostragemTalhao amostragemTalhao, boolean coletarDados) {
        try {
            new AsyncTask<Void, Void, List<PontoAmostragem>>() {
                @Override
                protected List<PontoAmostragem> doInBackground(Void... voids) {
                    return db.pontoAmostragemDao()
                            .getPontosAmostragemByAmostragemId(amostragemTalhao.getIdAmostragem());
                }

                @Override
                protected void onPostExecute(List<PontoAmostragem> pontoAmostragems) {
                    getContornoAmostragem(amostragemTalhao, coletarDados, pontoAmostragems);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_buscar_amostragem_por_id), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getContornoAmostragem(AmostragemTalhao amostragemTalhao, boolean coletarDados, List<PontoAmostragem> lista) {
        try {
            new AsyncTask<Void, Void, Talhao>() {
                @Override
                protected Talhao doInBackground(Void... voids) {
                    return db.talhaoDao().getTalhaoById(amostragemTalhao.getIdTalhao());
                }

                @Override
                protected void onPostExecute(Talhao talhao) {
                    view.openMapa(amostragemTalhao, talhao, lista, coletarDados);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_buscar_amostragem_por_id), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
