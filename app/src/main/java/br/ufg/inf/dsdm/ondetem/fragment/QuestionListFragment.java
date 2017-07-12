package br.ufg.inf.dsdm.ondetem.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.dsdm.ondetem.QuestionActivity;
import br.ufg.inf.dsdm.ondetem.R;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

/**
 * Created by ibruno on 08/07/17.
 */

public abstract class QuestionListFragment extends Fragment implements ValueEventListener {

    private DatabaseReference mDatabase;
    private ArrayAdapter<Pergunta> mAdapter;
    private ListView mQuestionList;
    private String anonymousKey;
    private String uid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String uidKey = view.getResources().getString(R.string.uid_user_session);
        anonymousKey = view.getResources().getString(R.string.anonymous_user);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        uid = sharedPref.getString(uidKey, "");


        mAdapter = new ArrayAdapter<Pergunta>(getContext(), android.R.layout.simple_list_item_1,
                new ArrayList<Pergunta>());

        mQuestionList = (ListView) view.findViewById(R.id.questionList);

        mQuestionList.setEmptyView(view.findViewById(R.id.questionListEmpty));

        mQuestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pergunta pergunta = (Pergunta) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("question", pergunta);

                startActivity(intent);
            }
        });

        Query query = getQuery(mDatabase);

        query.addValueEventListener(this);

        mQuestionList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        List<Pergunta> perguntas = new ArrayList<Pergunta>();
        mAdapter.clear();

        if (dataSnapshot.exists()) {
            Log.d("CONSULTA", "Resultado: " + dataSnapshot);

            for (DataSnapshot child : dataSnapshot.getChildren()) {
                perguntas.add(child.getValue(Pergunta.class));
            }
        } else {
            emptyData();
        }

        mAdapter.addAll(perguntas);
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }

    public String getUid() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        return !TextUtils.isEmpty(uid) ? uid : user != null ? user.getUid() :
                anonymousKey;

    }

    public abstract Query getQuery(DatabaseReference databaseReference);

    public abstract void emptyData();


}
