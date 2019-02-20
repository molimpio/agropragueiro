package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.MapaContrato;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.utils.Utils;

import static android.content.Context.MODE_PRIVATE;

public class MapaPresenter implements MapaContrato.MapaPresenter {

    private MapaContrato.MapaView view;
    private Context context;
    private GoogleMap mapa;
    private PolygonOptions opcoesPoligonoCoordendas = new PolygonOptions();
    private LatLng ultimaCoordenada;
    private AppDatabase db;
    private int layerSelecionado = 2;
    private SharedPreferences sharedPreferences;

    public MapaPresenter(MapaContrato.MapaView view, Context context, GoogleMap mapa) {
        this.view = view;
        this.context = context;
        this.mapa = mapa;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void cadastrar(int talhaoId, List<LatLng> coordenadas) {
        try {
            Gson gson = new Gson();
            String contorno = gson.toJson(coordenadas);
            Double areaHa = SphericalUtil.computeArea(coordenadas) / 10000;
            Double areaRound = Utils.round(areaHa, 2);

            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Talhao talhaoDb = db.talhaoDao().getTalhaoById(talhaoId);
                    talhaoDb.setContorno(contorno);
                    talhaoDb.setAreaHa(areaRound);
                    db.talhaoDao().update(talhaoDb);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Utils.showMessage(context, "", 1);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_salvar_contorno), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void removerContorno(int talhaoId) {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Talhao talhaoDb = db.talhaoDao().getTalhaoById(talhaoId);
                    talhaoDb.setContorno("");
                    talhaoDb.setAreaHa(0.0);
                    db.talhaoDao().update(talhaoDb);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    Utils.showMessage(context, context.getString(R.string.contorno_removido), 0);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_salvar_contorno), 0);
        }
    }

    @Override
    public void exibirContorno(String contorno, int qtdeAmostragemByTalhaoId) {
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

                if (qtdeAmostragemByTalhaoId == 0) polylineOptions.color(Color.RED);
                else if (qtdeAmostragemByTalhaoId > 0) polylineOptions.color(Color.BLUE);

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
    public void drawContorno(int contador, LatLng latLng) {
        try {
            if (contador == 0) {
                // ultima coordenada começa como sendo a inicial
                ultimaCoordenada = latLng;

                opcoesPoligonoCoordendas = new PolygonOptions();
                opcoesPoligonoCoordendas.strokeColor(Color.BLUE); // ou fill color
                opcoesPoligonoCoordendas.add(ultimaCoordenada);

            } else {
                mapa.clear();

                // cria o marcador p ultima coordenada
                MarkerOptions opcoesMarcadorUltimaCoordenada = new MarkerOptions();
                opcoesMarcadorUltimaCoordenada.position(ultimaCoordenada);
                mapa.addMarker(opcoesMarcadorUltimaCoordenada);

                // cria marcador p nova coordenada
                MarkerOptions opcoesMarcadorNovaCoordenada = new MarkerOptions();
                opcoesMarcadorNovaCoordenada.position(latLng);
                mapa.addMarker(opcoesMarcadorNovaCoordenada);

                // Polyline
                PolylineOptions opcoesLinhaEntreMarcadores = new PolylineOptions();
                opcoesLinhaEntreMarcadores.add(ultimaCoordenada);
                opcoesLinhaEntreMarcadores.add(latLng);
                opcoesLinhaEntreMarcadores.color(Color.RED); // altera a cor da linha

                mapa.addPolyline(opcoesLinhaEntreMarcadores); // adiciona no mapa*/

                // mudar o zoom do local para nova latlog
                mapa.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                ultimaCoordenada = latLng;

                opcoesPoligonoCoordendas.add(ultimaCoordenada);
                mapa.addPolygon(opcoesPoligonoCoordendas);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_desenhar_contorno), 0);
        }
    }

    @Override
    public void openInstrucoesDialog() {
        try {
            String sharedPreferencesString = context.getString(R.string.sharedpreferences_file);

            sharedPreferences = context.getSharedPreferences(sharedPreferencesString, MODE_PRIVATE);
            if (sharedPreferences.contains(context.getString(R.string.exibir_instrucao_mapa_contorno))) {

                boolean instrucao = sharedPreferences.getBoolean(
                        context.getString(R.string.exibir_instrucao_mapa_contorno), true);

                if (instrucao) instrucoesDialog();

            } else {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(context.getString(R.string.exibir_instrucao_mapa_contorno), true);
                editor.apply();
                instrucoesDialog();
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_abrir_instrucao_mapa_contorno), 0);
        }
    }

    private void instrucoesDialog() {
        try {
            final String dialogTitle = context.getString(R.string.mapa_instrucao_titulo);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(dialogTitle);
            builder.setMessage(context.getString(R.string.mapa_instrucao_texto));

            builder.setPositiveButton(context.getString(R.string.ok), (dialog, which) -> dialog.dismiss());

            builder.setNegativeButton(context.getString(R.string.nao_exibir), (dialog, which) -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(context.getString(R.string.exibir_instrucao_mapa_contorno), false);
                editor.apply();
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_abrir_instrucao_mapa_contorno), 0);
        }
    }

    @Override
    public void destroyView() {
        this.view = null;
    }
}
