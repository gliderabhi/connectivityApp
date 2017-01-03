package com.anurag.rebel.customerappstart;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SendToken extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/GCM_Scripts/GCMInsert.php";
    private Map<String, String> params;

    public SendToken(String mail, String token, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("email", mail);
        params.put("token",token);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}