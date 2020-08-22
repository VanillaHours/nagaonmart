package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MyAccount extends AppCompatActivity {

    ImageView back;
    TextView nameView, contactView, addressView, emailView, editdetails, viewallorders;
    Button logout;
    RecyclerView recyclerView;

    String contact;


    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Requests");

    private FirebaseRecyclerOptions<request> options;
    private FirebaseRecyclerAdapter<request, myorderviewholder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        nameView = findViewById(R.id.l_name);
        contactView = findViewById(R.id.l_contact);
        addressView = findViewById(R.id.l_address);
        emailView = findViewById(R.id.l_email);
        editdetails = findViewById(R.id.editpers);
        viewallorders = findViewById(R.id.allorder);
        logout = findViewById(R.id.logOut);
        back = findViewById(R.id.cat_back);
        recyclerView = findViewById(R.id.recOrder);

        if(!FirebaseAuth.getInstance().getCurrentUser().getUid().isEmpty()){
            String userId = ""+FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userId);
            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                    if (documentSnapshot.exists()) {

                        nameView.setText(String.format("%s", documentSnapshot.getString("name")));
                        contactView.setText(String.format("%s", documentSnapshot.getString("contact")));
                        emailView.setText(String.format("%s", documentSnapshot.getString("email")));
                        addressView.setText(documentSnapshot.getString("address"));
                        contact = documentSnapshot.getString("contact");
                        loadorders(contact);

                    }
                }
            });
        }
        editdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),updatedetails.class);
                intent.putExtra("name", nameView.getText().toString());
                intent.putExtra("contact", contactView.getText().toString());
                intent.putExtra("email", emailView.getText().toString());
                intent.putExtra("address", addressView.getText().toString());
                startActivity(intent);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyAccount.super.onBackPressed();
            }
        });
        viewallorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MyOrders.class));
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyAccount.this, loginpage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void loadorders(String contact) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Query query = ref.orderByChild("phone").equalTo(contact).limitToLast(1);
        options = new FirebaseRecyclerOptions.Builder<request>().setQuery(query, request.class).build();

        adapter = new FirebaseRecyclerAdapter<request, myorderviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final myorderviewholder holder, final int position, @NonNull final request model) {
                holder.OrderNum.setText(adapter.getRef(position).getKey());
                holder.orderstatus.setText(model.getStatus());
                holder.date.setText(model.getOrderDate());
                holder.paymentstatus.setText(model.getPaymentMethod());
                holder.totalprice.setText("₹" + model.getTotal());
            }

            @NonNull
            @Override
            public myorderviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.accorder, parent, false);
                return new myorderviewholder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}