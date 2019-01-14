package br.net.olimpiodev.agropragueiro.service.amostragem;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class ColetarDadosService {

    public static void coletarDadosDialog(Context context) {

        ArrayList<String> pragas = new ArrayList<String>();
        pragas.add("Meloidogyne");
        pragas.add("Pratylenchus");
        pragas.add("Radopholus");
        pragas.add("Aphelenchoides");

        View dialogView = View.inflate(context, R.layout.dialog_coletar_dados, null);

        AlertDialog alertDialog;

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle(context.getString(R.string.coletar_dados));


        Spinner spPragas = dialogView.findViewById(R.id.sp_pragas);
        ArrayAdapter<String> adapterPragas = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_dropdown_item, pragas);
        spPragas.setAdapter(adapterPragas);

        TextView tvDano = dialogView.findViewById(R.id.tv_dano);

        SeekBar sbDano = dialogView.findViewById(R.id.sb_dano);
        sbDano.setProgress(5);
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
    }
}
