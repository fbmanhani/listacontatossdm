package br.edu.ifsp.sdm.manhani.listacontatossdm.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import br.edu.ifsp.sdm.manhani.listacontatossdm.R;
import br.edu.ifsp.sdm.manhani.listacontatossdm.model.Contato;

public class ContatoActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nomeEditText;
    private EditText enderecoEditText;
    private EditText emailEditText;
    private EditText telefoneEditText;
    private Button cancelarButton;
    private Button salvarButton;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contato);


        nomeEditText = findViewById(R.id.nomeEditText);
        enderecoEditText = findViewById(R.id.enderecoEditText);
        telefoneEditText = findViewById(R.id.telefoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        cancelarButton = findViewById(R.id.cancelarButton);
        salvarButton = findViewById(R.id.salvarButton);

        cancelarButton.setOnClickListener(this);
        salvarButton.setOnClickListener(this);

        String subtitulo;
        Contato contato = (Contato) getIntent().getSerializableExtra(ListaContatosActivity.CONTATO_EXTRA);
        position = getIntent().getIntExtra("POSITION", -1);
        boolean isEdicao = position > -1;
        if (contato != null) {
            subtitulo = isEdicao ? "Editar Contato" : "Detalhes do contato";
            preencheDetalhe(contato, isEdicao);
        } else {
            subtitulo = "Novo contato";
        }
        getSupportActionBar().setSubtitle(subtitulo);
    }

    private void preencheDetalhe(Contato contato, Boolean isEdicao) {
        nomeEditText.setText(contato.getNome());
        nomeEditText.setEnabled(isEdicao);
        enderecoEditText.setText(contato.getEndereco());
        enderecoEditText.setEnabled(isEdicao);
        telefoneEditText.setText(contato.getTelefone());
        telefoneEditText.setEnabled(isEdicao);
        emailEditText.setText(contato.getEmail());
        emailEditText.setEnabled(isEdicao);
        if (isEdicao) {
            salvarButton.setVisibility(View.VISIBLE);
        } else {
            salvarButton.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelarButton:
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.salvarButton:
                Contato contato = new Contato();
                contato.setNome(nomeEditText.getText().toString());
                contato.setEndereco(enderecoEditText.getText().toString());
                contato.setEmail(emailEditText.getText().toString());
                contato.setTelefone(telefoneEditText.getText().toString());
                Intent resultado = new Intent();
                resultado.putExtra(ListaContatosActivity.CONTATO_EXTRA, contato);
                resultado.putExtra("POSITION", position);
                setResult(RESULT_OK, resultado);
                finish();
                break;
            default:
                finish();
                break;
        }

    }
}
