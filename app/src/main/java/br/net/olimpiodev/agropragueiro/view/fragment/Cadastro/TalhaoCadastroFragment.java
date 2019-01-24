package br.net.olimpiodev.agropragueiro.view.fragment.Cadastro;


import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
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

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.view.activity.MapaActivity;
import br.net.olimpiodev.agropragueiro.view.fragment.Lista.TalhaoListaFragment;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Talhao;
import br.net.olimpiodev.agropragueiro.model.TalhaoFazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class TalhaoCadastroFragment extends Fragment {

    private AppDatabase db;
    private EditText etNomeTalhao;
    private Spinner spFazenda;
    private Button btnCadastrar, btnContorno, btnNovo;
    private Talhao talhao;
    private List<ChaveValor> fazendaList;
    private Bundle bundle;
    private int fazendaIdSelecionado;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talhao_cadastro, container, false);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        bundle = this.getArguments();

        GetFazendas getFazendas = new GetFazendas();
        getFazendas.execute();

        setRefs(view);
        talhao = new Talhao();
        talhao.setId(0);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cadastrar Talhão");
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

        Button btnCancelar = view.findViewById(R.id.btn_cancelar_talhao);
        TalhaoListaFragment tlf = new TalhaoListaFragment();

        btnCancelar.setOnClickListener(view1 ->
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frg_principal, tlf).commit()
        );

        btnCadastrar = view.findViewById(R.id.btn_cadastrar_talhao);
        btnCadastrar.setOnClickListener(view1 -> cadastrar());

        btnContorno = view.findViewById(R.id.btn_contorno_talhao);
        btnContorno.setOnClickListener(view1 -> openMapa());

        btnNovo = view.findViewById(R.id.btn_novo_talhao);
        btnNovo.setOnClickListener(view1 -> {
            etNomeTalhao.setEnabled(true);
            spFazenda.setEnabled(true);
            etNomeTalhao.setText("");
            btnCadastrar.setEnabled(false);
            btnContorno.setEnabled(false);
            btnNovo.setVisibility(View.INVISIBLE);
            talhao = new Talhao();
            talhao.setId(0);
            talhao.setFazendaId(fazendaIdSelecionado);
        });
    }

    private void startSpinners(List<ChaveValor> fazendas) {
        ArrayAdapter<ChaveValor> adapterFazendas = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, fazendas);
        spFazenda.setAdapter(adapterFazendas);
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrar() {
        fazendaIdSelecionado = talhao.getFazendaId();
        String nome = etNomeTalhao.getText().toString().trim().toUpperCase();
        talhao.setNome(nome);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (talhao.getId() == 0) db.talhaoDao().insert(talhao);
                else db.talhaoDao().update(talhao);
                return null;
            }
        }.execute();

        Utils.showMessage(getContext(), "", 1);

        etNomeTalhao.setEnabled(false);
        spFazenda.setEnabled(false);
        btnCadastrar.setEnabled(false);
        btnContorno.setEnabled(true);
        btnNovo.setVisibility(View.VISIBLE);
    }

    private void openMapa() {
        Intent mapaIntent = new Intent(getContext(), MapaActivity.class);
        mapaIntent.putExtra(getResources().getString(R.string.talhao_id_param), talhao.getId());

        if (talhao.getContorno() != null) {
            mapaIntent.putExtra(getResources().getString(R.string.contorno_param), talhao.getContorno());
        }
        startActivity(mapaIntent);
    }

    private void getArgumentos(Bundle bundle) {
        try {
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
            Utils.logar(e.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetFazendas extends AsyncTask<Void, Void, List<ChaveValor>> {

        @Override
        protected List<ChaveValor> doInBackground(Void... voids) {
            List<ChaveValor> fazendas = db.fazendaDao().getFazendasDropDown(true);
            return fazendas;
        }

        @Override
        protected void onPostExecute(List<ChaveValor> fazendas) {
            fazendaList = fazendas;
            startSpinners(fazendas);
            getArgumentos(bundle);
        }
    }
}
