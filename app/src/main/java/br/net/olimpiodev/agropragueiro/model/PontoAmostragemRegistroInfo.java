package br.net.olimpiodev.agropragueiro.model;

import java.io.Serializable;

public class PontoAmostragemRegistroInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idPontoRegistro;
    private String cliente;
    private String fazenda;
    private String talhao;
    private String amostragem;
    private String praga;
    private int qtde;
    private int dano;
    private int qtdeFotos;

    public int getIdPontoRegistro() {
        return idPontoRegistro;
    }

    public void setIdPontoRegistro(int idPontoRegistro) {
        this.idPontoRegistro = idPontoRegistro;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getFazenda() {
        return fazenda;
    }

    public void setFazenda(String fazenda) {
        this.fazenda = fazenda;
    }

    public String getTalhao() {
        return talhao;
    }

    public void setTalhao(String talhao) {
        this.talhao = talhao;
    }

    public String getAmostragem() {
        return amostragem;
    }

    public void setAmostragem(String amostragem) {
        this.amostragem = amostragem;
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

    public int getDano() {
        return dano;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public int getQtdeFotos() {
        return qtdeFotos;
    }

    public void setQtdeFotos(int qtdeFotos) {
        this.qtdeFotos = qtdeFotos;
    }

    @Override
    public String toString() {
        return "PontoAmostragemRegistroInfo{" +
                "idPontoRegistro=" + idPontoRegistro +
                ", cliente='" + cliente + '\'' +
                ", fazenda='" + fazenda + '\'' +
                ", talhao='" + talhao + '\'' +
                ", amostragem='" + amostragem + '\'' +
                ", praga='" + praga + '\'' +
                ", qtde=" + qtde +
                ", dano=" + dano +
                ", qtdeFotos=" + qtdeFotos +
                '}';
    }
}
