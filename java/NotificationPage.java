package com.anurag.rebel.customerappstart;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NotificationPage extends AppCompatActivity {

    ArrayList<String > mail;
    ArrayList<String > ctc;
    ArrayList<String > name;
    ListView userList;
    List<String > contact;
    AlertDialog.Builder build;
    DbHelper db;
    SessionManager session;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.AboutUs:
                Intent i= new Intent(NotificationPage.this,aboutus.class);
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
        setContentView(R.layout.activity_notification_page);
        TextView lastmsg=(TextView)findViewById(R.id.notificatinMsg);

        lastmsg.setText("The  shopkeeper has accepted your request. Take your vehicle for servicing." +
                " Happy servicing ");

      /*  mail=new ArrayList<String>();
        ctc=new ArrayList<String>();
        name=new ArrayList<String>();
        prg= ProgressDialog.show(NotificationPage.this,"Please wait","",true);
       TextView getMsg=(TextView)findViewById(R.id.notificatinMsg);
         mail=db.EmailRecord();
        name=db.UsernameRecord();

        if(mail.size()==0 && contact.size()==0 && name.size()==0){
            getMsg.setText("We will notify when shopkeeper accepts ur request request ");

        }else {
            ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.listlayout, name);
            lst.setAdapter(adapter);
             }
        prg.dismiss();
*/
    }
}
