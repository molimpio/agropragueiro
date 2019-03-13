package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import br.net.olimpiodev.agropragueiro.utils.Utils;

@Entity(tableName = "previsao_tempo")

public class PrevisaoTempo implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(index = true)
    private int id;
    private String cidade;
    private String uf;
    private String data = Utils.getDataNow();
    private String previsao;

    public PrevisaoTempo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPrevisao() {
        return previsao;
    }

    public void setPrevisao(String previsao) {
        this.previsao = previsao;
    }

    @Override
    public String toString() {
        return "PrevisaoTempo{" +
                "id=" + id +
                ", cidade='" + cidade + '\'' +
                ", uf='" + uf + '\'' +
                ", data='" + data + '\'' +
                ", previsao='" + previsao + '\'' +
                '}';
    }
}
