package com.mart.nagaon;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class viewOrdersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView prodname, cartCount, cartquantity, cartprice;
    private ItemClickListener itemClickListener;

    public viewOrdersViewHolder(@NonNull View itemView) {
        super(itemView);

        prodname = itemView.findViewById(R.id.prodname);
        cartCount = itemView.findViewById(R.id.cartCount);
        cartquantity = itemView.findViewById(R.id.cartquantity);
        cartprice = itemView.findViewById(R.id.cartprice);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
