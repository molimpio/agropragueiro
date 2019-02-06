package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;

public interface AmostragemCadastroContrato {

    interface AmostragemCadastroView {

        void atualizarAdapter(List<AmostragemTalhao> amostragens);
    }

    interface AmostragemCadastroPresenter {

        void exibirView(AmostragemTalhao amostragem);
    }
}
