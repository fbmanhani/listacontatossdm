package br.edu.ifsp.sdm.manhani.listacontatossdm.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.sdm.manhani.listacontatossdm.R;
import br.edu.ifsp.sdm.manhani.listacontatossdm.adapter.ListaContatosAdapter;
import br.edu.ifsp.sdm.manhani.listacontatossdm.model.Contato;
import br.edu.ifsp.sdm.manhani.listacontatossdm.util.ArmazenamentoHelper;
import br.edu.ifsp.sdm.manhani.listacontatossdm.util.Configuracoes;
import br.edu.ifsp.sdm.manhani.listacontatossdm.util.JsonHelper;

public class ListaContatosActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private final int NOVO_CONTATO_REQUEST_CODE = 0;
    public static final String CONTATO_EXTRA = "CONTATO_EXTRA";
    private final String CONFIGURACOES_SHARED_PREFERENCES = "CONFIGURACOES";
    private final String TIPO_ARMAZENAMENTO_SHARED_PREFERENCES = "TIPO_ARMAZENAMENTO_SHARED_PREFERENCES";

    private ListView listaContatosListView;
    private List<Contato> listaContatos;
    private ListaContatosAdapter listaContatosAdapter;
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contato);
        listaContatosListView = findViewById(R.id.listaContatoListView);
        listaContatos = new ArrayList<>();
//        preencheListaContatos();

//        List<String> listaNomes = new ArrayList<>();
//        for (Contato contato : listaContatos) {
//            listaNomes.add(contato.getNome());
//        }
//
//        ArrayAdapter<String> listaContatosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNomes);
        listaContatosAdapter = new ListaContatosAdapter(this, listaContatos);
        listaContatosListView.setAdapter(listaContatosAdapter);
        listaContatosListView.setOnItemClickListener(this);
        registerForContextMenu(listaContatosListView);
        sharedPreferences = getSharedPreferences(CONFIGURACOES_SHARED_PREFERENCES, MODE_PRIVATE);
        restauraConfiguracoes();
        restauraContatos();
    }


    private void restauraConfiguracoes() {
        int tipoArmazenamento = sharedPreferences.getInt(TIPO_ARMAZENAMENTO_SHARED_PREFERENCES, Configuracoes.ARMAZENAMENTO_INTERNO);
        Configuracoes.getInstance().setTipoArmazenamento(tipoArmazenamento);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.configuracaoMenuItem:
                Intent configIntent = new Intent(this, ConfiguracoesActivity.class);
                startActivity(configIntent);
                return true;
            case R.id.novoContatoMenuItem:
                Intent novoContatoIntent = new Intent("XPTO");
                startActivityForResult(novoContatoIntent, NOVO_CONTATO_REQUEST_CODE);
                return true;
            case R.id.sairMenuItem:
                finish();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_contexto, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapter = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Contato contato = listaContatos.get(adapter.position);
        switch (item.getItemId()) {
            case R.id.editarContatoMenuItem:
                Intent novoContatoIntent = new Intent("XPTO");
                novoContatoIntent.putExtra("POSITION", adapter.position);
                novoContatoIntent.putExtra(CONTATO_EXTRA, contato);
                startActivityForResult(novoContatoIntent, NOVO_CONTATO_REQUEST_CODE);
                return true;
            case R.id.ligarContatoMenuItem:
                Uri ligarUri = Uri.parse("tel:" + contato.getTelefone());
                Intent ligacao = new Intent(Intent.ACTION_DIAL, ligarUri);
                startActivity(ligacao);
                return true;
            case R.id.enderecoContatoMenuItem:
                Uri endUri = Uri.parse("geo:0,0?q=" + contato.getEndereco());
                Intent endereco = new Intent(Intent.ACTION_VIEW, endUri);
                startActivity(endereco);
                return true;
            case R.id.enviarEmailMenuItem:
                Uri emailUri = Uri.parse("mailto:" + contato.getEmail());
                Intent email = new Intent(Intent.ACTION_SENDTO, emailUri);
                startActivity(email);
                return true;
            case R.id.removerContatoMenuItem:
                removerContato(adapter.position);
                return true;
        }
        return false;
    }

    private void removerContato(final int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage("Confirma remoção?");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listaContatos.remove(pos);
                listaContatosAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("Não", null);
        AlertDialog remover = builder.create();
        remover.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case NOVO_CONTATO_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Contato contato = (Contato) data.getSerializableExtra(CONTATO_EXTRA);
                    String msg = "Contato adicionado com sucesso!";
                    int position = data.getIntExtra("POSITION", -1);
                    if (position != -1) {
                        listaContatos.set(position, contato);
                        msg = "Contato alterado com sucesso!";
                    } else {
                        listaContatos.add(contato);
                    }
                    listaContatosAdapter.notifyDataSetChanged();
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    if (resultCode == RESULT_CANCELED) {
                        Toast.makeText(this, "Cadastro cancelado", Toast.LENGTH_SHORT).show();
                    }
                }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contato contato = listaContatos.get(position);
        Intent intent = new Intent("XPTO");
        intent.putExtra(CONTATO_EXTRA, contato);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        salvaConfiguracoes();
        salvarContatos();
    }

    private void salvarContatos() {
        try {
            JSONArray jsonArray = JsonHelper.listaContatosParaJsonArray(listaContatos);
            if (jsonArray != null) {
                ArmazenamentoHelper.salvarContatos(this, Configuracoes.getInstance().getTipoArmazenamento(), jsonArray);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }

    private void salvaConfiguracoes() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TIPO_ARMAZENAMENTO_SHARED_PREFERENCES, Configuracoes.getInstance().getTipoArmazenamento());
        editor.apply();
    }

    private void restauraContatos() {
        try {
            JSONArray jsonArray = ArmazenamentoHelper.buscarContatos(this, Configuracoes.getInstance().getTipoArmazenamento());
            if (jsonArray != null) {
                List<Contato> contatos = JsonHelper.jsonArrayParaListaContatos(jsonArray);
                listaContatos.addAll(contatos);
                listaContatosAdapter.notifyDataSetChanged();
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
    }
}
