package com.mart.nagaon.home;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.nagaon.ItemClickListener;
import com.mart.nagaon.R;

public class adapter1 extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView catg_name;
    public TextView catg_image;
    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager manager;
    public LinearLayout trans_lay;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public adapter1(@NonNull View itemView) {
        super(itemView);
        manager = new GridLayoutManager(itemView.getContext(), 3);
//        manager = new LinearLayoutManager(itemView.getContext(),RecyclerView.HORIZONTAL, false);
        catg_name = itemView.findViewById(R.id.catn_txt);
        catg_image = itemView.findViewById(R.id.catn_img);
        recyclerView = itemView.findViewById(R.id.expanded_menu);
        trans_lay = itemView.findViewById(R.id.trans_layout);
        recyclerView.setLayoutManager(manager);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
