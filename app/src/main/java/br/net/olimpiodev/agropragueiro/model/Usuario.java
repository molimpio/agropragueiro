package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import br.net.olimpiodev.agropragueiro.utils.Utils;

@Entity (tableName = "usuario")
public class Usuario {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String data = Utils.getDataNow();

    public Usuario() { }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
