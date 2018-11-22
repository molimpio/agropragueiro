package br.net.olimpiodev.agropragueiro.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Talhao;

@Dao
public interface TalhaoDao {
    @Insert
    public void insert(Talhao... talhao);

    @Update
    public void update(Talhao... talhao);

    @Query("SELECT * FROM talhao WHERE ativo = :ativo")
    public List<Talhao> getTalhoes(boolean ativo);

    @Query("SELECT * FROM talhao WHERE fazenda_id = :fazendaId")
    public List<Talhao> getTalhoesByFazendaID(int fazendaId);
    
}
