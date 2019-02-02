package br.net.olimpiodev.agropragueiro.view.activity.Cadastro;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.ClienteCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.Cliente;

public class ClienteCadastroActivity extends AppCompatActivity  {

    private EditText etNomeCliente, etCidadeCliente;
    private Spinner spUfCliente;
    private Button btCadastrarCliente, btnNovo;
    private Cliente cliente;
    private String ufs[];
    private ClienteCadastroContrato.ClienteCadastroPresenter presenter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_cadastro);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.cadastrar_cliente));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        setupView();
//        Bundle bundle = this.getArguments();
//        getArgumentos(bundle);
//        presenter = new ClienteCadastroPresenter(this, ClienteCadastroActivity.this);
    }

//    private void setupView() {
//        try {
//            etNomeCliente = findViewById(R.id.et_nome_cliente);
//            etNomeCliente.requestFocus();
//            etCidadeCliente = findViewById(R.id.et_cidade_cliente);
//            spUfCliente = findViewById(R.id.sp_uf_cliente);
//
//            btCadastrarCliente = findViewById(R.id.bt_cadastrar_cliente);
//            btCadastrarCliente.setOnClickListener(v-> cadastrar());
//
//            btnNovo = findViewById(R.id.btn_novo_cliente);
//            btnNovo.setOnClickListener(v -> novoCadastro());
//
//            startSpinners();
//        } catch (Exception ex) {
//            Utils.showMessage(this, getString(R.string.erro_view_clientes), 0);
//        }
//    }
//
//    private void startSpinners() {
//        try {
//            ufs = getResources().getStringArray(R.array.estados);
//            ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(Objects.requireNonNull(this),
//                    android.R.layout.simple_spinner_item, ufs);
//            adapterUfs.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
//            spUfCliente.setAdapter(adapterUfs);
//        } catch (Exception ex) {
//            Utils.showMessage(this, getString(R.string.erro_carregar_dados_cadastro_clientes), 0);
//        }
//    }
//
//    private void disabledCampos() {
//        etNomeCliente.setEnabled(false);
//        etCidadeCliente.setEnabled(false);
//        spUfCliente.setEnabled(false);
//        btCadastrarCliente.setEnabled(false);
//        btnNovo.setVisibility(View.VISIBLE);
//    }
//
//    private void getArgumentos(Bundle bundle) {
//        try {
//            cliente = new Cliente();
//            cliente.setId(0);
//
//            if (bundle != null) {
//                String keyBundle = getString(R.string.cliente_param);
//                Cliente c = (Cliente) bundle.getSerializable(keyBundle);
//
//                cliente.setId(c.getId());
//                cliente.setNome(c.getNome());
//                cliente.setCidade(c.getCidade());
//                cliente.setUf(c.getUf());
//
//                etNomeCliente.setText(cliente.getNome());
//                etCidadeCliente.setText(cliente.getCidade());
//                spUfCliente.setSelection(Utils.getIndex(ufs, cliente.getUf()));
//            }
//        } catch (Exception ex) {
//            Utils.showMessage(this, getString(R.string.erro_args_cliente), 0);
//        }
//    }
//
//    public void novoCadastro() {
//        etNomeCliente.setEnabled(true);
//        etCidadeCliente.setEnabled(true);
//        etNomeCliente.setText("");
//        etCidadeCliente.setText("");
//        spUfCliente.setEnabled(true);
//        btnNovo.setVisibility(View.INVISIBLE);
//        btCadastrarCliente.setEnabled(true);
//        etNomeCliente.requestFocus();
//        cliente = new Cliente();
//        cliente.setId(0);
//    }

//    @Override
//    public void cadastrar() {
//        String nome = etNomeCliente.getText().toString().trim().toUpperCase();
//        String cidade = etCidadeCliente.getText().toString().trim().toUpperCase();
//
//        if (nome.length() > 3 && cidade.length() > 1) {
//            cliente.setUf(spUfCliente.getSelectedItem().toString().toUpperCase());
//            cliente.setNome(nome);
//            cliente.setCidade(cidade);
//            presenter.cadastrar(cliente);
//            disabledCampos();
//        } else {
//            Utils.showMessage(this, getString(R.string.cliente_erro_cadastro), 0);
//        }
//    }
//
//    @Override
//    public void showMessage(String mensagem, int codigo) {
//        Utils.showMessage(this, mensagem, codigo);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        presenter.destroyView();
//    }
}
