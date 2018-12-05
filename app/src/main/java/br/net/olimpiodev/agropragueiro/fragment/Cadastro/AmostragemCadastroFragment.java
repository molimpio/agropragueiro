package br.net.olimpiodev.agropragueiro.fragment.Cadastro;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import br.net.olimpiodev.agropragueiro.fragment.Lista.AmostragemListaFragment;
import br.net.olimpiodev.agropragueiro.model.Amostragem;
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.utils.Utils;


public class AmostragemCadastroFragment extends Fragment {

    private AppDatabase db;
    private EditText etNomeAmostragem, etDataAmostragem, etObservacaoAmostragem;
    private Spinner spTalhao;
    private Button btnCadastrar, btnPontos, btnNovo;
    private Amostragem amostragem;
    private List<ChaveValor> talhaoList;
    private Bundle bundle;
    private int talhaoSelecionado;
    private int ano, mes, dia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amostragem_cadastro, container, false);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        bundle = this.getArguments();

        GetTalhoes getTalhoes = new GetTalhoes();
        getTalhoes.execute();

        setRefs(view);
        amostragem = new Amostragem();
        amostragem.setId(0);

        return view;
    }

    private void setRefs(View view) {
        etNomeAmostragem = view.findViewById(R.id.et_nome_amostragem);
        etNomeAmostragem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etNomeAmostragem.getText().toString().length() > 3) {
                    btnCadastrar.setEnabled(true);
                } else {
                    btnCadastrar.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etNomeAmostragem.requestFocus();

        etDataAmostragem = view.findViewById(R.id.et_data_amostragem);
        etDataAmostragem.setOnClickListener(view12 -> new DatePickerDialog(getContext(), date,
                myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());

        spTalhao = view.findViewById(R.id.sp_talhoes_amostragem);
        spTalhao.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                amostragem.setTalhaoId(talhaoList.get(position).getChave());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnCancelar = view.findViewById(R.id.btn_cadastrar_amostragem);
        AmostragemListaFragment alf = new AmostragemListaFragment();

        btnCancelar.setOnClickListener(view1 ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frg_principal, alf).commit()
        );

        btnCadastrar = view.findViewById(R.id.btn_cadastrar_amostragem);
        btnCadastrar.setOnClickListener(view1 -> cadastrar());

        btnPontos = view.findViewById(R.id.btn_pontos_amostragem);
        btnPontos.setOnClickListener(view1 -> openMapa());

        btnNovo = view.findViewById(R.id.btn_nova_amostragem);
        btnNovo.setOnClickListener(view1 -> {
            etNomeAmostragem.setEnabled(true);
            etObservacaoAmostragem.setEnabled(true);
            etNomeAmostragem.setText("");
            etObservacaoAmostragem.setText("");
            btnCadastrar.setEnabled(true);
            btnPontos.setEnabled(false);
            btnNovo.setEnabled(false);
            btnNovo.setVisibility(View.INVISIBLE);
            amostragem = new Amostragem();
            amostragem.setId(0);
            amostragem.setTalhaoId(talhaoSelecionado);
        });
    }

    private void startSpinners(List<ChaveValor> talhoes) {
        ArrayAdapter<ChaveValor> adapterTalhoes = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, talhoes);
        spTalhao.setAdapter(adapterTalhoes);

    }

    private void initDateTime() {
        if (ano == 0) {
            Calendar c = Calendar.getInstance();
            ano = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrar() {
        talhaoSelecionado = amostragem.getTalhaoId();
        String nome = etNomeAmostragem.getText().toString().trim().toUpperCase();
        amostragem.setNome(nome);

        if (etObservacaoAmostragem.getText().length() > 0) {
            String obs = etObservacaoAmostragem.getText().toString().trim().toUpperCase();
            amostragem.setObservacao(obs);
        }

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (amostragem.getId() == 0) db.amostragemDao().insert(amostragem);
                else db.amostragemDao().update(amostragem);
                return null;
            }
        }.execute();

        Utils.showMessage(getContext(), "", 1);

        etNomeAmostragem.setEnabled(false);
        etObservacaoAmostragem.setEnabled(false);
        btnCadastrar.setEnabled(false);
        btnPontos.setEnabled(true);
        btnNovo.setVisibility(View.VISIBLE);
    }

    private void openMapa() {

    }

    private void getArgumentos(Bundle bundle) {
        try {
            if (bundle != null) {
                String keyBundle = getResources().getString(R.string.amostragem_param);
                AmostragemTalhao at = (AmostragemTalhao) bundle.getSerializable(keyBundle);
                amostragem.setId(at.getIdAmostragem());
                amostragem.setTalhaoId(at.getIdTalhao());
                amostragem.setNome(at.getNomeAmostragem());
                amostragem.setObservacao(at.getObservacaoAmostragem());
                amostragem.setData(at.getData());

                etNomeAmostragem.setText(amostragem.getNome());
                etObservacaoAmostragem.setText(amostragem.getObservacao());

                spTalhao.setSelection(Utils.getIndexChaveValor(talhaoList, at.getTalhaoNome()));

                //TODO: setar a data...
            }
        } catch (Exception e) {
            Utils.logar(e.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetTalhoes extends AsyncTask<Void, Void, List<ChaveValor>> {

        @Override
        protected List<ChaveValor> doInBackground(Void... voids) {
            List<ChaveValor> talhoes = db.talhaoDao().getTalhoesDropDown(true);
            return talhoes;
        }

        @Override
        protected void onPostExecute(List<ChaveValor> talhoes) {
            talhaoList = talhoes;
            startSpinners(talhoes);
            getArgumentos(bundle);
        }
    }

    private void updateLabel() {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        //edittext.setText(sdf.format(myCalendar.getTime()));
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };
}
