package com.mart.nagaon.product;

import android.graphics.Paint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.nagaon.ItemClickListener;
import com.mart.nagaon.R;

public class prodadapter extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView prodName,prodQuantity,prodPrice,prodDisc,prodDiscount;
    public ImageView prodImage;
    public Button add;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public prodadapter(@NonNull View itemView) {
        super(itemView);

        prodName = (TextView)itemView.findViewById(R.id.prod_txt);
        prodQuantity = (TextView)itemView.findViewById(R.id.quantity);
        prodPrice = (TextView)itemView.findViewById(R.id.price);
        prodDiscount = (TextView)itemView.findViewById(R.id.disc);
        prodDisc = (TextView)itemView.findViewById(R.id.cutPrice);
        prodDisc.setPaintFlags(prodDisc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        prodImage = (ImageView)itemView.findViewById(R.id.prod_img);
        add = (Button)itemView.findViewById(R.id.addToCart);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
