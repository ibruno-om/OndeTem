package br.ufg.inf.dsdm.ondetem.helpers;

import android.widget.ArrayAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.dsdm.ondetem.Home;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

/**
 * Created by ibruno on 06/07/17.
 */

public class PerguntaHelper {

    public List<Pergunta> listarPeguntasUsuario(String UID){
        final List<Pergunta> perguntas = new ArrayList<Pergunta>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference();

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
