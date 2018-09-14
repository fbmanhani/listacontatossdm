package br.edu.ifsp.sdm.manhani.listacontatossdm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifsp.sdm.manhani.listacontatossdm.R;
import br.edu.ifsp.sdm.manhani.listacontatossdm.model.Contato;

public class ListaContatosAdapter extends ArrayAdapter<Contato> {
    private LayoutInflater layoutInflater;

    public ListaContatosAdapter(Context context, List<Contato> listaContatos) {
        super(context, R.layout.layout_view_contato_adapter, listaContatos);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.layout_view_contato_adapter, null);
            holder = new Holder();
            holder.nomeTextView = convertView.findViewById(R.id.nomeContatoTextView);
            holder.emailTextView = convertView.findViewById(R.id.emailContatoTextView);
            convertView.setTag(holder);
        }
        Contato contato = getItem(position);
        holder = (Holder) convertView.getTag();
        holder.nomeTextView.setText(contato.getNome());
        holder.emailTextView.setText(contato.getEmail());

//        ((TextView) convertView).setText(contato.getNome());
        return convertView;
    }

    private class Holder {
        public TextView nomeTextView;
        public TextView emailTextView;
    }
}

