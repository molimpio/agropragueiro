package br.net.olimpiodev.agropragueiro.fragment.Cadastro;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
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
import io.realm.Realm;
import io.realm.RealmResults;

public class FazendaDialogFragment extends DialogFragment {

    private View view;
    private EditText etNomeFaz, etCidadeFaz, etObsFaz;
    private Spinner spUfFaz, spClienteFaz;
    private Button btCadastrarFaz;
    private AlertDialog alertDialog;
    private Fazenda fazenda;
    private RealmResults<Cliente> realmResults;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.dialog_fazenda, null);
        builder.setView(view);
        setRefs();
        alertDialog = builder.create();
        fazenda = new Fazenda();
        return alertDialog;
    }

    private void setRefs() {
        etNomeFaz = view.findViewById(R.id.et_nome_faz);
        etCidadeFaz = view.findViewById(R.id.et_cidade_faz);
        etObsFaz = view.findViewById(R.id.et_obs_faz);
        spUfFaz = view.findViewById(R.id.sp_uf_faz);
        spClienteFaz = view.findViewById(R.id.sp_cliente_faz);
        spClienteFaz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                fazenda.setCliente(realmResults.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Button btCancelarFaz = view.findViewById(R.id.bt_cancelar_faz);
        btCadastrarFaz = view.findViewById(R.id.bt_cadastrar_faz);

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

        btCancelarFaz.setOnClickListener(view -> {
            Utils.showMessage(getContext(), "", 3);
            alertDialog.dismiss();
        });

        btCadastrarFaz.setOnClickListener(view -> cadastrar());

        startSpinners();
    }

    private void startSpinners() {
        String ufs[] = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"};
        ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, ufs);
        spUfFaz.setAdapter(adapterUfs);

        Realm realm = Realm.getDefaultInstance();
        realmResults = realm.where(Cliente.class)
                .findAll().sort("nome");
        List<Cliente> clientes = realm.copyFromRealm(realmResults);

        ArrayAdapter<Cliente> adapterClientes = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, clientes);
        spClienteFaz.setAdapter(adapterClientes);
    }

    private void validarNomeCidade() {
        String nome = etNomeFaz.getText().toString().trim().toUpperCase();
        String cidade = etCidadeFaz.getText().toString().trim().toUpperCase();

        if (nome.length() > 3 && cidade.length() > 1) {
            fazenda.setNome(nome);
            fazenda.setCidade(cidade);
            btCadastrarFaz.setEnabled(true);
        } else {
            btCadastrarFaz.setEnabled(false);
        }
    }

    private void cadastrar() {
        String uf = spUfFaz.getSelectedItem().toString().toUpperCase();
        String obs = etObsFaz.getText().toString().trim().toUpperCase();

        fazenda.setUf(uf);
        fazenda.setObservacao(obs);
        fazenda.setAreaHa(0.00);

        FazendaDao.salvar(fazenda);
        Utils.showMessage(getContext(), "", 1);
        alertDialog.dismiss();
    }

}
