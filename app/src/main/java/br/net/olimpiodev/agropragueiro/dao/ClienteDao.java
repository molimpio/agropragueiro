package br.net.olimpiodev.agropragueiro.dao;

import android.content.Context;

import br.net.olimpiodev.agropragueiro.model.Cliente;
import io.realm.Realm;
import io.realm.RealmResults;

public class ClienteDao {
    private Realm realm;

    public ClienteDao(Context context) { }

    public RealmResults<Cliente> getAll() {
        realm = Realm.getDefaultInstance();
        RealmResults<Cliente> clientes;
        clientes = realm.where(Cliente.class).equalTo("ativo", true)
                .findAll().sort("nome");
        realm.close();
        return clientes;
    }
}
