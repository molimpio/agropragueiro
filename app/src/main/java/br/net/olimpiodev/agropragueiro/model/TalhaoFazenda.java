package br.net.olimpiodev.agropragueiro.model;

import java.io.Serializable;

public class TalhaoFazenda implements Serializable {
    private static final long serialVersionUID = 1L;

    private int idTalhao;
    private String nomeTalhao;
    private Double areaHa;
    private String nomeFazenda;
    private String contorno;

    public int getIdTalhao() {
        return idTalhao;
    }

    public void setIdTalhao(int idTalhao) {
        this.idTalhao = idTalhao;
    }

    public String getNomeTalhao() {
        return nomeTalhao;
    }

    public void setNomeTalhao(String nomeTalhao) {
        this.nomeTalhao = nomeTalhao;
    }

    public Double getAreaHa() {
        return areaHa;
    }

    public void setAreaHa(Double areaHa) {
        this.areaHa = areaHa;
    }

    public String getNomeFazenda() {
        return nomeFazenda;
    }

    public void setNomeFazenda(String nomeFazenda) {
        this.nomeFazenda = nomeFazenda;
    }

    public String getContorno() {
        return contorno;
    }

    public void setContorno(String contorno) {
        this.contorno = contorno;
    }

    @Override
    public String toString() {
        return "TalhaoFazenda{" +
                "idTalhao=" + idTalhao +
                ", nomeTalhao='" + nomeTalhao + '\'' +
                ", areaHa=" + areaHa +
                ", nomeFazenda='" + nomeFazenda + '\'' +
                ", contorno='" + contorno + '\'' +
                '}';
    }
}
