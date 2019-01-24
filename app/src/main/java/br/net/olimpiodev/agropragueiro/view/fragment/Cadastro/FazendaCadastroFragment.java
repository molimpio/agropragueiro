package br.net.olimpiodev.agropragueiro.view.fragment.Cadastro;

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
import br.net.olimpiodev.agropragueiro.contracts.FazendaCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;
import br.net.olimpiodev.agropragueiro.presenter.FazendaCadastroPresenter;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import br.net.olimpiodev.agropragueiro.view.fragment.Lista.FazendaListaFragment;

public class FazendaCadastroFragment extends Fragment
        implements FazendaCadastroContrato.FazendaCadastroView {

    private EditText etNomeFaz, etCidadeFaz;
    private Spinner spUfFaz, spClienteFaz;
    private Button btnCadastrarFaz, btnNovo;
    private Fazenda fazenda;
    private List<ChaveValor> clienteList;
    private Bundle bundle;
    private int clienteIdSelecionado;
    private FazendaCadastroContrato.FazendaCadastroPresenter presenter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fazenda_cadastro, container,false);
        setupView(view);
        bundle = this.getArguments();
        presenter = new FazendaCadastroPresenter(this, getContext());
        presenter.getClientes();
        return view;
    }

    private void setupView(View view) {
        try {
            Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity()))
                    .getSupportActionBar()).setTitle(getString(R.string.cadastrar_fazenda));

            etNomeFaz = view.findViewById(R.id.et_nome_faz);
            etNomeFaz.requestFocus();
            etCidadeFaz = view.findViewById(R.id.et_cidade_faz);
            spUfFaz = view.findViewById(R.id.sp_uf_faz);
            spClienteFaz = view.findViewById(R.id.sp_cliente_faz);
            spClienteFaz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    fazenda.setClienteId(clienteList.get(position).getChave());
                    presenter.getClienteById(fazenda.getClienteId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            btnCadastrarFaz = view.findViewById(R.id.bt_cadastrar_faz);
            btnCadastrarFaz.setOnClickListener(view1 -> cadastrar());

            btnNovo = view.findViewById(R.id.btn_novo_fazenda);
            btnNovo.setOnClickListener(view1 -> novoCadastro());

            Button btCancelarFaz = view.findViewById(R.id.bt_cancelar_faz);
            btCancelarFaz.setOnClickListener(view1 -> cancelarFazenda());
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_view_fazendas), 0);
        }
    }

    private void disabledCampos() {
        etNomeFaz.setEnabled(false);
        etCidadeFaz.setEnabled(false);
        spClienteFaz.setEnabled(false);
        spUfFaz.setEnabled(false);
        btnCadastrarFaz.setEnabled(false);
        btnNovo.setVisibility(View.VISIBLE);
    }

    private void getArgumentos(Bundle bundle) {
        try {
            fazenda = new Fazenda();
            fazenda.setId(0);

            if (bundle != null) {
                String keyBundle = getString(R.string.fazenda_param);
                FazendaCliente fc = (FazendaCliente) bundle.getSerializable(keyBundle);
                fazenda.setId(fc.getIdFazenda());
                fazenda.setNome(fc.getNomeFazenda());
                fazenda.setCidade(fc.getCidadeFazenda());
                fazenda.setUf(fc.getUfFazenda());

                etNomeFaz.setText(fazenda.getNome());
                etCidadeFaz.setText(fazenda.getCidade());

                spClienteFaz.setSelection(Utils.getIndexChaveValor(clienteList, fc.getNomeCliente()));

                spUfFaz.setSelection(Utils.getIndex(getResources()
                        .getStringArray(R.array.estados), fazenda.getUf()));
            }
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_argumentos_fazenda), 0);
        }
    }

    private void cancelarFazenda() {
        FazendaListaFragment flf = new FazendaListaFragment();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.frg_principal, flf).commit();
    }

    private void novoCadastro() {
        etNomeFaz.setEnabled(true);
        etNomeFaz.requestFocus();
        etCidadeFaz.setEnabled(true);
        etNomeFaz.setText("");
        etCidadeFaz.setText("");
        spClienteFaz.setEnabled(true);
        spUfFaz.setEnabled(true);
        btnCadastrarFaz.setEnabled(true);
        btnNovo.setVisibility(View.INVISIBLE);
        fazenda = new Fazenda();
        fazenda.setId(0);
        fazenda.setClienteId(clienteIdSelecionado);
    }

    @Override
    public void startSpinners(List<ChaveValor> clientes) {
        try {
            clienteList = clientes;

            String[] ufs = getResources().getStringArray(R.array.estados);
            ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, ufs);
            spUfFaz.setAdapter(adapterUfs);

            ArrayAdapter<ChaveValor> adapterClientes = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_dropdown_item, clientes);
            spClienteFaz.setAdapter(adapterClientes);

            getArgumentos(bundle);
        } catch (Exception ex) {
            Utils.showMessage(getContext(), getString(R.string.erro_carregar_dados_cadastro_fazenda), 0);
        }
    }

    @Override
    public void cadastrar() {
        clienteIdSelecionado = fazenda.getClienteId();
        String nome = etNomeFaz.getText().toString().trim().toUpperCase();
        String cidade = etCidadeFaz.getText().toString().trim().toUpperCase();

        if (nome.length() > 2 && cidade.length() > 1) {
            String uf = spUfFaz.getSelectedItem().toString().toUpperCase();
            fazenda.setUf(uf);
            fazenda.setNome(nome);
            fazenda.setCidade(cidade);
            presenter.cadastrar(fazenda);
            disabledCampos();
        } else {
            Utils.showMessage(getContext(), getString(R.string.fazenda_erro_cadastro), 0);
        }
    }

    @Override
    public void getDadosCliente(Cliente cliente) {
        spUfFaz.setSelection(Utils.getIndex(getResources()
                .getStringArray(R.array.estados), cliente.getUf()));
        etCidadeFaz.setText(cliente.getCidade());
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
