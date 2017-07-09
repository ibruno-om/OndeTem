package br.ufg.inf.dsdm.ondetem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufg.inf.dsdm.ondetem.fragment.MyRecentQuestionFragment;
import br.ufg.inf.dsdm.ondetem.fragment.SearchQuestionsFragment;
import br.ufg.inf.dsdm.ondetem.helpers.PerguntaHelper;
import br.ufg.inf.dsdm.ondetem.model.Pergunta;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private PerguntaHelper perguntaHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.nav_action);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);

        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        setQuestionFragment(new MyRecentQuestionFragment());


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
                registerRecentQuestion(query);
                SearchQuestionsFragment searchQuestionsFragment = new SearchQuestionsFragment();
                searchQuestionsFragment.setQuery(query);
                setQuestionFragment(searchQuestionsFragment);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(HomeActivity.this, "Fechou", Toast.LENGTH_SHORT).show();
                setQuestionFragment(new MyRecentQuestionFragment());
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setQuestionFragment(Fragment questionFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ftransaction = fragmentManager.beginTransaction();
        ftransaction.replace(R.id.questionListFrame, questionFragment);
        ftransaction.commit();
    }

    private void registerRecentQuestion(String query) {
        String key = getResources().getString(R.string.recent_question_lits);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

        Set<String> questions = sharedPref.getStringSet(key, new HashSet<String>());

        questions.add(query);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
        editor.putStringSet(key, questions);
        editor.commit();

    }


}
