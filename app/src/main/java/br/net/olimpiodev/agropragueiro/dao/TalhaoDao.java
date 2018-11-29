package br.net.olimpiodev.agropragueiro.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;

@Dao
public interface TalhaoDao {
    @Insert
    void insert(Talhao... talhao);

    @Update
    void update(Talhao... talhao);

    @Query("SELECT t.id AS idTalhao, t.nome AS nomeTalhao, t.areaHa, f.nome AS nomeFazenda" +
            " FROM talhao AS t INNER JOIN fazenda AS f ON f.id = t.fazenda_id WHERE t.ativo = :ativo")
    List<TalhaoFazenda> getTalhoesFazenda(boolean ativo);

    @Query("SELECT t.id AS idTalhao, t.nome AS nomeTalhao, t.areaHa, f.nome AS nomeFazenda" +
            " FROM talhao AS t INNER JOIN fazenda AS f ON f.id = t.fazenda_id " +
            "WHERE t.ativo = :ativo AND t.fazenda_id = :fazendaId")
    List<TalhaoFazenda> getTalhoesByFazendaID(boolean ativo, int fazendaId);
    
}
