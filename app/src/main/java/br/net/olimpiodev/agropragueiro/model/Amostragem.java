package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "amostragem",
        foreignKeys = @ForeignKey(
                entity = Talhao.class, parentColumns = "id",
                childColumns = "talhao_id", onUpdate = CASCADE
        ),
        indices = {@Index("id"), @Index("talhao_id")}
)
public class Amostragem implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String nome;

    @ColumnInfo(name = "qtde_pontos")
    private int qtdePontos = 0;

    private String data;
    private String observacao;
    private Boolean ativo = true;
    private Boolean sincronizado = false;

    @ColumnInfo(name = "talhao_id")
    private int talhaoId;

    public Amostragem() { }

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

    public int getQtdePontos() {
        return qtdePontos;
    }

    public void setQtdePontos(int qtdePontos) {
        this.qtdePontos = qtdePontos;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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

    public int getTalhaoId() {
        return talhaoId;
    }

    public void setTalhaoId(int talhaoId) {
        this.talhaoId = talhaoId;
    }

    @Override
    public String toString() {
        return nome;
    }
}
