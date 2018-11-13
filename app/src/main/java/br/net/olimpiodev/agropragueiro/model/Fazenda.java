package br.net.olimpiodev.agropragueiro.model;

import java.io.Serializable;

public class Fazenda implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String nome;
    private String uf;
    private String cidade;
    private Double areaHa;
    private String observacao;
    private int clienteId;
    private Boolean ativo;
    private Boolean sincronizado;
    private int origem;
    private String lastUpdated;
    private int updatedBy;

    public Fazenda() {
    }

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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public Double getAreaHa() {
        return areaHa;
    }

    public void setAreaHa(Double areaHa) {
        this.areaHa = areaHa;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getClienteId() {
        return clienteId;
    }

    public void setClienteId(int clienteId) {
        this.clienteId = clienteId;
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

    public int getOrigem() {
        return origem;
    }

    public void setOrigem(int origem) {
        this.origem = origem;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(int updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Override
    public String toString() {
        return "Fazenda{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", uf='" + uf + '\'' +
                ", cidade='" + cidade + '\'' +
                ", areaHa=" + areaHa +
                ", observacao='" + observacao + '\'' +
                ", clienteId=" + clienteId +
                ", ativo=" + ativo +
                ", sincronizado=" + sincronizado +
                ", origem=" + origem +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", updatedBy=" + updatedBy +
                '}';
    }
}
