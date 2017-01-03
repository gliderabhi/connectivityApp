package com.anurag.rebel.customerappstart;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AbhishekSharma on 9/29/2016.
 */

public class DetailsRequest extends StringRequest {
    private Map<String, String> params;

    public DetailsRequest(String email, String url, Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
        params = new HashMap<>();
        params.put("email", email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}