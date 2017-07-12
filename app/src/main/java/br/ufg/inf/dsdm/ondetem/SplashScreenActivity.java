package br.ufg.inf.dsdm.ondetem;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler handle = new Handler();
        handle.postDelayed(new Runnable() {
            @Override
            public void run() {
                mostrarHome();
            }
        }, 2000);
    }

    private void mostrarHome() {
        Intent intentHome = new Intent(SplashScreenActivity.this,
                HomeActivity.class);
        startActivity(intentHome);
        finish();
    }
}
