package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Fazenda;

@Dao
public interface FazendaDao {
    @Insert
    public void insert(Fazenda... fazenda);

    @Update
    public void update(Fazenda... fazenda);

    @Query("SELECT * FROM fazenda WHERE ativo = :ativo")
    public List<Fazenda> getFazendas(boolean ativo);

    @Query("SELECT * FROM fazenda WHERE cliente_id = :clienteId")
    public List<Fazenda> getFazendasByClienteID(int clienteId);
}
