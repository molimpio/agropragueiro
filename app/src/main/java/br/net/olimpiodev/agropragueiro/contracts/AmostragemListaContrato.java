package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;

public interface AmostragemListaContrato {

    interface AmostragemListaView {

        void listarAmostragens(List<AmostragemTalhao> amostragens);

        void openMapa(AmostragemTalhao amostragem, boolean coletarDados);

        void exibirListaVazia();
    }

    interface AmostragemListaPresenter {

        void getAmostragens(int talhaoId);

        void destroyView();
    }
}
