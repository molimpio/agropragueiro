package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;

public interface TalhaoCadastroContrato {

    interface TalhaoCadastroView {

        void atualizarAdapter(List<TalhaoFazenda> talhoes);
    }

    interface TalhaoCadastroPresenter {

        void exibirView(TalhaoFazenda talhao);
    }
}
