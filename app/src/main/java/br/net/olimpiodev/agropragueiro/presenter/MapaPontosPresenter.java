package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
    private List<PontoAmostragem> pontosAmostragem;
    private int amostragemId;
    private String pontos;
    private String contorno;

    public MapaPontosPresenter(MapaPontosContrato.MapaPontosView view, Context context,
                               GoogleMap mapa, int amostragemId, String pontos, String contorno) {
        this.view = view;
        this.context = context;
        this.mapa = mapa;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
        this.pontosAmostragem = new ArrayList<>();
        this.amostragemId = amostragemId;
        this.pontos = pontos;
        this.contorno = contorno;
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void salvarPontos() {
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

    @SuppressLint("StaticFieldLeak")
    @Override
    public void removerPontos() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                db.pontoAmostragemDao().delete(amostragemId);
                return null;
            }
        }.execute();

        mapa.clear();
        pontosAmostragem.clear();
        exibirContorno();
    }

    @Override
    public void adicionarPontos(LatLng ponto, int pontoAmotragemId) {
        try {
            PontoAmostragem pontoAmostragem = new PontoAmostragem();

            if (pontoAmotragemId != 0) pontoAmostragem.setId(pontoAmotragemId);

            pontoAmostragem.setLatitude(ponto.latitude);
            pontoAmostragem.setLongitude(ponto.longitude);
            pontoAmostragem.setAmostragemId(amostragemId);
            pontosAmostragem.add(pontoAmostragem);
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_adicionar_pontos), 0);
        }
    }

    @Override
    public void exibirPontos() {
        try {
            JSONArray jsonArray = new JSONArray(pontos);

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                Double lat = Double.parseDouble(jsonObject.getString("latitude"));
                Double lng = Double.parseDouble(jsonObject.getString("longitude"));
                int pontoAmostragemId = Integer.parseInt(jsonObject.getString("id"));

                LatLng ponto = new LatLng(lat, lng);
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(ponto);
                marcador.snippet(pontoAmostragemId + "");
                mapa.addMarker(marcador);

                adicionarPontos(ponto, pontoAmostragemId);
            }
        } catch (JSONException e) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_pontos), 0);
        }
    }

    @Override
    public void exibirContorno() {
        try {
            LatLng primeiraCoordenada = null;
            JSONArray jsonArray = new JSONArray(contorno);
            PolylineOptions polylineOptions = new PolylineOptions();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                Double lat = Double.parseDouble(jsonObject.getString("latitude"));
                Double lng = Double.parseDouble(jsonObject.getString("longitude"));

                if (i == 0) {
                    primeiraCoordenada = new LatLng(lat, lng);
                }

                LatLng coordenada = new LatLng(lat, lng);
                polylineOptions.color(Color.RED);
                polylineOptions.add(coordenada);

                if ((i + 1) == jsonArray.length()) {
                    polylineOptions.add(primeiraCoordenada);
                }
            }

            if (primeiraCoordenada != null) {
                mapa.addPolyline(polylineOptions);
                mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(primeiraCoordenada, 16));
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_contorno), 0);
        }
    }
}
