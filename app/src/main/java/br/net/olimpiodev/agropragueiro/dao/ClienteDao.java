package br.net.olimpiodev.agropragueiro.dao;

import java.util.UUID;

import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import io.realm.Realm;
import io.realm.RealmResults;

public class ClienteDao {
    private Realm realm;

    public ClienteDao() { }

    public RealmResults<Cliente> getAll() {
        realm = Realm.getDefaultInstance();
        RealmResults<Cliente> clientes = realm.where(Cliente.class)
                .equalTo("ativo", true).findAll().sort("nome");
        realm.close();
        return clientes;
    }

    public void salvar(Cliente cliente) {
        realm = Realm.getDefaultInstance();
        Usuario usuario = realm.where(Usuario.class).findFirst();

        cliente.setId(UUID.randomUUID().toString());
        cliente.setAtivo(true);
        cliente.setSincronizado(false);
        cliente.setUsuario(usuario);

        realm.executeTransaction(realm -> realm.copyToRealmOrUpdate(cliente));
    }
}
