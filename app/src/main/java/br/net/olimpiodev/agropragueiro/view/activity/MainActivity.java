package br.net.olimpiodev.agropragueiro.view.activity;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Polyline;

import java.util.ArrayList;
import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.activity.Lista.AmostragemListaActivity;
import br.net.olimpiodev.agropragueiro.view.activity.Lista.ClienteListaActivity;
import br.net.olimpiodev.agropragueiro.view.activity.Lista.FazendaListaActivity;
import br.net.olimpiodev.agropragueiro.view.activity.Lista.TalhaoListaActivity;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private MapView map = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getUsuario();

        map = (MapView) findViewById(R.id.map_ol);
        map.setTileSource(TileSourceFactory.HIKEBIKEMAP);

        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(9.5);

        GeoPoint startPoint = new GeoPoint(-22.72, -47.64);
        mapController.setCenter(startPoint);

        Marker startMarker = new Marker(map);
        startMarker.setPosition(startPoint);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        startMarker.setIcon(getResources().getDrawable(R.drawable.pushpin));
        startMarker.setTitle("Start point");
        map.getOverlays().add(startMarker);

//        GpsMyLocationProvider prov= new GpsMyLocationProvider(getApplicationContext());
//        prov.addLocationSource(LocationManager.NETWORK_PROVIDER);
//        MyLocationNewOverlay locationOverlay = new MyLocationNewOverlay(prov, map);
//        locationOverlay.enableMyLocation();
//        map.getOverlayManager().add(locationOverlay);
//
//        Polyline line = new Polyline();
//        List<GeoPoint> geoPoints = new ArrayList<>();
//        GeoPoint p1 = new GeoPoint(-22.73799858866614, -47.67298959195614);
//        GeoPoint p2 = new GeoPoint(-22.73816309313967, -47.671602219343185);
//        GeoPoint p3 = new GeoPoint(-22.739501076119957, -47.670938707888126);
//        GeoPoint p4 = new GeoPoint(-22.740295139790017, -47.67222985625266);
//
//        geoPoints.add(p1);
//        geoPoints.add(p2);
//        geoPoints.add(p3);
//        geoPoints.add(p4);
//
//        line.setPoints(geoPoints);
//        line.setGeodesic(true);
//        map.getOverlayManager().add(line);
//        mapController.setCenter(startPoint);
    }

//    public static void drawRoute(Context context, MapView map, List<Point> points) {
//        Polyline line = new Polyline();
//
//        line.setSubDescription(Polyline.class.getCanonicalName());
//        line.setWidth(15f);
//        line.setColor(ContextCompat.getColor(context, R.color.colorAccent));
//
//        List<GeoPoint> geoPoints = new ArrayList<>();
//
//        for(Point point : points) {
//
//            geoPoints.add(point.Position);
//        }
//
//        line.setPoints(geoPoints);
//        line.setGeodesic(true);
//        map.getOverlayManager().add(line);
//    }

    @SuppressLint("StaticFieldLeak")
    private void getUsuario() {
        try {
            new AsyncTask<Void, Void, List<Usuario>>() {
                @Override
                protected List<Usuario> doInBackground(Void... voids) {
                    AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                            AppDatabase.class, AppDatabase.DB_NAME).build();

                    return db.usuarioDao().getUsuario();
                }

                @Override
                protected void onPostExecute(List<Usuario> usuarios) {
                    setUsuario(usuarios.get(0));

                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(getApplicationContext(), getString(R.string.erro_info_usuario), 0);
        }
    }

    private void setUsuario(Usuario usuario) {
        try {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView navUserName = (TextView) headerView.findViewById(R.id.tv_username);
            navUserName.setText(usuario.getNome());

            TextView navUserEmail = (TextView) headerView.findViewById(R.id.tv_useremail);
            navUserEmail.setText(usuario.getEmail());

            ImageView ivUserAvatar = (ImageView) headerView.findViewById(R.id.iv_useravatar);
            ivUserAvatar.setImageResource(R.mipmap.ic_launcher_round);
        } catch (Exception ex) {
            Utils.showMessage(getApplicationContext(), getString(R.string.erro_info_usuario), 0);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_clientes) {

            Intent clienteListaIntent = new Intent(this, ClienteListaActivity.class);
            startActivity(clienteListaIntent);

        } else if (id == R.id.nav_fazendas) {

            Intent fazendaListaIntent = new Intent(this, FazendaListaActivity.class);
            startActivity(fazendaListaIntent);

        } else if (id == R.id.nav_talhoes) {

            Intent talhaoListaIntent = new Intent(this, TalhaoListaActivity.class);
            startActivity(talhaoListaIntent);

        } else if (id == R.id.nav_amostragens) {

            Intent amostragemListaIntent = new Intent(this, AmostragemListaActivity.class);
            startActivity(amostragemListaIntent);

        } else if (id == R.id.nav_sobre) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
