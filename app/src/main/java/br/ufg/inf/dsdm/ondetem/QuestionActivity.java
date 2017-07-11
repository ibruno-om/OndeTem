package br.ufg.inf.dsdm.ondetem;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import br.ufg.inf.dsdm.ondetem.helpers.PerguntaHelper;
import br.ufg.inf.dsdm.ondetem.model.Localizacao;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

public class QuestionActivity extends AppCompatActivity {

    private TextView mQuestionContent;
    private TextView mQuestionAuthor;
    private FloatingActionButton mFabAddLocation;
    private int PLACE_PICKER_REQUEST = 1;
    private PerguntaHelper perguntaHelper;

    private Pergunta pergunta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        perguntaHelper = new PerguntaHelper();

        mQuestionContent = (TextView) findViewById(R.id.questionContent);
        mQuestionAuthor = (TextView) findViewById(R.id.questionAuthor);
        mFabAddLocation = (FloatingActionButton) findViewById(R.id.fabAddLocation);

        pergunta = (Pergunta) getIntent().getSerializableExtra("question");

        mQuestionContent.setText(pergunta.getConteudo() + "Agora vai");

        mFabAddLocation.setOnClickListener(new View.OnClickListener() {
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
                String toastMsg = String.format("Place: %s", place.getName() + " " + place.getId());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();


                String uuid = perguntaHelper.getUUID(pergunta);

                DatabaseReference mPlace = FirebaseDatabase.getInstance()
                        .getReference("pergunta/" + uuid + "/" + place.getId());

                Localizacao localizacao = new Localizacao(place.getId(), place.getLatLng().latitude,
                        place.getLatLng().longitude);

                mPlace.setValue(localizacao);


            }
        }
    }
}
