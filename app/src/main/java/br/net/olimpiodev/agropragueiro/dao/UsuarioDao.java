package br.net.olimpiodev.agropragueiro.dao;

import java.util.UUID;

import br.net.olimpiodev.agropragueiro.model.Usuario;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import io.realm.Realm;

public class UsuarioDao {
    private Realm realm;

    public UsuarioDao() { }

    public void salvar(Usuario usuario) {
        usuario.setId(UUID.randomUUID().toString());
        usuario.setData(Utils.getDataNow());

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm -> realm.copyToRealmOrUpdate(usuario));
    }
}
