package br.net.olimpiodev.agropragueiro.dao;

import java.util.UUID;

import br.net.olimpiodev.agropragueiro.model.Fazenda;
import io.realm.Realm;
import io.realm.RealmResults;

public class FazendaDao {

    public static void salvar(Fazenda fazenda) {
        fazenda.setAtivo(true);
        fazenda.setSincronizado(false);

        if (fazenda.getId() == null) fazenda.setId(UUID.randomUUID().toString());

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(fazenda));
    }

    public static int getIndex(RealmResults<Fazenda> fazendas, Fazenda fazenda) {
        int index = 0;

        for (int i=0; i < fazendas.size(); i++) {
            if (fazendas.get(i).getNome().equals(fazenda.getNome())) {
                index = i;
                break;
            }
        }

        return index;
    }
}
