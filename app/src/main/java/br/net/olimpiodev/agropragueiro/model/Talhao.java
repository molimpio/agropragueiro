package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "talhao",
        foreignKeys = @ForeignKey(
                entity = Fazenda.class, parentColumns = "id",
                childColumns = "fazenda_id", onUpdate = CASCADE
        )
)
public class Talhao implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String contorno = "";
    private Double areaHa = 0.00;
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "fazenda_id")
    private int fazendaId;

    public Talhao() { }

    @Override
    public String toString() {
        return nome;
    }
}
