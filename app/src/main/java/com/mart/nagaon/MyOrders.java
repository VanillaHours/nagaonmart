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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mart.nagaon.product.prodadapter;
import com.mart.nagaon.product.prodmodel;

import java.lang.ref.Reference;
import java.util.Collections;

public class MyOrders extends AppCompatActivity {

    String contact = "";

    public RecyclerView recyclerView;
    public ImageView back;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Requests");

    private FirebaseRecyclerOptions<request> options;
    private FirebaseRecyclerAdapter<request, myorderviewholder> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        recyclerView = findViewById(R.id.listOrders);
        back = findViewById(R.id.cat_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyOrders.super.onBackPressed();
            }
        });

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(uid);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot.exists()) {

                    contact = documentSnapshot.getString("contact");
                    loadorders(contact);

                } else {
                }
            }
        });

//        Toast.makeText(getApplicationContext(), contact, Toast.LENGTH_SHORT ).show();
//        Integer phone = Integer.parseInt(contact);

    }

    private void loadorders(String contact) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        Query query = ref.orderByChild("phone").equalTo(contact);
        options = new FirebaseRecyclerOptions.Builder<request>().setQuery(query, request.class).build();

        adapter = new FirebaseRecyclerAdapter<request, myorderviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final myorderviewholder holder, final int position, @NonNull final request model) {
                holder.OrderNum.setText(adapter.getRef(position).getKey());
                holder.orderstatus.setText(model.getStatus());
                holder.date.setText(model.getOrderDate());
                holder.paymentstatus.setText(model.getPaymentMethod());
                holder.totalprice.setText("₹" + model.getTotal());

                holder.ordercancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new AlertDialog.Builder(MyOrders.this)
                                .setTitle("Cancel Order")
                                .setMessage("Do you want to cancel this order?")
                                .setPositiveButton("YES",
                                        new DialogInterface.OnClickListener() {
                                            @TargetApi(11)
                                            public void onClick(DialogInterface dialog, int id) {
                                                if (!holder.orderstatus.getText().equals("Order Received"))
                                                    Toast.makeText(getApplicationContext(), "Cannot cancel this order. \n Order has already been processed", Toast.LENGTH_SHORT).show();
                                                else {
                                                    cancelOrder(adapter.getRef(position).getKey());
                                                }
                                                dialog.cancel();
                                            }
                                        })
                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                    @TargetApi(11)
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                }).show();
                    }
                });
                holder.viewOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent cart_del = new Intent(MyOrders.this, ViewOrder.class);
                        cart_del.putExtra("orderID", adapter.getRef(position).getKey());
                        startActivity(cart_del);
                    }
                });
            }

            @NonNull
            @Override
            public myorderviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_card, parent, false);
                return new myorderviewholder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    private void cancelOrder(final String key) {
        ref.child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(), new StringBuilder("Order ").append(key).append("has been cancelled.").toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}