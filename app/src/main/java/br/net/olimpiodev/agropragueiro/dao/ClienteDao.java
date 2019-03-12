package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Cliente;

@Dao
public interface ClienteDao {
    @Insert
    void insert(Cliente... cliente);

    @Update
    void update(Cliente... cliente);

    @Query("SELECT * FROM cliente WHERE ativo = 1 ORDER BY nome ASC")
    List<Cliente> getClientes();

    @Query("SELECT c.id AS chave, c.nome AS valor FROM cliente AS c WHERE ativo = 1")
    List<ChaveValor> getClientesDropDown();

    @Query("SELECT * FROM cliente WHERE id = :clienteId")
    Cliente getClienteById(int clienteId);

    @Query("SELECT id, GROUP_CONCAT(nome, '\r') AS nome, uf, cidade, latitude, longitude, usuario_id FROM cliente WHERE ativo = 1 AND latitude <> 0.0 GROUP BY cidade")
    List<Cliente> getClientesFromMapa();
}
