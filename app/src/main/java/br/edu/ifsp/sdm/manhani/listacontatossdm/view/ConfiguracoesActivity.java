package br.edu.ifsp.sdm.manhani.listacontatossdm.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.security.Permission;
import java.time.Duration;

import br.edu.ifsp.sdm.manhani.listacontatossdm.R;
import br.edu.ifsp.sdm.manhani.listacontatossdm.util.Configuracoes;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ConfiguracoesActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSAO_ARMAZENAMENTO_EXTERNO_REQUEST_CODE = 0;
    @BindView(R.id.armazenamentoRadioGroup)
    RadioGroup armazenamentoRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        ButterKnife.bind(this);
        getSupportActionBar().setSubtitle("Configurações");

        switch (Configuracoes.getInstance().getTipoArmazenamento()) {
            case Configuracoes.ARMAZENAMENTO_INTERNO:
                armazenamentoRadioGroup.check(R.id.internoRadioButton);
                break;
            case Configuracoes.ARMAZENAMENTO_EXTERNO:
                armazenamentoRadioGroup.check(R.id.externoRadioButton);
                break;
            case Configuracoes.BANCO_DE_DADOS:
                armazenamentoRadioGroup.check(R.id.bdRadioButton);
                break;
        }

        armazenamentoRadioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
            if (id == R.id.externoRadioButton) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    int pLeitura = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                    int pEscrita = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    if (pLeitura != PackageManager.PERMISSION_GRANTED || pEscrita != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSAO_ARMAZENAMENTO_EXTERNO_REQUEST_CODE);
                    }
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSAO_ARMAZENAMENTO_EXTERNO_REQUEST_CODE) {
            for (int i : grantResults) {
                if (i != PackageManager.PERMISSION_GRANTED) {
                    armazenamentoRadioGroup.check(R.id.internoRadioButton);
                    Toast.makeText(this, "Permissões não concedidas!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }
    }

    @OnClick({R.id.cancelarButton, R.id.salvarButton})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelarButton:
                finish();
                break;
            case R.id.salvarButton:
                int armazenamento = Configuracoes.getInstance().getTipoArmazenamento();
                switch (armazenamentoRadioGroup.getCheckedRadioButtonId()) {
                    case R.id.internoRadioButton:
                        armazenamento = Configuracoes.ARMAZENAMENTO_INTERNO;
                        break;
                    case R.id.externoRadioButton:
                        armazenamento = Configuracoes.ARMAZENAMENTO_EXTERNO;
                        break;
                    case R.id.bdRadioButton:
                        armazenamento = Configuracoes.BANCO_DE_DADOS;
                        break;
                }
                Configuracoes.getInstance().setTipoArmazenamento(armazenamento);
                finish();
                break;
        }
    }


}
