package com.anurag.rebel.customerappstart;

import android.content.Intent;

import com.google.android.gms.iid.InstanceIDListenerService;

/**
 * Created by AbhishekSharma on 10/3/2016.
 */

public class GCMTokenRefreshListenerService extends InstanceIDListenerService {
    @Override
    public void onTokenRefresh() {
        Intent i=new Intent(this,GCMRegistrationIntentService.class);
        startService(i);
    }
}
