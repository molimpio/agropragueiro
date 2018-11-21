package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import br.net.olimpiodev.agropragueiro.utils.Utils;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "fotos_registro",
        foreignKeys = @ForeignKey(
                entity = PontosAmostragemRegistro.class, parentColumns = "id",
                childColumns = "ponto_amostragem_registro_id", onUpdate = CASCADE
        )
)
public class FotosRegistro implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;
    private String observacao;
    private String data = Utils.getDataNow();
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "ponto_amostragem_registro_id")
    private int pontoAmostragemRegistroId;

    public FotosRegistro() { }
}
