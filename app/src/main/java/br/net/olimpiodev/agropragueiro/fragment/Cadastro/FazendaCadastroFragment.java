package br.net.olimpiodev.agropragueiro.fragment.Cadastro;

import android.annotation.SuppressLint;
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

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaCadastroFragment extends Fragment {

    private AppDatabase db;
    private EditText etNomeFaz, etCidadeFaz;
    private Spinner spUfFaz, spClienteFaz;
    private Button btnCadastrarFaz, btnNovo;
    private Fazenda fazenda;
    private List<ChaveValor> clienteList;
    private Bundle bundle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fazenda_cadastro, container,false);
        db = Room.databaseBuilder(getContext(), AppDatabase.class, AppDatabase.DB_NAME).build();

        bundle = this.getArguments();

        GetClientes getClientes = new GetClientes();
        getClientes.execute();

        setRefs(view);
        fazenda = new Fazenda();
        fazenda.setId(0);
        return view;
    }

    private void setRefs(View view) {
        etNomeFaz = view.findViewById(R.id.et_nome_faz);
        etNomeFaz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                validarNomeCidade();
            }
        });
        etNomeFaz.requestFocus();

        etCidadeFaz = view.findViewById(R.id.et_cidade_faz);
        etCidadeFaz.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                validarNomeCidade();
            }
        });

        spUfFaz = view.findViewById(R.id.sp_uf_faz);
        spClienteFaz = view.findViewById(R.id.sp_cliente_faz);
        spClienteFaz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                fazenda.setClienteId(clienteList.get(position).getChave());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btCancelarFaz = view.findViewById(R.id.bt_cancelar_faz);
        btCancelarFaz.setOnClickListener(view1 ->
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit()
        );

        btnCadastrarFaz = view.findViewById(R.id.bt_cadastrar_faz);
        btnCadastrarFaz.setOnClickListener(view1 -> cadastrar());

        btnNovo = view.findViewById(R.id.btn_novo_fazenda);
        btnNovo.setOnClickListener(view1 -> {
            etNomeFaz.setEnabled(true);
            etNomeFaz.requestFocus();
            etCidadeFaz.setEnabled(true);
            etNomeFaz.setText("");
            etCidadeFaz.setText("");
            spClienteFaz.setEnabled(true);
            spUfFaz.setEnabled(true);
            btnCadastrarFaz.setEnabled(false);
            btnNovo.setVisibility(View.INVISIBLE);
            fazenda = new Fazenda();
            fazenda.setId(0);
        });
    }

    private void startSpinners(List<ChaveValor> clientes) {
        String[] ufs = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, ufs);
        spUfFaz.setAdapter(adapterUfs);

        ArrayAdapter<ChaveValor> adapterClientes = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, clientes);
        spClienteFaz.setAdapter(adapterClientes);
    }

    private void validarNomeCidade() {
        String nome = etNomeFaz.getText().toString().trim().toUpperCase();
        String cidade = etCidadeFaz.getText().toString().trim().toUpperCase();

        if (nome.length() > 1 && cidade.length() > 1) {
            fazenda.setNome(nome);
            fazenda.setCidade(cidade);
            btnCadastrarFaz.setEnabled(true);
        } else {
            btnCadastrarFaz.setEnabled(false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrar() {
        String uf = spUfFaz.getSelectedItem().toString().toUpperCase();
        fazenda.setUf(uf);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                if (fazenda.getId() == 0) db.fazendaDao().insert(fazenda);
                else db.fazendaDao().update(fazenda);
                return null;
            }
        }.execute();

        Utils.showMessage(getContext(), "", 1);

        etNomeFaz.setEnabled(false);
        etCidadeFaz.setEnabled(false);
        spClienteFaz.setEnabled(false);
        spUfFaz.setEnabled(false);
        btnCadastrarFaz.setEnabled(false);
        btnNovo.setVisibility(View.VISIBLE);
    }

    private void getArgumentos(Bundle bundle) {
        try {
            if (bundle != null) {
                String keyBundle = getResources().getString(R.string.fazenda_param);
                FazendaCliente fc = (FazendaCliente) bundle.getSerializable(keyBundle);
                fazenda.setId(fc.getIdFazenda());
                fazenda.setNome(fc.getNomeFazenda());
                fazenda.setCidade(fc.getCidadeFazenda());
                fazenda.setUf(fc.getUfFazenda());

                etNomeFaz.setText(fazenda.getNome());
                etCidadeFaz.setText(fazenda.getCidade());

                spUfFaz.setSelection(Utils.getIndex(getResources()
                                .getStringArray(R.array.estados), fazenda.getUf()));

                spClienteFaz.setSelection(Utils.getIndexChaveValor(clienteList, fc.getNomeCliente()));
            }
        } catch (Exception e) {
            Utils.logar(e.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GetClientes extends AsyncTask<Void, Void, List<ChaveValor>> {

        @Override
        protected List<ChaveValor> doInBackground(Void... voids) {
            List<ChaveValor> clientes = db.clienteDao().getClientesDropDown(true);
            return clientes;
        }

        @Override
        protected void onPostExecute(List<ChaveValor> clientes) {
            clienteList = clientes;
            startSpinners(clientes);
            getArgumentos(bundle);
        }
    }

}
