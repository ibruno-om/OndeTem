package br.ufg.inf.dsdm.ondetem.helpers;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.dsdm.ondetem.model.Pergunta;

/**
 * Created by ibruno on 06/07/17.
 */

public class PerguntaHelper {


    FirebaseDatabase database;
    DatabaseReference reference;

    public PerguntaHelper() {
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference();
        ;
    }

    public List<String> findAllQuestions(String query) {

        final List<String> questions = new ArrayList<String>();

        DatabaseReference reference = database.getReference();

        Query questionQuery = reference.child("pergunta").orderByChild("conteudo").startAt(query);

        questionQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("CONSULTA", "Agora vai" + dataSnapshot.getValue());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return questions;

    }

    public List<Pergunta> listarPeguntasUsuario(String UID) {
        final List<Pergunta> perguntas = new ArrayList<Pergunta>();

        reference.child("user").child(UID).child("pergunta")
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                        perguntas.clear();

                        for (DataSnapshot child : children) {
                            Pergunta pergunta = child.getValue(Pergunta.class);
                            perguntas.add(pergunta);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        return perguntas;
    }

}
