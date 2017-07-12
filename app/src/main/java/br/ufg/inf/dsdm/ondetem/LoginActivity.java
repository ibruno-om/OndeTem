package br.ufg.inf.dsdm.ondetem;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashSet;
import java.util.Set;

public class LoginActivity extends AppCompatActivity {

    private EditText mLoginEmail;
    private EditText mLoginPassword;
    private Button mLoginBtn;

    private TextView mLoginCreate;

    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mProgress = new ProgressDialog(this);

        mLoginEmail = (EditText) findViewById(R.id.loginEmail);
        mLoginPassword = (EditText) findViewById(R.id.loginPassword);
        mLoginBtn = (Button) findViewById(R.id.loginBtn);

        mLoginCreate = (TextView) findViewById(R.id.loginCreate);

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkLogin();

            }
        });

        mLoginCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);

            }
        });

    }

    private void checkLogin() {

        String email = mLoginEmail.getText().toString().trim();
        String password = mLoginPassword.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

            mProgress.setMessage("Efetuando login...");
            mProgress.show();

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        String uid = mAuth.getCurrentUser().getUid();

                        String key = getResources().getString(R.string.uid_user_session);
                        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPref.edit();
                        String name = mAuth.getCurrentUser().getDisplayName();
                        editor.remove(key);
                        editor.commit();
                        editor.putString(key, uid);
                        editor.commit();

                        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
                        SharedPreferences.Editor edit = prefs.edit();
                        edit.putString("username", name);
                        edit.commit();

                        mProgress.dismiss();

                        LoginActivity.this.finish();

                    } else {

                        mProgress.dismiss();
                        Toast.makeText(LoginActivity.this, "Email ou senha incorreto!", Toast.LENGTH_LONG).show();

                    }

                }
            });

        } else {
            Toast.makeText(LoginActivity.this, "É necessário preencher todos os campos!", Toast.LENGTH_LONG).show();
        }

    }

}
