package br.net.olimpiodev.agropragueiro.contracts;

public interface ColetarDadosContrato {

    interface ColetarDadosView {

        void registrarFotos(String fotoName);
    }

    interface ColetarDadosPresenter {

        void exibirView();

        void salvarFotoRegistro(String path);
    }
}
