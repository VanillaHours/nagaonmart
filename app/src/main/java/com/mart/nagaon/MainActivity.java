package com.mart.nagaon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.mart.nagaon.home.homepage;

public class MainActivity extends AppCompatActivity {

    private static int SPLASH_TIMEOUT = 3500;
    FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fauth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(fauth.getCurrentUser() != null){
                    startActivity(new Intent(getApplicationContext(), homepage.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(),homepage.class));
                    overridePendingTransition(R.anim.left_trans2,R.anim.left_trans);
                }
                finish();
            }
        }, SPLASH_TIMEOUT);

    }
}