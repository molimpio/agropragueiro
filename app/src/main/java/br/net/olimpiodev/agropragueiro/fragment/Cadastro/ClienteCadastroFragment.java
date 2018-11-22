package br.net.olimpiodev.agropragueiro.fragment.Cadastro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.dao.ClienteDao;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteCadastroFragment extends Fragment {

    private EditText etNomeCliente, etCidadeCliente;
    private Spinner spUfCliente, spCategoriaCliente;
    private Button btCadastrarCliente, btnNovo;
    private Cliente cliente;
    private String ufs[];
    private String categorias[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cliente_cadastro, container, false);
        setRefs(view);
        cliente = new Cliente();
        Bundle bundle = this.getArguments();
        getArgumentos(bundle);
        return view;
    }

    private void setRefs(View view) {
        etNomeCliente = view.findViewById(R.id.et_nome_cliente);
        etNomeCliente.requestFocus();
        etNomeCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                validarNomeCidade();
            }
        });

        etCidadeCliente = view.findViewById(R.id.et_cidade_cliente);
        etCidadeCliente.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void afterTextChanged(Editable editable) {
                validarNomeCidade();
            }
        });

        spUfCliente = view.findViewById(R.id.sp_uf_cliente);
        spCategoriaCliente = view.findViewById(R.id.sp_categoria_cliente);

        Button btCancelarCliente = view.findViewById(R.id.bt_cancelar_cliente);
        btCancelarCliente.setOnClickListener(view1 ->
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit()
        );

        btCadastrarCliente = view.findViewById(R.id.bt_cadastrar_cliente);
        btCadastrarCliente.setOnClickListener(view1-> cadastrar());

        btnNovo = view.findViewById(R.id.btn_novo_cliente);
        btnNovo.setOnClickListener(view1 -> {
            etNomeCliente.setEnabled(true);
            etCidadeCliente.setEnabled(true);
            etNomeCliente.setText("");
            etCidadeCliente.setText("");
            spCategoriaCliente.setEnabled(true);
            spUfCliente.setEnabled(true);
            btnNovo.setVisibility(View.INVISIBLE);
            etNomeCliente.requestFocus();
            cliente = new Cliente();
        });

        startSpinners();
    }

    private void startSpinners() {
        ufs = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, ufs);
        adapterUfs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spUfCliente.setAdapter(adapterUfs);

        categorias = getResources().getStringArray(R.array.categorias_cliente);
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categorias);
        adapterCategorias.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spCategoriaCliente.setAdapter(adapterCategorias);
    }

    private void validarNomeCidade() {
        String nome = etNomeCliente.getText().toString().trim().toUpperCase();
        String cidade = etCidadeCliente.getText().toString().trim().toUpperCase();

//        if (nome.length() > 3 && cidade.length() > 1) {
//            cliente.setNome(nome);
//            cliente.setCidade(cidade);
//            btCadastrarCliente.setEnabled(true);
//        } else {
//            btCadastrarCliente.setEnabled(false);
//        }
    }

    private void cadastrar() {
//        String uf = spUfCliente.getSelectedItem().toString().toUpperCase();
//        String categoria = spCategoriaCliente.getSelectedItem().toString().toUpperCase();
//
//        cliente.setCategoria(categoria);
//        cliente.setUf(uf);
//        ClienteDao.salvar(cliente);
//
//        Utils.showMessage(getContext(), "", 1);
//
//        etNomeCliente.setEnabled(false);
//        etCidadeCliente.setEnabled(false);
//        spUfCliente.setEnabled(false);
//        spCategoriaCliente.setEnabled(false);
//        btCadastrarCliente.setEnabled(false);
//        btnNovo.setVisibility(View.VISIBLE);
    }

    private void getArgumentos(Bundle bundle) {
//        try {
//            if (bundle != null) {
//                String keyBundle = getResources().getString(R.string.cliente_param);
//                Cliente c = (Cliente) bundle.getSerializable(keyBundle);
//                cliente.setId(c.getId());
//                cliente.setNome(c.getNome());
//                cliente.setCidade(c.getCidade());
//                cliente.setUf(c.getUf());
//                cliente.setCategoria(c.getCategoria());
//
//                etNomeCliente.setText(cliente.getNome());
//                etCidadeCliente.setText(cliente.getCidade());
//                spUfCliente.setSelection(Utils.getIndex(ufs, cliente.getUf()));
//                spCategoriaCliente.setSelection(Utils.getIndex(categorias, cliente.getCategoria()));
//            }
//        } catch (Exception e) {
//
//        }
    }
}
