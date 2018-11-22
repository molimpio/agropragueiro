package br.net.olimpiodev.agropragueiro;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.NonNull;

import br.net.olimpiodev.agropragueiro.dao.ClienteDao;
import br.net.olimpiodev.agropragueiro.dao.FazendaDao;
import br.net.olimpiodev.agropragueiro.dao.TalhaoDao;
import br.net.olimpiodev.agropragueiro.dao.UsuarioDao;
import br.net.olimpiodev.agropragueiro.model.Amostragem;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.model.FotoRegistro;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragem;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistro;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.Usuario;

@Database(entities = {
        Usuario.class,
        Cliente.class,
        Fazenda.class,
        Talhao.class,
        Amostragem.class,
        PontoAmostragem.class,
        PontoAmostragemRegistro.class,
        FotoRegistro.class
}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "agropragueiro";
    public abstract UsuarioDao usuarioDao();
    public abstract ClienteDao clienteDao();
    public abstract FazendaDao fazendaDao();
    public abstract TalhaoDao talhaoDao();

    @NonNull
    @Override
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}