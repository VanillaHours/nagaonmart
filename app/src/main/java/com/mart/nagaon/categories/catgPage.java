package com.mart.nagaon.categories;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mart.nagaon.Firebasedata;
import com.mart.nagaon.ItemClickListener;
import com.mart.nagaon.R;
import com.mart.nagaon.home.homepage;
import com.mart.nagaon.product.prodmodel;
import com.mart.nagaon.product.productpg;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class catgPage extends AppCompatActivity {

    ImageView back;

    //recycler for categories
    RecyclerView cat_rec;

    FirebaseDatabase database;
    DatabaseReference reference;
    //cat firebase
    private FirebaseRecyclerOptions<model> options;
    private FirebaseRecyclerOptions<listmodel> listoptions;
    private FirebaseRecyclerAdapter<model, adapter> categoryAdapter;
    private FirebaseRecyclerAdapter<listmodel, listadapter> listAdapter;
    RecyclerView.LayoutManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catg_page);

        back = findViewById(R.id.cat_back);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        manager = new LinearLayoutManager(this);
        cat_rec = findViewById(R.id.cat_home1);
        cat_rec.setLayoutManager(manager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                catgPage.super.onBackPressed();
            }
        });

        options = new FirebaseRecyclerOptions.Builder<model>().setQuery(reference.child("Category"),model.class).build();
        categoryAdapter = new FirebaseRecyclerAdapter<model, adapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final adapter holder, int position, @NonNull model model) {
                holder.catg_name.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.catg_img);

                final model clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
                listoptions = new FirebaseRecyclerOptions.Builder<listmodel>().setQuery(reference.child("SubCategories").orderByChild("CatID").equalTo(categoryAdapter.getRef(position).getKey()),listmodel.class).build();

                listAdapter = new FirebaseRecyclerAdapter<listmodel, listadapter>(listoptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final listadapter holder, int position, @NonNull final listmodel model) {
                        holder.Name.setText(model.getName());

                        final listmodel clickItem = model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Intent prodpg = new Intent(catgPage.this,productpg.class);
                                prodpg.putExtra("SubID",model.getSubID());
                                prodpg.putExtra("Name",model.getName());
                                startActivity(prodpg);

                                Toast.makeText(catgPage.this,""+model.getSubID(),Toast.LENGTH_SHORT).show();
//                                Toast.makeText(catgPage.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public listadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2 = LayoutInflater.from(getBaseContext()).inflate(R.layout.expandmenu_card,parent,false);
                        return new listadapter(view2);
                    }
                };
                listAdapter.startListening();
                listAdapter.notifyDataSetChanged();
                holder.recyclerView.setAdapter(listAdapter);
            }

            @NonNull
            @Override
            public adapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.catpage_card,parent,false);
                return new adapter(view1);
            }
        };
        categoryAdapter.startListening();
        categoryAdapter.notifyDataSetChanged();
        cat_rec.setAdapter(categoryAdapter);
    }

}