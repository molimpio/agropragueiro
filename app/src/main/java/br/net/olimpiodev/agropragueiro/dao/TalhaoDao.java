package br.net.olimpiodev.agropragueiro.dao;

import java.util.UUID;

import br.net.olimpiodev.agropragueiro.model.Talhao;
import io.realm.Realm;

public class TalhaoDao {

    public static void salvar(Talhao talhao) {
        talhao.setId(UUID.randomUUID().toString());
        talhao.setAtivo(true);
        talhao.setSincronizado(false);


        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(talhao));
    }
}
