package com.anurag.rebel.customerappstart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;




public class DbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION=1;
    private static final String  DATABASE_NAME="Customers.db";
    public  static final String TABLE_NAME="Customer";
    SQLiteDatabase db;
    Cursor c;
    String mail;
    ArrayList<String> data;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table Customer " + "(id integer primary key  ,Email text ,Username text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Customer");
        onCreate(db);
    }


    public void addRow(String email,String uname){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Email",email);
        contentValues.put("Username",uname);

        db.insert("Customer",null,contentValues);
        db.close();

    }



    public void deleteRow(String mail){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DELETE  FROM  Customer WHERE Email = \""+mail + "\";");

    }

    public ArrayList<String> EmailRecord(){
        data= new ArrayList<String >();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.query(TABLE_NAME,new String[]{"Email"},null,null,null,null,null);
        mail=null;
        c.moveToFirst();
        while(c.moveToNext()){
            mail=c.getString(0);
            data.add(mail);
        }
        c.close();
        return data;
    }
    public ArrayList<String> UsernameRecord(){
        data= new ArrayList<String >();
        db = this.getReadableDatabase();
        c =db.query(TABLE_NAME,new String[]{"Username"},null,null,null,null,null);
        mail=null;
        c.moveToFirst();
        while(c.moveToNext()){
            mail=c.getString(0);
            data.add(mail);
        }
        c.close();
        return data;
    }
    public ArrayList<String> ContactRecord(){
        data = new ArrayList<String >();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c=db.query(TABLE_NAME,new String[]{"Contact"},null,null,null,null,null);
        mail=null;
        c.moveToFirst();
        while(c.moveToNext()){
            mail=c.getString(0);
            data.add(mail);
        }
        c.close();
        return data;
    }

}