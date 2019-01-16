package br.net.olimpiodev.agropragueiro.service.amostragem;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.File;

import br.net.olimpiodev.agropragueiro.AppDatabase;
import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.model.PontoAmostragemRegistro;
import br.net.olimpiodev.agropragueiro.utils.Utils;
import livroandroid.lib.utils.SDCardUtils;

public class ColetarDadosService {

    public static void coletarDadosDialog(Context context, Activity activity,
                                          LatLng latLng, int pontoAmostragemId) {

        View dialogView = View.inflate(context, R.layout.dialog_coletar_dados, null);
        PontoAmostragemRegistro pontoAmostragemRegistro = new PontoAmostragemRegistro();
        AlertDialog alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.coletar_dados));

        EditText etPraga = dialogView.findViewById(R.id.et_praga);
        EditText etQtde = dialogView.findViewById(R.id.et_qtde);
        TextView tvDano = dialogView.findViewById(R.id.tv_dano);

        SeekBar sbDano = dialogView.findViewById(R.id.sb_dano);
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

        Button btSalvar = dialogView.findViewById(R.id.bt_salvar_registro);

        Button btCancelar = dialogView.findViewById(R.id.bt_cancelar_registrar);
        btCancelar.setOnClickListener(v -> alertDialog.dismiss());

        Button btRegistraFotos = dialogView.findViewById(R.id.bt_registrar_fotos);
        btRegistraFotos.setOnClickListener(v -> registrarFotos(context, activity));

        Button btNovo = dialogView.findViewById(R.id.bt_novo_registro);
        btNovo.setOnClickListener(v -> {
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
        });

        btSalvar.setOnClickListener(v -> {
            String praga = etPraga.getText().toString().trim().toUpperCase();
            String qtde = etQtde.getText().toString().trim();
            String danoCausado = tvDano.getText().toString().trim();

            if (praga.length() > 2 && !qtde.isEmpty()) {
                pontoAmostragemRegistro.setLatitude(latLng.latitude);
                pontoAmostragemRegistro.setLongitude(latLng.longitude);
                pontoAmostragemRegistro.setPraga(praga);
                pontoAmostragemRegistro.setQtde(Integer.parseInt(qtde));
                pontoAmostragemRegistro.setDanoCausado(Integer.parseInt(danoCausado));
                pontoAmostragemRegistro.setPontoAmostragemId(pontoAmostragemId);

                salvarFotos(pontoAmostragemRegistro, context, btSalvar, btRegistraFotos, btNovo);
                etPraga.setEnabled(false);
                etQtde.setEnabled(false);
                sbDano.setEnabled(false);
            } else {
                Toast toast = Toast.makeText(context, context.getString(R.string.validar_praga_qtde), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 270);
                toast.show();
            }
        });
    }

    private static void registrarFotos(Context context, Activity activity) {

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
            Utils.logar(ex.getMessage());
        }
    }

    @SuppressLint("StaticFieldLeak")
    private static void salvarFotos(PontoAmostragemRegistro pontoAmostragemRegistro, Context context,
                                    Button btSalvar, Button btRegistrar, Button btNovo) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, AppDatabase.DB_NAME).build();
                db.pontoAmostragemRegistroDao().insert(pontoAmostragemRegistro);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Toast toast = Toast.makeText(context, context.getString(R.string.sucesso), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 270);
                toast.show();

                btSalvar.setEnabled(false);
                btRegistrar.setEnabled(true);
                btNovo.setEnabled(true);
            }
        }.execute();
    }
}
