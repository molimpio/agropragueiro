package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;

public interface MapaPontosContrato {

    interface MapaPontosView {

        void salvarPontos();
    }

    interface MapaPontosPresenter {

        void salvarPontos(List<PontoAmostragem> pontosAmostragem);

        void remover();

        void adicionarPontos();

        void exibirPontos();

        void opcoesPonto();
    }
}
