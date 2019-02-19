package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.Amostragem;
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;

@Dao
public interface AmostragemDao {
    @Insert
    void insert(Amostragem... amostragem);

    @Update
    void update(Amostragem... amostragem);

    @Query("SELECT a.id AS idAmostragem, a.nome AS nomeAmostragem, a.qtde_pontos AS qtdePontos," +
            " a.observacao AS observacaoAmostragem, a.data AS data, t.nome AS talhaoNome, t.id AS idTalhao" +
            " FROM amostragem AS a INNER JOIN talhao AS t ON t.id = a.talhao_id WHERE a.ativo = :ativo " +
            "ORDER BY t.nome ASC, a.id ASC")
    List<AmostragemTalhao> getAmostragensTalhao(boolean ativo);

    @Query("SELECT a.id AS idAmostragem, a.nome AS nomeAmostragem, a.qtde_pontos AS qtdePontos," +
            "  a.observacao AS observacaoAmostragem, a.data AS data, t.nome AS talhaoNome, t.id AS idTalhao" +
            " FROM amostragem AS a INNER JOIN talhao AS t ON t.id = a.talhao_id " +
            "WHERE a.ativo = :ativo AND a.talhao_id = :talhaoId")
    List<AmostragemTalhao> getAmostragensByTalhaoId(boolean ativo, int talhaoId);

    @Query("UPDATE amostragem SET qtde_pontos = :qtdePontos WHERE id = :amostragemId")
    void updateQtdePontosAmostragem(int qtdePontos, int amostragemId);
}
