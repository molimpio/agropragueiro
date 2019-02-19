package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;

@Dao
public interface PontoAmostragemDao {
    @Insert
    void insert(PontoAmostragem... pontoAmostragem);

    @Query("SELECT * FROM ponto_amostragem WHERE amostragem_id = :amostragemId")
    List<PontoAmostragem> getPontosAmostragemByAmostragemId(int amostragemId);

    @Query("DELETE FROM ponto_amostragem WHERE amostragem_id = :amostragemId")
    void delete(int amostragemId);

    @Query("DELETE FROM ponto_amostragem WHERE id = :pontoId")
    void deletePontoById(int pontoId);

    @Query("UPDATE ponto_amostragem SET possui_dados = 1 WHERE id = :pontoAmostragemId")
    void setPossuiDadosPontoAmostragem(int pontoAmostragemId);
}
