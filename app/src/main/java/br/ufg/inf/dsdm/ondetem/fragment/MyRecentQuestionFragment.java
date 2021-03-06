package br.ufg.inf.dsdm.ondetem.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufg.inf.dsdm.ondetem.QuestionActivity;
import br.ufg.inf.dsdm.ondetem.R;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

/**
 * Created by ibruno on 08/07/17.
 */

public class MyRecentQuestionFragment extends Fragment {

    private ArrayAdapter<Pergunta> mAdapter;
    private ListView mQuestionList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        List<String> questions = new ArrayList<String>(sharedPref.getStringSet(getResources()
                .getString(R.string.recent_question_lits), new HashSet<String>()));

        List<Pergunta> perguntas = new ArrayList<Pergunta>();

        Collections.sort(questions, Collections.<String>reverseOrder());

        for (String question : questions) {
            String pergunta = question.substring(question.lastIndexOf(";") + 1);

            perguntas.add(new Pergunta(pergunta));
        }

        mAdapter = new ArrayAdapter<Pergunta>(getContext(), android.R.layout.simple_list_item_1,
                new ArrayList<Pergunta>(perguntas));

        mQuestionList = (ListView) view.findViewById(R.id.questionList);

        TextView empty = (TextView) view.findViewById(R.id.questionListEmpty);

        empty.setText(getResources().getText(R.string.questionRecentListEmpty));

        mQuestionList.setEmptyView(empty);

        mQuestionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pergunta pergunta = (Pergunta) parent.getItemAtPosition(position);

                Intent intent = new Intent(getActivity(), QuestionActivity.class);
                intent.putExtra("question", pergunta);

                startActivity(intent);
            }
        });

        mQuestionList.setAdapter(mAdapter);

        return view;
    }


}
