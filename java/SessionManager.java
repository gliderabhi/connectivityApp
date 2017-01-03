package com.anurag.rebel.customerappstart;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SessionManager {
    SharedPreferences pref;
    Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "AndroidHivePref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAME = "shopname";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_ADDRESS="address";
     public static final String KEY_CONTACT="contact";
    public  static final String KEY_LONGITUDE="longitude";
    public  static final String KEY_LATITUDE="latitude";


    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession( String email,String contact,String username){
        editor.putBoolean(IS_LOGIN, true);
         editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_NAME, username);
        editor.putString(KEY_CONTACT, contact);

        editor.commit();
    }

    public void insertDetails(float lat,float lng){
        editor.putFloat(KEY_LATITUDE, lat);
        editor.putFloat(KEY_LONGITUDE, lng);
        editor.commit();

    }
    public void checkLogin(){
        if(!this.isLoggedIn()){
            Intent i = new Intent(_context, LoginPageUser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }

    }
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }


    public void logoutUser(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginPageUser.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }




    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

}
