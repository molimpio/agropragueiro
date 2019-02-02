package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.model.Talhao;

public interface AmostragemListaContrato {

    interface AmostragemListaView {

        void listarAmostragens(List<AmostragemTalhao> amostragens);

        void openMapa(AmostragemTalhao amostragem, Talhao talhao,
                      List<PontoAmostragem> pontoAmostragens, boolean coletarDados);

        void exibirListaVazia();
    }

    interface AmostragemListaPresenter {

        void getAmostragens(int talhaoId);

        void openMapa(AmostragemTalhao amostragemTalhao, boolean coletarDados);

        void destroyView();
    }
}
