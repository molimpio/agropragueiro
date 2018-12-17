package br.net.olimpiodev.agropragueiro.service;

import android.graphics.Color;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.net.olimpiodev.agropragueiro.utils.Utils;

public class MapaService {

    public static LatLng primeiraCoordenada = null;

    public static PolylineOptions coordenadasStringToList(String contorno) {
        try {
            JSONArray jsonArray = new JSONArray(contorno);
            PolylineOptions polylineOptions = new PolylineOptions();

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = new JSONObject(jsonArray.get(i).toString());

                Double lat = Double.parseDouble(jsonObject.getString("latitude"));
                Double lng = Double.parseDouble(jsonObject.getString("longitude"));

                if (i == 0) {
                    MapaService.primeiraCoordenada = new LatLng(lat, lng);
                }

                LatLng coordenada = new LatLng(lat, lng);
                polylineOptions.color(Color.RED);
                polylineOptions.add(coordenada);

                if ((i + 1) == jsonArray.length()) {
                    polylineOptions.add(MapaService.primeiraCoordenada);
                }
            }
            return polylineOptions;
        } catch (JSONException e) {
            Utils.logar(e.getMessage());
            return null;
        }
    }
}
