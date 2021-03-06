package br.ufg.inf.dsdm.ondetem;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.ufg.inf.dsdm.ondetem.adapter.AdapterLocalizacao;
import br.ufg.inf.dsdm.ondetem.helpers.PerguntaHelper;
import br.ufg.inf.dsdm.ondetem.model.Localizacao;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

public class QuestionActivity extends AppCompatActivity implements ValueEventListener {

    private TextView mQuestionContent;
    private FloatingActionButton mFabAddLocation;
    private int PLACE_PICKER_REQUEST = 1;
    private PerguntaHelper perguntaHelper;


    private DatabaseReference mDatabase;
    private AdapterLocalizacao mAdapter;

    private Pergunta pergunta;
    private ListView mLocationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        perguntaHelper = new PerguntaHelper();
        pergunta = (Pergunta) getIntent().getSerializableExtra("question");

        /* Listar localização */
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mAdapter = new AdapterLocalizacao(this, R.layout.list_location,
                new ArrayList<Localizacao>());

        mLocationList = (ListView) findViewById(R.id.locationList);

        Query query = getQuery(mDatabase);
        query.addValueEventListener(this);
        mLocationList.setAdapter(mAdapter);
        /* Listar localização */

        mQuestionContent = (TextView) findViewById(R.id.questionContent);
        mFabAddLocation = (FloatingActionButton) findViewById(R.id.fabAddLocation);

        mLocationList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Localizacao localizacao = (Localizacao) parent.getItemAtPosition(position);

                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?daddr=" + localizacao.getLatitude() +
                                "," + localizacao.getLongitude()));
                startActivity(intent);
            }
        });

        mLocationList.setEmptyView(findViewById(R.id.locationListEmpty));


        mQuestionContent.setText("Onde tem: " + pergunta.getConteudo() + "?");

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

    private Query getQuery(DatabaseReference mDatabase) {

        String uuid = perguntaHelper.getUUID(pergunta);
        return mDatabase.child("pergunta/" + uuid + "/localizacao")
                .orderByChild("timestamp");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);

                String uuid = perguntaHelper.getUUID(pergunta);

                DatabaseReference mPlace = FirebaseDatabase.getInstance()
                        .getReference("pergunta/" + uuid + "/localizacao/" + place.getId());

                Localizacao localizacao = new Localizacao(place.getId(), place.getLatLng().latitude,
                        place.getLatLng().longitude, Calendar.getInstance().getTime().getTime(),
                        (String) place.getName());

                mPlace.setValue(localizacao);


            }
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {

        List<Localizacao> localizacoes = new ArrayList<Localizacao>();
        mAdapter.clear();

        if (dataSnapshot.exists()) {
            Log.d("DADOS", dataSnapshot.toString());
            for (DataSnapshot child : dataSnapshot.getChildren()) {
                localizacoes.add(0, child.getValue(Localizacao.class));
            }
        } else {
            Log.d("NAO FOI", "DADO VAZIO");
        }

        mAdapter.addAll(localizacoes);

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
