package com.anurag.rebel.customerappstart;


import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String REGISTER_REQUEST_URL = "http://thinkers.890m.com/customer/customerRegister.php";
    private Map<String, String> params;

    public RegisterRequest(String mail, String password,String contact, String user, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);

        params = new HashMap<>();
        params.put("mail", mail);
        params.put("username", user);
        params.put("contact", contact);
        params.put("password",password);

    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}