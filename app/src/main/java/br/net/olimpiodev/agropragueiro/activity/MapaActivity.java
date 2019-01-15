package br.net.olimpiodev.agropragueiro.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.service.MapaService;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener {

    private final int CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO = 0;
    private GoogleMap mapa;
    private PolygonOptions opcoesPoligonoCoordendas;
    private LatLng ultimaCoordenada;
    private int contador = 0;
    private int talhaoId;
    private String contorno = "";
    private List<LatLng> coordenadas = new ArrayList<>();
    private int mapaTipoSelecionado = 2;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

        SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        fragmentoMapa.getMapAsync(this);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        if (getIntent().hasExtra(getResources().getString(R.string.talhao_id_param))) {
            talhaoId = (int) getIntent().getSerializableExtra(getResources().getString(R.string.talhao_id_param));
        }

        if (getIntent().hasExtra(getResources().getString(R.string.contorno_param))) {
            contorno = (String) getIntent().getSerializableExtra(getResources().getString(R.string.contorno_param));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.getUiSettings().setMyLocationButtonEnabled(true);

        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //LatLng sydney = new LatLng(-22.7347888 , -47.6675419);
        //mapa.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mapa.moveCamera(CameraUpdateFactory.newLatLng(sydney));

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
            mapa.setOnMyLocationButtonClickListener(this);
            mapa.setOnMyLocationClickListener(this);
        }

        mapa.setOnMapClickListener(this);

        if (!contorno.isEmpty()) {
            exibirContornoMapa();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mapa.setMyLocationEnabled(true);
            }
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        //TODO: limpar ultima coordenada...
        coordenadas.add(latLng);

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

        contador++;
    }

    @Override
    public void onPolygonClick(Polygon polygon) {
    }

    @Override
    public void onPolylineClick(Polyline polyline) {
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
                salvarContorno();
                return true;
            case R.id.remover:
                //TODO: verificar pq não está apagando.
                mapa.clear();
                coordenadas.clear();
                return true;
            case R.id.layers:
                opcoesLayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("StaticFieldLeak")
    public void salvarContorno() {
        Gson gson = new Gson();
        String contorno = gson.toJson(coordenadas);
        Double areaHa = SphericalUtil.computeArea(coordenadas) / 10000;
        Double areaRound = Utils.round(areaHa, 2);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Talhao talhao = db.talhaoDao().getTalhaoById(talhaoId);
                talhao.setContorno(contorno);
                talhao.setAreaHa(areaRound);
                db.talhaoDao().update(talhao);
                return null;
            }
        }.execute();

        Utils.showMessage(getApplicationContext(), "", 1);
        finish();
    }

    private void exibirContornoMapa() {
        // TODO: ver como pegar o centro do polygono para centralizar o zoom
        mapa.addPolyline(MapaService.coordenadasStringToList(contorno));
        mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(MapaService.primeiraCoordenada, 16));
    }

    private void opcoesLayer() {
        try {
            final String[] MAPA_TYPES = getResources().getStringArray(R.array.mapa_types);
            final String dialogTitle = getResources().getString(R.string.titulo_dialog);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(dialogTitle);

            builder.setSingleChoiceItems(
                    MAPA_TYPES, mapaTipoSelecionado,
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
                        mapaTipoSelecionado = item;
                        dialog.dismiss();
                    }
            );

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {

        }
    }
}
