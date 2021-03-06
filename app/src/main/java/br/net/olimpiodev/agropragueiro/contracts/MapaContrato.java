package br.net.olimpiodev.agropragueiro.contracts;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface MapaContrato {

    interface MapaView {

        void cadastrarContorno();

        void showMessage(String mensagem, int codigo);
    }

    interface MapaPresenter {

        void cadastrar(int talhaoId, List<LatLng> coordenadas);

        void removerContorno(int talhaoId);

        void exibirContorno(String contorno, int qtdeAmostragemByTalhaoId);

        void opcoesLayer();

        void drawContorno(int contador,  LatLng latLng);

        void destroyView();

        void openInstrucoesDialog();
    }
}
