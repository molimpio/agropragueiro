package br.net.olimpiodev.agropragueiro.dao;

import java.util.UUID;

import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import io.realm.Realm;

public class ClienteDao {

    public static void salvar(Cliente cliente) {
        Realm realm = Realm.getDefaultInstance();
        Usuario usuario = realm.where(Usuario.class).findFirst();

        if (cliente.getId() == null) cliente.setId(UUID.randomUUID().toString());

        cliente.setAtivo(true);
        cliente.setSincronizado(false);
        cliente.setUsuario(usuario);

        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(cliente));
    }
}
