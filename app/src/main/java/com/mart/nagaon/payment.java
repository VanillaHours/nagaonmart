package com.mart.nagaon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

    Button placeorder;
    String name = "";
    String contact = "";
    String address = "";
    public static final String TAG = "TAG";
    List<OrderModel> cart = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        placeorder = findViewById(R.id.order);
        cart = new database(this).getCarts();

        FirebaseFirestore.getInstance().collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.exists()) {

                            name = value.getString("name");
                            contact = value.getString("contact");

                        } else {
                            Log.d(TAG, "onEvent: Document doesn't exist ");
                        }
                    }
                });

        placeorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startPayment();
//                request request = new request(
//                        contact,
//                        name,
//                        address,
//                        "txtTotalPrice.getText().toString()",
//                        cart
//                );
//                FirebaseDatabase.getInstance().getReference("Requests").child(String.valueOf(System.currentTimeMillis()))
//                        .setValue(request);
//                new database(getBaseContext()).cleancart();
//                Toast.makeText(payment.this, "Order Placed", Toast.LENGTH_SHORT).show();
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
            options.put("description", "Reference No. #123456");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id", "order_DBJOWzybf0sJbb");
            options.put("theme.color", "#3282B8");
            options.put("currency", "INR");
            options.put("amount", "50000");//pass amount in currency subunits
            options.put("prefill.email", "gaurav.kumar@example.com");
            options.put("prefill.contact","9988776655");
            checkout.open(activity, options);
        } catch(Exception e) {
            Log.e(TAG, "Error in starting Razorpay Checkout", e);
        }
    }

    @Override
    public void onPaymentSuccess(String s) {

        Toast.makeText(payment.this, "Order Placed", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(payment.this, "Order Failed", Toast.LENGTH_SHORT).show();
        Toast.makeText(payment.this, s, Toast.LENGTH_SHORT).show();
    }

}