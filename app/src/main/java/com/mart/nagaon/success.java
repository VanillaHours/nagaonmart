package com.mart.nagaon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.mart.nagaon.home.homepage;

public class success extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(success.this, homepage.class));
    }
}