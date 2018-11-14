package br.net.olimpiodev.agropragueiro.model;

import br.net.olimpiodev.agropragueiro.utils.Utils;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Cliente extends RealmObject {
    @PrimaryKey
    private int id;

    @Required
    private String nome;

    @Required
    private String categoria;

    @Required
    private String uf;

    @Required
    private String cidade;

    @Required
    private Integer usuarioId;

    private Boolean ativo;
    private Boolean sincronizado;
    private int origem;
    private String lastUpdated;
    private int updatedBy;

    public Cliente() {
        ativo = true;
        sincronizado = false;
        origem = 1;
        lastUpdated = Utils.getDataNow();
        updatedBy = 1; // TODO: vem do usuario logado
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
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

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
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
        return "Cliente{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", categoria='" + categoria + '\'' +
                ", uf='" + uf + '\'' +
                ", cidade='" + cidade + '\'' +
                ", usuarioId=" + usuarioId +
                ", ativo=" + ativo +
                ", sincronizado=" + sincronizado +
                ", origem=" + origem +
                ", lastUpdated='" + lastUpdated + '\'' +
                ", updatedBy=" + updatedBy +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cliente) {
            Cliente c = (Cliente) obj;
            if (c.getNome().equals(nome) && getId() == id) return true;
        }
        return false;
    }
}
