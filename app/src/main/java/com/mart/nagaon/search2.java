package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class search2 extends AppCompatActivity {

    EditText search_edit_text;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    ArrayList<String> prodname;
    ArrayList<String> prodimage;
    ArrayList<Long> proddisc;
    ArrayList<String> prodquantity;
    ArrayList<Long> prodcutprice;
    ArrayList<Long> prodprice;
    ArrayList<String> prodID;
    SearchAdapter searchAdapter;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);

        back = findViewById(R.id.cat_back);
        search_edit_text = (EditText) findViewById(R.id.search_edit_text);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        prodname = new ArrayList<>();
        prodimage = new ArrayList<>();
        prodID = new ArrayList<>();
        proddisc = new ArrayList<>();
        prodquantity = new ArrayList<>();
        prodcutprice = new ArrayList<>();
        prodprice = new ArrayList<>();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search2.super.onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        search_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    setAdapter(s.toString());
                } else {
                    /*
                     * Clear the list when editText is empty
                     * */
                    prodname.clear();
                    prodID.clear();
                    prodimage.clear();
                    prodquantity.clear();
                    proddisc.clear();
                    prodcutprice.clear();
                    prodprice.clear();
                    recyclerView.removeAllViews();
                }
            }
        });
    }

    private void setAdapter(final String searchedString) {
        databaseReference.child("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prodname.clear();
                recyclerView.removeAllViews();
                int c = 0;

                for(DataSnapshot datasnapshot: snapshot.getChildren()){
                    String uid = datasnapshot.getKey();
                    String Name = datasnapshot.child("Name").getValue(String.class);
                    String Image = datasnapshot.child("Image").getValue(String.class);
                    Long Disc = datasnapshot.child("Discount").getValue(Long.class);
                    String Quantity = datasnapshot.child("Quantity").getValue(String.class);
                    Long Price = datasnapshot.child("Price").getValue(Long.class);
                    Long Cutprice = datasnapshot.child("MRP").getValue(Long.class);

                    if(Name.toLowerCase().contains(searchedString.toLowerCase())){

                        prodID.add(uid);
                        prodname.add(Name);
                        prodimage.add(Image);
                        proddisc.add(Disc);
                        prodquantity.add(Quantity);
                        prodcutprice.add(Cutprice);
                        prodprice.add(Price);

                        c++;
                    }

                    if(c == 10)
                        break;
                }
                searchAdapter = new SearchAdapter(search2.this,prodname,prodimage,proddisc,prodquantity,prodcutprice,prodprice);
                recyclerView.setAdapter(searchAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}