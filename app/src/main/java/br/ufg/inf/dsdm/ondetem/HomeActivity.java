package br.ufg.inf.dsdm.ondetem;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import br.ufg.inf.dsdm.ondetem.helpers.PerguntaHelper;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

public class HomeActivity extends AppCompatActivity {

    private ListView mQuestionList;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private ArrayAdapter<String> listResult;

    private Toolbar mToolbar;


    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private PerguntaHelper perguntaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mQuestionList = (ListView) findViewById(R.id.questionList);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("MENU", "Foi");

                switch (item.getItemId()) {
                    case R.id.login:

                        Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intentLogin);

                        break;
                    case R.id.logout:
                        mAuth.signOut();

                        break;
                }
                return false;
            }
        });


        perguntaHelper = new PerguntaHelper();

        //Iniciaiza a sessão de usuário
        initUser();


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.search_menu, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);

        SearchView search = (SearchView) item.getActionView();
        search.setQueryHint(getResources().getString(R.string.app_name));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(HomeActivity.this, query, Toast.LENGTH_SHORT).show();

                List<Pergunta> results = perguntaHelper.findAllQuestions(query);

                if (results != null) {
                    ArrayAdapter adapter = new ArrayAdapter<Pergunta>(HomeActivity.this,
                            android.R.layout.simple_list_item_1, results);

                    mQuestionList.setAdapter(adapter);
                } else {
                    mQuestionList.setAdapter(null);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
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
                } else {
                    mQuestionList.clearChoices();
                }

            }
        };
    }
}
