package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import br.net.olimpiodev.agropragueiro.utils.Utils;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "ponto_amostragem_registro",
        foreignKeys = @ForeignKey(
                entity = PontoAmostragem.class, parentColumns = "id",
                childColumns = "ponto_amostragem_id", onUpdate = CASCADE
        ),
        indices = {@Index("id"), @Index("ponto_amostragem_id")}
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

    public String getPraga() {
        return praga;
    }

    public void setPraga(String praga) {
        this.praga = praga;
    }

    public int getQtde() {
        return qtde;
    }

    public void setQtde(int qtde) {
        this.qtde = qtde;
    }

    public int getDanoCausado() {
        return danoCausado;
    }

    public void setDanoCausado(int danoCausado) {
        this.danoCausado = danoCausado;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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

    public int getPontoAmostragemId() {
        return pontoAmostragemId;
    }

    public void setPontoAmostragemId(int pontoAmostragemId) {
        this.pontoAmostragemId = pontoAmostragemId;
    }

    @Override
    public String toString() {
        return "PontoAmostragemRegistro{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", praga='" + praga + '\'' +
                ", qtde=" + qtde +
                ", danoCausado=" + danoCausado +
                ", data='" + data + '\'' +
                ", ativo=" + ativo +
                ", sincronizado=" + sincronizado +
                ", pontoAmostragemId=" + pontoAmostragemId +
                '}';
    }
}
