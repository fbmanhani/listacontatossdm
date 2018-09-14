package br.edu.ifsp.sdm.manhani.listacontatossdm.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.sdm.manhani.listacontatossdm.R;
import br.edu.ifsp.sdm.manhani.listacontatossdm.adapter.ListaContatosAdapter;
import br.edu.ifsp.sdm.manhani.listacontatossdm.model.Contato;

public class ListaContatosActivity extends AppCompatActivity {

    private ListView listaContatosListView;
    private List<Contato> listaContatos;
    private ListaContatosAdapter listaContatosAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_contato);
        listaContatosListView = findViewById(R.id.listaContatoListView);
        listaContatos = new ArrayList<>();
        preencheListaContatos();

//        List<String> listaNomes = new ArrayList<>();
//        for (Contato contato : listaContatos) {
//            listaNomes.add(contato.getNome());
//        }
//
//        ArrayAdapter<String> listaContatosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaNomes);
        listaContatosAdapter = new ListaContatosAdapter(this, listaContatos);
        listaContatosListView.setAdapter(listaContatosAdapter);
        registerForContextMenu(listaContatosListView);
    }

    private void preencheListaContatos() {
        for (int i = 0; i < 20; i++) {
            listaContatos.add(new Contato("C" + i, "Endereco" + i, "123123" + i, i + "email.com"));
        }
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
                return true;
            case R.id.novoContatoMenuItem:
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
                return true;
            case R.id.ligarContatoMenuItem:
                return true;
            case R.id.enderecoContatoMenuItem:
                return true;
            case R.id.enviarEmailMenuItem:
                return true;
            case R.id.removerContatoMenuItem:
                return true;
        }
        return false;
    }
}
