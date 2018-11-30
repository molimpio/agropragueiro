package br.net.olimpiodev.agropragueiro.fragment.Cadastro;


import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.fragment.Lista.ClienteListaFragment;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteCadastroFragment extends Fragment {

    private EditText etNomeCliente, etCidadeCliente;
    private Spinner spUfCliente;
    private Button btCadastrarCliente, btnNovo;
    private Cliente cliente;
    private String ufs[];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cliente_cadastro, container, false);
        setRefs(view);
        cliente = new Cliente();
        cliente.setId(0);
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

        Button btCancelarCliente = view.findViewById(R.id.bt_cancelar_cliente);

        ClienteListaFragment clf = new ClienteListaFragment();
        btCancelarCliente.setOnClickListener(view1 ->
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frg_principal, clf).commit()
        );

        btCadastrarCliente = view.findViewById(R.id.bt_cadastrar_cliente);
        btCadastrarCliente.setOnClickListener(view1-> cadastrar());

        btnNovo = view.findViewById(R.id.btn_novo_cliente);
        btnNovo.setOnClickListener(view1 -> {
            etNomeCliente.setEnabled(true);
            etCidadeCliente.setEnabled(true);
            etNomeCliente.setText("");
            etCidadeCliente.setText("");
            spUfCliente.setEnabled(true);
            btnNovo.setVisibility(View.INVISIBLE);
            etNomeCliente.requestFocus();
            cliente = new Cliente();
            cliente.setId(0);
        });

        startSpinners();
    }

    private void startSpinners() {
        ufs = getResources().getStringArray(R.array.estados);
        ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, ufs);
        adapterUfs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spUfCliente.setAdapter(adapterUfs);
    }

    private void validarNomeCidade() {
        String nome = etNomeCliente.getText().toString().trim().toUpperCase();
        String cidade = etCidadeCliente.getText().toString().trim().toUpperCase();

        if (nome.length() > 3 && cidade.length() > 1) {
            cliente.setNome(nome);
            cliente.setCidade(cidade);
            btCadastrarCliente.setEnabled(true);
        } else {
            btCadastrarCliente.setEnabled(false);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrar() {
        String uf = spUfCliente.getSelectedItem().toString().toUpperCase();
        cliente.setUf(uf);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                final AppDatabase db = Room.databaseBuilder(getContext(),
                        AppDatabase.class, AppDatabase.DB_NAME).build();
                List<Usuario> usuario = db.usuarioDao().getUsuario();
                cliente.setUsuarioId(usuario.get(0).getId());
                if (cliente.getId() == 0) db.clienteDao().insert(cliente);
                else db.clienteDao().update(cliente);
                return null;
            }
        }.execute();

        Utils.showMessage(getContext(), "", 1);

        etNomeCliente.setEnabled(false);
        etCidadeCliente.setEnabled(false);
        spUfCliente.setEnabled(false);
        btCadastrarCliente.setEnabled(false);
        btnNovo.setVisibility(View.VISIBLE);
    }

    private void getArgumentos(Bundle bundle) {
        try {
            if (bundle != null) {
                String keyBundle = getResources().getString(R.string.cliente_param);
                Cliente c = (Cliente) bundle.getSerializable(keyBundle);
                cliente.setId(c.getId());
                cliente.setNome(c.getNome());
                cliente.setCidade(c.getCidade());
                cliente.setUf(c.getUf());

                etNomeCliente.setText(cliente.getNome());
                etCidadeCliente.setText(cliente.getCidade());
                spUfCliente.setSelection(Utils.getIndex(ufs, cliente.getUf()));
            }
        } catch (Exception e) {

        }
    }
}
