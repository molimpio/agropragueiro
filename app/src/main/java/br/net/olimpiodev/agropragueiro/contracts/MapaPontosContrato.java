package br.net.olimpiodev.agropragueiro.contracts;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;

public interface MapaPontosContrato {

    interface MapaPontosView { }

    interface MapaPontosPresenter {

        void salvarPontos();

        void removerPontos();

        void adicionarPontos(LatLng ponto, int pontoAmostragemId);

        void exibirPontos();

        void exibirContorno();
    }
}
