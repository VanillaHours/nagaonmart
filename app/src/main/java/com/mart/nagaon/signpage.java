package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mart.nagaon.home.homepage;

import java.util.HashMap;
import java.util.Map;

public class signpage extends AppCompatActivity {

    String userID;
    public static final String TAG = "TAG";
    Button login,sign;
    FirebaseAuth fauth;
    TextInputLayout name_r,pass_r,email_r, phn_r;
    FirebaseFirestore fstore;

    ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signpage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        login = findViewById(R.id.log);
        sign = findViewById(R.id.signup);
        name_r = findViewById(R.id.name);
        phn_r = findViewById(R.id.phn);
        pass_r = findViewById(R.id.pswd);
        email_r = findViewById(R.id.email);

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                load = new ProgressDialog(signpage.this);
                load.show();
                load.setContentView(R.layout.loadprogress);
                load.setCancelable(false);
                load.setCanceledOnTouchOutside(false);

                final String email = email_r.getEditText().getText().toString().trim();
                String pass = pass_r.getEditText().getText().toString().trim();
                final String phone = phn_r.getEditText().getText().toString().trim();
                final String regname = name_r.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    email_r.setError("Email is Required");
                    load.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(pass)){
                    pass_r.setError("Password is Required");
                    load.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(phone)){
                    phn_r.setError("Contact Number is Required");
                    load.dismiss();
                    return;
                }
                if(phone.length() != 10){
                    phn_r.setError("Invalid Mobile Number");
                    load.dismiss();
                    return;
                }
                if (pass.length()<6){
                    pass_r.setError("Password should be minimum 6 characters");
                    load.dismiss();
                    return;
                }

                fauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(signpage.this, "User Created", Toast.LENGTH_SHORT).show();
                            userID = fauth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();
                            user.put("name",regname);
                            user.put("email",email);
                            user.put("contact",phone);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: User Profile created for"+ userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), homepage.class));
                            finish();
                        }
                        else{
                            Toast.makeText(signpage.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            load.dismiss();
                        }
                    }
                });

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),loginpage.class));
                overridePendingTransition(R.anim.right_trans,R.anim.right_trans2);
                finish();
            }
        });


    }
    @Override
    public void onBackPressed() {
        load.dismiss();
    }
}