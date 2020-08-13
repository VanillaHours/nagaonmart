package com.mart.nagaon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mart.nagaon.database.database;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCartname, txtCartprice, txtCartQuantity, txtCartCount, remove;
    public ImageView img;

    private ItemClickListener itemClickListener;

    public void setTxtCartname(TextView txtCartname) {
        this.txtCartname = txtCartname;
    }

    public cartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCartname = (TextView)itemView.findViewById(R.id.cart_txt);
        txtCartprice = (TextView)itemView.findViewById(R.id.cartprice);
        txtCartCount = (TextView)itemView.findViewById(R.id.cartCount);
        txtCartQuantity = (TextView)itemView.findViewById(R.id.cartquantity);
        img = (ImageView)itemView.findViewById(R.id.prod_img);
        remove = (TextView)itemView.findViewById(R.id.remove);
    }

    @Override
    public void onClick(View view) {

    }
}

public class cartadapter extends RecyclerView.Adapter<cartViewHolder>{

    private List<OrderModel> listdata = new ArrayList<>();
    private Context context;

    public cartadapter(List<OrderModel> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View itemview = inflater.inflate(R.layout.cart_card,parent,false);
        return new cartViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final cartViewHolder holder, int position) {
        holder.txtCartQuantity.setText(" ("+listdata.get(position).getQuantity()+")");
        holder.txtCartprice.setText("₹"+listdata.get(position).getPrice());
        holder.txtCartname.setText(listdata.get(position).getProdName());
        holder.txtCartCount.setText("Quantity: "+listdata.get(position).getCount());
        Picasso.get().load(listdata.get(position).getImage()).into(holder.img);

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new database(context).removeItem(new OrderModel())
            }
        });

    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}