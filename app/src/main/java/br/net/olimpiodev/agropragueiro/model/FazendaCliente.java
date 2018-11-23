package br.net.olimpiodev.agropragueiro.model;

public class FazendaCliente {

    private String nomeFazenda;
    private String nomeCliente;
    private String ufFazenda;
    private String cidadeFazenda;

    public String getNomeFazenda() {
        return nomeFazenda;
    }

    public void setNomeFazenda(String nomeFazenda) {
        this.nomeFazenda = nomeFazenda;
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
}
