package com.anurag.rebel.customerappstart;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by AbhishekSharma on 10/3/2016.
 */

public class GCMRegistrationIntentService extends IntentService {
    public  static final String REGISTRATION_SUCCESS="RegistrationSuccess";
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public GCMRegistrationIntentService( ) {
        super("");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        registerGCM();
    }
    private  void registerGCM(){
        Intent i=null;
        String token=null;
        try{
            InstanceID instanceID=InstanceID.getInstance(getApplicationContext());
            token=instanceID.getToken(getString(R.string.gcm_defaultSenderId), GoogleCloudMessaging.INSTANCE_ID_SCOPE,null);
            Log.v("GCMIntner","token:"+token);
            i=new Intent(REGISTRATION_SUCCESS);
            i.putExtra("token",token);
        }catch (Exception e)
        {

        }
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);
    }
}
