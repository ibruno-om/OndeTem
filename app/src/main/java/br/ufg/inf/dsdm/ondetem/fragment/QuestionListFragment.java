package br.ufg.inf.dsdm.ondetem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.dsdm.ondetem.R;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;
import br.ufg.inf.dsdm.ondetem.viewholder.PerguntaViewHolder;

/**
 * Created by ibruno on 08/07/17.
 */

public abstract class QuestionListFragment extends Fragment implements ValueEventListener {

    private DatabaseReference mDatabase;
    private ArrayAdapter<Pergunta> mAdapter;
    private ListView mQuestionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAdapter = new ArrayAdapter<Pergunta>(getContext(), android.R.layout.simple_list_item_1,
                new ArrayList<Pergunta>());

        mQuestionList = (ListView) view.findViewById(R.id.questionList);

        mQuestionList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Pergunta> perguntas = new ArrayList<Pergunta>();
        mAdapter.clear();

        if (dataSnapshot.exists()) {
            for(DataSnapshot child : dataSnapshot.getChildren()){
                perguntas.add( child.getValue(Pergunta.class));
            }
        }

        mAdapter.addAll(perguntas);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public abstract Query getQuery(DatabaseReference databaseReference);


}