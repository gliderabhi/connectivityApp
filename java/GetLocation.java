package com.anurag.rebel.customerappstart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.R.attr.onClick;

public class GetLocation extends AppCompatActivity  {

    BroadcastReceiver broadcastReceiver;
    String token;
    LocationManager locationManager;
    String provider;
    String email;
    AlertDialog.Builder builder;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater blowup=getMenuInflater();
        blowup.inflate(R.menu.menu, menu);
        return true;
    }
    SessionManager session;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.AboutUs:
                Intent i= new Intent(GetLocation.this,aboutus.class);
                startActivity(i);
                break;
            case R.id.Logout:
                Logout();

                finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void Logout() {
        session.logoutUser();
        Intent i = new Intent(getApplicationContext(), LoginPageUser.class);
        startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session=new SessionManager(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.sevis1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_get_location);
        builder = new AlertDialog.Builder(GetLocation.this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        SessionManager sm=new SessionManager(this);
        HashMap<String, String> user = sm.getUserDetails();
          email = user.get(SessionManager.KEY_EMAIL);
        boolean network = haveNetworkConnection();
        if (network) {

            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction().endsWith(GCMRegistrationIntentService.REGISTRATION_SUCCESS)) {
                        token = intent.getStringExtra("token");

                    } else {

                    }
                }
            };

            Intent i = new Intent(this, GCMRegistrationIntentService.class);
            startService(i);
        }

    }
    @Override
    protected  void onResume(){
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                new IntentFilter(GCMRegistrationIntentService.REGISTRATION_SUCCESS));

    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }


    public void CheckValues(){
        if(lat!=null || lng!=null) {
            Intent i = new Intent(getApplicationContext(), VehicleSelect.class);
           Bundle b = new Bundle();
            b.putDouble("latitude", lat);
            b.putDouble("longitude", lng);
            i.putExtras(b);
            startActivity(i);
        }
        else{
            builder.setMessage("Please select a place to continue")
                    .create()
                    .show();
                }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public void GetGps(View v) {
        boolean network = haveNetworkConnection();
        if (!network) {

             builder.setMessage("Please check your internet connection ")
                    .create()
                    .show();


        } else {
           selectPlace();

        }

    }


ProgressDialog prg;

    public void selectPlace() {

        builder.setTitle("Gps Location")
                .setMessage("Starting place selector please wait... ")
                .create()
                .show();
        int PLACE_PICKER_REQUEST = 1;
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(GetLocation.this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }


    }

    Double lat;
    Double lng;

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == -1) {
            Place place = PlacePicker.getPlace( this,data);
            Geocoder gcd = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                lat=place.getLatLng().latitude;
                lng=place.getLatLng().longitude;
                addresses = gcd.getFromLocation(place.getLatLng().latitude, place.getLatLng().longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addresses.size() > 0) {
              lat=addresses.get(0).getLatitude();
               lng=addresses.get(0).getLongitude();
                   SendTokn();
            }
            else {
                builder.setMessage("Could not select place please try again ..")
                        .create()
                        .show();
            }
        }
    }
    public void SendTokn() {

        boolean network = haveNetworkConnection();
        if (network) {
            prg=ProgressDialog.show(GetLocation.this,"","Please wait ... ",true);
            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    response = response.replaceFirst("<font>.*?</font>", "");
                    int jsonStart = response.indexOf("{");
                    int jsonEnd = response.lastIndexOf("}");

                    if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                        response = response.substring(jsonStart, jsonEnd + 1);
                    } else {
                        builder.setMessage("Sorry no result found... Error")
                                .create()
                                .show();
                    }

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            prg.dismiss();
                            CheckValues();
                        } else {
                            prg.dismiss();
                            builder.setMessage("Token Register Fialed")
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            SendToken sendToken = new SendToken(email, token, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(sendToken);


        } else {
            builder.setMessage( "Check your internet connection")
                    .create()
                    .show();
        }

    }



}
