package com.mart.nagaon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mart.nagaon.home.homepage;
import com.mart.nagaon.product.productpg;

public class Address extends AppCompatActivity {

    Button checkout;
    ImageView back;

    String full_address;
    String bag, delivery;
    TextInputLayout add_name, add_num, pin_code, address, town, city, state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        checkout = findViewById(R.id.checkout);
        back = findViewById(R.id.cat_back);

        add_name = findViewById(R.id.addr_name);
        add_num = findViewById(R.id.addr_num);
        pin_code = findViewById(R.id.pin_code);
        address = findViewById(R.id.address);
        town = findViewById(R.id.town);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);

        if(getIntent()!=null) {
            bag = getIntent().getStringExtra("bagtotal");
            delivery = getIntent().getStringExtra("delivery");

            Toast.makeText(getApplicationContext(),bag+"     "+delivery,Toast.LENGTH_LONG).show();
        }

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {

                            add_name.getEditText().setText(value.getString("name"));
                            add_num.getEditText().setText(value.getString("contact"));

                        } else {
                        }
                    }
                });

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String stringcontact = add_num.getEditText().getText().toString().trim();
                String stringname = add_name.getEditText().getText().toString().trim();
                String stringaddress = address.getEditText().getText().toString().trim();
                String stringpin = pin_code.getEditText().getText().toString().trim();
                String stringtown = town.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(stringname)){
                    add_name.setError("Name is Required");
                    return;
                }
                if (TextUtils.isEmpty(stringpin)){
                    pin_code.setError("Pincode is Required");
                    return;
                }
                if (TextUtils.isEmpty(stringcontact)){
                    add_num.setError("Contact Number is Required");
                    return;
                }
                if (TextUtils.isEmpty(stringaddress)){
                    address.setError("Contact Number is Required");
                    return;
                }
                if (TextUtils.isEmpty(stringtown)){
                    town.setError("Contact Number is Required");
                    return;
                }

                full_address = (pin_code.getEditText().getText().toString()+"+"+address.getEditText().getText().toString()+"+"+town.getEditText().getText().toString()+"+"+city.getEditText().getText().toString()+"+"+state.getEditText().getText().toString());
                Intent address_del = new Intent(Address.this, payment.class);
                address_del.putExtra("address",full_address);
                address_del.putExtra("bag",bag);
                address_del.putExtra("delivery",delivery);
                address_del.putExtra("name",add_name.getEditText().getText().toString());
                address_del.putExtra("contact",add_num.getEditText().getText().toString());
                startActivity(address_del);
                Toast.makeText(getApplicationContext(),full_address,Toast.LENGTH_SHORT).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Address.super.onBackPressed();
            }
        });
    }
}