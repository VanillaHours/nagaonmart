package com.mart.nagaon.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mart.nagaon.ItemClickListener;
import com.mart.nagaon.MyAccount;
import com.mart.nagaon.MyOrders;
import com.mart.nagaon.R;
import com.mart.nagaon.MainActivity;
import com.mart.nagaon.cart;
import com.mart.nagaon.categories.adapter;
import com.mart.nagaon.categories.catgPage;
import com.mart.nagaon.categories.listadapter;
import com.mart.nagaon.categories.listmodel;
import com.mart.nagaon.categories.model;
import com.mart.nagaon.database.database;
import com.mart.nagaon.loginpage;
import com.mart.nagaon.product.productpg;
import com.mart.nagaon.search;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;

public class homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //homepagename
    TextView name;
    CounterFab btncart;

    //navigation
    static final float END_SCALE = 0.7f;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ImageView menu_ic, search_ic;
    LinearLayout contentView;

    //firebase
    public static final String TAG = "TAG";
    FirebaseAuth fauth;
    FirebaseFirestore fstore;
    String userId;

    //slider
    SliderView sliderView;
    int imgCount;

    //banner
    String bannerurl1;
    ImageView banner1;

    //recycler for categories
    RecyclerView cat_rec, sub_cat_rec;
    //cat firebase
    private FirebaseRecyclerOptions<categoryModel> options;
    private FirebaseRecyclerAdapter<categoryModel, categoryViewHolder> categoryAdapter;
    private FirebaseRecyclerAdapter<categoryModel, adapter1> Adapter;
    private FirebaseRecyclerOptions<categoryModel1> options1;
    private FirebaseRecyclerAdapter<categoryModel1, categoryViewHolder1> categoryAdapter1;

    ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        fauth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        userId = fauth.getCurrentUser().getUid();

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        menu_ic = findViewById(R.id.menu);
        search_ic = findViewById(R.id.search);
        contentView = findViewById(R.id.content);

        sliderView = findViewById(R.id.imageSlider);

        cat_rec = findViewById(R.id.cat_home);
        sub_cat_rec = findViewById(R.id.sub_cat);
//        sub_cat_rec.setNestedScrollingEnabled(false);

        banner1 = findViewById(R.id.banner1);

        btncart = findViewById(R.id.cart);

        load = new ProgressDialog(homepage.this);
        load.show();
        load.setContentView(R.layout.loadprogress);
        load.setCancelable(false);
        load.setCanceledOnTouchOutside(false);

        search_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), search.class));
            }
        });

        FirebaseDatabase.getInstance().getReference("adImages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long count = snapshot.getChildrenCount();
                imgCount = count.intValue();

                sliderView.setSliderAdapter(new slideradapter(homepage.this, imgCount));
                sliderView.startAutoCycle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FirebaseDatabase.getInstance().getReference("bannerImages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bannerurl1 = snapshot.child("1").getValue().toString();
                Picasso.get().load(bannerurl1).into(banner1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        name = findViewById(R.id.name);

        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null){
                    if (documentSnapshot.exists()) {

                        name.setText(String.format("Hi, %s", documentSnapshot.getString("name")));

                    } else {
                        Log.d(TAG, "onEvent: Document doesn't exist ");
                    }
                }
            }
        });


        btncart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(homepage.this, cart.class));
            }
        });

        btncart.setCount(new database(this).getCountCart());

        cat_rec();
        subcat_rec();
        navigationDrawer();
        updateheader();

    }

    private void updateheader() {

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerview = navigationView.getHeaderView(0);

        final TextView nav_name = headerview.findViewById(R.id.menu_name);
        final TextView nav_mail = headerview.findViewById(R.id.menu_mail);

        DocumentReference documentReference = fstore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e == null){
                    if (documentSnapshot.exists()) {

                        nav_name.setText(documentSnapshot.getString("name"));
                        nav_mail.setText(documentSnapshot.getString("email"));

                    } else {
                        Log.d(TAG, "onEvent: Document doesn't exist ");
                    }
                }
            }
        });

        // Or use this
        //nav_mail.setText(fauth.getCurrentUser().getEmail());
        //nav_name.setText(fauth.getCurrentUser().getDisplayName());

    }

    private void navigationDrawer() {

        navigationView.bringToFront();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        menu_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (drawerLayout.isDrawerVisible(GravityCompat.START))
                    drawerLayout.closeDrawer(GravityCompat.START);
                else drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        animatenav();
    }

    private void animatenav() {

        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

                // Scale the View based on current slide offset
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerVisible(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_prof:
                startActivity(new Intent(getApplicationContext(), MyAccount.class));
                break;
            case R.id.nav_orders:
                startActivity(new Intent(getApplicationContext(), MyOrders.class));
                break;
            case R.id.nav_search:
                startActivity(new Intent(getApplicationContext(), search.class));
                break;
            case R.id.nav_catg:
                startActivity(new Intent(getApplicationContext(), catgPage.class));
                break;
            case R.id.nav_logout:
                fauth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void cat_rec() {
//        cat_rec.setHasFixedSize(true);
        cat_rec.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        options = new FirebaseRecyclerOptions.Builder<categoryModel>().setQuery(FirebaseDatabase.getInstance().getReference().child("Category"), categoryModel.class).build();

        categoryAdapter = new FirebaseRecyclerAdapter<categoryModel, categoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull categoryViewHolder holder, int position, @NonNull final categoryModel model) {

                holder.catName.setText(model.getName());
                Picasso.get().load(model.getImage()).into(holder.catImage);

                final categoryModel clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Intent prodpg = new Intent(homepage.this, productpg.class);
                        prodpg.putExtra("CategoryId", categoryAdapter.getRef(position).getKey());
                        prodpg.putExtra("Name", model.getName());
                        startActivity(prodpg);
                    }
                });
            }

            @NonNull
            @Override
            public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catg_card, parent, false);
                load.dismiss();
                return new categoryViewHolder(v);
            }
        };
        categoryAdapter.startListening();
        categoryAdapter.notifyDataSetChanged();
        cat_rec.setAdapter(categoryAdapter);
    }

    public void subcat_rec() {
//        sub_cat_rec.setHasFixedSize(true);
//        sub_cat_rec.setLayoutManager(new LinearLayoutManager(this));
//
//        options = new FirebaseRecyclerOptions.Builder<categoryModel>().setQuery(FirebaseDatabase.getInstance().getReference().child("SubCategories").orderByChild("CatID").equalTo("01"),categoryModel.class).build();
//
//        categoryAdapter = new FirebaseRecyclerAdapter<categoryModel, categoryViewHolder>(options) {
//            @Override
//            protected void onBindViewHolder(@NonNull categoryViewHolder holder, int position, @NonNull final categoryModel model) {
//
//                holder.catName.setText(model.getName());
////                Picasso.get().load(model.getImage()).into(holder.catImage);
//
//                final categoryModel clickItem = model;
//                holder.setItemClickListener(new ItemClickListener() {
//                    @Override
//                    public void onClick(View view, int position, boolean isLongClick) {
//                        Intent prodpg = new Intent(homepage.this, productpg.class);
//                        prodpg.putExtra("CategoryId",categoryAdapter.getRef(position).getKey());
//                        prodpg.putExtra("Name",model.getName());
//                        startActivity(prodpg);
//                    }
//                });
//            }
//
//            @NonNull
//            @Override
//            public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.catg_card,parent,false);
//                load.dismiss();
//                return new categoryViewHolder(v);
//            }
//        };
//        categoryAdapter.startListening();
//        categoryAdapter.notifyDataSetChanged();
//        sub_cat_rec.setAdapter(categoryAdapter);
        RecyclerView.LayoutManager manager;
        manager = new LinearLayoutManager(this);
        sub_cat_rec.setLayoutManager(manager);
        load.dismiss();
        options = new FirebaseRecyclerOptions.Builder<categoryModel>().setQuery(FirebaseDatabase.getInstance().getReference().child("Category"), categoryModel.class).build();
        Adapter = new FirebaseRecyclerAdapter<categoryModel, adapter1>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final adapter1 holder, int position, @NonNull categoryModel model) {
                holder.catg_name.setText(model.getName());

                FirebaseRecyclerOptions<listmodel> listoptions;
                FirebaseRecyclerAdapter<listmodel, categoryViewHolder> listAdapter;

                listoptions = new FirebaseRecyclerOptions.Builder<listmodel>().setQuery(FirebaseDatabase.getInstance().getReference().child("SubCategories").orderByChild("CatID").equalTo(categoryAdapter.getRef(position).getKey()), listmodel.class).build();

                listAdapter = new FirebaseRecyclerAdapter<listmodel, categoryViewHolder>(listoptions) {
                    @Override
                    protected void onBindViewHolder(@NonNull final categoryViewHolder holder, int position, @NonNull final listmodel model) {
                        holder.catName.setText(model.getName());
                        Picasso.get().load(model.getImage()).into(holder.catImage);

                        final listmodel clickItem = model;
                        holder.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onClick(View view, int position, boolean isLongClick) {
                                Intent prodpg = new Intent(homepage.this, productpg.class);
                                prodpg.putExtra("SubID", model.getSubID());
                                prodpg.putExtra("Name", model.getName());
                                startActivity(prodpg);
//                                Toast.makeText(catgPage.this,""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public categoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view2 = LayoutInflater.from(getBaseContext()).inflate(R.layout.subcatg_reccard, parent, false);
                        return new categoryViewHolder(view2);
                    }
                };
                listAdapter.startListening();
                listAdapter.notifyDataSetChanged();
                holder.recyclerView.setAdapter(listAdapter);
            }

            @NonNull
            @Override
            public adapter1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.subcatg_card, parent, false);
                return new adapter1(view1);
            }
        };
        Adapter.startListening();
        Adapter.notifyDataSetChanged();
        sub_cat_rec.setAdapter(Adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btncart.setCount(new database(this).getCountCart());
        if (categoryAdapter != null) {
            categoryAdapter.startListening();
            categoryAdapter.notifyDataSetChanged();
        }

    }
}