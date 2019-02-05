package br.net.olimpiodev.agropragueiro.view.fragment.Cadastro;


import android.content.Intent;
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

import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.TalhaoCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.presenter.TalhaoCadastroPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.activity.MapaActivity;

public class TalhaoCadastroFragment extends Fragment
        implements TalhaoCadastroContrato.TalhaoCadastroView {

    private EditText etNomeTalhao;
    private Spinner spFazenda;
    private Button btnContorno, btnNovo;
    private Talhao talhao;
    private List<ChaveValor> fazendaList;
    private Bundle bundle;
    private int fazendaIdSelecionado;
    private TalhaoCadastroContrato.TalhaoCadastroPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talhao_cadastro, container, false);
        setupView(view);
        bundle = this.getArguments();
        presenter = new TalhaoCadastroPresenter(this, getContext());
        presenter.getFazendas();
        return view;
    }

    private void setupView(View view) {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(getString(R.string.cadastrar_talhao));

            etNomeTalhao = view.findViewById(R.id.et_nome_talhao);
            etNomeTalhao.requestFocus();
            spFazenda = view.findViewById(R.id.sp_fazendas_talhao);
            spFazenda.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    talhao.setFazendaId(fazendaList.get(position).getChave());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Button btnCadastrar = view.findViewById(R.id.btn_cadastrar_talhao);
            btnCadastrar.setOnClickListener(view1 -> cadastrar());

            btnContorno = view.findViewById(R.id.btn_contorno_talhao);
            btnContorno.setOnClickListener(view1 -> openMapa());

            btnNovo = view.findViewById(R.id.btn_novo_talhao);
            btnNovo.setOnClickListener(v -> novoCadastro());

            Button btnCancelar = view.findViewById(R.id.btn_cancelar_talhao);
            btnCancelar.setOnClickListener(v -> cancelarTalhao());

        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_view_talhao), 0);
        }
    }

    private void disabledCampos() {
        etNomeTalhao.setEnabled(false);
        spFazenda.setEnabled(false);
        btnContorno.setEnabled(true);
        btnNovo.setVisibility(View.VISIBLE);
    }

    private void getArgumentos(Bundle bundle) {
        try {
            talhao = new Talhao();
            talhao.setId(0);

            if (bundle != null) {
                String keyBundle = getResources().getString(R.string.talhao_param);
                TalhaoFazenda tf = (TalhaoFazenda) bundle.getSerializable(keyBundle);
                talhao.setId(tf.getIdTalhao());
                talhao.setNome(tf.getNomeTalhao());
                talhao.setContorno(tf.getContorno());

                etNomeTalhao.setText(talhao.getNome());
                spFazenda.setSelection(Utils.getIndexChaveValor(fazendaList, tf.getNomeFazenda()));
            }
        } catch (Exception e) {
            Utils.showMessage(getContext(), getString(R.string.erro_ler_argumentos_talhao), 0);
        }
    }

    private void cancelarTalhao() {
//        TalhaoListaFragment tlf = new TalhaoListaFragment();
//        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
//                .replace(R.id.frg_principal, tlf).commit();
    }

    private void novoCadastro() {
        etNomeTalhao.setEnabled(true);
        spFazenda.setEnabled(true);
        etNomeTalhao.setText("");
        btnContorno.setEnabled(false);
        btnNovo.setVisibility(View.INVISIBLE);
        talhao = new Talhao();
        talhao.setId(0);
        talhao.setFazendaId(fazendaIdSelecionado);
    }

    private void openMapa() {
        Intent mapaIntent = new Intent(getContext(), MapaActivity.class);
        mapaIntent.putExtra(getResources().getString(R.string.talhao_id_param), talhao.getId());

        if (talhao.getContorno() != null) {
            mapaIntent.putExtra(getResources().getString(R.string.contorno_param), talhao.getContorno());
        }
        startActivity(mapaIntent);
    }

    @Override
    public void startSpinners(List<ChaveValor> fazendas) {
        try {
            fazendaList = fazendas;
            ArrayAdapter<ChaveValor> adapterFazendas = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, fazendas);
            spFazenda.setAdapter(adapterFazendas);
            getArgumentos(bundle);
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_carregar_dados_cadastro_talhao), 0);
        }
    }

    @Override
    public void cadastrar() {
        fazendaIdSelecionado = talhao.getFazendaId();
        String nome = etNomeTalhao.getText().toString().trim().toUpperCase();

        if (nome.length() > 0) {
            talhao.setNome(nome);
            presenter.cadastrar(talhao);
            disabledCampos();
        } else {
            Utils.showMessage(getContext(), getString(R.string.talhao_erro_cadastro), 0);
        }
    }

    @Override
    public void showMessage(String mensagem, int codigo) {
        Utils.showMessage(getContext(), mensagem, codigo);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.destroyView();
    }

}
