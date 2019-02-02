package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Cliente;

public interface ClienteCadastroContrato {

    interface ClienteCadastroView {

        void atualizarAdapter(List<Cliente> clientes);
    }

    interface ClienteCadastroPresenter {

        void exibirView(Cliente cliente);
    }

}
