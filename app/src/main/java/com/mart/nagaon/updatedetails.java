package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class updatedetails extends AppCompatActivity {

    ImageView back;
    TextInputLayout nameView, contactView, addressView, emailView;
    Button updatedetail;
    ProgressDialog load;

    String name, address, contact, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatedetails);

        nameView = findViewById(R.id.l_name);
        contactView = findViewById(R.id.l_contact);
        addressView = findViewById(R.id.l_address);
        emailView = findViewById(R.id.l_email);
        updatedetail = findViewById(R.id.updatedet);
        back = findViewById(R.id.cat_back);

        if (getIntent() != null) {
            address = getIntent().getStringExtra("address");
            name = getIntent().getStringExtra("name");
            contact = getIntent().getStringExtra("contact");
            email = getIntent().getStringExtra("email");
        }

        nameView.getEditText().setText(name);
        contactView.getEditText().setText(contact);
        addressView.getEditText().setText(address);
        emailView.getEditText().setText(email);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updatedetails.super.onBackPressed();
            }
        });

        updatedetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                load = new ProgressDialog(updatedetails.this);
                load.show();
                load.setContentView(R.layout.loadprogress);
                load.setCancelable(false);

                final String s_address = addressView.getEditText().getText().toString().trim();
                final String s_phone = contactView.getEditText().getText().toString().trim();
                final String s_regname = nameView.getEditText().getText().toString().trim();

                if (TextUtils.isEmpty(s_regname)) {
                    nameView.setError("Name is Required");
                    load.dismiss();
                    return;
                }
                if (TextUtils.isEmpty(s_phone)) {
                    contactView.setError("Contact Number is Required");
                    load.dismiss();
                    return;
                }
                if (s_phone.length() != 10) {
                    contactView.setError("Invalid Mobile Number");
                    load.dismiss();
                    return;
                }

                String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userID);
                Map<String, Object> user = new HashMap<>();
                user.put("name", s_regname);
                user.put("email", email);
                user.put("contact", s_phone);
                user.put("address", s_address);
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "User Profile Updated", Toast.LENGTH_SHORT).show();
                        load.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to Update Data" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        load.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MyAccount.class));
        finish();
    }
}