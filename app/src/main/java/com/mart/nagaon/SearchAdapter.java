package com.mart.nagaon;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mart.nagaon.database.database;
import com.mart.nagaon.product.prodmodel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.searchViewHolder> {
    Context context;
    ArrayList<String> prodID;
    ArrayList<String> prodname;
    ArrayList<String> prodimage;
    ArrayList<Long> proddisc;
    ArrayList<String> prodquantity;
    ArrayList<Long> prodcutprice;
    ArrayList<Long> prodprice;
    int q, cost, finalCost;

    public class searchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView prodName, prodQuantity, prodPrice, prodDisc, prodDiscount;
        public ImageView prodImage;
        public Button add;

        private ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        public searchViewHolder(@NonNull View itemView) {
            super(itemView);

            prodName = (TextView) itemView.findViewById(R.id.prod_txt);
            prodQuantity = (TextView) itemView.findViewById(R.id.quantity);
            prodPrice = (TextView) itemView.findViewById(R.id.price);
            prodDiscount = (TextView) itemView.findViewById(R.id.disc);
            prodDisc = (TextView) itemView.findViewById(R.id.cutPrice);
            prodDisc.setPaintFlags(prodDisc.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            prodImage = (ImageView) itemView.findViewById(R.id.prod_img);
            add = (Button) itemView.findViewById(R.id.addToCart);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view, getAdapterPosition(), false);
        }
    }

    public SearchAdapter(Context context, ArrayList<String> prodID) {
        this.context = context;
        this.prodID = prodID;
    }

    public SearchAdapter(Context context, ArrayList<String> prodname, ArrayList<String> prodimage, ArrayList<Long> proddisc, ArrayList<String> prodquantity, ArrayList<Long> prodcutprice, ArrayList<Long> prodprice) {
        this.context = context;
        this.prodname = prodname;
        this.prodimage = prodimage;
        this.proddisc = proddisc;
        this.prodquantity = prodquantity;
        this.prodcutprice = prodcutprice;
        this.prodprice = prodprice;
    }

    @NonNull
    @Override
    public SearchAdapter.searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.prodpage_card, parent, false);
        return new SearchAdapter.searchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final searchViewHolder holder, final int position) {
//        FirebaseDatabase.getInstance().getReference().child("Products")
//                .orderByKey().equalTo(prodID.get(position))
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                Toast.makeText(context,snapshot.child(prodID.get(position)).child("Name").getValue()+"",Toast.LENGTH_SHORT).show();
//                holder.prodName.setText(""+snapshot.child(prodID.get(position)).child("Name").getValue());
//                holder.prodPrice.setText("₹"+snapshot.child(prodID.get(position)).child("Price").getValue());
//                holder.prodQuantity.setText(""+snapshot.child(prodID.get(position)).child("Quantity").getValue());
//                Integer d = Integer.parseInt(snapshot.child(prodID.get(position)).child("Discount").getValue()+"");
//                if(d==0){
//                    holder.prodDiscount.setVisibility(View.GONE);
//                    holder.prodDisc.setVisibility(View.INVISIBLE);
//                }
//                holder.prodDiscount.setText(snapshot.child(prodID.get(position)).child("Discount").getValue()+"% OFF");
//                Picasso.get().load(snapshot.child(prodID.get(position)).child("Image").getValue().toString()).into(holder.prodImage);
//                holder.prodDisc.setText("₹"+snapshot.child(prodID.get(position)).child("MRP").getValue()+"");
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
        holder.prodName.setText(prodname.get(position));
        FirebaseDatabase.getInstance().getReference("Products").orderByChild("Name").equalTo(prodname.get(position)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot datasnapshot : snapshot.getChildren()) {
                    final String Name = datasnapshot.child("Name").getValue(String.class);
                    final String Image = datasnapshot.child("Image").getValue(String.class);
                    Long Disc = datasnapshot.child("Discount").getValue(Long.class);
                    final String Quantity = datasnapshot.child("Quantity").getValue(String.class);
                    final Long Price = datasnapshot.child("Price").getValue(Long.class);
                    Long Cutprice = datasnapshot.child("MRP").getValue(Long.class);
                    holder.prodPrice.setText("₹" + Price);
                    holder.prodQuantity.setText("" + Quantity);
                    if (Disc == 0) {
                        holder.prodDiscount.setVisibility(View.GONE);
                        holder.prodDisc.setVisibility(View.INVISIBLE);
                    }
                    holder.prodDiscount.setText(Disc + "% OFF");
                    Picasso.get().load(Image).into(holder.prodImage);
                    holder.prodDisc.setText("₹" + Cutprice);
                    holder.add.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context, R.style.SheetDialog);
                            bottomSheetDialog.setContentView(R.layout.bottom_sheet);

                            TextView botname = bottomSheetDialog.findViewById(R.id.prod_name);
                            TextView botquan = bottomSheetDialog.findViewById(R.id.quantity);
                            final TextView botprice = bottomSheetDialog.findViewById(R.id.price);
                            final TextView botdisplay = bottomSheetDialog.findViewById(R.id.display);
                            ImageView botinc = bottomSheetDialog.findViewById(R.id.increment);
                            ImageView botdec = bottomSheetDialog.findViewById(R.id.decrement);
                            final Button contshop = bottomSheetDialog.findViewById(R.id.cont);
                            final Button contshop11 = bottomSheetDialog.findViewById(R.id.cont11);
                            final Button addToCart = bottomSheetDialog.findViewById(R.id.addToCart);
                            final Button addToCart11 = bottomSheetDialog.findViewById(R.id.addToCart11);

                            botname.setText(""+Name);
                            botquan.setText("Quantity: "+Quantity);

                            q = 1;
                            cost = Integer.parseInt(Price+"");
                            finalCost = cost;

                            botprice.setText("₹"+finalCost);
                            botinc.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if(q<5) {
                                        finalCost = finalCost + cost;
                                        q++;
                                        botdisplay.setText("" + q);
                                        botprice.setText("₹" + finalCost);
                                    }else {
                                        Toast.makeText(context,"Sorry, Can't Add More Items",Toast.LENGTH_SHORT).show();
                                    }
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
                                    Toast.makeText(context,"Added to Cart",Toast.LENGTH_SHORT).show();
                                    new database(context).addtocart(new OrderModel(
                                            "XYZ",
                                            ""+Name,
                                            cost,
                                            Quantity,
                                            q,
                                            Image
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
                                    context.startActivity(new Intent(context,cart.class));
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
//                        Toast.makeText(getApplicationContext(), ""+clickItem.getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return prodname.size();
    }
}
