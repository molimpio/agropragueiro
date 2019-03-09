package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.ClienteCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ClienteCadastroPresenter implements ClienteCadastroContrato.ClienteCadastroPresenter {

    private ClienteCadastroContrato.ClienteCadastroView view;
    private Context context;
    private EditText etNomeCliente, etCidadeCliente;
    private Spinner spUfCliente;
    private Cliente cliente;
    private String ufs[];
    private String dadosCidades[];
    private AlertDialog alertDialog;

    public ClienteCadastroPresenter(ClienteCadastroContrato.ClienteCadastroView view, Context context) {
        this.view = view;
        this.context = context;
        this.cliente = new Cliente();
        this.dadosCidades = context.getResources().getStringArray(R.array.cidades);
    }

    @Override
    public void exibirView(Cliente cliente) {
        try {
            View dialogView = View.inflate(context, R.layout.dialog_cadastro_cliente, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(context.getString(R.string.cadastrar_cliente));

            etNomeCliente = dialogView.findViewById(R.id.et_nome_cliente);
            etCidadeCliente = dialogView.findViewById(R.id.et_cidade_cliente);
            spUfCliente = dialogView.findViewById(R.id.sp_uf_cliente);

            Button btCadastrarCliente = dialogView.findViewById(R.id.bt_cadastrar_cliente);
            btCadastrarCliente.setOnClickListener(v-> validarDados());

            startSpinners();
            getArgumentos(cliente);

            alertDialogBuilder.setView(dialogView);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_view_clientes), 0);
        }
    }

    private void getArgumentos(Cliente clienteView) {
        if (clienteView != null) {
            cliente = clienteView;
            etNomeCliente.setText(cliente.getNome());
            etCidadeCliente.setText(cliente.getCidade());
            spUfCliente.setSelection(Utils.getIndex(ufs, cliente.getUf()));
        } else {
            cliente.setId(0);
        }
    }

    private void validarDados() {
        try {
            String nome = etNomeCliente.getText().toString().trim().toUpperCase();
            String cidade = etCidadeCliente.getText().toString().trim().toUpperCase();

            if (nome.length() > 3 && cidade.length() > 1) {
                cliente.setUf(spUfCliente.getSelectedItem().toString().toUpperCase());
                cliente.setNome(nome);
                cliente.setCidade(cidade);
                getLatLongByCidade();
                cadastrar(cliente);
            } else {
                Utils.showMessage(context, context.getString(R.string.cliente_validacao), 0);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_validacao_cliente), 0);
        }
    }

    private void startSpinners() {
        try {
            ufs = context.getResources().getStringArray(R.array.estados);
            ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(Objects.requireNonNull(context),
                    android.R.layout.simple_spinner_item, ufs);

            spUfCliente.setAdapter(adapterUfs);
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_dados_cadastro_clientes), 0);
        }
    }

    private void getLatLongByCidade() {

        String cidade = cliente.getCidade();
        String uf = cliente.getUf();
        double lat = 0.0;
        double lng = 0.0;

        for (String dadoCidade: dadosCidades) {
            String[] dados = dadoCidade.trim().split("\\|");
            String cidadeNome = dados[2];
            String ufNome = dados[3];

            if (ufNome.trim().equals(uf)) {
                if (cidadeNome.trim().equals(cidade)){
                    lat = Double.valueOf(dados[0]);
                    lng = Double.valueOf(dados[1]);
                    break;
                }
            }
        }
        cliente.setLatitude(lat);
        cliente.setLongitude(lng);
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrar(Cliente cliente) {
        try {
            new AsyncTask<Void, Void, List<Cliente>>() {
                @Override
                protected List<Cliente> doInBackground(Void... voids) {
                    AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
                    List<Usuario> usuario = db.usuarioDao().getUsuario();
                    cliente.setUsuarioId(usuario.get(0).getId());

                    if (cliente.getId() == 0) db.clienteDao().insert(cliente);
                    else db.clienteDao().update(cliente);
                    return db.clienteDao().getClientes(true);
                }

                @Override
                protected void onPostExecute(List<Cliente> clientes) {
                    alertDialog.cancel();
                    view.atualizarAdapter(clientes);
                    Utils.showMessage(context, "", 1);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_cadastrar_cliente), 0);
        }
    }
}
