package com.anurag.rebel.customerappstart;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by AbhishekhKumarShamra on 9/25/2016.
 */
public class ShopRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/customer/getAll.php";
    private Map<String, String> params;

    public ShopRequest( int radius,Double lat,Double lng,Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();

        params.put("latitude", String.valueOf(lat));
        params.put("longitude", String.valueOf(lng));
        params.put("radius",String.valueOf(radius));

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}