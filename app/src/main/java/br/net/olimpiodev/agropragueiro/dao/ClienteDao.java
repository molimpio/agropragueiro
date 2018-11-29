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
    public void insert(Cliente... cliente);

    @Update
    void update(Cliente... cliente);

    @Query("SELECT * FROM cliente WHERE ativo = :ativo")
    List<Cliente> getClientes(boolean ativo);

    @Query("SELECT c.id AS chave, c.nome AS valor FROM cliente AS c WHERE ativo = :ativo")
    List<ChaveValor> getClientesDropDown(boolean ativo);
}
