package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.net.olimpiodev.agropragueiro.model.PrevisaoTempo;

@Dao
public interface PrevisaoTempoDao {

    @Insert
    void insert(PrevisaoTempo... previsaoTempo);

    @Query("SELECT * FROM previsao_tempo WHERE cidade = :cidade AND uf = :uf AND data = :data")
    PrevisaoTempo getPrevisaoByParams(String cidade, String uf, String data);
}
