package br.ufg.inf.dsdm.ondetem.fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by ibruno on 08/07/17.
 */

public class SearchQuestionsFragment extends QuestionListFragment {

    private String query;

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("pergunta").orderByChild("conteudo").startAt(this.query)
                .endAt(this.query + "\\uf8ff");
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
