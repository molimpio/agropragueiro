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

import org.json.JSONObject;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.api.PrevisaoTempoApi;
import br.net.olimpiodev.agropragueiro.contracts.MainContrato;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.PrevisaoTempo;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainPresenter implements MainContrato.MainPresenter {

    //"https://api.hgbrasil.com/weather?key=6ff81ae8&city_name=Campinas,SP"
    private static final String URL_BASE = "https://api.hgbrasil.com/";
    private static final String KEY = "6ff81ae8";
    private MainContrato.MainView view;
    private Context context;
    private AppDatabase db;
    private MapView mapView;
    private IMapController mapController;
    private PrevisaoTempo previsaoTempo;

    public MainPresenter(MainContrato.MainView view, Context context, MapView mapView) {
        this.view = view;
        this.context = context;
        this.mapView = mapView;
        this.mapController = mapView.getController();
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
        this.previsaoTempo = new PrevisaoTempo();
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

                previsaoTempo.setCidade(c.getCidade());
                previsaoTempo.setUf(c.getUf());
                String data[] = Utils.getDataNow().split(" ");
                previsaoTempo.setData(data[0]);

                InfoWindow infoWindow = new MyInfoWindow(R.layout.popup, mapView, c.getNome());
                startMarker.setInfoWindow(infoWindow);
                mapView.getOverlays().add(startMarker);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_clientes_mapa), 0);
        }
    }

    private class MyInfoWindow extends InfoWindow {

        private String nome;
        private String titleMarker;

        private MyInfoWindow(int layoutResId, MapView mapView, String nomeCliente) {
            super(layoutResId, mapView);
            this.nome = nomeCliente;
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
            btTempo.setOnClickListener(v -> getPrevisaoTempoLocal());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getPrevisaoTempoLocal() {
        try {
            new AsyncTask<Void, Void, PrevisaoTempo>() {
                @Override
                protected PrevisaoTempo doInBackground(Void... voids) {
                    return db.previsaoTempoDao().getPrevisaoByParams(previsaoTempo.getCidade(),
                            previsaoTempo.getUf(), previsaoTempo.getData());
                }

                @Override
                protected void onPostExecute(PrevisaoTempo previsaoTempoLocal) {
                    if (previsaoTempoLocal != null) {
                        previsaoTempo = previsaoTempoLocal;
                        Log.i("prv", previsaoTempo.toString());
                    } else {
                        getPrevisaoTempoApi();
                    }
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_previsao_local), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getPrevisaoTempoApi() {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    String cidadeUf = previsaoTempo.getCidade() + "," + previsaoTempo.getUf();

                    Retrofit retrofit;
                    Retrofit.Builder builder = new Retrofit.Builder();
                    builder.baseUrl(URL_BASE);
                    retrofit = builder.build();
                    PrevisaoTempoApi previsaoTempoApi = retrofit.create(PrevisaoTempoApi.class);

                    Call<ResponseBody> getDadosPrevisaoBody = previsaoTempoApi.getPrevisaoTempo(KEY, cidadeUf);
                    getDadosPrevisaoBody.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            try {
                                if (response.isSuccessful()) {
                                    JSONObject obj = new JSONObject(response.body().string());
                                    previsaoTempo.setPrevisao(obj.getString("results"));
                                    salvarPrevisaoTempo();
                                } else {
                                    Utils.showMessage(context, context.getString(R.string.erro_buscar_previsao_srv), 0);
                                }
                            } catch (Exception ex) {
                                Utils.showMessage(context, context.getString(R.string.erro_previsao_srv), 0);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Utils.showMessage(context, context.getString(R.string.erro_previsao_srv), 0);
                        }
                    });
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_previsao_srv), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void salvarPrevisaoTempo() {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    Log.i("prv", previsaoTempo.toString());
                    db.previsaoTempoDao().insert(previsaoTempo);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {

                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_salvar_previsao_local), 0);
        }
    }
}
