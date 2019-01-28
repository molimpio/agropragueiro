package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Talhao;

public interface TalhaoCadastroContrato {

    interface TalhaoCadastroView {

        void startSpinners(List<ChaveValor> clientes);

        void cadastrar();

        void showMessage(String mensagem, int codigo);
    }

    interface TalhaoCadastroPresenter {

        void getFazendas();

        void cadastrar(Talhao talhao);

        void destroyView();
    }
}
