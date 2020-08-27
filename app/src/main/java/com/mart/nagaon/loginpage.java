package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mart.nagaon.home.homepage;

import java.util.concurrent.TimeUnit;

public class loginpage extends AppCompatActivity {

    TextInputLayout phonenum, otp;
    TextView privacy, TandC;
    Button login, ver;
    FirebaseAuth fauth;
    String phone;
    String verificationCodeBySystem = "";

    ProgressBar progressBar;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                verifyCode(code);
                Toast.makeText(loginpage.this, "Verifying"+code, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(loginpage.this, "onVerificationFailed " + e.toString(), Toast.LENGTH_SHORT).show();

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(loginpage.this, "Invalid Request " + e.toString(), Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Toast.makeText(loginpage.this, "The SMS quota for the project has been exceeded " + e.toString(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCodeSent(String verificationId,
                               PhoneAuthProvider.ForceResendingToken token) {
            Toast.makeText(loginpage.this, "onCodeSent " + verificationId, Toast.LENGTH_SHORT).show();

            verificationCodeBySystem = verificationId;
            PhoneAuthProvider.ForceResendingToken mResendToken = token;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        fauth = FirebaseAuth.getInstance();
        privacy = findViewById(R.id.privacy);
        TandC = findViewById(R.id.terms);
        login = findViewById(R.id.log);
        phonenum = findViewById(R.id.phone);
        otp = findViewById(R.id.otp);
        ver = findViewById(R.id.ver);
        progressBar = findViewById(R.id.progressBar);

        TandC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(loginpage.this, "Terms and Conditions", Toast.LENGTH_SHORT).show();
            }
        });
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(loginpage.this, "Privacy Policy", Toast.LENGTH_SHORT).show();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                load = new ProgressDialog(loginpage.this);
//                load.show();
//                load.setContentView(R.layout.loadprogress);
//                load.setCancelable(false);
//                load.setCanceledOnTouchOutside(false);

                phone = phonenum.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(phone)||phone.length()!=10){
                    phonenum.setError("Invalid Phone Number");
                    return;
                }
                phonenum.setError(null);
                sendVerificationCodeToUser(phone);

                otp.setVisibility(View.VISIBLE);
                ver.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);
            }
        });

        ver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String code = otp.getEditText().getText().toString();

                if (code.isEmpty() || code.length() < 6) {
                    otp.setError("Wrong OTP");
                    otp.requestFocus();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                verifyCode(code);
            }
        });
    }

    private void sendVerificationCodeToUser(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,   // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        Toast.makeText(loginpage.this, "Sent "+ phone, Toast.LENGTH_SHORT).show();
    }

    private void verifyCode(String codeByUser) {

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCodeBySystem, codeByUser);
        signInTheUserByCredentials(credential);
    }

    private void signInTheUserByCredentials(PhoneAuthCredential credential) {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(loginpage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(loginpage.this, "Welcome!", Toast.LENGTH_SHORT).show();

                            //Perform Your required action here to either let the user sign In or do something required
                            final String userID = FirebaseAuth.getInstance().getUid();

                            FirebaseDatabase.getInstance().getReference("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    for (DataSnapshot ds : snapshot.getChildren()) {
                                        String userIDD = ds.getKey();
                                        Toast.makeText(loginpage.this, userIDD, Toast.LENGTH_SHORT).show();
                                        if (userIDD.equals(userID)) {
                                            Intent intent = new Intent(getApplicationContext(), homepage.class);
                                            intent.putExtra("phone","+91"+phone);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
//                        startActivity(new Intent(getApplicationContext(),signpage.class));
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            Intent intent = new Intent(getApplicationContext(), signpage.class);
                            intent.putExtra("phone","+91"+phone);
                            startActivity(intent);

                        } else {
                            Toast.makeText(loginpage.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}