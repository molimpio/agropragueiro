package br.net.olimpiodev.agropragueiro;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("database.realm")
                .schemaVersion(1)
                .build();
        Realm.setDefaultConfiguration(config);
    }

}