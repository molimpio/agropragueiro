package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Cliente;

@Dao
public interface ClienteDao {
    @Insert
    public void insert(Cliente... cliente);

    @Update
    public void update(Cliente... cliente);

    @Query("SELECT * FROM cliente WHERE ativo = :ativo")
    public List<Cliente> getClientes(boolean ativo);

    @Query("SELECT * FROM cliente WHERE id = :clienteId")
    public Cliente getClienteById(int clienteId);
}
