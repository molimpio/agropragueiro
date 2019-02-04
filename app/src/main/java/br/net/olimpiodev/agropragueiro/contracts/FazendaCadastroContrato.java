package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.FazendaCliente;

public interface FazendaCadastroContrato {

    interface FazendaCadastroView {

        void atualizarAdapter(List<FazendaCliente> fazendas);
    }

    interface FazendaCadastroPresenter {

        void exibirView(FazendaCliente fazenda);
    }
}
