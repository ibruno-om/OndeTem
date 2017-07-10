package br.ufg.inf.dsdm.ondetem.fragment;

import android.text.TextUtils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.UUID;

import br.ufg.inf.dsdm.ondetem.model.Pergunta;

/**
 * Created by ibruno on 08/07/17.
 */

public class SearchQuestionsFragment extends QuestionListFragment {

    private String query;

    private boolean insert;

    @Override
    public Query getQuery(DatabaseReference databaseReference) {
        if (isInsert()){
            return databaseReference.child("pergunta").orderByChild("conteudo").equalTo(query);
        } else {
            return databaseReference.child("pergunta").orderByChild("conteudo").startAt(this.query)
                    .endAt(this.query + "\\uf8ff");
        }
    }

    @Override
    public void emptyData() {
        if (!TextUtils.isEmpty(this.query) && isInsert()) {
            String uid = getUid();

            DatabaseReference mQuestionRef = FirebaseDatabase.getInstance().getReference("pergunta");

            Pergunta pergunta = new Pergunta(query);

            String questionKey = UUID.nameUUIDFromBytes(query.toUpperCase().getBytes()).toString();
            mQuestionRef.child(questionKey).setValue(pergunta);

            DatabaseReference mUserRef = FirebaseDatabase.getInstance().getReference("user")
                    .child(uid).child("pergunta").child(questionKey);

            mUserRef.setValue(pergunta);


        }
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public boolean isInsert() {
        return insert;
    }

    public void setInsert(boolean insert) {
        this.insert = insert;
    }
}
