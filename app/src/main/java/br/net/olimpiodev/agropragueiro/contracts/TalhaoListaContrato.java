package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;

public interface TalhaoListaContrato {

    interface TalhaoListaView {

        void listarTalhoes(List<TalhaoFazenda> talhoes);

        void exibirListaVazia();

        void openMapa(Talhao talhao);
    }

    interface TalhaoListaPresenter {

        void getTalhoes();

        void openMapa(int talhaoId);

        void destroyView();
    }
}
