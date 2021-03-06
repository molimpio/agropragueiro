package br.net.olimpiodev.agropragueiro.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.MapaContrato;
import br.net.olimpiodev.agropragueiro.presenter.MapaPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class MapaActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPolylineClickListener,
        GoogleMap.OnPolygonClickListener,
        GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,
        MapaContrato.MapaView {

    private final int CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO = 0;
    private GoogleMap mapa;
    private int contador = 0;
    private int talhaoId;
    private String contorno = "";
    private int qtdeAmostragemByTalhaoId = 0;
    private List<LatLng> coordenadas = new ArrayList<>();
    private MapaContrato.MapaPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        setupView();
    }

    private void setupView() {
        try {
            SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
            fragmentoMapa.getMapAsync(this);


            if (getIntent().hasExtra(getResources().getString(R.string.talhao_id_param))) {
                talhaoId = (int) getIntent().getSerializableExtra(getResources().getString(R.string.talhao_id_param));
            }

            if (getIntent().hasExtra(getResources().getString(R.string.contorno_param))) {
                contorno = (String) getIntent().getSerializableExtra(getResources().getString(R.string.contorno_param));
            }

            if (getIntent().hasExtra(getString(R.string.qtde_amostragem_by_talhao))) {
                qtdeAmostragemByTalhaoId = (int) getIntent().getSerializableExtra(getString(R.string.qtde_amostragem_by_talhao));
            }
        } catch (Exception ex) {
            showMessage(getString(R.string.erro_carregar_mapa), 0);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapa = googleMap;
        presenter = new MapaPresenter(this, MapaActivity.this, mapa);

        mapa.getUiSettings().setZoomControlsEnabled(true);
        mapa.getUiSettings().setMyLocationButtonEnabled(true);

        mapa.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

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
            presenter.exibirContorno(contorno, qtdeAmostragemByTalhaoId);
        }

        presenter.openInstrucoesDialog();
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
        //Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        showMessage(getString(R.string.my_location), 0);
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        coordenadas.add(latLng);
        presenter.drawContorno(contador, latLng);
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

        // somente exibe excluir quando talhao não tem amostragem
        if (qtdeAmostragemByTalhaoId > 0) {
            MenuItem item = menu.findItem(R.id.remover);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.salvar:
                cadastrarContorno();
                return true;
            case R.id.remover:
                presenter.removerContorno(talhaoId);
                mapa.clear();
                coordenadas.clear();
                return true;
            case R.id.layers:
                presenter.opcoesLayer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void cadastrarContorno() {
        presenter.cadastrar(talhaoId, coordenadas);
        Intent resultIntent = new Intent();
        setResult(Utils.COD_CONTORNO_CADASTRADO, resultIntent);
        finish();
    }

    @Override
    public void showMessage(String mensagem, int codigo) {
        Utils.showMessage(getApplicationContext(), mensagem, codigo);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }
}
