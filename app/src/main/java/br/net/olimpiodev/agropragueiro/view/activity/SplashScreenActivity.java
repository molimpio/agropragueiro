package br.net.olimpiodev.agropragueiro.view.activity;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.Usuario;

public class SplashScreenActivity extends AppCompatActivity {

    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, AppDatabase.DB_NAME).build();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                getUsuario();
            }
        }, 3000);
    }

    @SuppressLint("StaticFieldLeak")
    private void getUsuario() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                List<Usuario> usuario = db.usuarioDao().getUsuario();
                if (usuario.size() == 0) showActivity(null);
                else showActivity(usuario.get(0));
                return null;
            }
        }.execute();
    }

    private void showActivity(Usuario usuario) {
        if (usuario != null) {
//            Intent mainIntent = new Intent(this, MainActivity.class);
//            startActivity(mainIntent);
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        finish();
    }
}
