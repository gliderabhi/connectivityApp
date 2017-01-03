package com.anurag.rebel.customerappstart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class UserAreaActivity extends AppCompatActivity {
   int radius;
    int vehicle;
    ListView lst;
    Double lat;
    Double lng;
    ArrayList<String> email ;
    ArrayList<String> shopname ;
    ArrayList<String> contact ;
    ArrayList<shop> shopObj;

    AlertDialog.Builder builder;

    @Override
    public void onBackPressed() {
        Intent i=new Intent(  getApplicationContext(),GetLocation.class);
        startActivity(i);
    }

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
                Intent i= new Intent(UserAreaActivity.this,aboutus.class);
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
        setContentView(R.layout.activity_user_area);
        lst = (ListView) findViewById(R.id.listView);
        Bundle b=getIntent().getExtras();
        lat=b.getDouble("latitude");
        radius=b.getInt("radius");
        vehicle=b.getInt("vehicleType");
        lng=b.getDouble("longitude");
        builder= new AlertDialog.Builder(UserAreaActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                GetShops();
            }
        }, 1000);

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
ProgressDialog progressDialog;
    public void GetShops() {
        boolean network = haveNetworkConnection();
        if (!network) {
            builder.setMessage("Please Check your network connecton ....")
                    .create()
                    .show();
                  }
        if (network) {
            progressDialog=ProgressDialog.show(UserAreaActivity.this,"Looking for shops","Contacting Server Please Wait... ",true);

            final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                     response = response.replaceFirst("<font>.*?</font>", "");
                    int jsonStart = response.indexOf("{");
                    int jsonEnd = response.lastIndexOf("}");

                    if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                        response = response.substring(jsonStart, jsonEnd + 1);
                    }
                    try {
                        shopObj=new ArrayList<shop>();
                        email  = new ArrayList<String>();
                        shopname=new ArrayList<String>();
                        contact=new ArrayList<String>();
                        JSONObject obj= new JSONObject(response);
                        JSONArray jsonArray = obj.getJSONArray("shop");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject s = jsonArray.getJSONObject(i);
                            email.add(s.getString("Email"));
                            shopname.add(s.getString("ShopName"));
                            contact.add(s.getString("Contact"));
                            shopObj.add(i, (new shop(s.getString("ShopName"),s.getString("Email"),s.getString("Contact"))));
                        }

                        if(shopObj.size()==0){
                            progressDialog.dismiss();
                            builder.setMessage("Sorry No Shops Available around the requested region... Please Try again with change of selection ")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();

                        }else {
                            ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.listlayout,shopname);
                            lst.setAdapter(adapter);
                            progressDialog.dismiss();
                            lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                                    Intent i=new Intent(getApplicationContext(),ShopKeeperDetails.class);
                                    Bundle b=new Bundle();
                                    b.putString("Email",email.get(position));
                                    b.putString("shop",shopname.get(position));
                                    urlSelector();
                                    b.putString("contact",contact.get(position));
                                    b.putString("vehicleType",url);
                                    i.putExtras(b);
                                    startActivity(i);
                                    finish();
                                    }

                                }
                            );}
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            ShopRequest shopRequest = new ShopRequest(radius,lat,lng,responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(shopRequest);
        }
    }
    String url;
    public void urlSelector(){
        if(vehicle==1){
            url= "http://thinkers.890m.com/customer/twoWheelerDetails.php";
        }else if(vehicle==2){
            url= "http://thinkers.890m.com/customer/fourPetroWheeler.php";
        }else if(vehicle==3){
            url= "http://thinkers.890m.com/customer/fourDieselWheeler.php";
        }else if(vehicle==4){
            url= "http://thinkers.890m.com/customer/fourHeavy.php";
        }
    }
}


