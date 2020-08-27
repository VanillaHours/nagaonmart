package com.mart.nagaon.product;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mart.nagaon.ItemClickListener;
import com.mart.nagaon.OrderModel;
import com.mart.nagaon.database.database;
import com.mart.nagaon.cart;
import com.mart.nagaon.R;
import com.squareup.picasso.Picasso;

public class productpg extends AppCompatActivity {

    int q, cost, finalCost;
    BottomSheetDialog bottomSheetDialog;
    TextView botdisplay, botprice;

    RecyclerView cat_rec;
    //cat firebase
    private FirebaseRecyclerOptions<prodmodel> options;
    private FirebaseRecyclerAdapter<prodmodel, prodadapter> adapter;

    TextView prod_head, prod_num;
    ImageView back_img;

    String subId = "";
    String foodID = "";
    String catId = "";
    String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productpg);

        cat_rec = findViewById(R.id.prod_rec);
        back_img = findViewById(R.id.back_img);

        prod_head = findViewById(R.id.prod_txt);
        prod_num = findViewById(R.id.prodnum);

        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if (getIntent() != null) {
            subId = getIntent().getStringExtra("SubID");
            catId = getIntent().getStringExtra("CategoryId");
            name = getIntent().getStringExtra("Name");
        }
        if (subId != null && !subId.isEmpty()) {
            sub_rec();
        }
        if (catId != null && !catId.isEmpty()) {
            cat_rec();
        }
    }

    private void cat_rec() {
        cat_rec.setHasFixedSize(true);
        cat_rec.setLayoutManager(new LinearLayoutManager(this));
        prod_head.setText("All " + name);

        Query query = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("CatgID").equalTo(catId);
        options = new FirebaseRecyclerOptions.Builder<prodmodel>().setQuery(query, prodmodel.class).build();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prod_num.setText(snapshot.getChildrenCount() + " Items");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new FirebaseRecyclerAdapter<prodmodel, prodadapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final prodadapter holder, int position, @NonNull final prodmodel model) {
                holder.prodName.setText("" + model.getName());
                holder.prodPrice.setText("₹" + model.getPrice());
                holder.prodQuantity.setText("" + model.getQuantity());
                if (model.getDiscount() == 0) {
                    holder.prodDiscount.setVisibility(View.GONE);
                    holder.prodDisc.setVisibility(View.INVISIBLE);
                }
                holder.prodDiscount.setText(model.getDiscount() + "% OFF");
                Picasso.get().load(model.getImage()).into(holder.prodImage);
                String s = model.getMRP().toString();
                holder.prodDisc.setText("₹" + s);
                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog = new BottomSheetDialog(productpg.this, R.style.SheetDialog);
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

                        botname.setText("" + model.getName());
                        botquan.setText("Quantity: " + model.getQuantity());

                        q = 1;
                        cost = model.getPrice();
                        finalCost = cost;

                        botprice.setText("₹" + finalCost);
                        botinc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (q < 5) {
                                    finalCost = finalCost + cost;
                                    q++;
                                    botdisplay.setText("" + q);
                                    botprice.setText("₹" + finalCost);
                                }else {
                                    Toast.makeText(getApplicationContext(),"Sorry, Can't Add More Items",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        botdec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (q > 1) {
                                    finalCost = finalCost - cost;
                                    q--;
                                    botdisplay.setText("" + q);
                                    botprice.setText("₹" + finalCost);
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
                                Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                new database(getBaseContext()).addtocart(new OrderModel(
                                        foodID,
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
                                startActivity(new Intent(productpg.this, cart.class));
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
                        Toast.makeText(getApplicationContext(), "" + clickItem.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public prodadapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prodpage_card, parent, false);
                return new prodadapter(v);
            }
        };
        adapter.startListening();
        cat_rec.setAdapter(adapter);
    }

    public void sub_rec() {
        cat_rec.setHasFixedSize(true);
        cat_rec.setLayoutManager(new LinearLayoutManager(this));
        prod_head.setText(name);

        Query query = FirebaseDatabase.getInstance().getReference().child("Products").orderByChild("SubID").equalTo(subId);
        options = new FirebaseRecyclerOptions.Builder<prodmodel>().setQuery(query, prodmodel.class).build();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prod_num.setText(snapshot.getChildrenCount() + " Items");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        adapter = new FirebaseRecyclerAdapter<prodmodel, prodadapter>(options) {
            @Override
            protected void onBindViewHolder(@NonNull prodadapter holder, int position, @NonNull final prodmodel model) {
                holder.prodName.setText("" + model.getName());
                holder.prodPrice.setText("₹" + model.getPrice());
                holder.prodQuantity.setText("" + model.getQuantity());
                holder.prodDiscount.setText(model.getDiscount() + "% OFF");
                if (model.getDiscount() == 0) {
                    holder.prodDiscount.setVisibility(View.GONE);
                    holder.prodDisc.setVisibility(View.INVISIBLE);
                }
                Picasso.get().load(model.getImage()).into(holder.prodImage);
                final String s = model.getMRP().toString();
                holder.prodDisc.setText("₹" + s);

                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        bottomSheetDialog = new BottomSheetDialog(productpg.this, R.style.SheetDialog);
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

                        botname.setText("" + model.getName());
                        botquan.setText("" + model.getQuantity());

                        q = 1;
                        cost = model.getPrice();
                        finalCost = cost;

                        botprice.setText("" + finalCost);
                        botinc.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (q < 5) {
                                    finalCost = finalCost + cost;
                                    q++;
                                    botdisplay.setText("" + q);
                                    botprice.setText("₹" + finalCost);
                                }else {
                                    Toast.makeText(getApplicationContext(),"Sorry, Can't Add More Items",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        botdec.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (q > 1) {
                                    finalCost = finalCost - cost;
                                    q--;
                                    botdisplay.setText("" + q);
                                    botprice.setText("₹" + finalCost);
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
                                Toast.makeText(getApplicationContext(), "Added to Cart", Toast.LENGTH_SHORT).show();
                                new database(getBaseContext()).addtocart(new OrderModel(
                                        foodID,
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
                                startActivity(new Intent(productpg.this, cart.class));
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
                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prodpage_card, parent, false);
                return new prodadapter(v);
            }
        };
        adapter.startListening();
        cat_rec.setAdapter(adapter);
    }
}