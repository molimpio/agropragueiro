package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "fazendas",
        foreignKeys = @ForeignKey(
                entity = Cliente.class, parentColumns = "id",
                childColumns = "cliente_id", onUpdate = CASCADE
        )
)
public class Fazenda implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String uf;
    private String cidade;
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "cliente_id")
    private int clienteId;

    public Fazenda() { }

    @Override
    public String toString() {
        return nome;
    }
}
