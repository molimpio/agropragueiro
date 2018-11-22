package br.net.olimpiodev.agropragueiro.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import br.net.olimpiodev.agropragueiro.utils.Utils;

@Entity (tableName = "usuario")
public class Usuario {

    @PrimaryKey (autoGenerate = true)
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String data = Utils.getDataNow();

    public Usuario() { }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", senha='" + senha + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
