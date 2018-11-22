package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import br.net.olimpiodev.agropragueiro.utils.Utils;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "foto_registro",
        foreignKeys = @ForeignKey(
                entity = PontoAmostragemRegistro.class, parentColumns = "id",
                childColumns = "ponto_amostragem_registro_id", onUpdate = CASCADE
        )
)
public class FotoRegistro implements Serializable {
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

    public FotoRegistro() { }

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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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

    public int getPontoAmostragemRegistroId() {
        return pontoAmostragemRegistroId;
    }

    public void setPontoAmostragemRegistroId(int pontoAmostragemRegistroId) {
        this.pontoAmostragemRegistroId = pontoAmostragemRegistroId;
    }
}
