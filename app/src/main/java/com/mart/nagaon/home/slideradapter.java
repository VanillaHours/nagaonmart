package com.mart.nagaon.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mart.nagaon.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

public class slideradapter extends SliderViewAdapter<SliderViewHolder> {

    Context context;
    int setTotalCount;
    String imgUrl;

    public slideradapter(Context context, int setTotalCount) {
        this.context = context;
        this.setTotalCount = setTotalCount;
    }

    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_item,parent,false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SliderViewHolder viewHolder, final int position) {

        FirebaseDatabase.getInstance().getReference("adImages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                switch (position){
                    case 0:
                        imgUrl = dataSnapshot.child("1").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 1:
                        imgUrl = dataSnapshot.child("2").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 2:
                        imgUrl = dataSnapshot.child("3").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 3:
                        imgUrl = dataSnapshot.child("4").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 4:
                        imgUrl = dataSnapshot.child("5").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 5:
                        imgUrl = dataSnapshot.child("6").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 6:
                        imgUrl = dataSnapshot.child("7").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 7:
                        imgUrl = dataSnapshot.child("8").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 8:
                        imgUrl = dataSnapshot.child("9").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                    case 9:
                        imgUrl = dataSnapshot.child("10").getValue().toString();
                        Glide.with(viewHolder.itemView).load(imgUrl).into(viewHolder.sliderImageView);
                        break;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getCount() {
        return setTotalCount ;
    }
}

class SliderViewHolder extends SliderViewAdapter.ViewHolder {
    ImageView sliderImageView;
    public SliderViewHolder(View itemView) {
        super(itemView);
        sliderImageView = itemView.findViewById(R.id.image);
    }
}