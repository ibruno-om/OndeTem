package br.ufg.inf.dsdm.ondetem;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import br.ufg.inf.dsdm.ondetem.fragment.MyQuestionFragment;

public class MyQuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);

        MyQuestionFragment questionFragment = new MyQuestionFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ftransaction = fragmentManager.beginTransaction();
        ftransaction.replace(R.id.myQuestionListFrame, questionFragment);
        ftransaction.commit();
    }
}
