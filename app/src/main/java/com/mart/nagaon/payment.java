package com.mart.nagaon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mart.nagaon.database.database;
import com.mart.nagaon.home.homepage;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class payment extends AppCompatActivity implements PaymentResultListener {

    TextView nameView, contactView, addressView, bagTotalView, deliveryView, totalPriceView;

    Button placeorder;
    String name = "";
    String contact = "";
    String address = "";
    String bag = "";
    String delivery = "";
    String OrderID = "";

    int res, result;
    public static final String TAG = "TAG";
    List<OrderModel> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        nameView = findViewById(R.id.l_name);
        contactView = findViewById(R.id.l_contact);
        addressView = findViewById(R.id.l_address);
        bagTotalView = findViewById(R.id.bagTotal);
        deliveryView = findViewById(R.id.delivery);
        totalPriceView = findViewById(R.id.totalPrice);
        OrderID = ""+System.currentTimeMillis();

        if (getIntent() != null) {
            address = getIntent().getStringExtra("address");
            name = getIntent().getStringExtra("name");
            contact = getIntent().getStringExtra("contact");
            bag = getIntent().getStringExtra("bag");
            delivery = getIntent().getStringExtra("delivery");

            Toast.makeText(getApplicationContext(), name + contact + address, Toast.LENGTH_LONG).show();

        }
        nameView.setText(name);
        contactView.setText(contact);
        addressView.setText(address);
        bagTotalView.setText("₹" + bag);
        deliveryView.setText("₹" + delivery);
        res = Integer.parseInt(bag) + Integer.parseInt(delivery);
        result = res * 100;
        totalPriceView.setText("₹" + res);

        placeorder = findViewById(R.id.order);
        cart = new database(this).getCarts();

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPayment();
            }
        });
    }

    public void startPayment() {

        /**
         * Instantiate Checkout
         */
        Checkout checkout = new Checkout();
//
//        /**
//         * Set your logo here
//         */
//        checkout.setImage(R.drawable.logo);

        /**
         * Reference to current activity
         */
        final Activity activity = this;

        /**
         * Pass your payment options to the Razorpay Checkout as a JSONObject
         */
        try {
            JSONObject options = new JSONObject();

            options.put("name", "NagaonMart");
            options.put("description", "OrderID: "+OrderID);
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("theme.color", "#3282B8");
            options.put("currency", "INR");
            options.put("amount", "" + result);//pass amount in currency subunits
            checkout.open(activity, options);
        } catch (Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(payment.this, "Order Placed", Toast.LENGTH_SHORT).show();
        request request = new request(
                contact,
                name,
                address,
                ""+res,
                "Pay Now",
                "Success",
                cart
        );
        FirebaseDatabase.getInstance().getReference("Requests").child(OrderID)
                .setValue(request);
        new database(getBaseContext()).cleancart();
        startActivity(new Intent(payment.this, success.class));
        Toast.makeText(payment.this, "Order Placed", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(payment.this, "Order Failed", Toast.LENGTH_SHORT).show();
        Toast.makeText(payment.this, s, Toast.LENGTH_SHORT).show();
    }

}