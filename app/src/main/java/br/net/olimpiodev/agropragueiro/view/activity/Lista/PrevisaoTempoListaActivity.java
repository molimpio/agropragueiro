package br.net.olimpiodev.agropragueiro.view.activity.Lista;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.adapter.PrevisaoTempoDadosAdapter;
import br.net.olimpiodev.agropragueiro.model.PrevisaoTempo;
import br.net.olimpiodev.agropragueiro.model.PrevisaoTempoDados;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class PrevisaoTempoListaActivity extends AppCompatActivity {

    private PrevisaoTempo previsaoTempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previsao_tempo_lista);

        String titulo = getString(R.string.previsao_tempo);
        if (getIntent().hasExtra(getString(R.string.dados_previsao_param))) {
            previsaoTempo = (PrevisaoTempo) getIntent().getSerializableExtra(getString(R.string.dados_previsao_param));
            inicializarPrevisaoTempoDados();
            titulo +=  ": " + previsaoTempo.getCidade();
        }

        Objects.requireNonNull(getSupportActionBar()).setTitle(titulo);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void inicializarPrevisaoTempoDados() {
        try {
            String dadosPrevisao = previsaoTempo.getPrevisao();
            JSONObject dados = new JSONObject(dadosPrevisao);
            JSONArray forecast = dados.getJSONArray("forecast");
            List<PrevisaoTempoDados> previsaoTempoDadosList = new ArrayList<>();

            for(int i=0; i < forecast.length(); i++) {
                JSONObject obj = forecast.getJSONObject(i);

                PrevisaoTempoDados previsaoTempoDados = new PrevisaoTempoDados();
                previsaoTempoDados.setDia(obj.getString("date"));
                previsaoTempoDados.setMin(obj.getString("min"));
                previsaoTempoDados.setMax(obj.getString("max"));
                previsaoTempoDados.setCondicao(obj.getString("description"));
                previsaoTempoDados.setImagemId(getDrawable(obj.getString("condition")));

                previsaoTempoDadosList.add(previsaoTempoDados);
            }

            setupRecyclerView(previsaoTempoDadosList);
        } catch (JSONException ex) {
            Utils.showMessage(getApplicationContext(), getApplicationContext().getString(R.string.erro_exibir_dados_previsao), 0);
        }
    }

    private int getDrawable(String condicao) {
        int drawable = 0;

        if (condicao.equals(getString(R.string.storm)) || condicao.equals(getString(R.string.rain))) {
            drawable = R.drawable.ic_storm_rain;
        } else if (condicao.equals(getString(R.string.snow))) {
            drawable = R.drawable.ic_snow;
        } else if (condicao.equals(getString(R.string.hail))) {
            drawable = R.drawable.ic_hail;
        } else if (condicao.equals(getString(R.string.fog))) {
            drawable = R.drawable.ic_fog;
        } else if (condicao.equals(getString(R.string.clear_day))) {
            drawable = R.drawable.ic_clear_day;
        } else if (condicao.equals(getString(R.string.clear_night))) {
            drawable = R.drawable.ic_clear_night;
        } else if (condicao.equals(getString(R.string.cloud))) {
            drawable = R.drawable.ic_cloud;
        } else if (condicao.equals(getString(R.string.cloudly_day))) {
            drawable = R.drawable.ic_cloudly_day;
        } else if (condicao.equals(getString(R.string.cloudly_night))) {
            drawable = R.drawable.ic_cloudly_night;
        } else {
            drawable = R.drawable.ic_cloudly_day;
        }
        return drawable;
    }

    private void setupRecyclerView(List<PrevisaoTempoDados> previsaoTempoDadosList) {
        RecyclerView rvPrevisaoTempo = findViewById(R.id.rv_previsao_tempo);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvPrevisaoTempo.setLayoutManager(layoutManager);

        PrevisaoTempoDadosAdapter previsaoTempoDadosAdapter = new PrevisaoTempoDadosAdapter(previsaoTempoDadosList);
        rvPrevisaoTempo.setAdapter(previsaoTempoDadosAdapter);
    }
}
