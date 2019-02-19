package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.TalhaoCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class TalhaoCadastroPresenter implements TalhaoCadastroContrato.TalhaoCadastroPresenter {

    private TalhaoCadastroContrato.TalhaoCadastroView view;
    private Context context;
    private AppDatabase db;
    private EditText etNomeTalhao;
    private Spinner spFazenda;
    private Talhao talhao;
    private TalhaoFazenda talhaoView;
    private List<ChaveValor> fazendaList;
    private AlertDialog alertDialog;

    public TalhaoCadastroPresenter(TalhaoCadastroContrato.TalhaoCadastroView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
        this.talhao = new Talhao();
        this.talhaoView = new TalhaoFazenda();
    }

    @Override
    public void exibirView(TalhaoFazenda talhaoFazenda) {
        try {
            View dialogView = View.inflate(context, R.layout.dialog_cadastro_talhao, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(context.getString(R.string.cadastrar_talhao));

            etNomeTalhao = dialogView.findViewById(R.id.et_nome_talhao);
            spFazenda = dialogView.findViewById(R.id.sp_fazendas_talhao);
            spFazenda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    talhao.setFazendaId(fazendaList.get(position).getChave());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Button btnCadastrar = dialogView.findViewById(R.id.btn_cadastrar_talhao);
            btnCadastrar.setOnClickListener(view1 -> validarDados());

            talhaoView = talhaoFazenda;
            getFazendas();

            alertDialogBuilder.setView(dialogView);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_view_talhao), 0);
        }
    }

    private void getArgumentos() {
        if (talhaoView != null) {
            talhao.setId(talhaoView.getIdTalhao());
            talhao.setNome(talhaoView.getNomeTalhao());
            talhao.setFazendaId(talhaoView.getIdFazenda());

            if (!talhaoView.getContorno().isEmpty()) {
                talhao.setContorno(talhaoView.getContorno());
                talhao.setAreaHa(talhaoView.getAreaHa());
            }

            etNomeTalhao.setText(talhao.getNome());
            spFazenda.setSelection(Utils.getIndexChaveValor(fazendaList, talhaoView.getNomeFazenda()));
        } else {
            talhao.setId(0);
        }
    }

    private void validarDados() {
        try {
            String nome = etNomeTalhao.getText().toString().trim().toUpperCase();

            if (nome.length() > 0) {
                talhao.setNome(nome);
                cadastrar(talhao);
            } else {
                Utils.showMessage(context, context.getString(R.string.talhao_erro_cadastro), 0);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_cadastrar_talhao), 0);
        }
    }

    private void startSpinners(List<ChaveValor> fazendas) {
        try {
            fazendaList = fazendas;
            ArrayAdapter<ChaveValor> adapterFazendas = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item, fazendas);
            spFazenda.setAdapter(adapterFazendas);

            getArgumentos();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_dados_cadastro_talhao), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getFazendas() {
        try {
            new AsyncTask<Void, Void, List<ChaveValor>>() {
                @Override
                protected List<ChaveValor> doInBackground(Void... voids) {
                    return db.fazendaDao().getFazendasDropDown(true);
                }

                @Override
                protected void onPostExecute(List<ChaveValor> fazendas) {
                    startSpinners(fazendas);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_dados_cadastro_talhao), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrar(Talhao talhao) {
        try {
            new AsyncTask<Void, Void, List<TalhaoFazenda>>() {
                @Override
                protected List<TalhaoFazenda> doInBackground(Void... voids) {
                    if (talhao.getId() == 0) db.talhaoDao().insert(talhao);
                    else db.talhaoDao().update(talhao);
                    return db.talhaoDao().getTalhoesFazenda(true);
                }

                @Override
                protected void onPostExecute(List<TalhaoFazenda> talhoes) {
                    alertDialog.cancel();
                    view.atualizarAdapter(talhoes);
                    Utils.showMessage(context, "", 1);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_cadastrar_talhao), 0);
        }
    }
}
