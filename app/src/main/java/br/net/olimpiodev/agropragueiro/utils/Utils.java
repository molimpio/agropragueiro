package br.net.olimpiodev.agropragueiro.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.net.olimpiodev.agropragueiro.R;

public class Utils {

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static void showMessage(Context context, String message, int codigo) {
        if (codigo == 1) message = context.getResources().getString(R.string.sucesso);
        if (codigo == 2) message = context.getResources().getString(R.string.erro);
        if (codigo == 3) message = context.getResources().getString(R.string.cancelada);

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
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

    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static void logar(String texto) {
        Log.i("log", texto);
    }

    public static int getIndex(String array[], String search) {
        int index = 0;

        for (int i=0; i < array.length; i++) {
            if (array[i].equals(search)) {
                index = i;
                break;
            }
        }
        return index;
    }

}
