package br.net.olimpiodev.agropragueiro.dao;

import java.util.UUID;

import br.net.olimpiodev.agropragueiro.model.Fazenda;
import io.realm.Realm;

public class FazendaDao {

    public static void salvar(Fazenda fazenda) {
        fazenda.setId(UUID.randomUUID().toString());
        fazenda.setAtivo(true);
        fazenda.setSincronizado(false);

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(fazenda));
    }
}
