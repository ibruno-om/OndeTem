package br.ufg.inf.dsdm.ondetem.fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;

/**
 * Created by ibruno on 08/07/17.
 */

public class MyQuestionFragment extends QuestionListFragment {
    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        return databaseReference.child("user").child(getUid()).child("pergunta")
                .orderByChild("conteudo");
    }

    @Override
    public void emptyData() {
        //Do nothing
    }
}
