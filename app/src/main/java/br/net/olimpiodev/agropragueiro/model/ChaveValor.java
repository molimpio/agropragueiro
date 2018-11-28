package br.net.olimpiodev.agropragueiro.model;

public class ChaveValor {

    private int chave;
    private String valor;

    public int getChave() {
        return chave;
    }

    public void setChave(int chave) {
        this.chave = chave;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor;
    }
}
