package br.net.olimpiodev.agropragueiro.dao;

import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class ClienteDao {

    public static void salvar(Cliente cliente) {
        Realm realm = Realm.getDefaultInstance();
        Usuario usuario = realm.where(Usuario.class).findFirst();
        cliente.setAtivo(true);
        cliente.setSincronizado(false);
        cliente.setUsuario(usuario);

        realm.executeTransaction(realm1 -> realm1.copyToRealmOrUpdate(cliente));
    }

    public static void adicionarFazenda(Cliente cliente, Fazenda fazenda) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(realm1 -> cliente.getFazendas().add(fazenda));
    }

    public static int getIndex(RealmResults<Cliente> clientes, Cliente cliente) {
        int index = 0;

        for (int i=0; i < clientes.size(); i++) {
            if (clientes.get(i).getNome().equals(cliente.getNome())) {
                index = i;
                break;
            }
        }

        return index;
    }
}
