package br.net.olimpiodev.agropragueiro.fragment.Cadastro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.dao.FazendaDao;
import br.net.olimpiodev.agropragueiro.dao.TalhaoDao;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import io.realm.Realm;
import io.realm.RealmResults;

public class TalhaoCadastroFragment extends Fragment {

    private EditText etNomeTalhao, etObservacao;
    private Spinner spFazenda;
    private Button btnCadastrar, btnContorno, btnNovo;
    private Talhao talhao;
    private RealmResults<Fazenda> realmResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talhao_cadastro, container, false);
        setRefs(view);
        talhao = new Talhao();
        Bundle bundle = this.getArguments();
        getArgumentos(bundle);
        return view;
    }

    private void setRefs(View view) {
        etNomeTalhao = view.findViewById(R.id.et_nome_talhao);
        etNomeTalhao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etNomeTalhao.getText().toString().length() > 1) {
                    btnCadastrar.setEnabled(true);
                } else {
                    btnCadastrar.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etObservacao = view.findViewById(R.id.et_obs_talhao);

        spFazenda = view.findViewById(R.id.sp_fazendas_talhao);
        spFazenda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                talhao.setFazenda(realmResults.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btnCancelar = view.findViewById(R.id.btn_cancelar_talhao);
        btnCancelar.setOnClickListener(view1 ->
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit()
        );

        btnCadastrar = view.findViewById(R.id.btn_cadastrar_talhao);
        btnCadastrar.setOnClickListener(view1 -> cadastrar());

        btnContorno = view.findViewById(R.id.btn_contorno_talhao);
        btnContorno.setOnClickListener(view1 -> openMapa());

        btnNovo = view.findViewById(R.id.btn_novo_talhao);
        btnNovo.setOnClickListener(view1 -> {
            etNomeTalhao.setEnabled(true);
            etObservacao.setEnabled(true);
            spFazenda.setEnabled(true);
            etNomeTalhao.setText("");
            etObservacao.setText("");
            btnCadastrar.setEnabled(false);
            btnContorno.setEnabled(false);
            btnNovo.setVisibility(View.INVISIBLE);
        });

        startSpinners();
    }

    private void startSpinners() {
        Realm realm = Realm.getDefaultInstance();
        realmResults = realm.where(Fazenda.class)
                .findAll().sort("nome");
        List<Fazenda> fazendas = realm.copyFromRealm(realmResults);

        ArrayAdapter<Fazenda> adapterFazendas = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, fazendas);
        spFazenda.setAdapter(adapterFazendas);
    }

    private void cadastrar() {
        String nome = etNomeTalhao.getText().toString().trim().toUpperCase();
        String obs = etObservacao.getText().toString().trim().toUpperCase();

        talhao.setNome(nome);
        talhao.setObservacao(obs);
        talhao.setAreaHa(0.00);
        TalhaoDao.salvar(talhao);
        Utils.showMessage(getContext(), "", 1);

        etNomeTalhao.setEnabled(false);
        etObservacao.setEnabled(false);
        spFazenda.setEnabled(false);
        btnCadastrar.setEnabled(false);
        btnContorno.setEnabled(true);
        btnNovo.setVisibility(View.VISIBLE);
    }

    private void openMapa() {

    }

    private void getArgumentos(Bundle bundle) {
        try {
            if (bundle != null) {
                String keyBundle = getResources().getString(R.string.talhao_param);
                Talhao t = (Talhao) bundle.getSerializable(keyBundle);
                talhao.setId(t.getId());
                talhao.setNome(t.getNome());
                talhao.setObservacao(t.getObservacao());

                etNomeTalhao.setText(talhao.getNome());
                etObservacao.setText(talhao.getObservacao());
                spFazenda.setSelection(FazendaDao.getIndex(realmResults, talhao.getFazenda()));
            }
        } catch (Exception e) {

        }
    }

}
