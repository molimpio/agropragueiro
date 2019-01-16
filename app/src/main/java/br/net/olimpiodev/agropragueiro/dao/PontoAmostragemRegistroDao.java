package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistro;

@Dao
public interface PontoAmostragemRegistroDao {
    @Insert
    void insert(PontoAmostragemRegistro... pontoAmostragemRegistro);
}
