package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import br.net.olimpiodev.agropragueiro.model.FotoRegistro;

@Dao
public interface FotoRegistroDao {
    @Insert
    void insert(FotoRegistro... fotoRegistro);

    @Query("SELECT COALESCE(MAX(id),0) + 1 AS id FROM foto_registro")
    int getLastID();
}
