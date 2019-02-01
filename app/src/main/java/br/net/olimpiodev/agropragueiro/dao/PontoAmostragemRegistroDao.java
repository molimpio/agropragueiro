package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistro;

@Dao
public interface PontoAmostragemRegistroDao {
    @Insert
    void insert(PontoAmostragemRegistro... pontoAmostragemRegistro);

    @Query("SELECT MAX(id) AS id FROM ponto_amostragem_registro")
    int getIdInsert();
}
