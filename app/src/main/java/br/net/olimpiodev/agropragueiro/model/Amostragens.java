package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "amostragens",
        foreignKeys = @ForeignKey(
                entity = Talhao.class, parentColumns = "id",
                childColumns = "talhao_id", onUpdate = CASCADE
        )
)
public class Amostragens implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private int qtdePontos = 0;
    private String safra;
    private String cultura;
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "talhao_id")
    private int talhaoId;

    public Amostragens() { }

    @Override
    public String toString() {
        return nome;
    }
}
