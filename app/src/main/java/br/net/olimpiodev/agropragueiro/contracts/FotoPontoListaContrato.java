package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.FotoRegistro;

public interface FotoPontoListaContrato {

    interface FotoPontoListaView {

        void listarFotos(List<FotoRegistro> fotos);
    }

    interface FotoPontoListaPresenter {

        void getFotos(int pontoRegistroId);
    }
}
