package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistroInfo;
import br.net.olimpiodev.agropragueiro.model.Talhao;

public interface PontoAmostragemListaContrato {

    interface PontoAmostragemListaView {

        void listarPontoAmostragens(List<PontoAmostragemRegistroInfo> pontosAmostragens);

        void exibirListaVazia();
    }

    interface PontoAmostragemListaPresenter {

        void getPontosAmostragens(int amostragemId);

        void destroyView();
    }
}
