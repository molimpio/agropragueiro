package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Cliente;

public interface ClienteListaContrato {

    interface ClienteListaView {

        void listarClientes(List<Cliente> clientes);

        void exibirListaVazia();
    }

    interface ClienteListaPresenter {

        void getClientes();

        void destroyView();
    }
}
