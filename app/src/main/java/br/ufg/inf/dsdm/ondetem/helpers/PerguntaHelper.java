package br.ufg.inf.dsdm.ondetem.helpers;

import android.text.TextUtils;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import br.ufg.inf.dsdm.ondetem.model.Pergunta;

/**
 * Created by ibruno on 06/07/17.
 */


public class PerguntaHelper {


    private FirebaseDatabase database;
    private DatabaseReference reference;

    public PerguntaHelper() {
        this.database = FirebaseDatabase.getInstance();
        this.reference = database.getReference();
        ;
    }

    @Deprecated
    public List<Pergunta> listarPeguntasUsuario(String UID) {
        final List<Pergunta> perguntas = new ArrayList<Pergunta>();

        reference.child("user").child(UID).child("pergunta").orderByChild("conteudo")
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

    public String getUUID(Pergunta pergunta) {
        if (pergunta != null && !TextUtils.isEmpty(pergunta.getConteudo())) {
            return UUID.nameUUIDFromBytes(pergunta.getConteudo().toUpperCase().getBytes()).toString();
        } else {
            return null;
        }
    }

}
