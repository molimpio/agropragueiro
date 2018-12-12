package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;

@Dao
public interface PontoAmostragemDao {
    @Insert
    void insert(PontoAmostragem... pontoAmostragem);

    @Update
    void update(PontoAmostragem... pontoAmostragem);

    @Query("SELECT * FROM ponto_amostragem WHERE amostragem_id = :amostragemId")
    PontoAmostragem getPontosAmostragemByAmostragemId(int amostragemId);
}
