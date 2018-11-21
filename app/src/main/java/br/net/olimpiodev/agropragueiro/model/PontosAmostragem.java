package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "pontos_amostragem",
        foreignKeys = @ForeignKey(
                entity = Amostragens.class, parentColumns = "id",
                childColumns = "amostragem_id", onUpdate = CASCADE
        )
)
public class PontosAmostragem implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double latitude;
    private Double longitude;
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "amostragem_id")
    private int amostragemId;

    public PontosAmostragem() { }
}
