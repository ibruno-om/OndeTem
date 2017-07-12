package br.ufg.inf.dsdm.ondetem;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.ufg.inf.dsdm.ondetem.fragment.MyRecentQuestionFragment;
import br.ufg.inf.dsdm.ondetem.fragment.SearchQuestionsFragment;
import br.ufg.inf.dsdm.ondetem.helpers.PerguntaHelper;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private Toolbar mToolbar;

    private FirebaseAuth mAuth;
    private PerguntaHelper perguntaHelper;

    private View headerView;
    private TextView navUsername;
    private FirebaseUser mUser;

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

        updateUI();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("MENU", "Foi");


                switch (item.getItemId()) {
                    case R.id.login:

                        Intent intentLogin = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intentLogin);

                        break;
                    case R.id.myQuestions:

                        Intent intentMyQuestion = new Intent(HomeActivity.this, MyQuestionActivity.class);
                        startActivity(intentMyQuestion);

                        break;

                    case R.id.logout:

                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.clear();
                        editor.commit();
                        mAuth.signOut();
                        Toast.makeText(HomeActivity.this, "Seu usuário foi desconetado!",
                                Toast.LENGTH_SHORT).show();
                        updateUI();

                        break;
                }

                mDrawerLayout.closeDrawers();

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

        final SearchView search = (SearchView) item.getActionView();
        search.setQueryHint(getResources().getString(R.string.app_name));

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query) && query.length() >= 3) {
                    registerRecentQuestion(query);
                    SearchQuestionsFragment searchQuestionsFragment = new SearchQuestionsFragment();
                    searchQuestionsFragment.setQuery(query);
                    searchQuestionsFragment.setInsert(true);
                    setQuestionFragment(searchQuestionsFragment);
                    search.clearFocus();
                } else {
                    Toast.makeText(HomeActivity.this, "Quantidade insuficiente de caracteres!",
                            Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText) && newText.length() >= 3) {
                    SearchQuestionsFragment searchQuestionsFragment = new SearchQuestionsFragment();
                    searchQuestionsFragment.setQuery(newText);
                    setQuestionFragment(searchQuestionsFragment);
                }
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

        List<String> questions = new ArrayList<String>(sharedPref.getStringSet(key,
                new HashSet<String>()));

        questions.add(Calendar.getInstance().getTimeInMillis() + ";" + query);

        if (!questions.isEmpty()) {

            Collections.sort(questions, Collections.<String>reverseOrder());

            if (questions.size() > 10){
                questions.remove(10);
            }


        }


        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(key);
        editor.commit();
        editor.putStringSet(key, new HashSet<String>(questions));
        editor.commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();

    }

    private void updateUI() {
        mUser = mAuth.getCurrentUser();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        navUsername = (TextView) headerView.findViewById(R.id.textView);
        Menu navMenu = navigationView.getMenu();
        if (mUser != null) {
            navUsername.setText(mUser.getDisplayName());
            navMenu.findItem(R.id.myQuestions).setVisible(true);
            navMenu.findItem(R.id.minhaConta).setVisible(true);
            navMenu.findItem(R.id.login).setVisible(false);
            navMenu.findItem(R.id.logout).setVisible(true);
        } else {
            navUsername.setText("Não registrado");
            navMenu.findItem(R.id.myQuestions).setVisible(false);
            navMenu.findItem(R.id.minhaConta).setVisible(false);
            navMenu.findItem(R.id.login).setVisible(true);
            navMenu.findItem(R.id.logout).setVisible(false);
        }

    }


}
