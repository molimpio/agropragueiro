package br.net.olimpiodev.agropragueiro.view.fragment.Cadastro;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.AmostragemCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.Amostragem;
import br.net.olimpiodev.agropragueiro.model.AmostragemTalhao;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.presenter.AmostragemCadastroPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.fragment.Lista.AmostragemListaFragment;


public class AmostragemCadastroFragment extends Fragment
        implements AmostragemCadastroContrato.AmostragemCadastroView {

    private EditText etNomeAmostragem, etDataAmostragem, etObservacaoAmostragem;
    private Spinner spTalhao;
    private Button btnPontos, btnNovo;
    private Amostragem amostragem;
    private List<ChaveValor> talhaoList;
    private Bundle bundle;
    private int talhaoSelecionado;
    private int year, month, day = 0;
    private AmostragemCadastroContrato.AmostragemCadastroPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amostragem_cadastro, container, false);
        setupView(view);
        bundle = this.getArguments();
        presenter = new AmostragemCadastroPresenter(this, getContext());
        presenter.getTalhoes();
        return view;
    }

    private void setupView(View view) {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(getString(R.string.cadastrar_amostragem));

            etNomeAmostragem = view.findViewById(R.id.et_nome_amostragem);
            etNomeAmostragem.requestFocus();

            etDataAmostragem = view.findViewById(R.id.et_data_amostragem);
            etDataAmostragem.setFocusable(false);

            etDataAmostragem.setOnClickListener(view12 ->
                    new DatePickerDialog(getContext(), date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show());

            etObservacaoAmostragem = view.findViewById(R.id.et_observacao_amostragem);

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

            Button btnCancelar = view.findViewById(R.id.btn_cancelar_amostragem);
            btnCancelar.setOnClickListener(view1 -> cancelarAmostragem());

            Button btnCadastrar = view.findViewById(R.id.btn_cadastrar_amostragem);
            btnCadastrar.setOnClickListener(view1 -> cadastrar());

            btnPontos = view.findViewById(R.id.btn_pontos_amostragem);
            btnPontos.setOnClickListener(view1 -> openMapa());

            btnNovo = view.findViewById(R.id.btn_nova_amostragem);
            btnNovo.setOnClickListener(view1 -> novoCadastro());

        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_view_amostragem), 0);
        }
    }

    private void disabledCampos() {
        etNomeAmostragem.setEnabled(false);
        etObservacaoAmostragem.setEnabled(false);
        btnPontos.setEnabled(true);
        btnNovo.setVisibility(View.VISIBLE);
    }

    private void getArgumentos(Bundle bundle) {
        try {
            amostragem = new Amostragem();
            amostragem.setId(0);

            if (bundle != null) {
                String keyBundle = getString(R.string.amostragem_param);
                AmostragemTalhao at = (AmostragemTalhao) bundle.getSerializable(keyBundle);
                amostragem.setId(at.getIdAmostragem());
                amostragem.setTalhaoId(at.getIdTalhao());
                amostragem.setNome(at.getNomeAmostragem());
                amostragem.setObservacao(at.getObservacaoAmostragem());
                amostragem.setData(at.getData());

                etNomeAmostragem.setText(amostragem.getNome());
                etObservacaoAmostragem.setText(amostragem.getObservacao());
                etDataAmostragem.setText(amostragem.getData());

                spTalhao.setSelection(Utils.getIndexChaveValor(talhaoList, at.getTalhaoNome()));

                String[] dataSplit = amostragem.getData().split("/");
                day = Integer.parseInt(dataSplit[0]);
                month = Integer.parseInt(dataSplit[1]);
                year = Integer.parseInt(dataSplit[2]);
            }
        } catch (Exception e) {
            Utils.showMessage(getContext(), getString(R.string.erro_args_amostragem), 0);
        }
    }

    private void cancelarAmostragem() {
        AmostragemListaFragment alf = new AmostragemListaFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frg_principal, alf).commit();
    }

    private void novoCadastro() {
        etNomeAmostragem.setEnabled(true);
        etObservacaoAmostragem.setEnabled(true);
        etNomeAmostragem.setText("");
        etObservacaoAmostragem.setText("");
        btnPontos.setEnabled(false);
        btnNovo.setEnabled(false);
        btnNovo.setVisibility(View.INVISIBLE);
        amostragem = new Amostragem();
        amostragem.setId(0);
        amostragem.setTalhaoId(talhaoSelecionado);
        year = 0;
        month = 0;
        day = 0;
    }

    private void openMapa() { }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        etDataAmostragem.setText(sdf.format(myCalendar.getTime()));
    }

    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    @Override
    public void startSpinners(List<ChaveValor> talhoes) {
        try {
            talhaoList = talhoes;
            ArrayAdapter<ChaveValor> adapterTalhoes = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, talhoes);
            spTalhao.setAdapter(adapterTalhoes);
            getArgumentos(bundle);
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_carregar_dados_cadastro_amostragem), 0);
        }

    }

    @Override
    public void cadastrar() {
        talhaoSelecionado = amostragem.getTalhaoId();
        String nome = etNomeAmostragem.getText().toString().trim().toUpperCase();
        String obs = etObservacaoAmostragem.getText().toString().trim().toUpperCase();
        String data = etDataAmostragem.getText().toString().trim().toUpperCase();

        if (nome.length() > 3 && data.length() != 0) {
            amostragem.setNome(nome);
            amostragem.setData(data);
            amostragem.setObservacao(obs);
            presenter.cadastrar(amostragem);
            disabledCampos();
        } else {
            Utils.showMessage(getContext(), getString(R.string.erro_validacao_amostragem), 0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }
}
