package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.MainContrato;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class MainPresenter implements MainContrato.MainPresenter {

    private MainContrato.MainView view;
    private Context context;
    private AppDatabase db;
    private MapView mapView;
    private IMapController mapController;

    public MainPresenter(MainContrato.MainView view, Context context, MapView mapView) {
        this.view = view;
        this.context = context;
        this.mapView = mapView;
        this.mapController = mapView.getController();
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getUsuario() {
        try {
            new AsyncTask<Void, Void, List<Usuario>>() {
                @Override
                protected List<Usuario> doInBackground(Void... voids) {
                    return db.usuarioDao().getUsuario();
                }

                @Override
                protected void onPostExecute(List<Usuario> usuarios) {
                    view.setUsuario(usuarios.get(0));

                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_info_usuario), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getClientesFromMapa() {
        try {
            new AsyncTask<Void, Void, List<Cliente>>() {
                @Override
                protected List<Cliente> doInBackground(Void... voids) {
                    return db.clienteDao().getClientesFromMapa();
                }

                @Override
                protected void onPostExecute(List<Cliente> clientes) {
                    startMapa(clientes);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_clientes_mapa), 0);
        }
    }

    private void startMapa(List<Cliente> clientes) {
        try {
            mapView.setTileSource(TileSourceFactory.HIKEBIKEMAP);
            mapView.setMultiTouchControls(true);

            if (clientes.size() > 0) {
                mapController.setZoom(6.5);
                adicionarPontosMapa(clientes);
            } else {
                mapController.setZoom(4.5);
                GeoPoint startPoint = new GeoPoint(-14.35, -53.36);
                mapController.setCenter(startPoint);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_clientes_mapa), 0);
        }
    }

    private void adicionarPontosMapa(List<Cliente> clientes) {
        try {

            for (Cliente c: clientes) {

                GeoPoint startPoint = new GeoPoint(c.getLatitude(), c.getLongitude());
                mapController.setCenter(startPoint);

                Marker startMarker = new Marker(mapView);
                startMarker.setPosition(startPoint);
                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                startMarker.setIcon(context.getResources().getDrawable(R.drawable.pushpin));
                startMarker.setTitle(c.getNome());
                mapView.getOverlays().add(startMarker);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_clientes_mapa), 0);
        }
    }
}
