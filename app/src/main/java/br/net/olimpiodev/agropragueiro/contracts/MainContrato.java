package br.net.olimpiodev.agropragueiro.contracts;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Usuario;

public interface MainContrato {

    interface MainView {

        void setUsuario(Usuario usuario);
    }

    interface MainPresenter {

        void getUsuario();

        void getClientesFromMapa();
    }
}
