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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mart.nagaon.home.homepage;

import java.util.HashMap;
import java.util.Map;

public class signpage extends AppCompatActivity {

    String phone;
    public static final String TAG = "TAG";
    Button login,sign;
    FirebaseAuth fauth;
    TextInputLayout name_r,email_r, phn_r;
    FirebaseDatabase firebaseDatabase;

    ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signpage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fauth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        sign = findViewById(R.id.signup);
        name_r = findViewById(R.id.name);
        phn_r = findViewById(R.id.phn);
        email_r = findViewById(R.id.email);

        if(getIntent()!= null) {
            phone = getIntent().getStringExtra("phone");
            phn_r.getEditText().setText(phone);
        }

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                load = new ProgressDialog(signpage.this);
//                load.show();
//                load.setContentView(R.layout.loadprogress);
//                load.setCancelable(false);
//                load.setCanceledOnTouchOutside(false);

                final String email = email_r.getEditText().getText().toString().trim();
                final String regname = name_r.getEditText().getText().toString().trim();
                String address = "N/A";

                if(TextUtils.isEmpty(email)){
                    email_r.setError("Email is Required");

                    return;
                }
                if(TextUtils.isEmpty(regname)){
                    email_r.setError("Name is Required");
                    return;
                }

                String userID = FirebaseAuth.getInstance().getUid();

                userModel userModel = new userModel(
                        userID,
                        regname,
                        phone,
                        email,
                        address);
                firebaseDatabase.getReference("users").child(userID).setValue(userModel);

                Intent intent = new Intent(getApplicationContext(),homepage.class);
                intent.putExtra("phone",phone);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


//                fauth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(signpage.this, "User Created", Toast.LENGTH_SHORT).show();
//                            userID = fauth.getCurrentUser().getUid();
//                            DocumentReference documentReference = fstore.collection("users").document(userID);
//                            Map<String,Object> user = new HashMap<>();
//                            user.put("name",regname);
//                            user.put("email",email);
//                            user.put("contact",phone);
//                            user.put("address","N/A");
//                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Log.d(TAG, "onSuccess: User Profile created for"+ userID);
//                                }
//                            });
//                            startActivity(new Intent(getApplicationContext(), homepage.class));
//                            finish();
//                        }
//                        else{
//                            Toast.makeText(signpage.this, "Error "+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            load.dismiss();
//                        }
//                    }
//                });

            }
        });
    }
}