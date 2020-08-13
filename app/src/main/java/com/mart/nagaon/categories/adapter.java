package com.mart.nagaon.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.nagaon.ItemClickListener;
import com.mart.nagaon.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapter extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView catg_name;
    public ImageView catg_img;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager manager;
    public RelativeLayout trans_lay;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public adapter(@NonNull View itemView) {
        super(itemView);
        manager = new LinearLayoutManager(itemView.getContext());
        catg_name = itemView.findViewById(R.id.catn_txt);
        catg_img = itemView.findViewById(R.id.catn_img);
        recyclerView = itemView.findViewById(R.id.expanded_menu);
        trans_lay = itemView.findViewById(R.id.trans_layout);
        recyclerView.setLayoutManager(manager);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(),false);
    }
}
