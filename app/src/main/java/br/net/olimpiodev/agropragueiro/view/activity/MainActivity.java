package br.net.olimpiodev.agropragueiro.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.MainContrato;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import br.net.olimpiodev.agropragueiro.presenter.MainPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.activity.Lista.AmostragemListaActivity;
import br.net.olimpiodev.agropragueiro.view.activity.Lista.ClienteListaActivity;
import br.net.olimpiodev.agropragueiro.view.activity.Lista.FazendaListaActivity;
import br.net.olimpiodev.agropragueiro.view.activity.Lista.TalhaoListaActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainContrato.MainView {

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

        MapView map = (MapView) findViewById(R.id.map_ol);

        MainContrato.MainPresenter presenter = new MainPresenter(this, MainActivity.this, map);
        presenter.getUsuario();
        presenter.getClientesFromMapa();
    }

    public void setUsuario(Usuario usuario) {
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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
