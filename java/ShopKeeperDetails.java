package com.anurag.rebel.customerappstart;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopKeeperDetails extends AppCompatActivity {


    String m0;
    String m1;
    String m2;
    String m3;
    String m4;
    String m5;
    String m6;
    String m7;
    String m8;
    ArrayList<String> service;
    String email;
    ListView lst;
    String shopName;
    String url;
    String contact;
    TextView mail;
    TextView cntc;
    TextView shop;
    Switch swtch;
    Button sbtn;
    String umail,ucontact,uname;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    @Override
    public void onBackPressed() {
        Intent i=new Intent (getApplicationContext(),GetLocation.class);
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
                Intent i= new Intent(ShopKeeperDetails.this,aboutus.class);
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
        setContentView(R.layout.activity_shop_keeper_details);
        SessionManager sm=new SessionManager(getApplicationContext());
        HashMap<String, String> user = sm.getUserDetails();
         umail=user.get(SessionManager.KEY_EMAIL);
         ucontact=user.get(SessionManager.KEY_CONTACT);
          uname=user.get(SessionManager.KEY_NAME);
        Bundle b=getIntent().getExtras();
        email=b.getString("Email");
        shopName=b.getString("shop");
        url=b.getString("vehicleType");
        contact=b.getString("contact");

        lst=(ListView)findViewById(R.id.ServiceList);
        swtch=(Switch)findViewById(R.id.navSwitch);
        mail=(TextView)findViewById(R.id.email);
        shop=(TextView)findViewById(R.id.Shopname);
        cntc=(TextView)findViewById(R.id.contact);
        builder=new AlertDialog.Builder(ShopKeeperDetails.this);

        sbtn=(Button)findViewById(R.id.sbtn);
        mail.setText("Email : "+email);
        shop.setText("Shopname : "+shopName);
        cntc.setText("Contact : "+contact);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                sbtn.setEnabled(false);
              ShopDetails();
            }
        }, 1000);


    }

     public  void ConfirmRequest(View v){
         if (swtch.isChecked()){
             boolean network = haveNetworkConnection();
             if (!network) {

                   builder.setMessage("Please check your internet connection ")
                         .create()
                         .show();
             }

             if (network) {
                 progressDialog=ProgressDialog.show(ShopKeeperDetails.this,"","Working on it ... Please wait .... ",true);

                       Response.Listener<String> responseListener = new Response.Listener<String>() {

                     @Override
                     public void onResponse(String response) {
                         response = response.replaceFirst("<font>.*?</font>", "");
                         int jsonStart = response.indexOf("{");
                         int jsonEnd = response.lastIndexOf("}");

                         if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                             response = response.substring(jsonStart, jsonEnd + 1);
                         } else {

                         }

                         try {
                             JSONObject jsonResponse = new JSONObject(response);
                              progressDialog.dismiss();
                                 builder.setMessage("Updated your credentials . We will notify you when the shopkeeper accepts your request ")
                                         .create()
                                         .show();


                             } catch (JSONException e) {
                             progressDialog.dismiss();
                             builder.setMessage("Failed Please try again later ")
                                     .create()
                                     .show();

                             e.printStackTrace();
                         }
                     }
                 };

                  PutNotifiationAlert putNotifiationAlert = new PutNotifiationAlert(email, umail,/*ucontact,*/uname,responseListener);
                 RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                 queue.add(putNotifiationAlert);


             }
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
    public void ShopDetails(){
        boolean network = haveNetworkConnection();
        if (!network) {

           builder.setMessage("Please check your internet connection ")
                    .create()
                    .show();
        }
        if (network) {
            progressDialog=ProgressDialog.show(ShopKeeperDetails.this,"Shop Details","Contacting server for services of shopkeeper please wait ",true);
            Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    response = response.replaceFirst("<font>.*?</font>", "");
                    int jsonStart = response.indexOf("{");
                    int jsonEnd = response.lastIndexOf("}");

                    if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                        response = response.substring(jsonStart, jsonEnd + 1);
                    } else {

                    }

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");


                        if (success) {

                            progressDialog.dismiss();
                            m0=jsonResponse.getString("m0");
                            m1=jsonResponse.getString("m1");
                            m2=jsonResponse.getString("m2");
                            m3=jsonResponse.getString("m3");
                            m4=jsonResponse.getString("m4");
                            m5=jsonResponse.getString("m5");
                            m6=jsonResponse.getString("m6");
                            m7=jsonResponse.getString("m7");
                            m8=jsonResponse.getString("m8");
                            putText();

                        } else {
                            progressDialog.dismiss();
                            builder.setMessage("Please try again.. please check your details ")
                                    .create()
                                    .show();
                            }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            DetailsRequest detailsRequest = new DetailsRequest(email, url, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(detailsRequest);

            sbtn.setEnabled(true);
        }
    }

    public void  putText(){
        service=new ArrayList<String>();
        if(url.matches( "http://thinkers.890m.com/customer/twoWheelerDetails.php")){
                if(m0.matches("1")){
                    service.add("Dismantle, clean, check Engine components & assemble");
                }
            if(m1.matches("1")){
               service.add( "Adjust clutch plate");
            }
            if(m2.matches("1")){
                service.add(  "Adjust, remove links & lubricate drive chain");
            }
            if(m3.matches("1")){
                service.add( "Repair of Auto Electrical & Electronics Systems");
            }
            if(m4.matches("1")){
                service.add( "Clean/replace air cleaner, fuel strainers and oil filters");
            }
            if(m5.matches("1")){
                service.add("Remove, clean, check, refit/replace – fuel tank, fuel pipes, fuel tap operation");
            }
            if(m6.matches("1")){
                service.add( "Replace brake components, adjust brake top-up brake fluid");
            } if(m7.matches("1")){
                service.add("Check pressure, inflate, measure tread depth,inspect for damage, do Wheel truing, Repair tyre puncture & Tuffe-up tube");
            } if(m8.matches("1")){
                service.add( "Water washing / cleaning of 2 & 3 wheelers");
            }
            ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.listlayout,service);
            lst.setAdapter(adapter);

        }else if(url.matches( "http://thinkers.890m.com/customer/fourPetroWheeler.php")){
            if(m0.matches("1")){
                service.add("Repair & Overhauling of Engine Systems(Petrol)");
            }
            if(m1.matches("1")){
                service.add( "Repair & Overhauling of Chassis System(Petrol Engine) ");
            }
            if(m2.matches("1")){
                service.add( "Repair of Auto Electrical & Electronics Systems");
            }
            if(m3.matches("1")){
                service.add("Repairing of Auto Air Conditioning System");
            }
            if(m4.matches("1")){
                service.add( "Clean / replace – air cleaner, oil filter & fuel filter");
            }
            if(m5.matches("1")){
                service.add("Apply Grease to parts / through greasing points");
            }
            if(m6.matches("1")){
                service.add( "Wheel alignment & balancing");
            } if(m7.matches("1")){
                service.add("Minor repair of Auto body");
            } if(m8.matches("1")){
                service.add( "Adjust Hand brake and replace hand brake cable");
            }
            ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.listlayout,service);
            lst.setAdapter(adapter);


        }else if(url.matches("http://thinkers.890m.com/customer/fourDieselWheeler.php")){
            if(m0.matches("1")){
                service.add("Repair & Overhauling of Engine Systems(Diesel)");
            }
            if(m1.matches("1")){
                service.add("Repair & Overhauling of Chassis System(Diesel Vehicle)");
            }
            if(m2.matches("1")){
                service.add(  "Repair of Auto Electrical & Electronics Systems");
            }
            if(m3.matches("1")){
                service.add(  "Repairing of Auto Air Conditioning System");
            }
            if(m4.matches("1")){
                service.add( "Clean / replace – air cleaner, oil filter & fuel filter");
            }
            if(m5.matches("1")){
                service.add("Apply Grease to parts / through greasing points");
            }
            if(m6.matches("1")){
                service.add( "Wheel alignment & balancing");
            } if(m7.matches("1")){
                service.add("Minor repair of Auto body");
            } if(m8.matches("1")){
                service.add( "Adjust Hand brake and replace hand brake cable");
            }
            ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.listlayout,service);
            lst.setAdapter(adapter);


        }else{
            if(m0.matches("1")){
                service.add("Repair & Overhauling of Engine Systems(Heavy Diesel)");
            }
            if(m1.matches("1")){
                service.add("Repair & Overhauling of Chassis System(Heavy  Vehicle)");
            }
            if(m2.matches("1")){
                service.add("Repair of Auto Electrical & Electronics Systems");
            }
            if(m3.matches("1")){
                service.add("Repairing of Auto Air Conditioning System");
            }
            if(m4.matches("1")){
                service.add("Clean / replace – air cleaner, oil filter & fuel filter");
            }
            if(m5.matches("1")){
                service.add( "Apply Grease to parts / through greasing points");
            }
            if(m6.matches("1")){
                service.add( "Wheel alignment & balancing");
            } if(m7.matches("1")){
                service.add("Minor repair of Auto body");
            } if(m8.matches("1")){
                service.add("Adjust Hand brake and replace hand brake cable");
            }
            ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),R.layout.listlayout,service);
            lst.setAdapter(adapter);


        }

    }
}
