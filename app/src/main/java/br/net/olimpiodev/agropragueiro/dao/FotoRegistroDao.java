package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import br.net.olimpiodev.agropragueiro.model.FotoRegistro;

@Dao
public interface FotoRegistroDao {
    @Insert
    void insert(FotoRegistro... fotoRegistro);
}
