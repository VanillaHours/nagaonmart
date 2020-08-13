package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mart.nagaon.home.homepage;

public class loginpage extends AppCompatActivity {

    TextInputLayout mail,passwd;
    Button login,sign;
    FirebaseAuth fauth;

    ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fauth = FirebaseAuth.getInstance();
        login = findViewById(R.id.log);
        sign = findViewById(R.id.signup);
        mail = findViewById(R.id.email);
        passwd = findViewById(R.id.pswd);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                load = new ProgressDialog(loginpage.this);
                load.show();
                load.setContentView(R.layout.loadprogress);
                load.setCancelable(false);
                load.setCanceledOnTouchOutside(false);

                String email = mail.getEditText().getText().toString().trim();
                String pass = passwd.getEditText().getText().toString().trim();
                if(TextUtils.isEmpty(email)){
                    mail.setError("Email is Required");
                    load.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    passwd.setError("Password is Required");
                    load.dismiss();
                    return;
                }
                fauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(loginpage.this, "Welcome", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), homepage.class));
                            finish();
                        }
                        else{
                            load.dismiss();
                            Toast.makeText(loginpage.this, "Error"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),signpage.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_trans2,R.anim.left_trans);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        load.dismiss();
    }
}