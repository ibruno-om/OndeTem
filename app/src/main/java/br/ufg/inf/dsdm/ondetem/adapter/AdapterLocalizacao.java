package br.ufg.inf.dsdm.ondetem.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

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


}
