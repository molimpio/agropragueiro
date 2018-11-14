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

import java.util.ArrayList;
import java.util.List;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.dao.ClienteDao;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaDialogFragment extends DialogFragment {

    private View view;
    private EditText etNomeFaz;
    private EditText etCidadeFaz;
    private EditText etObsFaz;
    private Spinner spUfFaz;
    private Spinner spClienteFaz;
    private Button btCancelarFaz;
    private Button btCadastrarFaz;
    private AlertDialog alertDialog;
    private String nome;
    private String cidade;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fazenda, null);
        builder.setView(view);
        setRefs();
        alertDialog = builder.create();
        return alertDialog;
    }

    private void setRefs() {
        etNomeFaz = view.findViewById(R.id.et_nome_faz);
        etCidadeFaz = view.findViewById(R.id.et_cidade_faz);
        etObsFaz = view.findViewById(R.id.et_obs_faz);
        spUfFaz = view.findViewById(R.id.sp_uf_faz);
        spClienteFaz = view.findViewById(R.id.sp_cliente_faz);
        btCancelarFaz = view.findViewById(R.id.bt_cancelar_faz);
        btCadastrarFaz = view.findViewById(R.id.bt_cadastrar_faz);
        startSpinners();

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

        btCancelarFaz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.showMessage(getContext(), "", 3);
                alertDialog.dismiss();
            }
        });

        btCadastrarFaz.setOnClickListener(new View.OnClickListener() {
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
        spUfFaz.setAdapter(adapterUfs);

        ClienteDao clienteDao = new ClienteDao();
        ArrayList<Cliente> clientes = clienteDao.getIdNome();
        ArrayAdapter<Cliente> adapterClientes = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, clientes);
        adapterClientes.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spClienteFaz.setAdapter(adapterClientes);
    }

    private void validarNomeCidade() {
        nome = etNomeFaz.getText().toString().trim().toUpperCase();
        cidade = etCidadeFaz.getText().toString().trim().toUpperCase();

        if (nome.length() > 3 && cidade.length() > 1) {
            btCadastrarFaz.setEnabled(true);
        } else {
            btCadastrarFaz.setEnabled(false);
        }
    }

    private void cadastrar() {
        String uf = spUfFaz.getSelectedItem().toString().toUpperCase();
//        String categoria = spCategoriaCliente.getSelectedItem().toString().toUpperCase();
//
//        Cliente cliente = new Cliente();
//        cliente.setNome(nome);
//        cliente.setCategoria(categoria);
//        cliente.setUf(uf);
//        cliente.setCidade(cidade);
//
//        ClienteDao clienteDao = new ClienteDao();
//        clienteDao.salvar(cliente);
//        Utils.showMessage(getContext(), "", 1);
//        alertDialog.dismiss();
    }

}
