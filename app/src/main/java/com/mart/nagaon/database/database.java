package com.mart.nagaon.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import androidx.room.Database;

import com.mart.nagaon.OrderModel;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class database extends SQLiteAssetHelper {
    private static final String DB_NAME = "nagaonmartDB.db";
    private static final int DB_VER = 1;
    public database(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    public List<OrderModel> getCarts(){
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String[] sqlSelect = {"ID", "ProdName", "ProdID", "Quantity", "Price", "Count", "Image"};
        String sqltable = "OrderDetail";

        qb.setTables(sqltable);
        Cursor c = qb.query(db,sqlSelect,null,null,null,null,null);

        final List<OrderModel> result = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                result.add(new OrderModel(
                        c.getInt(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("ProdID")),
                        c.getString(c.getColumnIndex("ProdName")),
                        c.getInt(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getInt(c.getColumnIndex("Count")),
                        c.getString(c.getColumnIndex("Image"))
                ));
            }while (c.moveToNext());
        }
        return result;
    }

    public void addtocart(OrderModel order){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO OrderDetail(ProdID,ProdName,Price,Quantity,Count,Image) VALUES('%s','%s','%s','%s','%s','%s');",
                order.getProdID(),
                order.getProdName(),
                order.getPrice(),
                order.getQuantity(),
                order.getCount(),
                order.getImage());
        db.execSQL(query);
    }

    public void cleancart(){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM OrderDetail");
        db.execSQL(query);
    }

    public void removeItem(OrderModel order){
        SQLiteDatabase db = getReadableDatabase();
        db.delete("OrderDetail","ProdName =? and Price =?", new String[]{order.getProdName(),""+order.getPrice()});
    }

    public int getCountCart() {
         int count = 0;
         SQLiteDatabase db = getReadableDatabase();
         String query = String.format("SELECT COUNT(*) FROM OrderDetail");
         Cursor cursor = db.rawQuery(query,null);
         if(cursor.moveToNext()){
             do {
                 count=cursor.getInt(0);
             }while (cursor.moveToNext());
         }
         return count;
    }

    public void updatecart(OrderModel order) {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Count=%d WHERE ID=%d",order.getCount(),order.getID());
        db.execSQL(query);
    }
}
