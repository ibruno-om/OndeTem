package br.ufg.inf.dsdm.ondetem;

import android.content.pm.PermissionGroupInfo;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import br.ufg.inf.dsdm.ondetem.helpers.PerguntaHelper;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

public class Home extends AppCompatActivity {

    private ListView questionList;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private PerguntaHelper perguntaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        questionList = (ListView) findViewById(R.id.questionList);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("ibruno.om@gmail.com", "senha321");

        perguntaHelper = new PerguntaHelper();


        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    List<Pergunta> perguntas = perguntaHelper.listarPeguntasUsuario(user.getUid());

                    ArrayAdapter adapter = new ArrayAdapter<Pergunta>(Home.this,
                            android.R.layout.simple_list_item_1, perguntas);

                    questionList.setAdapter(adapter);
                }

            }
        };


    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
