package com.mart.nagaon;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class myorderviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView OrderNum, totalprice, paymentstatus, date, orderstatus, ordercancel, viewOrder;

    private ItemClickListener itemClickListener;

    public myorderviewholder(@NonNull View itemView) {
        super(itemView);

        OrderNum = itemView.findViewById(R.id.ordernum);
        totalprice = itemView.findViewById(R.id.orderPrice);
        paymentstatus = itemView.findViewById(R.id.paystatus);
        date = itemView.findViewById(R.id.date);
        orderstatus = itemView.findViewById(R.id.orderstatus);
        ordercancel = itemView.findViewById(R.id.cancelorder);
        viewOrder = itemView.findViewById(R.id.viewOrder);

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
