package br.ufg.inf.dsdm.ondetem;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import br.ufg.inf.dsdm.ondetem.helpers.PerguntaHelper;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

public class HomeActivity extends AppCompatActivity {

    private ListView mQuestionList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private PerguntaHelper perguntaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mQuestionList = (ListView) findViewById(R.id.questionList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword("ibruno.om@gmail.com", "senha321");

        perguntaHelper = new PerguntaHelper();

        //Iniciaiza a sessão de usuário
        initUser();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
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

    private void initUser() {
        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {

                user = firebaseAuth.getCurrentUser();

                if (user != null) {

                    List<Pergunta> perguntas = perguntaHelper.listarPeguntasUsuario(user.getUid());

                    ArrayAdapter adapter = new ArrayAdapter<Pergunta>(HomeActivity.this,
                            android.R.layout.simple_list_item_1, perguntas);

                    mQuestionList.setAdapter(adapter);
                }

            }
        };
    }
}
