package br.net.olimpiodev.agropragueiro.model;

import java.io.Serializable;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class Cliente extends RealmObject implements Serializable {
    private static final long serialVersionUID = 1L;

    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    @Required
    private String nome;

    @Required
    private String categoria;

    @Required
    private String uf;

    @Required
    private String cidade;

    private RealmList<Fazenda> fazendas;
    private Usuario usuario;

    private Boolean ativo = true;
    private Boolean sincronizado = false;

    public Cliente() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public RealmList<Fazenda> getFazendas() {
        return fazendas;
    }

    public void setFazendas(RealmList<Fazenda> fazendas) {
        this.fazendas = fazendas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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

    @Override
    public String toString() {
        return nome;
    }
}
