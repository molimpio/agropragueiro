package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Amostragem;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;

public interface AmostragemCadastroContrato {

    interface AmostragemCadastroView {

        void startSpinners(List<ChaveValor> talhoes);

        void cadastrar();
    }

    interface AmostragemCadastroPresenter {

        void getTalhoes();

        void cadastrar(Amostragem amostragem);

        void destroyView();
    }
}
