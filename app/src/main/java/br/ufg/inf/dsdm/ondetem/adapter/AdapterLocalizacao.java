package br.ufg.inf.dsdm.ondetem.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import br.ufg.inf.dsdm.ondetem.R;
import br.ufg.inf.dsdm.ondetem.model.Localizacao;

/**
 * Created by ibruno on 12/07/17.
 */

public class AdapterLocalizacao extends ArrayAdapter<Localizacao> {

    public AdapterLocalizacao(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public AdapterLocalizacao(Context context, int resource, List<Localizacao> items) {
        super(context, resource, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.list_location, null);
        }

        Localizacao localizacao = getItem(position);

        if (localizacao != null) {
            TextView mNomeLugar = (TextView) view.findViewById(R.id.listLocationName);
            TextView mDistanciaLugar = (TextView) view.findViewById(R.id.listLocationDistance);


            mNomeLugar.setText(localizacao.getNome());
            mDistanciaLugar.setText("0 m");
        }


        return view;
    }
}
