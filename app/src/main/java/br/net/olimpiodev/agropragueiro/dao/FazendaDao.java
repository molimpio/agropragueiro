package br.net.olimpiodev.agropragueiro.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaDao {
    private List<Fazenda> fazendas;

    public FazendaDao() {
        if (this.fazendas == null) {
            this.fazendas = new ArrayList<>();
        }
    }

    public Fazenda getFazendaById(int id) {
        return this.fazendas.get(id);
    }

    public void salvar(Fazenda f) {
        f.setLastUpdated(Utils.getDataNow());
        f.setUpdatedBy(1); //TODO: vem do usuario logado
        fazendas.add(f);
    }

    public List<Fazenda> listar() {
        Fazenda f = new Fazenda();
        f.setNome("ARAGUAIA");
        f.setUf("SP");
        f.setCidade("PIRACICABA");
        f.setAreaHa(20.89);
        f.setClienteId(1);

        Fazenda f2 = new Fazenda();
        f2.setNome("ARAGUAIA 2");
        f2.setUf("SP");
        f2.setCidade("PIRACICABA");
        f2.setAreaHa(20.89);
        f2.setClienteId(1);

        Fazenda f3 = new Fazenda();
        f3.setNome("ARAGUAIA 3");
        f3.setUf("SP");
        f3.setCidade("PIRACICABA");
        f3.setAreaHa(20.89);
        f3.setClienteId(1);

        fazendas.addAll(Arrays.asList(f, f2, f3));
        return fazendas;
    }
}
