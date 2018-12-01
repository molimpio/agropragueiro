package br.net.olimpiodev.agropragueiro.fragment.Cadastro;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.net.olimpiodev.agropragueiro.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AmostragemCadastroFragment extends Fragment {


    public AmostragemCadastroFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_amostragem_cadastro, container, false);
    }

}
