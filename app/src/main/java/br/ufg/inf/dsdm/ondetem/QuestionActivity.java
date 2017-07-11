package br.ufg.inf.dsdm.ondetem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import br.ufg.inf.dsdm.ondetem.model.Pergunta;

public class QuestionActivity extends AppCompatActivity {

    private TextView mQuestionContent;
    private TextView mQuestionAuthor;
    private Button mbtnAddAnswer;
    private int PLACE_PICKER_REQUEST = 1;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        mQuestionContent = (TextView) findViewById(R.id.questionContent);
        mQuestionAuthor = (TextView) findViewById(R.id.questionAuthor);
        mbtnAddAnswer = (Button) findViewById(R.id.btnAddAnswer);

        Pergunta pergunta = (Pergunta) getIntent().getSerializableExtra("question");

        mQuestionContent.setText(pergunta.getConteudo() + "Agora vai");

        mbtnAddAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

                try {
                    Intent intent = builder.build(QuestionActivity.this);
                    startActivityForResult(intent, PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }


            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }
}
