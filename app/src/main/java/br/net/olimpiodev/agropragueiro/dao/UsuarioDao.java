package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Usuario;

@Dao
public interface UsuarioDao {
    @Insert
    void insert(Usuario... usuario);

    @Query("SELECT * FROM usuario limit 1")
    List<Usuario> getUsuario();
}
