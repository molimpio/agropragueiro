package br.net.olimpiodev.agropragueiro.model;

import java.io.Serializable;

public class PrevisaoTempoDados implements Serializable {
    private static final long serialVersionUID = 1L;

    private String dia;
    private String max;
    private String min;
    private String condicao;
    private int imagemId;

    public PrevisaoTempoDados() {
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public int getImagemId() {
        return imagemId;
    }

    public void setImagemId(int imagemId) {
        this.imagemId = imagemId;
    }
}
