package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ponto_amostragem",
        foreignKeys = @ForeignKey(
                entity = Amostragem.class, parentColumns = "id",
                childColumns = "amostragem_id", onUpdate = CASCADE
        )
)
public class PontoAmostragem implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double latitude;
    private Double longitude;
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "amostragem_id")
    private int amostragemId;

    public PontoAmostragem() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public Boolean getSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(Boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    public int getAmostragemId() {
        return amostragemId;
    }

    public void setAmostragemId(int amostragemId) {
        this.amostragemId = amostragemId;
    }

    @Override
    public String toString() {
        return "PontoAmostragem{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", ativo=" + ativo +
                ", sincronizado=" + sincronizado +
                ", amostragemId=" + amostragemId +
                '}';
    }
}
