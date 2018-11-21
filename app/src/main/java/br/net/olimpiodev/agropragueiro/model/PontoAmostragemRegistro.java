package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import br.net.olimpiodev.agropragueiro.utils.Utils;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ponto_amostragem_registro",
        foreignKeys = @ForeignKey(
                entity = PontoAmostragem.class, parentColumns = "id",
                childColumns = "ponto_amostragem_id", onUpdate = CASCADE
        )
)
public class PontoAmostragemRegistro implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double latitude;
    private Double longitude;
    private String praga;
    private int qtde;

    @ColumnInfo(name = "dano_causado")
    private int danoCausado;
    private String data = Utils.getDataNow();
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "ponto_amostragem_id")
    private int pontoAmostragemId;

    public PontoAmostragemRegistro() { }
}
