package com.mart.nagaon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mart.nagaon.home.homepage;

public class success extends AppCompatActivity {

    String status = "";
    Button shome,fhome;
    LinearLayout successLayout, failedLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        successLayout = findViewById(R.id.successDialog);
        failedLayout = findViewById(R.id.failedDialog);
        shome = findViewById(R.id.successhome);
        fhome = findViewById(R.id.failedhome);

        if (getIntent() != null) {
            status = getIntent().getStringExtra("status");
        }

        if(status.equals("success")){
            successLayout.setVisibility(View.VISIBLE);
        }
        failedLayout.setVisibility(View.VISIBLE);

        shome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(success.this, homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        fhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(success.this, homepage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(success.this, homepage.class));
    }
}