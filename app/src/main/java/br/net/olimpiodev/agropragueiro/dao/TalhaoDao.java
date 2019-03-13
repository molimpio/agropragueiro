package br.net.olimpiodev.agropragueiro.dao;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;

@Dao
public interface TalhaoDao {
    @Insert
    void insert(Talhao... talhao);

    @Update
    void update(Talhao... talhao);

    @Query("SELECT * FROM talhao WHERE id = :id")
    Talhao getTalhaoById(int id);

    @Query("SELECT t.id AS chave, t.nome AS valor FROM talhao AS t WHERE ativo = :ativo")
    List<ChaveValor> getTalhoesDropDown(boolean ativo);

    @Query("SELECT t.id AS idTalhao, t.nome AS nomeTalhao, t.areaHa, t.contorno, f.nome AS nomeFazenda, " +
            "f.id AS idFazenda FROM talhao AS t INNER JOIN fazenda AS f ON f.id = t.fazenda_id " +
            "WHERE t.ativo = :ativo ORDER BY f.nome ASC, t.nome ASC")
    List<TalhaoFazenda> getTalhoesFazenda(boolean ativo);

}
