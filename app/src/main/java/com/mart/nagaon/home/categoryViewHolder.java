package com.mart.nagaon.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.nagaon.ItemClickListener;
import com.mart.nagaon.R;

public class categoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView catName;
    public ImageView catImage;
    public TextView catDesc;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public categoryViewHolder(@NonNull View itemView) {
        super(itemView);
        catName = (TextView)itemView.findViewById(R.id.catg_txt);
        catImage = (ImageView)itemView.findViewById(R.id.catg_img);
        catDesc = (TextView)itemView.findViewById(R.id.catg_desc);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);

    }

}
