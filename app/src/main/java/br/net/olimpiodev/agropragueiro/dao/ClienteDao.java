package br.net.olimpiodev.agropragueiro.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteDao {

    private List<Cliente> clientes;

    public ClienteDao() {
        if (this.clientes == null) {
            this.clientes = new ArrayList<>();
        }
    }

    public Cliente getClienteById(int id) {
        Cliente c = new Cliente();
        c.setId(1);
        c.setNome("AGROPIRA");
        c.setCidade("PIRACICABA");
        c.setUf("SP");
        c.setCategoria("Agricultor");
        return c;
    }

    public void salvar(Cliente c) {
        c.setUsuarioId(1); //TODO: vem do usuario logado
        c.setLastUpdated(Utils.getDataNow());
        c.setUpdatedBy(1); //TODO: vem do usuario logado
        clientes.add(c);
    }

    public List<Cliente> listar() {
        Cliente c = new Cliente();
        c.setNome("AGROPIRA");
        c.setCidade("PIRACICABA");
        c.setUf("SP");
        c.setCategoria("Agricultor");

        Cliente c1 = new Cliente();
        c1.setNome("AGROSANCA");
        c1.setCidade("SAO CARLOS");
        c1.setUf("SP");
        c1.setCategoria("CONSULTOR");

        Cliente c2 = new Cliente();
        c2.setNome("RAIZEN");
        c2.setCidade("PIRACICABA");
        c2.setUf("SP");
        c2.setCategoria("EMPRESA");

        Cliente c3 = new Cliente();
        c3.setNome("USINA NOVA");
        c3.setCidade("PIRACICABA");
        c3.setUf("SP");
        c3.setCategoria("EMPRESA");

        clientes.addAll(Arrays.asList(c, c1, c2, c3));
        return clientes;
    }
}
