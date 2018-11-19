package br.net.olimpiodev.agropragueiro.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import br.net.olimpiodev.agropragueiro.R;
import br.net.olimpiodev.agropragueiro.dao.UsuarioDao;
import br.net.olimpiodev.agropragueiro.model.Usuario;
import br.net.olimpiodev.agropragueiro.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private EditText etNomeCompleto, etEmail, etSenha;
    private Button btnLogin;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = new Usuario();

        etNomeCompleto = findViewById(R.id.et_nome_usuario);
        etEmail = findViewById(R.id.et_email_usuario);
        etSenha = findViewById(R.id.et_senha_usuario);

        etNomeCompleto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validarCampos();
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validarCampos();
            }
        });

        etSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                validarCampos();
            }
        });

        btnLogin = findViewById(R.id.bt_login);
        btnLogin.setOnClickListener(view -> cadastrar() );
    }

    private void validarCampos() {
        String nome = etNomeCompleto.getText().toString().toUpperCase();
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        if (nome.length() > 5 && Utils.validateEmail(email) && senha.length() > 5) {
            usuario.setNome(nome);
            usuario.setEmail(email);
            usuario.setSenha(senha);
            btnLogin.setEnabled(true);
        } else {
            btnLogin.setEnabled(false);
        }
    }

    private void cadastrar() {
        UsuarioDao usuarioDao = new UsuarioDao();
        usuarioDao.salvar(usuario);
        Utils.showMessage(getApplicationContext(), "", 1);
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }
}
