package br.net.olimpiodev.agropragueiro.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistro;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistroInfo;

@Dao
public interface PontoAmostragemRegistroDao {
    @Insert
    void insert(PontoAmostragemRegistro... pontoAmostragemRegistro);

    @Query("SELECT MAX(id) AS id FROM ponto_amostragem_registro")
    int getIdInsert();

    @Query("SELECT c.nome AS cliente, f.nome AS fazenda, t.nome AS talhao, a.nome AS amostragem, " +
            "pa.id AS idPontoRegistro, par.praga, par.qtde, par.dano_causado AS dano, " +
            "COUNT(fr.id) AS qtdeFotos FROM ponto_amostragem AS pa " +
            "INNER JOIN amostragem AS a ON a.id == pa.amostragem_id " +
            "INNER JOIN talhao AS t ON t.id == a.talhao_id " +
            "INNER JOIN fazenda AS f ON f.id == t.fazenda_id " +
            "INNER JOIN cliente AS c ON c.id == f.cliente_id " +
            "INNER JOIN ponto_amostragem_registro AS par ON par.ponto_amostragem_id == pa.id " +
            "INNER JOIN foto_registro AS fr ON fr.ponto_amostragem_registro_id == par.id " +
            "WHERE a.id = :amostragemId AND pa.ativo = 1 GROUP BY par.id ORDER BY pa.id ASC")
    List<PontoAmostragemRegistroInfo> getPontosAmostragemByAmostragemId(int amostragemId);
}
