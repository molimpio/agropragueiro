package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "clientes",
        foreignKeys = @ForeignKey(
                entity = Usuario.class, parentColumns = "id",
                childColumns = "usuario_id"
        )
)
public class Cliente implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String uf;
    private String cidade;
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "usuario_id")
    private int usuarioId;

    public Cliente() { }

    @Override
    public String toString() {
        return nome;
    }
}
