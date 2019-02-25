package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
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
import okhttp3.internal.Util;

import static android.content.Context.MODE_PRIVATE;

public class MapaPontosPresenter implements MapaPontosContrato.MapaPontosPresenter {

    private MapaPontosContrato.MapaPontosView view;
    private Context context;
    private GoogleMap mapa;
    private AppDatabase db;
    private List<PontoAmostragem> pontosAmostragem;
    private int amostragemId;
    private String pontos;
    private String contorno;
    private int layerSelecionado = 2;
    private SharedPreferences sharedPreferences;

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
                        if (pontoAmostragem.getId() == 0) {
                            db.pontoAmostragemDao().insert(pontoAmostragem);
                        }
                    }

                    // atualizar qtde_pontos em amostragem
                    db.amostragemDao().updateQtdePontosAmostragem(pontosAmostragem.size(), amostragemId);
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

                // atualizar qtde_pontos em amostragem
                db.amostragemDao().updateQtdePontosAmostragem(pontosAmostragem.size(), amostragemId);
                return null;
            }
        }.execute();

        mapa.clear();
        pontosAmostragem.clear();
        exibirContorno();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void removerPonto(int pontoId) {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    db.pontoAmostragemDao().deletePontoById(pontoId);

                    // atualizar qtde_pontos em amostragem
                    db.amostragemDao().updateQtdePontosAmostragem(pontosAmostragem.size(), amostragemId);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Utils.showMessage(context, context.getString(R.string.ponto_removido), 0);
                }
            }.execute();
        } catch (Exception e) {
            Utils.showMessage(context, context.getString(R.string.erro_remover_ponto), 0);
        }
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
                boolean possuiDados = Boolean.parseBoolean(jsonObject.getString("possuiDados"));

                LatLng ponto = new LatLng(lat, lng);
                MarkerOptions marcador = new MarkerOptions();
                marcador.position(ponto);
                marcador.snippet(pontoAmostragemId + "-semdados");
                marcador.title(""+pontoAmostragemId);

                if (possuiDados) {
                    marcador.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    marcador.snippet(pontoAmostragemId + "-comdados");
                }

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

                double lat = Double.parseDouble(jsonObject.getString("latitude"));
                double lng = Double.parseDouble(jsonObject.getString("longitude"));

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

    @Override
    public void opcoesLayer() {
        try {
            final String[] MAPA_TYPES = context.getResources().getStringArray(R.array.mapa_types);
            final String dialogTitle = context.getResources().getString(R.string.titulo_dialog);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(
                    MAPA_TYPES, layerSelecionado,
                    (dialog, item) -> {
                        switch (item) {
                            case 0:
                                mapa.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                break;
                            case 1:
                                mapa.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                break;
                            case 2:
                                mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                break;
                            case 3:
                                mapa.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                break;
                        }
                        layerSelecionado = item;
                        dialog.dismiss();
                    }
            );

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_opcoes_layer), 0);
        }
    }

    @Override
    public void openInstrucoesDialog() {
        try {
            String sharedPreferencesString = context.getString(R.string.sharedpreferences_file);

            sharedPreferences = context.getSharedPreferences(sharedPreferencesString, MODE_PRIVATE);
            if (sharedPreferences.contains(context.getString(R.string.exibir_instrucao_coletar_dados))) {

                boolean instrucao = sharedPreferences.getBoolean(
                        context.getString(R.string.exibir_instrucao_coletar_dados), true);

                if (instrucao) instrucoesDialog();

            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(context.getString(R.string.exibir_instrucao_coletar_dados), true);
                editor.apply();
                instrucoesDialog();
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_abrir_instrucao_coletar_dados), 0);
        }
    }

    private void instrucoesDialog() {
        try {
            final String dialogTitle = context.getString(R.string.coletar_dados_instrucao);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(dialogTitle);
            builder.setMessage(context.getString(R.string.coletar_dados_texto));

            builder.setPositiveButton(context.getString(R.string.ok), (dialog, which) -> dialog.dismiss());

            builder.setNegativeButton(context.getString(R.string.nao_exibir), (dialog, which) -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(context.getString(R.string.exibir_instrucao_coletar_dados), false);
                editor.apply();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_abrir_instrucao_coletar_dados), 0);
        }
    }
}
