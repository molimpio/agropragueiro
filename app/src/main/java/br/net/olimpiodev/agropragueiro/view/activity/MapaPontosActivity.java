package br.net.olimpiodev.agropragueiro.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.ColetarDadosContrato;
import br.net.olimpiodev.agropragueiro.contracts.MapaPontosContrato;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.presenter.ColetarDadosPresenter;
import br.net.olimpiodev.agropragueiro.presenter.MapaPontosPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import livroandroid.lib.utils.SDCardUtils;

public class MapaPontosActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnPolylineClickListener,
        MapaPontosContrato.MapaPontosView,
        ColetarDadosContrato.ColetarDadosView {

    private final int CODIGO_REQUISICAO_PERMISSAO_LOCALIZACAO = 0;
    private GoogleMap mapa;
    private String contorno;
    private String pontos;
    private int amostragemId;
    private int pontoAcaoSelecionado = 2;
    private boolean coletarDados;
    private MapaPontosContrato.MapaPontosPresenter presenter;
    private ColetarDadosContrato.ColetarDadosPresenter presenterColetor;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_pontos);

        SupportMapFragment fragmentoMapa = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        fragmentoMapa.getMapAsync(this);

        getParams();
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
        try {
            MarkerOptions marcador = new MarkerOptions();
            marcador.position(latLng);
            mapa.addMarker(marcador);
            presenter.adicionarPontos(latLng, 0);
        } catch (Exception ex) {
            Utils.showMessage(getApplicationContext(), getString(R.string.erro_adicionar_ponto), 0);
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        try {
            if (marker != null) {
                if (!coletarDados) opcoesPonto(marker);
                else exibirViewRegistro(marker);
            }
        } catch (Exception ex) {
            Utils.showMessage(getApplicationContext(), getString(R.string.erro_exibir_opcoes_ponto), 0);
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

        presenter = new MapaPontosPresenter(this, MapaPontosActivity.this, mapa,
                amostragemId, pontos, contorno);

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

        if (contorno != null) presenter.exibirContorno();
        if (pontos != null) presenter.exibirPontos();
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
                presenter.salvarPontos();
                finish();
                return true;
            case R.id.remover:
                presenter.removerPontos();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void getParams() {
        try {
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
        } catch (Exception ex) {
            Utils.showMessage(getApplicationContext(), getString(R.string.erro_args_amostragem), 0);
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

            builder.setPositiveButton(getString(R.string.cancelar), (dialogInterface, i) -> {});

            AlertDialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();
        } catch (Exception ex) {
            Utils.showMessage(getApplicationContext(), getString(R.string.erro_exibir_opcoes_ponto), 0);
        }
    }

    private void moveMarker(Marker marker) {
        marker.remove();
    }

    private void exibirViewRegistro(Marker marker) {
        int pontoAmostragemId = Integer.parseInt(marker.getSnippet());

        presenterColetor = new ColetarDadosPresenter(this, MapaPontosActivity.this,
                marker.getPosition(), pontoAmostragemId);
        presenterColetor.exibirView();
    }

    @Override
    public void registrarFotos(String fotoName) {
        try {
            file = SDCardUtils.getPrivateFile(getApplicationContext(), fotoName,
                    Environment.DIRECTORY_PICTURES);

            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = FileProvider.getUriForFile(getApplicationContext(),
                    getApplicationContext().getPackageName() + ".provider", file);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);

            this.startActivityForResult(intentCamera, 0);

        } catch (Exception ex) {
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.erro_salvar_foto_registro), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 270);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && file != null) {
            String path = file.getAbsolutePath();
            presenterColetor.salvarFotoRegistro(path);
        }
    }
}
