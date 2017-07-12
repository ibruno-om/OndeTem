package br.ufg.inf.dsdm.ondetem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class RegisterActivity extends AppCompatActivity {

    private EditText mRealName;
    private EditText mEmailUser;
    private EditText mPasswordUser;
    private Button mRegisterUser;


    private FirebaseAuth mAuth;

    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        mProgress = new ProgressDialog(this);


        mRealName = (EditText) findViewById(R.id.realName);
        mEmailUser = (EditText) findViewById(R.id.emailUser);
        mPasswordUser = (EditText) findViewById(R.id.passwordUser);
        mRegisterUser = (Button) findViewById(R.id.registerUser);

        mRegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startRegister();

            }
        });

    }

    private void startRegister() {

        final String name = mRealName.getText().toString().trim();
        String email = mEmailUser.getText().toString().trim();
        String password = mPasswordUser.getText().toString().trim();

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgress.setMessage("Cadastrando...");
            mProgress.show();


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {

                        FirebaseUser user = mAuth.getCurrentUser();

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name).build();

                        user.updateProfile(profileUpdates);


                        mProgress.dismiss();

                        RegisterActivity.this.finish();

                        Intent intentHome = new Intent(RegisterActivity.this, HomeActivity.class);
                        intentHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intentHome);



                    }

                }
            });
        }

    }
}
