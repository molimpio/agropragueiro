package br.net.olimpiodev.agropragueiro.dao;

import java.util.UUID;

import br.net.olimpiodev.agropragueiro.model.Fazenda;
import io.realm.Realm;
import io.realm.RealmResults;

public class FazendaDao {
    private Realm realm;

    public FazendaDao() { }

    public RealmResults<Fazenda> getAll() {
        realm = Realm.getDefaultInstance();
        RealmResults<Fazenda> fazendas = realm.where(Fazenda.class)
                .equalTo("ativo", true)
                .findAll().sort("nome");
        realm.close();
        return fazendas;
    }

    public void salvar(Fazenda fazenda) {
        fazenda.setId(UUID.randomUUID().toString());
        fazenda.setAtivo(true);
        fazenda.setSincronizado(false);

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm -> realm.copyToRealmOrUpdate(fazenda));
    }
}
