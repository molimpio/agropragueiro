package br.net.olimpiodev.agropragueiro.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.dao.ClienteDao;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteDialogFragment extends DialogFragment {

    private View view;
    private EditText etNomeCliente;
    private EditText etCidadeCliente;
    private Spinner spUfCliente;
    private Spinner spCategoriaCliente;
    private Button btCancelarCliente;
    private Button btCadastrarCliente;
    private AlertDialog alertDialog;
    private String nome;
    private String cidade;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_cliente, null);
        builder.setView(view);
        setRefs();
        alertDialog = builder.create();
        return alertDialog;
    }

    private void setRefs() {
        etNomeCliente = view.findViewById(R.id.et_nome_cliente);
        etCidadeCliente = view.findViewById(R.id.et_cidade_cliente);
        spUfCliente = view.findViewById(R.id.sp_uf_cliente);
        spCategoriaCliente = view.findViewById(R.id.sp_categoria_cliente);
        btCancelarCliente = view.findViewById(R.id.bt_cancelar_cliente);
        btCadastrarCliente = view.findViewById(R.id.bt_cadastrar_cliente);
        startSpinners();

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

        btCancelarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showMessage(getContext(), "", 3);
                alertDialog.dismiss();
            }
        });

        btCadastrarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastrar();
            }
        });
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
        nome = etNomeCliente.getText().toString().trim().toUpperCase();
        cidade = etCidadeCliente.getText().toString().trim().toUpperCase();

        if (nome.length() > 3 && cidade.length() > 1) {
            btCadastrarCliente.setEnabled(true);
        } else {
            btCadastrarCliente.setEnabled(false);
        }
    }

    private void cadastrar() {
        String uf = spUfCliente.getSelectedItem().toString().toUpperCase();
        String categoria = spCategoriaCliente.getSelectedItem().toString().toUpperCase();

        Cliente cliente = new Cliente();
        cliente.setNome(nome);
        cliente.setCategoria(categoria);
        cliente.setUf(uf);
        cliente.setCidade(cidade);

        ClienteDao clienteDao = new ClienteDao();
        clienteDao.salvar(cliente);
        Utils.showMessage(getContext(), "", 1);
        alertDialog.dismiss();
    }

}