package br.net.olimpiodev.agropragueiro.view.activity.Lista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.net.olimpiodev.agropragueiro.R;

public class PontoAmostragemListaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto_amostragem_lista);

        getSupportActionBar().setTitle("Cadastro Venda");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
