package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "talhao",
        foreignKeys = @ForeignKey(
                entity = Fazenda.class, parentColumns = "id",
                childColumns = "fazenda_id", onUpdate = CASCADE
        ),
        indices = {@Index("id"), @Index("fazenda_id")}
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContorno() {
        return contorno;
    }

    public void setContorno(String contorno) {
        this.contorno = contorno;
    }

    public Double getAreaHa() {
        return areaHa;
    }

    public void setAreaHa(Double areaHa) {
        this.areaHa = areaHa;
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

    public int getFazendaId() {
        return fazendaId;
    }

    public void setFazendaId(int fazendaId) {
        this.fazendaId = fazendaId;
    }

    public Talhao() { }

    @Override
    public String toString() {
        return nome;
    }
}
