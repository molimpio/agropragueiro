package br.net.olimpiodev.agropragueiro.model;

import java.io.Serializable;

public class FazendaCliente implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idFazenda;
    private String nomeFazenda;
    private int idCliente;
    private String nomeCliente;
    private String ufFazenda;
    private String cidadeFazenda;

    public int getIdFazenda() {
        return idFazenda;
    }

    public void setIdFazenda(int idFazenda) {
        this.idFazenda = idFazenda;
    }

    public String getNomeFazenda() {
        return nomeFazenda;
    }

    public void setNomeFazenda(String nomeFazenda) {
        this.nomeFazenda = nomeFazenda;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getUfFazenda() {
        return ufFazenda;
    }

    public void setUfFazenda(String ufFazenda) {
        this.ufFazenda = ufFazenda;
    }

    public String getCidadeFazenda() {
        return cidadeFazenda;
    }

    public void setCidadeFazenda(String cidadeFazenda) {
        this.cidadeFazenda = cidadeFazenda;
    }

    @Override
    public String toString() {
        return "FazendaCliente{" +
                "idFazenda=" + idFazenda +
                ", nomeFazenda='" + nomeFazenda + '\'' +
                ", idCliente=" + idCliente +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", ufFazenda='" + ufFazenda + '\'' +
                ", cidadeFazenda='" + cidadeFazenda + '\'' +
                '}';
    }
}
