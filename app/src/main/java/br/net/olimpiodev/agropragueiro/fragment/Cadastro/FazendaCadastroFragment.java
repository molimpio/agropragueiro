package br.net.olimpiodev.agropragueiro.fragment.Cadastro;

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

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.dao.ClienteDao;
import br.net.olimpiodev.agropragueiro.dao.FazendaDao;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaCadastroFragment extends Fragment {

    private EditText etNomeFaz, etCidadeFaz, etObsFaz;
    private Spinner spUfFaz, spClienteFaz;
    private Button btnCadastrarFaz, btnNovo;
    private Fazenda fazenda;
    private Cliente cliente;
//    private RealmResults<Cliente> realmResults;
    private String ufs[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fazenda_cadastro, container,false);
        setRefs(view);
        fazenda = new Fazenda();
        Bundle bundle = this.getArguments();
        getArgumentos(bundle);
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

        etObsFaz = view.findViewById(R.id.et_obs_faz);
        spUfFaz = view.findViewById(R.id.sp_uf_faz);
        spClienteFaz = view.findViewById(R.id.sp_cliente_faz);
        spClienteFaz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
//                cliente = realmResults.get(position);
//                fazenda.setCliente(realmResults.get(position));
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
            etObsFaz.setEnabled(true);
            etNomeFaz.setText("");
            etCidadeFaz.setText("");
            etObsFaz.setText("");
            spClienteFaz.setEnabled(true);
            spUfFaz.setEnabled(true);
            btnCadastrarFaz.setEnabled(false);
            btnNovo.setVisibility(View.INVISIBLE);
        });

        startSpinners();
    }

    private void startSpinners() {
        ufs = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, ufs);
        spUfFaz.setAdapter(adapterUfs);

//        Realm realm = Realm.getDefaultInstance();
//        realmResults = realm.where(Cliente.class)
//                .findAll().sort("nome");
//        List<Cliente> clientes = realm.copyFromRealm(realmResults);
//
//        ArrayAdapter<Cliente> adapterClientes = new ArrayAdapter<>(getContext(),
//                android.R.layout.simple_spinner_dropdown_item, clientes);
//        spClienteFaz.setAdapter(adapterClientes);
    }

    private void validarNomeCidade() {
        String nome = etNomeFaz.getText().toString().trim().toUpperCase();
        String cidade = etCidadeFaz.getText().toString().trim().toUpperCase();

//        if (nome.length() > 3 && cidade.length() > 1) {
//            fazenda.setNome(nome);
//            fazenda.setCidade(cidade);
//            btnCadastrarFaz.setEnabled(true);
//        } else {
//            btnCadastrarFaz.setEnabled(false);
//        }
    }

    private void cadastrar() {
        String uf = spUfFaz.getSelectedItem().toString().toUpperCase();
        String obs = etObsFaz.getText().toString().trim().toUpperCase();

//        fazenda.setUf(uf);
//        fazenda.setObservacao(obs);
//        fazenda.setAreaHa(0.00);
//
//        FazendaDao.salvar(fazenda);
//        ClienteDao.adicionarFazenda(cliente, fazenda);
//
//        Utils.showMessage(getContext(), "", 1);
//
//        etNomeFaz.setEnabled(false);
//        etCidadeFaz.setEnabled(false);
//        etObsFaz.setEnabled(false);
//        spClienteFaz.setEnabled(false);
//        spUfFaz.setEnabled(false);
//        btnCadastrarFaz.setEnabled(false);
//        btnNovo.setVisibility(View.VISIBLE);
    }

    private void getArgumentos(Bundle bundle) {
//        try {
//            if (bundle != null) {
//                String keyBundle = getResources().getString(R.string.fazenda_param);
//                Fazenda f = (Fazenda) bundle.getSerializable(keyBundle);
//                fazenda.setId(f.getId());
//                fazenda.setNome(f.getNome());
//                fazenda.setCidade(f.getCidade());
//                fazenda.setObservacao(f.getObservacao());
//
//                etNomeFaz.setText(fazenda.getNome());
//                etCidadeFaz.setText(fazenda.getCidade());
//                etObsFaz.setText(fazenda.getObservacao());
//                spUfFaz.setSelection(Utils.getIndex(ufs, fazenda.getUf()));
//                spClienteFaz.setSelection(ClienteDao.getIndex(realmResults, fazenda.getCliente()));
//            }
//        } catch (Exception e) {
//
//        }
    }

}
