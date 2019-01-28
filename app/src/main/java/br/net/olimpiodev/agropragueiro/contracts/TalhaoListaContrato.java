package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;

public interface TalhaoListaContrato {

    interface TalhaoListaView {

        void listarTalhoes(List<TalhaoFazenda> talhoes);

        void exibirListaVazia();

        void exibirError(String mensagem);
    }

    interface TalhaoListaPresenter {

        void getTalhoes(int fazendaId);

        void destroyView();
    }
}