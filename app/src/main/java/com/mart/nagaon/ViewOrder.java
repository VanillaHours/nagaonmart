package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewOrder extends AppCompatActivity {

    String orderID;
    public ImageView back;
    public TextView orderNumber, orderstatus, l_name, l_contact, l_address, totalPrice, pay_method, orderItems;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Requests");

    private FirebaseRecyclerOptions<request> options;
    private FirebaseRecyclerAdapter<request, viewOrdersViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        orderNumber = findViewById(R.id.select);
        orderstatus = findViewById(R.id.orderstatus);
        l_name = findViewById(R.id.l_name);
        l_contact = findViewById(R.id.l_contact);
        l_address = findViewById(R.id.l_address);
        totalPrice = findViewById(R.id.totalPrice);
        pay_method = findViewById(R.id.pay_method);
        orderItems = findViewById(R.id.orderItems);
        back = findViewById(R.id.cat_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewOrder.super.onBackPressed();
            }
        });

        if(getIntent()!=null) {
            orderID = getIntent().getStringExtra("orderID");
            orderNumber.setText(orderID);
        }

        final String[] concatenatedList = {""};
        ref.child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderstatus.setText(snapshot.child("status").getValue().toString());
                l_name.setText(snapshot.child("name").getValue().toString());
                l_contact.setText(snapshot.child("phone").getValue().toString());
                l_address.setText(snapshot.child("address").getValue().toString());
                totalPrice.setText("₹"+snapshot.child("total").getValue().toString());
                pay_method.setText(snapshot.child("paymentMethod").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        ref.child(orderID).child("foods").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> prod;
                for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                    concatenatedList[0] += "Name: "+childDataSnapshot.child("prodName").getValue()+"\n " +
                            "Quantity: "+childDataSnapshot.child("quantity").getValue()+" x "+childDataSnapshot.child("count").getValue()+"\n\n";
                }

                orderItems.setText(concatenatedList[0]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}