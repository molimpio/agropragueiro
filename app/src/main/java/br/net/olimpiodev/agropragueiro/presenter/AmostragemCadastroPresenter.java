package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.AmostragemCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.Amostragem;
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class AmostragemCadastroPresenter implements AmostragemCadastroContrato.AmostragemCadastroPresenter {

    private AmostragemCadastroContrato.AmostragemCadastroView view;
    private Context context;
    private AppDatabase db;
    private EditText etNomeAmostragem, etDataAmostragem, etObservacaoAmostragem;
    private Spinner spTalhao;
    private Amostragem amostragem;
    private AmostragemTalhao amostragemView;
    private List<ChaveValor> talhaoList;
    private int year, month, day = 0;
    private AlertDialog alertDialog;

    public AmostragemCadastroPresenter(AmostragemCadastroContrato.AmostragemCadastroView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
        this.amostragem = new Amostragem();
        this.amostragemView = new AmostragemTalhao();
    }

    public void exibirView(AmostragemTalhao amostragemTalhao) {
        try {
            View dialogView = View.inflate(context, R.layout.dialog_cadastro_amostragem, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(context.getString(R.string.cadastrar_amostragem));

            etNomeAmostragem = dialogView.findViewById(R.id.et_nome_amostragem);
            etNomeAmostragem.requestFocus();

            etDataAmostragem = dialogView.findViewById(R.id.et_data_amostragem);
            etDataAmostragem.setFocusable(false);

            etDataAmostragem.setOnClickListener(view12 ->
                    new DatePickerDialog(context, date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show());

            etObservacaoAmostragem = dialogView.findViewById(R.id.et_observacao_amostragem);

            spTalhao = dialogView.findViewById(R.id.sp_talhoes_amostragem);
            spTalhao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    amostragem.setTalhaoId(talhaoList.get(position).getChave());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Button btnCadastrar = dialogView.findViewById(R.id.btn_cadastrar_amostragem);
            btnCadastrar.setOnClickListener(view1 -> validarDados());

            amostragemView = amostragemTalhao;
            getTalhoes();

            if (amostragemTalhao.getQtdePontos() == 0) {
                spTalhao.setEnabled(true);
                spTalhao.setClickable(true);
            } else if (amostragemTalhao.getQtdePontos() > 0) {
                spTalhao.setEnabled(false);
                spTalhao.setClickable(false);
            }

            alertDialogBuilder.setView(dialogView);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_view_amostragem), 0);
        }
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        etDataAmostragem.setText(sdf.format(myCalendar.getTime()));
    }

    private void getArgumentos() {
        if (amostragemView != null) {
            amostragem.setId(amostragemView.getIdAmostragem());
            amostragem.setNome(amostragemView.getNomeAmostragem());
            amostragem.setObservacao(amostragemView.getObservacaoAmostragem());
            amostragem.setTalhaoId(amostragemView.getIdTalhao());
            amostragem.setData(amostragemView.getData());
            amostragem.setQtdePontos(amostragemView.getQtdePontos());

            etNomeAmostragem.setText(amostragem.getNome());
            etObservacaoAmostragem.setText(amostragem.getObservacao());
            etDataAmostragem.setText(amostragem.getData());
            spTalhao.setSelection(Utils.getIndexChaveValor(talhaoList, amostragemView.getTalhaoNome()));

            String[] dataSplit = amostragem.getData().split("/");
            day = Integer.parseInt(dataSplit[0]);
            month = Integer.parseInt(dataSplit[1]);
            year = Integer.parseInt(dataSplit[2]);
        } else {
            amostragem.setId(0);
        }
    }

    private void validarDados() {
        try {
            String nome = etNomeAmostragem.getText().toString().trim().toUpperCase();
            String obs = etObservacaoAmostragem.getText().toString().trim().toUpperCase();
            String data = etDataAmostragem.getText().toString().trim().toUpperCase();

            if (nome.length() > 3 && data.length() != 0) {
                amostragem.setNome(nome);
                amostragem.setData(data);
                amostragem.setObservacao(obs);
                cadastrar(amostragem);
            } else {
                Utils.showMessage(context, context.getString(R.string.erro_validacao_amostragem), 0);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_cadastrar_amostragem), 0);
        }
    }

    private void startSpinners(List<ChaveValor> talhoes) {
        try {
            talhaoList = talhoes;
            ArrayAdapter<ChaveValor> adapterTalhoes = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item, talhoes);
            spTalhao.setAdapter(adapterTalhoes);

            getArgumentos();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_dados_cadastro_amostragem), 0);
        }

    }

    @SuppressLint("StaticFieldLeak")
    private void getTalhoes() {
        try {
            new AsyncTask<Void, Void, List<ChaveValor>>() {
                @Override
                protected List<ChaveValor> doInBackground(Void... voids) {
                    return db.talhaoDao().getTalhoesDropDown(true);
                }

                @Override
                protected void onPostExecute(List<ChaveValor> talhoes) {
                    startSpinners(talhoes);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_dados_cadastro_amostragem), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrar(Amostragem amostragem) {
        try {
            new AsyncTask<Void, Void, List<AmostragemTalhao>>() {
                @Override
                protected List<AmostragemTalhao> doInBackground(Void... voids) {
                    if (amostragem.getId() == 0) db.amostragemDao().insert(amostragem);
                    else db.amostragemDao().update(amostragem);
                    return db.amostragemDao().getAmostragensTalhao(true);
                }

                @Override
                protected void onPostExecute(List<AmostragemTalhao> amostragens) {
                    alertDialog.cancel();
                    view.atualizarAdapter(amostragens);
                    Utils.showMessage(context, "", 1);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_cadastrar_amostragem), 0);
        }
    }
}
