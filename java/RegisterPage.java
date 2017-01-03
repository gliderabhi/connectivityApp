package com.anurag.rebel.customerappstart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
    import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
    import org.json.JSONObject;

public class RegisterPage extends AppCompatActivity {

        private EditText rmail;
        private EditText rpass;
        private EditText rcontact;
        private Button rbtn;
    private EditText rusername;
    AlertDialog.Builder builder;
    SessionManager session;
    EditText gpsLoc;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        super.onCreateOptionsMenu(menu);
        MenuInflater blowup=getMenuInflater();
        blowup.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.AboutUs:
                Intent i= new Intent(RegisterPage.this,aboutus.class);
                startActivity(i);
                break;


        }
        return super.onOptionsItemSelected(item);
    }



    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        session=new SessionManager(getApplicationContext());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.sevis1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
            setContentView(R.layout.activity_register_page);
            builder=new AlertDialog.Builder(RegisterPage.this);
            rmail = (EditText) findViewById(R.id.rmail);
            rpass = (EditText) findViewById(R.id.rpass);
            rcontact = (EditText) findViewById(R.id.rphone);
            rusername = (EditText) findViewById(R.id.ruser);
           rmail.setText("");
           rpass.setText("");
           rcontact.setText("");
           rusername.setText("");

        rbtn = (Button) findViewById(R.id.rbtn);
        session = new SessionManager(getApplicationContext());

    }
    public static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public boolean validCellPhone(String number)
    {
        String regexStr = "^[0-9]{10}$";
        if(number.length()<10 || number.length()>13 || number.matches(regexStr)==false  ) {
            return false;
        }
        else {
            return  true;
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile =false ;

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
    int i=0;
    public void CheckEditText(View v){
        if((rmail.getText().toString()).matches("")){
            i=1;
        }
        if((rpass.getText().toString()).matches("")){
            i=1;
        }
        if((rcontact.getText().toString()).matches("")){
            i=1;
        }
        if((rusername.getText().toString()).matches("")){
            i=1;
        }

        Register();
    }



    ProgressDialog prg;
        public void Register() {

            final String email = rmail.getText().toString();
            final String password = rpass.getText().toString();
            final String number = rcontact.getText().toString();
            final String user=rusername.getText().toString();
            if (i == 0) {
                if (isValidEmail(email)) {
                    if (validCellPhone(number)) {


                        boolean network=haveNetworkConnection();
                  if(network){
                 prg=ProgressDialog.show(RegisterPage.this,"Register","Contacting server please wait ",true);

                  final Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    response = response.replaceFirst("<font>.*?</font>", "");
                    int jsonStart = response.indexOf("{");
                    int jsonEnd = response.lastIndexOf("}");

                    if (jsonStart >= 0 && jsonEnd >= 0 && jsonEnd > jsonStart) {
                        response = response.substring(jsonStart, jsonEnd + 1);
                    } else {
                        // deal with the absence of JSON content here
                    }

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {

                            session.createLoginSession(email,number,user);
                            prg.dismiss();
                            Intent intent = new Intent(RegisterPage.this, GetLocation.class);
                            startActivity(intent);
                            finish();
                        } else {
                            prg.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                            builder.setMessage("Register Failed")
                                    .setNegativeButton("Retry", null)
                                    .create()
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            RegisterRequest registerRequest = new RegisterRequest(email, password, number, user, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RegisterPage.this);
            queue.add(registerRequest);


        }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterPage.this);
                builder.setMessage("Please Check Your Network ")
                        .setNegativeButton("Retry",null)
                        .create()
                        .show();
            }

                    } else {
                        builder.setMessage("Please enter a valid number")
                                .create()
                                .show();
                    }


                } else {
                    builder.setMessage("Please enter a valid email address")
                            .create()
                            .show();
                }
            }else
            {
                builder.setMessage("Please fill all fields ")
                        .create()
                        .show();
            }

            prg.dismiss();
        }




}

