package com.mart.nagaon.home.homeAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mart.nagaon.R;

import java.util.ArrayList;

public class catg_adapter extends RecyclerView.Adapter<catg_adapter.catgViewHolder> {

    ArrayList<catg_helper> catg_locations;

    public catg_adapter(ArrayList<catg_helper> catg_locations) {
        this.catg_locations = catg_locations;
    }

    @NonNull
    @Override
    public catgViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catg_card,parent,false);
        catgViewHolder catgViewHolder = new catgViewHolder(view);
        return catgViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull catgViewHolder holder, int position) {
        catg_helper catg_helper = catg_locations.get(position);
        holder.image.setImageResource(catg_helper.getImage());
        holder.title.setText(catg_helper.getTitle());


    }

    @Override
    public int getItemCount() {
        return catg_locations.size();
    }

    public static class catgViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView title;

        public catgViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.catg_img);
            title = itemView.findViewById(R.id.catg_txt);
        }
    }
}
