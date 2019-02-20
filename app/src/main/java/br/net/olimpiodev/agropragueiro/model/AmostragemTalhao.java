package br.net.olimpiodev.agropragueiro.model;

import java.io.Serializable;

public class AmostragemTalhao implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idAmostragem;
    private String nomeAmostragem;
    private int qtdePontos;
    private String data;
    private int idTalhao;
    private String talhaoNome;
    private String observacaoAmostragem;

    public int getIdAmostragem() {
        return idAmostragem;
    }

    public void setIdAmostragem(int idAmostragem) {
        this.idAmostragem = idAmostragem;
    }

    public String getNomeAmostragem() {
        return nomeAmostragem;
    }

    public void setNomeAmostragem(String nomeAmostragem) {
        this.nomeAmostragem = nomeAmostragem;
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

    public int getIdTalhao() {
        return idTalhao;
    }

    public void setIdTalhao(int idTalhao) {
        this.idTalhao = idTalhao;
    }

    public String getObservacaoAmostragem() {
        return observacaoAmostragem;
    }

    public void setObservacaoAmostragem(String observacaoAmostragem) {
        this.observacaoAmostragem = observacaoAmostragem;
    }

    public String getTalhaoNome() {
        return talhaoNome;
    }

    public void setTalhaoNome(String talhaoNome) {
        this.talhaoNome = talhaoNome;
    }


}
