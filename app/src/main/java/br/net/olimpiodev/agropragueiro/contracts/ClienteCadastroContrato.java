package br.net.olimpiodev.agropragueiro.contracts;

import br.net.olimpiodev.agropragueiro.model.Cliente;

public interface ClienteCadastroContrato {

    interface ClienteCadastroView {

        void cadastrar();

        void showMessage(String mensagem, int codigo);
    }

    interface ClienteCadastroPresenter {

        void cadastrar(Cliente cliente);

        void destroyView();
    }

}
