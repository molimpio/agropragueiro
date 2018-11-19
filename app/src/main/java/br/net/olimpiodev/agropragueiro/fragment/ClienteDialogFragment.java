package br.net.olimpiodev.agropragueiro.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.dao.ClienteDao;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteDialogFragment extends DialogFragment {

    private View view;
    private EditText etNomeCliente, etCidadeCliente;
    private Spinner spUfCliente, spCategoriaCliente;
    private Button btCadastrarCliente;
    private AlertDialog alertDialog;
    private Cliente cliente;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_cliente, null);
        builder.setView(view);
        setRefs();
        alertDialog = builder.create();
        cliente = new Cliente();
        return alertDialog;
    }

    private void setRefs() {
        etNomeCliente = view.findViewById(R.id.et_nome_cliente);
        etCidadeCliente = view.findViewById(R.id.et_cidade_cliente);
        spUfCliente = view.findViewById(R.id.sp_uf_cliente);
        spCategoriaCliente = view.findViewById(R.id.sp_categoria_cliente);

        Button btCancelarCliente = view.findViewById(R.id.bt_cancelar_cliente);
        btCadastrarCliente = view.findViewById(R.id.bt_cadastrar_cliente);

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

        btCancelarCliente.setOnClickListener(view -> {
            Utils.showMessage(getContext(), "", 3);
            alertDialog.dismiss();
        });

        btCadastrarCliente.setOnClickListener(view -> cadastrar());

        startSpinners();
    }

    private void startSpinners() {
        String ufs[] = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};
        ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, ufs);
        adapterUfs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spUfCliente.setAdapter(adapterUfs);

        String categorias[] = {"Agricultor", "Consultor", "Empresa"};
        ArrayAdapter<String> adapterCategorias = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, categorias);
        adapterCategorias.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spCategoriaCliente.setAdapter(adapterCategorias);
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

    private void cadastrar() {
        String uf = spUfCliente.getSelectedItem().toString().toUpperCase();
        String categoria = spCategoriaCliente.getSelectedItem().toString().toUpperCase();

        cliente.setCategoria(categoria);
        cliente.setUf(uf);

        ClienteDao.salvar(cliente);
        Utils.showMessage(getContext(), "", 1);
        alertDialog.dismiss();
    }

}
