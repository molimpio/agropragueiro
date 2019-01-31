package br.net.olimpiodev.agropragueiro.presenter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.contracts.ColetarDadosContrato;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistro;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import livroandroid.lib.utils.SDCardUtils;

public class ColetarDadosPresenter implements ColetarDadosContrato.ColetarDadosPresenter {

    private ColetarDadosContrato.ColetarDadosView view;
    private Context context;
    private Activity activity;
    private LatLng pponto;
    private int pontoAmostragemId;
    private AppDatabase db;
    private EditText etPraga, etQtde;
    private TextView tvDano;
    private SeekBar sbDano;
    private Button btRegistraFotos, btSalvar, btNovo;
    private PontoAmostragemRegistro pontoAmostragemRegistro;

    public ColetarDadosPresenter(ColetarDadosContrato.ColetarDadosView view, Context context,
                                 LatLng pponto, int pontoAmostragemId, Activity activity) {
        this.view = view;
        this.context = context;
        this.pponto = pponto;
        this.pontoAmostragemId = pontoAmostragemId;
        this.activity = activity;
        this.db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
        this.pontoAmostragemRegistro = new PontoAmostragemRegistro();
    }

    @Override
    public void exibirView() {
        try {
            View dialogView = View.inflate(context, R.layout.dialog_coletar_dados, null);
            AlertDialog alertDialog;

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle(context.getString(R.string.coletar_dados));

            etPraga = dialogView.findViewById(R.id.et_praga);
            etQtde = dialogView.findViewById(R.id.et_qtde);
            tvDano = dialogView.findViewById(R.id.tv_dano);

            sbDano = dialogView.findViewById(R.id.sb_dano);
            sbDano.setProgress(50);
            tvDano.setText(context.getString(R.string.cinquenta));

            sbDano.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tvDano.setText(progress + "");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            alertDialogBuilder.setView(dialogView);

            alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            Button btCancelar = dialogView.findViewById(R.id.bt_cancelar_registrar);
            btCancelar.setOnClickListener(v -> alertDialog.dismiss());

            btRegistraFotos = dialogView.findViewById(R.id.bt_registrar_fotos);
            btRegistraFotos.setOnClickListener(v -> registrarFotos());

            btNovo = dialogView.findViewById(R.id.bt_novo_registro);
            btNovo.setOnClickListener(v -> novoCadastro());

            btSalvar = dialogView.findViewById(R.id.bt_salvar_registro);
            btSalvar.setOnClickListener(v -> salvarPontoRegistro());
        } catch (Exception ex) {
            Utils.showMessage(context, context.getString(R.string.erro_carregar_view_coletar_dados), 0);
        }
    }

    private void novoCadastro() {
        etPraga.setEnabled(true);
        etQtde.setEnabled(true);
        sbDano.setEnabled(true);
        etPraga.setText("");
        etQtde.setText("");
        sbDano.setProgress(50);
        tvDano.setText(context.getString(R.string.cinquenta));
        btRegistraFotos.setEnabled(false);
        btNovo.setEnabled(false);
        btSalvar.setEnabled(true);
    }

    private boolean validarDadosPontoRegistro() {
        try {
            String praga = etPraga.getText().toString().trim().toUpperCase();
            String qtde = etQtde.getText().toString().trim();
            String danoCausado = tvDano.getText().toString().trim();

            if (praga.length() > 2 && !qtde.isEmpty()) {
                pontoAmostragemRegistro.setLatitude(pponto.latitude);
                pontoAmostragemRegistro.setLongitude(pponto.longitude);
                pontoAmostragemRegistro.setPraga(praga);
                pontoAmostragemRegistro.setQtde(Integer.parseInt(qtde));
                pontoAmostragemRegistro.setDanoCausado(Integer.parseInt(danoCausado));
                pontoAmostragemRegistro.setPontoAmostragemId(pontoAmostragemId);
                return true;
            } else {
                showToast(context.getString(R.string.validar_praga_qtde));
                return false;
            }
        } catch (Exception ex) {
            showToast(context.getString(R.string.erro_validacao_pontos_registro));
            return false;
        }
    }

    private void salvarPontoRegistro() {
        try {
            if (validarDadosPontoRegistro()) {
                salvarPontoAmostragemRegistro();
                disabledCampos();
            }
        } catch (Exception ex) {
            showToast(context.getString(R.string.erro_validacao_pontos_registro));
        }
    }

    private void disabledCampos() {
        etPraga.setEnabled(false);
        etQtde.setEnabled(false);
        sbDano.setEnabled(false);
        btSalvar.setEnabled(false);
        btRegistraFotos.setEnabled(true);
        btNovo.setEnabled(true);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 270);
        toast.show();
    }

    @SuppressLint("StaticFieldLeak")
    private void salvarPontoAmostragemRegistro() {
        try {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    db.pontoAmostragemRegistroDao().insert(pontoAmostragemRegistro);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    showToast(context.getString(R.string.sucesso));
                }
            }.execute();
        } catch (Exception ex) {
            Utils.logar(ex.getMessage());
            showToast(context.getString(R.string.erro_salvar_ponto_registro));
        }
    }

    private void registrarFotos() {
        try {
            File file = SDCardUtils.getPrivateFile(context, "foto.jpg", Environment.DIRECTORY_PICTURES);
            Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intentCamera, 0);
            // salvar em foto registro
            // nome (imagem_ponto_amostragem_registro_id)
            // path
        } catch (Exception ex) {
            showToast(context.getString(R.string.erro_salvar_foto_registro));
        }
    }
}
