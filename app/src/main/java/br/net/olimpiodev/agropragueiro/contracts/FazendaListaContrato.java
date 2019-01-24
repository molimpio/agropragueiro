package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.FazendaCliente;

public interface FazendaListaContrato {

    interface FazendaListaView {

        void listarFazendas(List<FazendaCliente> fazendas);

        void exibirListaVazia();

        void exibirError(String mensagem);
    }

    interface FazendaListaPresenter {

        void getFazendas(int clienteId);

        void destroyView();
    }
}
