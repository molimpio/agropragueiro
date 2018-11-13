package br.net.olimpiodev.agropragueiro.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import br.net.olimpiodev.agropragueiro.R;

public class Utils {

    public static void showMessage(Context context, String message, int codigo) {
        if (codigo == 1) message = context.getResources().getString(R.string.sucesso);
        if (codigo == 2) message = context.getResources().getString(R.string.erro);
        if (codigo == 3) message = context.getResources().getString(R.string.cancelada);

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static String getDataNow() {
        Calendar c = Calendar.getInstance();
        Date data = c.getTime();
        Locale brasilLocale = new Locale("pt", "BR");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", brasilLocale);
        return sdf.format(data);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
