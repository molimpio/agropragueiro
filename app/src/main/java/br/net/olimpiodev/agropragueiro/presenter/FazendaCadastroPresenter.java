package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.FazendaCadastroContrato;
import br.net.olimpiodev.agropragueiro.model.ChaveValor;
import br.net.olimpiodev.agropragueiro.model.Cliente;
import br.net.olimpiodev.agropragueiro.model.Fazenda;
import br.net.olimpiodev.agropragueiro.model.FazendaCliente;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class FazendaCadastroPresenter implements FazendaCadastroContrato.FazendaCadastroPresenter {

    private FazendaCadastroContrato.FazendaCadastroView view;
    private Context context;
    private AppDatabase db;
    private EditText etNomeFaz, etCidadeFaz;
    private Spinner spUfFaz, spClienteFaz;
    private List<ChaveValor> clienteList;
    private Fazenda fazenda;
    private FazendaCliente fazendaView;
    private AlertDialog alertDialog;

    public FazendaCadastroPresenter(FazendaCadastroContrato.FazendaCadastroView view, Context context) {
        this.view = view;
        this.context = context;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
        this.fazenda = new Fazenda();
        this.fazendaView = new FazendaCliente();
    }

    @Override
    public void exibirView(FazendaCliente fazendaCliente) {
        try {
            View dialogView = View.inflate(context, R.layout.dialog_cadastro_fazenda, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(context.getString(R.string.cadastrar_fazenda));

            etNomeFaz = dialogView.findViewById(R.id.et_nome_faz);
            etCidadeFaz = dialogView.findViewById(R.id.et_cidade_faz);
            spUfFaz = dialogView.findViewById(R.id.sp_uf_faz);
            spClienteFaz = dialogView.findViewById(R.id.sp_cliente_faz);
            spClienteFaz.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    fazenda.setClienteId(clienteList.get(position).getChave());
                    getClienteById(fazenda.getClienteId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            Button btnCadastrarFaz = dialogView.findViewById(R.id.bt_cadastrar_faz);
            btnCadastrarFaz.setOnClickListener(view1 -> validarDados());

            fazendaView = fazendaCliente;
            getClientes();

            alertDialogBuilder.setView(dialogView);
            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_view_fazendas), 0);
        }
    }

    private void getArgumentos() {
        if (fazendaView != null) {
            Utils.logar(fazendaView.toString());
            fazenda.setId(fazendaView.getIdFazenda());
            fazenda.setNome(fazendaView.getNomeFazenda());
            fazenda.setUf(fazendaView.getUfFazenda());
            fazenda.setCidade(fazendaView.getCidadeFazenda());
            fazenda.setClienteId(fazendaView.getIdCliente());

            etNomeFaz.setText(fazenda.getNome());
            etCidadeFaz.setText(fazenda.getCidade());

            spClienteFaz.setSelection(Utils.getIndexChaveValor(clienteList, fazendaView.getNomeCliente()));
            spUfFaz.setSelection(Utils.getIndex(context.getResources().getStringArray(R.array.estados), fazenda.getUf()));
        } else {
            fazenda.setId(0);
        }
    }

    private void validarDados() {
        try {
            String fazendaNome = etNomeFaz.getText().toString().trim().toUpperCase();
            String cidade = etCidadeFaz.getText().toString().trim().toUpperCase();
            String uf = spUfFaz.getSelectedItem().toString().toUpperCase();

            if (fazendaNome.length() > 3 && cidade.length() > 1) {
                fazenda.setNome(fazendaNome);
                fazenda.setCidade(cidade);
                fazenda.setUf(uf);
                cadastrar(fazenda);
            } else {
                Utils.showMessage(context, context.getString(R.string.fazenda_validacao), 0);
            }
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.fazenda_erro_validacao), 0);
        }
    }

    private void getDadosCliente(Cliente cliente) {
        spUfFaz.setSelection(Utils.getIndex(context.getResources()
                .getStringArray(R.array.estados), cliente.getUf()));
        etCidadeFaz.setText(cliente.getCidade());
    }

    private void startSpinners(List<ChaveValor> clientes) {
        try {
            clienteList = clientes;

            String[] ufs = context.getResources().getStringArray(R.array.estados);
            ArrayAdapter<String> adapterUfs = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item, ufs);
            spUfFaz.setAdapter(adapterUfs);

            ArrayAdapter<ChaveValor> adapterClientes = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item, clientes);
            spClienteFaz.setAdapter(adapterClientes);

            getArgumentos();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_dados_cadastro_fazenda), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getClientes() {
        try {
            new AsyncTask<Void, Void, List<ChaveValor>>() {
                @Override
                protected List<ChaveValor> doInBackground(Void... voids) {
                    return db.clienteDao().getClientesDropDown(true);
                }

                @Override
                protected void onPostExecute(List<ChaveValor> clientes) {
                    startSpinners(clientes);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_dados_cadastro_fazenda), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void cadastrar(Fazenda fazenda) {
        try {
            new AsyncTask<Void, Void, List<FazendaCliente>>() {
                @Override
                protected List<FazendaCliente> doInBackground(Void... voids) {
                    if (fazenda.getId() == 0) db.fazendaDao().insert(fazenda);
                    else db.fazendaDao().update(fazenda);
                    return db.fazendaDao().getFazendasCliente(true);
                }

                @Override
                protected void onPostExecute(List<FazendaCliente> fazendas) {
                    alertDialog.cancel();
                    view.atualizarAdapter(fazendas);
                    Utils.showMessage(context, "", 1);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_cadastrar_fazenda), 0);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void getClienteById(int clienteId) {
        try {
            new AsyncTask<Void, Void, Cliente>() {
                @Override
                protected Cliente doInBackground(Void... voids) {
                    return db.clienteDao().getClienteById(clienteId);
                }

                @Override
                protected void onPostExecute(Cliente cliente) {
                    getDadosCliente(cliente);
                }
            }.execute();
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_buscar_dados_cliente), 0);
        }
    }
}
