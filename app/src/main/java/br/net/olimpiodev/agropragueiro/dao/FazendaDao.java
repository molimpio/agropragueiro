package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;

@Dao
public interface FazendaDao {
    @Insert
    public void insert(Fazenda... fazenda);

    @Update
    public void update(Fazenda... fazenda);

    @Query("SELECT * FROM fazenda WHERE ativo = :ativo")
    public List<Fazenda> getFazendas(boolean ativo);

    @Query("SELECT * FROM fazenda WHERE cliente_id = :clienteId")
    public List<Fazenda> getFazendasByClienteID(int clienteId);

    @Query("SELECT f.nome AS nomeFazenda, f.uf AS ufFazenda, f.cidade AS cidadeFazenda," +
            " c.nome AS nomeCliente FROM fazenda AS f INNER JOIN cliente AS c ON c.id = f.cliente_id WHERE f.ativo = :ativo")
    public List<FazendaCliente> getFazendasCliente(boolean ativo);
}
