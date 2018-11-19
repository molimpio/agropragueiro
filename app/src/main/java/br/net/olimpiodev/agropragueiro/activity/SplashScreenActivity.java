package br.net.olimpiodev.agropragueiro.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import io.realm.Realm;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                showActivity();
            }
        }, 3000);
    }

    private void showActivity() {
        Realm realm = Realm.getDefaultInstance();
        Usuario usuario = realm.where(Usuario.class).findFirst();

        if (usuario != null) {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        } else {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        }
        finish();
    }
}
