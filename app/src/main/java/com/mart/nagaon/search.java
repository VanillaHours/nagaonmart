package com.mart.nagaon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mart.nagaon.database.database;
import com.mart.nagaon.product.prodadapter;
import com.mart.nagaon.product.prodmodel;
import com.mart.nagaon.product.productpg;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class search extends AppCompatActivity {

    FirebaseRecyclerAdapter<prodmodel, prodadapter> searchadapter;
    private FirebaseRecyclerOptions<prodmodel> options;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;
    BottomSheetDialog bottomSheetDialog;
    TextView botdisplay, botprice;
    int q, cost, finalCost;
    ImageView back;

    RecyclerView recyclerView;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        back = findViewById(R.id.cat_back);
        recyclerView = findViewById(R.id.search_rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        materialSearchBar = findViewById(R.id.search_bar);
        materialSearchBar.setHint("Search Products");
        loadSuggest();
        loadAll();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search.super.onBackPressed();
            }
        });
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                List<String> suggest = new ArrayList<String>();
                for(String search:suggestList){
                    if(search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if(!enabled)
                    recyclerView.setAdapter(searchadapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
            }
        });
    }

    private void loadAll() {
        Query query = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Name");
        options = new FirebaseRecyclerOptions.Builder<prodmodel>().setQuery(query,prodmodel.class).build();
        searchadapter = new FirebaseRecyclerAdapter<prodmodel, prodadapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull prodadapter holder, int position, @NonNull final prodmodel model) {
                holder.prodName.setText(""+model.getName());
                holder.prodPrice.setText("₹"+model.getPrice());
                holder.prodQuantity.setText(""+model.getQuantity());
                if(model.getDiscount()==0){
                    holder.prodDiscount.setVisibility(View.GONE);
                    holder.prodDisc.setVisibility(View.INVISIBLE);
                }
                holder.prodDiscount.setText(model.getDiscount()+"% OFF");
                Picasso.get().load(model.getImage()).into(holder.prodImage);
                String s = model.getMRP().toString();
                holder.prodDisc.setText("₹"+s);
                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog = new BottomSheetDialog(search.this, R.style.SheetDialog);
                        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

                        TextView botname = bottomSheetDialog.findViewById(R.id.prod_name);
                        TextView botquan = bottomSheetDialog.findViewById(R.id.quantity);
                        botprice = bottomSheetDialog.findViewById(R.id.price);
                        botdisplay = bottomSheetDialog.findViewById(R.id.display);
                        ImageView botinc = bottomSheetDialog.findViewById(R.id.increment);
                        ImageView botdec = bottomSheetDialog.findViewById(R.id.decrement);
                        final Button contshop = bottomSheetDialog.findViewById(R.id.cont);
                        final Button contshop11 = bottomSheetDialog.findViewById(R.id.cont11);
                        final Button addToCart = bottomSheetDialog.findViewById(R.id.addToCart);
                        final Button addToCart11 = bottomSheetDialog.findViewById(R.id.addToCart11);

                        botname.setText(""+model.getName());
                        botquan.setText("Quantity: "+model.getQuantity());

                        q = 1;
                        cost = model.getPrice();
                        finalCost = cost;

                        botprice.setText("₹"+finalCost);
                        botinc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finalCost = finalCost + cost;
                                q++;
                                botdisplay.setText(""+q);
                                botprice.setText("₹"+finalCost);
                            }
                        });

                        botdec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(q>1){
                                    finalCost = finalCost - cost;
                                    q--;
                                    botdisplay.setText(""+q);
                                    botprice.setText("₹"+finalCost);
                                }
                            }
                        });

                        contshop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                            }
                        });

                        addToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(),"Added to Cart",Toast.LENGTH_SHORT).show();
                                new database(getBaseContext()).addtocart(new OrderModel(
                                        "XYZ",
                                        model.getName(),
                                        cost,
                                        model.getQuantity(),
                                        q,
                                        model.getImage()
                                ));
                                addToCart.setVisibility(View.GONE);
                                contshop.setVisibility(View.GONE);
                                contshop11.setVisibility(View.VISIBLE);
                                addToCart11.setVisibility(View.VISIBLE);
                            }
                        });

                        addToCart11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(search.this,cart.class));
                                bottomSheetDialog.dismiss();
                            }
                        });
                        contshop11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                            }
                        });

                        bottomSheetDialog.show();
                    }
                });

                final prodmodel clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getApplicationContext(), ""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public prodadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prodpage_card,parent,false);
                return new prodadapter(v);
            }
        };
        searchadapter.startListening();
        recyclerView.setAdapter(searchadapter);
    }

    private void startSearch(CharSequence text) {

        Query query = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("Name").equalTo(text.toString());
        options = new FirebaseRecyclerOptions.Builder<prodmodel>().setQuery(query,prodmodel.class).build();
        searchadapter = new FirebaseRecyclerAdapter<prodmodel, prodadapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull prodadapter holder, int position, @NonNull final prodmodel model) {
                holder.prodName.setText(""+model.getName());
                holder.prodPrice.setText("₹"+model.getPrice());
                holder.prodQuantity.setText(""+model.getQuantity());
                if(model.getDiscount()==0){
                    holder.prodDiscount.setVisibility(View.GONE);
                    holder.prodDisc.setVisibility(View.INVISIBLE);
                }
                holder.prodDiscount.setText(model.getDiscount()+"% OFF");
                Picasso.get().load(model.getImage()).into(holder.prodImage);
                String s = model.getMRP().toString();
                holder.prodDisc.setText("₹"+s);
                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog = new BottomSheetDialog(search.this, R.style.SheetDialog);
                        bottomSheetDialog.setContentView(R.layout.bottom_sheet);

                        TextView botname = bottomSheetDialog.findViewById(R.id.prod_name);
                        TextView botquan = bottomSheetDialog.findViewById(R.id.quantity);
                        botprice = bottomSheetDialog.findViewById(R.id.price);
                        botdisplay = bottomSheetDialog.findViewById(R.id.display);
                        ImageView botinc = bottomSheetDialog.findViewById(R.id.increment);
                        ImageView botdec = bottomSheetDialog.findViewById(R.id.decrement);
                        final Button contshop = bottomSheetDialog.findViewById(R.id.cont);
                        final Button contshop11 = bottomSheetDialog.findViewById(R.id.cont11);
                        final Button addToCart = bottomSheetDialog.findViewById(R.id.addToCart);
                        final Button addToCart11 = bottomSheetDialog.findViewById(R.id.addToCart11);

                        botname.setText(""+model.getName());
                        botquan.setText("Quantity: "+model.getQuantity());

                        q = 1;
                        cost = model.getPrice();
                        finalCost = cost;

                        botprice.setText("₹"+finalCost);
                        botinc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                finalCost = finalCost + cost;
                                q++;
                                botdisplay.setText(""+q);
                                botprice.setText("₹"+finalCost);
                            }
                        });

                        botdec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(q>1){
                                    finalCost = finalCost - cost;
                                    q--;
                                    botdisplay.setText(""+q);
                                    botprice.setText("₹"+finalCost);
                                }
                            }
                        });

                        contshop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                            }
                        });

                        addToCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getApplicationContext(),"Added to Cart",Toast.LENGTH_SHORT).show();
                                new database(getBaseContext()).addtocart(new OrderModel(
                                        "XYZ",
                                        model.getName(),
                                        cost,
                                        model.getQuantity(),
                                        q,
                                        model.getImage()
                                ));
                                addToCart.setVisibility(View.GONE);
                                contshop.setVisibility(View.GONE);
                                contshop11.setVisibility(View.VISIBLE);
                                addToCart11.setVisibility(View.VISIBLE);
                            }
                        });

                        addToCart11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(search.this,cart.class));
                                bottomSheetDialog.dismiss();
                            }
                        });
                        contshop11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                bottomSheetDialog.dismiss();
                            }
                        });

                        bottomSheetDialog.show();
                    }
                });

                final prodmodel clickItem = model;
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getApplicationContext(), ""+clickItem.getName(),Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public prodadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prodpage_card,parent,false);
                return new prodadapter(v);
            }
        };
        searchadapter.startListening();
        recyclerView.setAdapter(searchadapter);
    }

    private void loadSuggest() {
        FirebaseDatabase.getInstance().getReference("Products").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postsnapshot : snapshot.getChildren()){
                    prodmodel item = postsnapshot.getValue(prodmodel.class);
                    suggestList.add(item.getName());
                }
                materialSearchBar.setLastSuggestions(suggestList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}