package br.ufg.inf.dsdm.ondetem;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import br.ufg.inf.dsdm.ondetem.model.Pergunta;

public class QuestionActivity extends AppCompatActivity {

    private TextView mQuestionContent;
    private TextView mQuestionAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mQuestionContent = (TextView) findViewById(R.id.questionContent);
        mQuestionAuthor = (TextView) findViewById(R.id.questionAuthor);

        Pergunta pergunta = (Pergunta) getIntent().getSerializableExtra("question");

        mQuestionContent.setText(pergunta.getConteudo());

    }
}
