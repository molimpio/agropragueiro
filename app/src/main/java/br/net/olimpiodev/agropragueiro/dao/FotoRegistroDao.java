package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.FotoRegistro;

@Dao
public interface FotoRegistroDao {
    @Insert
    void insert(FotoRegistro... fotoRegistro);

    @Query("SELECT COALESCE(MAX(id),0) + 1 AS id FROM foto_registro")
    int getLastID();

    @Query("SELECT * FROM foto_registro WHERE ponto_amostragem_registro_id = :ponto_registro_id")
    List<FotoRegistro> getFotosByPontoRegistroId(int ponto_registro_id);
}
