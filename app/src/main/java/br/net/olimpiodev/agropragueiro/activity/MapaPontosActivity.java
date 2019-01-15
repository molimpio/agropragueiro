package br.net.olimpiodev.agropragueiro.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.service.MapaService;
import br.net.olimpiodev.agropragueiro.service.amostragem.ColetarDadosService;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class MapaPontosActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPolylineClickListener {

    private final int CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO = 0;
    private GoogleMap mapa;
    private String contorno;
    private String pontos;
    private int amostragemId;
    private List<PontoAmostragem> pontosAmostragem = new ArrayList<>();
    private int pontoAcaoSelecionado = 2;
    private boolean coletarDados;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_pontos);

        SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        fragmentoMapa.getMapAsync(this);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        if (getIntent().hasExtra(getResources().getString(R.string.contorno_param))) {
            contorno = (String) getIntent().getSerializableExtra(getResources().getString(R.string.contorno_param));
        }

        if (getIntent().hasExtra(getResources().getString(R.string.amostragem_id_param))) {
            amostragemId = (Integer) getIntent().getSerializableExtra(getResources().getString(R.string.amostragem_id_param));
        }

        if (getIntent().hasExtra(getResources().getString(R.string.amostragem_pontos))) {
            pontos = (String) getIntent().getSerializableExtra(getResources().getString(R.string.amostragem_pontos));
        }

        if (getIntent().hasExtra(getString(R.string.coletar_dados))) {
            coletarDados = (Boolean) getIntent().getSerializableExtra(getString(R.string.coletar_dados));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            mapa.setMyLocationEnabled(true);
                        } else {
                            // leitura storage
                        }
                    } else {
                        // escrita storage
                    }
                } else {
                    // camera
                }
            } else {
                // location
            }
        } else {
            // codigo requisicao
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        MarkerOptions marcador = new MarkerOptions();
        marcador.position(latLng);
        mapa.addMarker(marcador);

        adicionarPontoAmostragemArray(latLng, 0);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker != null) {
            if (!coletarDados) {
                opcoesPonto(marker);
            } else {
                ColetarDadosService.coletarDadosDialog(this, this);

            }
        }
        return false;
    }

    @Override
    public void onPolylineClick(Polyline polyline) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);

        // verifica se tem a permissao por triangulação e por GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // verifica a versao da api
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO);
            }
        } else {
            mapa.setMyLocationEnabled(true);
        }

        mapa.setOnMapClickListener(this);
        mapa.setOnMarkerClickListener(this);

        exibirContornoMapa();

        if (pontos != null) {
            exibirPontosMapa();
        }
    }

    private void adicionarPontoAmostragemArray(LatLng latLng, int pontoAmotragemId) {
        PontoAmostragem pontoAmostragem = new PontoAmostragem();

        if (pontoAmotragemId != 0) pontoAmostragem.setId(pontoAmotragemId);

        pontoAmostragem.setLatitude(latLng.latitude);
        pontoAmostragem.setLongitude(latLng.longitude);
        pontoAmostragem.setAmostragemId(amostragemId);
        pontosAmostragem.add(pontoAmostragem);
    }

    private void exibirContornoMapa() {
        mapa.addPolyline(MapaService.coordenadasStringToList(contorno));
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(MapaService.primeiraCoordenada, 16));
    }

    private void exibirPontosMapa() {
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
                mapa.addMarker(marcador);

                adicionarPontoAmostragemArray(ponto, pontoAmostragemId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void salvarPontos() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                for (PontoAmostragem pontoAmostragem: pontosAmostragem) {
                    db.pontoAmostragemDao().insert(pontoAmostragem);
                }
                return null;
            }
        }.execute();

        Utils.showMessage(getApplicationContext(), "", 1);
        finish();
    }

    @SuppressLint("StaticFieldLeak")
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
        exibirContornoMapa();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_mapa, menu);

        // usar quando não tem contorno, somente mostra adicionar
        // caso seja adicionar não mostra a opção de excluir
//        if (!getIntent().hasExtra("contato")) {
//            MenuItem item = menu.findItem(R.id.delContato);
//            item.setVisible(false);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvar:
                salvarPontos();
                return true;
            case R.id.remover:
                removerPontos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void opcoesPonto(final Marker marker) {
        try {
            final String[] MAPA_PONTOS = getResources().getStringArray(R.array.mapa_pontos);
            final String dialogTitle = getResources().getString(R.string.titulo_dialog_ponto);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(
                    MAPA_PONTOS, pontoAcaoSelecionado, (dialog, item) -> {
                        switch (item) {
                            case 0:
                                marker.remove();
                                break;
                            case 1:
                                moveMarker(marker);
                                break;
                        }
                        pontoAcaoSelecionado = item;
                        dialog.dismiss();
                    });

            String cancelar = getResources().getString(R.string.cancelar);
            builder.setPositiveButton(cancelar, (dialogInterface, i) -> {
                //Util.toast(getApplicationContext(), getResources().getString(R.string.acao_cancelada));
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Log.i("erro", ex.getMessage());
        }
    }

    private void moveMarker(Marker marker) {
        //Util.toast(getApplicationContext(), getResources().getString(R.string.ponto_mover));
        marker.remove();
    }


}
