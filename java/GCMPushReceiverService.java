package com.anurag.rebel.customerappstart;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
public class GCMPushReceiverService extends GcmListenerService {

   /* Context ctx;
    DbHelper db;
    @Override
    public void onCreate() {
        ctx=getApplicationContext();
        db=new DbHelper(ctx);
    }*/

    @Override
    public void onMessageReceived(String s, Bundle bundle) {
        String message=bundle.getString("message");
        String title=bundle.getString("title");
         /*
        String shname=bundle.getString("username");
        String shmail=bundle.getString("umail");
        db.addRow(shname,shmail);*/
        Log.v("Message receieved","Message"+message);
        sendNotification(message,title);
    }

    private void sendNotification(String message,String title){
        Intent i =new Intent( this, NotificationPage.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        int requestCode=0;
        PendingIntent pendingIntent=PendingIntent.getActivity(this,requestCode,i,PendingIntent.FLAG_ONE_SHOT);

        Uri sound= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mbuilder= (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.sevis)
                .setContentText("My msg ")
                .setContentText(message)
                .setContentTitle(title)
                .setSound(sound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,mbuilder.build());

    }
}
