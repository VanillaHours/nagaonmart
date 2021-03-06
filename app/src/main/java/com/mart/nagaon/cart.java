package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.ValueEventListener;
import com.mart.nagaon.database.database;


public class cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    public TextView txtTotalPrice, bagtotal, Delivery, totalprice;
    Button btnPlaceOrder, startshop;
    ImageView btnback;
    RelativeLayout cat_rel1;
    View card1;
    LinearLayout card2, emptyDialog;

    int total = 0;
    int intVal;
    String name = "";
    String contact = "";
    String address = "";
    public static final String TAG = "TAG";

    List<OrderModel> cart = new ArrayList<>();
    cartadapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cat_rel1 = findViewById(R.id.cat_rel1);
        card1 = findViewById(R.id.card1);
        emptyDialog = findViewById(R.id.emptyDialog);
        card2 = findViewById(R.id.card2);
        startshop = findViewById(R.id.start);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerView = (RecyclerView) findViewById(R.id.listcart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        bagtotal = (TextView) findViewById(R.id.bagTotal);
        Delivery = (TextView) findViewById(R.id.delivery);
        totalprice = (TextView) findViewById(R.id.totalPrice);
        btnPlaceOrder = (Button) findViewById(R.id.btnPlaceOrder);
        btnback = findViewById(R.id.cat_back);

        loadlistfood();
    }

    private void loadlistfood() {

        cart = new database(this).getCarts();
        adapter = new cartadapter(cart, this);
        recyclerView.setAdapter(adapter);

        for (OrderModel order : cart)
            total += order.getPrice() * order.getCount();

        if (total == 0) {
            recyclerView.setVisibility(View.GONE);
            cat_rel1.setVisibility(View.GONE);
            card1.setVisibility(View.GONE);
            emptyDialog.setVisibility(View.VISIBLE);
            card2.setVisibility(View.GONE);
            startshop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cart.super.onBackPressed();
                }
            });
        }

        bagtotal.setText("₹" + total);

        database.getReference("DeliveryPrice").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                intVal = Integer.parseInt(String.valueOf(snapshot.getValue()));
                Delivery.setText("₹" + snapshot.getValue().toString());
                int res = total + intVal;
                txtTotalPrice.setText("₹" + res);
                totalprice.setText(("₹" + res));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.super.onBackPressed();
            }
        });

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                    Intent cart_del = new Intent(cart.this, Address.class);
                    cart_del.putExtra("bagtotal", "" + total);
                    cart_del.putExtra("delivery", "" + intVal);
                    startActivity(cart_del);
                }else
                    startActivity(new Intent(getApplicationContext(),loginpage.class));
//                request request = new request(
//                        contact,
//                        name,
//                        address,
//                        txtTotalPrice.getText().toString(),
//                        cart
//                );
//                requests.child(String.valueOf(System.currentTimeMillis()))
//                        .setValue(request);
//                new database(getBaseContext()).cleancart();
//                Toast.makeText(com.mart.nagaon.cart.this, "Order Placed", Toast.LENGTH_SHORT).show();
//                finish();
            }
        });

    }

}