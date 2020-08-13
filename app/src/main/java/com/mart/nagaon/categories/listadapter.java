package com.mart.nagaon.categories;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.nagaon.ItemClickListener;
import com.mart.nagaon.R;

public class listadapter extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView Name;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    public listadapter(@NonNull View itemView) {
        super(itemView);

        Name = itemView.findViewById(R.id.exp_txt);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
