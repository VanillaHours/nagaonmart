package com.mart.nagaon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.mart.nagaon.database.database;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class cartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCartname, txtCartprice, txtCartQuantity, txtCartCount;
    public ImageView img;
    public TextView remove;
    public ElegantNumberButton num_btn;

    private ItemClickListener itemClickListener;

    public void setTxtCartname(TextView txtCartname) {
        this.txtCartname = txtCartname;
    }

    public cartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtCartname = (TextView) itemView.findViewById(R.id.cart_txt);
        txtCartprice = (TextView) itemView.findViewById(R.id.cartprice);
        txtCartCount = (TextView) itemView.findViewById(R.id.cartCount);
        txtCartQuantity = (TextView) itemView.findViewById(R.id.cartquantity);
        img = (ImageView) itemView.findViewById(R.id.prod_img);
        remove = itemView.findViewById(R.id.remove);
        num_btn = itemView.findViewById(R.id.num_btn);
    }

    @Override
    public void onClick(View view) {

    }
}

public class cartadapter extends RecyclerView.Adapter<cartViewHolder> {

    private List<OrderModel> listdata = new ArrayList<>();
    private cart cart;

    public cartadapter(List<OrderModel> listdata, cart cart) {
        this.listdata = listdata;
        this.cart = cart;
    }

    @NonNull
    @Override
    public cartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemview = inflater.inflate(R.layout.cart_card, parent, false);
        return new cartViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull final cartViewHolder holder, final int position) {
        holder.txtCartQuantity.setText(" x (" + listdata.get(position).getQuantity() + ")");
        holder.txtCartprice.setText("₹" + listdata.get(position).getPrice()*listdata.get(position).getCount());
        holder.txtCartname.setText(listdata.get(position).getProdName());
        holder.txtCartCount.setText("" + listdata.get(position).getCount());
        Picasso.get().load(listdata.get(position).getImage()).into(holder.img);

        holder.num_btn.setNumber("" + listdata.get(position).getCount());
        holder.num_btn.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                OrderModel order = listdata.get(position);
                order.setCount(newValue);
                new database(cart).updatecart(order);

                int total = 0;
                List<OrderModel> orders = new database(cart).getCarts();

                int s_total = order.getPrice()*listdata.get(position).getCount();
                holder.txtCartprice.setText("₹"+s_total);
                holder.txtCartCount.setText(""+newValue);

                for (OrderModel item : orders) {
                    total += item.getPrice() * item.getCount();
                }

                cart.bagtotal.setText("₹" + total);
                int res = total + cart.intVal;
                cart.txtTotalPrice.setText("₹" + res);
                cart.totalprice.setText(("₹" + res));

            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new database(cart).removeItem(new OrderModel(
                        listdata.get(position).getProdID(),
                        listdata.get(position).getProdName(),
                        listdata.get(position).getPrice(),
                        listdata.get(position).getQuantity(),
                        listdata.get(position).getCount(),
                        listdata.get(position).getImage()
                ));
//                listdata.remove(holder.getAdapterPosition());
//                notifyDataSetChanged();
                int total = 0;
                List<OrderModel> orders = new database(cart).getCarts();
                for (OrderModel item : orders)
                    total += item.getPrice();

                if (total == 0) {
                    cart.recyclerView.setVisibility(View.GONE);
                    cart.cat_rel1.setVisibility(View.GONE);
                    cart.card1.setVisibility(View.GONE);
                    cart.emptyDialog.setVisibility(View.VISIBLE);
                    cart.card2.setVisibility(View.GONE);
                    cart.startshop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cart.finish();
                        }
                    });
                }
                cart.bagtotal.setText("₹" + total);
                int res = total + cart.intVal;
                cart.txtTotalPrice.setText("₹" + res);
                cart.totalprice.setText(("₹" + res));

                removeAt(holder.getAdapterPosition());
                Toast.makeText(cart, "Removed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeAt(int position) {
        listdata.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, listdata.size());
    }

    @Override
    public int getItemCount() {
        return listdata.size();
    }
}