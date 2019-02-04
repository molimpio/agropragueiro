package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;

@Dao
public interface FazendaDao {
    @Insert
    void insert(Fazenda... fazenda);

    @Update
    void update(Fazenda... fazenda);

    @Query("SELECT f.id AS chave, f.nome AS valor FROM fazenda AS f WHERE ativo = :ativo ORDER BY f.nome ASC")
    List<ChaveValor> getFazendasDropDown(boolean ativo);

    @Query("SELECT f.id AS idFazenda, f.nome AS nomeFazenda, f.uf AS ufFazenda, f.cidade AS cidadeFazenda," +
            " c.nome AS nomeCliente, c.id AS idCliente FROM fazenda AS f INNER JOIN cliente AS c ON c.id = f.cliente_id" +
            " WHERE f.ativo = :ativo AND f.cliente_id = :clienteId ORDER BY f.nome ASC")
    List<FazendaCliente> getFazendasByClienteID(boolean ativo, int clienteId);

    @Query("SELECT f.id AS idFazenda, f.nome AS nomeFazenda, f.uf AS ufFazenda, f.cidade AS cidadeFazenda," +
            " c.nome AS nomeCliente, c.id AS idCliente FROM fazenda AS f INNER JOIN cliente AS c ON c.id = f.cliente_id" +
            " WHERE f.ativo = :ativo ORDER BY f.nome ASC")
    List<FazendaCliente> getFazendasCliente(boolean ativo);
}
