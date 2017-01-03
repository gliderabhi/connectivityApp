package com.anurag.rebel.customerappstart;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AbhishekSharma on 10/1/2016.
 */

public class PutNotifiationAlert extends StringRequest {
    private Map<String, String> params;
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/GCM_Scripts/CustomerSendNotification.php";



    public PutNotifiationAlert(String semail,String umail /*String contact*/,String username,Response.Listener<String> listener) {
        super(Method.POST,REGISTER_REQUEST_URL , listener, null);

        params = new HashMap<>();
        params.put("email", semail);
        params.put("umail",umail);
       // params.put("ucontact",contact);
        params.put("username",username);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}