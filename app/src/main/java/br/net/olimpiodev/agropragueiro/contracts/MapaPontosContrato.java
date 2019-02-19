package br.net.olimpiodev.agropragueiro.contracts;

import com.google.android.gms.maps.model.LatLng;

public interface MapaPontosContrato {

    interface MapaPontosView { }

    interface MapaPontosPresenter {

        void salvarPontos();

        void removerPonto(int pontoId);

        void removerPontos();

        void adicionarPontos(LatLng ponto, int pontoAmostragemId);

        void exibirPontos();

        void exibirContorno();

        void opcoesLayer();
    }
}
