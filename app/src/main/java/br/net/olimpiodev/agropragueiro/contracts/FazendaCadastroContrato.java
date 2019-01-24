package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Fazenda;

public interface FazendaCadastroContrato {

    interface FazendaCadastroView {

        void startSpinners(List<ChaveValor> clientes);

        void cadastrar();

        void getDadosCliente(Cliente cliente);

        void showMessage(String mensagem, int codigo);
    }

    interface FazendaCadastroPresenter {

        void getClientes();

        void cadastrar(Fazenda fazenda);

        void getClienteById(int clienteId);

        void destroyView();
    }
}
