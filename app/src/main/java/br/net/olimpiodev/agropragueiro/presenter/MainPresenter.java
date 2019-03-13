package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

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

                InfoWindow infoWindow = new MyInfoWindow(R.layout.popup, mapView, c.getNome(), c.getCidade());
                startMarker.setInfoWindow(infoWindow);
                mapView.getOverlays().add(startMarker);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_clientes_mapa), 0);
        }
    }

    private class MyInfoWindow extends InfoWindow {

        private String nome;
        private String cidade;
        private String titleMarker;

        private MyInfoWindow(int layoutResId, MapView mapView, String nomeCliente, String cidade) {
            super(layoutResId, mapView);
            this.nome = nomeCliente;
            this.cidade = cidade;
            this.titleMarker = "";
        }

        public void onClose() {

        }

        public void onOpen(Object arg0) {
            Button btTempo = (Button) mView.findViewById(R.id.bt_tooltip);
            TextView txtTitle = (TextView) mView.findViewById(R.id.tv_tooltip);
            LinearLayout popup = mView.findViewById(R.id.tooltip_layout);

            Marker marker = (Marker) arg0;

            if (titleMarker.isEmpty()) {
                titleMarker = marker.getTitle();
                popup.setVisibility(View.VISIBLE);
            } else {
                if (titleMarker.equals(marker.getTitle())) {
                    popup.setVisibility(View.INVISIBLE);
                    titleMarker = "";
                }
            }

            txtTitle.setText(nome);
            btTempo.setOnClickListener(v -> {
                Log.i("marker", "clicou" + cidade);
            });
        }
    }
}
